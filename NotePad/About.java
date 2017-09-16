import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class About extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea txtrAutoSave;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// get's native system look and feel for the application so it looks natively on
	// all platforms
	private void getNativeLook() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exceptionLookAndFeel) {
			exceptionLookAndFeel.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occured while loading native look and feel: " + exceptionLookAndFeel);
		}
	}

	public About() {
		setResizable(false);
		getNativeLook();
		setTitle("MyNotes v1.0 - About");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 435);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(null, "General Information", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Verdana Pro Light", Font.PLAIN, 15), Color.BLACK));
		panel.setBounds(12, 13, 460, 104);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBounds(12, 27, 436, 64);
		panel.add(scrollPane);
		
		JTextArea txtrQww = new JTextArea();
		txtrQww.setBorder(null);
		txtrQww.setLineWrap(true);
		txtrQww.setEditable(false);
		txtrQww.setText("MyNotes - is a mini note pad that allows to store   information in separate stickers. ");
		txtrQww.setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		scrollPane.setViewportView(txtrQww);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Functionality", TitledBorder.LEADING, TitledBorder.TOP,

						new Font("Verdana Pro Light", Font.PLAIN, 15), Color.BLACK));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(12, 130, 460, 257);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(null);
		scrollPane_1.setBounds(12, 27, 436, 217);
		panel_1.add(scrollPane_1);
		
		txtrAutoSave = new JTextArea();
		txtrAutoSave.setText("Auto Save - saves information in a sticker auto - matically when user click \"Back\" button or on  application closing. Prevent to lose information.\r\n\r\nWarn Before Delete - shows a confirmation win - dow before deleting. Helps to prevent  an acc -  idental deleting. \r\n\r\nNote: all stickers must have unique names. \r\n\r\nCurrent version: MyNotes  v1.0\r\nMykola GOLOVAN");
		txtrAutoSave.setBorder(null);
		scrollPane_1.setViewportView(txtrAutoSave);
		txtrAutoSave.setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		txtrAutoSave.setLineWrap(true);
		txtrAutoSave.select(0, 0);
	}
}
