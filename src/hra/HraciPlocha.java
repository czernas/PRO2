package hra;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

import obrazek.Obrazek;
import obrazek.ZdrojObrazkuSoubor;

public class HraciPlocha extends JPanel {
	public static final boolean DEBUG = true;
	public static final int VYSKA = 800;
	public static final int SIRKA = 600;
	
	//rychlost bìhu pozadí
	public static final int RYCHLOST = -2;
	
	//musí být alespoò tøi zdi, jinak se první zeï "nestihne posunout" za levy okraj = nestihne zajet za levy okraj hraci plochy
	//drive nez je potreba ji posunou pred pravy okraj hraci plochy a vykreslit
	public static final int POCET_ZDI = 6;
	
	private SeznamZdi seznamZdi;
	private Zed aktualniZed;
	private Zed predchoziZed;
	
	//TODO
	
	
	private Hrac hrac;
	private BufferedImage imgPozadi;
	private Timer casovacAnimace;
	private boolean pauza = false;
	private boolean hraBezi = false;
	private int posunPozadiX = 0;
	
	public HraciPlocha(){
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
	}
	
	private void vyrobZdi(int pocet){
		Zed zed;
		int vzdalenost = HraciPlocha.SIRKA;
		
		for (int i = 0; i < pocet; i++) {
			seznamZdi.add(new Zed(vzdalenost));
			vzdalenost = vzdalenost + (HraciPlocha.SIRKA / 2);
		}
		
		vzdalenost = vzdalenost - HraciPlocha.SIRKA - Zed.SIRKA;
		Zed.setVzdalenostPosledniZdi(vzdalenost);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		//dve pozadi za sebe pro plynule prechody
		//prvni
		g.drawImage(imgPozadi, posunPozadiX, 0, null);
		//druhe je posunuto o sirku obrazku
		g.drawImage(imgPozadi, posunPozadiX + imgPozadi.getWidth(), 0, null);
		
		if (HraciPlocha.DEBUG) {
			g.setColor(Color.WHITE);
			g.drawString("posunPozadiX = " + posunPozadiX, 0, 10);
		}
		
		for (Zed zed : seznamZdi) {
			zed.paint(g);
		}
		
		hrac.paint(g);
		
	}
	
	private void posun(){
		if (!pauza && hraBezi) {
			
			//TODO
			
			for (Zed zed : seznamZdi) {
				zed.posun();
			}
			
			hrac.posun();
			
			//posun pozice pozadi hraci plochy (skrolovani)
			posunPozadiX = posunPozadiX + HraciPlocha.RYCHLOST;
			//kdyz se pozadi cele doposouva, zacni od zacatku
			if (posunPozadiX == -imgPozadi.getWidth()){
				posunPozadiX = 0;
			}
			
		}
	}
	
	private void spustHru(){
		casovacAnimace = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
				posun();
			}
		});
		
		hraBezi = true;
		casovacAnimace.start();
	}
	
	public void pripravHraciPlochu(){
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					//TODO skok hrace
					hrac.skok();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (hraBezi) {
						if (pauza) {
							pauza = false;
						} else{
							pauza = true;
						}
					} else{
						pripravNovouHru();
						spustHru();
					}
				}
			}	
		});
		setSize(SIRKA,VYSKA);
	}

	protected void pripravNovouHru() {
		// TODO
		
		vyrobZdi(POCET_ZDI);
		
	}
}
