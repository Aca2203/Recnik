package gui;
import java.awt.Frame;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JOptionPane;

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
		try {
			ucitajRecnik();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
	                null,
	                "Фајл не постоји!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			System.exit(1);
		}
		
	}

	private void ucitajRecnik() throws IOException {
		List<String> linije = Files.readAllLines(Paths.get("recnik.txt"));
		
	}

	private void popuniProzor() {
		
	}

	public static void main(String[] args) {
		new Forma();
	}

}
