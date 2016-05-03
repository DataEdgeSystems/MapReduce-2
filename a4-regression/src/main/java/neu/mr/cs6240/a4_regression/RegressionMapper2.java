package neu.mr.cs6240.a4_regression;

/**
 * mapper2 takes in the CSV again but now we filter the CSV based
 * on the cheapest airline that we figured out in the previous MR
 * job
 *
 * Mapper2 Emits : {Year, Object(AvgPrice, month, day)}
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.FlightContants;

public class RegressionMapper2 extends Mapper<LongWritable, Text, IntWritable, CheapFlightData> {

	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	public void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] eachLine = csvReader.parseLine(line.toString());
		Configuration conf = context.getConfiguration();
		String cheapestAirline = conf.get(FlightContants.CHEAPEST_AIRLINE);
		String airline = eachLine[8];

		if (airline != null && airline.equals(cheapestAirline) && DataValidation.isSane(eachLine)) {
			double avgInCents = Double.parseDouble(eachLine[109]) * FlightContants.CENTS;
			int month = Integer.parseInt(eachLine[2]);
			int day = Integer.parseInt(eachLine[3]);

			context.write(new IntWritable(Integer.parseInt(eachLine[0])), new CheapFlightData(
					new IntWritable((int) avgInCents), new IntWritable(month), new IntWritable(day)));

		}
	}

}
