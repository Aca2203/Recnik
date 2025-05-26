package gui;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class Forma extends Frame {

	public Forma() {
		setBounds(700, 300, 700, 500);
		setResizable(false);
		setTitle("Речник");
		
		fillWindow();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		setVisible(true);
	}
	
	private void fillWindow() {
		
	}

	public static void main(String[] args) {
		new Forma();
	}

}
