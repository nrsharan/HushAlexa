package silence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class AudioFilePlayer {

	/*
	 * public static void main(String[] args) throws URISyntaxException { final
	 * ScheduledExecutorService executorService =
	 * Executors.newSingleThreadScheduledExecutor();
	 * executorService.scheduleAtFixedRate(() -> { try { play(); System.gc(); }
	 * catch (URISyntaxException e) { e.printStackTrace(); } }, 0, 300,
	 * TimeUnit.SECONDS); }
	 */
	private AudioInputStream in1;
	private AudioInputStream in2;

	public AudioFilePlayer() {
		try {
			this.in1 = getAudioInputStream(this.getClass().getClassLoader().getResource("ding.mp3"));
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.in2 = getAudioInputStream(this.getClass().getClassLoader().getResource("quiet.mp3"));
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play(String fileName) throws URISyntaxException {
		try {
			AudioInputStream in;
			if (fileName == "ding.mp3")
				in = in1;
			else
				in = in2;
			final AudioFormat outFormat = getOutFormat(in.getFormat());
			final Info info = new Info(SourceDataLine.class, outFormat);

			try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {

				if (line != null) {
					line.open(outFormat);
					line.start();
					stream(getAudioInputStream(outFormat, in), line);
					line.drain();
					line.stop();
				}
			}

		} catch (LineUnavailableException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static AudioFormat getOutFormat(AudioFormat inFormat) {
		final int ch = inFormat.getChannels();

		final float rate = inFormat.getSampleRate();
		return new AudioFormat(PCM_SIGNED, rate, 8, ch, ch * 2, rate, false);
	}

	private static void stream(AudioInputStream in, SourceDataLine line) throws IOException {
		final byte[] buffer = new byte[4096];
		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
			line.write(buffer, 0, n);
		}
	}

	public static void play2(String fileName) {
		URL url = Thread.currentThread().getClass().getClassLoader().getResource(fileName);
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

}
