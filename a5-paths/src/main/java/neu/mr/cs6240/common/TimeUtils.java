package neu.mr.cs6240.common;

/**
 * Time utility functions 
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import org.apache.hadoop.io.IntWritable;

public class TimeUtils {

	/**
	 * @param time
	 *            in mins
	 * @return hour
	 */
	public static int getHour(IntWritable time) {
		return (time.get() / FlightContants.MINS60);
	}

	/**
	 * @param time
	 *            in mins
	 * @return the hour slot for multimap
	 */
	public static int getStart(int time) {
		return (time + FlightContants.MIN_LAYOFF) / FlightContants.MINS60;
	}

	/**
	 * @param time
	 *            in mins
	 * @return the hour slot for multimap
	 */
	public static int getEnd(int time) {
		return ((time + FlightContants.MINS_6HRS) / FlightContants.MINS60);
	}

}
