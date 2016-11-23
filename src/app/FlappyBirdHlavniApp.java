package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import hra.HraciPlocha;

public class FlappyBirdHlavniApp extends JFrame{
	private HraciPlocha hp;
	
	public FlappyBirdHlavniApp() {
		//TODO 
	}
	
	public void initGUI(){
		setSize(HraciPlocha.SIRKA, HraciPlocha.VYSKA);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("NiggaDuck");
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void spust(){
		hp = new HraciPlocha();
		hp.pripravHraciPlochu();
		
		getContentPane().add(hp, "Center");
		hp.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				FlappyBirdHlavniApp app = new FlappyBirdHlavniApp();
				app.initGUI();
				app.spust();
			}
		});

	}

}
