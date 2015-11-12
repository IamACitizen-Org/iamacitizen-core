package com.iamacitizen.core.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author felipe
 */
public class ImageUtils {

	/**
	 * Gera uma miniatura de um imagem.
	 * @param btyes array de bytes que representa a imagem
	 * @param width largura da imagem
	 * @param height altura da imagem
	 * @return a miniatura
	 */
	public static Image getThumb(byte[] btyes, int width, int height) {
		Image img = null;
		try {
			img = ImageIO.read(new ByteArrayInputStream(btyes));
			img = img.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);

		} catch (IOException ex) {
			//TODO: Desenhar uma imagem de erro padrão para retorno
		}
		return img;
	}
}
