package org.codeaffectionado.training.jackrabbit_tutorial;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws LoginException, RepositoryException
    {
    	Repository repo = new TransientRepository();
    	Session session = repo.login(
    			new SimpleCredentials("admin","admin".toCharArray()));
    	Node root = session.getRootNode();
    	
    	if(!root.hasNode("content")){
	    	Node contentNode = root.addNode("content");
	    	Node peopleNode = contentNode.addNode("people");
	    	Node jsmith = peopleNode.addNode("jsmith");
	    	
	    	jsmith.setProperty("firstname", "john");
	    	jsmith.setProperty("age", 43);
	    	
	    	session.save();
    	}else{
    		Node contentNode = root.getNode("content");
    		contentNode.remove();
    		session.save();
    	}
    	
    	NodeIterator i = root.getNodes();
    	while(i.hasNext()){
    		Node node = i.nextNode();
    		System.out.println(node.getName());
    	}
    	
        session.logout();
    }
}
