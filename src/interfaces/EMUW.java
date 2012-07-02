/**
 * @author rdru
 * INV: 
 */

package interfaces;

	import java.awt.BorderLayout;
import java.awt.Color;
	import java.awt.Dialog;
import java.awt.Event;
	import java.awt.EventQueue;
	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.InputEvent;
	import java.awt.event.KeyEvent;
	
import javax.swing.DefaultListModel;
	import javax.swing.JDialog;
	import javax.swing.JFrame;
	import javax.swing.JMenu;
	import javax.swing.JMenuBar;
	import javax.swing.JMenuItem;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JSeparator;
	import javax.swing.JSplitPane;
	import javax.swing.JTextPane;
	import javax.swing.KeyStroke;
	import javax.swing.ScrollPaneConstants;
	import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
	
	import emu.EMU;
	
	import abstractions.EMUDoc;
	import static emu.EMU.*;
	import java.awt.Dimension;
	import javax.swing.JTextArea;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;


@SuppressWarnings("serial")
public class EMUW extends JFrame {
	private JPanel contentPane;

	// window attributes
	public int x, y;
	public int height, width;
	public boolean isVisible;
	public boolean isTop;
	
	public JScrollPane textPane;
	public JTextPane  status;
	public final JTextArea text;	
	
	// EMUW internal windows
    public FindW findW;
    public UsersW usersW;

    // semantic data
    public EMUDoc cd;			// Doc being viwed at current window
    public int curDocId;

    // handle fonts
    public int curFontSize = 3;		// initial value is default: font size 12
	
	public EMUW() {
        this(winX, winY, winHeight, winWidth, null);   
        cd = new EMUDoc();
        //text.setText("asdasdasdsdas 4444444 asdasdsd 6666 8888 11111 sssss 666 888888 222 asdasd 888\n 222 3333 999 asd 3333");

	}

    // standard position and sizes and a EMUDoc associated
    public EMUW(EMUDoc doc) {
        this(winX, winY, winHeight, winWidth, doc);
    }

	/**
	 * Create the frame.
	 */
	public EMUW(int x, int y, int height, int width, EMUDoc doc) {
		
		if (doc != null)
			cd = doc;
		else
			cd = new EMUDoc(" ");
		
		cw = this;
		int CMASK = InputEvent.CTRL_MASK;
		int AMASK = InputEvent.ALT_MASK;
		int SMASK = InputEvent.SHIFT_MASK;
		
		setMinimumSize(new Dimension(400, 200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("EMU - Multiuser Editor");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("EMU");
		mnNewMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// close all documents
				
				// logout from server
				
				System.exit(0);
			}
		});
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmAboutEmu = new JMenuItem("About EMU");
		mntmAboutEmu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(getContentPane(),
	                    "EMU 1.1\nMulti-User Editor\n"
	                    + "2012 Rogerio Drummond\nIC-Unicamp",
	                    "About EMU", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu.add(mntmAboutEmu);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences");
		mntmPreferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefsW.setVisible(true);
			}
		});
		mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, CMASK));
		mnNewMenu.add(mntmPreferences);
		
		JSeparator separator_1 = new JSeparator();
		mnNewMenu.add(separator_1);
		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginW.setVisible(true);
			}
		});
		mntmLogin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CMASK));
		mnNewMenu.add(mntmLogin);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// sends all recent comands to the Server
				
				// logout user
			}
		});
		mntmLogout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CMASK | AMASK));
		mnNewMenu.add(mntmLogout);
		
		JSeparator separator_2 = new JSeparator();
		mnNewMenu.add(separator_2);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// close all open documents
				
				System.exit(0);
			}
		});
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, CMASK));
		mnNewMenu.add(mntmQuit);
		
		JMenu mnFile = new JMenu("FIle");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New File");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newFileW == null)
					newFileW = new NewFileW();
				newFileW.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				newFileW.setVisible(true);
			}
		});
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, CMASK));
		mnFile.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Open & Save File");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("File chooser here");
				fileChooserW.setVisible(true);
			}
		});
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, CMASK));
		mnFile.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Close File");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cd.closeDoc();
			}
		});
		mntmNewMenuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CMASK));
		mnFile.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Close All");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (EMUDoc d: EMU.docList) {
					d.closeDoc();
				}
			}
		});
		mntmNewMenuItem_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CMASK | AMASK));
		mnFile.add(mntmNewMenuItem_3);
		
		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Print");
		mntmNewMenuItem_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, CMASK));
		mnFile.add(mntmNewMenuItem_4);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assert cd != null : "Cannot undo: current document does not exist";
				cd.undo();
				cd.h.undo();
			}
		});
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, CMASK));
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Redo");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assert cd != null : "Cannot redo: current document does not exist";
				cd.redo();
				cd.h.redo();
			}
		});
		mntmNewMenuItem_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, CMASK));
		mnEdit.add(mntmNewMenuItem_5);
		
		JSeparator separator_4 = new JSeparator();
		mnEdit.add(separator_4);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assert cd != null : "Cannot cut: current document does not exist";
				cd.cut();
			}
		});
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, CMASK));
		mnEdit.add(mntmCut);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assert cd != null : "Cannot copy: current document does not exist";
				cd.copy();
			}
		});
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, CMASK));
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assert cd != null : "Cannot paste: current document does not exist";
				cd.paste();
			}
		});
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, CMASK));
		mnEdit.add(mntmPaste);
		
		JSeparator separator_5 = new JSeparator();
		mnEdit.add(separator_5);
		
		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cd.selectAll();
			}
		});
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, CMASK));
		mnEdit.add(mntmSelectAll);
		
		JSeparator separator_6 = new JSeparator();
		mnEdit.add(separator_6);
		
		JMenuItem mntmGetLock = new JMenuItem("Get Lock");
		mntmGetLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(cw, "Wait while server process the Lock request ...", 
						Dialog.ModalityType.DOCUMENT_MODAL);
				// >>> request the locks on the current selection and wait server response
				@SuppressWarnings("unused")
				JDialog confirm = new JDialog(cw, "Confirma??");
				//d.setVisible(false);
				}
		});
		mntmGetLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CMASK));
		mnEdit.add(mntmGetLock);
		
		JMenuItem mntmReleaseLock = new JMenuItem("Release Lock");
		mntmReleaseLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(cw, "Requesting release locks to the server.\n"
					+ "Please wait ...", Dialog.ModalityType.DOCUMENT_MODAL);
				// >>> request the locks on the current selection and wait server response
				@SuppressWarnings("unused")
				JDialog confirm = new JDialog(cw, "Confirma??");
				//d.setVisible(false);
			}
		});
		mntmReleaseLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CMASK | AMASK));
		mnEdit.add(mntmReleaseLock);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Zoom In");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (curFontSize < fontSizes.length - 2) {
	                curFontSize++;
	                text.setFont(new Font("MONOSPACED", Font.PLAIN, fontSizes[curFontSize]));
	            }
			}
		});
		mntmNewMenuItem_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, CMASK));
		mnView.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Zoom Out");
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (curFontSize > 0) {
	                curFontSize--;
	                text.setFont(new Font("MONOSPACED", Font.PLAIN, fontSizes[curFontSize]));
	            }
			}
		});
		mntmNewMenuItem_7.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, CMASK));
		mnView.add(mntmNewMenuItem_7);
		
		JMenu mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);
		
		JMenuItem mntmFind = new JMenuItem("Find...");
		mntmFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (findW == null)
					findW = new FindW();
				findW.emuw =  EMUW.this;
				findW.setVisible(true);
			}
		});
		mntmFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, CMASK));
		mnSearch.add(mntmFind);
		
		JMenuItem mntmFindNext = new JMenuItem("Find Next");
		mntmFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, CMASK));
		mnSearch.add(mntmFindNext);
		
		JMenuItem mntmFindPrev = new JMenuItem("Find Prev");
		mntmFindPrev.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, CMASK | AMASK));
		mnSearch.add(mntmFindPrev);
		
		JMenuItem mntmReplace = new JMenuItem("Replace");
		mntmReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, CMASK));
		mnSearch.add(mntmReplace);
		
		JMenuItem mntmReplaceFind = new JMenuItem("Replace & Find");
		mntmReplaceFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, CMASK | AMASK));
		mnSearch.add(mntmReplaceFind);
		
		JMenuItem mntmReplaceAll = new JMenuItem("Replace All");
		mntmReplaceAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, AMASK | SMASK));
		mnSearch.add(mntmReplaceAll);
		
		JMenu mnWindows = new JMenu("Windows");
		menuBar.add(mnWindows);
		
		JMenuItem mntmNewView = new JMenuItem("New View");
		mntmNewView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, CMASK | AMASK));
		mnWindows.add(mntmNewView);
		
		JMenuItem mntmCloseWindow = new JMenuItem("Close Window");
		mntmCloseWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CMASK));
		mnWindows.add(mntmCloseWindow);
		
		JSeparator separator_7 = new JSeparator();
		mnWindows.add(separator_7);
		
		JMenuItem mntmMinimize = new JMenuItem("Minimize");
		mntmMinimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, CMASK));
		mnWindows.add(mntmMinimize);
		
		JMenuItem mntmHide = new JMenuItem("Hide");
		mntmHide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, CMASK));
		mnWindows.add(mntmHide);
		
		JMenuItem mntmHideOthers = new JMenuItem("Hide Others");
		mntmHideOthers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, CMASK | AMASK));
		mnWindows.add(mntmHideOthers);
		
		JMenuItem mntmShowAll = new JMenuItem("Show All");
		mnWindows.add(mntmShowAll);
		
		JSeparator separator_8 = new JSeparator();
		mnWindows.add(separator_8);
		
		JMenuItem mntmShowhideUsers = new JMenuItem("Show/Hide Users");
		mntmShowhideUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create a list to be view in the user list
				if (usersW == null)
					usersW = new UsersW();
				String s = cd.userList.toString();
				String ar[] = s.substring(1,s.length()-1).split(", ");
				usersW.listData.clear();
				for (int i=0; i < ar.length; i++)
					usersW.listData.addElement(ar[i]); 	
				usersW.emuw =  EMUW.this;
				usersW.setVisible(true);
			}
		});
		mntmShowhideUsers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, CMASK));
		mnWindows.add(mntmShowhideUsers);
		
		JSeparator separator_9 = new JSeparator();
		mnWindows.add(separator_9);
		
		JMenuItem mntmUser = new JMenuItem("Doc1");
		mntmUser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, CMASK));
		mnWindows.add(mntmUser);
		
		JMenuItem mntmUser_1 = new JMenuItem("Doc2");
		mntmUser_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, CMASK));
		mnWindows.add(mntmUser_1);
		
		JMenuItem mntmUser_2 = new JMenuItem("Doc3");
		mntmUser_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, CMASK));
		mnWindows.add(mntmUser_2);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmEmuHelp = new JMenuItem("EMU Help...");
		mntmEmuHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpW.setVisible(true);
			}
		});
		mnHelp.add(mntmEmuHelp);
		
		JMenuItem mntmShortcuts = new JMenuItem("Shortcuts...");
		mntmShortcuts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shortCutW.setVisible(true);
			}
		});
		mnHelp.add(mntmShortcuts);
		contentPane = new JPanel();
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				
				
				//JOptionPane.showMessageDialog(EMUW.this,new String(text.getWidth() + "" ));
				
				
				
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setMinimumSize(new Dimension(200, 600));
		splitPane.setResizeWeight(1.0);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		textPane = new JScrollPane();
		textPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		text = new JTextArea();
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ev) {
				//if (ev.getKeyCode() == Event.ENTER)
					
			}
		});
		text.setLineWrap(true);
		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				text.getHighlighter().removeAllHighlights();
			}
		});
		text.setColumns(60);
		splitPane.setTopComponent(textPane);
		text.setDocument(cd);
		
		text.setMinimumSize(new Dimension(0, 100));
		text.setBounds(new Rectangle(0, 0, 0, 400));
		textPane.setViewportView(text);
		
		status = new JTextPane();
		status.setPreferredSize(new Dimension(0, 80));
		status.setEditable(false);
		splitPane.setBottomComponent(status);
	}

	// retorna o documento corrente associado a janela
    public EMUDoc getCurrentDoc() {
        return null;		// to compile ...
    }

    // Associa o documento d a janela.
    public void setDocument(EMUDoc doc) {
		cd = doc;
		
		text.setDocument(doc);

		// no need to set isVisible and isTop, since the window must be the active one
    }

    // retorna a largura da janela
    public int getW() {
        return width;
    }

    // retorna a altura da janela
    public int getH() {
        return height;
    }

    // Atualiza largura e altura da janela.
    public void resizeWin(int w, int h) {
    }

    // Mod: currentPosition (current position)
    public void scrollLine(int nlin) {
    }

    // Mod: currentPosition (current position)
    public void scrollWin(int nwin) {
    }

    public void openWin(EMUDoc doc) {
        cw = new EMUW(winX, winY, winHeight, winWidth, doc);
        winList.add(cw);
        if (doc != null) {
            cd = doc;
            cd.winList.add(cw);
        } else {
            cd = null;
        }
    }

    public void closeWin(EMUW win) {
        // remove win from the winList
        // set new current window
    	winList.remove(win);
    }

    public void highLight(String pattern)
    {
        try {
            Highlighter hilite = text.getHighlighter();
            Document doc = text.getDocument();
            String txt = doc.getText(0, doc.getLength());
            int pos = 0;

       	    // Remove previous highlights and put only the last one
     	    hilite.removeAllHighlights();
    	    
            // Search for pattern
            while ((pos = txt.indexOf(pattern, pos)) >= 0) {
            	// Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos+pattern.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EMUW frame = new EMUW();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
