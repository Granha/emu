/**
 * @author rdru
 * INV: 
 */
package server;
	
	import java.util.LinkedList;
	import abstractions.CommandC;
	import abstractions.User;

public class OpenDocument {
	int docId;				// id for open documents
	String docName;
	String text;			// doc contents before any user edits
	LinkedList<CommandC> commandList = new LinkedList<CommandC>();
	LinkedList<User> editingUsers = new LinkedList<User>();
	

	OpenDocument(int docId, String docName, String text) {
		this.docId = docId;
		this.docName = docName;
		this.text = text;
	}
	
	public void addUser(User user) {
		editingUsers.add(user);
	}
	
	public void removeUser(User user) {
		if(!editingUsers.remove(user)) {
			// problems removing from editingUsers
		}
	}
	
	public static OpenDocument isOpen(String docName) {
		for (OpenDocument d: Server.openDocuments)
			if(docName.equals(d.docName))
				return d;
		return null;
	}
}
