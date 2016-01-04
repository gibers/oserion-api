package com.oserion.framework.api.business.impl.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.IPage;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;

import org.springframework.stereotype.Component;

@Component
@EnableMongoRepositories
public class MongoDBDataHandler implements IDataHandler {

	private final String MONGO_COLLECTION_TEMPLATE = "MongoTemplate";

	private MongoDatabase database;
	private MongoOperations operations;

    @Autowired
    private ITemplificator templificator;
	
	@Autowired
	private TemplateRepository templateR;

	public MongoDBDataHandler(){
		this.database = MongoDBConnection.getInstance().getDatabase();
		this.operations = MongoDBConnection.getInstance().getOperations();
	}

	@Override
	public boolean insertOrUpdateTemplate(ITemplate template) {
		return false;
	}

	@Override
	public boolean insertTemplate(ITemplate template) {
		return false;
    }

	@Override
	public boolean updateTemplate(String templateName, String fluxHtml) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("name").is(templateName));
        MongoTemplate mongoTemplate = operations.findOne(q, MongoTemplate.class);

        if(mongoTemplate == null)
            throw new OserionDatabaseException("Can't update: the template '"+templateName+"' does not exists");

        JsoupTemplate jsoupTemplate = templificator.createTemplateFromHTML(fluxHtml, templateName);
        List<ContentElement> newlistElement = jsoupTemplate.getListTemplateElement();
        List<ContentElement> oldlistElement = mongoTemplate.getListTemplateElement();

        List<ContentElement> newlistVariableElement = jsoupTemplate.getListVariableElement();
        List<ContentElement> oldlistVariableElement = mongoTemplate.getListVariableElement();

        //filtrer(mongoTemplate.getListVariableElement());
        /*private List<ContentElement> filtrer(List<ContentElement> newlistVariableElement) {
            List<ContentElement> listCte = new ArrayList<ContentElement>();
            for (ContentElement cte : newlistVariableElement ) {
                if(cte.getRef().contains("_ref:page"))
                    listCte.add(cte);
            }
            return listCte;
        }*/


        majContentTemplateElement( mongoTemplate, newlistElement, oldlistElement );
        majContentVariableElement( mongoTemplate, newlistVariableElement, oldlistVariableElement );


        t1.setHtml(jStemplate1.getHtml());
        mongoOperation.save(t1);
		return false;
	}



    private void majContentVariableElement(MongoTemplate t1, List<ContentElement> newlistElement, List<ContentElement> oldlistElement) {
        List<ContentElement> listToUpdate = new ArrayList<>();
        List<ContentElement> listToInsert = new ArrayList<>();
        List<ContentElement> listToDelete = new ArrayList<>();
        List<ContentElement> newlistElementTraiter = new ArrayList<>();

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

        List<Integer> listKey = listKeyFromTemplateName(t1.getName());


        for( ContentElement cte : newlistElementARajouter ) {
            t1.getListVariableElement().add(cte);
            mongoOperation.insert(cte);
            mongoOperation.save(t1);
            addListVariableElement(t1, cte, listKey);
        }

        for( ContentElement cte : newlistElementASupprimer ) {
            removeVariableContentElementFromListKey(t1, cte, listKey);
//			for(int key : listKey)
//				removeContentElementToKey(t1, key);
//			mongoOperation.remove(cte);
//			t1.getListVariableElement().remove(cte);
        }

        for( ContentElement cte : newlistElementAModifier ) {
            updateVariableContentElementFromListKey(t1, cte, listKey);
        }

//			Query q2 = new Query(Criteria.where("ref").is(cte.getRef()));
//			ContentElement t2 = (ContentElement) mongoOperation.findOne(q2, ContentElement.class);
//			t2.setType(cte.getType());
//			mongoOperation.save(t1);

    }

    private void majContentTemplateElement(MongoTemplate t1, List<ContentElement> newlistElement, List<ContentElement> oldlistElement) {
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

        for( ContentElement cte : newlistElementARajouter ) {
            System.out.println("rajout de l'element => " + cte.getRef());
            operations.insert(cte);
            t1.getListTemplateElement().add(cte);
        }
        for( ContentElement cte : newlistElementASupprimer ) {
            operations.remove(cte);
            t1.getListTemplateElement().remove(cte);
        }
        for( ContentElement cte : newlistElementAModifier ) {
            Query q2 = new Query(Criteria.where("ref").is(cte.getRef()));
            ContentElement t2 = (ContentElement) operations.findOne(q2, ContentElement.class);
            t2.setType(cte.getType());
            operations.save(t2);
        }
    }

	public boolean insertOrUpdateManyContent(List<ContentElement> listElement) {
		for(ContentElement ele : listElement) {
			insertOrUpdateContent(ele);
		}
		return false;
	}

	public boolean insertOrUpdateContent(ContentElement ele) {
		MongoCollection<Document> collectionContenu = database.getCollection("Contenu");
		Document doc = new Document("ref", ele.getRef()).append("type" , ele.getType());
		Document docComplet = new Document("ref", ele.getRef()).append("type" , ele.getType()).append("value", ele.getValue());

		if(collectionContenu.find(doc).first() != null) { // update
			collectionContenu.updateMany(doc, new Document("$set", docComplet));
			System.out.println("update contenue : " + ele.getRef() + " : " + ele.getType());
		} else {   // insertion
			collectionContenu.insertOne(docComplet);
			System.out.println("insertion de contenue : " + ele.getRef() + " : " + ele.getType());
		}
		return false;
	}

	public boolean insertPageURL(String templateName, String URL) {

		MongoCollection<Document> collectionTemplate = database.getCollection(MONGO_COLLECTION_TEMPLATE);

		// we get the first template from his name.
		Document doc = collectionTemplate.find(eq("name", templateName)).first();

		// we get the URL field
		boolean urlInBase = false;
		List<Document> docListUrl = (List<Document>) doc.get("listUrl");
		if(docListUrl == null) {
			docListUrl = new ArrayList<Document>();
			docListUrl.add(new Document("key", 5).append("url", URL ));
		}
		else 
			for(Document d : docListUrl) {
				if(d.getString("url").equalsIgnoreCase(URL)) {
					docListUrl.remove(d);
					docListUrl.add(new Document("key", 5).append("url", URL ));
				}
			}
		
		if(!urlInBase) { // insert URL in MongoTemplate
			Document nouveauUrl = new Document("key", 5).append("url", URL );
			docListUrl.add(nouveauUrl);
			Document newDoc = new Document("name", templateName).append( "listUrl", nouveauUrl );
			collectionTemplate.updateOne(doc , new Document("$set", newDoc ));
			System.out.println("les pages ne sont pas nulls");
		} else { // URL already in MongoTemplate.
			System.out.println("les pages SONT nulls");
		}

		//		if(!monMap.containsValue(URL)) { // insert the URL
		//			monMap.put(5, URL);
		//			Document docWithUrl = new Document("listUrl", monMap);
		//			collectionTemplate.updateOne(doc, new Document("$set", docWithUrl));
		//		} else {
		//			System.out.println("");
		//		}


		//		Object o = database.getCollection(MONGO_COLLECTION_TEMPLATE).updateOne(
		//				new Document("name", templateName), //SELECTOR
		//				new Document("$addToSet", //ACTION
		//						new Document("listUrl", //FIELD
		//								URL))); //VALUE {id:uniqueid, url:URL}
		//
		return false;
	}

	public boolean deletePageURL(String URL) {
		return false;
	}

	public boolean deleteContent(String contentId, String contentType) {
		return false;
	}

	public boolean deleteTemplate(String templateName) {
		return false;
	}

	public String selectHTMLTemplate(String templateName) {
		MongoCollection<Document> collectionTemplate = database.getCollection(MONGO_COLLECTION_TEMPLATE);
		Document doc = collectionTemplate.find(eq("name", templateName)).first();
		return doc.getString("html");
	}

	public IPage selectFullPage(String Url) {
		return null;
	}
	

	public List<ITemplate> selectTemplates(String tamplateName, boolean withUrl, boolean withElements, boolean withHtml) {		
		MongoTemplate template = templateR.findByName(tamplateName);
		System.out.println(template.getHtml());
		return null;
	}

	public ContentElement selectContent(String contentId, String contentType) {
		return null;
	}

	public void displayContentBase() {
		MongoCollection<Document> collection = database.getCollection("collectionTest1");
		System.out.println(collection.count());
	}

	
	public void jbcSaveTemplate(String html) {
		MongoTemplate t1 = new MongoTemplate();
		t1.setName("nouveaut");
		t1.setHtml(html);
		
	}



}

