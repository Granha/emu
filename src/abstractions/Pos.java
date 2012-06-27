/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;

// Pos is mutable and represents a position within the EMUDoc.
// Contains a pair <a, c> for Atom and char position within the atom's string.
// Unsolved issue: the Pos should store the Atom or it AtomId?
// Storing the Atom it is cheap to get to its id, the oposite is not true and requires
// a search in the hashmap.
// The compacted history can not have references to Atoms requiring a translation
// of all Pos objects to reffer to the AtomIds.
public class Pos {
	Atom a;
	int c;
	
	//Pre: 0 <= c < a.at.length
	Pos(Atom a, int c) {
		this.a = a;
		this.c = c;
	}

	//Effects: returns the AtomId
	public AtomId getId() {
		return a.id;
	}
	
	public Atom getAtom() {
		return a;
	}


	//Effects: returns the line in the Atom for the current Pos (given a window width).
	public int line(int width) {
		return 0;		// to compile ...
	}



	//Effects: returns the char position
	public int getC() {
		return c;
	}

	// compares 2 Pos objects. Returns true if this is greather than p.
	// use equals for equality.
	public boolean greather(Pos p) {
		if (a.id.greather(p.a.id))
			return true;
		if (a.id.equals(p.a.id))
			if (c < p.c)
				return false;
		return true;
	}
	
	public String prettyPrint() {
		return "Pos(Atom(" + a.id.prettyPrint() + ")," + c + ")";
	}
}

