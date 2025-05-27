package gui;
import java.awt.Frame;
import java.awt.event.*;

import imp.Recnik;

@SuppressWarnings("serial")
public class Forma extends Frame {

	private Recnik recnik;
	
	public Forma() {
		setBounds(700, 300, 700, 500);
		setResizable(false);
		setTitle("Речник");
		
		popuniRecnik();
		popuniProzor();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		setVisible(true);
	}
	
	private void popuniRecnik() {
		recnik = new Recnik(fajl);
	}

	private void popuniProzor() {
		
	}

	public static void main(String[] args) {
		new Forma();
	}

}
