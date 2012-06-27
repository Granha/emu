/**
 * 
 */
package abstractions;

import java.io.Serializable;

/**
 * @author rdru
 * INV: 
 */
public class CommandC implements Serializable {
	private static final long serialVersionUID = 1L;
	int uid; 
	int docId;
	PosC pos;
	Part in, out;

	CommandC(int uid, int docId, PosC pos, Part out, Part in) {
		this.uid = uid;
		this.docId = docId;
		this.pos = pos;
		this.in = in;
		this.out = out;
	}

	CommandC reverse() {
		return new CommandC(uid, docId, pos, out, in);
	}		
}
