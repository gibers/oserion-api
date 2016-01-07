package com.oserion.framework.api.business.impl.mongo;


import java.util.ArrayList;
import java.util.List;

import com.oserion.framework.api.business.beans.PageReference;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.oserion.framework.api.business.*;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.oserion.framework.api.business.beans.ContentElement;


@EnableMongoRepositories
public class MongoDBDataHandler implements IDataHandler {

    @Autowired
    private ITemplificator templificator;

    @Autowired
    private IDBConnection connection;
    private MongoOperations operations;

    public MongoDBDataHandler(IDBConnection connection) throws OserionDatabaseException {
        setConnection(connection);
    }

    @Override
    public boolean insertOrUpdateTemplate(String templateName, String fluxHtml) {
        return false;
    }

    @Override
    public void insertTemplate(String name, String html) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("name").is(name));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);

        if (t != null)
            throw new OserionDatabaseException(
                    String.format("The template %s already exists.", name));

        t = new MongoTemplate(templificator.createTemplateFromHTML(name, html));

        operations.insert(t);
    }

    @Override
    public List<ITemplate> selectTemplates() throws OserionDatabaseException {
        try {
            return (List<ITemplate>)(List<?>) operations.findAll(MongoTemplate.class);
        } catch (Exception e) {
            throw new OserionDatabaseException("An error happen while retrieving templates");
        }
    }

    @Override
    public void insertPageUrl(String templateName, String newUrl ) throws OserionDatabaseException {
        Query q = new Query(Criteria.where("listPage").elemMatch(Criteria.where("url").is(newUrl)));
        MongoTemplate t = operations.findOne(q, MongoTemplate.class);
        if(t != null)
            throw new OserionDatabaseException(
                    String.format("The url %s already exists.", newUrl));

        q = new Query(Criteria.where("name").is(templateName));
        t = operations.findOne(q, MongoTemplate.class);
        if(t == null)
            throw new OserionDatabaseException(
                    String.format("The template %s does not exists.", templateName));

        List<PageReference> l = t.getListPage() == null ? new ArrayList<>() : t.getListPage();
        l.add(new PageReference(newUrl));
        t.setListPage(l);
        operations.save(t);
    }

    @Override
    public void updateTemplate(ITemplate template) throws OserionDatabaseException {
        MongoTemplate t;
        if(template instanceof MongoTemplate && ((MongoTemplate) template).getId() != null){
            t = (MongoTemplate) template;
        }
        else{
            Query q = new Query(Criteria.where("name").is(template.getName()));
            t = operations.findOne(q, MongoTemplate.class);
            if (t == null)
                throw new OserionDatabaseException(
                        String.format("The template %s does not exists.", template.getName()));
            t.setHtml(template.getHtml());
            t.setListTemplateElement(template.getListTemplateElement());
            t.setListVariableElement(template.getListTemplateElement());
            t.setListPage(template.getListPage());
        }

        operations.save(t);
    }

    @Override
    public boolean insertOrUpdateManyContent(List<ContentElement> listElement) {
        return false;
    }

    @Override
    public boolean insertOrUpdateContent(ContentElement ele) {
        return false;
    }

    @Override
    public boolean insertPageURL(String templateName, String URL) {
        return false;
    }

    @Override
    public boolean deletePageURL(String URL) {
        return false;
    }

    @Override
    public boolean deleteContent(String contentId, String contentType) {
        return false;
    }

    @Override
    public boolean deleteTemplate(String templateName) {
        return false;
    }

    @Override
    public String selectHTMLTemplate(String templateName) {
        return null;
    }

    @Override
    public IPage selectFullPage(String Url) {
        return null;
    }

    @Override
    public ContentElement selectContent(String contentId, String contentType) {
        return null;
    }

    @Override
    public void displayContentBase() {

    }

    public ITemplificator getTemplificator() {
        return templificator;
    }

    public void setTemplificator(ITemplificator templificator) {
        this.templificator = templificator;
    }

    public IDBConnection getConnection() {
        return connection;
    }

    public void setConnection(IDBConnection connection) throws OserionDatabaseException {
        this.connection = connection;
        if (this.connection instanceof MongoDBConnection)
            this.operations = ((MongoDBConnection) this.connection).getOperations();
        else
            throw new OserionDatabaseException("Invalid Connection to MongoDB");
    }

}

