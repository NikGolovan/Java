import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Security extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableAdmins;
	private JTextField txtSearchAdmin;
	private JButton btnApply;
	private JButton btnDismiss;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL = "jdbc:sqlite:admin.db";

	DefaultTableModel defaultTableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Security frame = new Security();
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
	public Security() {
		setTitle("Payment Manager - Security");
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
		panel.setBounds(12, 13, 420, 38);
		contentPane.add(panel);

		JLabel lblYouCanGive = new JLabel("You can give root permission to another administrator.");
		lblYouCanGive.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 15));
		panel.add(lblYouCanGive);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 78, 118, 174);
		contentPane.add(scrollPane);

		String[] columns = { "Login" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		tableAdmins = new JTable(model);
		tableAdmins.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
		tableAdmins.getTableHeader().setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		tableAdmins.setShowGrid(false);
		scrollPane.setViewportView(tableAdmins);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSearch.setBounds(147, 79, 56, 24);
		contentPane.add(lblSearch);

		txtSearchAdmin = new JTextField();
		txtSearchAdmin.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtSearchAdmin.setBounds(221, 79, 168, 26);
		contentPane.add(txtSearchAdmin);
		txtSearchAdmin.setColumns(10);

		btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hasAlreadyRootPermission()) {
					JOptionPane.showMessageDialog(null, "This administrator has already root permission");
					return;
				}
				giveRootPermission();
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		btnApply.setBounds(329, 227, 97, 25);
		contentPane.add(btnApply);

		btnDismiss = new JButton("Dismiss");
		btnDismiss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnDismiss.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		btnDismiss.setBounds(221, 227, 97, 25);
		contentPane.add(btnDismiss);
		getTableData();
	}

	private void getTableData() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(CONNECTION_URL);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT ID, Name FROM Administrators");

			while (resultSet.next()) {
				String name = resultSet.getString(2);

				Object[] content = { name };

				defaultTableModel = (DefaultTableModel) tableAdmins.getModel();
				defaultTableModel.addRow(content);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error has occured: " + e);
		} finally {
			try {
				statement.close();
				connection.close();
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error has occured: " + e);
			}
		}
	}

	private boolean hasAlreadyRootPermission() {
		String database = "";

		DefaultTableModel model = (DefaultTableModel) tableAdmins.getModel();
		String selection = (String) model.getValueAt(tableAdmins.getSelectedRow(), 0);

		database = "SELECT * FROM Administrators WHERE Name = " + "'" + selection + "'";

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
				if (resultSet.getString(6).equals("Yes"))
					return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void giveRootPermission() {
		DefaultTableModel model = (DefaultTableModel) tableAdmins.getModel();
		String selection = (String) model.getValueAt(tableAdmins.getSelectedRow(), 0);

		PreparedStatement preparedStatement = null;

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
			preparedStatement = connection.prepareStatement(
					"UPDATE Administrators SET hasRootPermission = ? WHERE Name = " + "'" + selection + "'");
			preparedStatement.setString(1, "Yes");

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while initializing root account: " + e);
		}
		JOptionPane.showMessageDialog(null, "Administrator " + "'" + selection + "'" + " has now root permission");
	}
}
