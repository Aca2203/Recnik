package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class FormaV2 extends JFrame {
	private JComboBox<String> poljeRec = new JComboBox<>();
	private JTextArea poljeZnacenje = new JTextArea(5, 20);
	
	public FormaV2() {
		setBounds(700, 300, 400, 400);
		setResizable(false);
		setTitle("Речник");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		popuniProzor();
		dodajOsluskivace();
		popuniRecnik();		
		
		setVisible(true);
	}

	private void popuniProzor() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel recPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		recPanel.add(new JLabel("Реч:", JLabel.RIGHT));
		
		poljeRec.setPreferredSize(new Dimension(200, 25));
		poljeRec.setMaximumSize(new Dimension(200, 25));
		
		recPanel.add(poljeRec);
		
		JPanel znacenjePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		znacenjePanel.add(new JLabel("Значење:", JLabel.RIGHT));
		znacenjePanel.add(poljeZnacenje);
		
		recPanel.setMaximumSize(new Dimension(400, 50));
		znacenjePanel.setMaximumSize(new Dimension(400, 100));
		
		this.add(recPanel);
		this.add(znacenjePanel);
	}

	private void dodajOsluskivace() {
		
	}

	private void popuniRecnik() {
		
	}
	
	public static void main(String[] args) {
		new FormaV2();
	}
}
