package neu.mr.cs6240.common;

import java.util.ArrayList;
import java.util.Collections;

import neu.mr.cs6240.a4_regression.DataForMedian;

/**
 * @info Class contains methods for calculating mean ,median
 *
 * @author ajay subramanya & smitha bangalore naresh
 */
public class MeanMedianUtility {

	/**
	 * Used to calculate the median of the passed in array list of Integers
	 * Median Price Calculation : (ex1) 1 , 3, 5, 6, 7, 10 count = 6 index 2, 3
	 * (ex2) 1 , 3, 6, 7, 10 count = 5 index 2
	 *
	 * @param avgPLst
	 *            the list whose median we need to find after sorting
	 * @return the median of the passed list
	 */
	public static double calculateMedian(ArrayList<Integer> avgPLst) {
		Collections.sort(avgPLst);
		int count = avgPLst.size();
		double median = 0.0;
		if ((count % 2) == 0) {
			median = ((avgPLst.get((count / 2) - 1) + avgPLst.get(count / 2)) / 2);
		} else {
			median = (avgPLst.get((int) Math.floor(count / 2)));
		}
		return (median / FlightContants.CENTS);
	}

	/**
	 * @info Calculates median for DataForMedian type
	 * @param dfm
	 *            ArrayList of DataForMedian
	 * @return an object of DataForMedian holding median value in Integer(i.e.
	 *         Cents) and date
	 */
	public static DataForMedian calculateMedianForDate(ArrayList<DataForMedian> dfm) {
		Collections.sort(dfm);
		int count = dfm.size();
		int mid = (int) Math.floor(count / 2);
		DataForMedian median = new DataForMedian();
		median.setDate(dfm.get(mid).getDate());
		if ((count % 2) == 0) {
			median.setMedianP(((dfm.get(mid - 1).getMedianP() + dfm.get(mid).getMedianP()) / 2));
		} else {
			median.setMedianP((dfm.get(mid).getMedianP()));
		}
		return median;
	}
}
