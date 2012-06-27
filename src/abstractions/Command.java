/**
 * 
 */
package abstractions;

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
	
	Command(CommandC c) {
		// Constructor for creating q Command based on
		// a CommandC
		AtomId aid = c.pos.aid;
		Pos pos = new Pos(Atom.id2atom.get(aid), c.pos.c);
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
}
