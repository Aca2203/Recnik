package imp;

import javax.swing.*;

public class Alatke {
	public static Element proveraPoljaRecZnacenje(JTextField poljeRec, JTextArea poljeZnacenje, ButtonGroup grupa) {
		String rec = poljeRec.getText().strip();
		String znacenje = poljeZnacenje.getText().strip();
		if(rec.isEmpty() || znacenje.isEmpty()) {
			JOptionPane.showMessageDialog(
	                null,
	                "Унесите реч и значење!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		if(grupa.getSelection() == null) {
			JOptionPane.showMessageDialog(
	                null,
	                "Изаберите врсту речи!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		
		return new Element(rec, Integer.parseInt(grupa.getSelection().getActionCommand()), znacenje);
	}
	
	public static String proveraPoljeRec(JTextField poljeRec) {
		String rec = poljeRec.getText().strip();
		if(rec.isEmpty()) {
			JOptionPane.showMessageDialog(
	                null,
	                "Унесите реч!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		
		return rec;
	}
	
	public static boolean proveriIndeks(int indeks, String poruka) {
		if(indeks == -1) {
			JOptionPane.showMessageDialog(
	                null,
	                poruka,
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
	
	public static boolean proveraPretrazivanje(Element e) {
		if(e == null) {
			JOptionPane.showMessageDialog(
	                null,
	                "Реч не постоји у речнику!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
	
	public static boolean provera(int povratnaVrednost) {
		if(povratnaVrednost == 0) {
			JOptionPane.showMessageDialog(
	                null,
	                "Реч не постоји у речнику!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
}
