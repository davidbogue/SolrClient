package com.surmize.solrclient;

import com.surmize.solrclient.models.Article;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class SolrClientTest {
    
    public SolrClientTest() {
    }

    @Test
    public void testAddQueryRemove() throws Exception {
        SolrClient client = new SolrClient();
        Article doc = new Article();
        doc.id = "TEST_ARTICLE_TO_DELETE";
        doc.author = "David Bogue";
        doc.title = Arrays.asList("This is the title");
        doc.text = "Normal article with one really strange word fasfvagaersef";
        
        client.addDocument(doc);
        
        List<Article> docsFound = client.query("fasfvagaersef", Article.class);
        assertNotNull(docsFound);
        assertTrue( docsFound.size() == 1);
        assertEquals("David Bogue", docsFound.get(0).author);
        
        client.deleteById("TEST_ARTICLE_TO_DELETE");
        docsFound = client.query("fasfvagaersef", Article.class);
        assertTrue(docsFound.size() == 0);
    }

    
}
