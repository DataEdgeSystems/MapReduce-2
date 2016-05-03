package neu.mr.cs6240.a5_paths;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.FlightContants;

/**
 * Mapper 2: The reducer 1 emits airline, year, connections, missed connections
 * for a given airport and airline. Mapper 2 emits {airline year} as Key and
 * Results{connections, missed}. So Reducer 2 can sum up Results{connections,
 * missed} for a year and airline
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */

public class PathsMapper2 extends Mapper<LongWritable, Text, Text, Result> {
	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Result>.Context context)
			throws IOException, InterruptedException {
		String[] line = value.toString().split(",");
		context.write(new Text(line[FlightContants.AIRLINE] + " " + line[FlightContants.YEAR]),
				new Result(toIntW(line[FlightContants.CONNECTIONS]), toIntW(line[FlightContants.MISSED])));
	}

	private IntWritable toIntW(String s) {
		return new IntWritable(Integer.parseInt(s));
	}

}
