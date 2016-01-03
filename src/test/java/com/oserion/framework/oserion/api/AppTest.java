package com.oserion.framework.oserion.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import com.oserion.framework.api.business.IPageReference;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.business.IPageReference;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.util.CodeReturn;
import com.oserion.framework.api.util.OserionBuilder;
import com.oserion.framework.api.util.Supervision;


/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=OserionBuilder.class)
public class AppTest {

	public static final String PROPERTY_CONFIG_PATH = "oserion.config.path";

	private File f1 = new File("C:\\Users\\Jean-Baptiste\\Documents\\oserion\\fichierTestHtml4.html"); 

	@Autowired
	private Api418Facade a418f;

	@Autowired
	private Supervision supervision;

	@BeforeClass
	public static void avantTests() {
		// Si lancement avec mvn, on sort, car les variables systèmes sont déjà initialisées par surefire.
		if(System.getProperty("skipTests") != null) return;
		System.out.println("------------------------");
		System.out.println("Avant Tests");
		System.out.println("------------------------");
		FileInputStream configFile;
		try {
			configFile = new FileInputStream(System.getProperty(AppTest.PROPERTY_CONFIG_PATH));
			System.getProperties().load(configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println(" ===> " + System.getProperty("database.host"));
	}

	//	@Test
	//	public void testInsertOrUpdateTemplate() {
	//		String strTemplate = null;
	//		try {
	//			strTemplate = FileUtils.readFileToString(f1);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		String templateName = FilenameUtils.removeExtension( f1.getName());
	//  
	//		a418f.insertTemplate(templateName , strTemplate);
	//		
	//		System.out.println("adresse mémoire => " + a418f );
	//		assertTrue(true);
	//	}

	
//	@Test
	public void insertTemplate() {
		String strTemplate = null;
		try {
			strTemplate = FileUtils.readFileToString(f1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String templateName = FilenameUtils.removeExtension( f1.getName());
		Template t1 = a418f.insertTemplate(templateName, strTemplate);
		
		assertEquals( templateName , t1.getName() );
	}

	
	//	@Test 
	public void updateTemplate () {
		String strTemplate = null;
		try {
			strTemplate = FileUtils.readFileToString(f1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String templateName = FilenameUtils.removeExtension( f1.getName());
		a418f.updateTemplate(templateName , strTemplate);
	}


//	@Test
	public void addPageUrl() {
		String templateName = FilenameUtils.removeExtension( f1.getName() );
//		a418f.removePageUrl("bertrand1");
		
		PageReference p1 = a418f.addPageUrl(templateName, "bertrand1");
		System.out.println( p1.getUrl() );
	}
	
//	@Test
	public void modifyContentElement() {
		String templateName = FilenameUtils.removeExtension( f1.getName() );
		ContentElement cte = a418f.modifyContentElement(templateName, "idjeromee_ref:page" , "editable" , "mon nouveau value ");
		System.out.println(cte.getRef());
	}

//	@Test
 	public void selectTemplate() {
		List<ITemplate> listITemplate = a418f.selectTemplates();
		for( ITemplate it : listITemplate) {
			System.out.println("nom TEMPLATE : " + it.getName());

			if(it.getListPage() != null) {
				for(IPageReference p1 : it.getListPage()) {
					if(it.getListPage() != null)
						if( p1 instanceof PageReference ) {
							System.out.println("p1 est une pageReference");
							((PageReference) p1).getTemplate();
							System.out.println(" ** URL : " + p1.getUrl() );
							System.out.println(" ** KEY : " + p1.getKey() );
						}
				}
			}
		}
	}

	@Test
	public void testSupervision() {
		// idjeromee_ref:page
		String templateName = FilenameUtils.removeExtension( f1.getName());
		
//		insertTemplate();
		
//		a418f.addPageUrl(templateName, "bobo/titi");
//		a418f.addPageUrl(templateName, "bertrand1");
		
		assertTrue(a418f.removeTemplate(templateName) == 2);

		supervision.listContentElementFromTemplateName(templateName);
//		supervision.listPageFromTemplateName(templateName);
	}



}

