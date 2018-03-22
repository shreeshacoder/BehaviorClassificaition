
import examples.CountEventTypeExample;
import examples.CountTestType;
import examples.EventExamples;
import examples.GettingStarted;
import examples.rsse.calls.BMNMining;

public class RunMe {

	/*
	 * download the interaction data and unzip it into the root of this project (at
	 * the level of the pom.xml). Unpack it, you should now have a folder that
	 * includes a bunch of folders that have dates as names and that contain .zip
	 * files.
	 */
	public static String eventsDir = "DataNew";

	/*
	 * download the context data and follow the same instructions as before.
	 */
	public static String contextsDir = "Contexts-170503";

	public static void main(String[] args) {

		//new EventExamples();
		// BASIC DATA READING
		//System.out.println(EventExamples.findAllUsers());
		//new GettingStarted(eventsDir).run();
		// new GettingStarted(eventsDir).run();
		//new CountTestType(eventsDir).run();
		new CountTestType(eventsDir).run();
		// new GettingStartedContexts(contextsDir).run();
		
		// RSSE RELATED EXAMPLES
		//new BMNMining().run();
	}
};
