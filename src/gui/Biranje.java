package gui;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Biranje extends JFrame {
	
	private JButton dugmeV1 = new JButton("Табеларни приказ");
	private JButton dugmeV2 = new JButton("Претрага речи");
	
	public Biranje() {
		setSize(400, 100);
		setLocationRelativeTo(null);
		
		setResizable(false);
		setTitle("Изаберите врсту речника");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		popuniProzor();
		dodajOsluskivace();
		
		setVisible(true);
	}	

	private void popuniProzor() {
		
		JPanel content = new JPanel(new GridLayout(1, 2, 10, 0));
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		content.add(dugmeV1);
		content.add(dugmeV2);

		setContentPane(content);
	}
	
	private void dodajOsluskivace() {
		dugmeV1.addActionListener((ae) -> {
			this.dispose();
			new Forma();
		});
		
		dugmeV2.addActionListener((ae) -> {
			this.dispose();
			new FormaV2();
		});
	}

	public static void main(String[] args) {
		new Biranje();
	}
}
