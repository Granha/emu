/**
 * 
 */
package interfaces;
	
	import java.awt.BorderLayout;
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JEditorPane;
	import javax.swing.JComboBox;
	import javax.swing.DefaultComboBoxModel;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	import java.io.IOException;

/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class HelpW extends JFrame {
	public int helpTopic = 0;
	final String[] topics = { "Introduction", "Preferences", "Managing Documents", 
			"Editing Window", "Search and Replace", "Regular Expressions",
			"Server Setup", "Server Monitor"};
	public JEditorPane helpPane;	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpW frame = new HelpW();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HelpW() {
		setTitle("EMU Help");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 640, 883);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		helpPane = new JEditorPane();
		contentPane.add(helpPane, BorderLayout.CENTER);
		
		JComboBox comboBox = new JComboBox();
		contentPane.add(comboBox, BorderLayout.NORTH);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox source = (JComboBox)e.getSource();
				helpTopic = source.getSelectedIndex();
				setTopic(helpTopic);
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(topics));
		comboBox.setSelectedIndex(helpTopic);
		//helpPane.setColumnHeaderView(comboBox);
	}


	void setTopic(int topic) {
		String fileName = "help/" + topics[topic] + ".html";
		System.out.printf("\nFile: %s", fileName);		
		java.net.URL helpURL = HelpW.class.getResource(fileName);
		if (helpURL != null) {
			try {
				helpPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else
			System.err.println("Couldn't find file: help.html");
	}

}
