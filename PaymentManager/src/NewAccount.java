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
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.awt.event.ActionEvent;

public class NewAccount extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JTextField txtCode;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewAccount frame = new NewAccount();
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
	public NewAccount() {
		setResizable(false);
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(12, 13, 420, 52);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblUseTheCode = new JLabel("Use the code generated by root administator to proceed the ");
		lblUseTheCode.setBounds(12, 0, 420, 20);
		panel.add(lblUseTheCode);
		lblUseTheCode.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		JLabel lblRegistraion = new JLabel("registration.");
		lblRegistraion.setBounds(12, 22, 420, 20);
		panel.add(lblRegistraion);
		lblRegistraion.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblLogin.setBounds(12, 91, 56, 23);
		contentPane.add(lblLogin);

		JLabel lblCode = new JLabel("Code:");
		lblCode.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblCode.setBounds(12, 129, 54, 23);
		contentPane.add(lblCode);

		txtLogin = new JTextField();
		txtLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtLogin.setBounds(80, 93, 188, 26);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);

		txtCode = new JTextField();
		txtCode.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtCode.setColumns(10);
		txtCode.setBounds(80, 127, 188, 26);
		contentPane.add(txtCode);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerNewAdmin();
				updateCurrentSession();

				JOptionPane.showMessageDialog(null,
						"New account has been sucessfully created.\n You can change password later on in settings.");

				openMainWindow();
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnApply.setBounds(290, 130, 97, 25);
		contentPane.add(btnApply);
		applyPropertiesOnLoad();
	}

	/* Method applies all properties when application is loaded */
	private void applyPropertiesOnLoad() {

		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String alwaysLaunchFullScreen = properties.getProperty("alwaysLaunchFullScreen");

			if (alwaysLaunchFullScreen.equals("true")) {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				setUndecorated(true);
				setVisible(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting properties on load " + e);
		}
	}

	/* Checks if previously generated code exists in the database */
	private boolean verifyCode(String adminPassword) {
		String database;

		database = "SELECT * FROM Administrators WHERE Password LIKE " + "'" + adminPassword + "'";

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

	/* Creating new administrator account */
	private void registerNewAdmin() {
		String login = txtLogin.getText().toString();
		String code = txtCode.getText().toString();
		PreparedStatement preparedStatement = null;

		if (verifyCode(code)) {

			try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
				preparedStatement = connection
						.prepareStatement("UPDATE Administrators SET Name = ?, Password = ?, Question = ?, Answer = ? "
								+ "WHERE Password = " + "'" + code + "'");
				preparedStatement.setString(1, login);
				preparedStatement.setString(2, code);
				preparedStatement.setString(3, "");
				preparedStatement.setString(4, "");

				preparedStatement.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
			}
		} else
			JOptionPane.showMessageDialog(null, "Invalid code. If this error persists, contact root administrator.");
	}

	/* Method verifies if user has root permission */
	private String verifyIfUserIsRootAdmin() {
		String database;
		String adminLogin = txtLogin.getText().toString();
		String result = "";

		database = "SELECT hasRootPermission FROM Administrators WHERE Name LIKE " + "'" + adminLogin + "'";

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
				result = resultSet.getString(1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/* Inserts into table CurrentSession the administrator that just logged in */
	private void updateCurrentSession() {
		String loginSession = txtLogin.getText().toString();
		String hasRootPermission = "";

		PreparedStatement preparedStatement = null;

		hasRootPermission = verifyIfUserIsRootAdmin();

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
			preparedStatement = connection
					.prepareStatement("INSERT INTO CurrentSession (Name, hasRootPermission) values (?, ?)");
			preparedStatement.setString(1, loginSession);
			preparedStatement.setString(2, hasRootPermission);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
		}
	}

	/* Opens MainWindow */
	private void openMainWindow() {
		dispose();

		MainWindow mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.setLocationRelativeTo(null);
	}
}
