package gui;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import imp.PrefiksnoStablo;

@SuppressWarnings("serial")
public class FormaV2 extends JFrame {
	private static final String PUTANJA = "recnik.txt";
	private static final int VREME_CUVANJA = 20;
	
	private PrefiksnoStablo prefiksnoStablo = new PrefiksnoStablo();
	private JComboBox<String> cmbRec = new JComboBox<>();
	private JTextField poljeRec = (JTextField) cmbRec.getEditor().getEditorComponent();
	private JTextArea poljeZnacenje = new JTextArea(5, 25);
	private ButtonGroup grupa = new ButtonGroup();
	
	private JButton dodajRec = new JButton("Додај реч");
	private JButton izmeniRec = new JButton("Измени реч");
	private JButton obrisiRec = new JButton("Обриши реч");
	private JButton pretraziRec = new JButton("Претражи реч");
	
	public FormaV2() {
		setSize(400, 300);
		setLocationRelativeTo(null);
		
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
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setMaximumSize(new Dimension(400, 200));
		
		cmbRec.setEditable(true);
		cmbRec.setPreferredSize(new Dimension(250, 20));
		poljeZnacenje.setPreferredSize(new Dimension(200, 20));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		panel.add(new JLabel("Реч:", JLabel.RIGHT), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		panel.add(cmbRec, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		panel.add(new JLabel("Значење:", JLabel.RIGHT), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(poljeZnacenje, gbc);
		
		this.add(panel);
		
		JRadioButton imenica = new JRadioButton("Именица");
		JRadioButton glagol = new JRadioButton("Глагол");
		JRadioButton pridev = new JRadioButton("Придев");
		
		imenica.setActionCommand("Именица");
		glagol.setActionCommand("Глагол");
		pridev.setActionCommand("Придев");
		
		grupa.add(imenica);
		grupa.add(glagol);
		grupa.add(pridev);
		
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		panel.setMaximumSize(new Dimension(400, 50));
		panel.add(imenica);
		panel.add(glagol);
		panel.add(pridev);		
		
		this.add(panel);
		
		panel = new JPanel(new GridBagLayout());
		panel.setMaximumSize(new Dimension(400, 100));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		panel.add(dodajRec, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		panel.add(izmeniRec, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		
		panel.add(obrisiRec, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		
		panel.add(pretraziRec, gbc);
		
		this.add(panel);
	}

	private void dodajOsluskivace() {
		poljeRec.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        azuriraj();
		    }		    

			@Override
		    public void removeUpdate(DocumentEvent e) {
		    	azuriraj();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {}		    
		});
	}

	private void popuniRecnik() {
		List<String> linije;
		try {
			linije = Files.readAllLines(Paths.get(PUTANJA));
			for(String linija: linije) {
				String[] recZnacenje = linija.split("#");
				prefiksnoStablo.ubaci(recZnacenje[0], Integer.parseInt(recZnacenje[1]), recZnacenje[2]);
			}
			azuriraj();
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
	
	private void azuriraj() {
		String prefiks = poljeRec.getText();
		//cmbRec.removeAllItems();
		String[] reci = prefiksnoStablo.vratiReciSaPrefiksom(prefiks);
		for(String rec: reci) {
			cmbRec.addItem(rec);
		}
	}
	
	public static void main(String[] args) {
		new FormaV2();
	}
}
