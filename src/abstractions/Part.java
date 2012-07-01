/* EMU 1.1
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;

import java.util.*;

// A Part represents all the eleemnts in a general selection.
// It is imutable and composed of: initial-string, a list of Atoms, final-string.
// The strings and the list of Atoms can be empty, but at least one of them must be
// non-empty for a proper Part.
// The Part always retains the original id of the Atoms so that Undo and Redo operations
// can reestore the original AtomIds used in the EMUDoc.
public class Part {
	String pt_inic;			//pode ser vazio
	String pt_final;			//Pode ser vazio
	ArrayList<Atom> list;	//Pode ser vazio, as ids novas são geradas após sairem do ClipBoard
	Pos end;

	// EFFECTS: Copies the strings and the list of Atoms preserving their AtomIds.
	// Note: it has to copy the elements to keep the Part imutable.
	Part(Selection sel) {}

	// Constructs a Part by extracting selection from the atom list
	public Part(Selection sel, LinkedList<Atom> al) {
		Pos start = sel.getStart();
		Pos end = sel.getEnd();
		
		this.end = end;
		
		int startIndex = al.indexOf(start.getAtom());
		int endIndex = al.indexOf(end.getAtom());
		
		// selection must be inside the atom list
		assert startIndex != -1 : "start atom not in al";
		assert endIndex != -1 : "end atom not in al";

		//System.out.println("startIndex: " + startIndex + ", endIndex: " + endIndex);
		
		// create the initial string if necessary
		{
			StringBuilder at = start.getAtom().getAt();

			// get the whole string
			if (startIndex != endIndex) {
				this.pt_inic = at.substring(start.getC());
			}
			// in the same atom
			else {
				this.pt_inic = at.substring(start.getC(), end.getC());
			}
		
		}
		int next = startIndex + 1;
		this.list = null;

		// create the intermediate list of atoms
		while (next < endIndex) {
			Atom curr = al.get(next);
						
			// initialize list
			if (this.list == null) {
				this.list = new ArrayList<Atom>();
			}
			
			this.list.add(curr.copy());
			
			next++;
		}
		
		// handle the final part (it may be become an atom or just a string)
		this.pt_final = null;
		if (startIndex != endIndex) {
			StringBuilder at = end.getAtom().getAt();
			
			// the last part takes only a portion of the atom
			if (end.getC() < at.length()) {
				this.pt_final = at.substring(0, end.getC());
			}
			// the last part is the whole atom
			else if (end.getC() > 0) {
				if (this.list == null) {
					this.list = new ArrayList<Atom>();
				}
				
				this.list.add(end.getAtom().copy());
			}
		}
	}
	
	// Creates a part from a text string staring at a give position, preceding
	// end and assigned to user whose id is uid
	public Part(String text, Pos start, Pos end, int uid) {
		String ls = System.getProperty("line.separator");
		String[] paragraphs = text.split(ls);
		
		//System.out.println("broke into " + paragraphs.length);
		
		AtomId aid = null;
		AtomId endAid = null;
		
		// anticipate that first atom will be splited
		if (paragraphs.length > 0 && start.getC() < start.getAtom().length()) {
			endAid = start.getAtom().getAtomId().next();
		}
		
		if (endAid == null && end != null) {
			endAid = end.getAtom().getAtomId();
		}

		for (int i = 0; i < paragraphs.length; i++) {
			String p = paragraphs[i];
			
			//System.out.printf("[%d] {%s}\n", i, p);
			
			// handles the first paragraph
			if (i == 0) {
				if (p.length() > 0) {
					this.pt_inic = p;
					continue ;
				}
			}
			
			if (aid == null) {
				
				// there is no atom following start, so it can just use the
				// next atom id
				if (endAid == null) {
					aid = start.getAtom().getAtomId().next();
				}
				// there is an atom following, so it should go down
				// in the atom hierarchy
				else {
					aid = new AtomId(start.getAtom().getAtomId(),
									 endAid);
				}
			}
			else {
				aid = aid.next();
			}
			
			// a final string will be recorded apart if the change was in the middle of
			// the first atom (this indicates that it should be splited)
			if (i == (paragraphs.length - 1) && start.getC() < start.getAtom().length()
					&& p.length() > 0) {
				this.pt_final = p;
			}
			else {
				Atom atom = new Atom(aid, uid, p);
				
				if (this.list == null) {
					this.list = new ArrayList<Atom>();
				}
				this.list.add(atom);
			}
		}
	}
	
	// returns the initial content of this part
	public String getInic() {
		return this.pt_inic;
	}
	
	public void setInic(String s) {
		this.pt_inic = s;
	}
	
	// return all the atom of this part
	public ArrayList<Atom> getAtomList() {
		return this.list;
	}
	
	public Pos getEnd() {
		return this.end;
	}
	
	// returns the final content of this atom
	public String getFinal() {
		return this.pt_final;
	}
	
	// returns true if part is completely empty
	public boolean isEmpty() {
		return (pt_inic == null || pt_inic.equals("")) 
				&& list == null && 
				(pt_final == null || pt_final.equals(""));
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		
		if (pt_inic != null) {
			output.append(pt_inic);
			
			if (list != null || pt_final != null) {
				output.append(System.getProperty("line.separator"));
			}
		}
		
		if (list != null) {
			Atom last = list.get(list.size() - 1);
			for (Atom a: list) {
				output.append(a.getAt().toString());
				
				if (a != last || pt_final != null)
					output.append(System.getProperty("line.separator"));
			}
		}

		if (pt_final != null)
			output.append(pt_final);
		
		return output.toString();
	}

	// Returns a human comprehensible string representation
	public String prettyPrint() {
		StringBuilder out = new StringBuilder();
		
		out.append("pt_inic: " + pt_inic + "\n");
		
		if (list != null) {
			for (Atom a: list) {
				out.append(a.prettyPrint() + "\n");
			}
		}
		else {
			out.append("list = null\n");
		}
		
		out.append("pt_final: " + pt_final);
		
		return out.toString();
	}
	
	public boolean equals(Part other) {
		
		if (other == null)
			return false;
		
		if (this.pt_inic != null && other.pt_inic != null) {
			if (!this.pt_inic.equals(other.pt_inic))
				return false;
		}
		else if(this.pt_inic != other.pt_inic)
			return false;
		
		if (this.pt_final != null && other.pt_final != null) {
			if (!this.pt_final.equals(other.pt_final))
				return false;
		}
		else if(this.pt_final != other.pt_final)
			return false;
		
		if (this.list != null && other.list != null) {
			
			if (this.list.size() != other.list.size())
				return false;
			
			for (int i = 0; i < this.list.size(); i++) {
			
				if (!this.list.get(i).equals(other.list.get(i))) {
					return false;
				}				
			}
		}
		else if (this.list != other.list)
			return false;
		
		return true;
	}
}

