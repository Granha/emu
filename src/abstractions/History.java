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
		Command c;
		CommandC cc; */
		
		// disables user actions (while compacting histU)
		
		// 1-Compact all Commnads in histU
		while (modified) {		// until no more compacts are available
			modified = false;
			for (int i=0; i<histU.size()-1; ) {
				f = histU.get(i);
				s = histU.get(i+1);
				
				// cancelling commands (pos, partOut, partIn)
				// (p1, null, i1) (p1, i1, null) --> ()
				// (p1, o1, null) (p1, null, o1) --> ()
				// (p1, o1, i1)   (p1, i1, o1)   --> ()
				if (f.pos.equals(s.pos) &&
					(f.in == null && s.out == null && f.out != null && f.out.equals(s.in)) ||
					(f.out == null && s.in == null && f.in != null && f.in.equals(s.out)) ||
					(f.in != null && f.out != null && f.out.equals(s.in) && f.in.equals(s.out))) {
					histU.remove(i+1);					
					histU.remove(i);
					modified = true;
				}
				else if(f.pos.equals(s.pos) &&
						// split followed by join
						((f.isSplit() && s.isJoin()) || 
						 (f.isJoin() && s.isSplit()))) {
					histU.remove(i+1);					
					histU.remove(i);
					modified = true;					
				}
				// two consecutive strings insertions in the same atom
				// @todo: make it more generic
				else if (f.in != null && f.out == null &&
						 s.in != null && s.out == null &&
						 f.in.getInic() != null && f.in.getAtomList() == null && f.in.getFinal() == null &&
						 s.in.getInic() != null && s.in.getAtomList() == null && s.in.getFinal() == null &&
						 f.pos.getAtom().getAtomId().equals(s.pos.getAtom().getAtomId())) {
												
						 // forward insertion
						 if ((f.pos.c + f.in.getInic().length()) == s.pos.c) {
							 histU.remove(i+1);
							 f.in.setInic(f.in.getInic() + s.in.getInic());
							 modified = true;
						 }
						 // backwards insertion
						 else if (f.pos.equals(s.pos)) {
							 histU.remove(i);
							 s.in.setInic(s.in.getInic() + f.in.getInic());
							 modified = true;
						 }
						 else
							 i++;
				}
				// two consecutive string removals
				// @todo: make it more generic
				else if (f.in == null && f.out != null &&
						 s.in == null && s.out != null &&
						 f.out.getInic() != null && f.out.getAtomList() == null && f.out.getFinal() == null &&
						 s.out.getInic() != null && s.out.getAtomList() == null && s.out.getFinal() == null) {
	
					// forward removal from the same position
					if (f.pos.equals(s.pos)) {
						histU.remove(i+1);
						f.out.setInic(f.out.getInic() + s.out.getInic());
						modified = true;
					}
					// backwards removal
					else if	((s.pos.c + s.out.getInic().length()) == f.pos.c) {
						histU.remove(i);
						s.out.setInic(s.out.getInic() + f.out.getInic());
						modified = true;
					}
					else {
						i++;
					}
				}
				else
					i++;
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
