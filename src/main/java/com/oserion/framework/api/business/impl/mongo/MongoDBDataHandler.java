package com.oserion.framework.api.business.impl.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.IPage;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.util.CodeReturn;

import org.springframework.stereotype.Component;

@Component
@EnableMongoRepositories
public class MongoDBDataHandler implements IDataHandler {

	private final String MONGO_COLLECTION_TEMPLATE = "Template";

	//	@Autowired
	//	private MongoDatabase database;
	//	@Autowired
	//	private TemplateRepository templateR;

	@Autowired 	
	public MongoOperations mongoOperation;


	public boolean isTemplateInDB(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 != null) return true;
		return false;
	}

	/**
	 * Retourne l'objet pageReference de la base. 
	 * 
	 * @param url de la pageReference que l'on recherche.
	 * @return null si l'objet n'existe pas .
	 */
	public PageReference getPageReference(String url) {
		Query q1 = new Query(Criteria.where("url").is(url));
		PageReference p1 = (PageReference) mongoOperation.findOne(q1, PageReference.class);
		return p1;
	}

	
	/**
	 * A partir d'un nom de template, on renvoie une liste des pageReferences qui contiennent ce template.
	 * 
	 * @param templateName : nom du template.
	 * @return liste vide si le nom du template passé n'existe pas. 
	 */
	public List<PageReference> getPageReferencesFromTemplateName(String templateName) {
		List<PageReference> mylistPageReference = new ArrayList<PageReference>();
		Template t1 = getTemplateFromDB(templateName);
		if(t1 == null) return mylistPageReference; // on retourne une liste vide, si pas de template sous ce nom.
		
		return getPageReferences(t1);
	}

	public List<PageReference> getPageReferences(Template t1) {
		List<PageReference> mylistPageReference = new ArrayList<PageReference>();
		Query q2 = new Query(Criteria.where("template").is(t1));
		mylistPageReference = (List<PageReference>) mongoOperation.find(q2, PageReference.class);
		
		return mylistPageReference;
	}

	/**
	 * Retourne L'objet Template de la base. 
	 * 
	 * @param nom du template recherché.
	 * @return null si l'objet n'existe pas. 
	 */
	public Template getTemplateFromDB(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		return t1;
	}

	/**
	 * Retourne l'objet contentElement de la base. 
	 * 
	 * @param cte
	 * @return null si l'objet n'existe pas.
	 * 
	 */
	private ContentElement getContentElementInDB(ContentElement cte) {
		if(cte == null) return null;
		Query q1 = new Query(Criteria.where("ref").is(cte.getRef()));
//				.andOperator(Criteria.where("type").is(cte.getType())));

		ContentElement newcte = (ContentElement) mongoOperation.findOne(q1, ContentElement.class);
		if(newcte == null) return null;
		return newcte;
	}


	public void insertListTemplateElementToTemplate(Template t1, List<ContentElement> listTemplateElement) {
		for(ContentElement cte : listTemplateElement) {
			ContentElement newcte = getContentElementInDB(cte);
			if(newcte == null) {
				mongoOperation.insert(cte);
				t1.getListTemplateElement().add(cte);
			} else {
				if(!t1.getListTemplateElement().contains(newcte))
					t1.getListTemplateElement().add(newcte);
			}
		}
		mongoOperation.save(t1);
	}

	public void insertListVariableElementToTemplate(Template t1, List<ContentElement> listTemplateElement) {
		for(ContentElement cte : listTemplateElement) {
			ContentElement newcte = getContentElementInDB(cte);
			if(newcte == null) {
				mongoOperation.save(cte);
				t1.getListVariableElement().add(cte);
			} else {
				if(!t1.getListVariableElement().contains(newcte))
					t1.getListVariableElement().add(newcte);
			}
		}
		mongoOperation.save(t1);		
	}


	public Template createTemplate(String templateName, String fluxTemplate, List<ContentElement> listTemplateElement,
			List<ContentElement> listVariableElement) {

		Template t1 = new Template();
		t1.setName(templateName);
		t1.setHtml(fluxTemplate);
		mongoOperation.insert(t1);

		insertListTemplateElementToTemplate(t1, listTemplateElement);
		insertListVariableElementToTemplate(t1, listVariableElement);

		return t1;
	}

	public PageReference createPageReference(String newUrl, Template t1) {
		int key = getNextKey();
		PageReference p1 = new PageReference(t1, newUrl, key);
		addContentElementToKey(t1, key);
		mongoOperation.insert(p1);
		return p1;
	}


	public void removePageReference(String url) {
		Query q1 = new Query(Criteria.where("url").is(url));
		PageReference p1 = (PageReference) mongoOperation.findOne(q1, PageReference.class);
		if(p1 != null) {
			removeContentElementToKey(p1.getTemplate(), p1.getKey());
			mongoOperation.remove(p1);
		}
	}


	public int removeTemplate(Template t1) {
		List<PageReference> listPageReference = getAllPageReferenceWithTemplate(t1);
		//		List<ContentElement> listComplete = t1.getListTemplateElement();
		//		listComplete.addAll(t1.getListVariableElement());
		//		for(ContentElement cte : listComplete) 
		//			mongoOperation.remove(cte);

		int nb = 0;
		for(PageReference pr1 : listPageReference) {
			WriteResult wr =  mongoOperation.remove(pr1);
			nb += wr.getN();
		}
		mongoOperation.remove(t1);
		return nb;
	}


	public Template updateTemplate(Template t1, JsoupTemplate jStemplate) {
		List<ContentElement> newlistElement = jStemplate.getListTemplateElement();
		List<ContentElement> oldlistElement = t1.getListTemplateElement();
		
		List<ContentElement> newlistVariableElement = jStemplate.getListVariableElement();
		List<ContentElement> oldlistVariableElement = filtrer(t1.getListVariableElement());
		
		majContentTemplateElement( t1, newlistElement, oldlistElement );
		majContentVariableElement( t1, newlistVariableElement, oldlistVariableElement );
		
		t1.setHtml(jStemplate.getHtml());
		mongoOperation.save(t1);
		return t1;
	}
	

	public ContentElement modifyValueOfContentElement(List<ContentElement> listComplete, String ref, String type, String newValue) {
		for(ContentElement cte : listComplete) {
			if(cte.getType().equalsIgnoreCase(type) && cte.getRef().equalsIgnoreCase(ref) ) {
				cte.setValue(newValue);
				mongoOperation.save(cte);
				return cte ;
			}
		}
		return null;
	}
	
	private void majContentTemplateElement(Template t1, List<ContentElement> newlistElement, List<ContentElement> oldlistElement) {
		List<ContentElement> newlistElementAModifier = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementARajouter = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementASupprimer = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementTraiter = new ArrayList<ContentElement>();
		
		for(ContentElement ctenew : newlistElement ) {
			boolean flag = false;
			for(ContentElement cteold : oldlistElement ) {
				if(ctenew.getRef().equalsIgnoreCase(cteold.getRef())) {
					if(!ctenew.getType().equalsIgnoreCase(cteold.getType())) {
						newlistElementAModifier.add(ctenew);
					}
					newlistElementTraiter.add(cteold);
					flag = true;
					break;
				}
			}
			if(!flag)
				newlistElementARajouter.add(ctenew);
		}
		
		for(ContentElement cteold : oldlistElement) {
			if(!newlistElementTraiter.contains(cteold))
				newlistElementASupprimer.add(cteold);
		}

		// les éléments dans la listes des éléments à rajouter qui existent déjà en BDD 
		// doivent être mis dans la liste des éléments à modifier.
		for(ContentElement cte : newlistElementARajouter) {
			ContentElement cteDB = getContentElementInDB(cte);
			if(cteDB != null) 
				newlistElementAModifier.add(cte);
		}

		// rajout des éléments.
		insertListTemplateElementToTemplate(t1, newlistElementARajouter);
		
		for( ContentElement cte : newlistElementASupprimer ) {
//			mongoOperation.remove(cte);
			t1.getListTemplateElement().remove(cte);
		}
		for( ContentElement cte : newlistElementAModifier ) {
			Query q2 = new Query(Criteria.where("ref").is(cte.getRef()));
			ContentElement t2 = (ContentElement) mongoOperation.findOne(q2, ContentElement.class);
			t2.setType(cte.getType());
			mongoOperation.save(t2);
		}
	}
	

	private void majContentVariableElement(Template t1, List<ContentElement> newlistElement, List<ContentElement> oldlistElement) {
		List<ContentElement> newlistElementAModifier = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementARajouter = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementASupprimer = new ArrayList<ContentElement>();
		List<ContentElement> newlistElementTraiter = new ArrayList<ContentElement>();
		
		for(ContentElement ctenew : newlistElement ) {
			boolean flag = false;
			for(ContentElement cteold : oldlistElement ) {
				if(ctenew.getRef().equalsIgnoreCase(cteold.getRef())) {
					if(!ctenew.getType().equalsIgnoreCase(cteold.getType())) {
						newlistElementAModifier.add(ctenew);
					}
					newlistElementTraiter.add(cteold);
					flag = true;
					break;
				}
			}
			if(!flag)
				newlistElementARajouter.add(ctenew);
		}

		for(ContentElement cteold : oldlistElement) {
			if(!newlistElementTraiter.contains(cteold))
				newlistElementASupprimer.add(cteold);
		}

		System.out.println("element à modifier ");
		for(ContentElement cte : newlistElementAModifier) {
			System.out.println(" ref : " + cte.getRef() );
			System.out.println(" type : " + cte.getType() );
			System.out.println(" value : " + cte.getValue() );
		}
		System.out.println(" ----------------------- ");
			
		System.out.println("element à rajouter ");
		for(ContentElement cte : newlistElementARajouter) {
			System.out.println(" ref : " + cte.getRef() );
			System.out.println(" type : " + cte.getType() );
			System.out.println(" value : " + cte.getValue() );
		}
		System.out.println(" ----------------------- ");

		System.out.println("element à traiter ");
		for(ContentElement cte : newlistElementTraiter) {
			System.out.println(" ref : " + cte.getRef() );
			System.out.println(" type : " + cte.getType() );
			System.out.println(" value : " + cte.getValue() );
		}
		System.out.println(" ----------------------- ");

		System.out.println("element à supprimer ");
		for(ContentElement cte : newlistElementASupprimer) {
			System.out.println(" ref : " + cte.getRef() );
			System.out.println(" type : " + cte.getType() );
			System.out.println(" value : " + cte.getValue() );
		}
		System.out.println(" ----------------------- ");

		List<Integer> listKey = listKeyFromTemplateName(t1);
		
		//Il convient de vérifier si les éléments à rajouter ne sont pas présent en base(par leurs ref).
		// Si présent, ils doivent être retiré de la liste des éléments à rajouter, pour les mettre 
		// dans la liste des éléments à modifier . 
		for(ContentElement cte : newlistElementARajouter) {
			ContentElement cteDB = getContentElementInDB(cte);
			if(cteDB != null) 
				newlistElementAModifier.add(cte);
		}
		
		// élément à rajouter.
		addContentElementToKey(t1, newlistElementARajouter, listKey);
		
		for( ContentElement cte : newlistElementASupprimer ) 
			removeVariableContentElementFromListKey(t1, cte, listKey);
		
		for( ContentElement cte : newlistElementAModifier ) {
			updateVariableContentElementFromListKey(t1, cte, listKey);
		}
			
//			Query q2 = new Query(Criteria.where("ref").is(cte.getRef()));
//			ContentElement t2 = (ContentElement) mongoOperation.findOne(q2, ContentElement.class);
//			t2.setType(cte.getType());
//			mongoOperation.save(t1);
		
	}

	
	public List<Integer> listKeyFromTemplateName(Template t1) {
		List<Integer> listKey = new ArrayList<Integer>();
		
		List<PageReference> mylistPageReference = getPageReferences(t1);
		if(mylistPageReference == null) return listKey;
		for(PageReference p : mylistPageReference) {
			listKey.add(p.getKey());
		}
		return listKey;
	}

	
	private List<ContentElement> filtrer(List<ContentElement> newlistVariableElement) {
		List<ContentElement> listCte = new ArrayList<ContentElement>();
		for (ContentElement cte : newlistVariableElement ) {
			if(cte.getRef().contains("_ref:page")) 
				listCte.add(cte);
		}
		return listCte;
	}

	private List<PageReference> getAllPageReferenceWithTemplate(Template t1) {
		Query q1 = new Query(Criteria.where("template").is(t1));
		List<PageReference> lp = (List<PageReference>) mongoOperation.find(q1, PageReference.class);
		return lp;
	}


	private void removeVariableContentElementFromListKey(Template t1, ContentElement cte, List<Integer> listKey) {
		List<String> listRefTotal = new ArrayList<String>();
		List<ContentElement> listCteASupprimer = new ArrayList<ContentElement>();
		listRefTotal.add(cte.getRef());
		for(int i : listKey) {
			listRefTotal.add(cte.getRef().replaceFirst("ref:page", String.valueOf(i)) );
		}
		for(ContentElement localcte : t1.getListVariableElement()) {
			if(listRefTotal.contains(localcte.getRef())) {
				listCteASupprimer.add(localcte);
//				mongoOperation.remove(localcte);
			}
		}
		t1.getListVariableElement().removeAll(listCteASupprimer);
		mongoOperation.save(t1);
	}
	
	
	private void updateVariableContentElementFromListKey(Template t1, ContentElement cte, List<Integer> listKey) {
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
	
	
	/**
	 * 
	 * @param t1 
	 * @param key
	 */
	private void addContentElementToKey(Template t1, int key) {
		List<ContentElement> listContentVariable = filtrer(t1.getListVariableElement());
		List<Integer> listKey = new ArrayList<Integer>();
		listKey.add(key);
		addContentElementToKey(t1,listContentVariable, listKey);
	}

	
	private void addContentElementToKey(Template t1, List<ContentElement> listContentVariable, List<Integer> listKey) {
		List<ContentElement> newListContentElement = new ArrayList<ContentElement>();
		for(int key : listKey)
			for (ContentElement cte : listContentVariable) {
				String newRef = cte.getRef().replaceFirst("ref:page", String.valueOf(key));
				ContentElement ncte = new ContentElement(newRef, cte.getType(), cte.getValue());
				newListContentElement.add(ncte);
			}
		for (ContentElement cte : listContentVariable) {
			ContentElement ncte = new ContentElement(cte.getRef(), cte.getType(), cte.getValue());
			newListContentElement.add(ncte);
		}

		insertListVariableElementToTemplate(t1, newListContentElement);
	}


	/**
	 * Appelé lorsque l'on supprime une pageReference. Il faut décrocher les contentElement 
	 * variabilisés du template. Les ContentElements ne sont pas supprimés de la base .
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
				//				mongoOperation.remove(cte);
			}
		}
		template.getListVariableElement().removeAll(listVarEleASupp);
		mongoOperation.save(template);
	}


	private int getNextKey() {
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

