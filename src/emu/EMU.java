package emu;
	
	import java.awt.Dimension;
	import java.awt.Toolkit;
	import java.util.ArrayList;
	import java.util.LinkedList;
	import javax.swing.JFrame;
	import javax.swing.UIManager;
	
import abstractions.CommandC;
	import abstractions.EMUDoc;
	import abstractions.User;
	import interfaces.EMUW;
	import interfaces.FileChooserW;
	import interfaces.HelpW;
	import interfaces.LoginW;
	import interfaces.NewFileW;
	import interfaces.PreferencesW;
import interfaces.ShortCutW;

import java.io.*;


public class EMU {
	// ********** General constants

	// Interface constants
	public final static int winX = 20;
	public final static int winY = 50;
	public final static int winHeight = 600;
	public final static int winWidth = 500;
	public final static int textHeight = 60;
	public final static int textWidth = 80;
	public final static int statusHeight = 4;
	public final static int statusWidth = 80;

	public final static int usersWidth = 100;
	public final static int usersHeight = 200;

	public static Toolkit kit = Toolkit.getDefaultToolkit();
	public static Dimension screenSize = kit.getScreenSize();
	public final static int screenWidth = screenSize.width;
	public final static int screenHeight = screenSize.height;

	// handle fonts
	public final static int[] fontSizes = {10, 11, 12, 14, 18, 24, 28, 32, 36, 40};
	public static final String[] fontSizesS = 
		{"10", "11", "12", "14", "18", "24", "28", "32", "36", "40"};
	public static final int fontSizeDefault = 3;		// default: font size 14

	// Local Editor constants
	// update frequency
	public static final String[] updateFreqs = 
		{"Same as Server", "1/2 of Server", "1/4 of Server", "1/8 of Server"};
	public static final int updateFreqDefault = 1;		// default: 1/2 of Server
	public static int prefferedUpdateFrequency = updateFreqDefault;

	// Server Constants 


	// ********* Globals
	public static ArrayList<String> serverNames = new ArrayList<String>();
	public static ArrayList<String> serverIP = new ArrayList<String>();

	// User
	public static User curUser;

	// current path for files
	public static String curLocalPath;
	public static String curRemotePath;

	// fonts for viewing the document
	public static int prefferedFontSize = fontSizeDefault;	// initial value is default: font size 12
	public final static int fontSize = prefferedFontSize;	// initial value is default: font size 12

	public static int prefUpdateFreq = updateFreqDefault;
	public static int curUpdateFreq = prefUpdateFreq;

	public static EMUW		cw;		// the EMU window with focus (current window)
	public static EMUDoc	cd;		// the document within the current window

	// application wide windows (a single window per application)
	// (create but does not show them)
	public static FileChooserW fileChooserW;
	public static HelpW helpW;
	public static LoginW loginW;
	public static NewFileW newFileW;
	public static PreferencesW prefsW;
	//public static PrintFileW    printFileW;
	//public static pageSetupW    pageSetupW;
	public static ShortCutW shortCutW;
	// ??
	// list of all open windows and documents
	public static LinkedList<EMUW> winList = new LinkedList<EMUW>();
	public static LinkedList<EMUDoc> docList = new LinkedList<EMUDoc>();
	// User info
	public static String logName = new String();
	public static char[] password;
	// Users (other Users editing the same doc set as the current User)
	public static LinkedList<User> userList = new LinkedList<User>();
	//
	public static EMU emu;

	EMU() {
		// dynamic initializations 
		serverNames.add("A-HAND EMU Server");
		serverIP.add("143.106.25.1");

		// create and show the first EMU Window
		cw = new EMUW(winX, winY, 60, textWidth, null);
		System.out.printf("After new EMUW\n");
		winList.add(cw);
		System.out.printf("After winList.add\n");

		// application wide windows (a single window per application)
		// (create but does not show them)
		fileChooserW = new FileChooserW();
		helpW = new HelpW();
		loginW = new LoginW();
		newFileW = new NewFileW();
		prefsW = new PreferencesW();
		//printFileW = new PrintFileW();
		shortCutW = new ShortCutW();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			javax.swing.SwingUtilities.updateComponentTreeUI(cw);
		} catch (Exception e) {
			System.err.println("Look and Feel not set.");
		}
		System.out.printf("After setlookandfeel\n");
		cw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		cw.pack();
		cw.setVisible(true);
		System.out.printf("After setVisible\n");
	}
	
	private static ArrayList<String> readFile(String filePath) {
		try {
			ArrayList<String> paragraphs = new ArrayList<String>();
			BufferedReader reader = new BufferedReader( new FileReader (filePath));
			String         line = null;

			while( ( line = reader.readLine() ) != null ) {
				paragraphs.add(line);
			}

			reader.close();
			
			return paragraphs;
		}
		catch (IOException e) {
			System.err.println("Failed to open file: " + filePath);
		} 
		  
		return null;		
	}
	
	private static String rawContent(ArrayList<String> paragraphs) {
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		for (String p: paragraphs) {
	        stringBuilder.append(p);
	        stringBuilder.append(ls);			
		}
		
		 return stringBuilder.toString();
	}
	
	private static String rawContent(String filePath) {
		return rawContent(readFile(filePath));
	}
	
	public static void openDoc(EMUW win, String pathName, boolean isRemote) {
		String text = null;
		// prof. cd = new EMUDoc(pathName, isRemote);
		
		// open document file and get its contents in text
		if (!isRemote) {
			text = rawContent(pathName);
		}
		else{
			System.err.println("Remote files are not handled yet");
		}

		if (text != null) {
			System.out.print(text);
			cd = new EMUDoc(text);
			docList.add(cd);
			win.setDocument(cd);
		}
		else {
			System.err.println("Impossible to open document");
			System.exit(1);
		}
	}

	public void closeDoc(EMUDoc doc) {
		int i;

		// remove doc from the docList
		for (i = 0; i < docList.size(); i++)
			if (docList.get(i) == doc)
				docList.remove(i);

		// remove doc from all windows it is being viewed
		for (i = 0; i < doc.winList.size(); i++)
			winList.get(i).cd = null;

		// tells the server doc is not being edited any more
		// remote call to void closeDoc(String client, int userId, int docId)

	}

	// returns a names of the open docs (String[])
	public static String[] getDocsList() {
		System.out.printf("docList.size():%d\n", docList.size());
		String[] docNames = new String[docList.size()];
		int i = 0;
		System.out.printf("docList.size():%d\n", docList.size());
		for (EMUDoc d : docList) {
			docNames[i++] = d.fileName;
		}
		return docNames;
	}

	public static int curDocIndex() {
		return 0;
	}

	private static void startEMU() {
		emu = new EMU();
	}

	
	// ********************** Methods called by the Server
	
	// Update the docId with the Commands issued by other clients 
	void updateDoc(int serverId, int docId, CommandC[][] mCmds) {}

	void updateServerFreq(int serverId, int serverFreq) {}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				// connect with the server

				// creates the thread pool for server edits

				// init the first EMU Window
				startEMU();
			}
		});
	}
}
