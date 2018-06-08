import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogInGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JPasswordField txtPassword;
	private JCheckBox chShowPassword;
	private JButton btnLogIn;
	private JLabel lblNewAccount;
	private JLabel lblForgotPassword;

	private boolean adminRootInitialized = false;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInGUI frame = new LogInGUI();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
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
	public LogInGUI() {
		setTitle("Payment Manager - Log in");
		setResizable(false);
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 430);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPaymentManager = new JLabel("Payment Manager");
		lblPaymentManager.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 20));
		lblPaymentManager.setBounds(141, 30, 213, 33);
		contentPane.add(lblPaymentManager);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		lblLogin.setBounds(28, 99, 82, 24);
		contentPane.add(lblLogin);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		lblPassword.setBounds(28, 142, 82, 25);
		contentPane.add(lblPassword);

		txtLogin = new JTextField();
		txtLogin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtLogin.selectAll();
			}
		});
		txtLogin.selectAll();
		txtLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		txtLogin.setBounds(141, 98, 156, 26);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);

		btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String adminLogin = txtLogin.getText().toString();
				@SuppressWarnings("deprecation")
				String adminPassword = txtPassword.getText().toString();

				if (areFieldsEmpty())
					JOptionPane.showMessageDialog(null, "Login and Password fields are mandatory.");
				else if (verifyNameAndPassword(adminLogin, adminPassword)) {
					updateCurrentSession();

					dispose();

					Notification notification = new Notification();

					notification.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					notification.setVisible(true);
					notification.setLocationRelativeTo(null);

				} else
					JOptionPane.showMessageDialog(null, "Admin does not exists");
			}
		});
		btnLogIn.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		btnLogIn.setBounds(200, 232, 97, 25);
		contentPane.add(btnLogIn);

		lblNewAccount = new JLabel("New account");
		lblNewAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();

				NewAccount newAccount = new NewAccount();

				newAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				newAccount.setVisible(true);
				newAccount.setLocationRelativeTo(null);
			}
		});
		lblNewAccount.setForeground(Color.BLUE);
		lblNewAccount.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		lblNewAccount.setBounds(28, 340, 97, 16);
		contentPane.add(lblNewAccount);

		JLabel label = new JLabel("/");
		label.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		label.setBounds(137, 342, 56, 16);
		contentPane.add(label);

		lblForgotPassword = new JLabel("Forgot password");
		lblForgotPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ForgotPassword forgotPassword = new ForgotPassword();
				forgotPassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				forgotPassword.setVisible(true);
				forgotPassword.setLocationRelativeTo(null);
			}
		});
		lblForgotPassword.setForeground(Color.BLUE);
		lblForgotPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		lblForgotPassword.setBounds(158, 334, 122, 29);
		contentPane.add(lblForgotPassword);

		JLabel lblVersion = new JLabel("v1.0 beta");
		lblVersion.setBounds(376, 342, 56, 16);
		contentPane.add(lblVersion);

		txtPassword = new JPasswordField();
		txtPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtPassword.selectAll();
			}
		});
		txtPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String adminLogin = txtLogin.getText().toString();
				@SuppressWarnings("deprecation")
				String adminPassword = txtPassword.getText().toString();

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					if (verifyNameAndPassword(adminLogin, adminPassword)) {
						updateCurrentSession();

						dispose();
						Notification notification = new Notification();

						notification.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						notification.setVisible(true);
						notification.setLocationRelativeTo(null);
					}
			}
		});
		txtPassword.setBounds(141, 145, 156, 26);
		txtPassword.setEchoChar('*');
		contentPane.add(txtPassword);

		chShowPassword = new JCheckBox("show password");
		chShowPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chShowPassword.isSelected())
					txtPassword.setEchoChar((char) 0);
				else
					txtPassword.setEchoChar('*');
			}
		});
		chShowPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		chShowPassword.setBounds(141, 180, 156, 25);
		contentPane.add(chShowPassword);
		createAdminTable();
		createCurrentSessionTable();

		if (!adminRootInitialized)
			createConfig();
	}

	/* Method that accepts query and executes it */
	private void executeStatementQuery(String query) {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver: " + e);
		}

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
				Statement statement = connection.createStatement()) {
			statement.execute(query);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not execute statement: " + e);
		}
	}

	/* Creating table that will contain all administrators */
	private void createAdminTable() {
		String adminTable = null;

		adminTable = "CREATE TABLE IF NOT EXISTS Administrators (ID INTEGER PRIMARY KEY, Name VARCHAR(80), "
				+ "Password VARCHAR(80), Question VARCHAR(120), Answer VARCHAR(200), hasRootPermission VARCHAR(3))";
		executeStatementQuery(adminTable);
	}

	/* Creating table that will contain users that logging in */
	private void createCurrentSessionTable() {
		String adminTable = null;

		adminTable = "CREATE TABLE IF NOT EXISTS CurrentSession (ID INTEGER PRIMARY KEY, Name VARCHAR(80), hasRootPermission VARCHAR(3))";
		executeStatementQuery(adminTable);
	}

	/* Method checks if user name and password are valid and sends true or false */
	private boolean verifyNameAndPassword(String adminLogin, String adminPassword) {
		String database;

		database = "SELECT * FROM Administrators WHERE Name LIKE " + "'" + adminLogin + "'" + " AND Password LIKE "
				+ "'" + adminPassword + "'";

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

	/*
	 * Method checks if a configuration file exists already, if not it'll create one
	 */
	private void createConfig() {
		File file = new File("config.properties");

		if (!file.exists()) {
			initializeRootAccount();
		} else {
			applyPropertiesOnLoad();
		}
	}

	/* Method sets all properties */
	private void setProperty() {

		try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
			Properties properties = new Properties();

			properties.setProperty("adminRootCreated", "true");
			properties.setProperty("remindLater", "true");
			properties.setProperty("alwaysLaunchFullScreen", "false");
			properties.store(fileOutputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing data in properties " + e);
		}
	}

	/* Method applies all properties when application is loaded */
	private void applyPropertiesOnLoad() {

		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String adminRootCreated = properties.getProperty("adminRootCreated");

			if (adminRootCreated.equals("true"))
				adminRootInitialized = true;

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting properties on load " + e);
		}
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

	/*
	 * Creating root administrator. This method called only once when application is
	 * launched for the first time.
	 */
	private void initializeRootAccount() {
		String login = "admin";
		String password = "admin";
		PreparedStatement preparedStatement = null;
		boolean success = false;

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO Administrators (Name, Password, Question, Answer, hasRootPermission) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, "");
			preparedStatement.setString(4, "");
			preparedStatement.setString(5, "Yes");

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
		}

		success = true;

		if (success)
			setProperty();
	}

	/* Method verifies if all fields are filled */
	private boolean areFieldsEmpty() {
		return (txtLogin.getText().isEmpty() || txtPassword.getPassword().length == 0);
	}
}
