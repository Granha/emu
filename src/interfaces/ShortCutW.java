/**
 * 
 */
package interfaces;
	import java.awt.Dimension;
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.*;
	import java.awt.Rectangle;
	
/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class ShortCutW extends JFrame {

	public JScrollPane shortCutTablePane;

	// semantic data
	String[] shortCutCols = {"Kind", "Name", "ShortCut"};
	Object[][] shortCutData = {
		{"MENU", "EMU",             "Alt-M"},
		{"",     "Preferences...",  "Control-,"},
		{"",     "Login...",        "Control-L"},
		{"",     "Logout",          "Control-Alt-L"},
		{"",     "Quit",            "Control-Q"},
		{"",     "",                ""},
		{"MENU", "File",            "Alt-F"},
		{"",     "New File...",     "Control-N"},
		{"",     "Open File...",    "Control-O"},
		{"",     "New View",        "Control-Alt-N"},
		{"",     "Close Window",    "Control-W"},
		{"",     "Close All",       "Control-Alt-W"},
		{"",     "Save",            "Control-S"},
		{"",     "Save As...",      "Control-Alt-S"},
		{"",     "Print...",        "Control-P"},
		{"",     "",                ""},
		{"MENU", "Edit",            "Alt-E"},
		{"",     "Undo",            "Control-Z"},
		{"",     "Redo",            "Control-Y"},
		{"",     "Cut",             "Control-X"},
		{"",     "Copy",            "Control-C"},
		{"",     "Paste",           "Control-V"},
		{"",     "Select All",      "Control-A"},
		{"",     "Get Lock",        "Control-L"},
		{"",     "Release Lock",    "Control-Alt-L"},
		{"",     "",                ""},
		{"MENU", "View",            "Alt-V"},
		{"",     "Zoom  In",        "Control-EQUAL"},
		{"",     "Zoom Out",        "Control-MINUS"},
		{"",     "",                ""},
		{"MENU", "Search",          "Alt-S"},
		{"",     "Find...",         "Control-F"},
		{"",     "Find Next",       "Control-G"},
		{"",     "Find Prev",       "Control-Alt-P"},
		{"",     "Replace",         "Control-R"},
		{"",     "Replace & Find",  "Control-Alt-R"},
		{"",     "Replace All",     "Control-Shift-R"},
		{"",     "",                ""},
		{"MENU", "Windows",         "Alt-W"},
		{"",     "Minimize",        "Control-M"},
		{"",     "Hide",            "Control-H"},
		{"",     "Hide Others",     "Control-Alt-H"},
		{"",     "Show/Hide Users", "Control-U"},
		{"",     "Doc1",            "Control-1"},
		{"",     "Doc2",            "Control-2"},
		{"",     "Doc3",            "Control-3"},
		{"",     "",                ""},
		{"MENU", "Help",            "Alt-H\n"}
	};


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShortCutW frame = new ShortCutW();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShortCutW() {
		final JFrame shortCutFrame = new JFrame();
		shortCutFrame.setBounds(new Rectangle(0, 22, 300, 800));
		shortCutFrame.setTitle("EMU Shortcuts");
		final JTable shortCutTable = new JTable(shortCutData, shortCutCols);
		shortCutTable.setPreferredScrollableViewportSize(new Dimension(400, 800));
		shortCutTable.setFillsViewportHeight(true);
		shortCutTablePane = new JScrollPane(shortCutTable);
		shortCutFrame.getContentPane().add(shortCutTablePane);
		//cw.addStatusMessage("ShortCut created\n");
	}

}

