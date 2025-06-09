package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import imp.*;

@SuppressWarnings("serial")
public class Forma extends JFrame {

	private static final String PUTANJA = "recnik.txt";
	
	private Recnik recnik;
	private JTable tabela;
	private DefaultTableModel model;
	private JButton dodajRec = new JButton("Додај реч");
	private JButton izmeniRec = new JButton("Измени реч");
	private JButton obrisiRec = new JButton("Обриши реч");
	private JButton pretraziRec = new JButton("Претражи реч");
	private JTextField poljeRec = new JTextField(20);
	private JTextArea poljeZnacenje = new JTextArea(20, 5);
	
	public Forma() {
		setBounds(700, 300, 1000, 600);
		setResizable(false);
		setTitle("Речник");
		
		popuniProzor();
		dodajOsluskivace();
		popuniRecnik();		
		
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
		recnik = new Recnik(PUTANJA);
	}

	private void popuniProzor() {
		this.setLayout(new GridLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel(new GridLayout(0, 1));
		
		model = new DefaultTableModel(new Object[]{"Реч", "Значење"}, 0);
        tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel1.add(scrollPane);
        
		JPanel recZnacenje = new JPanel(new GridLayout(2, 2));
		recZnacenje.add(new JLabel("Реч:"));		
		recZnacenje.add(poljeRec);
		recZnacenje.add(new JLabel("Значење:"));
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);
		recZnacenje.add(poljeZnacenje);
		
		panel2.add(recZnacenje);
		
		JPanel dugmici = new JPanel(new GridLayout(1, 3));		
		
		dugmici.add(dodajRec);
		dugmici.add(izmeniRec);
		dugmici.add(obrisiRec);
		
		panel2.add(dugmici);
		
		panel2.add(pretraziRec);
		
		this.add(panel1);
		this.add(panel2);
	}
	
	private void dodajOsluskivace() {
		dodajRec.addActionListener((ae) -> {
			String rec = poljeRec.getText().strip();
			String znacenje = poljeZnacenje.getText().strip();
			if(rec.isEmpty() || znacenje.isEmpty()) {
				JOptionPane.showMessageDialog(
		                null,
		                "Унесите реч и значење!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			int indeks = recnik.ubaci(rec, znacenje);
			if(indeks == -1) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч већ постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			model.insertRow(indeks, new Object[]{rec, znacenje});			
		});
		
		izmeniRec.addActionListener((ae) -> {
			String rec = poljeRec.getText().strip();
			String znacenje = poljeZnacenje.getText().strip();
			if(rec.isEmpty() || znacenje.isEmpty()) {
				JOptionPane.showMessageDialog(
		                null,
		                "Унесите реч и значење!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			int indeks = recnik.izmeni(rec, znacenje);
			if(indeks == -1) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч не постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			model.setValueAt(znacenje, indeks, 1);
		});
		
		obrisiRec.addActionListener((ae) -> {
			String rec = poljeRec.getText().strip();
			if(rec.isEmpty()) {
				JOptionPane.showMessageDialog(
		                null,
		                "Унесите реч!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			int indeks = recnik.obrisi(rec);
			if(indeks == -1) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч не постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			model.removeRow(indeks);
		});
		
		pretraziRec.addActionListener((ae) -> {
			String rec = poljeRec.getText().strip();
			if(rec.isEmpty()) {
				JOptionPane.showMessageDialog(
		                null,
		                "Унесите реч!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			int[] indeks = new int[1];
			Element e = recnik.pretrazi(rec, indeks);
			if(e == null) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч не постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			poljeZnacenje.setText(e.znacenje);
			tabela.setRowSelectionInterval(indeks[0], indeks[0]);
		});
		
		tabela.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				int row = tabela.rowAtPoint(e.getPoint());	     
		        
		        if (row >= 0) {
		            String rec = (String) tabela.getValueAt(row, 0);
		            String znacenje = (String) tabela.getValueAt(row, 1);
		            poljeRec.setText(rec);
		            poljeZnacenje.setText(znacenje);
		        }
		    }
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					recnik.sacuvaj();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(
			                null,
			                "Грешка при чувању фајла!",
			                "Грешка!",
			                JOptionPane.ERROR_MESSAGE
			        );
				}
				dispose();
			}
		});
	}

	public static void main(String[] args) {
		new Forma();
	}

}
