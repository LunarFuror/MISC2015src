//package org.lejos.pcsample.usbsend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

/**
 * Compile this program with javac (not nxjc), and run it with java.
 *
 * You need pccomm.jar on the CLASSPATH and the jfantom.dll DLL or liblibnxt.so
 * shared library on the Java library path.
 *
 * Run the program by:
 *
 * java USBSend
 *
 * Your NXT should be running a sample such as USBReceive.
 *
 * @author Lawrie Griffiths Modified by Grayson Lorenz
 *
 */
public class USBSend {
	public NXTConnector			conn	= new NXTConnector();
	public DataInputStream		inDat;
	public DataOutputStream	outDat;
	
	public USBSend() {
		while (!conn.connectTo("usb://")) {
			conn.addLogListener(new NXTCommLogListener() {
				public void logEvent(String message) {
					System.out.println("USBSend Log.listener: " + message);
				}
				
				public void logEvent(Throwable throwable) {
					System.out.println("USBSend Log.listener - stack trace: ");
					throwable.printStackTrace();
				}
			});
			
		}
		inDat = new DataInputStream(conn.getInputStream());
		outDat = new DataOutputStream(conn.getOutputStream());
		
	}
	
	public int getSensor() {
		int x = 0;
		// try to write out
		try {
			outDat.writeInt(1);
			outDat.flush();
		} catch (IOException ioe) {
			System.err.println("IO Exception writing bytes");
		}
		// try to read response
		try {
			x = inDat.readInt();
		} catch (IOException ioe) {
			System.err.println("IO Exception reading reply");
		}
		// print response and return it
		// System.out.println("Sent " + 1 + " Received " + x);
		return x;
	}
	
	public void closeStreams() {
		// try to close all the streamss
		try {
			inDat.close();
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
		
		try {
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
	}
}
