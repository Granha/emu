/**
 * 
 */
package abstractions;
	
	import java.awt.EventQueue;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JLabel;
	import java.awt.Font;
	import javax.swing.JTextArea;
	import javax.swing.JSeparator;
	import javax.swing.JCheckBox;
	import javax.swing.SwingConstants;
	import java.awt.Color;
	import javax.swing.BorderFactory;
	import javax.swing.JScrollPane;


/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class DebugW extends JFrame {
	private JPanel contentPane;
	private JTextArea atomList;
	private JTextArea history;
	JCheckBox allowAddUser;
	JCheckBox allowRemoveUser;	
	JCheckBox allowRemoveDocument;	
	JCheckBox allowAddDocument;	
	JCheckBox allowServerLoad;	
	JCheckBox allowUpdateFrequency;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebugW frame = new DebugW();
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
	public DebugW() {
		setResizable(false);
		setTitle("EMU Debug");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//Main Frame
		setBounds(100, 100, 740, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAddUser = new JLabel("Document Internals");
		lblAddUser.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblAddUser.setBounds(22, 19, 200, 16);
		contentPane.add(lblAddUser);

		atomList = new JTextArea();
		atomList.setEditable(false);
		atomList.setBounds(22, 64, 300, 1010);
		contentPane.add(atomList);
		atomList.setColumns(50);
		atomList.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.gray));
		atomList.setRows(50);
		
		JScrollPane scroll = new JScrollPane(atomList);
		scroll.setBounds(22,64,350,400);
		contentPane.add(scroll);


		JLabel lblDocumentManagement = new JLabel("History");
		lblDocumentManagement.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblDocumentManagement.setBounds(390, 19, 184, 16);
		contentPane.add(lblDocumentManagement);
		
		history = new JTextArea();
		history.setEditable(false);
		history.setColumns(50);
		history.setRows(50);
		history.setBounds(390, 64, 320, 1010);
		history.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.gray));
		contentPane.add(history);
		
		JScrollPane scroll2 = new JScrollPane(history);
		scroll2.setBounds(390,64,330,400);
		contentPane.add(scroll2);
		
		JSeparator vertical = new JSeparator();
		vertical.setOrientation(SwingConstants.VERTICAL);
		vertical.setForeground(new Color(154, 154, 154));
		vertical.setBounds(378, 6, 12, 537);
		contentPane.add(vertical);
	}
	
	void setAtomList(String text) {
		atomList.setText(text);
	}
	
	void setHistory(String text) {
		history.setText(text);
		history.setCaretPosition(0);
	}
}
