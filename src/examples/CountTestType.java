package examples;

import java.io.File;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.testrunevents.TestResult;
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
		int printCounter = 0;
		int numEvents = 0;

		DayOfWeek dayOfWeek = null;
		DayOfWeek dayOfWeekTest = null;
		ChronoUnit unit = ChronoUnit.HOURS;

		Map<String, Integer> counts = Maps.newHashMap();
		Map<DayOfWeek, Long> totalWork = Maps.newHashMap();

		for (String zip : zips) {
			double perc = 100 * zipCount / (double) zipTotal;
			zipCount++;

			System.out.printf("## %s, processing %s... (%d/%d, %.1f%% done)\n", new Date(), zip, zipCount, zipTotal,
					perc);
			File zipFile = Paths.get(dir, zip).toFile();
			Map<LocalDate, ZonedDateTime> dayWiseActivityFirst = Maps.newHashMap();
			Map<LocalDate, ZonedDateTime> dayWiseActivityLast = Maps.newHashMap();
			try (IReadingArchive ra = new ReadingArchive(zipFile)) {
				while (ra.hasNext()) {
					if (printCounter++ % 100 == 0) {
						System.out.printf(".");
					}
					numEvents++;
					IIDEEvent e = ra.getNext(IIDEEvent.class);
					// TestResult ts = ra.getNext(IIDEEvent.class);
					String key = e.getClass().getSimpleName();
					if (key.equals("ActivityEvent")) {
						if (dayWiseActivityFirst.containsKey(e.getTriggeredAt().toLocalDate())) {
							dayWiseActivityLast.put(e.getTriggeredAt().toLocalDate(), e.getTriggeredAt());
						} else {
							dayWiseActivityFirst.put(e.getTriggeredAt().toLocalDate(), e.getTriggeredAt());
						}
					}

					counts.put("<total>", numEvents);

					Integer count = counts.get(key);
					if (count == null) {
						counts.put(key, 1);
					} else {
						counts.put(key, count + 1);
					}
				}
			}

			for (Map.Entry<LocalDate, ZonedDateTime> entry : dayWiseActivityFirst.entrySet()) {
				System.out.println(entry.getKey() + "/" + entry.getValue());
				dayOfWeek = entry.getValue().getDayOfWeek();
				if (totalWork.containsKey(dayOfWeek)) {
					System.out.println("totalWork.get(dayOfWeek)"+totalWork.get(dayOfWeek));
					System.out.println("entry.getValue()"+entry.getValue());
					System.out.println("dayWiseActivityLast.get(entry.getKey()))"+dayWiseActivityLast.get(entry.getKey()));
					if(dayWiseActivityLast.containsKey(entry.getKey()))
					totalWork.put(dayOfWeek, totalWork.get(dayOfWeek)
							+ unit.between(entry.getValue(), dayWiseActivityLast.get(entry.getKey())));
				}
				else
					totalWork.put(dayOfWeek, unit.between(entry.getValue(), dayWiseActivityLast.get(entry.getKey())));
			}

			System.out.println("\n");
			for (Map.Entry<DayOfWeek, Long> entry : totalWork.entrySet())
				System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.printf("\nFound the following events:\n");
		for (String key : counts.keySet()) {
			int count = counts.get(key);
			System.out.printf("%s: %d\n", key, count);
		}

		System.out.println("\n");
		for (Map.Entry<DayOfWeek, Long> entry : totalWork.entrySet())
			System.out.println(entry.getKey() + " " + entry.getValue());

		System.out.printf("Done (%s)\n", new Date());
	}

}