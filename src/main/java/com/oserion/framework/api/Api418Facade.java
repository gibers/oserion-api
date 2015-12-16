package com.oserion.framework.api;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElementRepository;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;
import com.oserion.framework.api.business.impl.mongo.TemplateRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class Api418Facade {

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

	
	public String insertTemplate( String fluxTemplate, String templateName ) {
		JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);

		List<PageReference> listReference = new ArrayList<PageReference>();
		listReference.add(new PageReference(1, "toto"));
		listReference.add(new PageReference(2, "admin"));
//		listReference.add(new PageReference(2, "bernard"));
		
		Template t1 = new Template();
		t1.setName(templateName);
		t1.setHtml(fluxTemplate);
		t1.setListTemplateElement(template1.getListTemplateElement());
		t1.setListVariableElement(template1.getListVariableElement());
//		t1.setListReference(listReference);

		mongoOperation.insertAll(template1.getListTemplateElement());
		mongoOperation.insertAll(template1.getListVariableElement());
//		mongoOperation.insertAll(listReference);
		
		mongoOperation.insert(t1);

		return null;
	}

	
	public String addListTemplateElement(String templateName) {
		ContentElement cte = new ContentElement("test1" , ContentElement.Type.EDITABLE.toString() , "contenue de la balise" );
		mongoOperation.save(cte);
		
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);

		t1.getListTemplateElement().add(cte);
		mongoOperation.save(t1);

		
		
		System.out.println(" requête trouvée => " + t1.toString());
		return null;
	}
	

	public String uploadTemplateFromHtml( String fluxTemplate, String templateName ) {
		JsoupTemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);

		Template t1 = new Template();
		t1.setName("premierTemplate");
		t1.setHtml(fluxTemplate);
		
		
		temrepo.insert(t1);
//		idh.insertOrUpdateTemplate(template1);
		
//		idh.displayContentBase();
		return templateName;
	}

	public String saveTemplate(MongoOperations mongoOperation) {
		
//		mongoOperation.save();
		
		return null;
	}

	public String getHTMLPage(String nameTemplate) {
		return idh.selectHTMLTemplate(nameTemplate);
	}


	public void setContent(ContentElement e) {
		ContentElement cte = ijst.majContenu(e);
		idh.insertOrUpdateContent(cte);
	}

	public boolean addPageUrl(String templateName, String url){
		return idh.insertPageURL(templateName, url);
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
		mongoOperation.remove(q1, "pageReference");
	}
	
}

