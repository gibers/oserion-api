package com.oserion.framework.api.business.impl.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;


public class MongoDBDataHandler implements IDataHandler {

	private MongoDatabase database ;

	public MongoDBDataHandler(String adresseMachine, int port, String nomBase) {
		MongoClient mongoClient = new MongoClient( adresseMachine , port );
		this.database = mongoClient.getDatabase(nomBase);
	}


	public void insertOrUpdateTemplate(ITemplate template ) {

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

		insertOrUpdateManyContenue(template.getListTemplateElement());
		insertOrUpdateManyContenue(template.getListVariableElement());
		
	}


	public void insertOrUpdateManyContenue(List<ContentElement> listElement) {
		MongoCollection<Document> collectionContenu = database.getCollection("Contenu");
		for(ContentElement ele : listElement) {
			insertOrUpdateContenue(ele);
		}
	}
	

	public void insertOrUpdateContenue(ContentElement ele) {
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
	}

	
	public String getTemplate(String nameTemplate) {
		MongoCollection<Document> collectionTemplate = database.getCollection("Template");
		Document doc = collectionTemplate.find(eq("name", nameTemplate)).first();
		return doc.getString("html");
	}
	

	/**
	 * DEBUG
	 */
	public void afficheContenuBase() {
		MongoCollection<Document> collection = database.getCollection("collectionTest1");
		System.out.println(collection.count());    	
	}

}

