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
	
	public boolean ubaci(String rec, int vrsta, String znacenje) {
		Cvor trenutni = koren;
		boolean postoji = true;
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				postoji = false;
				trenutni.deca.put(karakter, new Cvor());
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		if(postoji) return true;
		
		trenutni.krajReci = true;
		trenutni.vrsta = vrsta;
		trenutni.znacenje = znacenje;
		
		return false;
	}
	
	public boolean izmeni(String rec, int vrsta, String znacenje) {
		Cvor trenutni = koren;
		for(char karakter: rec.toCharArray()) {
			if(!trenutni.deca.containsKey(karakter)) {
				return false;
			}
			trenutni = trenutni.deca.get(karakter);
		}
		
		if(!trenutni.krajReci) return false;
		
		trenutni.vrsta = vrsta;
		trenutni.znacenje = znacenje;
		return true;
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
}
