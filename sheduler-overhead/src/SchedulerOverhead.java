
public class SchedulerOverhead {
	public static void main(String[] args) throws InterruptedException {
		var begin = System.currentTimeMillis();
		for (int i = 0; i < 2_000; ++i) {
			Thread.sleep(2);
		}
		var end = System.currentTimeMillis();
		System.out.println("Millis elapsed: " + (end - begin));
		System.out.println("Overhead      : " + (end - begin) / 4_000.0);
	}

}