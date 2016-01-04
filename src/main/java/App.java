import java.io.FileInputStream;
import java.io.IOException;

import com.oserion.framework.api.OserionApiFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.util.MyLogger;
import com.oserion.framework.api.util.OserionBuilder;
import com.oserion.framework.api.util.Supervision;

/**
 * Hello world!
 *
 */
public class App  {
	
    public static final String PROPERTY_DB_CONNECTION = "database.connection";
    public static final String PROPERTY_CONFIG_PATH = "oserion.config.path";
	
	public static void main( String[] args ) throws IOException {

//    	ApplicationContext  context = new ClassPathXmlApplicationContext("oserion-spring.xml");

		MyLogger.setup();

		FileInputStream configFile = new FileInputStream(System.getProperty(App.PROPERTY_CONFIG_PATH));
		System.getProperties().load(configFile);

		System.out.println(System.getProperty("database.host"));
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OserionBuilder.class);
		
		OserionApiFacade a418f = context.getBean(OserionApiFacade.class);
		Supervision supervision = context.getBean(Supervision.class);
		
				
//		MongoOperations mongoOperation = (MongoOperations) context.getBean("mongoTemplate");

		String fluxTemplate = "<div> bobo "
				+ "<div id='monid1' class='bernard editable'>ceci est du <p>texte</p> </div>"
				+ "<div id='monid2_ref:page' class='bernard editable'>ceci est un autre <p>texte</p> </div>"
				+ "</div>";

//		a418f.test();
		
//		a418f.temrepo.deleteAll();
		
//		a418f.removeall(); OK
//		a418f.insertTemplate(fluxTemplate, "premierTemplate2"); 		 OK
//		a418f.addPageUrl("premierTemplate2", "/bernard/toto"); 
		
//		a418f.removePageUrl("/bernard/toto6");

//		a418f.insertOrUpdateTemplate("premierTemplate1", fluxTemplate);
		
//		a418f.addPageUrl("premierTemplate1", "/bernard/toto8");
		
//		int nb = a418f.test();
		
//		a418f.addListTemplateElement("premierTemplate1", "monId", Type.EDITABLE, "<div> une balise aléatoire </div>" );
//		a418f.addListVariableElement("premierTemplate1", "idd_ref:page", Type.EDITABLE, "<div> une balise aléatoire </div>" );

//		a418f.getEmptyTemplateFromName("premierTemplate1");
		
//		System.out.println(a418f.modifyContentElement("premierTemplate1", "monid1", Type.EDITABLE, "turlute"));

//		a418f.removeTemplate("fichierTestHtml1");
		
//		supervision.listContentElementFromTemplateName("fichierTestHtml1");
		supervision.listPageFromTemplateName("fichierTestHtml1");
		
		
//		System.out.println(a418f.getFullTemplateFromName("/bernard/toto8"));
		

		System.out.println(" -- FIN -- ");
		System.exit(0);

		MongoTemplate t1 = new MongoTemplate();
		t1.setName("premierTemplate");
		t1.setHtml(fluxTemplate);

		a418f.temrepo.insert(t1);
		
//		mongoOperation.insert(t1);
		
		Query q1 = new Query(Criteria.where("name").is("premierTemplate"));
		Query q2 = new Query();
//		System.out.println("nb de template q1 => " + mongoOperation.count(q1, MongoTemplate.class));
//		System.out.println("nb de template q2 => " + mongoOperation.count(q2, MongoTemplate.class));
		
		
		MongoTemplate t2 = a418f.temrepo.findByName("premierTemplate");
		System.out.println("t2 => " + t2);
		System.out.println("t2.id => " + t2.getId() );
		
		
		MongoTemplate t21 = a418f.temrepo.findByName("premierTemplate");
		System.out.println("t21 => " + t21);
		System.out.println("t21.id => " + t21.getId() );

		
		
		MongoTemplate t3 = a418f.temrepo.findByName("bernard");
		System.out.println("t3 => " + t3);
				
		
//		IDBConnection idbc = new OserionBuilder().buildDBConnection();
       /* OserionApiFacade facade = new OserionApiFacade(idbc);
        
		
		String fluxTemplate = "<div><div id='monid1' class='bernard editable'>ceci est du <p>texte</p> </div><div id='monid2_ref:page' class='bernard editable'>ceci est un autre <p>texte</p> </div></div>";
		String templateName = "premierTemplate";
		
//		facade.uploadTemplateFromHtml(fluxTemplate, templateName);
		
		facade.selectTemplate("premierTemplate");
		
//		facade.addPageUrl(templateName, "tototo1");
		
//		String retourTemplate = facade.getHTMLPage(templateName);
//		System.out.println("retourTemplate => " + retourTemplate);

//		a418f.setContent("monid11", ContentElement.Type.EDITABLE.name(), "nouveau contenu");
		*/
	}
	
}


