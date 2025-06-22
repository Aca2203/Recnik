package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class IgricaV1 extends JFrame {
	private JFrame roditelj;
	
	public IgricaV1(JFrame roditelj) {
		this.roditelj = roditelj;
		
		setBounds(700, 300, 500, 300);
		setResizable(false);
		setTitle("Игрица верзија 1");
		
		dodajOsluskivace();
		
		setVisible(true);
	}

	private void dodajOsluskivace() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {				
				dispose();
				roditelj.setVisible(true);
			}
		});
	}
}
