package org.codeaffectionado.training.jackrabbit_tutorial;

import java.io.FileInputStream;

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

import org.apache.jackrabbit.core.TransientRepository;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	Repository repo = new TransientRepository();
    	Session session = repo.login(
    			new SimpleCredentials("admin","admin".toCharArray()));
    	Node root = session.getRootNode();
    
    	
    	if(!root.hasNode("catalog")){
	    	Node contentNode = root.addNode("catalog");
	    	FileInputStream stream = new FileInputStream("tutorial.xml");
	        session.importXML(contentNode.getPath(),stream,
	        		ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
	        stream.close();
	    	session.save();
    	}else{
    		//Node contentNode = root.getNode("content");
    		//contentNode.remove();
    		//session.save();
    	}
    	
    	dumpToConsole(root);
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
