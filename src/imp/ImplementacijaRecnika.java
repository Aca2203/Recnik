package imp;

import java.io.IOException;
import java.util.List;

public abstract class ImplementacijaRecnika {
	public AutomatskiCuvar cuvar;
	
	abstract void popuni(List<Element> reci);
	abstract int ubaci(String rec, int vrsta, String znacenje);
	abstract int izmeni(String rec, int vrsta, String znacenje);
	abstract int obrisi(String rec);
	abstract Element pretrazi(String rec, int[] indeks);
	
	public abstract List<Element> pretvoriUListu() throws IOException;
	
	public void zapocniTajmer() {
		synchronized (cuvar) {
			cuvar.promenjen = true;
			cuvar.notify();
		}		
	}
}
