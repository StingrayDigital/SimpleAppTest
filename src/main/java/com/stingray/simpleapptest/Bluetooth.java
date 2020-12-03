package com.stingray.simpleapptest;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

import static com.stingray.simpleapptest.SimpleAppTest.log;

class Bluetooth {
	private static final String RESPONSE = "Greetings from serverland";

	Bluetooth() {
	}

	void startDaemon() throws Exception {
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		log("Address: " + localDevice.getBluetoothAddress());
		log("Name: " + localDevice.getFriendlyName());

		UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
		String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";
		StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

		log("Server Started. Waiting for clients to connect...");
		StreamConnection connection = streamConnNotifier.acceptAndOpen();

		RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
		log("Remote device address: " + dev.getBluetoothAddress());
		log("Remote device name: " + dev.getFriendlyName(true));

		// read string from spp client
		InputStream inStream = connection.openInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		String lineRead = bReader.readLine();
		log("Message from mobile device: " + lineRead);

		// send response to spp client
		OutputStream outStream = connection.openOutputStream();
		PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
		log("Sending response (" + RESPONSE + ")");
		pWriter.write(RESPONSE + "\r\n");
		pWriter.flush();

		pWriter.close();

		streamConnNotifier.close();
	}
}
