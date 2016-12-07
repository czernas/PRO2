package hra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import obrazek.Obrazek;
import obrazek.ZdrojObrazkuSoubor;

public class HraciPlocha extends JPanel {
	public static final boolean DEBUG = true;
	public static final int VYSKA = 800;
	public static final int SIRKA = 600;

	// rychlost bìhu pozadí
	public static final int RYCHLOST = -2;

	// musí být alespoò tøi zdi, jinak se první zeï "nestihne posunout" za levy
	// okraj = nestihne zajet za levy okraj hraci plochy
	// drive nez je potreba ji posunou pred pravy okraj hraci plochy a vykreslit
	public static final int POCET_ZDI = 6;

	private SeznamZdi seznamZdi;
	private Zed aktualniZed;
	private Zed predchoziZed;

	// TODO
	private int skore = 0;
	private JLabel lbSkore;
	private JLabel lbZprava;
	private Font font;
	private Font fontZpravy;

	private Hrac hrac;
	private BufferedImage imgPozadi;
	private Timer casovacAnimace;
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;

	public HraciPlocha() {
		ZdrojObrazkuSoubor z = new ZdrojObrazkuSoubor();
		z.naplnMapu();
		z.setZdroj(Obrazek.POZADI.getKlic());

		try {
			imgPozadi = z.getObrazek();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		z.setZdroj(Obrazek.HRAC.getKlic());
		BufferedImage imgHrac;

		try {
			imgHrac = z.getObrazek();
			hrac = new Hrac(imgHrac);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		z.setZdroj(Obrazek.ZED.getKlic());
		BufferedImage imgZed;
		try {
			imgZed = z.getObrazek();
			Zed.setObrazek(imgZed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		seznamZdi = new SeznamZdi();

		VyrobFontyALabely();
	}

	private void VyrobFontyALabely() {
		font = new Font("Arial", Font.BOLD, 40);
		fontZpravy = new Font("Arial", Font.BOLD, 20);

		this.setLayout(new BorderLayout());

		lbZprava = new JLabel("");
		lbZprava.setFont(fontZpravy);
		lbZprava.setForeground(Color.WHITE);
		lbZprava.setHorizontalAlignment(SwingConstants.CENTER);

		lbSkore = new JLabel("0");
		lbSkore.setFont(font);
		lbSkore.setForeground(Color.GREEN);
		lbSkore.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(lbSkore, BorderLayout.NORTH);
		this.add(lbZprava, BorderLayout.CENTER);

	}

	private void vyrobZdi(int pocet) {
		int vzdalenost = HraciPlocha.SIRKA;

		for (int i = 0; i < pocet; i++) {
			seznamZdi.add(new Zed(vzdalenost));
			vzdalenost = vzdalenost + (HraciPlocha.SIRKA / 2);
		}

		vzdalenost = vzdalenost - HraciPlocha.SIRKA - Zed.SIRKA;
		Zed.setVzdalenostPosledniZdi(vzdalenost);
	}

	public void paint(Graphics g) {
		super.paint(g);
		// dve pozadi za sebe pro plynule prechody
		// prvni
		g.drawImage(imgPozadi, posunPozadiX, 0, null);
		// druhe je posunuto o sirku obrazku
		g.drawImage(imgPozadi, posunPozadiX + imgPozadi.getWidth(), 0, null);

		if (HraciPlocha.DEBUG) {
			g.setColor(Color.WHITE);
			g.drawString("posunPozadiX = " + posunPozadiX, 0, 10);
		}

		for (Zed zed : seznamZdi) {
			zed.paint(g);
		}

		hrac.paint(g);

		lbSkore.paint(g);
		lbZprava.paint(g);

	}

	private void posun() {
		if (!pauza && hraBezi) {
			// nastav zed v poradi
			aktualniZed = seznamZdi.getAktualniZed();

			// nastav predchozi zed
			predchoziZed = seznamZdi.getPredchoziZed();

			// detekce kolizi
			if (isKolizeSeZdi(predchoziZed, hrac) || isKolizeSeZdi(aktualniZed, hrac) || isKolizeSHraniciPlochy(hrac)) {
				ukonciAVyresetujHruPoNarazu();
			} else {

				for (Zed zed : seznamZdi) {
					zed.posun();
				}

				hrac.posun();

				// hrac prosel zdi bez narazu
				// zjistit, kde se nachazi
				// bud pred aktualni zdi - nedelej nic
				// nebo za aktualni zdi - posun dalsi zed v poradi
				// prepocitej skore

				if (hrac.getX() >= aktualniZed.getX()) {
					seznamZdi.nastavDalsiZedNaAktualni();
					zvedniSkoreZed();
					lbSkore.setText(skore + "");
				}
			}

			// posun pozice pozadi hraci plochy (skrolovani)
			posunPozadiX = posunPozadiX + HraciPlocha.RYCHLOST;
			// kdyz se pozadi cele doposouva, zacni od zacatku
			if (posunPozadiX == -imgPozadi.getWidth()) {
				posunPozadiX = 0;
			}

		}
	}

	private void ukonciAVyresetujHruPoNarazu() {
		hraBezi = false;
		casovacAnimace.stop();
		casovacAnimace = null;
		vyresetujHru();
	}

	private boolean isKolizeSeZdi(Zed zed, Hrac hrac) {
		return (zed.getMezSpodniCastiZdi().intersects(hrac.getMez()))
				|| (zed.getMezHorniCastiZdi().intersects(hrac.getMez()));
	}

	private boolean isKolizeSHraniciPlochy(Hrac hrac) {
		return (hrac.getY() <= 0) || (hrac.getY() >= HraciPlocha.VYSKA - hrac.getVyskaHrace() - 40);
	}

	private void spustHru() {
		casovacAnimace = new Timer(20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				posun();
			}
		});

		nastavZpravuPrazdna();
		hraBezi = true;
		casovacAnimace.start();
	}

	public void pripravHraciPlochu() {
		nastavZpravuOvladani();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					// TODO skok hrace
					hrac.skok();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (hraBezi) {
						if (pauza) {
							nastavZpravuPrazdna();
							pauza = false;
						} else {
							nastavZpravuPauza();
							pauza = true;
						}
					} else {
						pripravNovouHru();
						spustHru();
					}
				}
			}
		});
		setSize(SIRKA, VYSKA);
	}

	protected void pripravNovouHru() {
		// TODO

		vyrobZdi(POCET_ZDI);

	}

	private void vyresetujHru() {
		resetujVsechnyZdi();
		hrac.reset();

		// nejprve zobraz stare skore, aby hrac videl kolik bodu ziskal
		lbSkore.setText(skore + "");
		// skore pak vynuluj
		vynulujSkore();
		nastavZpravuNarazDoZdi();
	}

	private void resetujVsechnyZdi() {
		seznamZdi.clear();
	}

	private void vynulujSkore() {
		skore = 0;
	}

	private void zvedniSkoreZed() {
		skore = skore + Zed.BODY_ZA_ZED;
	}

	private void nastavZpravuNarazDoZdi() {
		lbZprava.setFont(font);
		lbZprava.setText("Narazil jsi, zkus to znovu");
	}

	private void nastavZpravuPauza() {
		lbZprava.setFont(font);
		lbZprava.setText("pauza");
	}

	private void nastavZpravuOvladani() {
		lbZprava.setFont(fontZpravy);
		lbZprava.setText("pravy klik = start/stop, levy klik = skok");
	}

	private void nastavZpravuPrazdna() {
		lbZprava.setFont(font);
		lbZprava.setText("");
	}
}
