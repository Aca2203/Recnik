package imp;
import java.io.IOException;
import java.util.*;

public class Recnik extends ImplementacijaRecnika {
	// SC: O(n)
	private List<Element> niz;
	private int iterator;
	
	public Recnik() {
		this.niz = new ArrayList<>();
		this.iterator = 0;
		this.cuvar = new AutomatskiCuvar(this, Podesavanja.PUTANJA, Podesavanja.VREME_CUVANJA);
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
	@Override
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
		zapocniTajmer();
		return levi;
	}
	
	// TC: O(logn)
	// SC: O(1)
	@Override
	public synchronized int izmeni(String rec, int vrsta, String znacenje) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			Element e = niz.get(indeks);
			if(vrsta != -1) e.vrsta = vrsta;
			e.znacenje = znacenje;
			zapocniTajmer();
			return indeks;
		}
		else return -1;
	}
	
	// TC: O(n)
	// SC: O(1)
	@Override
	public synchronized int obrisi(String rec) {
		int indeks = binarnaPretraga(rec);
		if(indeks != -1) {
			niz.remove(indeks);
			zapocniTajmer();
			return indeks;
		}
		else return -1;
	}
	
	@Override
	public synchronized Element pretrazi(String rec, int[] indeks) {
		indeks[0] = binarnaPretraga(rec);
		if(indeks[0] == -1) return null;
		else return niz.get(indeks[0]);
	}

	@Override
	public List<Element> pretvoriUListu() throws IOException {
		return niz;
	}

	@Override
	void popuni(List<Element> reci) {
		this.niz = reci;
	}
}
