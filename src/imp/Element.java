package imp;

public class Element {
	public String rec;
	public int vrsta; // 0 -> именица, 1 -> глагол, 2 -> придев 
	public String znacenje;
	
	Element(String rec, int vrsta, String znacenje){
		this.rec = rec;
		this.vrsta = vrsta;
		this.znacenje = znacenje;
	}
}