package com.stingray.simpleapptest;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class SimpleAppTest {
	private final Audio audio;
	private final Bluetooth bluetooth;

	@Parameter(names = {"--play"})
	private String mediaFilePath = "";

	@Parameter(names = {"--start-bluetooth-daemon"})
	private boolean startBluetoothDaemon = false;

	@Parameter(names = {"--help"})
	private boolean showHelp = false;

	private SimpleAppTest(String[] args) {
		JCommander.newBuilder().addObject(this).build().parse(args);
		audio = new Audio();
		bluetooth = new Bluetooth();
	}

	private void play() throws Exception {
		if (!mediaFilePath.isEmpty()) {
			audio.play(mediaFilePath);
		}
	}

	private void startBluetoothDaemon() throws Exception {
		if (startBluetoothDaemon) {
			bluetooth.startDaemon();
		}
	}

	private void showHelp() {
		String help = "Options:\n" +
				"\t--play\t\t\t\tPlay specified media file\n" +
				"\t--start-bluetooth-daemon\tStart bluetooth daemon and wait for messages\n" +
				"\t--help\t\t\t\tThis page";
		System.out.println(help);
	}

	public static void main(String[] args) throws Exception {
		SimpleAppTest simpleAppTest = new SimpleAppTest(args);
		if (!simpleAppTest.showHelp) {
			simpleAppTest.play();
			simpleAppTest.startBluetoothDaemon();
		} else {
			simpleAppTest.showHelp();
		}
	}

	static void log(String message) {
		System.out.println(String.format("[INFO] %s", message));
	}
}
