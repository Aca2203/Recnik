package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class IgricaV1 extends JFrame {
	private static final int brojOpcija = 4;
	
	private JFrame roditelj;
	private JRadioButton[][] opcije = {
		{ new JRadioButton("Опција 1"), new JRadioButton("Опција 2"), },
		{ new JRadioButton("Опција 3"), new JRadioButton("Опција 4") }
	};
	private JButton potvrdi = new JButton("Потврди одговор");
	
	private JTextArea poljeZnacenje = new JTextArea(4, 20);
	
	public IgricaV1(JFrame roditelj) {
		this.roditelj = roditelj;
		
		setBounds(700, 300, 500, 300);
		setResizable(false);
		setTitle("Игрица верзија 1");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		popuniProzor();
		dodajOsluskivace();
		
		setVisible(true);
	}

	private void popuniProzor() {		
		poljeZnacenje.setEditable(false);
		poljeZnacenje.setFocusable(false);
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);

		poljeZnacenje.setText("простор испред главног улаза у неку грађевину или дворану, често делимично или потпуно затворен");
        
		JPanel panel = new JPanel(new GridBagLayout());
		ButtonGroup grupa = new ButtonGroup();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(25, 50, 25, 50);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
		
		for(int i = 0; i < brojOpcija / 2; i++) {
			for(int j = 0; j < brojOpcija / 2; j++) {
				gbc.gridx = i;
				gbc.gridy = j;
				
				grupa.add(opcije[i][j]);
				panel.add(opcije[i][j], gbc);
			}			
		}
        
		this.add(poljeZnacenje);
        this.add(panel);
        JPanel tmp = new JPanel();
        tmp.add(potvrdi);
        this.add(tmp);
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
