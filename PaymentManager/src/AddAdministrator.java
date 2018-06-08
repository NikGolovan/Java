import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddAdministrator extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtTemporaryPass;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";
	String GENERATED_CODES = "generatedCodes.txt";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddAdministrator frame = new AddAdministrator();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occured while setting Native " + "System Look and Feel " + e);
		}
	}

	/**
	 * Create the frame.
	 */
	public AddAdministrator() {
		setTitle("Payment Manager - New administrator");
		setResizable(false);
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(12, 13, 450, 105);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPassThisCode = new JLabel("pass this code to new administrator. It can be changed later.");
		lblPassThisCode.setBounds(12, 75, 423, 20);
		panel.add(lblPassThisCode);
		lblPassThisCode.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		JLabel lblGeneratingNewLog = new JLabel("generating temporary login password. Click on \"generate\" and ");
		lblGeneratingNewLog.setBounds(12, 42, 423, 20);
		panel.add(lblGeneratingNewLog);
		lblGeneratingNewLog.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		JLabel lblForTheSecurity = new JLabel("For the security reason new administrator can be created by");
		lblForTheSecurity.setBounds(27, 13, 423, 16);
		panel.add(lblForTheSecurity);
		lblForTheSecurity.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		txtTemporaryPass = new JTextField();
		txtTemporaryPass.setEditable(false);
		txtTemporaryPass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtTemporaryPass.selectAll();
			}
		});
		txtTemporaryPass.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		txtTemporaryPass.setBounds(148, 146, 314, 26);
		contentPane.add(txtTemporaryPass);
		txtTemporaryPass.setColumns(10);

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String password = txtTemporaryPass.getText().toString();

				if (isFieldEmpty() && !verifyIfPasswordWasGenerated(password)) {
					btnGenerate.setEnabled(false);
					generateTmpPassword();
					memorizeGeneratedPassword();
				} else if (verifyIfPasswordWasGenerated(password)) {
					btnGenerate.setEnabled(false);
					generateTmpPassword();
					memorizeGeneratedPassword();
				} else {
					JOptionPane.showMessageDialog(null, "All possible combinations of passwords are used.");
				}
			}
		});
		btnGenerate.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnGenerate.setBounds(12, 145, 97, 27);
		contentPane.add(btnGenerate);

		JLabel lblLastGeneratedCode = new JLabel("Last generated code :");
		lblLastGeneratedCode.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		lblLastGeneratedCode.setBounds(148, 185, 166, 26);
		contentPane.add(lblLastGeneratedCode);

		JLabel lblNone = new JLabel("None");
		lblNone.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		lblNone.setBounds(307, 185, 82, 26);
		contentPane.add(lblNone);
	}

	/* Method verifies if all fields are filled */
	private boolean isFieldEmpty() {
		return (txtTemporaryPass.getText().length() == 0);
	}

	/*
	 * Generates random password for new administrators to log in then adds it to
	 * database. This password can be changed later. TODO : verify if it does not
	 * generate same codes.
	 */
	private void generateTmpPassword() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		PreparedStatement preparedStatement = null;

		while (salt.length() < 5) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();

		txtTemporaryPass.setText(saltStr);

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Administrators (Name, Password, Question, Answer, hasRootPermission) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, saltStr);
			preparedStatement.setString(2, saltStr);
			preparedStatement.setString(3, "");
			preparedStatement.setString(4, "");
			preparedStatement.setString(5, "No");

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
		}
	}

	private void memorizeGeneratedPassword() {
		String generatedPassword = txtTemporaryPass.getText().toString();

		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(GENERATED_CODES, true), "utf-8"))) {

			writer.write(generatedPassword);
			writer.write("\n");

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing generated codes : " + e);
		}
	}

	private boolean verifyIfPasswordWasGenerated(String password) {
		try (BufferedReader br = new BufferedReader(new FileReader(GENERATED_CODES))) {

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.equals(password))
					return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
