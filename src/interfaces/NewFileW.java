/**
 * 
 */
package interfaces;
	
	import java.awt.BorderLayout;
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JFileChooser;
	import javax.swing.JCheckBox;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	
/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class NewFileW extends JFrame {
	private JPanel contentPane;
	public boolean isLocal = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewFileW frame = new NewFileW();
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
	public NewFileW() {
		super("New File");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// >>> use VFSJFileChooser: http://vfsjfilechooser.sourceforge.net/tutorial.html
		// >>> it supports open/save of local/remote files
		JFileChooser fileChooser = new JFileChooser();
		contentPane.add(fileChooser, BorderLayout.CENTER);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Remote FIle");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isLocal = !isLocal;
			}
		});
		contentPane.add(chckbxNewCheckBox, BorderLayout.NORTH);
	}

}
