package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import imp.Element;
import imp.PrefiksnoStablo;

@SuppressWarnings("serial")
public class FormaV2 extends Forma {
	private PrefiksnoStablo prefiksnoStablo;
	
	private JPopupMenu meni = new JPopupMenu();

	public FormaV2(Biranje biranje) {
		super(biranje);
		this.inicializuj();
	}
	
	@Override
	protected void popuniProzor() {
		setSize(400, 300);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		poljeZnacenje = new JTextArea(5, 25);
		
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
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);
		panel.add(poljeZnacenje, gbc);
		
		this.add(panel);		
		
		postaviRadioDugmad();
		
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

	@Override
	protected void dodajOsluskivace() {
		super.dodajOsluskivace();
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
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				prefiksnoStablo.cuvar.interrupt();
				try {
					prefiksnoStablo.cuvar.sacuvaj();
					prefiksnoStablo.cuvar.join();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(
			                null,
			                "Грешка!",
			                "Грешка!",
			                JOptionPane.ERROR_MESSAGE
			        );
				}
				dispose();
				if(biranje != null) biranje.setVisible(true);
			}
		});
	}
	
	@Override
	protected void popuniRecnik() {
		prefiksnoStablo = new PrefiksnoStablo();
		azuriraj();
	}

	private void azuriraj() {
		String prefiks = poljeRec.getText().strip();
        meni.setVisible(false);
        meni.removeAll();

        List<Element> elementi = prefiksnoStablo.vratiReciSaPrefiksom(prefiks);
        
        if (elementi == null) return;

        for (Element el : elementi) {
            JMenuItem item = new JMenuItem(el.rec);
            item.addActionListener(e -> {
                poljeRec.setText(el.rec);
                meni.setVisible(false);
            });
            meni.add(item);
        }

        try {
            meni.show(poljeRec, 0, poljeRec.getBounds().height);
            poljeRec.requestFocus();
        } catch (Exception ex) {}
	}	
	
	public static void main(String[] args) {
		new FormaV2(null);
	}

	@Override
	protected void dodajElement(Element e) {
		int postoji = prefiksnoStablo.ubaci(e.rec, e.vrsta, e.znacenje, true);
		if(postoji == 1) {
			JOptionPane.showMessageDialog(
	                null,
	                "Реч већ постоји у речнику!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return;
		}
		
		azuriraj();
	}

	@Override
	protected void izmeniElement(Element e) {
		int povratnaVrednost = prefiksnoStablo.izmeni(e.rec, e.vrsta, e.znacenje);
		provera(povratnaVrednost);
	}

	@Override
	protected void obrisiRec(String rec) {
		int povratnaVrednost = prefiksnoStablo.obrisi(rec);
		if(!provera(povratnaVrednost)) return;
		
		poljeRec.setText("");
		poljeZnacenje.setText("");
		grupa.clearSelection();
	}

	@Override
	protected Element pretrazi(String rec, int[] indeks) {
		return prefiksnoStablo.pretrazi(rec, indeks);
	}

	@Override
	protected void oznaciUTabeli(int[] indeks) {}
}
