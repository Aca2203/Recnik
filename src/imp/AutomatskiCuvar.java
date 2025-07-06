package imp;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class AutomatskiCuvar extends Thread {
	private String putanja;
	private int vremeCuvanja;
	private ImplementacijaRecnika recnik;
	private int sekunde = 0;
	
	boolean promenjen = false;
	
	public AutomatskiCuvar(ImplementacijaRecnika recnik, String putanja, int vremeCuvanja) {
		this.recnik = recnik;
		this.putanja = putanja;
		this.vremeCuvanja = vremeCuvanja;
		try {
			List<String> linije = Files.readAllLines(Paths.get(putanja));
			List<Element> reci = new ArrayList<>();
			for(String linija: linije) {
				String[] recZnacenje = linija.split("#");
				reci.add(new Element(recZnacenje[0], Integer.parseInt(recZnacenje[1]), recZnacenje[2]));
			}
			recnik.popuni(reci);
			this.start();
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
	
	@Override
	public void run() {
		try {
			while(!isInterrupted()) {
				synchronized (this) {
					while(!promenjen) {
						wait();
					}
				}
				sleep(1000);
				sekunde++;
				
				if(sekunde == vremeCuvanja) {
					sacuvaj();					
					sekunde = 0;
					promenjen = false;
				}
			}
		} catch (InterruptedException e) {}
		  catch (IOException e) {
			  JOptionPane.showMessageDialog(
	               	null,
	                "Грешка при чувању речника у фајл!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
		       );
			  System.exit(-1);
		  }
	}

	public void sacuvaj() throws IOException {
		List<Element> lista = recnik.pretvoriUListu();
		
		new FileWriter(putanja, false).close();
		FileWriter fw = new FileWriter(putanja, true);
		for(Element e: lista) {
			fw.write(e.rec + "#" + e.vrsta + "#" + e.znacenje + "\n");
		}
		fw.close();
	}
}
