package imp;
import java.util.*;

public class Recnik {
	// SC: O(n)
	private ArrayList<Element> niz = new ArrayList<>();
	private int iterator = 0;
	
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
	
	public Recnik(List<String> linije) {
		for(String linija: linije) {
			String[] recZnacenje = linija.split("#");
			niz.add(new Element(recZnacenje[0], recZnacenje[1]));			
		}		
	}
	
	public void ispisi() {
		for(Element e: niz) {
			System.out.println(e.rec + " " + e.znacenje);
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
		return levi;
	}
	
	// TC: O(logn)
	// SC: O(1)
	public boolean izmeni(String rec, String znacenje) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.add(indeks, new Element(rec, znacenje));
			return true;
		}
		else return false;
	}
	
	// TC: O(n)
	// SC: O(1)
	public boolean obrisi(String rec) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.remove(indeks);
			return true;
		}
		else return false;
	}
}
