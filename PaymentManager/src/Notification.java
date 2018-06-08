import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Notification extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNewLogin;
	private JPasswordField txtNewPasswordRepeat;
	private JPasswordField txtNewPassword;

	private boolean remindLaterFlag = false;

	/**
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { Notification frame = new
	 * Notification(); frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 * frame.setVisible(true); frame.setLocationRelativeTo(null); } catch (Exception
	 * e) { e.printStackTrace(); } } }); }
	 */

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
	public Notification() {
		setTitle("Payment Manager - Notification");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				if (!remindLaterFlag)
					openMainWindow();
			}
		});
		setSystemLookAndFeel();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblWelcomeToThe = new JLabel("Welcome to the Payment Manager");
		lblWelcomeToThe.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		lblWelcomeToThe.setBounds(93, 13, 263, 23);
		contentPane.add(lblWelcomeToThe);

		JLabel lblNewLogin = new JLabel("New login:");
		lblNewLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		lblNewLogin.setBounds(27, 61, 82, 23);
		contentPane.add(lblNewLogin);

		JLabel lblNewPassword = new JLabel("New password:");
		lblNewPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		lblNewPassword.setBounds(27, 106, 118, 23);
		contentPane.add(lblNewPassword);

		JLabel lblRe = new JLabel("New password (again):");
		lblRe.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		lblRe.setBounds(27, 144, 199, 23);
		contentPane.add(lblRe);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fieldsAreEmpty())
					JOptionPane.showMessageDialog(null, "All fields are mandatory");
				else if (!passwordsMatch())
					JOptionPane.showMessageDialog(null, "Passwords does not match");
				else {
					flagRemindLaterFalse();
					openMainWindow();
				}
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		btnApply.setBounds(335, 290, 97, 25);
		contentPane.add(btnApply);

		JButton btnRemindLater = new JButton("Remind later");
		btnRemindLater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remindLaterChoice();
			}
		});
		btnRemindLater.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		btnRemindLater.setBounds(185, 290, 136, 25);
		contentPane.add(btnRemindLater);

		JLabel lblItIsRecommended = new JLabel("It is recommended to change default login and password.");
		lblItIsRecommended.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 14));
		lblItIsRecommended.setBounds(72, 228, 360, 23);
		contentPane.add(lblItIsRecommended);

		txtNewLogin = new JTextField();
		txtNewLogin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtNewLogin.selectAll();
			}
		});
		txtNewLogin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		txtNewLogin.setColumns(10);
		txtNewLogin.setBounds(238, 64, 177, 26);
		contentPane.add(txtNewLogin);

		txtNewPassword = new JPasswordField();
		txtNewPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtNewPassword.selectAll();
			}
		});
		txtNewPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		txtNewPassword.setEchoChar('*');
		txtNewPassword.setBounds(238, 109, 177, 26);
		contentPane.add(txtNewPassword);

		txtNewPasswordRepeat = new JPasswordField();
		txtNewPasswordRepeat.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtNewPasswordRepeat.selectAll();
			}
		});
		txtNewPasswordRepeat.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 17));
		txtNewPasswordRepeat.setEchoChar('*');
		txtNewPasswordRepeat.setBounds(238, 147, 177, 26);
		contentPane.add(txtNewPasswordRepeat);

		JCheckBox chShowPassword = new JCheckBox("show password");
		chShowPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (chShowPassword.isSelected()) {
					txtNewPassword.setEchoChar((char) 0);
					txtNewPasswordRepeat.setEchoChar((char) 0);
				} else {
					txtNewPassword.setEchoChar('*');
					txtNewPasswordRepeat.setEchoChar('*');
				}
			}
		});
		chShowPassword.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		chShowPassword.setBounds(238, 182, 177, 25);
		contentPane.add(chShowPassword);
		createConfig();
	}

	private void createConfig() {
		File file = new File("config.properties");

		if (file.exists()) {
			loadRemindFlag();
		}
	}

	private boolean fieldsAreEmpty() {
		return (txtNewLogin.getText().isEmpty() || txtNewPassword.getPassword().length == 0
				|| txtNewPasswordRepeat.getPassword().length == 0);
	}

	@SuppressWarnings("deprecation")
	private boolean passwordsMatch() {
		return (txtNewPassword.getText().toString().equals(txtNewPasswordRepeat.getText().toString()));
	}

	private void remindLaterChoice() {
		dispose();

		flagRemindLaterTrue();
		
		openMainWindow();
	}

	// sets property when check box is selected / unselected
	private void flagRemindLaterTrue() {

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

	private void flagRemindLaterFalse() {

		try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
			Properties properties = new Properties();

			properties.setProperty("adminRootCreated", "true");
			properties.setProperty("remindLater", "false");
			properties.setProperty("alwaysLaunchFullScreen", "false");
			properties.store(fileOutputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing data in properties " + e);
		}
	}

	// applying all properties when application is loaded
	private void loadRemindFlag() {

		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String remindLater = properties.getProperty("remindLater");

			if (remindLater.equals("true"))
				remindLaterFlag = true;

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting properties on load " + e);
		}
	}

	private void openMainWindow() {
		dispose();
		
		MainWindow mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.setLocationRelativeTo(null);
	}
}
