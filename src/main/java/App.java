import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.util.OserionBuilder;

/**
 * Hello world!
 *
 */
public class App  {
	
    public static final String PROPERTY_DB_CONNECTION = "database.connection";
    public static final String PROPERTY_CONFIG_PATH = "oserion.config.path";
	
	public static void main( String[] args ) throws IOException {

//    	ApplicationContext  context = new ClassPathXmlApplicationContext("oserion-spring.xml");

		FileInputStream configFile = new FileInputStream(System.getProperty(App.PROPERTY_CONFIG_PATH));
		System.getProperties().load(configFile);

		System.out.println(System.getProperty("database.host"));
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OserionBuilder.class);
		
		Api418Facade a418f = context.getBean(Api418Facade.class);
		System.out.println(a418f.getIdh().toString());
		System.out.println(a418f.getIjst().toString());
				
//		MongoOperations mongoOperation = (MongoOperations) context.getBean("mongoTemplate");

		String fluxTemplate = "<div> bobo "
				+ "<div id='monid1' class='bernard editable'>ceci est du <p>texte</p> </div>"
				+ "<div id='monid2_ref:page' class='bernard editable'>ceci est un autre <p>texte</p> </div>"
				+ "</div>";

		
		
//		a418f.temrepo.deleteAll();
		
		a418f.removeall();
		a418f.insertTemplate(fluxTemplate, "premierTemplate1");
		
		
//		a418f.addListTemplateElement("premierTemplate1");

		
		
		System.exit(0);

		Template t1 = new Template();
		t1.setName("premierTemplate");
		t1.setHtml(fluxTemplate);

		a418f.temrepo.insert(t1);
		
//		mongoOperation.insert(t1);
		
		Query q1 = new Query(Criteria.where("name").is("premierTemplate"));
		Query q2 = new Query();
//		System.out.println("nb de template q1 => " + mongoOperation.count(q1, Template.class));
//		System.out.println("nb de template q2 => " + mongoOperation.count(q2, Template.class));
		
		
		Template t2 = a418f.temrepo.findByName("premierTemplate");
		System.out.println("t2 => " + t2);
		System.out.println("t2.id => " + t2.getId() );
		
		
		Template t21 = a418f.temrepo.findByName("premierTemplate");
		System.out.println("t21 => " + t21);
		System.out.println("t21.id => " + t21.getId() );

		
		
		Template t3 = a418f.temrepo.findByName("bernard");
		System.out.println("t3 => " + t3);
				
		
//		IDBConnection idbc = new OserionBuilder().buildDBConnection();
       /* Api418Facade facade = new Api418Facade(idbc);
        
		
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


