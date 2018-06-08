import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.awt.event.ActionEvent;

public class GeneralSettings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";
	private JCheckBox chFullScreenLaunch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneralSettings frame = new GeneralSettings();
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
	public GeneralSettings() {
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
		panel.setBounds(12, 13, 420, 32);
		contentPane.add(panel);

		JLabel lblDisplaySettings = new JLabel("Display settings");
		lblDisplaySettings.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		panel.add(lblDisplaySettings);

		chFullScreenLaunch = new JCheckBox("always launch program in full screen ");
		chFullScreenLaunch.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		chFullScreenLaunch.setBounds(12, 54, 284, 25);
		contentPane.add(chFullScreenLaunch);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyChanges();
				JOptionPane.showMessageDialog(null, "Settings have been changed.");
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnApply.setBounds(335, 227, 97, 25);
		contentPane.add(btnApply);

		JButton btnDismiss = new JButton("Dismiss");
		btnDismiss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnDismiss.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnDismiss.setBounds(226, 227, 97, 25);
		contentPane.add(btnDismiss);

		JButton btnRestToDefault = new JButton("Reset to default");
		btnRestToDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetSettings();
				applyChanges();
				JOptionPane.showMessageDialog(null, "Settings have been reset to default");
			}
		});
		btnRestToDefault.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnRestToDefault.setBounds(73, 227, 141, 25);
		contentPane.add(btnRestToDefault);
		applyPropertiesOnLoad();
	}

	/* Method applies all properties when application is loaded */
	private void applyPropertiesOnLoad() {

		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String alwaysLaunchFullScreen = properties.getProperty("alwaysLaunchFullScreen");
			
			chFullScreenLaunch.setSelected(Boolean.valueOf(alwaysLaunchFullScreen));

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting properties on load " + e);
		}
	}
	
	private void applyChanges() {
		try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
			Properties properties = new Properties();

			properties.setProperty("adminRootCreated", "true");
			properties.setProperty("remindLater", "true");
			properties.setProperty("alwaysLaunchFullScreen", String.valueOf(chFullScreenLaunch.isSelected()));
			properties.store(fileOutputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing data in properties " + e);
		}
	}
	
	private void resetSettings() {
		chFullScreenLaunch.setSelected(false);
	}
}
