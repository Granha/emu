/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;
import java.util.*;

import abstractions.CommandC;
import abstractions.Command;

// Stores the commands that changed the EMUDoc in recent history (histU) and
// the long term history (histC).  The histU contains only local commands while
// the histC contains commands form all users editing the EMUDoc.
// The histU contins Commands while the histC contains CommandCs.
// Recebe os comandos do editor local que podem ter sido executados pelo usuário local ou
// pelos outros usuários remotos via servidor.
// Os comandos do usuario local sempre entram na história não compactada, enquanto os dos
// usuários remotos são anexados ao final da história compactada.
public class History {
	
	static boolean debug = false;
	
	// the compacted history must be threadSafe, therefore it is a Vector.
	Vector<CommandC> histC = new Vector<CommandC>();
	LinkedList<Command> histU = new LinkedList<Command>();
	ArrayList<Command> undo = new ArrayList<Command>();
	//ArrayList<Command> redo = new ArrayList<Command>();
	
	HashMap<String, Atom> id2atom;

	// all lists sbove behave primarely as a stack.  The top index for each of them:
	int histCTop = -1, histTop = -1, undoTop = -1, redoTop = -1, checkPoint = -1;

	// Creates a new history where the compacted history is initiated with the given Command list
	// Used when a client opens a file that is already being edited by others.
	public History(Vector<Command> l) {
		this.histU.addAll(l);
	}

	public History(HashMap<String, Atom> id2atom) {
		this.id2atom = id2atom;
	}


	// sets a checkPoint at the top of histU.
	public void setCheckPoint() {
		checkPoint = histU.size();
	}

	// Clear the checkPoint (probably not needed ...)
	public void clearCheckPoint() {
		checkPoint = -1;
	}

	// Insert a Command in the histU.
	synchronized public void insert(Command c) {
		histU.addLast(c);
		undo = new ArrayList<Command>();
		histTop++;
	}

	// Insert a Command in the compacted history.
	// Used for adding remote commands to the histC.
	// This MUST be thread safe
	synchronized public void insertCompacted(CommandC c) {
		histC.add(c);
		histCTop++;		 
	}
	
	boolean print() {
		System.out.println("Condition true until now");
		return true;
	}

	// Performs the folling action:
	// 1-Compact all Commands in histU
	// 2-Remove them from histU and 
	// 3-insert into the histC
	// 4-Sends all new compacted Commands to the server.
	// (#4 may be moved to the docManagement thread ...)
	// local user can not edit during steps 1 and 2
	synchronized public void compact() {
		boolean modified = true;
		Command f, s;
		/*StringBuilder cmds = new StringBuilder();
		Command newC;
		CommandC cc; */
		Command newC;
		
		// disables user actions (while compacting histU)
		
		// 1-Compact all Commnads in histU
		while (modified) {		// until no more compacts are available
			modified = false;
			for (int i=0; i<histU.size()-1; ) {
				f = histU.get(i);
				s = histU.get(i+1);
				
				if (f.isCollapsible(s)) {
					if (debug) System.out.println("h: Collapsible");
					histU.remove(i+1);
					histU.remove(i);
					modified = true;
				}
				else if ((newC=f.forwardDelete(s)) != null) {
					if (debug) System.out.println("h: forwardDelete");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else if ((newC=f.backwardDelete(s)) != null) {
					if (debug) System.out.println("h: backwardDelete");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else if ((newC=f.forwardInsert(s)) != null) {
					if (debug) System.out.println("h: forwardInsert");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else if ((newC=f.backwardInsert(s)) != null) {
					if (debug) System.out.println("h: backwardInsert");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else if ((newC=f.insertDelete(s)) != null) {
					if (debug) System.out.println("h: insertDelete");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else if ((newC=f.deleteInsert(s)) != null) {
					if (debug) System.out.println("h: deleteInsert");
					histU.remove(i+1);
					histU.remove(i);
					histU.add(i, newC);
					modified = true;
				}
				else {
					i++;
				}
			}
			
			// test other patterns ...
		}
		
		// @todo: remove this line		
		return ;
		
		// 2-Remove them from histU
		// 3-insert into the histC
		/*
		while (histU.size() > 0) {
			// removes the first (oldest) command and store it in c
			c = histU.remove(0);
			// transform it into a CommandC and insert into histC
			insertCompacted(cc = c.CommandC(EMU.curUser.userId, EMU.cw.curDocId));
			// add a serialiazed version of cc to cmds
			cmds.append(cc.toString());
		}
		*/
		
		// enable user actions
		

		// 4-Sends all new compacted Commands to the server.
		// sends cmds to the server ...
	}	
	
	// removes the most recent Command from the history (histU).
	// inserts the Command into the undo list and returns the removed Command.
	// When the histU is empty the histC is searched for the most recent CommandC issued
	// by the User.  An undo in a histC command does not remove it from histC.
	// Its undoing must be considered as a new command and be added to the histU
	// as a positive command.  This is COMPLICATED ...
	// A CommandC (from histC) can be undone only if no other User has edited the Atoms
	// envolved in the Command.
	// Returns null if no available undoable Command (in which case no Command is removed
	// from the histC list.
	synchronized public Command undo() {
		// from histU
		Command c = null;
				
	    if (histU.size() > 0) {
	    	c = histU.removeLast();
	    	histTop--;
	    }
		// from histC
	    else if (histC.size() > 0) {
	    	c = new Command(histC.remove(histC.size()-1), id2atom);
	    	// this command needs to be resent, so it is inserted in
	    	// the histU
	    	insert(c);
	    }
	    
	    if (c != null) {
	    	c = c.reverse();
	    	undo.add(c);
	    }
	    	
		return c;		// to compile ...
	}
	
	// Removes and returns the most recent Command in the redo list.
	// Returns null when the redo list is empty.
	synchronized public Command redo() {
		Command c = null;
		if (undo.size() > 0) {
			c = undo.remove(undo.size()-1);
			c = c.reverse();
			histU.addLast(c);
		}

		return c;
	}
	
	synchronized public String prettyPrint() {
		StringBuilder output = new StringBuilder();
		
		output.append("========= histU ==========\n");
		for (int i = histU.size() - 1; i >= 0 ; i--) {
			Command c = histU.get(i);
			output.append("-------------------\n");
			output.append(c.prettyPrint() + "\n");
		}
		
		output.append("========= histC ==========\n");
		for (int i = histC.size() - 1; i >= 0 ; i--) {
			Command c = new Command(histC.get(i), id2atom);
			output.append("--------------------\n");
			output.append(c.prettyPrint());
		}
		
		return output.toString();
	}
}
