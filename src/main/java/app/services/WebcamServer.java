package app.services;

import app.ctrl.Controller;

import java.awt.image.BufferedImage;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:24
 */
public class WebcamServer extends Thread {

	private byte buf[] = new byte[8192];
	private Controller controller;
	DatagramSocket ds;
	private volatile boolean running;
	private DatagramSocket socket;

	public WebcamServer(){

	}

	/**
	 * 
	 * @param c
	 * @exception SocketException
	 */
	public WebcamServer(Controller c)
	  throws SocketException {

	}

	public void run(){

	}

	/**
	 * 
	 * @param frame
	 */
	public void sendFrame(BufferedImage frame){

	}

	/**
	 * 
	 * @param run
	 */
	public void setRunning(boolean run){

	}
}//end WebcamServer