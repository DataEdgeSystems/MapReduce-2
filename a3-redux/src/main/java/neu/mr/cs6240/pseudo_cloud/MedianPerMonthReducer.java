package neu.mr.cs6240.pseudo_cloud;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 01/29/2016
 * @info Assignment 3
 * Reducer class -
 * Gets called for each Airline and data is arranged by Month using partitioner and
 * group sorting. Values are checked if flight is active for 2015 and average for
 * each month is calculated and flight frequency is calculated.
 * Output : AirlineName, (month, avg price)..., frequency
 * */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import neu.mr.cs6240.common.MeanMedianUtility;

public class MedianPerMonthReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	/**
	 * writes to context if the carrier(key) is active in 2015
	 * 
	 * @author Smitha Bangalore Naresh
	 * @author Ajay Subramanya
	 * 
	 * @param key
	 *            the carrier
	 * @param value
	 *            month, avg_price, year
	 * @param context
	 *            the output written is in the format "m C v" . 'm' : month, 'C'
	 *            : carrier , 'v' : median for the month
	 * 
	 */
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Map<Short, ArrayList<Integer>> prcMonth = new TreeMap<>();
		Boolean isActive = false;
		for (Text val : values) {
			String[] params = val.toString().split(",");
			if (params[2].equals("2015")) isActive = true;
			addToMap(prcMonth, params);
		}
		if (!isActive) return;
		for (Short mon : prcMonth.keySet()) {
			double avgP = MeanMedianUtility.calculateMedian(prcMonth.get(mon));
			context.write(new Text(mon + " "), new Text(key.toString() + " " + avgP));
		}
	}

	/**
	 * adds prices/month to HashMap
	 * 
	 * @author Smitha Bangalore Naresh
	 * @author Ajay Subramanya
	 * 
	 * @param prcMonth
	 *            the treeMap of month and the prices for that month
	 * @param params
	 *            month, avg_price, year
	 */
	private void addToMap(Map<Short, ArrayList<Integer>> prcMonth, String[] params) {
		int price = Integer.parseInt(params[1]);
		short month = Short.parseShort(params[0]);
		if (prcMonth.containsKey(month))
			prcMonth.get(month).add(price);
		else {
			ArrayList<Integer> prices = new ArrayList<>();
			prices.add(price);
			prcMonth.put(month, prices);
		}
	}
}
