package hra;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Hrac {
	public static final int SIRKA = 40;
	public static final int VYSKA = 33;
	// velikost skoku hrace
	private static final int KOEF_ZRYCHLENI = 1;
	// rychlost padu hrace
	private static final int KOEF_RYCHLOST = 2;
	private BufferedImage img = null; // na bufferovaném obrázku zùstává to, co
										// jsme tam nakreslili i po minimalizaci
										// okna.. pokud bychom kreslili rovnou
										// na plátno aplikace, po minimalizaci
										// zmizí..
	// pocatecni x-ova pozice hrace, nemeni se (neskace dopredu ani dozadu)
	private int x;
	// pocatecni y-ova pozice hrace, meni se (skace nahoru / dolu)
	private int y;
	private int rychlost;

	public Hrac(BufferedImage img) {
		this.img = img;
		x = (HraciPlocha.SIRKA / 2) - (img.getWidth() / 2);
		y = HraciPlocha.VYSKA / 2;

		rychlost = KOEF_RYCHLOST;
	}

	/**
	 * Vola se po narazu do zdi / kraje okna (vykresli hrace na puvodni misto)
	 */
	public void reset() {
		y = HraciPlocha.VYSKA / 2;
		rychlost = KOEF_RYCHLOST;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void skok() {
		rychlost = -17;
	}

	/**
	 * Zajistuje pohyb hrace
	 */
	public void posun() {
		rychlost = rychlost + KOEF_ZRYCHLENI;
		y = y + rychlost;
	}

	/**
	 * Vykresleni hrace
	 */
	public void paint(Graphics g) {
		g.drawImage(img, x, y, null);

		
		
		if (HraciPlocha.DEBUG) {
			g.setColor(Color.WHITE);
			g.drawString("[x = " + x + ", y = " + y + ", rychlost = " + rychlost + "]", x, y - 5);
		}
	}

	public int getVyskaHrace() {
		return img.getHeight();
	}

	/**
	 * Vraci pomyslny ctverec/obdelnik, ktery opisuje hrace
	 * 
	 * @return
	 */
	public Rectangle getMez() {
		return new Rectangle(x, y, img.getWidth(), img.getHeight());
	}
}
