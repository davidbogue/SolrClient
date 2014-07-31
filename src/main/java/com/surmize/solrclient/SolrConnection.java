package com.surmize.solrclient;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrConnection {
    
    private SolrServer server = null;
    
    private SolrConnection() {
    }
    
    public static SolrConnection getInstance() {
        return SolrConnectionHolder.INSTANCE;
    }
    
    public SolrServer getServer(){
        return getServerInstance();
    }
    
    synchronized private SolrServer getServerInstance(){
        if(server == null){
            String url = PropertyManager.getSetting("url");
            server = new HttpSolrServer( url ); 
        }
        return server;
    }
    
    private static class SolrConnectionHolder {

        private static final SolrConnection INSTANCE = new SolrConnection();
    }
}
