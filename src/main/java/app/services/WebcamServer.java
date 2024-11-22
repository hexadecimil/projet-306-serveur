package app.services;

import app.ctrl.Controller;
import app.ctrl.IControllerForWebcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class WebcamServer {

	private IControllerForWebcam controller;
	private DatagramSocket ds;
	private String ipClient;

	// Constructeur pour initialiser le serveur avec le controller
	public WebcamServer(Controller c, String ipClient) throws SocketException {
		this.controller = c;
		this.ipClient = ipClient;
		ds = new DatagramSocket();  // Crée un DatagramSocket pour envoyer des paquets
	}

	public void sendFrame(BufferedImage frame) {
		if (ipClient == null) {
			System.out.println("Erreur : Adresse IP du client est null.");
			return;
		}
		if (frame == null) {
			System.out.println("Erreur : L'image passée est null.");
			return;
		}

		System.out.println("Dimensions de l'image : " + frame.getWidth() + "x" + frame.getHeight());
		System.out.println("Type de l'image : " + frame.getType());

		// Vérifiez la compatibilité avec JPG
		if (!ImageIO.getImageWritersByFormatName("jpg").hasNext()) {
			System.out.println("Erreur : Aucun writer pour JPG trouvé.");
			return;
		}

		// Convertir l'image en RGB si nécessaire
		if (frame.getType() != BufferedImage.TYPE_INT_RGB) {
			BufferedImage rgbImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = rgbImage.createGraphics();
			g.drawImage(frame, 0, 0, null);
			g.dispose();
			frame = rgbImage;
			System.out.println("Image convertie en TYPE_INT_RGB.");
		}

		int targetPort = 3100; // Port du client

		try {
			InetAddress targetAddress = InetAddress.getByName(ipClient);
			System.out.println("Envoi de l'image à " + targetAddress + ":" + targetPort);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			boolean isWritten = ImageIO.write(frame, "jpg", baos);
			if (!isWritten) {
				System.out.println("Erreur : Impossible d'écrire l'image au format JPG.");
				return;
			}
			baos.flush();
			byte[] imageBytes = baos.toByteArray();

			int dataSize = imageBytes.length;
			System.out.println("Taille de l'image (en bytes) : " + dataSize);

			if (dataSize > 65507) {
				System.out.println("Erreur : La taille des données dépasse la limite UDP.");
				return;
			}

			DatagramPacket packet = new DatagramPacket(imageBytes, dataSize, targetAddress, targetPort);
			ds.send(packet);
			System.out.println("Image envoyée avec succès.");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'envoi de l'image.");
		}
	}




	// Méthode pour fermer le socket si nécessaire
	public void close() {
		if (ds != null && !ds.isClosed()) {
			ds.close();
		}
	}

	public void setIpClient(String ipClient) {
		this.ipClient = ipClient;
	}
}