/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;
//import emu.*;
//import emu.interfaces.*;
//inport emu.client.*;
//import emu.server.*;

// Selection: represents a selection of text in a document.
// Inv: start must precede end (end.greater(start) is true)
public class Selection {
	Pos start, end;
	
	Selection(Pos start, Pos end) {
		if (end.greather(start)) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;			
		}
	}
	
	void set(Pos start, Pos end) {
		if (end.greather(start)) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;			
		}
	}
	
	public Pos getStart() {
		return this.start;
	}
	
	public Pos getEnd() {
		return this.end;
	}
	
	// Pos: after both start and end be set, start must precede end
	void setStart(Pos start) {
		this.start = start;
	}
		
	// Pos: after both start and end be set, start must precede end
	void setEnd( Pos end) {
		this.end = end;
	}
	
	public String prettyPrint() {
		return "Selection("+ start.prettyPrint() +", " + end.prettyPrint() + ")";
	}
}

