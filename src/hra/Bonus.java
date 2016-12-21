package hra;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bonus {
	public static final int SIRKA = 40;
	public static final int VYSKA = 40;
	public static final int BODY_ZA_BONUS = 50;
	private Random random;
	private BufferedImage img;
	private SeznamZdi seznamZdi;
	private int x;
	private int y;

	public Bonus(BufferedImage img) {
		this.img = img;
		random = new Random();
		x = -200;
		y = -200;
	}

	public void paint(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	public void posun() {
		x = x + Zed.RYCHLOST;

		// kdyz se bonus dostane posunem hraci plochy zprava doleva za konec
		// okna, tak vygeneruj nove honoty pro umisteni bonusu

		if (x <= 0) {
			nastavNovyBonus();
		}
	}

	public void nastavNovyBonus() {
		//osetreni aby se negeneroval do zdi
		boolean stop = false;
		Zed zed;
		
		while( !stop){
			vygenerujNahodneHodnotyProBonus();
			stop = true;
			int i = 0;
			
			while (i < seznamZdi.size()) {
				zed = seznamZdi.get(i);
				
				if (isBonusVygenerovanDoZdi(zed)) {
					stop = false;
					
					i = seznamZdi.size();
				}
				
				i++;
			}
		}
	}

	private void vygenerujNahodneHodnotyProBonus() {
		x = HraciPlocha.SIRKA + (HraciPlocha.SIRKA/2);
		y = random.nextInt(HraciPlocha.VYSKA-100);		
	}
	
	public Rectangle getMez() {
		return new Rectangle(x, y, SIRKA, VYSKA);
	}

	private boolean isBonusVygenerovanDoZdi(Zed zed) {
		return (zed.getMezSpodniCastiZdi().intersects(this.getMez()) || zed.getMezHorniCastiZdi().intersects(this.getMez()));
	}
	
	public void setSeznamZdi(SeznamZdi seznamZdi) {
		this.seznamZdi = seznamZdi;
	}
}
