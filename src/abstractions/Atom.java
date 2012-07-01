/**
 * The smallest editable unit (a paragraph on plain text documents).
 * Has an imutable unique id (AtomId).  A lock is required to edit an Atom to prevent
 * 2 or more editors modify the same Atom.
 */
package abstractions;


/**
 * @author rdru
 *
 */
public class Atom {
	AtomId id;					// AtomId
	int lock;					// user the has the lock for this atom (null if none).
	StringBuilder at;			// contents of the atom (text)
	//ArrayList<Integer> al;		// list of line breaks for a window with w width.
	//int w; 					// width for which al was calculated

	// ****************** Constructors:
	// Returns a new atom locked for uid (unlocked if null) with AtomId based on prev
	// and next.
	// Initialize at with String s when provided.
	public Atom(AtomId id, int uid, String s) {
		this.id = id;
		this.lock = uid;
		this.at = new StringBuilder(s);		
	}
	
	Atom(int uid, AtomId prev) {
		lock = uid;
		id = new AtomId(prev);
	}
	
	Atom(int uid, AtomId prev, String s) {
		this(new AtomId(prev), uid, s);
	}
	
	Atom(int uid, AtomId prev, AtomId next) {}
	
	Atom(int uid, AtomId prev, AtomId next, String s) {}


	// ****************** other methods

	// return a deep copy of this object without inserting it to the hash
	public Atom copy() {
		return new Atom(this.id, this.lock, this.at.toString());
	}
	
	// Insert character c at position p.	
	public void insertC(Pos p, char c) {
		at.insert(p.c, c);
	}

	// Insert String s at position p.
	public void insertS(Pos p, String s) {
		at.insert(p.c, s);
	}
	
	public void insertS(int c, String s) {
		at.insert(c, s);
	}
	
	public void appendS(String s) {
		at.append(s);
	}
	
	public void removeS(Pos p, String str) {
		int start = p.c;
		int end = start + str.length();
		
		assert end <= at.length() : "string to be removed goes beyond at";
		
		at.delete(start, end);
	}
	
	public void setText(String s) {
		at = new StringBuilder(s);	
	}
	
	// remove the prefix from the atom text
	public void removePrefix(String prefix) {
		// ensure that prefix is smaller 
		assert at.length() >= prefix.length() : "prefix is bigger than at";
		
		at.delete(0, prefix.length());
	}
	
	// remove the suffix from the atom text
	public void removeSuffix(String suffix) {
		// ensure that suffix is smaller
		assert at.length() >= suffix.length() : "suffix is bigger than at";

		int end = at.length() - 1;
		int start = end - suffix.length();
		
		at.delete(start, end);
	}

	// returns the AtomId of the current atom.
	public AtomId getAtomId() {
		return id;
	}

	// returns the int of the lock holder
	public int getlock() {
		return lock;
	}
	
	public StringBuilder getAt() {
		return at;
	}
	
	public int length() {
		return at.length();
	}

	// retunrs the number of lines needed to layout the current atom in a cols wide window.
	// Obs: Eh necessario pois quando ao se mudar o conceito de atomo, nao perderemos a
	// modularidade da janela.
	public int getLines(int cols) {
		int length = at.length();
		return (int) Math.ceil(length/cols);
	}

	// change the windows width and recalculate the line breaks for the current atom
	public void setWidth(int w) {}

	// redraw the editing window ???
	public int redraw(int w) {
		return 0;		// to compile ...
	}
	
	public String toString() {
		return "atom ("+  id +"):" + at;
	}
	
	// split the current atom in the specified position, returning a new atom
	// containing the rest of the content. Use the next atom to correctly generate
	// the AtomId.
	public Atom split(int pos, Atom next) {
		if (pos > at.length()) {
			System.err.printf("Cannot split, pos, %d, is bigger that length", pos);
			System.exit(1);
		}
		/*
		if (next != null)
			System.out.println("spliting with next = " + next.getAtomId().prettyPrint());
		else
			System.out.println("spliting with next = " + null);
			*/
		AtomId newId = null;
		
		if (next != null) {
			newId = new AtomId(id, next.getAtomId());
		}
		else {
			newId = id.next();
		}
	
		String rest = "";
		
		if (pos < at.length())
			rest = at.substring(pos);
		
		at = new StringBuilder(at.subSequence(0, pos));
		
		return new Atom(newId, lock, rest);
	}
	
	// join the current atom with another one, merging the current content
	// with the other atom content
	public void join(Atom atom) {
		at.append(atom.at);
		//System.out.println("joining atoms");
		if (lock != atom.lock) {
			System.err.print("Cannot join atoms, lock owners differ");
			System.exit(1);
		}
	}
	
	public String prettyPrint() {
		StringBuilder output = new StringBuilder();
		
		output.append("Atom(" + id.prettyPrint() + ")\n");
		output.append(at);
		
		return output.toString();
	}
	
	public boolean equals(Atom other) {
		return id.equals(other.id) && at.equals(other.at);
	}
}
