package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import imp.*;

@SuppressWarnings("serial")
public class Forma extends JFrame {

	private static final String PUTANJA = "recnik.txt";
	private static final int VREME_CUVANJA = 20;
	
	private Recnik recnik;
	private JTable tabela;
	private DefaultTableModel model;
	private JButton dodajRec = new JButton("Додај реч");
	private JButton izmeniRec = new JButton("Измени реч");
	private JButton obrisiRec = new JButton("Обриши реч");
	private JButton pretraziRec = new JButton("Претражи реч");
	private JTextField poljeRec = new JTextField(20);
	private JTextArea poljeZnacenje = new JTextArea(20, 5);
	private JLabel recnikSacuvan = new JLabel();
	
	private boolean programiranoAzuriranje = false;
	
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
		recnik = new Recnik(PUTANJA, recnikSacuvan, VREME_CUVANJA);
	}

	private void popuniProzor() {
		this.setLayout(new GridLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel(new GridLayout(0, 1));
		
		model = new DefaultTableModel(new Object[]{"Реч", "Значење"}, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 1;
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
        tabela.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel1.add(scrollPane);
        panel1.add(recnikSacuvan, BorderLayout.SOUTH);
        
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
		model.addTableModelListener(e -> {
			if(programiranoAzuriranje) return;
			
		    if (e.getType() == TableModelEvent.UPDATE) {
		        int red = e.getFirstRow();
		        String rec = (String) model.getValueAt(red, 0);
		        String znacenje = (String) model.getValueAt(red, 1);
		        recnik.izmeni(rec, znacenje);
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
			programiranoAzuriranje = true;
			model.setValueAt(znacenje, indeks, 1);
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
				poljeZnacenje.setText((String) tabela.getValueAt(tabela.getSelectedRow(), 1));
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
				recnik.interrupt();
				try {
					recnik.sacuvaj();
					recnik.join();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(
			                null,
			                "Грешка!",
			                "Грешка!",
			                JOptionPane.ERROR_MESSAGE
			        );
				}
				dispose();
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
	}

	public static void main(String[] args) {
		new Forma();
	}

	static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
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
            return this;
        }
    }
}
