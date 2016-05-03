package neu.mr.cs6240.a4_regression;

/**
 * reducer2 takes in {Year, Object(AvgPrice, month, day)} and finds the
 * median price of each week for a year for the cheapest airline.
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import neu.mr.cs6240.common.FlightContants;
import neu.mr.cs6240.common.MeanMedianUtility;

public class RegressionReducer2 extends Reducer<IntWritable, CheapFlightData, Text, Text> {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();

	@Override
	protected void reduce(IntWritable year, Iterable<CheapFlightData> cheapFlightData, Context context)
			throws IOException, InterruptedException {
		int month, day;
		int avgP;
		int yr = year.get();
		Configuration conf = context.getConfiguration();
		/**
		 * using TreeMap to have the keys of the map in sorted order
		 */
		Map<Integer, ArrayList<DataForMedian>> hm = new TreeMap<>();

		/**
		 * creating a TreeMap of week -> [DataForMedian]
		 */

		for (CheapFlightData cfd : cheapFlightData) {
			avgP = cfd.getAvgPriceCents().get();
			month = cfd.getMonth().get();
			day = cfd.getDay().get();

			// calculate week - it will be a key
			cal.set(yr, month, day);
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			Date date = cal.getTime();
			String dateStr = formatter.format(date);

			if (hm.containsKey(week)) {
				hm.get(week).add(new DataForMedian(dateStr, avgP));
			} else {
				ArrayList<DataForMedian> dm = new ArrayList<DataForMedian>();
				dm.add(new DataForMedian(dateStr, avgP));
				hm.put(week, dm);
			}
		}
		context.write(new Text(conf.get(FlightContants.CHEAPEST_AIRLINE) + ","),
				new Text(conf.get(FlightContants.SCHEDULE_TIME_MINS)));

		Iterator<Entry<Integer, ArrayList<DataForMedian>>> iterator = hm.entrySet().iterator();

		/**
		 * iterating over the HashMap { WeekNum , ArrayList<DataForMedian>} to
		 * get the median price for a given week
		 */
		while (iterator.hasNext()) {
			Map.Entry<Integer, ArrayList<DataForMedian>> entry = iterator.next();
			DataForMedian res = MeanMedianUtility.calculateMedianForDate(entry.getValue());
			double medianPrice = ((double) res.getMedianP()) / FlightContants.CENTS;
			context.write(new Text(res.getDate() + "," + entry.getKey().toString() + ","),
					new Text(String.valueOf(medianPrice)));
		}
	}
}
