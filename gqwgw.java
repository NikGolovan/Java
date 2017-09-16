import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtRegularCalculator;
	private JTextField txtFactorial;
	private JButton btnCalculateFactorial;
	private JButton btnResetFactorial;
	private JLabel lblResultFactorial;
	private JList listFactorial;
	private JTextArea textArea;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator frame = new Calculator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// this method sets native cross-platform look and feel for the application
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error has occured while setting native look and feel: " + e);
		}
	}

	public Calculator() {
		setResizable(false);
		setTitle("MyCalculator");
		setLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 450);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 600, 389);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		tabbedPane.addTab("Regular Calculator", null, panel, null);
		panel.setLayout(null);
		
		txtRegularCalculator = new JTextField();
		txtRegularCalculator.setBounds(12, 13, 206, 22);
		panel.add(txtRegularCalculator);
		txtRegularCalculator.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Factorial", null, panel_1, null);
		panel_1.setLayout(null);
		
		txtFactorial = new JTextField();
		txtFactorial.setColumns(10);
		txtFactorial.setBounds(12, 13, 206, 22);
		panel_1.add(txtFactorial);
		
		btnCalculateFactorial = new JButton("Calculate");
		btnCalculateFactorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int numberToFactorize = Integer.valueOf(txtFactorial.getText());
				calculateFactorial(numberToFactorize);
			}
		});
		btnCalculateFactorial.setBounds(121, 48, 97, 25);
		panel_1.add(btnCalculateFactorial);
		
		btnResetFactorial = new JButton("Reset");
		btnResetFactorial.setBounds(12, 48, 97, 25);
		panel_1.add(btnResetFactorial);
		
		lblResultFactorial = new JLabel("New label");
		lblResultFactorial.setBounds(250, 16, 122, 16);
		panel_1.add(lblResultFactorial);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(371, 29, 212, 317);
		panel_1.add(scrollPane);
		
		listFactorial = new JList<Integer>();
		scrollPane.setViewportView(listFactorial);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Verdana Pro Light", Font.PLAIN, 16));
		textArea.setBounds(12, 106, 347, 115);
		panel_1.add(textArea);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Bases", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Matrix", null, panel_3, null);
	}
	
	private void calculateFactorial(int numberToFactorize) {
		BigInteger result = BigInteger.ONE;
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		for (int i = 1; i <= numberToFactorize; i++) {
			result = result.multiply(BigInteger.valueOf(i));
			values.add(i);
		}
		
		textArea.append("1) Multiply given number step by step: \n");
		for (int j = 0; j < values.size(); j++) {
			if (j != values.size()-1) {
				textArea.append(values.get(j).toString() + " x ");
			}
			if (j == values.size()-1) {
				textArea.append(values.get(j) + " = " + result.toString());
			}
		}
		textArea.append("\nResult: " + result.toString());
		lblResultFactorial.setText(String.valueOf(result));
	}
}
