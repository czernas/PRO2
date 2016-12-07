package obrazek;

import java.awt.Color;

import hra.Hrac;
import hra.HraciPlocha;
import hra.Zed;

public enum Obrazek {
	HRAC("hrac", Hrac.SIRKA, Hrac.VYSKA, new Color(255, 255, 255)), POZADI("pozadi", HraciPlocha.SIRKA * 3,
			HraciPlocha.VYSKA, new Color(0, 0, 150)), ZED("zed", Zed.SIRKA, HraciPlocha.VYSKA, new Color(120, 0, 0));

	// pocet prvku
	private static final int size = Obrazek.values().length;

	// pole pro iteraci
	private static final Obrazek[] obrazky = Obrazek.values();

	private final String klic;
	private final int sirka;
	private final int vyska;
	private final Color barva;

	private Obrazek(String klic, int sirka, int vyska, Color barva) {
		this.klic = klic;
		this.sirka = sirka;
		this.vyska = vyska;
		this.barva = barva;
	}

	public String getKlic() {
		return klic;
	}

	public int getSirka() {
		return sirka;
	}

	public int getVyska() {
		return vyska;
	}

	public Color getBarva() {
		return barva;
	}

	public static int getSize() {
		return size;
	}

	public static Obrazek[] getObrazky() {
		return obrazky;
	}
}
