import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.configurationbeans.CentraleBean;
import com.oserion.framework.api.implementation.Api418Facade;

/**
 * Hello world!
 *
 */
public class App  {
	
	public static void main( String[] args ) {

//    	ApplicationContext  context = new ClassPathXmlApplicationContext("oserion-spring.xml");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CentraleBean.class);
		Api418Facade a418f = context.getBean(Api418Facade.class);
		
		String fluxTemplate = "<div><div id='monid1' class='bernard editable'>ceci est du texte</div><div id='monid2_ref:page' class='bernard'>ceci est un autre texte</div></div>";
		String templateName = "premierTemplate";

		a418f.uploadTemplateFromHtml(fluxTemplate, templateName);
		

	}
}


