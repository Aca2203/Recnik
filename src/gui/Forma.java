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
public class Forma extends JFrame {
	Recnik recnik;
	private JTable tabela;
	private DefaultTableModel model;
	private JButton dodajRec = new JButton("Додај реч");
	private JButton izmeniRec = new JButton("Измени реч");
	private JButton obrisiRec = new JButton("Обриши реч");
	private JButton pretraziRec = new JButton("Претражи реч");
	private JTextField poljeRec = new JTextField(20);
	private JTextArea poljeZnacenje = new JTextArea(20, 5);
	private JMenuItem igricav1 = new JMenuItem("Понуђени одговори");
	private JMenuItem igricav2 = new JMenuItem("Сам укуцај реч");
	
	private ButtonGroup grupa = new ButtonGroup();
	private JRadioButton imenica = new JRadioButton("Именица");
	private JRadioButton glagol = new JRadioButton("Глагол");
	private JRadioButton pridev = new JRadioButton("Придев");
	
	private boolean programiranoAzuriranje = false;
	
	private Biranje biranje;
	
	public Forma(Biranje biranje) {
		this.biranje = biranje;
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Речник");
		
		popuniProzor();
		dodajOsluskivace();
		popuniRecnik();
		
		setVisible(true);
	}
	
	private void popuniProzor() {
		this.setLayout(new GridLayout());
		
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
		
		imenica.setActionCommand("0");
		glagol.setActionCommand("1");
		pridev.setActionCommand("2");
		
		grupa.add(imenica);
		grupa.add(glagol);
		grupa.add(pridev);
		
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

	private void dodajOsluskivace() {
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
			if(grupa.getSelection() == null) {
				JOptionPane.showMessageDialog(
		                null,
		                "Изаберите врсту речи!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			
			int vrsta = Integer.parseInt(grupa.getSelection().getActionCommand());
			
			int indeks = recnik.ubaci(rec, vrsta, znacenje, true);
			if(indeks == -1) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч већ постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			String vrstaTekst = vrstaUTekst(vrsta);
			model.insertRow(indeks, new Object[]{rec, vrstaTekst, znacenje});
			tabela.setRowSelectionInterval(indeks, indeks);
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
			if(grupa.getSelection() == null) {
				JOptionPane.showMessageDialog(
		                null,
		                "Изаберите врсту речи!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			
			int vrsta = Integer.parseInt(grupa.getSelection().getActionCommand());
			
			int indeks = recnik.izmeni(rec, vrsta, znacenje);
			if(indeks == -1) {
				JOptionPane.showMessageDialog(
		                null,
		                "Реч не постоји у речнику!",
		                "Грешка!",
		                JOptionPane.ERROR_MESSAGE
		        );
				return;
			}
			
			String vrstaTekst = vrstaUTekst(vrsta);
			programiranoAzuriranje = true;
			model.setValueAt(vrstaTekst, indeks, 1);
			model.setValueAt(znacenje, indeks, 2);
			programiranoAzuriranje = false;
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
			if(tabela.getRowCount() > 0) {
				tabela.setRowSelectionInterval(Math.min(indeks, tabela.getRowCount() - 1), Math.min(indeks, tabela.getRowCount() - 1));
				poljeRec.setText((String) tabela.getValueAt(tabela.getSelectedRow(), 0));
				postaviRadioDugmice((String) tabela.getValueAt(tabela.getSelectedRow(), 1));
				poljeZnacenje.setText((String) tabela.getValueAt(tabela.getSelectedRow(), 2));
			}			
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
			postaviRadioDugmice(vrstaUTekst(e.vrsta));
			tabela.setRowSelectionInterval(indeks[0], indeks[0]);
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
	
	private void postaviRadioDugmice(String vrsta) {
		switch (vrsta) {
			case "Именица": {
				imenica.setSelected(true);
				break;
			}
			case "Глагол": {
				glagol.setSelected(true);
				break;
			}
			case "Придев": {
				pridev.setSelected(true);
				break;
			}
			default: {
				break;
			}
		}
	}
	
	private String vrstaUTekst(int v) {
		String vrsta = "";
		switch (v) {
			case 0: {
				vrsta = "Именица";
				break;
			}
				
			case 1: {
				vrsta = "Глагол";
				break;
			}
				
			case 2: {
				vrsta = "Придев";
				break;
			}
			default: {
				break;
			}
		}
		return vrsta;
	}

//	public static void main(String[] args) {
//		new Forma();
//	}

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
		new Forma(null);
	}
}
