/**
 * 
 */
package interfaces;
	import emu.EMU;
	import java.awt.EventQueue;
	
import java.util.regex.Pattern;
import java.util.regex.Matcher;	
	
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
	
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Dimension;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class FindW extends JFrame {
	FindW findWindow = this;
	public int findFile = EMU.curDocIndex();  // index of the file to be searched
	public boolean useRegExps = false;
	public boolean ignoreCase = false;
	public boolean ignoreWhiteSpace = false;
	public boolean matchWholeWord = false;
	public boolean highlightAll = false;
	public EMUW emuw; 
	
	private JPanel pane;
	private int pos=-1;
    
    /**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindW frame = new FindW();
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FindW() {
		setMaximumSize(new Dimension(2147483647, 350));
		setTitle("Search");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 661, 339);
		pane = new JPanel();
		pane.setMaximumSize(new Dimension(32767, 280));
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pane);
		SpringLayout sl_pane = new SpringLayout();
		pane.setLayout(sl_pane);
		final JButton findNextBt = new JButton("Find Next");
		
		JLabel lblSearchInFile_1 = new JLabel("Search in File:");
		sl_pane.putConstraint(SpringLayout.NORTH, lblSearchInFile_1, 17, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, lblSearchInFile_1, 22, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, lblSearchInFile_1, 118, SpringLayout.WEST, pane);
		pane.add(lblSearchInFile_1);
		
		JLabel lblFind = new JLabel("Find:");
		sl_pane.putConstraint(SpringLayout.NORTH, lblFind, 45, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, lblFind, 77, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, lblFind, 118, SpringLayout.WEST, pane);
		pane.add(lblFind);
		
		JLabel lblReplace = new JLabel("Replace:");
		sl_pane.putConstraint(SpringLayout.NORTH, lblReplace, 85, SpringLayout.SOUTH, lblFind);
		sl_pane.putConstraint(SpringLayout.WEST, lblReplace, 57, SpringLayout.WEST, pane);
		pane.add(lblReplace);
		
		JLabel lblOptions = new JLabel("Options:");
		sl_pane.putConstraint(SpringLayout.NORTH, lblOptions, 240, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.SOUTH, lblOptions, 265, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, lblOptions, 57, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, lblOptions, 118, SpringLayout.WEST, pane);
		pane.add(lblOptions);
		
		final JCheckBox chckbxIgnoreCase = new JCheckBox("Ignore Case");
		chckbxIgnoreCase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ignoreCase = !ignoreCase;
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxIgnoreCase, 240, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, chckbxIgnoreCase, 126, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.SOUTH, chckbxIgnoreCase, 268, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxIgnoreCase, 240, SpringLayout.WEST, pane);
		pane.add(chckbxIgnoreCase);
		
		final JCheckBox chckbxWholeWord = new JCheckBox("Whole Word");
		chckbxWholeWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				matchWholeWord = !matchWholeWord;
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxWholeWord, 270, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, chckbxWholeWord, 126, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.SOUTH, chckbxWholeWord, 295, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxWholeWord, 254, SpringLayout.WEST, pane);
		pane.add(chckbxWholeWord);
		
		final JCheckBox chckbxIgnoreWhiteSpace = new JCheckBox("Ignore White Spaces");
		sl_pane.putConstraint(SpringLayout.WEST, chckbxIgnoreWhiteSpace, 252, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxIgnoreWhiteSpace, 420, SpringLayout.WEST, pane);
		chckbxIgnoreWhiteSpace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ignoreWhiteSpace = !ignoreWhiteSpace;
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxIgnoreWhiteSpace, 240, SpringLayout.NORTH, pane);
		pane.add(chckbxIgnoreWhiteSpace);

		final JCheckBox chckbxHighlightAll = new JCheckBox("Highlight All Results");
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxHighlightAll,240, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, chckbxHighlightAll, 446, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxHighlightAll, 620, SpringLayout.WEST, pane);
		chckbxHighlightAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highlightAll = !highlightAll;
				pos = -1;
			}
		});
		pane.add(chckbxHighlightAll);
		
		JCheckBox chckbxUseRegExps = new JCheckBox("Use Regular Expressions");
		chckbxUseRegExps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxIgnoreWhiteSpace.setEnabled(useRegExps);
				chckbxWholeWord.setEnabled(useRegExps);
				chckbxIgnoreCase.setEnabled(useRegExps);
				
				useRegExps = !useRegExps;
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxUseRegExps, 270, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, chckbxUseRegExps, 252, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.SOUTH, chckbxUseRegExps, 295, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxUseRegExps, 446, SpringLayout.WEST, pane);
		pane.add(chckbxUseRegExps);
		
		JComboBox<String[]> comboBox = new JComboBox<String[]>();
		comboBox.setModel(new DefaultComboBoxModel(EMU.getDocsList()));
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				findFile = e.getID();
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, comboBox, 13, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, comboBox, 126, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, comboBox, 254, SpringLayout.WEST, pane);
		pane.add(comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_pane.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, comboBox);
		sl_pane.putConstraint(SpringLayout.WEST, scrollPane, 11, SpringLayout.EAST, lblFind);
		sl_pane.putConstraint(SpringLayout.SOUTH, scrollPane, 93, SpringLayout.SOUTH, comboBox);
		sl_pane.putConstraint(SpringLayout.EAST, scrollPane, -160, SpringLayout.EAST, pane);
		pane.add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		sl_pane.putConstraint(SpringLayout.WEST, scrollPane_1, 129, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, lblReplace, -11, SpringLayout.WEST, scrollPane_1);
		sl_pane.putConstraint(SpringLayout.SOUTH, chckbxIgnoreWhiteSpace, 39, SpringLayout.SOUTH, scrollPane_1);
		sl_pane.putConstraint(SpringLayout.NORTH, scrollPane_1, 0, SpringLayout.NORTH, lblReplace);
		sl_pane.putConstraint(SpringLayout.SOUTH, scrollPane_1, 96, SpringLayout.SOUTH, scrollPane);
		sl_pane.putConstraint(SpringLayout.EAST, scrollPane_1, 0, SpringLayout.EAST, scrollPane);
		
		final JTextPane textfind = new JTextPane();
		scrollPane.setViewportView(textfind);
		pane.add(scrollPane_1);
		
		final JTextPane textreplace = new JTextPane();
		scrollPane_1.setViewportView(textreplace);

		findNextBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
		            Highlighter hilite = emuw.text.getHighlighter();
		            Document doc = emuw.text.getDocument();
		            String txt = doc.getText(0, doc.getLength());

		            if (txt == null)
		               return;
		            
		            String pattern = textfind.getText();

		            if (pattern == null)
			           return;
		            
		            boolean cor = true;

     		        hilite.removeAllHighlights();

     		        
		            if (pos >= txt.length() || pos == -1)
		            {
		               pos = 0;
		               emuw.text.setCaretPosition(0);
		            }

		            // Search for pattern

		            // Searches everything in upper case
		     	    if (useRegExps == false && ignoreCase == true)
		     	    {
		     	    	txt = txt.toUpperCase ();
		     	    	pattern = pattern.toUpperCase ();
		     	    }
		            		            
 		            if (ignoreWhiteSpace == true || matchWholeWord == true || useRegExps == true)
		            { 
			           int i;

			           String regex = "";
			           
			           // Just need to include \b before and after the regular expression			           
			           if (matchWholeWord == true && useRegExps == false)
			        	   regex = "\\b";
			          
			           // Example: search = "cats"
			           // regex = c[ \t]*a[ \t]*t[ \t]*s
			           
			           if (useRegExps == false)
			           {	   
				           if (ignoreWhiteSpace == true)
					           for (i = 0; i < pattern.length (); i++)
					           {
					        	  if (i != pattern.length () - 1)
					        	     regex += pattern.charAt(i) + "[ \\t]*";
					        	  else   
					        		 regex += pattern.charAt(i); 
					           }
				           else
				        	  regex += pattern; 
                       
			               if (matchWholeWord == true)
			        	      regex += "\\b";
			           }
			           else
			           {
			        	   regex = pattern.replace ("\\", "\\\\");
			        	   
			        	   //regex = regex.replace ("\\\\b", "\\b");
			        	   //regex = regex.replace ("\\\\*", "\\*");
			        	   
			        	   
			        	   System.out.println ("Regular Expression = " + regex);
			           }
			           
			           if (pattern.length () <= 0)
			        	   return;
			           
	        		   Pattern p = Pattern.compile (regex);
	
	     		       Matcher matcher = p.matcher (txt);

   	     		 	   int pos_begin = 0;
	     		  	   int pos_end = 0;	
	     		       boolean repeat;
	     		       
	    		       while (true)
	    		       {
	    		    	  repeat = matcher.find ();
	    		    	 
	    		    	  if (repeat == false)
	    		    		 break;
	    		    	 
	    		          pos_begin = matcher.start ();
	    		          pos_end   = matcher.end ();

	    		    	  if (pos > pos_begin)
	    		    		 continue;
	    		         
			        	  if (highlightAll == false)
			        	     hilite.removeAllHighlights();
	    		    	  
	    		    	  if (cor == true)
	    		    	  {		   
			        	     hilite.addHighlight(pos_begin, pos_end, new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
			        	     
			        	     cor = false;
	    		    	  }
	    		    	  else
	    		    	  {	  
	    		    		  hilite.addHighlight(pos_begin, pos_end, new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
	    		    		  cor = true;
	    		    	  }
	    		    	  
	    		    	  pos = pos_end;
				          emuw.text.setCaretPosition(pos);
				          
             	          if (highlightAll == false)
			            	 break;	   
	    		       }
	    		      
	    		       if (repeat == false)
	    		       {	  
	    		          pos = 0;
	    		    	  
			        	  if (highlightAll == false)
	    		             JOptionPane.showMessageDialog(FindW.this,new String("End of search"));
	    		       }
	    		    }
		            else
		            {
		               while (true)
		               {	   
			               pos = txt.indexOf (pattern, pos);
			               
				           if (pattern.length () <= 0)
				        	   return;
			               
				           if (pos == -1)
				           {	   
				        	  if (highlightAll == false)
				        	     JOptionPane.showMessageDialog (FindW.this,new String("End of search"));
				        	  
				              break;
				           }
				           else
				           {   
				        	   // Remove previous highlights and put only the last one
				        	   
				        	   if (highlightAll == false)
				        	      hilite.removeAllHighlights();
				        	   
		    		    	   if (cor == true)
		    		    	   {		   
					        	  // Create highlighter using private painter and apply around pattern
					        	  hilite.addHighlight(pos, pos+pattern.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
					        	  cor = false;
		    		    	   }
		    		    	   else
		    		    	   {	  
		    		    		   hilite.addHighlight(pos, pos+pattern.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
		    		    		   cor = true;
		    		    	   }
				        	   
				        	   pos += pattern.length();
					           emuw.text.setCaretPosition(pos);
				           }
				           
		            	   if (highlightAll == false)
		            	      break;	   
		               }
		            }
				} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, findNextBt, 13, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, findNextBt, -145, SpringLayout.EAST, pane);
		findNextBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// >>> find in file: docsList.get(findFile) 
			}
		});
		sl_pane.putConstraint(SpringLayout.EAST, findNextBt, -10, SpringLayout.EAST, pane);
		pane.add(findNextBt);
		
		JButton replaceBt = new JButton("Replace");
		replaceBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// >>> replace
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, replaceBt, 43, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, replaceBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, replaceBt, 0, SpringLayout.EAST, findNextBt);
		pane.add(replaceBt);
		
		JButton replaceFindBt = new JButton("Replace & Find");
		replaceFindBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// >>> replace and find
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, replaceFindBt, 73, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, replaceFindBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, replaceFindBt, -10, SpringLayout.EAST, pane);
		pane.add(replaceFindBt);
		
		JButton replaceAllBt = new JButton("Replace All");
		replaceAllBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// >>> replace all
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, replaceAllBt, 103, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, replaceAllBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, replaceAllBt, -10, SpringLayout.EAST, pane);
		pane.add(replaceAllBt);
		
		JButton helpBt = new JButton("Help");
		helpBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// >>> help find
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, helpBt, 133, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, helpBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, helpBt, -10, SpringLayout.EAST, pane);
		pane.add(helpBt);
		
		JButton closeBt = new JButton("Close");
		closeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//findWindow.setVisible(false);

          	  WindowEvent wev = new WindowEvent(FindW.this, WindowEvent.WINDOW_CLOSING);
              Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);


			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, closeBt, 163, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, closeBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, closeBt, -10, SpringLayout.EAST, pane);
		pane.add(closeBt);
	}
}
