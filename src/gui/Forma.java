package gui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class Forma extends JFrame {
	JTextField poljeRec = new JTextField(20);
	JTextArea poljeZnacenje;
	
	ButtonGroup grupa = new ButtonGroup();
	JRadioButton imenica = new JRadioButton("Именица");
	JRadioButton glagol = new JRadioButton("Глагол");
	JRadioButton pridev = new JRadioButton("Придев");
	
	JButton dodajRec = new JButton("Додај реч");
	JButton izmeniRec = new JButton("Измени реч");
	JButton obrisiRec = new JButton("Обриши реч");
	JButton pretraziRec = new JButton("Претражи реч");
	
	Biranje biranje;
	
	protected Forma(Biranje biranje) {
		this.biranje = biranje;
		setLocationRelativeTo(null);
		
		setResizable(false);
		setTitle("Речник");
	}
	
	protected final void inicializuj() {
		popuniProzor();
		dodajOsluskivace();
		popuniRecnik();
		setVisible(true);
	}
	
	protected final void postaviRadioDugmad() {
		imenica.setActionCommand("0");
		glagol.setActionCommand("1");
		pridev.setActionCommand("2");
		
		grupa.add(imenica);
		grupa.add(glagol);
		grupa.add(pridev);
	}

	protected abstract void popuniProzor();
	protected abstract void dodajOsluskivace();
	protected abstract void popuniRecnik();
}
