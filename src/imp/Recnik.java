package imp;
import java.io.File;
import java.util.ArrayList;

class Element{
	String rec;
	String znacenje;
	
	Element(String rec, String znacenje){
		this.rec = rec;
		this.znacenje = znacenje;
	}
}

public class Recnik {
	// SC: O(n)
	private ArrayList<Element> niz = new ArrayList<>();
	
	public Recnik(File fajl) {
		
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
	public boolean ubaci(String rec, String znacenje) {
		int levi = 0, desni = niz.size() - 1;
		while(levi <= desni) {
			int sredina = levi + (desni - levi) / 2;
			int cmp = niz.get(sredina).rec.compareTo(rec);
			if(cmp == 0) {
				return false;
			} else if(cmp > 0) desni = sredina - 1;
			else levi = sredina + 1;			
		}
		
		niz.add(levi, new Element(rec, znacenje));
		return true;
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
