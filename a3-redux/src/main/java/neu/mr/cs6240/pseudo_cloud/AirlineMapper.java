package neu.mr.cs6240.pseudo_cloud;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 01/29/2016
 * @info Assignment 3
 * Mapper class -
 * Gets called for each line in the file read. CSVParser is used to split line.
 * Data is then check for sanity. If data is sane then AirlineName and Month is written
 * as Composite Key and then Avg Price and Year is written as MapWritable.
 * Output : (Key=(AirlineName, Month), Values((1,AvgPrice),(2,Year))
 * */
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.DataValidation;

public class AirlineMapper extends Mapper<LongWritable, Text, Text, Text> {
	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	public void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] eachLine = csvReader.parseLine(line.toString());
		if (DataValidation.isSane(eachLine)) {
			int avgInCents = (int) Double.parseDouble((eachLine[109])) * 100;
			context.write(new Text(eachLine[8]), new Text(eachLine[2] + "," + avgInCents + "," + eachLine[0]));
		}
	}
}
