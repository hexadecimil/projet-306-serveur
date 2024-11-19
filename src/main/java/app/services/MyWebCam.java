package app.services;

import app.ctrl.IControllerForWebcam;

/**
 * @author waeberla
 * @version 1.0
 * @created 18-nov.-2024 16:36:23
 */
public class MyWebCam extends Thread {

	public static final double FPS = 60.0;
	//private MBFImage frame1;
	private int height;
	private IControllerForWebcam iControllerForWebcam;
	private volatile boolean reading;
	//private VideoCapture vc1;
	private int width;

	public MyWebCam(){

	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param controller
	 */
	public MyWebCam(int width, int height, IControllerForWebcam controller){

	}

	/**
	 * 
	 * @param mili
	 */
	private void attend(int mili){

	}

	private boolean createWebcam(){
		return false;
	}

	public int getHeight(){
		return 0;
	}

	public int getWidth(){
		return 0;
	}

	public boolean isReading(){
		return false;
	}

	@Override
	public void run(){

	}

	/**
	 * 
	 * @param reading
	 */
	public void setReading(boolean reading){

	}
}//end MyWebCam