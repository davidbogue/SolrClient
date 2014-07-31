package com.surmize.solrclient;

import com.surmize.solrclient.models.SolrDocument;
import java.io.IOException;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;

public class SolrClient {

    SolrServer server = SolrConnection.getInstance().getServer();
    
    public UpdateResponse addDocument(SolrDocument doc) throws SolrServerException, IOException{
        UpdateResponse resp =server.addBean(doc);
        server.commit();
        return resp;
    }
    
    public UpdateResponse addDocuments(List<SolrDocument> docs) throws SolrServerException, IOException{
        UpdateResponse resp =server.addBeans(docs);
        server.commit();
        return resp;
    }
    
    public void deleteById(String id) throws SolrServerException, IOException{
        server.deleteById(id);
        server.commit();
    }
    
    public void deleteByIds(List<String> ids) throws SolrServerException, IOException{
        server.deleteById(ids);
        server.commit();
    }
    
    public void deleteByQuery(String query) throws SolrServerException, IOException{
        server.deleteByQuery(query);
        server.commit();
    }
    
    public  <T extends SolrDocument> List<T> query(String queryString, Class<T> docClass) throws SolrServerException{
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        QueryResponse resp = server.query(query);
        List<T> documents = resp.getBeans(docClass);
        return documents;
    }
}
