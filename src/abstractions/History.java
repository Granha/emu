/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;
import emu.EMU;
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

	// all lists sbove behave primarely as a stack.  The top index for each of them:
	int histCTop = -1, histTop = -1, undoTop = -1, redoTop = -1, checkPoint = -1;

	// Creates a new history where the compacted history is initiated with the given Command list
	// Used when a client opens a file that is already being edited by others.
	public History(Vector<Command> l) {
		this.histU.addAll(l);
	}

	public History() {}


	// sets a checkPoint at the top of histU.
	public void setCheckPoint() {
		checkPoint = histU.size();
	}


	// Clear the checkPoint (probably not needed ...)
	public void clearCheckPoint() {
		checkPoint = -1;
	}


	// Insert a Command in the histU.
	public void insert(Command c) {
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

	// Performs the folling action:
	// 1-Compact all Commands in histU
	// 2-Remove them from histU and 
	// 3-insert into the histC
	// 4-Sends all new compacted Commands to the server.
	// (#4 may be moved to the docManagement thread ...)
	// local user can not edit during steps 1 and 2
	public void compact() {
		boolean modified = true;
		Command f, s;
		StringBuilder cmds = new StringBuilder();
		Command c;
		CommandC cc;
		
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
					(f.in == null && s.out == null && f.out.equals(s.in)) ||
					(f.out == null && s.in == null && f.in.equals(s.out)) ||
					(f.out.equals(s.in) && f.in.equals(s.out))) {
					histU.remove(i);
					histU.remove(i+1);
					modified = true;
				}
				// two consecutive strings insertions in the same atom
				// @todo: test
				else if (
						 f.pos.getAtom().getAtomId().toString() == f.pos.getAtom().getAtomId().toString() &&
						(f.pos.c + f.in.getInic().length()) == s.pos.c &&
						 f.in == null && f.out == null &&
						 s.in == null && s.out == null &&
						 f.in.getInic() != null && f.in.getAtomList() == null && f.in.getFinal() == null &&
						 s.in.getInic() != null && s.in.getAtomList() == null && s.in.getFinal() == null) {
					histU.remove(i+1);
					f.in.setInic(f.in.getInic() + s.in.getInic());
					modified = true;
				}
				// two consecutive string removals
				// @todo: test
				else if (f.pos.getAtom().getAtomId().toString() == f.pos.getAtom().getAtomId().toString() &&
						(f.pos.c + f.in.getInic().length()) == s.pos.c &&
						 f.in == null && f.out == null &&
						 s.in == null && s.out == null &&
						 f.in.getInic() != null && f.in.getAtomList() == null && f.in.getFinal() == null &&
						 s.in.getInic() != null && s.in.getAtomList() == null && s.in.getFinal() == null) {
					histU.remove(i+1);
					f.out.setInic(f.out.getInic() + s.out.getInic());
					modified = true;
				}
				else
					i++;
			}
			
			// test other patterns ...
		}
			
		// 2-Remove them from histU
		// 3-insert into the histC
		while (histU.size() > 0) {
			// removes the first (oldest) command and store it in c
			c = histU.remove(0);
			// transform it into a CommandC and insert into histC
			insertCompacted(cc = c.CommandC(EMU.curUser.userId, EMU.cw.curDocId));
			// add a serialiazed version of cc to cmds
			cmds.append(cc.toString());
		}
		
		
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
	public Command undo() {
		// from histU
		Command c = null;
				
	    if (histU.size() > 0) {
	    	c = histU.removeLast();
	    	histTop--;
	    }
		// from histC
	    else if (histC.size() > 0) {
	    	c = new Command(histC.remove(histC.size()-1));
	    	// this command needs to be resend, so it is inserted in
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
	public Command redo() {
		Command c = null;
		if (undo.size() > 0) {
			c = undo.remove(undo.size()-1);
			c = c.reverse();
			histU.addLast(c);
		}

		return c;
	}
	
}
