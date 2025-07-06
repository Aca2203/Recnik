package imp;

import java.util.*;

public class PrefiksnoStablo {
	private static class Cvor {
		Map<Character, Cvor> deca = new TreeMap<>();
		boolean krajReci = false;
		int vrsta = -1;
		String znacenje = null;
	}
	
	private Cvor koren = new Cvor();
	
	public int ubaci(String rec, int vrsta, String znacenje) {
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
		trenutni.vrsta = vrsta;
		trenutni.znacenje = znacenje;
		
		return 0;
	}
	
	public int izmeni(String rec, int vrsta, String znacenje) {
		Cvor trenutni = koren;
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return 0;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		if(!trenutni.krajReci) return 0;
		
		trenutni.vrsta = vrsta;
		trenutni.znacenje = znacenje;
		return 1;
	}
	
	public Element pretrazi(String rec) {
		Cvor trenutni = koren;
		
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return null;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		return trenutni.krajReci ? new Element(rec, trenutni.vrsta, trenutni.znacenje) : null;
	}
	
	public String[] vratiReciSaPrefiksom(String prefiks){
		ArrayList<String> rezultat = new ArrayList<>();
		
		Cvor trenutni = koren;
		if(prefiks == "") {
			for(char karakter: trenutni.deca.keySet()) {
				dfs(rezultat, "" + karakter, trenutni.deca.get(karakter));
			}
			return rezultat.toArray(new String[0]);
		}
				
		for(char karakter: prefiks.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return null;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		dfs(rezultat, prefiks, trenutni);
		
		
		return rezultat.toArray(new String[0]);
	}

	private void dfs(ArrayList<String> rezultat, String rec, Cvor trenutni) {
		if(trenutni.krajReci) {
			rezultat.add(rec);
			return;
		}
		
		for(char karakter: trenutni.deca.keySet()) {
			dfs(rezultat, rec + karakter, trenutni.deca.get(karakter));
		}
	}

	public int obrisi(String rec) {
		int[] povratnaVrednost = {0};
        dfsBrisanje(koren, rec, 0, povratnaVrednost);
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

    private boolean jelPrazan(Cvor cvor) {
        return cvor.deca.size() == 0;
    }
}
