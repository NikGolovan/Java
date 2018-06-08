import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class ForgotPassword extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";
	private JTextField txtLogin;
	private JTextField txtAnswer;
	private JComboBox<String> cmbQuestion;
	private JTextField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForgotPassword frame = new ForgotPassword();
					frame.setVisible(true);
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
	public ForgotPassword() {
		setSystemLookAndFeel();
		setTitle("Payment Manegement - Forgot Password");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 495, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(12, 13, 465, 76);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblIfYouForgot = new JLabel("If you forgot your password, write down your login, answer the");
		lblIfYouForgot.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		lblIfYouForgot.setBounds(12, 13, 441, 20);
		panel.add(lblIfYouForgot);

		JLabel lblSecretQuestionAnd = new JLabel("secret question and click on \"Restore\".");
		lblSecretQuestionAnd.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		lblSecretQuestionAnd.setBounds(12, 46, 441, 20);
		panel.add(lblSecretQuestionAnd);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblLogin.setBounds(12, 113, 56, 27);
		contentPane.add(lblLogin);

		JLabel lblSecretQuestion = new JLabel("Secret question:");
		lblSecretQuestion.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSecretQuestion.setBounds(12, 153, 125, 27);
		contentPane.add(lblSecretQuestion);

		JLabel lblAnswer = new JLabel("Answer:");
		lblAnswer.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblAnswer.setBounds(12, 193, 69, 27);
		contentPane.add(lblAnswer);

		JButton btnRestore = new JButton("Restore");
		btnRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fieldsAreEmpty())
					JOptionPane.showMessageDialog(null, "Fields are empty");
				else
					restorePassword();
			}
		});
		btnRestore.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnRestore.setBounds(380, 286, 97, 25);
		contentPane.add(btnRestore);

		JButton btnDismiss = new JButton("Dismiss");
		btnDismiss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnDismiss.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnDismiss.setBounds(265, 286, 97, 25);
		contentPane.add(btnDismiss);

		txtLogin = new JTextField();
		txtLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtLogin.setBounds(151, 117, 169, 26);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);

		txtAnswer = new JTextField();
		txtAnswer.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtAnswer.setColumns(10);
		txtAnswer.setBounds(151, 194, 169, 26);
		contentPane.add(txtAnswer);

		cmbQuestion = new JComboBox<String>();
		cmbQuestion
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Question A", "Question B", "Question C" }));
		cmbQuestion.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		cmbQuestion.setBounds(149, 157, 171, 22);
		contentPane.add(cmbQuestion);

		JLabel lblYourPasswordIs = new JLabel("Your password is:");
		lblYourPasswordIs.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblYourPasswordIs.setBounds(12, 235, 125, 27);
		contentPane.add(lblYourPasswordIs);

		txtPassword = new JTextField();
		txtPassword.setEditable(false);
		txtPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtPassword.setColumns(10);
		txtPassword.setBounds(151, 233, 169, 26);
		contentPane.add(txtPassword);
	}

	private void restorePassword() {
		String login = txtLogin.getText().toString();
		String answer = txtAnswer.getText().toString();
		String database = "SELECT * FROM Administrators WHERE Name LIKE " + "'" + login + "'";

		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver. Error details: " + e);
		}

		if (!isAdministratorExists()) {
			JOptionPane.showMessageDialog(null, "Given login does not exist in database");
			return;
		}
		
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(database)) {

			if (resultSet.next()) {
				if (resultSet.getString(4).equals(cmbQuestion.getSelectedItem().toString())
						&& resultSet.getString(5).equals(answer))
					txtPassword.setText(resultSet.getString(3));
				else
					JOptionPane.showMessageDialog(null, "Question or answer is incorrect");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isAdministratorExists() {
		String login = txtLogin.getText().toString();
		String database = "SELECT Name FROM Administrators WHERE Name LIKE " + "'" + login + "'";

		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver. Error details: " + e);
		}

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(database)) {

			if (resultSet.next())
					return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean fieldsAreEmpty() {
		return (txtLogin.getText().isEmpty() || txtAnswer.getText().isEmpty());
	}
}
