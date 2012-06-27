/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;

// PosC is mutable and represents a position within the EMUDoc.
// Contains a pair <aid, c> for AtomId and char position within the atom's string.
// Unsolved issue: the PosC should store the AtomId or it AtomId?
// Storing the AtomId it is cheap to get to its id, the oposite is not true and
// requires a search in the hashmap.
// The compacted history can not have references to Atoms requiring a translation
// of all PosC objects to reffer to the AtomIds.
public class PosC {
	AtomId aid;
	int c;
	
	//Pre: 0 <= c < aid.at.length
	PosC(AtomId aid, int c) {
		this.aid = aid;
		this.c = c;
	}

	PosC(Pos p) {
		new PosC(p.a.getAtomId(), p.c);
	}

	//Effects: returns the line in the AtomId for the current PosC 
	// (given a window width).
	public int line(int width) {
		return 0;
	}


	//Effects: returns the char position
	public int getC() {
		return c;
	}

	// compares 2 PosC objects. Returns true if this is greather than p.
	// use equals for equality.
	public boolean greather(PosC p) {
		if (aid.smaller(p.aid))
			return false;
		if (aid.equals(p.aid))
			if (c < p.c)
				return false;
		return true;
	}
}

