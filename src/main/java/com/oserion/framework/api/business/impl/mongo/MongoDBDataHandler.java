package com.oserion.framework.api.business.impl.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.IPage;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.impl.beansDB.Template;

//@Component
@EnableMongoRepositories
public class MongoDBDataHandler implements IDataHandler {

	private final String MONGO_COLLECTION_TEMPLATE = "Template";

	private MongoDatabase database;

	public MongoDBDataHandler(IDBConnection c) {

		this.database = (MongoDatabase) c.getDatabase();
	}

	public boolean insertOrUpdateTemplate(ITemplate template ) {

		MongoCollection<Document> collectionTemplate = database.getCollection("Template");

		Map<String, Object> listTypeEtRefFixe = template.listTemplateMap(template.getListTemplateElement());
		Map<String, Object> listTypeEtRefVariable = template.listTemplateMap(template.getListVariableElement());

		Document doc = collectionTemplate.find(eq("name", template.getName())).first();

		Document nouveauDdoc = new Document("name", template.getName())
		.append("html", template.getHtml())
		.append("listTemplateElement", new Document(listTypeEtRefFixe))
		.append("listVariableElement", new Document(listTypeEtRefVariable));

		if(doc == null) { // insertion
			System.out.println("insertion du template : " + template.getName());
			collectionTemplate.insertOne(nouveauDdoc);
		} else { // modification
			System.out.println("update du template : " + template.getName());
			collectionTemplate.updateOne(doc, new Document("$set", nouveauDdoc));
		}

		insertOrUpdateManyContent(template.getListTemplateElement());
		insertOrUpdateManyContent(template.getListVariableElement());

		return false;
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
		Document doc = collectionTemplate.find(new Document("name", templateName)).first();

		// We get his URL field 
		//		Map<Integer, String> monMap = new HashMap<Integer, String>();

		boolean urlInBase = false;
		List<Document> docListUrl = (List<Document>) doc.get("listUrl");
		if(docListUrl == null) 
			docListUrl = new ArrayList<Document>();
		else 
			for(Document d : docListUrl) {
				if(d.getString("url").equalsIgnoreCase(URL)) {
					urlInBase = true;
				}
			}
		
		if(!urlInBase) { // insert URL in Template
			Document nouveauUrl = new Document("key", 5).append("url", URL );
			docListUrl.add(nouveauUrl);
			Document newDoc = new Document("name", templateName).append( "listUrl", nouveauUrl );
			collectionTemplate.updateOne(doc , new Document("$set", newDoc ));
			System.out.println("les pages ne sont pas nulls");
		} else { // URL already in Template.
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
	

	@Autowired
	private TemplateRepository repository;
	public List<ITemplate> selectTemplates(String tamplateName, boolean withUrl, boolean withElements, boolean withHtml) {
		
		Template template = repository.findByName(tamplateName);
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


}

