package obrazek;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManazerObrazku {
	private Map<String, BufferedImage> mapaObr;
	private ZdrojObrazku zo;

	public ManazerObrazku(ZdrojObrazku zo) {
		mapaObr = new HashMap<>();
		this.zo = zo;
		this.zo.naplnMapu();
	}

	private void pripravObrazek(Obrazek o) {
		zo.setZdroj(o.getKlic());
		mapaObr.put(o.getKlic(), nactiObrazek(o));
	}

	public void pripravObrazky() {
		pripravObrazek(Obrazek.HRAC);
		pripravObrazek(Obrazek.POZADI);
		pripravObrazek(Obrazek.ZED);
	}

	private BufferedImage nactiObrazek(Obrazek o) {
		BufferedImage img;
		
		try {
			
			img = zo.getObrazek();
			
			if (img != null) {
			
				if ( ! obrazekMaSpravneRozmery(img, o.getSirka(), o.getVyska())){
					img = upravObrazek(img, o.getSirka(), o.getVyska());
				}
				
			} else {
				img = vyrobObrazek(o.getSirka(), o.getVyska(), o.getBarva());
			}
			
		} catch (IOException e) {
			img = vyrobObrazek(o.getSirka(), o.getVyska(), o.getBarva());
		}
		
		return img;
	}

	private BufferedImage upravObrazek(BufferedImage img, int sirka, int vyska) {
		BufferedImage zmenenyImage = new BufferedImage(sirka, vyska, img.getType());
		Graphics2D g = zmenenyImage.createGraphics();
		g.drawImage(img, 0, 0, sirka, vyska, null);
		
		return null;
	}

	private boolean obrazekMaSpravneRozmery(BufferedImage img, int sirka, int vyska) {
		return (img.getWidth() == sirka) && (img.getHeight() == vyska);
	}

	private BufferedImage vyrobObrazek(int sirka, int vyska, Color barva) {
		BufferedImage img = new BufferedImage(sirka, vyska, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = img.createGraphics();
		g.setColor(barva);
		g.fillRect(0, 0, sirka, vyska);
		g.dispose(); //na smazání plátna
		return img;
	}

	public BufferedImage getObrazek(Obrazek o) {
		return mapaObr.get(o.getKlic());
	}
}
