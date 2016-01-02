package com.oserion.framework.oserion.api;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.util.OserionBuilder;
import com.oserion.framework.api.util.Supervision;


/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=OserionBuilder.class)
public class AppTest {

    public static final String PROPERTY_CONFIG_PATH = "oserion.config.path";
    
    private File f1 = new File("C:\\Users\\Jean-Baptiste\\Documents\\oserion\\fichierTestHtml1.html"); 
    
	@Autowired
	private Api418Facade a418f;
	
	@Autowired
	private Supervision supervision;
	
	@BeforeClass
    public static void avantTests() {
		
		// Si lancement avec mvn, on sort, car les variables systèmes sont déjà initialisées.
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
	public void testInsertOrUpdateTemplate() {
		String strTemplate = null;
		try {
			strTemplate = FileUtils.readFileToString(f1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String templateName = FilenameUtils.removeExtension( f1.getName());
  
		a418f.insertTemplate(templateName , strTemplate);
		
		System.out.println("adresse mémoire => " + a418f );
		assertTrue(true);
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
		String templateName = FilenameUtils.removeExtension( f1.getName());
		a418f.addPageUrl(templateName, "/toto/titi");
	}
	

	@Test
	public void testSupervision() {
		String templateName = FilenameUtils.removeExtension( f1.getName());
		
		supervision.listContentElementFromTemplateName(templateName);
//		supervision.listPageFromTemplateName(templateName);
	}
	
	
	
}

