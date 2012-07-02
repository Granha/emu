/**
 * 
 */
package interfaces;
	
	import java.awt.BorderLayout;
	import java.awt.EventQueue;
	
import javax.swing.DefaultListModel;
	import javax.swing.JFrame;
import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
	
/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class UsersW extends JFrame {

	private JPanel contentPane;
	public  DefaultListModel listData = new DefaultListModel();
	private JList list; 
	public EMUW emuw; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UsersW frame = new UsersW();
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
	public UsersW() {
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		list = new JList(listData);
		
	
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
		 		// See if this is a listbox selection and the
		 		// event stream has settled
					// Get the current selection and place it in the
					// edit field
					String stringValue = list.getSelectedValue().toString();
					emuw.highLight(stringValue);
		} } );
		contentPane.add(list, BorderLayout.CENTER);
	}
	
	public void updateUsers()
	{
		
	}

}
