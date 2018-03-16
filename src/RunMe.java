
import examples.CountEventTypeExample;
import examples.rsse.calls.BMNMining;

public class RunMe {

	/*
	 * download the interaction data and unzip it into the root of this project (at
	 * the level of the pom.xml). Unpack it, you should now have a folder that
	 * includes a bunch of folders that have dates as names and that contain .zip
	 * files.
	 */
	public static String eventsDir = "Data";

	/*
	 * download the context data and follow the same instructions as before.
	 */
	public static String contextsDir = "Contexts-170503";

	public static void main(String[] args) {

		// BASIC DATA READING
		
		// new GettingStarted(eventsDir).run();
		new CountEventTypeExample(eventsDir).run();
		// new GettingStartedContexts(contextsDir).run();
		
		// RSSE RELATED EXAMPLES
		//new BMNMining().run();
	}
};
