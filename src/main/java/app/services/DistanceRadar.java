package app.services;


import app.ctrl.IControllerDistanceRadar;
import com.phidget22.DistanceSensor;
import com.phidget22.DistanceSensorDistanceChangeEvent;
import com.phidget22.DistanceSensorDistanceChangeListener;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:21
 */
public class DistanceRadar extends Thread implements DistanceSensorDistanceChangeListener {

	private double distance;
	private DistanceSensor distanceSensor;
	private IControllerDistanceRadar iControllerDistanceRadar;
	public static final double MINIMAL_DISTANCE = 20;

	public DistanceRadar(){

	}
	/**
	 * 
	 * @param controller
	 */
	public DistanceRadar(IControllerDistanceRadar controller){

	}

	/**
	 * 
	 * @param e
	 */
	public void onDistanceChange(DistanceSensorDistanceChangeEvent e){

	}

	@Override
	public void run(){

	}
}