/**
 * @author rdru
 * INV: 
 */
package server;
	import java.util.LinkedList;
	import javax.print.Doc;
	import javax.swing.JOptionPane;

	import abstractions.User;
	import static server.OpenDocument.*;
	import server.ServerSetup;
	import server.ServerMonitor;
	
public class Server {
	// Server windows
	public static ServerW serverW = new ServerW();
	public static ServerSetup setup = new ServerSetup();
	public static ServerMonitor monitor = new ServerMonitor();
	
	// semantic data
	private final String[] serverNames = {"Local"};
	private final String[] serverURL = {"http://localhost"};
	private final String admPassword = "[C@3925d53a"; //senha do EMU server
	private final String serverPath = "/Volumes/Macintosh HD/Users/Shared/EMU_Server/";
	private final String EMUUserFile = serverPath + "userList";
	private final String EMUSharedDir = serverPath + "shared/";
	static LinkedList<User> userList = new LinkedList<User>();
	LinkedList<String> docsList = new LinkedList<String>();
	String sharedDirectory = "path to the shared directory";

	// dynamic data (currently logged clients and open documents)
	public static LinkedList<ClientUser> loggedUsersList = new LinkedList<ClientUser>();
	public static LinkedList<OpenDocument> openDocuments = new LinkedList<OpenDocument>();
	public static StringBuilder fullLog = new StringBuilder();
	
	// id manager (for now the ids are generated sequentially...)
	static int nextUserId = 0;
	int nextClientId = 0;
	int nextDocId = 0; 
	
	Server() {
		String password;
		
		// get adm password... and validate it
		password = new String();		// substitute for user input password
		if (!password.equals(admPassword)) {
			// show invalid authentication dialog
			
		}
		
		// reads password file and init userList
		
		// reads the file list from file system and inserts than into docsList ...
		// at boot, the server adds all files from the shared file directory.

		
		// start all service threads
		
		// show server window
		serverW.setVisible(true);
		
	}
	
	
	// *************** local services
	// adds a new user in the server known user list.
	public void addUser(String logname, String password, String userName, String userEmail) {
		for (User u: userList)
			if (u.logname.equals(logname) || u.userName.equals(userName) || u.userEmail.equals(userEmail)) {
	            JOptionPane.showMessageDialog(monitor.getContentPane(),
	                    "Add User:\nUser Name, Email or Log Name already exists.", "", JOptionPane.ERROR_MESSAGE);
	            return;
			}

		// inserts the user
		userList.add(new User(nextUserId++, logname, password, userName, userEmail));
	}
	
	// remove a user from the user list.  Returns false on error, or true otherwise
	// Pre: user does not have an open session
	public boolean removeUser(User u) {
		if (loggedUsersList.contains(u))
            JOptionPane.showMessageDialog(monitor.getContentPane(),
                    "Remove User:\nUser is currently active, can not remove.", "", JOptionPane.ERROR_MESSAGE);
		else
			userList.remove(u);
		return true;
	}
	
	// returns true if pair <Client/User> is valid and on the loggedUserList,
	// false otherwise.
	public boolean validadeUser(String client, int userId) {
		ClientUser cu = new ClientUser(client, userId);
		for (ClientUser u: loggedUsersList)
			if (u.equals(cu))
				return true;
		return false;
	}
	
	// 
	public boolean validateAdm() {
        JOptionPane.showMessageDialog(monitor.getContentPane(),
                "Remove User:\nUser is currently active, can not remove.", "", JOptionPane.ERROR_MESSAGE);
        return false;
	}
	
	// add user to the list of users editing a given doc
	public void addUserToDoc(String client, int userId, String docName) {}
	
	// remove user from the list of users editing a given doc
	public void removeUserToDoc(String client, int userId, String docName) {}
	
	public static String userName(int userId) {
		String s = null;
		return s;
	}
	
	public static String docName(int docId) {
		String s = null;
		return s;
	}
	
	public static String[] userNames(int[] users) {
		String s[] = null;
		return s;
	}
	
	public static String[] docNames(int[] docs) {
		String[] s = null;
		return s;
	}
	
	
	// logs all 

	// **************** service provided to clients:
	
	// authenticate the given logname and register the user as loged on c.
	// Returns the User data in case of success,
	// otherwise returns null.
	public static User login(String c, String logname, String password) {
		ClientUser cu;
		for (User u: userList)
			if (u.logname.equals(logname)) {
				if (!u.password.equals(password))
					return null;
				u.userId = nextUserId++;
				cu = new ClientUser(c, u.userId);
				loggedUsersList.add(cu);
				return u;
			}
		return null;
	}
	
	// logout the user: send any pending updates to other clients,
	// removes all associated docs from the docsList, 
	//  remove the user from the userList and send to all clients editing some
	// of this user's docs a removeUserLocally.
	public void logout(String c, int userId) {
	}
	
	// addDoc: add a new document to the server managed documents docsList.
	// returns true in case of success and false otherwise
	public boolean addDocument(String client, int userId, String docName, String docText) {
		if(!validadeUser(client, userId))
			return false;
		// if docsList already contains a file with docName returns false
		
		// create a new file named docName in server docs directory containig docText
		docsList.add(docName);
		return true;
	}
		
	// remove a doc from the server managed documents docsList.
	public boolean removeDocument(String client, int userId, String docName) {
		return true;
	}
	
	// openDoc: opens an existing document stored in the server.
	// For now the server handles all documents in a single directory and
	// all users have the same access rights...
	// docPath (for now) is just the file's name.
	// Returns the docId of the opened document, or -1 in case of error ...
	public OpenDocument openDoc(String client, int userId, String docPath) {
		
		// verify <c,userId>
		if(!validadeUser(client, userId))
			return null;
		
		// if can not find file, returns null
		if(!docsList.contains(docPath))
			return null;
		
		// if doc is already open, just add the user to the editingUsers list
		for (OpenDocument d: openDocuments)
			if ((d = isOpen(docPath)) != null)
				return d;
		
		// has to open the document
		// fileSystem open the docPath file and read its contains to text
		String text = "Empty file ... to compile!!";
		OpenDocument d2;
		if (!openDocuments.add(d2 = new OpenDocument(nextDocId++, docPath, text)))
			return null;
		return d2;
	}

	public void closeDoc(String client, int userId, int docId) {}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				// connect with the server

				// creates the thread pool for server edits

				// init the first EMU Window
				serverW.setVisible(true);
			}
		});
	}
}

class ClientUser {
	String client;
	int userId;
	LinkedList<Doc> docsList = new LinkedList<Doc>();
	
	ClientUser(String client, int userId) {
		this.client = client;
		this.userId = userId;
	}
	
	void addDocument(Doc doc) {
		docsList.add(doc);
	}
	
	void removeDocument(Doc doc) {
		docsList.remove(doc);
	}
}
