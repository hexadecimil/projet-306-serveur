package app.services;


import app.ctrl.IControllerDistanceRadar;
import com.phidget22.*;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:21
 */
public class DistanceRadar extends Thread {

	private double distance;
	private DistanceSensor distanceSensor;
	private IControllerDistanceRadar iControllerDistanceRadar;
	public static final double MINIMAL_DISTANCE = 80;

	public DistanceRadar(IControllerDistanceRadar controller, int hubSerial) throws PhidgetException {
		this.iControllerDistanceRadar = controller;

		DistanceSensor distanceSensor0 = new DistanceSensor();
		distanceSensor0.setIsRemote(true);
		distanceSensor0.setDeviceSerialNumber(hubSerial);

		distanceSensor0.addDistanceChangeListener(new DistanceSensorDistanceChangeListener() {
			public void onDistanceChange(DistanceSensorDistanceChangeEvent e) {
				if (e.getDistance()<=MINIMAL_DISTANCE) {
					System.out.println("mur detecter");
					iControllerDistanceRadar.wallDetected(true);
				} else {
					iControllerDistanceRadar.wallDetected(false);
				}

			}
		});
		distanceSensor0.open(20000);
	}

	@Override
	public void run(){

	}
}