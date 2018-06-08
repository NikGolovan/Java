import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

public class ChangeRootSettings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNewLogin;
	private JPasswordField txtNewPassword;
	private JPasswordField txtNewPasswordRepeat;
	private JTextField txtResponse;
	private JComboBox<String> cmbSecretQuestion;
	private JCheckBox chShowPasswords;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeRootSettings frame = new ChangeRootSettings();
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
	public ChangeRootSettings() {
		setTitle("Payment Manager - Change Root Settings");
		setResizable(false);
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(12, 13, 500, 75);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblYouHaveBeen = new JLabel("You have been given default login and password, here you can change");
		lblYouHaveBeen.setBounds(12, 13, 476, 20);
		panel.add(lblYouHaveBeen);
		lblYouHaveBeen.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		JLabel lblItToCustomized = new JLabel("it to customized.");
		lblItToCustomized.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		lblItToCustomized.setBounds(12, 46, 133, 16);
		panel.add(lblItToCustomized);

		JLabel lblNewLogin = new JLabel("New login:");
		lblNewLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblNewLogin.setBounds(12, 114, 103, 26);
		contentPane.add(lblNewLogin);

		JLabel lblNewPassword = new JLabel("New password:");
		lblNewPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblNewPassword.setBounds(12, 147, 121, 26);
		contentPane.add(lblNewPassword);

		txtNewLogin = new JTextField();
		txtNewLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtNewLogin.setBounds(208, 114, 160, 26);
		contentPane.add(txtNewLogin);
		txtNewLogin.setColumns(10);

		txtNewPassword = new JPasswordField();
		txtNewPassword.setBounds(208, 149, 162, 26);
		txtNewPassword.setEchoChar('*');
		contentPane.add(txtNewPassword);

		JLabel lblNewPasswordrepeat = new JLabel("New password (repeat):");
		lblNewPasswordrepeat.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblNewPasswordrepeat.setBounds(12, 186, 182, 26);
		contentPane.add(lblNewPasswordrepeat);

		txtNewPasswordRepeat = new JPasswordField();
		txtNewPasswordRepeat.setBounds(208, 188, 162, 26);
		txtNewPasswordRepeat.setEchoChar('*');
		contentPane.add(txtNewPasswordRepeat);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fieldsAreEmpty())
					JOptionPane.showMessageDialog(null, "Some fields are empty");
				else if (!passwordsAreEqual())
					JOptionPane.showMessageDialog(null, "Passwords does not match");
				else {
					commitUpdate();
					flagRemindLaterFalse();
					dispose();
				}
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnApply.setBounds(415, 428, 97, 25);
		contentPane.add(btnApply);

		JButton btnDismiss = new JButton("Dismiss");
		btnDismiss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnDismiss.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnDismiss.setBounds(304, 428, 97, 25);
		contentPane.add(btnDismiss);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_1.setBounds(12, 238, 500, 49);
		contentPane.add(panel_1);

		JLabel lblIfYouForget = new JLabel("If you forget your password, you can get it back by filling this form.");
		lblIfYouForget.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		lblIfYouForget.setBounds(12, 13, 476, 20);
		panel_1.add(lblIfYouForget);

		JLabel lblSecretQuestion = new JLabel("Secret question:");
		lblSecretQuestion.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSecretQuestion.setBounds(12, 300, 132, 26);
		contentPane.add(lblSecretQuestion);

		cmbSecretQuestion = new JComboBox<String>();
		cmbSecretQuestion
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Question A", "Question B", "Question C" }));
		cmbSecretQuestion.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		cmbSecretQuestion.setBounds(208, 304, 160, 22);
		contentPane.add(cmbSecretQuestion);

		JLabel lblResponse = new JLabel("Response:");
		lblResponse.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblResponse.setBounds(12, 350, 132, 26);
		contentPane.add(lblResponse);

		txtResponse = new JTextField();
		txtResponse.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtResponse.setColumns(10);
		txtResponse.setBounds(208, 350, 160, 26);
		contentPane.add(txtResponse);

		chShowPasswords = new JCheckBox("show passwords");
		chShowPasswords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chShowPasswords.isSelected()) {
					txtNewPassword.setEchoChar((char) 0);
					txtNewPasswordRepeat.setEchoChar((char) 0);
				} else {
					txtNewPassword.setEchoChar('*');
					txtNewPasswordRepeat.setEchoChar('*');
				}
			}
		});
		chShowPasswords.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 14));
		chShowPasswords.setBounds(378, 150, 134, 25);
		contentPane.add(chShowPasswords);
	}

	private void commitUpdate() {
		String newLogin = txtNewLogin.getText().toString();
		@SuppressWarnings("deprecation")
		String newPassword = txtNewPassword.getText().toString();
		String response = txtResponse.getText().toString();

		PreparedStatement preparedStatement = null;

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
			preparedStatement = connection.prepareStatement(
					"UPDATE Administrators SET Name = ?, Password = ?, Question = ?, Answer = ? WHERE ID = 1");
			preparedStatement.setString(1, newLogin);
			preparedStatement.setString(2, newPassword);
			preparedStatement.setString(3, cmbSecretQuestion.getSelectedItem().toString());
			preparedStatement.setString(4, response);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
		}

		JOptionPane.showMessageDialog(null,
				"Data has been sucessufully updated.\n You can use now your new login and password.");
	}

	@SuppressWarnings("deprecation")
	private boolean passwordsAreEqual() {
		return (txtNewPassword.getText().equals(txtNewPasswordRepeat.getText()));
	}

	private boolean fieldsAreEmpty() {
		return (txtNewLogin.getText().isEmpty() || txtNewPassword.getPassword().length == 0
				|| txtNewPasswordRepeat.getPassword().length == 0 || txtResponse.getText().isEmpty());
	}

	private void flagRemindLaterFalse() {

		try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
			Properties properties = new Properties();

			properties.setProperty("adminRootCreated", "true");
			properties.setProperty("remindLater", "false");
			properties.store(fileOutputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing data in properties " + e);
		}
	}
}
