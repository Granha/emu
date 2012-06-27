/* EMU 1.1mp
 * 2012 Rogerio Drummond
 * IC – Unicamp
*/

package abstractions;
import interfaces.EMUW;
import javax.swing.text.*;
import java.util.*;

import java.util.LinkedList;

// Manages the EMU Document abstraction: a list of atoms that represents the document.
public class EMUDoc extends PlainDocument {
	private static final long serialVersionUID = 4522298333103862294L;

	// info about the EMUDoc file
	public String fileName;
	public String pathName;
	public boolean isRemote;
	
	private DebugW debug = new DebugW();

	// where it is shown and who edits it
	public LinkedList<EMUW> winList;			// where this EMUDoc is shown
	public LinkedList<Integer> userList;		// users editing this EMUDoc

	// EMU implementation of the EMUDoc
	LinkedList<Atom> al = new LinkedList<Atom>();	// lista dos atomos do documento
	Pos cp;					// current position
	Selection cs;			// current selection
	Part cb;				// clipboard
	public History h;		// execution history (histU, histC, undo and redo)

	// state info
	public boolean isDirty;
	
	// ********************** CONSTRUCTORS

	// Cria um documento vazio
	//public EMUDoc() {
		
	//}

	public EMUDoc(String pathName, boolean isRemote) {
		this.pathName = pathName;
		this.isRemote = isRemote;
	}

	// Cria um documento a partir de um arquivo de texto
	public EMUDoc(String text) {
		debug.setVisible(true);
		this.h = new History();
		this.cp = null;
		this.al = new LinkedList<Atom>();
		Atom atom = new Atom(new AtomId(), -1, "");
		this.al.add(atom);
		this.replace(0, 0, text, null);
	}

	// Cria um documento a partir de um arquivo de texto e historia de modificacoes
	public EMUDoc(String text, History h) {
		this.h = h;
		new EMUDoc(text);
	}
	
	// ********************** Auxiliary methods

	// give an offset in the document, returns the position
	// containing the atom and the offset inside it
	private Pos offset2Pos(int offset) {
		Pos pos = null;
		int start = 0;
		int end = 0;
		int lsLen = System.getProperty("line.separator").length();
		
		for (Atom a: al) {
			end = start + a.length();
			
			if (offset >= start && offset <= end) {
				return new Pos(a, offset - start);
			}
			
			// add one, due to line separator
			start = end + lsLen;
		}
		
		// offset was not found this is an implementation error
		if (pos == null) {
			System.err.println("offset2Pos failed: offset not found " + offset);
			System.exit(1);
		}
		
		return pos;
	}
	
	private int pos2Offset(Pos p) {
		int offset = 0;
		int lsLen = System.getProperty("line.separator").length();
		
		Atom a = getOrDieAtom(p.getAtom().getAtomId());
		int index = getOrDieAtomIndex(a);
		
		for (int i = 0; i < index; i++) {
			offset += al.get(i).length() + lsLen;
		}
		
		offset += p.c;
		
		return offset;
	}
	
	// return the part starting in offset with the specified length
	private Part dumpPart(int offset, int length) {
		Pos start = offset2Pos(offset);
		Pos end = offset2Pos(offset + length);
		Selection sel = new Selection(start, end);
		
		System.out.println(sel.prettyPrint());
		
		return new Part(sel, al);
	}

	// exit if atom id does not exist in id2atom
	private Atom getOrDieAtom(AtomId aid) {
		Atom at = Atom.id2atom.get(aid.prettyPrint());
		
		String msg = "Atom id " + aid.prettyPrint() + " not found in id2atom";
		assert at != null : msg;
		
		return at;
	}
	
	// exit if atom is not present in the list
	private int getOrDieAtomIndex(Atom at) {
		int index = al.indexOf(at);	
	
		String msg = "Atom " + at.getAtomId().prettyPrint() + " not found in the current al";
		assert index != -1 : msg;
	
		return index;
	}
	
	public void insertString(int offset, String str, AttributeSet a) {
		try {
			super.insertString(offset, str, a);
			System.out.println("insertString:" + str);
			
			Element e = super.getParagraphElement(offset);
			System.out.println("paragraph: start" + e.getStartOffset() +" end: "+ e.getEndOffset()+
					" name:" + e.getName());
			
		}
		catch(BadLocationException e) {
			System.err.println("insertString failed with:" + e);
			System.exit(1);
		}
    }
	//public void insertUpdate(AbstractDocument.DefaultDocumentEvent chng, AttributeSet attr) {
		//super.insertUpdate(chng, attr);
		//System.out.println("insertUpdate: "+ chng);
	//}

	public void replace(int offset, int length, String str, AttributeSet a) {
		// when length is zero, replace method behave exactly like insertString
		//try {
			Part removedPart = null;
			
			if (length > 0) {
				removedPart = dumpPart(offset, length);
			}
			
			// must parse the command to generate a new part
			Pos start = offset2Pos(offset);
			Pos end = null;
			
			int endIndex = al.indexOf(start) + 1;
			
			if (endIndex > 0 && endIndex < al.size()) {
				end = new Pos(al.get(endIndex), 0);
			}

			// @todo: change uid (-1) to something valid
			Part insertedPart = new Part(str, start, end, -1);
	
			Command c = new Command(start, removedPart, insertedPart);
			h.insert(c);
			
			System.out.println("replace: " + str + ", length: " + length);
			apply(c);
			
			//super.replace(offset, length, str, a);
		/*}
		catch(BadLocationException e) {
			System.err.println("replace failed with:" + e);
			System.exit(1);
		}*/
    }
	
	public void remove(int offset, int length) {
		//try {
			Part removedPart = dumpPart(offset, length);
			Command c = new Command(offset2Pos(offset), removedPart, null);
			h.insert(c);
			System.out.println("remove: " + offset + ", lenght: " + length);
			apply(c);
			//super.remove(offset, length);
		/*}
		catch(BadLocationException e) {
			System.err.println("remove failed with:" + e);
			System.exit(1);
		}*/
	}
	
	public void undo() {
		
		Command c = h.undo();
		
		if (c != null) {
			apply(c);
		}
	}
	
	public void redo() {
		
		Command c = h.redo();
		
		if (c != null) {
			apply(c);
		}
	}

	// apply a command to the internal representation of a document
	public void apply(Command c) {
		Pos start = c.getPos();
		Part out = c.getOut();
		Part in = c.getIn();
		
		if (debug != null) {
			debug.setHistory(c.prettyPrint());
		}
		
		System.out.println("applying command\n" + c.prettyPrint());
		
		// new line was entered
		// the atom determined by start must be splited
		if (in != null && in.isEmpty() && out == null) {
			splitAtom(start);
			int offset = pos2Offset(start);
			try {
				super.insertString(offset, System.getProperty("line.separator"), null);
			}
			catch(BadLocationException e) {
				System.err.println("Failed to insert a new line when spliting:" + e);
				System.exit(1);
			}
		}
		// a new line was remove, then atoms must be joined
		else if (out != null && out.isEmpty() && in == null) {
			Atom startAtom = start.getAtom();
			joinAtom(startAtom.getAtomId());
			int offset = pos2Offset(start);
			try {
				super.remove(offset, System.getProperty("line.separator").length());
			}
			catch(BadLocationException e) {
				System.err.println("Failed to remove new line when joining:" + e);
				System.exit(1);
			}
		}
		else {
			delP(out, start);
			insertP(in, start);
		}
		
		if (debug != null) {
			debug.setAtomList(prettyPrint());
		}
	}
	
	//public void removeUpdate(AbstractDocument.DefaultDocumentEvent chng) {
		//super.removeUpdate(chng);
		//System.out.println("removeUpdate: "+ chng);
	//}
		
	// ********************** Window manager

	// add a new Window to the list of Windows for this EMUDoc
	void addWin(EMUW win) {
		winList.add(win);
	}
	
	public void setText(String text) {
		// parse the text, setup each Atom and insert them into the al.
		String ls = System.getProperty("line.separator");
		String[] paragraphs = text.split(ls);//("[\\r\\n]+");
		
		AtomId aid = null;

		al = new LinkedList<Atom>();
		for (String p: paragraphs) {
			
			if (aid == null) {
				aid = new AtomId();
			}
			else {
				aid = aid.next();
			}
			
			// @todo: assign the correct user (instead of -1)
			Atom at = new Atom(aid, -1, p);
			al.addLast(at);
		}

		cp = new Pos(al.get(0), 0);
	}

	// ********************** Document manager

	public void openDoc() {
		
	}
	
	public void closeDoc() {
		if (isRemote) {
			// sends all recent comands to the Server
		
		} else 
			if (isDirty) {
				@SuppressWarnings("unused")
				String texto = this.toString();
			
				// salva arquivo
			
		}
		// close (removes this from the doc and win lists
		
	}
	
	public String prettyPrint() {
		StringBuilder output = new StringBuilder();
		
		if (al != null) {
			for (Atom a: al) {
				output.append("[===============================]\n");
				output.append(a.prettyPrint() + "\n");
			}
		}
		else {
			output.append(">Atom list is empty<");
		}
		
		return output.toString();
	}

	// returns the text of the document
	public String toString() {
		StringBuilder s = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		for (Atom a: al) {
			s.append(a.at);
			if (a != al.getLast()) s.append(ls);
		}
		return s.toString();
	}
	
	// ********************** Metods indirectly called by the Server
	
	// Locks the atomId to userId
	void lockAtom(int serverId, int docId, AtomId aId, int userId) {}

	// Unlocks the atomId
	void unlockAtom(int serverId, int docId, AtomId aId) {}

	// Add an user to the userList
	void addClientToDoc(int serverId, int docId, int userId) {}

	// Remove an user from the userList
	void removeClientFromDoc(int serverId, int docId, int userId) {}
	
	// ********************** Editing the EMUDoc

	// This methods are called in response to user actions on the editing window which
	// can be of 2 forms: menu commands (cut, copy, paste, select all, ...) and
	// direct editing actions on the text window (events captured at that window).

	// Retorna o Atom posterior a a na atomList
	Atom next(Atom a) {
		// it is safer to get atoms using the id,
		// once there may exist multiple copy with the same id but only one
		// mapped
		a = getOrDieAtom(a.getAtomId());
		int index = getOrDieAtomIndex(a);
		index++;
		
		if (index < al.size()) {
			return al.get(index);
		}
		else {
		   return null;
		}
	}

	// Retorna o Atom anterior a ana AtomList
	Atom prev(Atom a) {
		int index = getOrDieAtomIndex(a);
		index--;
		
		if (index >= 0) {
			return al.get(index);
		}
		else {
			return null;
		}
	}
	
	// insert this part in the atom list at the 
	// given position
	void insertP(Part p, Pos pos) {
		
		// nothing
		if (p == null)
			return ;

		String pt_inic = p.getInic();
		String pt_final = p.getFinal();
		
		Atom start = getOrDieAtom(pos.getAtom().getAtomId());

		// remove content from the initial atom
		if (pt_inic != null) {
			
			if (pt_final != null) {
				Atom end = splitAtom(pos);
				end.insertS(0, pt_final);
			}
			
			start.insertS(pos, pt_inic);
		}
		
		ArrayList<Atom> pt_atoms = p.getAtomList();
		
		Atom prev = start;
		
		// remove all intermediate atoms
		if (pt_atoms != null) {
			for (Atom a: pt_atoms) {
				insertAtom(prev.getAtomId(), a);
				prev = a;
			}
		}
		
		// change document in the view
		String content = p.toString();
		int offset = pos2Offset(pos);
		
		try {
			System.out.printf("finally inserting: offset = %d, content = %s\n", offset, content);
			super.insertString(offset, content, null);
		}
		catch(BadLocationException e) {
			System.err.println("insertP failed with:" + e);
			System.exit(1);
		}
	}

	// remove the part from the atom list at the
	// given position
	void delP(Part p, Pos pos) {
		int lsLen = System.getProperty("line.separator").length();
		
		int offset = pos2Offset(pos);
		int length = 0;
		
		// nothing to do
		if (p == null)
			return ;
		
		String pt_inic = p.getInic();
		
		Atom start = getOrDieAtom(pos.getAtom().getAtomId());
		ArrayList<Atom> pt_atoms = p.getAtomList();
		String pt_final = p.getFinal();

		// remove content from the initial atom
		if (pt_inic != null) {
			start.removeS(pos, pt_inic);
			length += pt_inic.length();
					
			if (pt_atoms != null || pt_final != null) {
				length += lsLen;
			}
		}
		
		// remove all intermediate atoms
		if (pt_atoms != null) {
			for (Atom a: pt_atoms) {
				delAtom(a.getAtomId());
				length += a.length() + lsLen;
			}
		}
		
		// remove final portion, possible joining atoms
		if (pt_final != null) {
			length += pt_final.length();
			
			Atom end = next(start);
			assert end != null : "Impossible to join: there is no atom following " 
					+ start.getAtomId().prettyPrint();
			
			end.removePrefix(pt_final);
			if (pt_inic  != null) {
				joinAtom(start.getAtomId());
			}
		}
		
		try {
			System.out.printf("finally removing: offset = %d, len = %d\n", offset, length);
			super.remove(offset, length);
		}
		catch(BadLocationException e) {
			System.err.println("delP failed with:" + e);
			System.exit(1);
		}
	}

	// Insere o ClipBoard do usuario, Part pode ser composta pelo conteudo de varios
	// Atom e no comeco e no fim por pedacos de conteudo de Atom.
	void setClipBoard(Part p) {}

	// Remove n caracteres a frente se n>0, remove -n caracteres atras se n<0
	void del(Pos pos, int n) {}

	// guarda selecao no clipboard
	public void copy() {}

	// Seleciona um intervalo (co,cc) a (ca1,cc1). Apos o uso (ca1,cc1).
	void setSelection(Pos l) {}

	// Remove a selecao e guarda no clipboard
	public void cut() {}

	// insere conteudo do clipboard na posicao do cursor
	public void paste() {
	
	}

	// Cria um Atom e insere o antes do Atom dado.
	Atom newAtomBefore(AtomId aid) {
		return null;		// to compile ...
	}

	// Cria um Atom e insere depois do Atom dado.
	Atom newAtomAfter(AtomId aid) {
		return null;		// to compile ...
	}
	
	// insert the atom in the list after prev
	public void insertAtom(AtomId aid, Atom newAtom) {
		Atom prev = getOrDieAtom(aid);
		int index = getOrDieAtomIndex(prev);
		
		al.add(index + 1, newAtom);
		
		// keep the atom mapping updated
		// the atoms may have come from elsewhere, and obviously
		// are not mapped
		Atom.id2atom.put(newAtom.getAtomId().prettyPrint(), newAtom);
	}

	// remove do EMUDoc o Atom passado
	void delAtom(AtomId aid) {
		Atom atom = getOrDieAtom(aid);
		int index = getOrDieAtomIndex(atom);
		
		al.remove(index);
	}

	// divide o Atom a em dois Atoms (a, b) na posicao p.
	// Retorna o Atom b
	Atom splitAtom(Pos p) {
		// must search for atom id because the atom in part, in spite
		// of having the same id are a different instances
		Atom atom = getOrDieAtom(p.getAtom().getAtomId());
		int index = getOrDieAtomIndex(atom);
		
		Atom next = null;
		
		if (index < (al.size() - 1)) {
			next = al.get(index + 1);
		}
		
		Atom newAtom = atom.split(p.c, next);
		
		al.add(index+1, newAtom);

		return atom;
	}
	
	// Une dois Atom em um novo Atom que retem a AtomId de a.}
	void joinAtom(AtomId aidA) {
		Atom atomA = getOrDieAtom(aidA);
		int indexA = getOrDieAtomIndex(atomA);
		int indexB = indexA + 1;
		
		String msg = "Impossible to join atoms, there is no atom following: " +
				atomA.prettyPrint();
		assert al.size() > indexB : msg;
		
		Atom atomB = al.get(indexB);
		
		atomA.join(atomB);
		al.remove(indexB);
	}

	// Creates a copy of the
	Part copy(Selection sel) {
		return null;		// to compile ...
	}

	// Remove a Part correspondente a curSelection e NAO armazena no clipBoard.
	void del(Selection sel) {
		
	}
	

	// Remove a Part correspondente a curSelection e armazena no clipBoard.
	void cut(Selection sel) {
		
	}
	
	// Insere na curPos a Part p.
	void paste(Part p) {
		
	}

	// set the current selection.
	void setSelection(Pos p1, Pos p2) {
		cs = new Selection(p1, p2);
	}

	//
	public void selectAll() {
		// Effects: 
		// Pre: 
		// Mod: 
		setSelection(new Pos(al.getFirst(), 0), new Pos(al.getLast(), al.getLast().at.length()));
	}
}

