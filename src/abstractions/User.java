/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;
import emu.EMU;

import java.util.*;

public class User {
	public int userId;
	public String logname;
	public String password;
	public String userName;
	public String userEmail;
	LinkedList<EMUDoc> docsList;
	
	public User(int userId, String logname, String passwd, String userName, 
		String userEmail) {
		this.userId = userId;
		this.logname = logname;
		this.password = passwd;
		this.userName = userName;
		this.userEmail = userEmail;
		
		// add user to the EMU user list
		EMU.userList.add(this);
	}
	
	public void addDoc(EMUDoc doc) {
		docsList.add(doc);
	}
	
	public void removeDoc(EMUDoc doc) {
		// find doc in docsList and remove it
		
		// if docsList is empty, remove user from the EMU user list
		if (docsList.size() == 0)
			EMU.userList.remove(this);
	}
	
	public String getName(){
		return this.userName;
	}	

	public String getLogname(){
		return this.logname;
	}
	
	public String getPassword(){
		return this.password;
	}	
	
	public String getEmail(){
		return this.userEmail;
	}	
}
