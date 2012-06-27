/**
 * 
 */
package interfaces;
	import emu.EMU;
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JLabel;
	import javax.swing.JCheckBox;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	import javax.swing.JComboBox;
	import javax.swing.SpringLayout;
	import javax.swing.JScrollPane;
	import java.awt.event.ItemListener;
	import java.awt.event.ItemEvent;
	import javax.swing.DefaultComboBoxModel;
	import java.awt.Dimension;

/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class FindW extends JFrame {
	FindW findWindow = this;
	public int findFile = EMU.curDocIndex();				// index of the file to be searched
	public boolean useRegExps = false;
	public boolean ignoreCase = false;
	public boolean ignoreWhiteSpace = false;
	public boolean matchWholeWord = false;
	
	
	private JPanel pane;

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
		setTitle("Find and Replace");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 661, 339);
		pane = new JPanel();
		pane.setMaximumSize(new Dimension(32767, 280));
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pane);
		SpringLayout sl_pane = new SpringLayout();
		pane.setLayout(sl_pane);
		
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
		
		JCheckBox chckbxIgnoreCase = new JCheckBox("Ignore case");
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
		
		JCheckBox chckbxWholeWord = new JCheckBox("Whole word");
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
		
		JCheckBox chckbxIgnoreWhiteSpace = new JCheckBox("Ignore white space");
		sl_pane.putConstraint(SpringLayout.WEST, chckbxIgnoreWhiteSpace, 252, SpringLayout.WEST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, chckbxIgnoreWhiteSpace, 420, SpringLayout.WEST, pane);
		chckbxIgnoreWhiteSpace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ignoreWhiteSpace = !ignoreWhiteSpace;
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, chckbxIgnoreWhiteSpace, 240, SpringLayout.NORTH, pane);
		pane.add(chckbxIgnoreWhiteSpace);
		
		JCheckBox chckbxUseRegExps = new JCheckBox("Use regular expressions");
		chckbxUseRegExps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		pane.add(scrollPane_1);
		
		JButton findNextBt = new JButton("Find Next");
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
				findWindow.setVisible(false);
			}
		});
		sl_pane.putConstraint(SpringLayout.NORTH, closeBt, 163, SpringLayout.NORTH, pane);
		sl_pane.putConstraint(SpringLayout.WEST, closeBt, -145, SpringLayout.EAST, pane);
		sl_pane.putConstraint(SpringLayout.EAST, closeBt, -10, SpringLayout.EAST, pane);
		pane.add(closeBt);
	}
}
