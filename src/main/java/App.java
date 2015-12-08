import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.util.CentraleBean;

/**
 * Hello world!
 *
 */
public class App  {
	
	
	public static void main( String[] args ) {

//    	ApplicationContext  context = new ClassPathXmlApplicationContext("oserion-spring.xml");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CentraleBean.class);
		Api418Facade a418f = context.getBean(Api418Facade.class);
		
		
		String fluxTemplate = "<div><div id='monid1' class='bernard editable'>ceci est du <p>texte</p> </div><div id='monid2_ref:page' class='bernard editable'>ceci est un autre <p>texte</p> </div></div>";
		String templateName = "premierTemplate";

		
		a418f.uploadTemplateFromHtml(fluxTemplate, templateName);
		
		String retourTemplate = a418f.getHTMLPage(templateName);
		System.out.println("retourTemplate => " + retourTemplate);

//		a418f.setContent("monid11", ContentElement.Type.EDITABLE.name(), "nouveau contenu");
		
	}
	
}


