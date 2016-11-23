package hra;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Zed {

	public static final int SIRKA = 45;
	//rychlost pohybu zdi
	public static final int RYCHLOST = -6;
	public static final int MEZERA_MEZ_HORNI_A_DOLNI_CASTI_ZDI = 200;
	private static int vzdalenostPosledniZdi = 0;
	private Random random;
	
	//TODO
	//ruzne zdi ruzne obrazky => nelze pouzit static
	private static BufferedImage img = null;
	
	//x-ova souradnice zdi (meni se zprava doleva)
	private int x;
	//y-ova souradnice zdi(horní souøadnice spodní èásti zdi)
	private int y;
	private int vyska;
	
	public Zed(int vzdalenostZdiOdZacatkuHraciPlochy) {
		this.x = vzdalenostZdiOdZacatkuHraciPlochy;
		
		random = new Random();
		vygenerujNahodneHodnotyProZed();
	}

	private void vygenerujNahodneHodnotyProZed() {
		//y-ova souradnice horni casti spodni zdi
		y = random.nextInt(HraciPlocha.VYSKA - 400) + MEZERA_MEZ_HORNI_A_DOLNI_CASTI_ZDI;
		//vyska spodni casti zdi
		vyska = HraciPlocha.VYSKA - y;
	}
	
	public void paint(Graphics g){
		//spodni cast zdi
		g.drawImage(img, x, y, null);
		g.drawImage(img, x, (-HraciPlocha.VYSKA) + (y - MEZERA_MEZ_HORNI_A_DOLNI_CASTI_ZDI), null);
	}
	
	public void posun(){
		//posun zdi
		x = x + RYCHLOST;
		
		//kdyz se zed posunem hraci plochy zprava doleva dostane za konec okna, tak nastav nove hodnoty pro umisteni zdi
		if (x <= 0 - Zed.SIRKA) {
			x = Zed.vzdalenostPosledniZdi;
			
			//TODO
		}
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public static void setVzdalenostPosledniZdi(int vzdalenostPosledniZdi){
		Zed.vzdalenostPosledniZdi = vzdalenostPosledniZdi;
	}
	
	public static int getVzdalenostPosledniZdi() {
		return vzdalenostPosledniZdi;
	}
	
	public static void setObrazek(BufferedImage img){
		Zed.img = img;
	}
}
