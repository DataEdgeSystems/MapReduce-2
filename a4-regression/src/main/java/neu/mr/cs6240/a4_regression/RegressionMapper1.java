package neu.mr.cs6240.a4_regression;

/**
 * Mapper1 Emits : {Year, Object(CarrierName, AvgPrice,CRSElapsedTime)}
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.DataValidation;

public class RegressionMapper1 extends Mapper<LongWritable, Text, IntWritable, FlightWritable> {

	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	public void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] eachLine = csvReader.parseLine(line.toString());

		if (DataValidation.isSane(eachLine)) {
			double avgInCents = Double.parseDouble(eachLine[109]) * 100;
			FlightWritable fw = new FlightWritable(new Text(eachLine[8]), new IntWritable((int) avgInCents),
					new IntWritable(Integer.parseInt(eachLine[50])));

			context.write(new IntWritable(Integer.parseInt(eachLine[0])), fw);
		}

	}

}
