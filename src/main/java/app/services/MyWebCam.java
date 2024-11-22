package app.services;

import app.ctrl.IControllerForWebcam;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;


import java.awt.image.BufferedImage;

/**
 * @author waeberla
 * @version 1.0
 * @created 18-nov.-2024 16:36:23
 */
public class MyWebCam extends Thread {

	public static final double FPS = 60.0;
	private MBFImage frame1;
	private int height;
	private IControllerForWebcam iControllerForWebcam;
	private volatile boolean reading;
	private VideoCapture vc1;
	private int width;

	public MyWebCam(IControllerForWebcam controller) {
		this.iControllerForWebcam = controller;
		setName("ThreadWebcam");
		reading = false;
	}

	private void attend(int mili) {
		try {
			sleep(mili);
		} catch (InterruptedException ex) {
			// n'arrive jamais
		}
	}

	private boolean createWebcam() {
		boolean ok = false;
		try {
			//Création de la webcam avec la résolution comme paramètre
			vc1 = new VideoCapture(480, 240);
			vc1.setFPS(FPS);
			ok = true;
		} catch (VideoCaptureException ex) {
			ex.printStackTrace();
		}
		return ok;
	}

	public int getHeight(){
		return height;
	}

	public int getWidth(){
		return width;
	}

	public boolean isReading(){
		return reading;
	}

	@Override
	public void run() {
		reading = true;
		System.out.println("start run");
		boolean ok = createWebcam();
		if (ok) {
			while (reading) {
				frame1 = vc1.getNextFrame();
				BufferedImage img2 = ImageUtilities.createBufferedImage(frame1);
				iControllerForWebcam.actionWebcamFrame(img2);
			}
			vc1.close();
		}
	}

	/**
	 *
	 * @param reading
	 */
	public void setReading(boolean reading){
		this.reading = reading;
	}
}