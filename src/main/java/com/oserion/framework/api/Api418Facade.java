package com.oserion.framework.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;
import com.oserion.framework.api.business.impl.mongo.TemplateRepository;
import com.oserion.framework.api.util.CodeReturn;


@Component
public class Api418Facade {
	
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Autowired
	private JsoupTemplificator ijst;

	@Autowired
	private MongoDBDataHandler idh;

	//	@Autowired
	//	public MongoTemplate mongoTemplate;

	@Autowired 
	public TemplateRepository temrepo;

	@Autowired 	
	public MongoOperations mongoOperation;


	//	@Autowired 
	//	public ContentElementRepository contentrepo;


	public void insertOrUpdateTemplate( String templateName, String fluxTemplate) {
				
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);

		if(t1 == null) {
			LOG.info("insertion du template : " + templateName );
			insertTemplate(fluxTemplate ,templateName );
		} else {
			LOG.info("maj du template : " + templateName );
			JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
			t1.setHtml(template1.getHtml());
			t1.setListTemplateElement(template1.getListTemplateElement());
			t1.setListVariableElement(template1.getListVariableElement());
			mongoOperation.insertAll(template1.getListTemplateElement());
			mongoOperation.insertAll(template1.getListVariableElement());

			mongoOperation.save(t1);
		}
	}


	public String insertTemplate( String fluxTemplate, String templateName ) {
		JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);

		Template t1 = new Template();
		t1.setName(templateName);
		t1.setHtml(fluxTemplate);

		t1.setListTemplateElement(template1.getListTemplateElement());
		t1.setListVariableElement(template1.getListVariableElement());

		mongoOperation.insertAll(template1.getListTemplateElement());
		mongoOperation.insertAll(template1.getListVariableElement());

		mongoOperation.insert(t1);

		return null;
	}

	/**
	 * 
	 * Si la page URL n'existe pas, on l'insère dans la collection PageReference. On duplique les ContentElement
	 * en variabilisant les ref. 
	 * 
	 * Si l'url existe déjà, on retourne une erreur. Il convient de supprimer l'url avec la méthode rmovePageUrl.
	 * 
	 * @param templateName nouveau template à mettre dans la collection en cas de mise à jour.
	 * @param newUrl 
	 * @return
	 */
	public String addPageUrl(String templateName, String newUrl ) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) {
			return CodeReturn.error22;
		}
		q1 = new Query(Criteria.where("url").is(newUrl));
		PageReference p1 = (PageReference) mongoOperation.findOne(q1, PageReference.class);
		if(p1==null) {
			int key = getNextKey();
			p1 = new PageReference(t1, newUrl, key);
			//mise à jour de la collection contentElement . 
			addContentElementToKey(t1, key);
			mongoOperation.insert(p1);
		} else {
			return CodeReturn.error23; 
		}
		return null;
	}


	/**
	 * 
	 * @param t1 
	 * @param key
	 */
	private void addContentElementToKey(Template t1, int key) {
		List<ContentElement> listContentVariable = t1.getListVariableElement();
		// suppression des listVariableElements qui ne contiennt pas ref:page

		List<ContentElement> newListContentElement = new ArrayList<ContentElement>();
		for (ContentElement cte : listContentVariable) {
			if(cte.getRef().contains("ref:page")) {
				String newRef = cte.getRef().replaceFirst("ref:page", String.valueOf(key));
				ContentElement ncte = new ContentElement(newRef, cte.getType(), cte.getValue());
				mongoOperation.insert(ncte);
				newListContentElement.add(ncte);
			}
		}
		t1.getListVariableElement().addAll(newListContentElement);
		mongoOperation.save(t1);
	}

	
	/**
	 * 
	 * supprime une page de la collection PageReference.
	 * Supprime également tous les contentElement qui y sont relié.
	 * 
	 * @param url
	 * @return
	 */
	public String removePageUrl(String url) {
		Query q1 = new Query(Criteria.where("url").is(url));
		PageReference p1 = (PageReference) mongoOperation.findOne(q1, PageReference.class);
		if(p1==null) 
			return CodeReturn.error22;
		//supprimmer les contentElement reliés à cette url.
		removeContentElementToKey(p1.getTemplate(), p1.getKey());
		mongoOperation.remove(p1);
		return null;
	}

	/**
	 * Appelé par removePageUrl. Lorsque l'on supprime une page, il faut supprimer tous les contentElement qui y 
	 * sont reliés. 
	 * 
	 * @param template
	 * @param key
	 */
	private void removeContentElementToKey(Template template, int key) {
		List<ContentElement> listVariableElement = template.getListVariableElement();
		List<ContentElement> listVarEleASupp = new ArrayList<ContentElement>();
		for(ContentElement cte : listVariableElement) {
			if(cte.getRef().contains("_"+key)) {
				listVarEleASupp.add(cte);
				mongoOperation.remove(cte);
			}
		}
		template.getListVariableElement().removeAll(listVarEleASupp);
		mongoOperation.save(template);
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
	public String addListVariableElement(String templateName, String ref, Type type, String value) {
		//Si pas de motif à remplacer dans ref, on sort.
		if(!ref.contains("_ref:page"))
			return CodeReturn.error24;
		//Si template n'existe pas, on sort.
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) {
			return CodeReturn.error22;
		}
		//récupérer la liste des keys utilisés par ce template. 
		q1 = new Query(Criteria.where("template").is(t1));
		List<PageReference> lp1 = (List<PageReference>) mongoOperation.find(q1, PageReference.class);
		for(PageReference p1 : lp1) {
			String refmodif = ref.replaceFirst("_ref:page", "_"+p1.getKey());
			ContentElement cte = new ContentElement(refmodif, type.name(), value);
			mongoOperation.insert(cte);
			t1.getListVariableElement().add(cte);
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
	

	public String modifyContentElement(String templateName, String ref, Type type, String newValue) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) 
			return CodeReturn.error22;
		List<ContentElement> listComplete = t1.getListTemplateElement();
		listComplete.addAll(t1.getListVariableElement());
		
//		boolean flag = false;
		for(ContentElement cte : listComplete) {
			if(cte.getType().equalsIgnoreCase(type.name()) && cte.getRef().equalsIgnoreCase(ref) ) {
				cte.setValue(newValue);
//				flag = true;
				mongoOperation.save(cte);
				return null;
			}
		}
//		if(flag)
//			return null;
		return CodeReturn.error26(ref, type.name());
	}
	
	/**
	 * Supprime le template passé en argument et ses ContentElement et ContentVariable.
	 * @param templateName
	 * @return null si OK . 
	 */
	public String removeTemplate(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) 
			return CodeReturn.error22;
		q1 = new Query(Criteria.where("template").is(templateName));
		List<PageReference> lp = (List<PageReference>) mongoOperation.find(q1, PageReference.class);
		
		List<ContentElement> listComplete = t1.getListTemplateElement();
		listComplete.addAll(t1.getListVariableElement());
		for(ContentElement cte : listComplete) 
			mongoOperation.remove(cte);
		for(PageReference lp1 : lp) 
			mongoOperation.remove(lp1);
		
		mongoOperation.remove(t1);
		return null;
	}
	
	public void test() {
		LOG.info("Ceci est un message qui s'affiche dans le fichier de log.");
		LOG.finest("Ceci est un message finest qui s'affiche dans le fichier de log.");
	}
	

	public String getHTMLPage(String nameTemplate) {
		return idh.selectHTMLTemplate(nameTemplate);
	}


	public void setContent(ContentElement e) {
		ContentElement cte = ijst.majContenu(e);
		idh.insertOrUpdateContent(cte);
	}

	public JsoupTemplificator getIjst() {
		return ijst;
	}

	public void setIjst(JsoupTemplificator ijst) {
		this.ijst = ijst;
	}

	public IDataHandler getIdh() {
		return idh;
	}

	public void setIdh(MongoDBDataHandler idh) {
		this.idh = idh;
	}

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


	public int getNextKey() {
		Aggregation agg = Aggregation.newAggregation( Aggregation.project("key"), 
				Aggregation.sort(Direction.ASC , "key" ));

		AggregationResults<PageReference> results = mongoOperation.aggregate(agg, "pageReference", PageReference.class );
		List<PageReference> listPage = results.getMappedResults();

		if( listPage.size() == 0  ) return 1;
		int firstKey = listPage.get(0).getKey();
		for(PageReference pr : listPage ) {
			if(firstKey < pr.getKey())
				return firstKey;
			firstKey++;
		}
		return firstKey;
	}



}

