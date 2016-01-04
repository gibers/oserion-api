package com.oserion.framework.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.util.OserionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
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
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.mongo.TemplateRepository;
import com.oserion.framework.api.util.CodeReturn;


@Component
public class OserionApiFacade {

	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	@Autowired
	private IDataHandler dataHandler;

	//private OserionBuilder builder;

	public OserionApiFacade(){
	//	builder = new OserionBuilder();
	}

	public List<ITemplate> selectTemplates() {
		return ijst.selectTemplates();
	}


	public String updateTemplate(String templateName, String fluxTemplate) {
		dataHandler.updateTemplate(templateName, fluxTemplate);

		return "ok";
	}


	
	
	private void removeVariableContentElementFromListKey(MongoTemplate t1, ContentElement cte, List<Integer> listKey) {
		List<String> listRefTotal = new ArrayList<String>();
		List<ContentElement> listCteASupprimer = new ArrayList<ContentElement>();
		listRefTotal.add(cte.getRef());
		for(int i : listKey) {
			listRefTotal.add(cte.getRef().replaceFirst("ref:page", String.valueOf(i)) );
		}
		for(ContentElement localcte : t1.getListVariableElement()) {
			if(listRefTotal.contains(localcte.getRef())) {
				listCteASupprimer.add(localcte);
				mongoOperation.remove(localcte);
			}
		}
		t1.getListVariableElement().removeAll(listCteASupprimer);
		mongoOperation.save(t1);
	}


	private void updateVariableContentElementFromListKey(MongoTemplate t1, ContentElement cte, List<Integer> listKey) {
		List<String> listRefTotal = new ArrayList<String>();
		listRefTotal.add(cte.getRef());
		for(int i : listKey) {
			listRefTotal.add(cte.getRef().replaceFirst("ref:page", String.valueOf(i)) );
		}
		
		for(ContentElement localcte : t1.getListVariableElement()) {
			if(listRefTotal.contains(localcte.getRef())) {
				localcte.setType(cte.getType());
				mongoOperation.save(localcte);
			}
		}
	}
	





	public String insertTemplate(String templateName, String fluxTemplate) {

		Query q1 = new Query(Criteria.where("name").is(templateName));
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);

		if(t1 != null) 
			return CodeReturn.error21(templateName);
		
		LOG.info("insertion du template : " + templateName );
		insertListFromTemplate( fluxTemplate ,templateName );

		//			LOG.info("maj du template : " + templateName );
		//			JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		//			t1.setHtml(template1.getHtml());
		//			t1.setListTemplateElement(template1.getListTemplateElement());
		//			t1.setListVariableElement(template1.getListVariableElement());
		//			mongoOperation.insertAll(template1.getListTemplateElement());
		//			mongoOperation.insertAll(template1.getListVariableElement());
		//
		//			mongoOperation.save(t1);

		return "ok";
	}


	public String insertListFromTemplate( String fluxTemplate, String templateName ) {
		JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);

		MongoTemplate t1 = new MongoTemplate();
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
	 * A partir d'un nom de template, on renvoie une liste des pageReferences qui contiennent ce template.
	 * 
	 * @param templateName : nom du template.
	 * @return liste vide si le nom du template passé n'existe pas. 
	 */
	public List<PageReference> listPageReferenceFromTemplateName(String templateName) {
		List<PageReference> mylistPageReference = new ArrayList<PageReference>();
		Query q1 = new Query(Criteria.where("name").is(templateName));
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
		if(t1 == null) return mylistPageReference; // on retourne une liste vide, si pas de template sous ce nom.
		
		Query q2 = new Query(Criteria.where("template").is(t1));
		mylistPageReference = (List<PageReference>) mongoOperation.find(q2, PageReference.class);
		
		return mylistPageReference;
	}
	
	
	public List<Integer> listKeyFromTemplateName(String templateName) {
		List<Integer> listKey = new ArrayList<Integer>();
		List<PageReference> mylistPageReference = listPageReferenceFromTemplateName(templateName);
		for(PageReference p : mylistPageReference) {
			listKey.add(p.getKey());
		}
		return listKey;
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
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
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
	private void addContentElementToKey(MongoTemplate t1, int key) {
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
	private void removeContentElementToKey(MongoTemplate template, int key) {
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
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
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
	public String addListVariableElement(MongoTemplate t1, ContentElement cte, List<Integer> listKey) {
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
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
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
		MongoTemplate t1 = p1.getTemplate();
		String htmlOrError = ijst.construireFlux(t1, p1.getKey());

		return htmlOrError;
	}


	public String modifyContentElement(String templateName, String ref, Type type, String newValue) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
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
		MongoTemplate t1 = (MongoTemplate) mongoOperation.findOne(q1, MongoTemplate.class);
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

//	public JsoupTemplificator getIjst() {
//		return ijst;
//	}
//
//	public void setIjst(JsoupTemplificator ijst) {
//		this.ijst = ijst;
//	}

//	public IDataHandler getIdh() {
//		return idh;
//	}
//
//	public void setIdh(MongoDBDataHandler idh) {
//		this.idh = idh;
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

