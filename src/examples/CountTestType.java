package examples;

import java.io.File;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

public class CountTestType {
	private String dir;

	public CountTestType(String dir) {
		this.dir = dir;
	}

	public void run() {
		Set<String> zips = IoHelper.findAllZips(dir);

		int zipTotal = zips.size();
		int zipCount = 0;
		long duration = 0;
		ChronoUnit unit = ChronoUnit.SECONDS;
		for (String zip : zips) {
			double perc = 100 * zipCount / (double) zipTotal;
			zipCount++;

			System.out.printf("## %s, processing %s... (%d/%d, %.1f%% done)\n", new Date(), zip, zipCount, zipTotal,
					perc);
			File zipFile = Paths.get(dir, zip).toFile();

			int numEvents = 0;
			Map<String, Integer> counts = Maps.newHashMap();

			int printCounter = 0;
			int testCount = 0;
			//int totalCount = 0;
			try (IReadingArchive ra = new ReadingArchive(zipFile)) {
				while (ra.hasNext()) {
					if (printCounter++ % 100 == 0) {
						System.out.printf(".");
					}
					numEvents++;
					IIDEEvent e = ra.getNext(IIDEEvent.class);
					String key = e.getClass().getSimpleName();
					//System.out.println("Sdfsd");
					if (key.equals("TestRunEvent")) {
						duration += unit.between(e.getTriggeredAt(), e.getTriggeredAt());
						testCount++;
					}
					Integer count = counts.get(key);
					if (count == null) {
						counts.put(key, 1);
					} else {
						counts.put(key, count + 1);
					}
				}
			}
			counts.put("<total>", numEvents);

			System.out.printf("\nFound the following events:\n");
			for (String key : counts.keySet()) {
				int count = counts.get(key);
				System.out.printf("%s: %d\n", key, count);
			}
			float percentTest = (float)testCount/(float)numEvents * 100;
			System.out.println(testCount+ " "+ duration);
			//System.out.printf("%d\n", totalCount);
			System.out.println(percentTest);
			System.out.println("\n");
		}

		System.out.printf("Done (%s)\n", new Date());
	}
}