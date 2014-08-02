package com.surmize.solrclient;

import com.surmize.solrclient.models.Article;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.solr.client.solrj.SolrServerException;

public class SolrClientCLI {

    private final String CAMEL_CASE_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private final SolrClient solr = new SolrClient();
    
    public static void main(String[] parameters) {
        CommandLine commandLine;
        Option option_D = OptionBuilder.withArgName("dir").hasArg().withDescription("Directory to index").create("D");
        Option option_E = OptionBuilder.withArgName("extension").hasArg().withDescription("index files with this extension").create("E");
        Option option_r = new Option("r", "Recursively index sub directories");
        Options options = new Options();
        CommandLineParser parser = new GnuParser();

        options.addOption(option_D);
        options.addOption(option_E);
        options.addOption(option_r);

        try {
            commandLine = parser.parse(options, parameters);
            String directory = commandLine.getOptionValue("D");
            String ext = commandLine.getOptionValue("E");
            boolean recursive = commandLine.hasOption("r");

            if (directory == null || ext == null) {
                System.out.println("You must supply a directory and an extension");
            } else {
                SolrClientCLI cli = new SolrClientCLI();
                cli.indexDirectory(new File(directory), ext, recursive);
            }
        } catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
        } catch (IOException | SolrServerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void indexDirectory(File node, String fileExtension, boolean recursive) throws IOException, SolrServerException {
        System.out.println(node.getAbsoluteFile());
        
        if (node.isDirectory()) {
            if (recursive) {
                String[] subNote = node.list();
                for (String filename : subNote) {
                    indexDirectory(new File(node, filename), fileExtension, recursive);
                }
            }
        } else if (isValidExtension(fileExtension, getExtension(node.getName()))) {
            System.out.println("Indexing: " + node.getName());
            String author = camelCaseToSpaces(node.getParentFile().getName());
            String text = readFile(node, Charset.defaultCharset());
            Article a = new Article();
            a.id = UUID.randomUUID().toString();
            a.author = author;
            a.text = text;
            solr.addDocument(a);
        }
    }

    private String camelCaseToSpaces(String camelString) {
        String[] strings = camelString.split(CAMEL_CASE_REGEX);
        StringBuilder builder = new StringBuilder("");
        if (strings != null) {
            for (String string : strings) {
                builder.append(string).append(" ");
            }
            if (builder.length() > 1) {
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        return builder.toString();
    }

    private String readFile(File node, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(node.toPath());
        return new String(encoded, encoding);
    }

    private boolean isValidExtension(String validExt, String fileExt) {
        if (validExt == null || validExt.equals("")) {
            // if no valid extension is supplied return true
            return true;
        }
        return validExt.equals(fileExt);
    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        }
        return "";
    }
}
