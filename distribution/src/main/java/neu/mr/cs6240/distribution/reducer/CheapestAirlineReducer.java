package neu.mr.cs6240.distribution.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer class - Gets called for each Airline and data is arranged by Month
 * using partitioner and group sorting. Values are checked if flight is active
 * for 2015 and average for each month is calculated and flight frequency is
 * calculated. Output : AirlineName, (month, avg price)..., frequency
 * 
 * @author smitha bangalore naresh
 * @author ajay subramanya
 * @date 01/29/2016
 */
public class CheapestAirlineReducer extends Reducer<Text, Text, Text, Text> {
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		boolean active = false;
		List<Double> avgPrices = new ArrayList<>();
		for (Text val : values) {
			String[] params = val.toString().split(",");
			if (params[1].equals("2015")) active = true;
			avgPrices.add(Double.parseDouble(params[0]));
		}
		String[] keys = key.toString().split(",");
		context.write(new Text(keys[0] + " , "), new Text(keys[1] + " , " + avg(avgPrices) / 100 + " , " + active));
	}

	/**
	 * 
	 * @param lst
	 * @return
	 */
	private Double avg(List<Double> lst) {
		Double sum = 0.0;
		for (Double l : lst) {
			sum += l;
		}
		return sum / lst.size();
	}
}
