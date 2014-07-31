package com.surmize.solrclient.models;

import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

// Reference http://svn.apache.org/viewvc/lucene/dev/trunk/solr/example/solr/collection1/conf/schema.xml?view=markup for valid field names
public class Article implements SolrDocument{

    @Field
    public String id;
    
    @Field
    public String author;
    
    @Field
    public List<String> title;
    
    @Field
    public String text;
    
}
