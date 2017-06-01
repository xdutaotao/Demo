

package com.tutk.ffmpeg;

public class FpsCounter {
	private final int frameCount;
	private int counter = 0;
	boolean start = true;

	private long startTime = 0;

	private String tick = "- fps";

	public FpsCounter(int frameCount) {
		this.frameCount = frameCount;
	}

	public String tick() {
		if (this.start) {
			this.start = false;
			this.startTime = System.nanoTime();
		}
		if (this.counter++ < this.frameCount) {
			return this.tick;
		}

		long stopTime = System.nanoTime();
		double fps = (double) this.frameCount * (1000.0 * 1000.0 * 1000.0)
				/ (double) (stopTime - this.startTime);
		this.startTime = stopTime;
		this.counter = 0;

		this.tick = String.format("%.2f fps", fps);
		return this.tick;
	}
}
