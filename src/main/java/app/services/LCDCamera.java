package app.services;


import com.phidget22.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:22
 */
public class LCDCamera {

	private LCD lcd;

	public LCDCamera(int hubSerial) throws PhidgetException {
		this.lcd = new LCD();
		lcd.setIsRemote(true);
		lcd.setHubPort(3);
		lcd.setChannel(0);
		lcd.setDeviceSerialNumber(hubSerial);

		lcd.open(50000000); // Timeout de 2 secondes
		lcd.setBacklight(1);
		System.out.println("opened");

	}

	public void close() throws PhidgetException {
		if (lcd != null) {
			lcd.close();
		}
	}

	/**
	 * 
	 * @param frame
	 */
	public void sendFrame(BufferedImage frame) throws PhidgetException, IOException {
		sendFrameToLCD(frame);
	}

	/**
	 * 
	 * @param frame
	 */
	private void sendFrameToLCD(BufferedImage frame) throws IOException, PhidgetException {
		lcd.writeBitmap(0, 0, 128, 64, convertImage(frame));
		lcd.flush();
	}

	private static byte[] convertImage(BufferedImage image) throws IOException {
		int width = 128;
		int height = 64;
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		resizedImage.getGraphics().drawImage(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

		// Convertir l'image en un tableau de bytes pour le bitmap
		byte[] bitmap = new byte[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = resizedImage.getRGB(x, y);
				// Vérifier si le pixel est noir (0xFF000000) ou blanc (0xFFFFFFFF)
				bitmap[y * width + x] = (byte) ((pixel & 0xFFFFF0) == 0x000000 ? 1 : 0);
				// 1 pour noir, 0 pour blanc

			}
		}

		return bitmap;
	}
}