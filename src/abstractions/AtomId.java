/**
 * (Atom identification) The AtomId is imutable and unique for each atom for all users.
 */
package abstractions;

import java.util.ArrayList;

/**
 * @author rdru
 *
 */
public class AtomId {
	// the actual AtomId is composed of a hierarchy of ints
	ArrayList<Integer> id;
	
	// ************ Constructors
	
	// returns the first atom id
	public AtomId() {
		id = new ArrayList<Integer>();
		id.add(0);
	}
	
	// Returns a new AtomId that imediatly succeeds prev (1.2, 1.3)
	public AtomId(AtomId prev) {
		id = new ArrayList<Integer>(prev.id);
		id.set(prev.id.size()-1, id.get(prev.id.size()-1) + 1);
	}

	// returns a new AtomId with a given id (ATTENTION: it is NOT copied)
	public AtomId(ArrayList<Integer> id) {
		this.id = id;
	}
	
	// Returns a new AtomId which immediately follows id at the same level
	public AtomId next() {
		return next(1);
	}

	public AtomId next(int inc) {
		ArrayList<Integer> id1 = new ArrayList<Integer>(id);
		
		id1.set(id1.size()-1, id1.get(id1.size()-1) + inc);
		return new AtomId(id1);
	}

	// Returns: an unique AtomId that is greather than prev and lower than next.
	// The new AtomId follows the following patterns:
	// (2.1, 2.2)   --> (2.1, 2.1.1, 2.2)
	// (2.1, 2.3)   --> (2.1, 2.2,   2.3)
	// (2.1, 2.1.3) --> (2.1, 2.1.2, 2.1.3) subtracts the last level number
	// Pre: prev.lesser(next)
	AtomId(AtomId prev, AtomId next) {
			
		if (prev.id.size() == next.id.size()) {
			if (prev.id.get(prev.id.size()-1)+1 == next.id.get(next.id.size()-1)) {
				// 2.1.1
				id = new ArrayList<Integer>(prev.id);
				id.add(1);
			} else {		// 2.2
				id = new ArrayList<Integer>(prev.id);
				id.set(id.size()-1, id.get(id.size()-1) + 1);
			}
		}
		// (4, 5.1) --> (5.0)
		else if (prev.id.size() < next.id.size()) {
			id = new ArrayList<Integer>(next.id);				
			id.set(id.size()-1, id.get(id.size()-1) - 1);
		}
		// (4.1, 5) --> (4.2)
		else {
			id = new ArrayList<Integer>(prev.id);
			id.set(id.size()-1, id.get(id.size()-1) + 1);
		}
		//System.out.println("constructor q quero: " + this.prettyPrint());
		//System.out.println("prev: " + prev.prettyPrint());
		//System.out.println("next: " + next.prettyPrint());
	}
	
	// ************** other methods
	// returns true if current AtomId succeds a
	boolean greather(AtomId a) {
		int i = 0, ic = 0, ia = 0;
		
		while (i < id.size() && i < a.id.size()) {
			ic = id.get(i);
			ia = a.id.get(i);
			
			if (ic > ia)
				return true;
			if (ic < ia)
				return false;
			i++;
		}
		if (i == id.size())
			return false;
		return true;
	}
	
	// returns true if current AtomId preceeds a
	boolean smaller(AtomId a) {
		Integer i=0, ic=id.get(0), ia=a.id.get(0);
		while (ic != null && ia != null) {
			if (ic < ia)
				return true;
			if (ic > ia)
				return false;
			i++;
			ic = id.get(i);
			ia = a.id.get(i);
		}
		if (ic == null)
			return true;
		return false;
	}
	
	boolean equals(AtomId aid) {
		return this.id.toString().equals(aid.id.toString());
	}
	
	public String prettyPrint() {
		StringBuilder output = new StringBuilder();
		
		for (int i = 0; i < id.size(); i++) {
			output.append(id.get(i));
			
			if (i != (id.size() - 1))
				output.append(".");
		}
		
		return output.toString();
	}

}
