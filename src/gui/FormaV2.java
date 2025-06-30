package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import imp.PrefiksnoStablo;

@SuppressWarnings("serial")
public class FormaV2 extends JFrame {
	private static final String PUTANJA = "recnik.txt";
	private static final int VREME_CUVANJA = 20;
	
	private PrefiksnoStablo prefiksnoStablo = new PrefiksnoStablo();
	private JTextField poljeRec = new JTextField(20);
	private JPopupMenu meni = new JPopupMenu();
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
		
		poljeRec.setPreferredSize(new Dimension(250, 20));
		poljeZnacenje.setPreferredSize(new Dimension(200, 20));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		panel.add(new JLabel("Реч:", JLabel.RIGHT), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		panel.add(poljeRec, gbc);
		
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
			public void removeUpdate(DocumentEvent e) {
				azuriraj();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				azuriraj();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		
		poljeRec.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				meni.show(poljeRec, 0, poljeRec.getBounds().height);
				poljeRec.requestFocus();
			}
		});
	}

	private void azuriraj() {
		String prefiks = poljeRec.getText().strip();
        meni.setVisible(false);
        meni.removeAll();

        String[] reci = prefiksnoStablo.vratiReciSaPrefiksom(prefiks);
        
        if (reci == null) return;

        for (String rec : reci) {
            JMenuItem item = new JMenuItem(rec);
            item.addActionListener(e -> {
                poljeRec.setText(rec);
                meni.setVisible(false);
            });
            meni.add(item);
        }

        try {
            meni.show(poljeRec, 0, poljeRec.getBounds().height);
            poljeRec.requestFocus();
        } catch (Exception ex) {}
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
	
	public static void main(String[] args) {
		new FormaV2();
	}
}
