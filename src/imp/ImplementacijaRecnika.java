package imp;

import java.io.IOException;
import java.util.List;

public abstract class ImplementacijaRecnika {
	public AutomatskiCuvar cuvar = new AutomatskiCuvar(this, Podesavanja.PUTANJA, Podesavanja.VREME_CUVANJA);
	
	abstract int ubaci(String rec, int vrsta, String znacenje);
	abstract int izmeni(String rec, int vrsta, String znacenje);
	abstract int obrisi(String rec);
	abstract Element pretrazi(String rec, int[] indeks);
	
	public abstract List<Element> pretvoriUListu() throws IOException;
	
	public void zapocniTajmer() {
		cuvar.promenjen = true;
		cuvar.notify();
	}
}
