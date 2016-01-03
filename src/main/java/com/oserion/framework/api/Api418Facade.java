package com.oserion.framework.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;
import com.oserion.framework.api.business.impl.mongo.TemplateRepository;
import com.oserion.framework.api.util.CodeReturn;


@Component
public class Api418Facade {

	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Autowired
	private ITemplificator ijst;

	@Autowired
	private IDataHandler idh;

	//	@Autowired
	//	public MongoTemplate mongoTemplate;

	@Autowired 
	public TemplateRepository temrepo;

	@Autowired 	
	public MongoOperations mongoOperation;


	//	@Autowired 
	//	public ContentElementRepository contentrepo;

	
	public List<ITemplate> selectTemplates() {
		return ijst.selectTemplates();
	}

	
	public Template updateTemplate(String templateName, String fluxTemplate) {
		Template t1 = idh.getTemplateFromDB(templateName);
		if(t1 == null) return null;
		
		JsoupTemplate jStemplate = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		Template tret = idh.updateTemplate(t1, jStemplate);
		
		return tret;
	}
	


	public Template insertTemplate(String templateName, String fluxTemplate) {
		if(idh.isTemplateInDB(templateName)) return null;
		
		LOG.info("insertion du template : " + templateName );
		JsoupTemplate jsoupTemplate = ijst.createTemplateFromHTML(fluxTemplate, templateName);

		Template t1 = idh.createTemplate(templateName, fluxTemplate, 
				jsoupTemplate.getListTemplateElement(), jsoupTemplate.getListVariableElement());

		return t1;
	}

	
	

	/**
	 * 
	 * Si la page URL n'existe pas, on l'insère dans la collection PageReference. On duplique les ContentElement
	 * en variabilisant les ref. 
	 * 
	 * Si l'url existe déjà, on retourne une erreur. Il convient de supprimer l'url avec la méthode removePageUrl.
	 * 
	 * @param templateName nouveau template à mettre dans la collection en cas de mise à jour.
	 * @param newUrl 
	 * @return
	 */
	public PageReference addPageUrl(String templateName, String newUrl ) {
		Template t1 = idh.getTemplateFromDB(templateName);
		if(t1 == null) return null; // template n'est pas en base.
		PageReference p1 = idh.getPageReference(newUrl);
		if(p1 != null) return null; // PageReference existe déjà.
		p1 = idh.createPageReference(newUrl, t1);
		return p1;
	}


	/**
	 * 
	 * supprime une page de la collection PageReference.
	 * Supprime également tous les contentElement qui y sont relié.
	 * 
	 * @param url
	 * @return
	 */
	public void removePageUrl(String url) {
		idh.removePageReference(url);
	}


	/**
	 * Ajoute une contentElement et le relie au template adéquat. 
	 * @param templateName
	 * @param ref
	 * @param type
	 * @param value
	 * 
	 * @return un message d'erreur si le template n'existe pas.
	 * @return null si OK.
	 * 
	 */
	public String addListTemplateElement(String templateName, String ref, Type type, String value) {
		ContentElement cte = new ContentElement(ref , type.toString() , value );

		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1==null)
			return CodeReturn.error22;
		t1.getListTemplateElement().add(cte);
		mongoOperation.save(cte);
		mongoOperation.save(t1);

		return null;
	}

	/**
	 * Ajoute autant de contentElement qu'il y a de page pour un template donné.
	 * Les contentElements ajoutés seront variabilisés avec les key qui correspondent aux pages.
	 * 
	 * @param templateName
	 * @param ref
	 * @param type
	 * @param value
	 * @return message d'erreur si le template n'existe pas . 
	 * @return message d'erreur si la ref ne contient pas le motif "ref:page".
	 * return null si OK .
	 * 
	 */
	public String addListVariableElement(Template t1, ContentElement cte, List<Integer> listKey) {
		if(t1 == null) {
			return CodeReturn.error22;
		}
		for(int key : listKey) {
			String refmodif = cte.getRef().replaceFirst("_ref:page", "_"+key);
			ContentElement ncte = new ContentElement(refmodif, cte.getType(), cte.getValue());
			mongoOperation.insert(ncte);
			t1.getListVariableElement().add(ncte);
		}
		mongoOperation.save(t1);
		//créer autant de ContentElement qu'il y a de key.

		return null;
	}

	/**
	 * Renvoie le html du template dont le nom est passé en paramètre.
	 * @param templateName
	 * @return
	 */
	public String getEmptyTemplateFromName(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) {
			return CodeReturn.error22;
		}
		return t1.getHtml();
	}

	/**
	 * A partir d'une url, renvoit le html complet. 
	 * @param url
	 * @return
	 */
	public String getFullTemplateFromName(String url) {
		Query q1 = new Query(Criteria.where("url").is(url));
		PageReference p1 = (PageReference) mongoOperation.findOne(q1, PageReference.class);
		if(p1==null) 
			return CodeReturn.error22;
		Template t1 = p1.getTemplate();
		String htmlOrError = ijst.construireFlux(t1, p1.getKey());

		return htmlOrError;
	}


	public ContentElement modifyContentElement(String templateName, String ref, String type, String newValue) {
		Template t1 = idh.getTemplateFromDB(templateName);
		if(t1 == null) return null; // pas de template en base sous ce nom.
		
		List<ContentElement> listComplete = t1.getListTemplateElement();
		listComplete.addAll(t1.getListVariableElement());
		ContentElement cte = idh.modifyValueOfContentElement(listComplete, ref, type, newValue);
		
		return cte;
	}

	/**
	 * Supprime le template passé en argument sans ses ContentElement et ContentVariable.
	 * Suprimme également les PageReferences qui contenaient ce template. 
	 * 
	 * @param templateName
	 * @return le nombre de PageReference qui ont été Supprimé de la base.
	 */
	public int removeTemplate(String templateName) {
		Template t1 = idh.getTemplateFromDB(templateName);
		if(t1 == null) return 0;
		int nbPageReference = idh.removeTemplate(t1);
		return nbPageReference;
	}

	
	public void test() {
		LOG.info("Ceci est un message qui s'affiche dans le fichier de log.");
		LOG.finest("Ceci est un message finest qui s'affiche dans le fichier de log.");
	}


//	public String getHTMLPage(String nameTemplate) {
//		return idh.selectHTMLTemplate(nameTemplate);
//	}
//
//
//	public void setContent(ContentElement e) {
//		ContentElement cte = ijst.majContenu(e);
//		idh.insertOrUpdateContent(cte);
//	}


	public TemplateRepository getTemrepo() {
		return temrepo;
	}

	public void setTemrepo(TemplateRepository temrepo) {
		this.temrepo = temrepo;
	}


	public void removeall() {

		Query q1 = new Query();

		mongoOperation.remove(q1, "template");
		mongoOperation.remove(q1, "contentElement");
		//		mongoOperation.remove(q1, "pageReference");
	}


}

