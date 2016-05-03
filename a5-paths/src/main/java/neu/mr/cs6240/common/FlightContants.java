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
	public final static short MINS60 = 60;
	/**
	 * minimum time in minutes before which a connecting flight can depart
	 */
	public final static short MIN_LAYOFF = 30;
	/**
	 * maximum time in minutes before which a connecting flight can depart
	 */
	public final static short MINS_6HRS = 360;
	/**
	 * 1 in the FlightInfo object signifies that the row is a origin row
	 */
	public final static int ORIGIN = 1;
	/**
	 * 1 in the FlightInfo object signifies that the row is a destination row
	 */
	public final static int DEST = 2;
	/**
	 * temporary folder to hold the intermediate output from reducer 1
	 */
	public final static String TEMP_FILE = "temp_file";
	/**
	 * the index of airline in the intermediate output CSV
	 */
	public final static short AIRLINE = 0;
	/**
	 * the year of airline in the intermediate output CSV
	 */
	public final static short YEAR = 1;
	/**
	 * the connections made by the airline in the intermediate output CSV
	 */
	public final static short CONNECTIONS = 2;
	/**
	 * the missed connections made by the airline in the intermediate output CSV
	 */
	public final static short MISSED = 3;

}
