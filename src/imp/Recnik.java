package imp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Recnik {
	// SC: O(n)
	private String putanja;
	private ArrayList<Element> niz = new ArrayList<>();
	private int iterator = 0;
	private boolean promenjen = false;
	
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
	
	public Recnik(String putanja) throws IOException {
		this.putanja = putanja;
		List<String> linije = Files.readAllLines(Paths.get(putanja));
		for(String linija: linije) {
			String[] recZnacenje = linija.split("#");
			niz.add(new Element(recZnacenje[0], recZnacenje[1]));			
		}		
	}
	
	public void ispisi() {
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
	public int ubaci(String rec, String znacenje) {
		int levi = 0, desni = niz.size() - 1;
		while(levi <= desni) {
			int sredina = levi + (desni - levi) / 2;
			int cmp = niz.get(sredina).rec.compareTo(rec);
			if(cmp == 0) {
				return -1;
			} else if(cmp > 0) desni = sredina - 1;
			else levi = sredina + 1;			
		}
		
		niz.add(levi, new Element(rec, znacenje));
		promenjen = true;
		return levi;
	}
	
	// TC: O(logn)
	// SC: O(1)
	public int izmeni(String rec, String znacenje) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.get(indeks).znacenje = znacenje;
			promenjen = true;
			return indeks;
		}
		else return -1;
	}
	
	// TC: O(n)
	// SC: O(1)
	public int obrisi(String rec) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.remove(indeks);
			promenjen = true;
			return indeks;
		}
		else return -1;
	}
	
	public Element pretrazi(String rec) {
		int indeks = binarnaPretraga(rec);
		if(indeks == -1) return null;
		else return niz.get(indeks);
	}
	
	public void sacuvaj() throws IOException {
		if(!promenjen) return;
		Path p = Paths.get(putanja);
		List<String> linije = new ArrayList<>();
		for(Element e: niz) {
			linije.add(e.rec + "#" + e.znacenje);
		}
		Files.write(p, linije);
	}
}
