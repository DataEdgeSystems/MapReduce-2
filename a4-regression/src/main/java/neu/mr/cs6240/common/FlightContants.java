package neu.mr.cs6240.common;

/**
 * @info Maintains global constants
 *
 * @author Ajay Subramanya
 * @author Smitha Bangalore Naresh
 */
public class FlightContants {
	/**
	 * to convert from cents to dollars
	 */
	public final static short CENTS = 100;
	/**
	 * the cheapest airline for the given N
	 */
	public final static String CHEAPEST_AIRLINE = "cheapest_airline";
	/**
	 * the intermediate file name (between job1 and job2)
	 */
	public final static String INTERMEDIATE_OUTPUT_PATH_VAL = "intermediate_output";
	/**
	 * the key for the configuration
	 */
	public final static String INTERMEDIATE_OUTPUT_PATH_KEY = "temp_output";
	/**
	 * the scheduled time for which we need to figure out the average ticket
	 * price
	 */
	public final static String SCHEDULE_TIME_MINS = "N";
}
