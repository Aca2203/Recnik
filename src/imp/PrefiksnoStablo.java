package imp;

import java.io.IOException;
import java.util.*;

public class PrefiksnoStablo extends ImplementacijaRecnika {
	private static class Cvor {
		Map<Character, Cvor> deca = new TreeMap<>();
		boolean krajReci = false;
		Element element = null;
	}
	
	private Cvor koren = new Cvor();
	
	public PrefiksnoStablo() {
		this.cuvar = new AutomatskiCuvar(this, Podesavanja.PUTANJA, Podesavanja.VREME_CUVANJA);
	}
	
	@Override
	public synchronized int ubaci(String rec, int vrsta, String znacenje, boolean cuvanje) {
		Cvor trenutni = koren;
		boolean postoji = true;
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				postoji = false;
				trenutni.deca.put(karakter, new Cvor());
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		if(postoji) return 1;
		
		trenutni.krajReci = true;
		trenutni.element = new Element(rec, vrsta, znacenje);
		
		if(cuvanje) zapocniTajmer();
		
		return 0;
	}
	
	@Override
	public synchronized int izmeni(String rec, int vrsta, String znacenje) {
		Cvor trenutni = koren;
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return 0;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		if(!trenutni.krajReci) return 0;
		
		trenutni.element.vrsta = vrsta;
		trenutni.element.znacenje = znacenje;
		
		zapocniTajmer();
		
		return 1;
	}
	
	@Override
	public synchronized int obrisi(String rec) {
		int[] povratnaVrednost = {0};
        dfsBrisanje(koren, rec, 0, povratnaVrednost);
        
        zapocniTajmer();
        
        return povratnaVrednost[0];
    }
	
	private boolean dfsBrisanje(Cvor cvor, String rec, int indeks, int[] povratnaVrednost) {
        if (cvor == null) return false;

        if (indeks == rec.length()) {
            if (!cvor.krajReci) return false;
            cvor.krajReci = false;
            povratnaVrednost[0] = 1;

            return jelPrazan(cvor);
        }

        Cvor dete = cvor.deca.get(rec.charAt(indeks));
        boolean daLiDaObriseDete = dfsBrisanje(dete, rec, indeks + 1, povratnaVrednost);

        if (daLiDaObriseDete) {
            cvor.deca.remove(rec.charAt(indeks));

            return !cvor.krajReci && jelPrazan(cvor);
        }

        return false;
    }
	
	@Override
	public synchronized Element pretrazi(String rec, int[] indeks) {
		Cvor trenutni = koren;
		
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return null;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		return trenutni.krajReci ? trenutni.element : null;
	}
	
	public synchronized List<Element> vratiReciSaPrefiksom(String prefiks){
		List<Element> rezultat = new ArrayList<>();
		
		Cvor trenutni = koren;
		if(prefiks == "") {
			for(char karakter: trenutni.deca.keySet()) {
				dfs(rezultat, "" + karakter, trenutni.deca.get(karakter));
			}
			return rezultat;
		}
				
		for(char karakter: prefiks.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return null;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		dfs(rezultat, prefiks, trenutni);
		
		
		return rezultat;
	}

	private void dfs(List<Element> rezultat, String rec, Cvor trenutni) {
		if(trenutni.krajReci) {
			rezultat.add(trenutni.element);
			return;
		}
		
		for(char karakter: trenutni.deca.keySet()) {
			dfs(rezultat, rec + karakter, trenutni.deca.get(karakter));
		}
	}    

    private boolean jelPrazan(Cvor cvor) {
        return cvor.deca.size() == 0;
    }

	@Override
	synchronized void popuni(List<Element> reci) {
		for(Element e: reci) {
			ubaci(e.rec, e.vrsta, e.znacenje, false);
		}
	}

	@Override
	public synchronized List<Element> pretvoriUListu() throws IOException {
		return vratiReciSaPrefiksom("");
	}
}
