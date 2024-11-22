package app.services;


import com.phidget22.DCMotor;
import com.phidget22.Net;
import com.phidget22.PhidgetException;
import com.phidget22.ServerType;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:23
 */
public class Rover {

	private DCMotor dcMotor1;
	private DCMotor dcMotor2;
	private boolean isForwarding = false;

	public Rover(int hubSerial) throws PhidgetException {
		this.dcMotor1 = new DCMotor();
		dcMotor1.setIsRemote(true);
		dcMotor1.setHubPort(5);
		dcMotor1.setDeviceSerialNumber(hubSerial);
		dcMotor1.setChannel(0);
		this.dcMotor1.open(10000);
		this.dcMotor2 = new DCMotor();
		dcMotor2.setIsRemote(true);
		dcMotor2.setChannel(1);
		dcMotor2.setHubPort(5);
		dcMotor2.setDeviceSerialNumber(hubSerial);
		this.dcMotor2.open(10000);

	}


	public void sendValue(double speed, double turn) throws PhidgetException {
		if (speed >= 0) {
			isForwarding = true;
		} else {
			isForwarding = false;
		}
		// Normaliser les vitesses pour rester dans l'intervalle [-1.0, 1.0]
		double leftMotorSpeed = Math.max(-1.0, Math.min(1.0, speed + turn));
		double rightMotorSpeed = Math.max(-1.0, Math.min(1.0, speed - turn));
		// Définir les vitesses des moteurs
		dcMotor1.setTargetVelocity(leftMotorSpeed);
		dcMotor2.setTargetVelocity(rightMotorSpeed);
	}

	public boolean getSpeed() throws PhidgetException {
		return isForwarding;
	}

	public void closeDcMotor() throws PhidgetException {
		if (dcMotor1 != null) {
			dcMotor1.close();
		}
		if (dcMotor2 != null) {
			dcMotor2.close();
		}
	}
}