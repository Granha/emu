/**
 * 
 */
package abstractions;

import java.util.HashMap;

/**
 * @author rdru
 * INV: pos reffers to the position at the beginig of the selection.
 */
public class Command {
	//int uid;		// actualy only needed when in the Compacted History
	//int docId;		// actualy only needed when in the Compacted History
	Pos pos;
	Part in, out;

	Command(Pos pos, Part out, Part in) {
		this.pos = pos;
		this.in = in;
		this.out = out;
	}
	
	Command(CommandC c, HashMap<String, Atom> id2atom) {
		// Constructor for creating q Command based on
		// a CommandC
		AtomId aid = c.pos.aid;
		Pos pos = new Pos(id2atom.get(aid), c.pos.c);
		this.pos = pos;
		this.in = c.in;
		this.out = c.out;			
	}
	
	Pos getPos() {
		return this.pos;
	}
	
	Part getIn() {
		return this.in;
	}
	
	Part getOut() {
		return this.out;
	}

	Command reverse() {
		return new Command(pos, in, out);
	}		
	
	// create a new CommandC based on c by adding the current UserId and DocId
	CommandC CommandC(int uid, int docId) {
		return new CommandC(uid, docId, new PosC(pos), out, in);
	}
	
	public String prettyPrint() {
		StringBuilder output = new StringBuilder();
		output.append("Command:\n");
		
		output.append(pos.prettyPrint()+ "\n");
		
		if (out != null) {
			output.append("out:\n" + out.prettyPrint() + "\n");
		}
		else {
			output.append("out: null\n");
		}
		
		if (in != null) {
			output.append("in:\n" + in.prettyPrint());
		}
		else {
			output.append("in: null\n");			
		}
		
		return output.toString();
	}
	
	public boolean isSplit() {
		return in != null && out == null && 
					in.isEmpty();
	}
	
	public boolean isJoin() {
		return in == null && out != null &&
				out.isEmpty();
	}
	
	public boolean isInsert() {
		return in != null && out == null 
				&& !in.isEmpty();
	}
	
	public boolean isDelete() {
		return in == null && out != null
				&& !out.isEmpty();
	}
	
	public boolean isReplace() {
		return in != null && out != null
				&& !in.isEmpty() && !out.isEmpty();
	}
	
	// Returns true if the combination of this command with other
	// will result in none
	// Canceling commands (pos, partOut, partIn)
	// (p1, null, i1) (p1, i1, null) --> ()
	// (p1, o1, null) (p1, null, o1) --> ()
	// (p1, o1, i1)   (p1, i1, o1)   --> ()
	public boolean isCollapsible(Command other) {
		assert other != null : "ohter command is null";
		
		// command must start in the same position
		if (!this.pos.equals(other.pos))
			return false;
	
        // removal followed by an insert
		if (this.isDelete() && other.isInsert() && 
				this.out.equals(other.in))
			return true;
		
		// insert followed by a removal		
		if (this.isInsert() && other.isDelete() &&
				this.in.equals(other.out))
			return true;
		
		// replace followed by the opposite replace
	    if (this.isReplace() && other.isReplace() &&
	    		this.out.equals(other.in) && this.in.equals(other.out))
	    	return true;
	    
	    
	    // split followed by join or vice-versa
	    if ((this.isSplit() && other.isJoin()) || 
	    	(this.isJoin() && other.isSplit()))
	    	return true;
	    	
	    return false;
	}
	
	// 2   forward inserts     (p1, null, i1)       (p1, null, i1|i2)
    //                         (p1+i1, null, i2)
	// @side-effect: may change this and other
	Command forwardInsert(Command other) {
		
		// both commands must be insert
		if (! (this.isInsert() && other.isInsert()) )
			return null;
		
		if (this.in.end.equals(other.pos)) {
			this.in.append(other.in);
			return this;
		}
		
		return null;
	}
	
	// 3   backward inserts    (p1, null, i1)       (p1, null, i2|i1)
    //                         (p1, null, i2) 
	// @side-effect: may change this and other
	Command backwardInsert(Command other) {

		// both commands must be insert
		if (! (this.isInsert() && other.isInsert()) )
			return null;
		
		if (this.pos.equals(other.pos)) {
			other.in.append(this.in);
			return other;
		}
		
		return null;
	}
	
	// 2   forward deletes     (p1, o1, null)       (p1, o1|o2, null)
    //                         (p1, o2, null)
	// @side-effect: may change this and other
	Command forwardDelete(Command other) {

		// both commands must be insert
		if (! (this.isDelete() && other.isDelete()) )
			return null;
		
		if (this.pos.equals(other.pos)) {
			this.out.append(other.out);
			return this;
		}
		
		return null;
	}

	// 2   backward deletes    (p1, o1, null)       (p2-o2, o2|o1, null)
    //                         (p1-o1, o2, null)
	// @side-effect: may change this and other
	Command backwardDelete(Command other) {

		// both commands must be insert
		if (! (this.isDelete() && other.isDelete()) )
			return null;
		
		if (other.out.end.equals(this.pos)) {
			other.out.append(this.out);
			return other;
		}
		
		return null;
	}

	// @warning: handles non collapsible only
	//3   insert delete       (p1, null, i1)       (p1, null, i1]o2)        o2<=i1
    //                        (p1, o2, null)       
    //3   insert delete       (p1, null, i1)       (p1, o2]i1, null)        o2>i1
    //                        (p1, o2, null)
	// @side-effect: may change this and other
	Command insertDelete(Command other) {
		// both commands must be insert
		if (! (this.isInsert() && other.isDelete()) )
			return null;
		
		if (!this.pos.equals(other.pos))
			return null;
		
		if (this.in.longest(other.out)) {
			this.in.removePrefix(other.out);
			return this;
		}
		else {
			other.out.removePrefix(this.in);
			return other;
		}
	}
	
	// @todo: improve this rule
	// 3   delete insert       (p1, o1, null)       (p1, o1, i2)
    //                         (p1, null, i2) 
	// @side-effect: may change this and other
	Command deleteInsert(Command other) {

		// both commands must be insert
		if (! (this.isDelete() && other.isInsert()) )
			return null;
		
		if (this.pos.equals(other.pos)) {
			// command become a replace
			this.in = other.in;
			return this;
		}
		
		return null;
	}
}
