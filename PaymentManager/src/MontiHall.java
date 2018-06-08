
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MontiHall extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableResolver;

	private String GOAT = "Goat";
	private String CAR = "Car";
	private JSpinner numberOfGames;
	DefaultTableModel defaultTableModel;
	private JLabel lblResultNoSwapping;
	private JLabel lblResultWithSwapping;
	private JLabel lblShowNumberGamesNoSwap;
	private JLabel lblShowNumberGamesWithSwap;
	private JLabel lblGames;
	private JLabel label_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MontiHall frame = new MontiHall();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public MontiHall() {
		setSystemLookAndFeel();
		setTitle("Monti Hall Problem Resolver");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (defaultTableModel.getRowCount() > 0) {
					clearRows();
					initialiseTable();
					calculateResults();
				} else {
					initialiseTable();
					calculateResults();
				}
			}
		});
		btnGenerate.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		btnGenerate.setBounds(91, 13, 97, 25);
		contentPane.add(btnGenerate);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 67, 520, 347);
		contentPane.add(scrollPane);

		String[] columns = { "Not Swapping", "Swapping" };
		defaultTableModel = new DefaultTableModel(columns, 0);
		tableResolver = new JTable(defaultTableModel);
		tableResolver.setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		tableResolver.getTableHeader().setFont(new Font("Verdana Pro Light", Font.PLAIN, 18));
		tableResolver.setBackground(Color.WHITE);
		tableResolver.setShowGrid(false);
		scrollPane.setViewportView(tableResolver);

		numberOfGames = new JSpinner();
		numberOfGames.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		numberOfGames.setBounds(358, 14, 70, 22);
		numberOfGames.setValue(10);
		contentPane.add(numberOfGames);

		JLabel lblNumberOfGames = new JLabel("Number of games:");
		lblNumberOfGames.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblNumberOfGames.setBounds(200, 15, 146, 21);
		contentPane.add(lblNumberOfGames);

		JLabel lblProbNoSwapping = new JLabel("Probability to pick car with no swapping out of ");
		lblProbNoSwapping.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		lblProbNoSwapping.setBounds(12, 427, 344, 21);
		contentPane.add(lblProbNoSwapping);

		JLabel lblProbWithSwapping = new JLabel("Probability to pick car with swapping out of");
		lblProbWithSwapping.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		lblProbWithSwapping.setBounds(12, 451, 315, 21);
		contentPane.add(lblProbWithSwapping);

		lblResultNoSwapping = new JLabel("0 %");
		lblResultNoSwapping.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblResultNoSwapping.setBounds(460, 426, 72, 21);
		contentPane.add(lblResultNoSwapping);

		lblResultWithSwapping = new JLabel("0 %");
		lblResultWithSwapping.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblResultWithSwapping.setBounds(460, 450, 72, 21);
		contentPane.add(lblResultWithSwapping);

		lblShowNumberGamesNoSwap = new JLabel("0");
		lblShowNumberGamesNoSwap.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblShowNumberGamesNoSwap.setBounds(339, 427, 37, 21);
		contentPane.add(lblShowNumberGamesNoSwap);

		lblShowNumberGamesWithSwap = new JLabel("0");
		lblShowNumberGamesWithSwap.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 16));
		lblShowNumberGamesWithSwap.setBounds(339, 450, 37, 21);
		contentPane.add(lblShowNumberGamesWithSwap);

		lblGames = new JLabel("games:");
		lblGames.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		lblGames.setBounds(388, 427, 60, 21);
		contentPane.add(lblGames);

		label_2 = new JLabel("games:");
		label_2.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
		label_2.setBounds(388, 451, 52, 21);
		contentPane.add(label_2);
	}

	/**
	 * Initialises table with rows that contain the result of a pick of each game
	 * for each column.
	 * <p>
	 * This method stores in two separate ArrayLists (first ArrayList for column "No
	 * Swapping" and second for column "Swapping") values of rows of each column.
	 * Then, it loops each of ArrayList separately with for loop and counts how many
	 * cars {@link #CAR} are there in the column by incrementing corresponding
	 * variable that serves as a counter for this specific column. Finally,
	 * percentage is calculated by dividing each counter by number of rows in the
	 * table and then multiplied by 100. These results are stored in two different
	 * variables for column "No Swapping" and "Swapping". Then, the result is shown
	 * to user a label in the application.
	 *
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if one of the for loops has condition of i < than the real number
	 *             of games
	 */
	private void initialiseTable() {
		// will contain 1 car and 2 goats
		List<String> doors = new ArrayList<String>();
		// will contain choices without swapping
		List<String> stickToChoicePicks = new ArrayList<String>();
		// will contain choices with swapping
		List<String> swappedPicks = new ArrayList<String>();

		// adding a car and 2 goats to the List
		doors.add(CAR);
		doors.add(GOAT);
		doors.add(GOAT);

		for (int i = 0; i < (int) numberOfGames.getValue(); i++) {
			// shuffling List, so we do not know which door has a car
			Collections.shuffle(doors);

			// getting random number from 0 to 2 as a door position (0 = first door, 1 =
			// second door, 2 = third door)
			int randomPosition = new Random().nextInt(doors.size());
			// getting a random pick of the door (simulates user choice)
			String randomPick = doors.get(randomPosition);
			// calling swap(List<String> doors, int randomPosition) method and affecting it
			// to a String variable
			String swappedPick = swap(doors, randomPosition);

			// putting initial choices to the list (without swapping)
			stickToChoicePicks.add(randomPick);
			// putting choices after swapping
			swappedPicks.add(swappedPick);
		}

		for (int i = 0; i < (int) numberOfGames.getValue(); i++) {
			// initialising column "Not Swapping" of the table with List of choices without
			// swapping and
			// column "Swapping" with choices after swapping
			defaultTableModel.addRow(new Object[] { stickToChoicePicks.get(i), swappedPicks.get(i) });
		}
	}

	private String swap(List<String> doors, int randomPosition) {
		for (int i = 0; i < doors.size(); i++) {
			switch (randomPosition) {
			// if our initial pick is door 1
			case 0:
				// we suppose that goat was shown in door two, and our pick was door one so we
				// switch position to door three
				int newPositionOne = 2;

				if (doors.get(newPositionOne).equals(CAR))
					return CAR;
				break;
			// if our initial pick is door 2
			case 1:
				// we suppose that goat was shown in door three and our pick was door two so we
				// switch to door one
				int newPositionTwo = 0;

				if (doors.get(newPositionTwo).equals(CAR))
					return CAR;
				break;
			// if our initial pick is door 3
			default:
				// we suppose that goat was shown in door one, and our pick was door three so we
				// switch position to door two
				int newPositionThree = 1;

				if (doors.get(newPositionThree).equals(CAR))
					return CAR;
				break;
			}
		}
		return GOAT;
	}

	/**
	 * Clears all rows from table.
	 * <p>
	 * This method clears rows that has been displayed to user when user clicks on
	 * "Generate" button again.
	 * 
	 * @throws NullPointerException
	 *             if table does not contain any row
	 */
	private void clearRows() {
		if (defaultTableModel.getRowCount() > 0) {
			for (int i = defaultTableModel.getRowCount() - 1; i > -1; i--) {
				defaultTableModel.removeRow(i);
			}
		}
	}

	/**
	 * Calculates percentage of success to pick a car by calculating obtained
	 * results from the table.
	 * <p>
	 * This method stores in two separate ArrayLists (first ArrayList for column "No
	 * Swapping" and second for column "Swapping") values of rows of each column.
	 * Then, it loops each of ArrayList separately with for loop and counts how many
	 * cars {@link #CAR} are there in the column by incrementing corresponding
	 * variable that serves as a counter for this specific column. Finally,
	 * percentage is calculated by dividing each counter by number of rows in the
	 * table and then multiplied by 100. These results are stored in two different
	 * variables for column "No Swapping" and "Swapping". Then, the result is shown
	 * to user a label in the application.
	 *
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if one of the for loops has condition of i < than the real number
	 *             of table rows
	 */
	private void calculateResults() {
		// will contain values of "Not Swapping" column of the table
		ArrayList<Object> noSwappingColumn = new ArrayList<Object>();
		// will contain values of "Swapping" column of the table
		ArrayList<Object> swappingColumn = new ArrayList<Object>();
		// serve to know how many cars in column "Not Swapping" of the table
		double counterCarNoSwapping = 0;
		// serve to know how many cars in column "Swapping" of the table
		double counterCarWithSwapping = 0;
		// will contain final percentage of cars of column "Not Swapping" of the table
		double finalResultNoSwapping = 0.0;
		// will contain final percentage of cars of column "Swapping" of the table
		double finalResultWithSwapping = 0.0;

		// putting values to each ArrayList of corresponding columns of the table
		for (int i = 0; i < tableResolver.getRowCount(); i++) {
			noSwappingColumn.add(tableResolver.getModel().getValueAt(i, 0));
			swappingColumn.add(tableResolver.getModel().getValueAt(i, 1));
		}

		// counting amount of cars in "Not Swapping" column of the table
		for (int i = 0; i < tableResolver.getRowCount(); i++) {
			if (noSwappingColumn.get(i).equals(CAR))
				counterCarNoSwapping++;
		}

		// counting amount of cars in "Swapping" column of the table
		for (int i = 0; i < tableResolver.getRowCount(); i++) {
			if (swappingColumn.get(i).equals(CAR))
				counterCarWithSwapping++;
		}

		// calculating percentage for each column
		finalResultNoSwapping = (counterCarNoSwapping / noSwappingColumn.size()) * 100;
		finalResultWithSwapping = (counterCarWithSwapping / noSwappingColumn.size()) * 100;

		// displaying percentage by setting label text
		lblResultNoSwapping.setText(finalResultNoSwapping + " % ");
		lblResultWithSwapping.setText(finalResultWithSwapping + " % ");
		lblShowNumberGamesNoSwap.setText(String.valueOf(noSwappingColumn.size()));
		lblShowNumberGamesWithSwap.setText(String.valueOf(noSwappingColumn.size()));
	}
}
