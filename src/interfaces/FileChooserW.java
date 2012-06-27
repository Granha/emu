/**
 * 
 */
package interfaces;

import emu.EMU;

import abstractions.EMUDoc;
import static emu.EMU.*;
	
	import java.awt.BorderLayout;
import java.awt.Color;
	import java.awt.EventQueue;
	
import javax.swing.BorderFactory;
	import javax.swing.JFrame;
	import javax.swing.JList;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JCheckBox;
	import javax.swing.SwingConstants;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	import java.io.File;
	
	import javax.swing.BoxLayout;
	import javax.swing.JFileChooser;
	import javax.swing.JScrollPane;
	
/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class FileChooserW extends JFrame {
	public boolean localFile = false;   
	private JPanel contentPane;
	public JPanel panel;
	// >>> use VFSJFileChooser: http://vfsjfilechooser.sourceforge.net/tutorial.html
	// >>> it supports open/save of local/remote files
	public FileChooserW frame;
	JButton addButton = new JButton("Open");
	//Declarar aqui a lista de arquivos a serem lidos
	String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig", "Bird", "Cat", "Dog", "Rabbit", "Pig" , "Bird", "Cat", "Dog", "Rabbit", "Pig", "Bird", "Cat", "Dog", "Rabbit", "Pig","Bird", "Cat", "Dog", "Rabbit", "Pig", "Bird", "Cat", "Dog", "Rabbit", "Pig" };
	JList petList = new JList(petStrings);

	
	JScrollPane scroll = new JScrollPane(petList);

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FileChooserW frame = new FileChooserW();
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
	public FileChooserW() {
		frame = this;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 482, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Local file");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					localFile = !localFile;
					
					if(!localFile) {
						
						// handle remote file
					    System.out.println("batata");
						petList.setBounds(22,64,300,300);
						petList.setSize(300, 10000);
						scroll.setBounds(22,64,300,300);//300, 300
					}
			}
		});
		chckbxNewCheckBox.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chckbxNewCheckBox);

		JButton btnNewButton = new JButton("Browse Files");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Clickei em open file");

				if (localFile) {
					// >>> use VFSJFileChooser: http://vfsjfilechooser.sourceforge.net/tutorial.html
					// >>> it supports open/save of local/remote files
					JFileChooser fileChooser = new JFileChooser();
					contentPane.add(fileChooser, BorderLayout.CENTER);
					contentPane.remove(scroll);
					panel.remove(addButton);
					frame.setVisible(false);
					frame.setVisible(true);
					@SuppressWarnings("unused")
					File file;
					if (fileChooser.showOpenDialog(FileChooserW.this) == JFileChooser.APPROVE_OPTION) {
						file = fileChooser.getSelectedFile();
					    System.out.println("File choosen found: " + file);
					    openDoc(cw, file.toString(), !localFile);
					}


				} else {

					// handle remote file
				    System.out.println("batata");
					petList.setBounds(22,64,300,300);
					petList.setSize(300, 10000);
					scroll.setBounds(22,64,300,300);
					
					addButton.addActionListener(new ActionListener() {
					      public void actionPerformed(ActionEvent e) {
							    System.out.println(petList.getSelectedValue());
							    //openDoc(cw, petList.getSelectedValue(), !localFile);
					      }
					    });
					
					panel.add(addButton);
					contentPane.add(scroll);
					frame.setVisible(true);
				}
			}
		});
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Save File");
		panel.add(btnNewButton_1);

	}



}