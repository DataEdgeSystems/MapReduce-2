package neu.mr.cs6240.a5_paths;

/**
 * The reducer gets all the data for an airline and an airport, a key could
 * look something like UA12266, which means airline UA and airport id 12266.
 * Values are all the details about the airport except one interesting variable
 * called index, for the mapper we emit two entries for a row. One for the
 * origin airport and the other for the destination airport.
 *
 * The data structure we have used here is, a TreeMap holding a LinkedMultiMap
 * TreeMap<YMD, ListMultimap<Integer, UnpackFlightInfo>>
 *
 * YMD is an object holding year, month and day.
 * ListMultiMap has Scheduled departure time as the key and the unpacked flight
 * info got from the mapper as value. Since the value implements Writable we
 * could not use it as is.
 *
 * @author Smitha Naresh Bangalore
 * @author Ajay Subramanya
 */

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import neu.mr.cs6240.common.FlightContants;
import neu.mr.cs6240.common.TimeUtils;

public class PathsReducer1 extends Reducer<Text, FlightInfo, Text, Text> {

	@Override
	protected void reduce(Text cmpKey, Iterable<FlightInfo> fInfo,
			Reducer<Text, FlightInfo, Text, Text>.Context context) throws IOException, InterruptedException {

		TreeMap<YMD, ListMultimap<Integer, UnpackFlightInfo>> tm = new TreeMap<>(new YMD());
		List<UnpackFlightInfo> arrFlights = new LinkedList<>();

		/**
		 * Insert data to TreeMap -> sorted on (Year, Month, Day) as Key
		 * ListMulitMap as value -> (CRSDepTime /60) as Key and List of
		 * departing flights as value
		 */
		for (FlightInfo fNew : fInfo) {
			UnpackFlightInfo unpack = unpackFlightObj(fNew);
			if (fNew.getIndex().get() == 2) {
				arrFlights.add(unpack);
			} else {
				addToMap(tm, unpack);
			}
		}

		/**
		 * Airlines for this airport is not providing connections if there are
		 * no departing flights, hence we need not consider them for connections
		 */
		if (tm.size() != 0) {
			arrivalsConnections(cmpKey, context, tm, arrFlights);
		}

	}

	/**
	 * loops over all the arriving flights in the airport of the reducer and
	 * finds the number of connections and missed connections
	 * 
	 * @param cmpKey
	 *            the composite key contains airline name and
	 *            (Origin|Destination)Airport ID
	 * @param context
	 * @param tm
	 *            TreeMap holding the data for a flight
	 * @param arrFlights
	 *            list of all the flights that arrive in the airport
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void arrivalsConnections(Text cmpKey, Reducer<Text, FlightInfo, Text, Text>.Context context,
			TreeMap<YMD, ListMultimap<Integer, UnpackFlightInfo>> tm, List<UnpackFlightInfo> arrFlights)
					throws IOException, InterruptedException {
		int numOfConn = 0;
		int missedConn = 0;
		String[] airlineFromKey = cmpKey.toString().split(" ");

		Calendar cal = Calendar.getInstance();
		HashMap<Integer, Integer[]> hmYear = new HashMap<>();

		for (UnpackFlightInfo fArr : arrFlights) {

			YMD arrYMD = new YMD(fArr.getYear(), fArr.getMonth(), fArr.getDay());

			numOfConn = 0;
			missedConn = 0;
			int year = arrYMD.getYear();
			// get arrival time
			int crsArrTime = fArr.getCrsArrTime();
			int start = TimeUtils.getStart(crsArrTime);
			int end = TimeUtils.getEnd(crsArrTime);

			while (start <= end) {
				if (start > 23) {
					/**
					 * check for next days flights for connections Ex. : flight
					 * arriving at 2000 can have connections next day also(as
					 * window is 30 + 6hrs)
					 */

					YMD nextDay = getNextDayYMD(cal, arrYMD);
					int[] res = connection(tm, fArr, start % 24, nextDay, true);
					numOfConn += res[0];
					missedConn += res[1];
				} else {
					// for flights that are departing today and arriving
					// tomorrow
					YMD ymdCur = arrYMD;
					if (fArr.getCrsDepTime() > fArr.getCrsArrTime()) {
						ymdCur = getNextDayYMD(cal, arrYMD);

					}
					int[] res = connection(tm, fArr, start, ymdCur, false);
					numOfConn += res[0];
					missedConn += res[1];
				}
				start++;
			}

			if (hmYear.containsKey(year)) {
				hmYear.get(year)[0] = hmYear.get(year)[0] + numOfConn;
				hmYear.get(year)[1] = hmYear.get(year)[1] + missedConn;
			} else {
				Integer[] cm = new Integer[] { numOfConn, missedConn };
				hmYear.put(year, cm);
			}
		}

		for (int yr : hmYear.keySet()) {
			context.write(new Text(airlineFromKey[0] + ","),
					new Text(yr + "," + hmYear.get(yr)[0] + "," + hmYear.get(yr)[1]));
		}
	}

	/**
	 * Add the departing flights information to TreeMap where Key is YMD object
	 * for a flight and value is ListMultiMap ListMultiMap - > Key is CRS
	 * DepTime / 60 i.e. Hours slots Values are UnpackFlightInfo leaving during
	 * that hour window
	 *
	 * @param tm
	 *            tree map holding all departing flights
	 * @param unpack
	 *            unpacked FlightInfo object
	 */
	private void addToMap(TreeMap<YMD, ListMultimap<Integer, UnpackFlightInfo>> tm, UnpackFlightInfo unpack) {
		YMD ymd = new YMD(unpack.getYear(), unpack.getMonth(), unpack.getDay());
		if (tm.containsKey(ymd)) {
			tm.get(ymd).put((unpack.getCrsDepTime() / FlightContants.MINS60), unpack);
		} else {
			ListMultimap<Integer, UnpackFlightInfo> mm = ArrayListMultimap.create();
			mm.put((unpack.getCrsDepTime() / FlightContants.MINS60), unpack);
			tm.put(ymd, mm);
		}
	}

	/**
	 * Function to form connections and missed connections
	 *
	 * @param tm
	 *            tree map holding all departing flights
	 * @param arrInfo
	 *            info of the arriving flight
	 * @param start
	 *            the hour from which a departing flight qualifies as a
	 *            connecting flight
	 * @param ymdCur
	 *            the current value of year, month and day for the arriving
	 *            flight
	 * @param rollover
	 *            if the method should look into the next day
	 * @return the number of connections and missed connection made by the
	 *         flight in `arrInfo`
	 */
	private int[] connection(TreeMap<YMD, ListMultimap<Integer, UnpackFlightInfo>> tm, UnpackFlightInfo arrInfo,
			int start, YMD ymdCur, boolean rollover) {
		int connection = 0;
		int missed = 0;
		// check if the next corresponding date's info is found
		if (tm.containsKey(ymdCur)) {
			// get all the flight info for start bucket
			for (UnpackFlightInfo deptInfo : tm.get(ymdCur).get(start)) {
				// check if flights are connected
				if (connected(arrInfo, deptInfo, rollover)) {
					connection++;
					// check for missed connections
					if (missed(arrInfo, deptInfo, rollover))
						missed++;
				}
			}
		}
		return new int[] { connection, missed };
	}

	/**
	 * Get next day YMD object
	 *
	 * @param cal
	 *            instance of Calendar
	 * @param ymd
	 *            wrapper around year, month, year
	 * @return the next day
	 */
	private YMD getNextDayYMD(Calendar cal, YMD ymd) {
		// checking next day slot
		cal.set(ymd.getYear(), ymd.getMonth(), ymd.getDay());
		cal.add(Calendar.DATE, 1);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		YMD nextDay = new YMD(year, month, day);
		return nextDay;
	}

	/**
	 * Unpacks the FlightInfo Writable object and removes fields such as index
	 *
	 * @param f
	 *            FlighInfo
	 * @return UnpackedFlightInfo
	 */
	private UnpackFlightInfo unpackFlightObj(FlightInfo f) {
		UnpackFlightInfo up = new UnpackFlightInfo();
		up.setCrsDepTime(f.getCrsDepTime().get());
		up.setCrsArrTime(f.getCrsArrTime().get());
		up.setDepTime(f.getDepTime().get());
		up.setArrTime(f.getArrTime().get());
		up.setCancelled(f.getCancelled().get());
		up.setDestAirportId(f.getDestAirportId().get());
		up.setOrgAirportId(f.getOrgAirportId().get());
		up.setYear(f.getYear().get());
		up.setMonth(f.getMonth().get());
		up.setDay(f.getDay().get());
		return up;
	}

	/**
	 * (1) A connection is missed when the actual arrival of F < 30 minutes
	 * before the actual departure of G. (2) Cancelled flights (our reasoning
	 * was: a cancelled arrival is a missed connection, while a cancelled
	 * departure isn't)
	 *
	 * @param fArr
	 *            the info of the arriving flight
	 * @param fDept
	 *            the info of the departing flight
	 * @return boolean which suggests if the connection was missed
	 */
	private boolean missed(UnpackFlightInfo fArr, UnpackFlightInfo fDept, boolean nextDay) {
		int time = 0;
		if (nextDay)
			time = 1440;
		return (fDept.getDepTime() < fArr.getArrTime())
				|| (((fDept.getDepTime() + time) - fArr.getArrTime()) < FlightContants.MIN_LAYOFF)
				|| (fArr.getCancelled() == 1);
	}

	/**
	 * Checks if the flight is connected EX. (1)23:00 arrival -> 23:30 to next
	 * day 5:30 (2) 13:30 arrival -> 14:00 to same day 20:00
	 * 
	 * @param arr
	 *            arriving flight info
	 * @param dept
	 *            departing flight info
	 * @param nextDay
	 *            -> add 1440(24 hrs in mins times)
	 * @return boolean which suggests if the flight is connected or not
	 */
	private boolean connected(UnpackFlightInfo arr, UnpackFlightInfo dept, boolean nextDay) {
		int time = nextDay ? 1440 : 0;
		return (arr.getDestAirportId() == dept.getOrgAirportId()) && (scheduledConnected(arr, dept, time));
	}

	/**
	 * Checks for connection window of 30 minutes to 6 Hrs
	 *
	 * @param arr
	 *            arriving flight info
	 * @param dept
	 *            departing flight info
	 * @param time
	 * @return boolean which suggests if the departure time is in the safe range
	 *         (30 mins -> 6 hours)
	 */
	private boolean scheduledConnected(UnpackFlightInfo arr, UnpackFlightInfo dept, int time) {
		return ((dept.getCrsDepTime() + time) <= (arr.getCrsArrTime() + FlightContants.MINS_6HRS))
				&& (dept.getCrsDepTime() + time) >= (arr.getCrsArrTime() + FlightContants.MIN_LAYOFF);
	}

}