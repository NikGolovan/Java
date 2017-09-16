import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.awt.event.*;
import javax.swing.border.TitledBorder;

public class NotePad extends JFrame {

	final String connectionURL = "jdbc:sqlite:content.db";
	final String jdbcDriver = "org.sqlite.JDBC";

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> listOfNotes;
	private JTextField txtTag;
	private JButton btnBack;
	private JButton btnDelete;
	private JButton btnSave;
	private JPanel newNotePanel;
	private JPanel listOfNotesPanel;
	private JTextArea textArea;

	private String selectedNote; // holds name of a selected table
	private String textInNote; // holds text that user has written in the note
	boolean warnOnDelete = true;
	int counter = 0;
	boolean exists = false;
	boolean newNoteClicked = false;

	private JPanel propertiesPanel;
	private JLabel lblTextSizeNote;
	private JPanel panel;
	private JComboBox<Integer> comboTextNote;
	private JCheckBox checkAutoSave;
	private JCheckBox checkSecuredDelete;
	private JMenuItem mntmAbout;
	private JButton btnBackSettings;
	private JLabel lblAllChangesAre;
	private JPanel panel_1;
	private JLabel lblDeleteAllStickers;
	private JButton btnDeleteAll;

	// launches the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotePad frame = new NotePad();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// get's native system look and feel for the application so it looks natively on
	// all platforms
	private void getNativeLook() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exceptionLookAndFeel) {
			exceptionLookAndFeel.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occured while loading native look and feel: " + exceptionLookAndFeel);
		}
	}

	// SWING design and it's components
	public NotePad() {
		setTitle("MyNotes v1.0");
		setResizable(false);
		getNativeLook();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 485);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Notes");
		mnNewMenu.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewNote = new JMenuItem("New note");
		mntmNewNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mntmNewNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listOfNotesPanel.setVisible(false);
				newNotePanel.setVisible(true);
				newNoteClicked = true;
				txtTag.setEditable(true);
				txtTag.requestFocus();
				txtTag.setText("");
				textArea.setText("");
				comboTextNote.setVisible(false);
				btnBackSettings.setVisible(false);
				btnDeleteAll.setVisible(false);
			}
		});
		mntmNewNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		mnNewMenu.add(mntmNewNote);

		JMenu mnSettings = new JMenu("Settings");
		mnSettings.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		menuBar.add(mnSettings);

		JMenuItem mntmProperties = new JMenuItem("Properties");
		mntmProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mntmProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				propertiesPanel.setVisible(true);
				newNotePanel.setVisible(false);
				listOfNotesPanel.setVisible(false);
				comboTextNote.setVisible(true);
				btnBackSettings.setVisible(true);
				btnDeleteAll.setVisible(true);
				saveNote();
			}
		});
		mntmProperties.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		mnSettings.add(mntmProperties);

		checkAutoSave = new JCheckBox("Auto Save");
		checkAutoSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkAutoSave.isSelected()) {
					setProperty();
				} else {
					setProperty();
				}
			}
		});
		checkAutoSave.setSelected(true);
		checkAutoSave.setFont(new Font("Verdana Pro Light", Font.PLAIN, 15));
		mnSettings.add(checkAutoSave);

		checkSecuredDelete = new JCheckBox("Warn Before Delete");
		checkSecuredDelete.setSelected(true);
		checkSecuredDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkSecuredDelete.isSelected()) {
					setProperty();
					warnOnDelete = true;
				} else {
					setProperty();
					warnOnDelete = false;
				}
			}
		});
		checkSecuredDelete.setFont(new Font("Verdana Pro Light", Font.PLAIN, 15));
		mnSettings.add(checkSecuredDelete);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About aboutWindow = new About();
				aboutWindow.setVisible(true);
				aboutWindow.setLocationRelativeTo(null);
				aboutWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
		mntmAbout.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		listOfNotesPanel = new JPanel();
		listOfNotesPanel.setBackground(Color.WHITE);
		contentPane.add(listOfNotesPanel, "name_184407994984207");
		listOfNotesPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		scrollPane.setBounds(12, 13, 310, 371);
		listOfNotesPanel.add(scrollPane);

		listOfNotes = new JList<String>();
		listOfNotes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (listOfNotes.getSelectedIndex() >= 0) {
					listOfNotesPanel.setVisible(false);
					newNotePanel.setVisible(true);
					openExistingNote();
				}
			}
		});
		listOfNotes.setFont(new Font("Verdana Pro Cond Light", Font.PLAIN, 25));
		scrollPane.setViewportView(listOfNotes);
		listOfNotes.setBackground(Color.WHITE);

		newNotePanel = new JPanel();
		newNotePanel.setBackground(Color.WHITE);
		contentPane.add(newNotePanel, "name_184881845387559");
		newNotePanel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(null);
		scrollPane_1.setBounds(12, 70, 310, 293);
		newNotePanel.add(scrollPane_1);

		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Verdana Pro Light", Font.PLAIN, 20));

		JLabel lblTag = new JLabel("Tag");
		lblTag.setFont(new Font("Verdana Pro Light", Font.ITALIC, 18));
		lblTag.setBounds(12, 29, 56, 28);
		newNotePanel.add(lblTag);

		txtTag = new JTextField();
		txtTag.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				checkForUniqueName();
			}
		});
		txtTag.setBorder(null);
		txtTag.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		txtTag.setBounds(80, 32, 230, 22);
		newNotePanel.add(txtTag);
		txtTag.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(80, 55, 230, 2);
		newNotePanel.add(separator);

		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newNotePanel.setVisible(false);

				if (checkAutoSave.isSelected()) {
					saveNote();
					updateList();
					listOfNotesPanel.setVisible(true);
				} else {
					updateList();
					listOfNotesPanel.setVisible(true);
				}
				deleteIfNoRecords();
				updateList();
			}
		});
		btnBack.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		btnBack.setBounds(12, 376, 97, 25);
		newNotePanel.add(btnBack);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteNote();
				newNotePanel.setVisible(false);
				listOfNotesPanel.setVisible(true);
				updateList();
			}
		});
		btnDelete.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		btnDelete.setBounds(121, 376, 97, 25);
		newNotePanel.add(btnDelete);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tag = txtTag.getText();
				if (!tag.isEmpty()) {
					saveNote();
					updateList();
					newNotePanel.setVisible(false);
					listOfNotesPanel.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "The tag field is empty");
				}
			}
		});
		btnSave.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		btnSave.setBounds(225, 376, 97, 25);
		newNotePanel.add(btnSave);

		propertiesPanel = new JPanel();
		propertiesPanel.setBackground(Color.WHITE);
		contentPane.add(propertiesPanel, "name_188341712098654");
		propertiesPanel.setLayout(null);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Text Size", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Verdana Pro Light", Font.PLAIN, 15), Color.BLACK));
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 13, 310, 82);
		propertiesPanel.add(panel);
		panel.setLayout(null);

		lblTextSizeNote = new JLabel("Text in note");
		lblTextSizeNote.setBounds(12, 24, 221, 38);
		panel.add(lblTextSizeNote);
		lblTextSizeNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 20));

		comboTextNote = new JComboBox<Integer>();
		comboTextNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTextSize();
			}
		});
		comboTextNote.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 20, 25, 30 }));
		comboTextNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		comboTextNote.setBounds(245, 33, 53, 22);
		panel.add(comboTextNote);

		btnBackSettings = new JButton("Back");
		btnBackSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listOfNotesPanel.setVisible(true);
				propertiesPanel.setVisible(false);
				btnDeleteAll.setVisible(false);
				updateList();
			}
		});
		btnBackSettings.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		btnBackSettings.setBounds(12, 361, 78, 25);
		propertiesPanel.add(btnBackSettings);
		
		lblAllChangesAre = new JLabel("All changes are saved dynamically");
		lblAllChangesAre.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		lblAllChangesAre.setBounds(12, 310, 260, 25);
		propertiesPanel.add(lblAllChangesAre);
		
		panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Management", TitledBorder.LEADING, TitledBorder.TOP,

						new Font("Verdana Pro Light", Font.PLAIN, 15), Color.BLACK));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(12, 108, 310, 82);
		propertiesPanel.add(panel_1);
		
		lblDeleteAllStickers = new JLabel("Delete all notes");
		lblDeleteAllStickers.setFont(new Font("Verdana Pro Light", Font.PLAIN, 20));
		lblDeleteAllStickers.setBounds(12, 24, 221, 38);
		panel_1.add(lblDeleteAllStickers);
		
		btnDeleteAll = new JButton("Delete");
		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = { "Yes", "No" };
				int answ = JOptionPane.showOptionDialog(null, "Are you sure you want to delete all notes?", "Confirmation",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (answ == 0) {
					deleteAllNotes();
				}
			}
		});
		btnDeleteAll.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		btnDeleteAll.setBounds(206, 34, 92, 25);
		panel_1.add(btnDeleteAll);
		// calling these methods when application is launched
		dispayAllNotes(); // displaying data
		createConfig(); // creates a configuration file 
		checkIfDeleteIsChecked(); // checks if 'warn on delete' delete check box is selected
		autoSaveOnCloseApplication(); // saves changes when application is closed
	}

	/*
	 *
	 * MyNotes FUNCTIONAL METHODS
	 *
	 */

	// executes statement query
	private void executeStatementQuery(String query) {
		try {
			Class.forName(jdbcDriver);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver: " + e);
		}

		try (Connection connection = DriverManager.getConnection(connectionURL);
				Statement statement = connection.createStatement()) {
			statement.execute(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// executes prepared statement
	private void executePreparedStatement(String query, String text) {
		try {
			Class.forName(jdbcDriver);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver: " + e);
		}

		try (Connection connection = DriverManager.getConnection(connectionURL);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, text);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// displaying all notes in the list from database
	private void dispayAllNotes() {
		Connection connection = null;
		ResultSet resultSet = null;

		DefaultListModel<String> defaultListModel = new DefaultListModel<>();

		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(connectionURL);

			DatabaseMetaData databaseMetaData = connection.getMetaData();
			String[] types = { "TABLE" };
			resultSet = databaseMetaData.getTables(null, null, "%", types);

			while (resultSet.next()) {
				String tableName = resultSet.getString(3);
				defaultListModel.addElement(tableName);
			}

			listOfNotes.setModel(defaultListModel);
			defaultListModel = (DefaultListModel<String>) listOfNotes.getModel();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error while displaying data in the list: " + e);
		}
	}

	// saving current / modified note
	private void saveNote() {
		String tag = txtTag.getText(); // getting the tag for the note from user
		String text = textArea.getText(); // getting the text from user

		checkForUniqueName();

		if (newNoteClicked && exists) {
			newNoteClicked = false;
			exists = false;
		} else if (!exists) {
			String newNote = "CREATE TABLE IF NOT EXISTS" + "`" + tag + "`"
					+ " (ID INTEGER PRIMARY KEY, Text VARCHAR(800))";
			executeStatementQuery(newNote);
			saveText();
			newNoteClicked = false;
			exists = false;
		} else {
			String query = "UPDATE " + "`" + tag + "`" + " SET `Text`=?";
			executePreparedStatement(query, text);
		}
	}

	// checking for unique sticker name
	private void checkForUniqueName() {
		String tag = txtTag.getText();

		try {
			Class.forName(jdbcDriver);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver: " + e);
		}

		String query = "SELECT * FROM sqlite_master WHERE type='table' AND name= " + "'" + tag + "'";

		try (Connection connection = DriverManager.getConnection(connectionURL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if (resultSet.next() && newNoteClicked) {
				btnSave.setEnabled(false);
				exists = true;
			} else {
				btnSave.setEnabled(true);
				exists = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// deletes table if there are no records and user just pressed the button Back
	private void deleteIfNoRecords() {
		String tag = txtTag.getText();
		String text = textArea.getText();

		if (tag.isEmpty() && text.isEmpty()) {
			String newNote = "DROP TABLE " + "`" + tag + "`";
			executeStatementQuery(newNote);
		}
	}

	// deletes all notes in note pad
	private void deleteAllNotes() {	
		for (int i = 0; i < listOfNotes.getModel().getSize(); i++) {
			String tag = listOfNotes.getModel().getElementAt(i);
			String delete = "DROP TABLE " + "`" + tag + "`";
			executeStatementQuery(delete);
		}
	}
	
	// saving text in a note
	private void saveText() {
		String tag = txtTag.getText(); // getting the tag for the note from user
		String text = textArea.getText(); // getting the text from user

		String query = "INSERT INTO " + "`" + tag + "`" + " (Text) values (?)";

		executePreparedStatement(query, text);
	}

	// opening an existing note
	private void openExistingNote() {
		ListModel<String> model = listOfNotes.getModel();
		selectedNote = (String) model.getElementAt(listOfNotes.getSelectedIndex());

		exists = true;

		try {
			Class.forName(jdbcDriver);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find JDBC driver: " + e);
		}

		String query = "SELECT Text FROM " + "`" + selectedNote + "`";

		try (Connection connection = DriverManager.getConnection(connectionURL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				textInNote = resultSet.getString(1);
			}

			txtTag.setEditable(false);
			txtTag.setText(selectedNote);
			textArea.setText(textInNote);

		} catch (Exception exceptionOpenExistingNote) {
			exceptionOpenExistingNote.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occured while opening a note: " + exceptionOpenExistingNote);
		}
	}

	// deleting an existing note
	private void deleteNote() {
		String tag = txtTag.getText(); // getting the tag for the note from user

		if (warnOnDelete) {
			String[] options = { "Yes", "No" };
			int answ = JOptionPane.showOptionDialog(null, "Delete this note?", "Confirmation",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (answ == 0) {
				String newNote = "DROP TABLE IF EXISTS" + "`" + tag + "`";
				executeStatementQuery(newNote);
			}
		} else {
			String newNote = "DROP TABLE IF EXISTS" + "`" + tag + "`";
			executeStatementQuery(newNote);
		}
		updateList();
	}

	// updating list of notes after pressing button Return
	private void updateList() {
		DefaultListModel<String> listModel = (DefaultListModel<String>) listOfNotes.getModel();
		listModel.removeAllElements();

		dispayAllNotes();
	}

	/*
	 *
	 * MyNotes PROPERTIES AND SETTINGS METHODS
	 *
	 */

	// sets property when check box is selected / unselected
	private void setProperty() {

		try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
			Properties properties = new Properties();

			properties.setProperty("AutoSave", String.valueOf(checkAutoSave.isSelected()));
			properties.setProperty("WarnOnDelete", String.valueOf(checkSecuredDelete.isSelected()));
			properties.setProperty("TextSize", String.valueOf(comboTextNote.getSelectedItem()));
			properties.setProperty("Index", String.valueOf(comboTextNote.getSelectedIndex()));
			properties.store(fileOutputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while writing data in properties " + e);
		}
	}

	// applying all properties when application is loaded
	private void applyPropertiesOnLoad() {

		try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String autoSave = properties.getProperty("AutoSave");
			String warnOnDelete = properties.getProperty("WarnOnDelete");
			String textSize = properties.getProperty("TextSize");
			String textSizeIndex = properties.getProperty("Index");

			checkAutoSave.setSelected(Boolean.valueOf(autoSave));
			checkSecuredDelete.setSelected(Boolean.valueOf(warnOnDelete));
			comboTextNote.setSelectedIndex(Integer.valueOf(textSizeIndex));
			textArea.setFont(new Font("Verdana Pro Light", Font.PLAIN, Integer.valueOf(textSize)));

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting properties on load " + e);
		}
	}

	// this method saves the information when the application is closed ONLY IF
	// autoSave is selected
	private void autoSaveOnCloseApplication() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (checkAutoSave.isSelected() && !exists) {
					saveNote();
				}
			}
		});
	}

	// this method checks whenever warn on delete is selected or not and puts it to
	// the boolean value
	private void checkIfDeleteIsChecked() {
		if (checkSecuredDelete.isSelected()) {
			warnOnDelete = true;
		} else {
			warnOnDelete = false;
		}
	}

	// this method checks if a configuration file exists already, if not it'll
	// create one
	private void createConfig() {
		File file = new File("config.properties");

		if (!file.exists()) {
			setProperty();
		} else {
			applyPropertiesOnLoad();
		}
	}

	// dynamically changes text size in properties
	private void changeTextSize() {
		int selection = comboTextNote.getSelectedIndex();

		switch (selection) {
		case 0:
			lblTextSizeNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 20));
			setProperty();
			applyPropertiesOnLoad();
			break;
		case 1:
			lblTextSizeNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 25));
			setProperty();
			applyPropertiesOnLoad();
			break;
		case 2:
			lblTextSizeNote.setFont(new Font("Verdana Pro Light", Font.PLAIN, 30));
			setProperty();
			applyPropertiesOnLoad();
			break;
		}
	}
}
