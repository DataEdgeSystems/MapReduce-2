package neu.mr.cs6240.a4_regression;

/**
 * Driver of MapReduce jobs 
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.CompareUtils;
import neu.mr.cs6240.common.CompareUtilsEnum;
import neu.mr.cs6240.common.FileUtility;
import neu.mr.cs6240.common.FlightContants;

public class RegressionDriver extends Configured implements Tool {

	public static void validateArgsAndRun(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Usage: RegressionDriver <input path> <output path> <time>");
			System.exit(-1);
		}
		ToolRunner.run(new RegressionDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
		conf.set(FlightContants.SCHEDULE_TIME_MINS, args[2]);
		conf.set(FlightContants.INTERMEDIATE_OUTPUT_PATH_KEY, FlightContants.INTERMEDIATE_OUTPUT_PATH_VAL);

		/**
		 * JOB 1
		 */
		Job job = Job.getInstance(conf);

		job.setJarByClass(RegressionDriver.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(FlightWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(FlightContants.INTERMEDIATE_OUTPUT_PATH_VAL));

		job.setMapperClass(RegressionMapper1.class);
		job.setReducerClass(RegressionReducer1.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.submit();

		/**
		 * If job 1 aborts we do not go any further since we need the cheapest
		 * flight for the given N in Job2
		 */
		if (!job.waitForCompletion(true)) {
			System.err.println("Aborted due to failure of job1");
			System.exit(-1);
		}

		/**
		 * find the cheapest airline from the output of job1 since reducers of
		 * job1 emit cheapest airline for each year , we need to figure out the
		 * airline that has the cheapest over the years
		 */
		findCheapestAirline(conf);

		/**
		 * JOB 2
		 */

		Job job2 = Job.getInstance(conf);
		job2.setJarByClass(RegressionDriver.class);

		job2.setMapperClass(RegressionMapper2.class);
		job2.setReducerClass(RegressionReducer2.class);

		job2.setOutputKeyClass(IntWritable.class);
		job2.setOutputValueClass(CheapFlightData.class);

		FileInputFormat.addInputPath(job2, new Path(args[0]));
		FileOutputFormat.setOutputPath(job2, new Path(args[1]));

		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);

		job2.submit();

		return job2.waitForCompletion(true) ? 0 : 1;

	}

	/**
	 * This method is used to find out the cheapest airline over the years from
	 * a list of (airline, year, price)
	 *
	 * @param conf
	 *            The Configuration object
	 *            {@org.apache.hadoop.conf.Configuration}
	 */
	private static void findCheapestAirline(Configuration conf) {
		try {
			List<String> cheapestAirlinesEachYear = FileUtility
					.readLines(new Path(conf.get(FlightContants.INTERMEDIATE_OUTPUT_PATH_KEY)), conf);
			HashMap<String, Double> hm = new HashMap<>();
			CSVParser csvReader = new CSVParser(',', '"');
			for (String airline : cheapestAirlinesEachYear) {
				String[] eachLine = csvReader.parseLine(airline.toString());
				String carrierName = eachLine[0];

				/**
				 * create a HashMap{CarrierName, x} where "x" is an Number that
				 * tells us for how many years the particular carrier has been
				 * the cheapest. The one with the higher count is the cheapest
				 * carrier for the given value of "N"
				 */
				if (hm.containsKey(carrierName)) {
					hm.put(carrierName, hm.get(carrierName) + 1.0);
				} else {
					hm.put(carrierName, 1.0);
				}
			}

			/**
			 * sort the HashMap{CarrierName, count} based on the value
			 */
			Map<String, Double> sortedHm = CompareUtils.sortHashMapVals(hm, CompareUtilsEnum.DECREASING);

			/**
			 * get the highest value
			 */
			Entry<String, Double> MaxValueEntry = sortedHm.entrySet().iterator().next();
			conf.set(FlightContants.CHEAPEST_AIRLINE, MaxValueEntry.getKey());

		} catch (Exception e) {
			System.err.println("exception occoured while finding out the cheapest airline over the years");
		}
	}

}
