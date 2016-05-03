package neu.mr.cs6240.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @info Class for sanity checks
 *
 * @author Ajay Subramanya
 * @author Smitha Bangalore Naresh
 * 
 */
public class DataValidation {
	static SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD");

	/**
	 * Validating each row from the CSV
	 *
	 * @param line
	 *            String array containing individual columns of a row
	 * @return boolean the result of the sanity test
	 */
	public static boolean isSane(String[] nextLine) {
		if (nextLine.length != 110 || nextLine.length < 0) return false;

		try {
			int CRSElapsedTime, OriginAirportID, OriginAirportSeqID, OriginCityMarketID, OriginStateFips, OriginWac,
					DestAirportID, DestAirportSeqID, DestCityMarketID, DestStateFips, DestWac, Cancelled, dayOfWeek,
					dist;

			String CRSArrTime = nextLine[40];
			String CRSDepTime = nextLine[29];

			String Origin = nextLine[14];
			String OriginCityName = nextLine[15];
			String OriginStateName = nextLine[18];

			String Destination = nextLine[23];
			String DestCityName = nextLine[24];
			String DestStateName = nextLine[27];

			String ArrTime = nextLine[41];
			String DepTime = nextLine[30];
			String ActualElapsedTime = nextLine[51];

			String ArrDelay = nextLine[42];
			String ArrDel15 = nextLine[44];
			String ArrDelMin = nextLine[43];
			String UniqueCarrier = nextLine[8];
			String AvgTicketPrice = nextLine[109];

			String Month = nextLine[2];
			String Year = nextLine[0];

			try {
				CRSElapsedTime = Integer.parseInt(nextLine[50]);
				OriginAirportID = Integer.parseInt(nextLine[11]);
				OriginAirportSeqID = Integer.parseInt(nextLine[12]);
				OriginCityMarketID = Integer.parseInt(nextLine[13]);
				OriginStateFips = Integer.parseInt(nextLine[17]);
				OriginWac = Integer.parseInt(nextLine[19]);
				DestAirportID = Integer.parseInt(nextLine[20]);
				DestAirportSeqID = Integer.parseInt(nextLine[21]);
				DestCityMarketID = Integer.parseInt(nextLine[22]);
				DestStateFips = Integer.parseInt(nextLine[26]);
				DestWac = Integer.parseInt(nextLine[28]);
				Cancelled = Integer.parseInt(nextLine[47]);
				dayOfWeek = Integer.parseInt(nextLine[4]);
				dist = Integer.parseInt(nextLine[54]);
			} catch (NumberFormatException e) {
				throw new SanityFailedException("");
			}

			if (OriginAirportID <= 0 || DestAirportID <= 0 || OriginAirportSeqID <= 0 || DestAirportSeqID <= 0
					|| OriginCityMarketID <= 0 || DestCityMarketID <= 0 || Origin == null || Destination == null
					|| OriginStateName == null || DestStateName == null || OriginCityName == null
					|| DestCityName == null || CRSArrTime == null || CRSArrTime.isEmpty() || CRSDepTime == null
					|| CRSDepTime.isEmpty() || CRSArrTime.equals("NA") || CRSDepTime.equals("NA")) {
				throw new SanityFailedException("");
			}

			int dCRSArrTime = Integer.MIN_VALUE;
			int dCRSDepTime = Integer.MIN_VALUE;

			try {
				dCRSArrTime = Integer.parseInt(CRSArrTime);
				dCRSDepTime = Integer.parseInt(CRSDepTime);
			} catch (NumberFormatException e) {
				throw new SanityFailedException("");
			}

			if (dCRSArrTime == 0 || dCRSDepTime == 0) throw new SanityFailedException("");

			int totalMinutes = calcuateMins(CRSArrTime, CRSDepTime);
			int timeZone = (totalMinutes - CRSElapsedTime);

			if ((timeZone % 60) != 0) throw new SanityFailedException("");

			if (OriginStateFips <= 0 && DestStateFips <= 0) throw new SanityFailedException("");

			if (OriginWac <= 0 && DestWac <= 0) throw new SanityFailedException("");

			if (dayOfWeek < 1 || dayOfWeek > 7) throw new SanityFailedException("");

			if (dist <= 0) throw new SanityFailedException("");

			if (ArrTime == null || ArrTime.isEmpty() || ArrTime.equals("NA") || DepTime.equals("NA")
					|| ArrTime.length() == 0 || DepTime == null || DepTime.isEmpty() || DepTime.length() == 0
					|| ArrTime.equals("NA") || DepTime.equals("NA")) {
				throw new SanityFailedException("");
			}

			if (Cancelled == 0) {
				if (ArrDel15 == null || ArrDel15.isEmpty() || ArrDelay == null || ArrDelay.isEmpty()
						|| ActualElapsedTime == null || ActualElapsedTime.isEmpty()) {
					throw new SanityFailedException("");
				} else {
					int dActualElapsedTime = Integer.parseInt(ActualElapsedTime);

					int totalArrDepMinutes = calcuateMins(ArrTime, DepTime);

					if ((totalArrDepMinutes - dActualElapsedTime) % 60 != 0) throw new SanityFailedException("");

					double dArrDelay = Double.parseDouble(ArrDelay);
					double dArrDelayMinutes = Double.parseDouble(ArrDelMin);
					double dArrDel15 = Double.parseDouble(ArrDel15);

					if (dArrDelay > 0 && dArrDelay != dArrDelayMinutes) throw new SanityFailedException("");

					if (dArrDelay < 0 && dArrDelayMinutes != 0) throw new SanityFailedException("");

					if (dArrDelayMinutes >= 15 && dArrDel15 != 1.0) throw new SanityFailedException("");

					if (UniqueCarrier == null || AvgTicketPrice == null || UniqueCarrier.isEmpty()
							|| AvgTicketPrice.isEmpty() || AvgTicketPrice.equals("999999999") || Month == null
							|| Year == null || Month.isEmpty() || Year.isEmpty())
						throw new SanityFailedException("");

				}
			}
		} catch (SanityFailedException e) {
			return false;
		}
		return true;
	}

	/**
	 * Calculate minutes elapsed for Arrival and Departure
	 *
	 * @param ArrTime
	 * @param DepTime
	 * @return
	 */
	private static int calcuateMins(String ArrTime, String DepTime) {

		int totalMinutes = 0;
		try {
			int hoursArrival = getHoursTime(ArrTime);
			int hoursDeparture = getHoursTime(DepTime);
			int minsArrival = getMinsTime(ArrTime);
			int minsDeparture = getMinsTime(DepTime);
			int diffHours = hoursArrival - hoursDeparture;
			int diffMins = minsArrival - minsDeparture;

			if (diffHours > 0)
				totalMinutes += diffHours * 60;
			else
				totalMinutes += (diffHours + 24) * 60;

			totalMinutes += diffMins;
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("INVALID:" + ArrTime + " " + DepTime);
		}

		return totalMinutes;
	}

	/**
	 * Extracts mins from string of format 1823, 719, 56, 8
	 *
	 * @param time
	 * @return
	 */
	public static int getMinsTime(String time) {
		int len = time.length();
		if (len == 1 || len == 2) return Integer.parseInt(time);

		return Integer.parseInt(time.substring(len - 2));
	}

	/**
	 * Extracts hrs from string of format 1823, 745
	 *
	 * @param time
	 * @return
	 */
	public static int getHoursTime(String time) {
		int len = time.length();
		if (len == 1 || len == 2) return 0;
		return Integer.parseInt(time.substring(0, len - 2));
	}

	/**
	 * validates the arguments coming to the code keeps only arguments passed in
	 * the format <-someName=someValue> ignores any other format
	 *
	 * @param preArgs
	 *            String array of all the arguments passed to the code
	 * @return args String array of only valid arguments
	 */
	public static String[] validateCLIArgs(String[] preArgs) {
		ArrayList<String> tempArgs = new ArrayList<>();
		for (int i = 0; i < preArgs.length; i++) {
			String[] options = preArgs[i].split("=");
			if (preArgs[i].startsWith("-") && preArgs[i].contains("=") && options.length > 1) tempArgs.add(options[1]);

		}
		return tempArgs.toArray(new String[tempArgs.size()]);
	}

	/**
	 * Validating each row from the CSV for test data
	 *
	 * @param line
	 *            String array containing individual columns of a row
	 * @return boolean the result of the sanity test
	 */
	public static boolean isTestSane(String[] nextLine) {

		if (nextLine.length != 112 || nextLine.length < 0) return false;

		try {
			int iCRSElapsedTime, iCRSArrTime, iCRSDepTime, idayOfWeek, dist;

			int index = Consts.INDEX_SHIFT;

			String Origin = nextLine[14 + index];
			String Destination = nextLine[23 + index];
			String UniqueCarrier = nextLine[8 + index];
			String Month = nextLine[2 + index];
			String Year = nextLine[0 + index];
			String Day = nextLine[3 + index];
			String CRSArrTime = nextLine[40 + index];
			String CRSDepTime = nextLine[29 + index];
			String CRSElapsedTime = nextLine[50 + index];
			String DayOfWeek = nextLine[4 + index];
			String Distance = nextLine[54 + index];
			String flightNum = nextLine[10 + index];
			String flightDate = nextLine[5 + index];

			try {
				if (CRSElapsedTime.equals("NA") || DayOfWeek.equals("NA") || Distance.equals("NA")) {
					throw new SanityFailedException("");
				}
				iCRSElapsedTime = Integer.parseInt(CRSElapsedTime);
				idayOfWeek = Integer.parseInt(DayOfWeek);
				dist = Integer.parseInt(Distance);
				iCRSArrTime = Integer.parseInt(CRSArrTime);
				iCRSDepTime = Integer.parseInt(CRSDepTime);
			} catch (NumberFormatException e) {
				throw new SanityFailedException("");
			}

			if (Origin == null || Destination == null || Origin.equals("NA") || Destination.equals("NA")
					|| CRSArrTime == null || CRSArrTime.isEmpty() || CRSDepTime == null || CRSDepTime.isEmpty()
					|| CRSArrTime.equals("NA") || CRSDepTime.equals("NA")) {
				throw new SanityFailedException("");
			}

			if (iCRSArrTime == 0 || iCRSDepTime == 0) throw new SanityFailedException("");

			/* calculate CRSArrTime - CRSDepTime */
			int totalMinutes = calcuateMins(CRSArrTime, CRSDepTime);
			int timeZone = (totalMinutes - iCRSElapsedTime);

			if ((timeZone % 60) != 0) {
				throw new SanityFailedException("");
			}

			if (idayOfWeek < 1 || idayOfWeek > 7) throw new SanityFailedException("");

			if (dist <= 0) throw new SanityFailedException("");

			if (UniqueCarrier == null | Month == null || Year == null || Month.isEmpty() || Year.isEmpty()
					|| Day == null || Day.isEmpty() || flightNum == null || flightDate == null || flightNum.isEmpty()
					|| flightNum.equals("NA") || flightDate.equals("NA"))
				throw new SanityFailedException("");

		} catch (SanityFailedException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if validate file lines have sane data
	 *
	 * @param sCurrentLine
	 * @return
	 */
	public static String[] isValidateSane(String sCurrentLine) {
		String[] s = sCurrentLine.split(",");
		if (s.length == 2) {
			String[] key = s[0].split("_");
			if (key.length == 3 && s[1] != null) {
				// Do sanity
				try {
					@SuppressWarnings("unused")
					int flNum = Integer.parseInt(key[0]);
					@SuppressWarnings("unused")
					int crsDepTime = Integer.parseInt(key[2]);
					@SuppressWarnings("unused")
					Date cur = sd.parse(key[1]);
					@SuppressWarnings("unused")
					boolean pred = Boolean.parseBoolean(s[1]);
				} catch (NumberFormatException | ParseException e) {
					return null;
				}
			}
		}
		return s;
	}
}
