package org.codeaffectionado.training.jackrabbit_tutorial;

import java.io.FileInputStream;
import java.io.FileReader;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.core.TransientRepository;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	Repository repository = new TransientRepository();
    	  Session session = repository.login(
    	          new SimpleCredentials("admin","admin".toCharArray()));

    	  try{
    	      CndImporter.registerNodeTypes(new FileReader("article.cnd"),session,true);

    	      Node root = session.getRootNode();
    	      
    	      if(!root.hasNode("articles")){
	    	      Node articles = root.addNode("articles");
	    	      Node newNode = articles.addNode("article","ca:article");
	    	      
	    	      newNode.setProperty("ca:body","Hello World!");
	    	      newNode.setProperty("ca:headline","New Hello World Article");
	
	    	      session.save();   
    	      }

    	      Node savedNode = root.getNode("articles/article");
    	      dumpToConsole(savedNode);


    	  }finally{
    	      session.logout();
    	  }
    	
    	//dumpToConsole(root);
    }
    
    public static void dumpToConsole(Node node) throws RepositoryException {

        System.out.println(node.getPath());

        if(node.hasProperties()){
            PropertyIterator props = node.getProperties();
            while(props.hasNext()){
                Property property = (Property) props.next();

                if(property.isMultiple()){
                    Value[] values = property.getValues();
                    System.out.print(property.getPath()+"="+"[");

                    for(Value value : values){
                        System.out.print(value.getString()+",");
                    }
                    System.out.println("]");
                } else {
                    System.out.println(property.getPath()+ "="+property.getString());
                }
            }
        }

        NodeIterator iterator = node.getNodes();

        while(iterator.hasNext()){
            Node next = (Node) iterator.next();

            dumpToConsole(next);
        }
    }
}
