import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.business.IDBConnection;
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

//		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OserionBuilder.class);
		
//		Api418Facade a418f = context.getBean(Api418Facade.class);
		
		FileInputStream configFile = new FileInputStream(System.getProperty(App.PROPERTY_CONFIG_PATH));
		System.getProperties().load(configFile);
		
		IDBConnection idbc = new OserionBuilder().buildDBConnection();
        Api418Facade facade = new Api418Facade(idbc);
        
		
		String fluxTemplate = "<div><div id='monid1' class='bernard editable'>ceci est du <p>texte</p> </div><div id='monid2_ref:page' class='bernard editable'>ceci est un autre <p>texte</p> </div></div>";
		String templateName = "premierTemplate";
		
		facade.uploadTemplateFromHtml(fluxTemplate, templateName);
		
//		facade.uploadTemplateFromHtml(fluxTemplate, templateName);
		
		String retourTemplate = facade.getHTMLPage(templateName);
		System.out.println("retourTemplate => " + retourTemplate);

//		a418f.setContent("monid11", ContentElement.Type.EDITABLE.name(), "nouveau contenu");
		
	}
	
}


