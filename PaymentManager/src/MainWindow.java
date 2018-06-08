import java.awt.Dimension;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import javax.swing.JCheckBox;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static String semester;
	private boolean searchUsed = false;
	DefaultTableModel defaultTableModel;

	String JDBC_DRIVER = "org.sqlite.JDBC";
	String CONNECTION_URL_ADMIN = "jdbc:sqlite:admin.db";
	String CONNECTION_URL_SEMESTERS = "jdbc:sqlite:semesters.db";

	private JLabel lblName;
	private JMenuItem menuAddAdministrator;
	private JTable tableStudents;
	private JTextField txtSearch;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtStudentN;
	private JTextField txtCost;
	private JTextField txtCharges;
	private JTextField txtTotal;
	private JTextField txtAmount;
	private JTextField txtRemainder;
	private JCheckBox chckFemale;
	private JCheckBox chckMale;
	private JTextField txtNewSemester;
	private JComboBox<String> cmbPaymentType;
	private JList<String> listSemesters;
	private JCheckBox chckNonClosedPayments;
	private JLabel lblNotFound;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public MainWindow() {
		setTitle("Payment Manager - Main Window");
		setSystemLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1600, 750);
		setMinimumSize(new Dimension(1600, 750));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnGeneral = new JMenu("General");
		mnGeneral.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		menuBar.add(mnGeneral);

		JMenuItem mntmNewMenuItem = new JMenuItem("Switch account");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

				LogInGUI logInGUI = new LogInGUI();
				logInGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				logInGUI.setVisible(true);
				logInGUI.setLocationRelativeTo(null);
			}
		});
		mntmNewMenuItem.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnGeneral.add(mntmNewMenuItem);

		JSeparator separator_1 = new JSeparator();
		mnGeneral.add(separator_1);

		JMenuItem mntmPrintTable_1 = new JMenuItem("Print table");
		mntmPrintTable_1.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnGeneral.add(mntmPrintTable_1);

		JMenuItem mntmPrintTable = new JMenuItem("Export table");
		mntmPrintTable.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnGeneral.add(mntmPrintTable);

		JMenu mnSettings = new JMenu("Settings");
		mnSettings.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		menuBar.add(mnSettings);

		menuAddAdministrator = new JMenuItem("Add administrator");
		menuAddAdministrator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddAdministrator addAdministrator = new AddAdministrator();

				addAdministrator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				addAdministrator.setVisible(true);
				addAdministrator.setLocationRelativeTo(null);
			}
		});
		menuAddAdministrator.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSettings.add(menuAddAdministrator);

		JMenuItem mntmChangeRootSettings = new JMenuItem("Change root settings");
		mntmChangeRootSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeRootSettings changeRootSettings = new ChangeRootSettings();
				changeRootSettings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				changeRootSettings.setVisible(true);
				changeRootSettings.setLocationRelativeTo(null);
			}
		});
		mntmChangeRootSettings.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSettings.add(mntmChangeRootSettings);

		JMenuItem mntmGeneralSettings = new JMenuItem("General settings");
		mntmGeneralSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralSettings generalSettings = new GeneralSettings();
				generalSettings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				generalSettings.setVisible(true);
				generalSettings.setLocationRelativeTo(null);
			}
		});
		mntmGeneralSettings.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSettings.add(mntmGeneralSettings);

		JMenuItem mntmSecurity = new JMenuItem("Security");
		mntmSecurity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Security security = new Security();
				security.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				security.setVisible(true);
				security.setLocationRelativeTo(null);
			}
		});
		mntmSecurity.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSettings.add(mntmSecurity);

		JSeparator separator = new JSeparator();
		mnSettings.add(separator);

		JMenuItem mntmChangeSettings = new JMenuItem("Change settings");
		mntmChangeSettings.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSettings.add(mntmChangeSettings);

		JMenu mnLog = new JMenu("Log");
		mnLog.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		menuBar.add(mnLog);

		JMenuItem mntmOpenTheLog = new JMenuItem("Open the log");
		mntmOpenTheLog.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnLog.add(mntmOpenTheLog);
		
		JMenu mnSemesters = new JMenu("Semesters");
		mnSemesters.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		menuBar.add(mnSemesters);
		
		JMenuItem mntmTransitStudents = new JMenuItem("Transfer records");
		mntmTransitStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TransferRecords transferRecords = new TransferRecords();

				transferRecords.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				transferRecords.setVisible(true);
				transferRecords.setLocationRelativeTo(null);
			}
		});
		mntmTransitStudents.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		mnSemesters.add(mntmTransitStudents);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblLoggedAs = new JLabel("Logged as:");
		lblLoggedAs.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		lblName = new JLabel("Name");
		lblName.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JSeparator separator_2 = new JSeparator();

		JScrollPane scrollPaneTable = new JScrollPane();

		String[] columns = { "TransactionNumber", "StudentNumber", "FirstName", "LastName", "Sex", "Total",
				"Remainder" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		tableStudents = new JTable(model);
		tableStudents.setFont(new Font("Verdana Pro Light", Font.PLAIN, 19));
		tableStudents.getTableHeader().setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		tableStudents.setBackground(Color.WHITE);
		tableStudents.setShowGrid(false);

		scrollPaneTable.setViewportView(tableStudents);

		JScrollPane scrollPaneList = new JScrollPane();

		listSemesters = new JList<String>();
		listSemesters.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openSemesterFromList();
			}
		});
		listSemesters.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));

		scrollPaneList.setViewportView(listSemesters);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1548, Short.MAX_VALUE)
						.addComponent(scrollPaneTable)
						.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 1548, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblLoggedAs)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(scrollPaneList)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 1213, Short.MAX_VALUE)))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(scrollPaneList)
								.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPaneTable)
						.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(9)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblLoggedAs)
								.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))));

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("NEW STUDENT", null, panel_1, null);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(
				new TitledBorder(null, "Student Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Proceed payment",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE)
						.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE))
				.addGap(11)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)));

		JLabel lblPaymentType = new JLabel("Payment type:");
		lblPaymentType.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtAmount = new JTextField();
		txtAmount.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtAmount.setColumns(10);

		JLabel lblRemainder = new JLabel("Remainder:");
		lblRemainder.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtRemainder = new JTextField();
		txtRemainder.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtRemainder.setColumns(10);

		cmbPaymentType = new JComboBox<String>();
		cmbPaymentType.setModel(new DefaultComboBoxModel<String>(new String[] { "Credit card", "Cheque", "Cash" }));
		cmbPaymentType.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JButton btnApply = new JButton("Confirm");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fieldsAreEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields are mandatory.");
					return;
				} else if (!checkBoxesAreSelected()) {
					JOptionPane.showMessageDialog(null, "Form is incomplete");
					return;
				} else {
					submitNewPayment();
					getSemesterData();
					resetFields();
				}
			}
		});
		btnApply.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
						.addComponent(lblPaymentType, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(cmbPaymentType, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblAmount).addGap(15)
						.addComponent(txtAmount, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRemainder)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(txtRemainder, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 482, Short.MAX_VALUE).addComponent(btnApply)
						.addContainerGap()));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_4
				.createSequentialGroup().addGap(4)
				.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING, false).addGroup(gl_panel_4
						.createSequentialGroup().addGap(1)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE, false)
								.addGroup(gl_panel_4.createSequentialGroup().addGap(6).addComponent(lblPaymentType,
										GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_4.createSequentialGroup().addGap(6).addComponent(lblAmount,
										GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
								.addComponent(cmbPaymentType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(1))
						.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE, false)
								.addGroup(gl_panel_4.createSequentialGroup().addGap(3).addComponent(txtAmount,
										GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_4.createSequentialGroup().addGap(8).addComponent(lblRemainder,
										GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_4.createSequentialGroup().addGap(3).addComponent(txtRemainder,
										GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnApply)))
				.addGap(6)));
		panel_4.setLayout(gl_panel_4);

		JLabel lblFirstName = new JLabel("First name:");
		lblFirstName.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtFirstName = new JTextField();
		txtFirstName.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtFirstName.setColumns(10);

		txtLastName = new JTextField();
		txtLastName.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtLastName.setColumns(10);

		JLabel lblLastName = new JLabel("Last name:");
		lblLastName.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JLabel lblStudentN = new JLabel("Student #:");
		lblStudentN.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtStudentN = new JTextField();
		txtStudentN.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtStudentN.setColumns(10);

		chckMale = new JCheckBox("Male");
		chckMale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lockCheckBoxMaleFemale();
			}
		});
		chckMale.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		chckFemale = new JCheckBox("Female");
		chckFemale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lockCheckBoxMaleFemale();
			}
		});
		chckFemale.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JLabel lblDegreeCost = new JLabel("Degree cost:");
		lblDegreeCost.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtCost = new JTextField();
		txtCost.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtCost.setColumns(10);

		JLabel lblAdditionalCharges = new JLabel("Additional charges:");
		lblAdditionalCharges.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtCharges = new JTextField();
		txtCharges.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtCharges.setColumns(10);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtTotal = new JTextField();
		txtTotal.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtTotal.setColumns(10);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblFirstName)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtFirstName, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblLastName, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addGap(5).addComponent(txtLastName, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblStudentN, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtStudentN, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE).addGap(8)
								.addComponent(chckMale).addPreferredGap(ComponentPlacement.RELATED).addComponent(
										chckFemale)
								.addGap(371))
						.addGroup(gl_panel_3.createSequentialGroup()
								.addComponent(lblDegreeCost, GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtCost, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblAdditionalCharges, GroupLayout.PREFERRED_SIZE, 136,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txtCharges, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblTotal)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txtTotal, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(chckMale)
								.addComponent(chckFemale, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup().addGap(3).addComponent(lblLastName,
								GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblStudentN, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtStudentN, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE, false).addComponent(lblFirstName)
								.addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, 26,
										GroupLayout.PREFERRED_SIZE)))
				.addGap(18)
				.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup().addGap(3).addComponent(lblDegreeCost))
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtCost, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAdditionalCharges)
								.addComponent(txtCharges, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotal)
								.addComponent(txtTotal, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		panel_3.setLayout(gl_panel_3);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("NEW SEMESTER", null, panel_2, null);
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Create new semester",

				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(12, 13, 1185, 77);
		panel_2.add(panel_5);

		JLabel lblSemester = new JLabel("Semester name:");
		lblSemester.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		txtNewSemester = new JTextField();
		txtNewSemester.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtNewSemester.setColumns(10);

		JButton btnCreateSemester = new JButton("Create");
		btnCreateSemester.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNewSemester.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Semester name is empty");
					return;
				} else {
					createNewSemester();
					showSemestersInList();
				}
			}
		});
		btnCreateSemester.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap().addComponent(lblSemester)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(txtNewSemester, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(btnCreateSemester).addContainerGap(730, Short.MAX_VALUE)));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSemester, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNewSemester, GroupLayout.PREFERRED_SIZE, 26,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCreateSemester))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_5.setLayout(gl_panel_5);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Delete semester",

				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6.setBounds(12, 107, 1185, 77);
		panel_2.add(panel_6);

		JButton bntDelete = new JButton("Delete");
		bntDelete.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));

		JLabel lblChooseSemesterYou = new JLabel("Choose semester and click on \"Delete\" button.");
		lblChooseSemesterYou.setFont(new Font("Verdana Pro Cond", Font.ITALIC, 16));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap().addComponent(bntDelete).addGap(18)
						.addComponent(lblChooseSemesterYou, GroupLayout.PREFERRED_SIZE, 636, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(432, Short.MAX_VALUE)));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE).addComponent(bntDelete)
						.addComponent(lblChooseSemesterYou, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);
		panel.setLayout(null);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblSearch.setBounds(12, 13, 56, 16);
		panel.add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtSearch.selectAll();
			}
		});
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchStudentInSemester(txtSearch.getText());
			}
		});
		txtSearch.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		txtSearch.setBounds(80, 8, 245, 26);
		panel.add(txtSearch);
		txtSearch.setColumns(10);

		chckNonClosedPayments = new JCheckBox("select only non-closed payments");
		chckNonClosedPayments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckNonClosedPayments.isSelected()) {
					clearSemesterData();
					selectOnlyUnpayed();
				} else if (chckNonClosedPayments.isSelected()) {
					getSemesterData();
				} else {
					clearSemesterData();
					getSemesterData();
				}
			}
		});
		chckNonClosedPayments.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		chckNonClosedPayments.setBounds(333, 11, 260, 25);
		panel.add(chckNonClosedPayments);

		lblNotFound = new JLabel("");
		lblNotFound.setFont(new Font("Verdana Pro Cond", Font.BOLD, 16));
		lblNotFound.setBounds(601, 15, 172, 16);
		panel.add(lblNotFound);
		contentPane.setLayout(gl_contentPane);
		getCurrentUser();
		lockMenus();
		applySettings();
	}

	/**
	 * Applies all properties when application is loaded.
	 * <p>
	 * This method applies configurations written in configuration file.
	 * 
	 * @throws Exception
	 *             if can not read or open properties file.
	 */
	private void applySettings() {
		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String alwaysLaunchFullScreen = properties.getProperty("alwaysLaunchFullScreen");

			if (alwaysLaunchFullScreen.equals("true")) {
				this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : Can not read file while setting properties on load " + e);
		}
		showSemestersInList();
	}

	/**
	 * Gets current user and setting label to his name.
	 * <p>
	 * This method uses a query to select all user information in descent order
	 * limited by one result. Then, it takes user name from admin.db database,
	 * Administrators table and set it to the label.
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if problems with ResultSet
	 */
	private void getCurrentUser() {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_ADMIN);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrentSession ORDER BY ID DESC LIMIT 1")) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class has not been found while getting user root permission. \n Details : " + e);
			}

			/* getting user name */
			if (resultSet.next())
				lblName.setText(resultSet.getString(2));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifies if user that logged into application has root permission by
	 * searching him in administrator database.
	 * <p>
	 * This method uses a query to select all user information in descent order
	 * limited by one result. Then, the column hasRootPermission of a table
	 * CurrentSession from database admin.db gets compared with resultSet. If
	 * resultSet hasRootPermission is equal to "Yes", method returns true and false
	 * otherwise.
	 * 
	 * @return boolean <b>true</b> if user has root permission or <b>false</b> if
	 *         user does not have root permission.
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if problems with ResultSet
	 */
	private boolean currentUserHasRootPermission() {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_ADMIN);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrentSession ORDER BY ID DESC LIMIT 1")) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class has not been found while verifying user's root permission. \n Details : " + e);
			}

			/*
			 * searching for "Yes" in administrators database (admin.db) from
			 * "Administrators" table
			 */
			if (resultSet.next())
				if (resultSet.getString(3).equals("Yes"))
					return true;

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Could not find user with root permission. \n Details : " + e);
		}
		return false;
	}

	/**
	 * Locks check boxes.
	 * <p>
	 * This method sets as not enabled male check box if female is used and female
	 * as not enabled if male used. Initialises both check boxes to enabled if user
	 * removes marker from one of them.
	 * 
	 */
	private void lockCheckBoxMaleFemale() {
		if (chckMale.isSelected()) {
			chckFemale.setSelected(false);
			chckFemale.setEnabled(false);
		} else if (chckFemale.isSelected()) {
			chckMale.setSelected(false);
			chckMale.setEnabled(false);
		} else {
			chckMale.setEnabled(true);
			chckFemale.setEnabled(true);
		}
	}

	/**
	 * Verifies if check boxes are selected.
	 * <p>
	 * This method verifies if check boxes are selected.
	 * 
	 * @return <b>true</b> if {@link #chckFemale} is selected OR {@link #chckMale}
	 *         is selected. Otherwise, return <b>false</b>.
	 * 
	 */
	private boolean checkBoxesAreSelected() {
		return (chckFemale.isSelected() || chckMale.isSelected());
	}

	/**
	 * Verifies if fields in form are empty because they are all mandatory and
	 * should be filled before writing them to database.
	 * <p>
	 * This method loops through variables in the array. Each variable in the array
	 * represent one particular field in the form.
	 * 
	 * @return <b>true</b> if it detects at least one empty field, return
	 *         <b>false</b> otherwise.
	 * 
	 */
	private boolean fieldsAreEmpty() {
		/* initialising each field from form to variables */
		String firstName = txtFirstName.getText();
		String lastName = txtLastName.getText();
		String studentNumber = txtStudentN.getText();
		String degreeCost = txtCost.getText();
		String charges = txtCharges.getText();
		String total = txtTotal.getText();
		String amount = txtAmount.getText();
		String remainder = txtRemainder.getText();

		/* putting variables to an array */
		String fields[] = { firstName, lastName, studentNumber, degreeCost, charges, total, amount, remainder };

		/* looping through fields */
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets all fields to empty, check boxes to enabled and not selected and sets
	 * selected index of combo box to 0 after writing user input to database to one
	 * of the semesters.
	 * 
	 */
	private void resetFields() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtStudentN.setText("");
		txtCost.setText("");
		txtCharges.setText("");
		txtTotal.setText("");
		txtAmount.setText("");
		txtRemainder.setText("");

		chckFemale.setSelected(false);
		chckMale.setSelected(false);
		chckFemale.setEnabled(true);
		chckMale.setEnabled(true);

		cmbPaymentType.setSelectedIndex(0);
	}

	/**
	 * Displays all semesters in {@link #listSemesters}.
	 * <p>
	 * This method does the following : <br>
	 * <ol>
	 * <li>Connects to database that contain all semesters.</li>
	 * <li>Using DatabaseMetaData to get tables (e.g. semesters) from database.</li>
	 * <li>Retrieving only table names (e.g. semesters) from database.</li>
	 * <li>Adds previously retrieved table names (e.g. semesters) to list
	 * {@link #listSemesters}.</li>
	 * <li>Sets all results to DefaultListModel and affecting it to
	 * {@link #listSemesters}.</li>
	 * </ol>
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if problems with creation of statement
	 * 
	 */
	private void showSemestersInList() {
		String[] types = { "TABLE" };
		ResultSet resultSet = null;
		DefaultListModel<String> defaultListModel = new DefaultListModel<String>();

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
				defaultListModel.addElement(tableName);
			}

			listSemesters.setModel(defaultListModel);
			defaultListModel = (DefaultListModel<String>) listSemesters.getModel();

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error : Could not retreive semesters from database. \n Details :  " + e);
		}
	}

	/**
	 * Create new semester with the name given by user.
	 * <p>
	 * This method create new semester (table database) with the name taken from
	 * text field. If semester (table in database) already exists with current name,
	 * user get warning and it does not let add this semester. Otherwise, new
	 * semester created.
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if problems with creation of statement
	 */
	public void createNewSemester() {
		/* getting semester name from user */
		String semesterName = txtNewSemester.getText();

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement()) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class not found while creating new semester. \n Details : " + e);
			}

			/* declaring and using DatabaseMetaData to get table name from database */
			DatabaseMetaData dbm = connection.getMetaData();
			/* declaring and using ResultSet to get actual tables (e.g. semesters) */
			ResultSet tables = dbm.getTables(null, null, semesterName, null);

			/* if table (e.g. semester) with this name already exist, warn user */
			if (tables.next()) {
				JOptionPane.showMessageDialog(null, "Semester " + "\"" + semesterName + "\"" + " already exists");
				return;
			} else {
				/* adding new table (e.g. semester) to database */
				String sql = "CREATE TABLE  " + "`" + semesterName + "`" + "(TransactionNumber INTEGER PRIMARY KEY, "
						+ " StudentNumber VARCHAR(6), " + " FirstName           VARCHAR(45), "
						+ " LastName           VARCHAR(45), " + " Sex        VARCHAR(50), " + "  "
						+ " Total DOUBLE(45), "
						+ " Date Timestamp DATETIME DEFAULT (STRFTIME('%d-%m-%Y', 'NOW','localtime')), "
						+ " Remainder DOUBLE(45))";

				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Could not create new semester. \n Details : " + e);
		}
	}

	/**
	 * Create new semester with the name given by user.
	 * <p>
	 * This method create new semester (table database) with the name taken from
	 * text field. If semester (table in database) already exists with current name,
	 * user get warning and it does not let add this semester. Otherwise, new
	 * semester created.
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if problems with creation of statement
	 * @throws java.lang.ArrayIndexOutOfBoundsException:-1
	 *             if user click on empty list of semesters {@link #listSemesters}
	 */
	private void openSemesterFromList() {
		boolean tableIsEmpty = false;
		boolean tableIsOpened = false;
		ListModel<String> model = listSemesters.getModel();

		if (listSemesters.isSelectionEmpty()) {
			return;
		} else {
			/* affecting to global variable selected by user semester from listSemesters */
			semester = (String) model.getElementAt(listSemesters.getSelectedIndex());

			try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
					Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(
							"SELECT TransactionNumber, StudentNumber, FirstName, LastName, Sex, Total, Remainder FROM "
									+ "`" + semester + "`" + ";")) {
				/* finding and connecting to JDBC_DRIVER */
				try {
					Class.forName(JDBC_DRIVER);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Error : Class not found while creating new semester. \n Details : " + e);
				}
				connection.setAutoCommit(false);

				/*
				 * setting booleans tableIsEmpty and tableIsOpened to true or false depending on
				 * resultSet.
				 */
				if (!resultSet.next()) {
					tableStudents.setModel(DbUtils.resultSetToTableModel(resultSet));
					tableIsEmpty = true;
					tableIsOpened = true;
				} else {
					tableIsOpened = true;
					tableIsEmpty = false;
					getSemesterData();
				}

				/*
				 * if current table is already opened and it is not empty calling method
				 * clearSemesterData() to clear all rows from table and afterwards calling
				 * method getSemesterData() to display same data again.
				 */
				if (tableIsOpened && !tableIsEmpty) {
					clearSemesterData();
					getSemesterData();
				} else {
					/*
					 * table that user opens is empty, so we do not need to clear rows, method
					 * getSemesterData() called directly.
					 */
					tableIsEmpty = true;
					getSemesterData();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error: Could not open table from list. \n Details : " + e1);
			}
		}
	}

	/**
	 * Clears shown data in the semester but DOES NOT delete it from database.
	 * <p>
	 * This method clears rows that has been displayed for semester when user
	 * selects another table.
	 */
	private void clearSemesterData() {
		if (defaultTableModel.getRowCount() > 0) {
			for (int i = defaultTableModel.getRowCount() - 1; i > -1; i--) {
				defaultTableModel.removeRow(i);
			}
		}
	}

	/**
	 * Displays all records that contains selected semester.
	 * <p>
	 * This method selects data from database to display from the semester opened by
	 * user.
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if global variable {@link #semester} is null.
	 */
	private void getSemesterData() {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT TransactionNumber, StudentNumber, FirstName, LastName, Sex, Total, Remainder FROM "
								+ "`" + semester + "`")) {
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
				int iD = resultSet.getInt(1);
				String studentNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String sex = resultSet.getString(5);
				double total = resultSet.getDouble(6);
				double remainder = resultSet.getDouble(7);

				Object[] content = { iD, studentNumber, firstName, lastName, sex, total, remainder };

				defaultTableModel = (DefaultTableModel) tableStudents.getModel();
				defaultTableModel.addRow(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : Could not display data from database. \n Details : " + e);
		}
	}

	/**
	 * Submits new payment to selected semester.
	 * <p>
	 * This method add new payment to the semester that has been chose by user using
	 * preparedStatement. Values that are written to semester are taken directly
	 * from text fields of the form.
	 * 
	 * @throws ClassNotFoundException
	 *             if JDBC_DRIVER was not found.
	 * @throws SQLException
	 *             if could not write data to database or commit changes.
	 * @throws NullPointerException
	 *             if global variable {@link #semester} is null.
	 */
	private void submitNewPayment() {
		/* declaration of variables which take text from form text field */
		String studentNumber = txtStudentN.getText();
		String firstName = txtFirstName.getText();
		String lastName = txtLastName.getText();
		String totalString = txtTotal.getText();
		String remainderString = txtRemainder.getText();
		double totalDouble = Double.parseDouble(totalString);
		double remainderDouble = Double.parseDouble(remainderString);

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement();
				PreparedStatement preparedStatement = connection.prepareStatement("insert into " + "`" + semester + "`"
						+ " (StudentNumber, FirstName, LastName, Sex, Total, Remainder) values (?, ?, ?, ?, ?, ?)")) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class not found for sumbit of new payment. \n Details : " + e);
			}
			connection.setAutoCommit(false);

			/* writing data to database */
			preparedStatement.setString(1, studentNumber);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			if (chckMale.isSelected()) {
				preparedStatement.setString(4, "Male");
			} else {
				preparedStatement.setString(4, "Female");
			}
			preparedStatement.setDouble(5, totalDouble);
			preparedStatement.setDouble(6, remainderDouble);

			preparedStatement.executeUpdate();

			connection.commit();

		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : Could not write data to database. \n Details : " + e1);
		}
	}

	private void searchStudentInSemester(String student) {
		String query = "SELECT TransactionNumber, StudentNumber, FirstName, LastName, Sex, Total, Remainder FROM " + "`"
				+ semester + "`" + " WHERE FirstName LIKE '%" + txtSearch.getText() + "%' OR LastName LIKE '%"
				+ txtSearch.getText() + "%' OR StudentNumber LIKE '%" + txtSearch.getText() + "%'";

		if (semester == null) {
			JOptionPane.showMessageDialog(null, "Select a table to perform research");
			txtSearch.setFocusable(false);
			txtSearch.setFocusable(true);
			return;
		}

		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class not found while searching for student. \nDetails : " + e);
			}

			if (!student.isEmpty()) {
				lblNotFound.setText("");
				tableStudents.setModel(DbUtils.resultSetToTableModel(resultSet));
			} else {
				lblNotFound.setText("");
				tableStudents.setModel(DbUtils.resultSetToTableModel(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Could not find student. \nDetails : " + e);
		}
		searchUsed = true;
	}

	private void selectOnlyUnpayed() {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL_SEMESTERS);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"Select TransactionNumber, StudentNumber, FirstName, LastName, Sex, Total, Remainder FROM "
								+ "`" + semester + "`" + " where Remainder > 0")) {
			/* finding and connecting to JDBC_DRIVER */
			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error : Class not found while selecting non-closed records. \nDetails : " + e);
			}

			connection.setAutoCommit(false);

			while (resultSet.next()) {
				int iD = resultSet.getInt(1);
				String studentNumber = resultSet.getString(2);
				String firstName = resultSet.getString(3);
				String lastName = resultSet.getString(4);
				String sex = resultSet.getString(5);
				double total = resultSet.getDouble(6);
				double remainder = resultSet.getDouble(7);

				Object[] content = { iD, studentNumber, firstName, lastName, sex, total, remainder };

				defaultTableModel = (DefaultTableModel) tableStudents.getModel();
				defaultTableModel.addRow(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Could not select only unpayed records. \nDetails : " + e);
		}
	}

	/**
	 * Verifies if current user that was logged into application has root
	 * permission. If this user has root permission, enables all menus in menuBar
	 * component. Otherwise, set some menus in menuBar component not enabled for the
	 * users without root permission.
	 * <p>
	 * This method calls another {@link #currentUserHasRootPermission()
	 * currentUserHasRootPermission} method which returns either true or false. In
	 * case of true, it sets a component of menuBar, menuAddAdministrator enabled.
	 * Otherwise, it sets a component of menuBar, menuAddAdministrator not enabled.
	 */
	private void lockMenus() {
		if (currentUserHasRootPermission())
			menuAddAdministrator.setEnabled(true);
		else
			menuAddAdministrator.setEnabled(false);
	}
}
