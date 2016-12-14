package hra;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bonus {
	public static final int SIRKA = 40;
	public static final int VYSKA = 40;
	public static final int BODY_ZA_BONUS = 5;
	private Random random;
	private BufferedImage img;
	private SeznamZdi seznamZdi;
	private int x;
	private int y;
	
	public void paint(Graphics g) {
		g.drawImage(img, x, y, null);
	}
	
	public void posun(){
		x = x + Zed.RYCHLOST;
	}
}
