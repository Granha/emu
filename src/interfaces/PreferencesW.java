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
	import javax.swing.JComboBox;
	import javax.swing.DefaultComboBoxModel;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;

/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class PreferencesW extends JFrame {
	public JFrame prefW;
	private JPanel contentPane;
	int internalUpdateFrequency = EMU.prefferedUpdateFrequency;
	int internalFontSize = EMU.prefferedFontSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreferencesW frame = new PreferencesW();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PreferencesW() {
		setResizable(false);
		prefW = this;
		internalFontSize = EMU.prefferedFontSize;
		internalUpdateFrequency = EMU.prefferedUpdateFrequency;
		
		setTitle("EMU Preferences");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 288, 159);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFontSize = new JLabel("Preffered Font Size:");
		lblFontSize.setBounds(16, 20, 125, 16);
		contentPane.add(lblFontSize);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				internalFontSize = ((JComboBox)(e.getSource())).getSelectedIndex();
			}
		});
		comboBox.setEditable(true);
		comboBox.setMaximumRowCount(10);
		comboBox.setModel(new DefaultComboBoxModel(EMU.fontSizesS));
		comboBox.setSelectedIndex(EMU.prefferedFontSize);
		comboBox.setBounds(146, 16, 52, 27);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				internalUpdateFrequency = ((JComboBox)(e.getSource())).getSelectedIndex();
			}
		});
		comboBox_1.setEditable(true);
		comboBox_1.setMaximumRowCount(4);
		comboBox_1.setModel(new DefaultComboBoxModel(EMU.updateFreqs));
		comboBox_1.setSelectedIndex(EMU.prefferedUpdateFrequency);
		comboBox_1.setBounds(146, 55, 125, 27);
		contentPane.add(comboBox_1);
		
		JLabel lblUpdateFrequency = new JLabel("Update Frequency:");
		lblUpdateFrequency.setBounds(17, 59, 117, 16);
		contentPane.add(lblUpdateFrequency);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefW.setVisible(false);
			}
		});
		btnCancel.setBounds(47, 94, 90, 27);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EMU.prefferedUpdateFrequency = internalUpdateFrequency;
				EMU.prefferedFontSize = internalFontSize;
				prefW.setVisible(false);
			}
		});
		btnSave.setBounds(149, 94, 90, 27);
		contentPane.add(btnSave);
	}
}
