package com.stingray.simpleapptest;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

import static com.stingray.simpleapptest.SimpleAppTest.log;

class Audio {
	private static final int NB_BITS = 16;
	private static final boolean BIG_ENDIAN = true;
	private static final int CHANNELS = 2;
	private static final boolean SIGNED = true;
	private static final int BUFFER_SIZE = 65536;
	private static final int AUDIO_CHUNK_SIZE = 4096;
	private URL mediaUrl;
	private BufferedInputStream bufferedInputStream;
	private AudioInputStream audioInputStream;
	private SourceDataLine line;

	void play(String mediaFilePath) throws Exception {
		if (!mediaFilePath.isEmpty()) {
			if (new File(mediaFilePath).exists()) {
				mediaUrl = new URL(String.format("file://%s", mediaFilePath));
				play();
			} else {
				throw new FileNotFoundException(String.format("File not found: %s", mediaFilePath));
			}
		}
	}

	private void play() throws Exception {
		prepare();

		log(String.format("Playing file: %s", mediaUrl.getPath()));

		int nbByteRead;
		byte[] chunk = new byte[AUDIO_CHUNK_SIZE];
		if (line != null) {
			line.start();
			while ((nbByteRead = bufferedInputStream.read(chunk, 0, chunk.length)) != -1) {
				line.write(chunk, 0, nbByteRead);
			}
			line.stop();
		}

		log(String.format("Finished playing file: %s", mediaUrl.getPath()));
		destroy();
	}

	private void prepare() throws Exception {
		log(String.format("Preparing file: %s", mediaUrl.getPath()));
		audioInputStream = AudioSystem.getAudioInputStream(mediaUrl);
		AudioFormat outputFormat = new AudioFormat(audioInputStream.getFormat().getSampleRate(), NB_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
		line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, outputFormat));
		line.open(outputFormat);
		bufferedInputStream = new BufferedInputStream(AudioSystem.getAudioInputStream(outputFormat, audioInputStream), BUFFER_SIZE);
	}

	private void destroy() {
		try {
			if (line != null) {
				if (line.isRunning()) {
					line.stop();
				}
				line.close();
				line = null;
			}
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
				bufferedInputStream = null;
			}
			if (audioInputStream != null) {
				audioInputStream.close();
				audioInputStream = null;
			}
		} catch (IOException ignored) {
		}
	}
}
