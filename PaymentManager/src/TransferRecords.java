import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransferRecords extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableFrom;
	private JTable tableTo;
	private JButton btnFrom;
	private JButton btnTo;
	private JPanel panel;
	private JTextField textField;
	private JTextField textField_1;
	private JCheckBox chckDeleteRecordsFrom;
	private JComboBox<String> comboSemesterFrom;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL_SEMESTERS = "jdbc:sqlite:semesters.db";
	private JComboBox<String> comboSemestersTo;
	DefaultTableModel defaultTableModelFrom;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransferRecords frame = new TransferRecords();
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
	public TransferRecords() {
		setResizable(false);
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1283, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 194, 598, 395);
		contentPane.add(scrollPane);

		String[] columnsFrom = { "StudentNumber", "FirstName", "LastName", "Sex" };
		DefaultTableModel modelFrom = new DefaultTableModel(columnsFrom, 0);
		tableFrom = new JTable(modelFrom);
		tableFrom.setFont(new Font("Verdana Pro Light", Font.PLAIN, 19));
		tableFrom.getTableHeader().setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		tableFrom.setBackground(Color.WHITE);
		tableFrom.setShowGrid(false);
		scrollPane.setViewportView(tableFrom);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(673, 194, 592, 395);
		contentPane.add(scrollPane_1);

		String[] columnsTo = { "StudentNumber", "FirstName", "LastName", "Sex" };
		DefaultTableModel modelTo = new DefaultTableModel(columnsTo, 0);
		tableTo = new JTable(modelTo);
		tableTo.setFont(new Font("Verdana Pro Light", Font.PLAIN, 19));
		tableTo.getTableHeader().setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		tableTo.setBackground(Color.WHITE);
		tableTo.setShowGrid(false);
		scrollPane.setViewportView(tableFrom);
		scrollPane_1.setViewportView(tableTo);

		btnFrom = new JButton(">");
		btnFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (semestersAreSame()) {
					JOptionPane.showMessageDialog(null,
							"Tranfer can not be done. \nTo proceed tranfer, two different semesters must be choosed.");
					return;
				}

			}
		});
		btnFrom.setFont(new Font("Verdana Pro Cond", Font.BOLD, 16));
		btnFrom.setBounds(622, 307, 43, 25);
		contentPane.add(btnFrom);

		btnTo = new JButton("<");
		btnTo.setFont(new Font("Verdana Pro Cond", Font.BOLD, 16));
		btnTo.setBounds(622, 365, 43, 25);
		contentPane.add(btnTo);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Transfer options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 13, 1253, 87);
		contentPane.add(panel);
		panel.setLayout(null);

		chckDeleteRecordsFrom = new JCheckBox("delete records from semester after transfer");
		chckDeleteRecordsFrom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (chckDeleteRecordsFrom.isSelected())
					warningForSuppression();
			}
		});
		chckDeleteRecordsFrom.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		chckDeleteRecordsFrom.setBounds(8, 34, 329, 25);
		panel.add(chckDeleteRecordsFrom);

		textField = new JTextField();
		textField.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		textField.setColumns(10);
		textField.setBounds(80, 155, 123, 26);
		contentPane.add(textField);

		JLabel label = new JLabel("Search:");
		label.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		label.setBounds(12, 160, 56, 16);
		contentPane.add(label);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		textField_1.setColumns(10);
		textField_1.setBounds(741, 155, 123, 26);
		contentPane.add(textField_1);

		JLabel label_1 = new JLabel("Search:");
		label_1.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		label_1.setBounds(673, 160, 56, 16);
		contentPane.add(label_1);

		JLabel lblSemester = new JLabel("Semester:");
		lblSemester.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSemester.setBounds(316, 158, 70, 21);
		contentPane.add(lblSemester);

		comboSemesterFrom = new JComboBox<String>();
		comboSemesterFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearSemesterData();
				getSemesterDataFrom();
			}
		});
		comboSemesterFrom.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		comboSemesterFrom.setBounds(398, 154, 212, 27);
		contentPane.add(comboSemesterFrom);

		comboSemestersTo = new JComboBox<String>();
		comboSemestersTo.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		comboSemestersTo.setBounds(1068, 154, 197, 27);
		contentPane.add(comboSemestersTo);

		JLabel lblSemester_1 = new JLabel("Semester:");
		lblSemester_1.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSemester_1.setBounds(989, 158, 78, 21);
		contentPane.add(lblSemester_1);

		JCheckBox chckbxSelectAll = new JCheckBox("select all");
		chckbxSelectAll.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		chckbxSelectAll.setBounds(218, 156, 98, 25);
		contentPane.add(chckbxSelectAll);

		JCheckBox checkBox = new JCheckBox("select all");
		checkBox.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		checkBox.setBounds(883, 156, 98, 25);
		contentPane.add(checkBox);
		showSemestersFrom();
		getSemesterDataFrom();
	}

	private void clearSemesterData() {
		if (defaultTableModelFrom.getRowCount() > 0) {
			for (int i = defaultTableModelFrom.getRowCount() - 1; i > -1; i--) {
				defaultTableModelFrom.removeRow(i);
			}
		}
	}
	
	private boolean semestersAreSame() {
		return (comboSemesterFrom.getSelectedIndex() == comboSemestersTo.getSelectedIndex());
	}

	private void showSemestersFrom() {
		String[] types = { "TABLE" };
		ResultSet resultSet = null;

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement()) {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Error : Class has not been found while displaying semesters from database. \n Details : " + e);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Error : Could not open connection to retreive semesters from database. \n Details :  " + e);
		}

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS)) {
			DatabaseMetaData metadata = connection.getMetaData();
			resultSet = metadata.getTables(null, null, "%", types);

			while (resultSet.next()) {
				String tableName = resultSet.getString(3);
				comboSemesterFrom.addItem(tableName);
				comboSemestersTo.addItem(tableName);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error : Could not retreive semesters from database. \n Details :  " + e);
		}
	}

	private void getSemesterDataFrom() {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT StudentNumber, FirstName, LastName, Sex FROM "
								+ "`" + comboSemesterFrom.getSelectedItem() + "`")) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class not found for sumbit of new payment. \n Details : " + e);
			}
			connection.setAutoCommit(false);

			/* if semester has data, affecting this data to defaultTableModel */
			while (resultSet.next()) {
				String studentNumber = resultSet.getString(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String sex = resultSet.getString(4);
		
				Object[] content = { studentNumber, firstName, lastName, sex };

				defaultTableModelFrom = (DefaultTableModel) tableFrom.getModel();
				defaultTableModelFrom.addRow(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : Could not display data from database. \n Details : " + e);
		}
	}
	
	private void makeTransfer() {
		
	}
	
	private void warningForSuppression() {
		String[] options = { "Yes", "No" };
		int answer = JOptionPane.showOptionDialog(null,
				"All records that you have choosed will be deleted after transfer from semester you transfer from. \nApply this option? ",
				"Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if (answer == 0) {
			chckDeleteRecordsFrom.setSelected(true);
		} else {
			chckDeleteRecordsFrom.setSelected(false);
			return;
		}
	}
}
