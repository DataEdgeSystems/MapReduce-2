package neu.mr.cs6240.a6_prediction;

import java.util.ArrayList;
import java.util.List;

import neu.mr.cs6240.common.Consts;
import weka.core.Attribute;

/**
 * Used to generate the factors of meta data for the model
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

public class Factors {

	/**
	 *
	 * @param begin
	 *            the start of the sequence
	 * @param end
	 *            the end of the sequence
	 * @return the sequence of String integers from begin to end
	 */
	public static List<String> makeSeq(int begin, int end) {
		List<String> ret = new ArrayList<String>(end - begin + 1);
		for (Integer i = begin; i <= end; i++) {
			ret.add(i.toString());
		}
		return ret;
	}

	/**
	 *
	 * @return a list of years from 1990 to 2020
	 */
	public static List<String> getYears() {
		return makeSeq(1990, 2020);
	}

	/**
	 *
	 * @return list of months from 1 to 12
	 */
	public static List<String> getMonths() {
		return makeSeq(1, 12);
	}

	/**
	 *
	 * @return list of days from 1 to 31
	 */
	public static List<String> getDays() {
		return makeSeq(1, 31);
	}

	/**
	 *
	 * @return list of week days from 1 to 7
	 */
	public static List<String> getWeek() {
		return makeSeq(1, 7);
	}

	/**
	 *
	 * @return list of hours from 0 to 24
	 */
	public static List<String> getHours() {
		return makeSeq(0, 24);
	}

	/**
	 *
	 * @return a list of 0 (not cancelled) and 1 (cancelled)
	 */
	public static List<String> cancelled() {
		return makeSeq(0, 1);
	}

	/**
	 *
	 * @return a list of carrier values from the Enum
	 *         neu.mr.cs6240.a6_prediction.Carriers
	 */
	public static List<String> getcarrier() {
		List<String> carrier = new ArrayList<>();
		for (Carriers i : Carriers.values()) {
			carrier.add(i.toString());
		}
		return carrier;
	}

	/**
	 *
	 * @return a list of airport values from
	 *         neu.mr.cs6240.a6_prediction.Airports
	 */
	public static List<String> getAirports() {
		List<String> airports = new ArrayList<>();
		for (Airports i : Airports.values()) {
			airports.add(i.toString());
		}
		return airports;
	}

	/**
	 *
	 * @return a list of arrival delay
	 */
	public static List<String> getArrDelay() {
		List<String> arrDelay = new ArrayList<>();
		arrDelay.add("false");
		arrDelay.add("true");
		return arrDelay;
	}

	/**
	 *
	 * @return buckets into which the distances may fall into, starting from 0
	 *         and incrementing by 250 until 10000.
	 */
	public static List<String> getDist() {
		List<String> distance = new ArrayList<>();
		for (Integer i = 0; i <= Consts.DIST_THRESHOLD; i = i + Consts.DIST_BUCKET_SIZE) {
			distance.add(i.toString());
		}
		return distance;
	}

	/**
	 *
	 * @return an ArryList of all the headers (meta-data) for the dataset
	 */
	public static ArrayList<Attribute> setAttrs() {
		ArrayList<Attribute> attr = new ArrayList<>();
		List<String> airports = Factors.getAirports();

		attr.add(new Attribute("DAY_OF_MONTH", Attribute.NUMERIC));
		attr.add(new Attribute("DAY_OF_WEEK", Attribute.NUMERIC));
		attr.add(new Attribute("UNIQUE_CARRIER", Factors.getcarrier()));
		attr.add(new Attribute("ORIGIN", airports));
		attr.add(new Attribute("DEST", airports));
		attr.add(new Attribute("CRS_DEP_TIME", Attribute.NUMERIC));
		attr.add(new Attribute("CRS_ARR_TIME", Attribute.NUMERIC));
		attr.add(new Attribute("CRS_ELAPSED_TIME", Attribute.NUMERIC));
		attr.add(new Attribute("ARR_DELAY", Factors.getArrDelay()));
		attr.add(new Attribute("DISTANCE", Attribute.NUMERIC));
		return attr;
	}

}
