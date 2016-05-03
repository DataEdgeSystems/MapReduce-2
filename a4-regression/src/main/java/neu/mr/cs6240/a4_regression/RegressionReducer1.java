package neu.mr.cs6240.a4_regression;

/**
 * reducer1 takes in year -> [Object(CarrierName, AvgPrice,CRSElapsedTime)]
 * 
 * (1) Finds the regression value for the given value of time (N)
 * (2) Finds the least expensive airline (for the given air time) 
 *     for a year and writes it to file
 * 
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import neu.mr.cs6240.common.CompareUtils;
import neu.mr.cs6240.common.CompareUtilsEnum;
import neu.mr.cs6240.common.FlightContants;

public class RegressionReducer1 extends Reducer<IntWritable, FlightWritable, Text, DoubleWritable> {

	@Override
	protected void reduce(IntWritable year, Iterable<FlightWritable> flightDataList, Context context)
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		int scheduledTime = Integer.parseInt(conf.get(FlightContants.SCHEDULE_TIME_MINS));

		Map<String, ArrayList<DataPoint>> hm = new HashMap<>();
		Map<String, Double> results = new HashMap<>();

		/**
		 * iterate over the FlightWritable object and create a hashMap {
		 * CarrierName , Object(CRSElspsedTime, AvgPrice) }
		 */
		for (FlightWritable fw : flightDataList) {
			String airlineName = fw.getCarrierName().toString();
			if (hm.containsKey(airlineName)) {
				hm.get(airlineName).add(new DataPoint(fw.getAvgPriceInCents().get(), fw.getScheduledTime().get()));
			} else {
				ArrayList<DataPoint> dp = new ArrayList<>();
				dp.add(new DataPoint(fw.getAvgPriceInCents().get(), fw.getScheduledTime().get()));
				hm.put(fw.getCarrierName().toString(), dp);
			}
		}

		Iterator<Entry<String, ArrayList<DataPoint>>> iterator = hm.entrySet().iterator();

		/**
		 * iterating over the HashMap { CarrierName , Object(CRSElspsedTime,
		 * AvgPrice) } to get the average price for a carrier at the given
		 * scheduled time. We now create an other HashMap{CarrierName,
		 * AvgPriceForN} that holds the results of the reducer
		 */
		while (iterator.hasNext()) {
			Map.Entry<String, ArrayList<DataPoint>> entry = (Map.Entry<String, ArrayList<DataPoint>>) iterator.next();
			results.put(entry.getKey(), LinearRegression.getRegressionValue(entry.getValue(), scheduledTime));
		}

		/**
		 * sort the results hash map based on the the values to get the cheapest
		 * carrier of the year for the given N
		 */
		Map<String, Double> sortedResults = CompareUtils.sortHashMapVals(results, CompareUtilsEnum.INCREASING);

		/**
		 * Entry is used here to get the first item(cheapest airline for the
		 * year) from the results HashMap
		 */
		Entry<String, Double> MinPriceEntry = sortedResults.entrySet().iterator().next();
		context.write(new Text(MinPriceEntry.getKey().concat("," + year.toString() + ",")),
				new DoubleWritable(MinPriceEntry.getValue()));

	}

}
