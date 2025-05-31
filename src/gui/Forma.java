package gui;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import javax.swing.*;

import imp.Recnik;

@SuppressWarnings("serial")
public class Forma extends JFrame {

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
		recnik = new Recnik(linije);
	}

	private void popuniProzor() {
		this.setLayout(new GridLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel(new GridLayout());
		
		JTable tabela = new JTable()
		panel1.add();
		
		this.add(panel1);
		this.add(panel2);
	}

	public static void main(String[] args) {
		new Forma();
	}

}
