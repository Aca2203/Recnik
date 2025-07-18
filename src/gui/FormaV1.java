package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import imp.*;

@SuppressWarnings("serial")
public class FormaV1 extends Forma {
	private Recnik recnik;
	
	private JTable tabela;
	private DefaultTableModel model;
	
	private JMenuItem igricav1 = new JMenuItem("Понуђени одговори");
	private JMenuItem igricav2 = new JMenuItem("Сам укуцај реч");	
	
	private boolean programiranoAzuriranje = false;
	
	public FormaV1(Biranje biranje) {
		super(biranje);
		this.inicializuj();
	}
	
	Recnik dohvatiRecnik() {
		return recnik;
	}
	
	@Override
	protected void popuniProzor() {
		setSize(1000, 600);
		this.setLayout(new GridLayout());
		
		poljeZnacenje = new JTextArea(20, 5);
		
		JMenuBar traka = new JMenuBar();
		JMenu meniIgrice = new JMenu("Игрице");
		meniIgrice.add(igricav1);
		meniIgrice.add(igricav2);
		traka.add(meniIgrice);
		this.setJMenuBar(traka);
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel(new GridLayout(0, 1));
		
		model = new DefaultTableModel(new Object[]{"Реч", "Врста", "Значење"}, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 2;
		    }
		};
        tabela = new JTable(model);
        
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabela.getSelectedRow();
                if (selectedRow != -1) {
                	tabela.scrollRectToVisible(tabela.getCellRect(selectedRow, 0, true));
                }
            }
        });
        tabela.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel1.add(scrollPane);
        
		JPanel recZnacenje = new JPanel(new GridLayout(2, 2, 10, 0));
		recZnacenje.add(new JLabel("Реч:", SwingConstants.RIGHT));		
		recZnacenje.add(poljeRec);
		recZnacenje.add(new JLabel("Значење:", SwingConstants.RIGHT));
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);
		recZnacenje.add(poljeZnacenje);
		
		panel2.add(recZnacenje);
		
		postaviRadioDugmad();
		
		JPanel okruzujuciPanelImenica = new JPanel();
		okruzujuciPanelImenica.setLayout(new BoxLayout(okruzujuciPanelImenica, BoxLayout.Y_AXIS));
		okruzujuciPanelImenica.add(Box.createVerticalGlue());

		JPanel panelImenica = new JPanel();
		panelImenica.add(imenica);
		okruzujuciPanelImenica.add(panelImenica);

		okruzujuciPanelImenica.add(Box.createVerticalGlue());
		
		JPanel okruzujuciPanelGlagol = new JPanel();
		okruzujuciPanelGlagol.setLayout(new BoxLayout(okruzujuciPanelGlagol, BoxLayout.Y_AXIS));
		okruzujuciPanelGlagol.add(Box.createVerticalGlue());

		JPanel panelGlagol = new JPanel();
		panelGlagol.add(glagol);
		okruzujuciPanelGlagol.add(panelGlagol);

		okruzujuciPanelGlagol.add(Box.createVerticalGlue());

		JPanel okruzujuciPanelPridev = new JPanel();
		okruzujuciPanelPridev.setLayout(new BoxLayout(okruzujuciPanelPridev, BoxLayout.Y_AXIS));
		okruzujuciPanelPridev.add(Box.createVerticalGlue());

		JPanel panelPridev = new JPanel();
		panelPridev.add(pridev);
		okruzujuciPanelPridev.add(panelPridev);

		okruzujuciPanelPridev.add(Box.createVerticalGlue());

		panel2.add(okruzujuciPanelImenica);
		panel2.add(okruzujuciPanelGlagol);
		panel2.add(okruzujuciPanelPridev);
		
		JPanel dugmici = new JPanel(new GridLayout(1, 3));		
		
		dugmici.add(dodajRec);
		dugmici.add(izmeniRec);
		dugmici.add(obrisiRec);
		
		panel2.add(dugmici);
		
		panel2.add(pretraziRec);
		
		this.add(panel1);
		this.add(panel2);
	}

	@Override
	protected void dodajOsluskivace() {
		super.dodajOsluskivace();
		model.addTableModelListener(e -> {
			if(programiranoAzuriranje) return;
			
		    if (e.getType() == TableModelEvent.UPDATE) {
		        int red = e.getFirstRow();
		        String rec = (String) model.getValueAt(red, 0);
		        String znacenje = (String) model.getValueAt(red, 2);
		        recnik.izmeni(rec, -1, znacenje);
		        poljeZnacenje.setText(znacenje);
		    }
		});
		
		tabela.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				int row = tabela.rowAtPoint(e.getPoint());	     
		        
		        if (row >= 0) {
		            String rec = (String) tabela.getValueAt(row, 0);
		            String vrsta = (String) tabela.getValueAt(row, 1);		            
		            String znacenje = (String) tabela.getValueAt(row, 2);
		            poljeRec.setText(rec);
		            poljeZnacenje.setText(znacenje);
		            
		            postaviRadioDugmice(vrsta);
		        }
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				recnik.cuvar.interrupt();
				try {
					recnik.cuvar.sacuvaj();
					recnik.cuvar.join();
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
		
		poljeZnacenje.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	dodajRec.doClick();
		        }
		    }
		});
		
		igricav1.addActionListener((ae) -> {
			this.setVisible(false);
			new IgricaV1(this);
		});
		
		igricav2.addActionListener((ae) -> {
			this.setVisible(false);
			new IgricaV2(this);
		});
	}
	
	@Override
	protected void popuniRecnik() {
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

	private void ucitajRecnik() throws IOException {		
		recnik = new Recnik();
	}
	
	private void popuniTabelu() {
		List<Element> reci = recnik.pretvoriUListu();
		for(Element e: reci) {
			String vrsta = vrstaUTekst(e.vrsta);
			model.addRow(new Object[]{e.rec, vrsta, e.znacenje});
		}
	}

	static class TextAreaRenderer extends JTextArea implements TableCellRenderer {		
		private Border focusBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
		private Border noFocusBorder = BorderFactory.createEmptyBorder(
		    focusBorder.getBorderInsets(null).top,
		    focusBorder.getBorderInsets(null).left,
		    focusBorder.getBorderInsets(null).bottom,
		    focusBorder.getBorderInsets(null).right
		);
		
		public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            if (hasFocus) {
                setBorder(focusBorder);
            } else {
                setBorder(noFocusBorder);
            }
            return this;
        }
    }
	
	public static void main(String[] args) {
		new FormaV1(null);
	}

	@Override
	protected void dodajElement(Element e) {
		int indeks = recnik.ubaci(e.rec, e.vrsta, e.znacenje, true);
		if(!proveriIndeks(indeks, "Реч већ постоји у речнику!")) return;
		
		String vrstaTekst = vrstaUTekst(e.vrsta);
		model.insertRow(indeks, new Object[]{e.rec, vrstaTekst, e.znacenje});
		tabela.setRowSelectionInterval(indeks, indeks);
	}

	@Override
	protected void izmeniElement(Element e) {
		int indeks = recnik.izmeni(e.rec, e.vrsta, e.znacenje);
		if(!proveriIndeks(indeks, "Реч не постоји у речнику!")) return;
		
		String vrstaTekst = vrstaUTekst(e.vrsta);
		programiranoAzuriranje = true;
		model.setValueAt(vrstaTekst, indeks, 1);
		model.setValueAt(e.znacenje, indeks, 2);
		programiranoAzuriranje = false;
	}

	@Override
	protected void obrisiRec(String rec) {
		int indeks = recnik.obrisi(rec);
		if(!proveriIndeks(indeks, "Реч не постоји у речнику!")) return;
		
		model.removeRow(indeks);
		if(tabela.getRowCount() > 0) {
			tabela.setRowSelectionInterval(Math.min(indeks, tabela.getRowCount() - 1), Math.min(indeks, tabela.getRowCount() - 1));
			poljeRec.setText((String) tabela.getValueAt(tabela.getSelectedRow(), 0));
			postaviRadioDugmice((String) tabela.getValueAt(tabela.getSelectedRow(), 1));
			poljeZnacenje.setText((String) tabela.getValueAt(tabela.getSelectedRow(), 2));
		}	
	}

	@Override
	protected Element pretrazi(String rec, int[] indeks) {
		return recnik.pretrazi(rec, indeks);
	}

	@Override
	protected void oznaciUTabeli(int[] indeks) {
		tabela.setRowSelectionInterval(indeks[0], indeks[0]);
	}
}
