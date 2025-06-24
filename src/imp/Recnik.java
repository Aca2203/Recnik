package imp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Recnik extends Thread {
	// SC: O(n)
	private String putanja;
	private ArrayList<Element> niz = new ArrayList<>();
	private int iterator = 0;
	private boolean promenjen = false;
	private JLabel labela;
	private int vremeCuvanja;
	private int sekunde = 0;
	
	public Recnik(String putanja, JLabel labela, int vremeCuvanja) throws IOException {
		this.putanja = putanja;
		this.labela = labela;
		this.vremeCuvanja = vremeCuvanja;
		List<String> linije = Files.readAllLines(Paths.get(putanja));
		for(String linija: linije) {
			String[] recZnacenje = linija.split("#");
			niz.add(new Element(recZnacenje[0], Integer.parseInt(recZnacenje[1]), recZnacenje[2]));			
		}
		this.start();
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
					labela.setText("Успешно сачувано!");
					labela.revalidate();
					sekunde = 0;
					promenjen = false;
					sleep(2000);
					labela.setText("");
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
	
	public void pocetak() {
		iterator = 0;
	}
	
	public void sledeci() {
		iterator++;
	}
	
	public boolean kraj() {
		return iterator == niz.size();
	}
	
	public Element dohvati() {
		return niz.get(iterator);
	}
	
	public Element dohvati(int indeks) {
		return niz.get(indeks);
	}
	
	public int dohvatiVelicinu() {
		return niz.size();
	}
	
	public synchronized void ispisi() {
		for(Element e: niz) {
			System.out.println(e.rec + " - " + e.znacenje);
		}
	}
	
	private int binarnaPretraga(String rec) {
		int levi = 0, desni = niz.size() - 1;
		while(levi <= desni) {
			int sredina = levi + (desni - levi) / 2;
			int cmp = niz.get(sredina).rec.compareTo(rec);
			if(cmp == 0) {
				return sredina;
			} else if(cmp > 0) desni = sredina - 1;
			else levi = sredina + 1;			
		}
		
		return -1;
	}
	
	// TC: O(n)
	// SC: O(1)
	public synchronized int ubaci(String rec, int vrsta, String znacenje) {
		int levi = 0, desni = niz.size() - 1;
		while(levi <= desni) {
			int sredina = levi + (desni - levi) / 2;
			int cmp = niz.get(sredina).rec.compareTo(rec);
			if(cmp == 0) {
				return -1;
			} else if(cmp > 0) desni = sredina - 1;
			else levi = sredina + 1;			
		}
		
		niz.add(levi, new Element(rec, vrsta, znacenje));
		promenjen = true;
		notify();
		return levi;
	}
	
	// TC: O(logn)
	// SC: O(1)
	public synchronized int izmeni(String rec, int vrsta, String znacenje) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			Element e = niz.get(indeks);
			if(vrsta != -1) e.vrsta = vrsta;
			e.znacenje = znacenje;
			promenjen = true;
			notify();
			return indeks;
		}
		else return -1;
	}
	
	// TC: O(n)
	// SC: O(1)
	public synchronized int obrisi(String rec) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.remove(indeks);
			promenjen = true;
			notify();
			return indeks;
		}
		else return -1;
	}
	
	public synchronized Element pretrazi(String rec, int[] indeks) {
		indeks[0] = binarnaPretraga(rec);
		if(indeks[0] == -1) return null;
		else return niz.get(indeks[0]);
	}
	
	public synchronized void sacuvaj() throws IOException {
		if(!promenjen) return;
		Path p = Paths.get(putanja);
		List<String> linije = new ArrayList<>();
		for(Element e: niz) {
			linije.add(e.rec + "#" + e.vrsta + "#" + e.znacenje);
		}
		Files.write(p, linije);
	}
}
