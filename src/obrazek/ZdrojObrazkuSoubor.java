package obrazek;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ZdrojObrazkuSoubor extends ZdrojObrazku {
	private static final String CESTA = "img/";

	@Override
	public void naplnMapu() {
		getMapa().put(Obrazek.POZADI.getKlic(), "pozadi.png");
		getMapa().put(Obrazek.HRAC.getKlic(), "hrac.png");
		getMapa().put(Obrazek.ZED.getKlic(), "zed.png");
		getMapa().put(Obrazek.BONUS.getKlic(), "bonus.png");
		// TODO pro hrace, zed a bonus
	}

	@Override
	public BufferedImage getObrazek() throws IOException {
		// TODO Auto-generated method stub
		return ImageIO.read(new File(CESTA + getZdroj()));
	}

}
