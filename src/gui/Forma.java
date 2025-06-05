package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import imp.*;

@SuppressWarnings("serial")
public class Forma extends JFrame {

	private Recnik recnik;
	private DefaultTableModel model;
	
	public Forma() {
		setBounds(700, 300, 1000, 600);
		setResizable(false);
		setTitle("Речник");
		
		popuniProzor();
		popuniRecnik();
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
			popuniTabelu();
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

	private void popuniTabelu() {
		for(recnik.pocetak(); !recnik.kraj(); recnik.sledeci()) {
			Element el = recnik.dohvati();
			model.addRow(new Object[]{el.rec, el.znacenje});
		}
	}

	private void ucitajRecnik() throws IOException {
		List<String> linije = Files.readAllLines(Paths.get("recnik.txt"));
		recnik = new Recnik(linije);
	}

	private void popuniProzor() {
		this.setLayout(new GridLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel(new GridLayout(0, 1));
		
		model = new DefaultTableModel(new Object[]{"Реч", "Значење"}, 0);
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel1.add(scrollPane);
		
		JPanel recZnacenje = new JPanel(new GridLayout(2, 2));
		recZnacenje.add(new JLabel("Реч:"));
		recZnacenje.add(new JTextField(20));
		recZnacenje.add(new JLabel("Значење:"));
		recZnacenje.add(new JTextField(20));
		
		panel2.add(recZnacenje);
		
		JPanel dugmici = new JPanel(new GridLayout(1, 3));
		dugmici.add(new JButton("Додај реч"));
		dugmici.add(new JButton("Измени реч"));
		dugmici.add(new JButton("Обриши реч"));
		
		panel2.add(dugmici);
		
		panel2.add(new JButton("Претражи реч"));
		
		this.add(panel1);
		this.add(panel2);
	}

	public static void main(String[] args) {
		new Forma();
	}

}
