package neu.mr.cs6240.distribution.mapper;

import static neu.mr.cs6240.distribution.utils.Constants.AIRLINE;
import static neu.mr.cs6240.distribution.utils.Constants.AVG_PRICE;
import static neu.mr.cs6240.distribution.utils.Constants.MONTH;
import static neu.mr.cs6240.distribution.utils.Constants.YEAR;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.distribution.utils.Validation;

/**
 * Mapper class - Gets called for each line in the file read. CSVParser is used
 * to split line. Data is then check for sanity. If data is sane then
 * AirlineName and Month is written as key and then Avg Price and Year is
 * written as value. Output : (Key=(AirlineName, Month),
 * Values((1,AvgPrice),(2,Year))
 * 
 * @author ajay subramanya
 * @author smitha bangalore naresh
 * @date 01/29/2016
 */
public class CheapestAirlineMapper extends Mapper<LongWritable, Text, Text, Text> {

	private CSVParser csvReader = new CSVParser(',', '"');

	@Override
	public void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] eachLine = csvReader.parseLine(line.toString());
		if (!Validation.isSane(eachLine)) return;
		Double avgInCents = Double.parseDouble(eachLine[AVG_PRICE]) * 100;
		String avgTicketPrice = avgInCents.toString();
		String year = eachLine[YEAR];
		context.write(new Text(eachLine[AIRLINE] + "," + eachLine[MONTH]), new Text(avgTicketPrice + "," + year));
	}
}
