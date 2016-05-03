package neu.mr.cs6240.a6_prediction;

/**
 * EMITS : {Quarter, FlightData}
 * Quarter : the quarter to which the row belongs
 * FlightData : the features from the row which we use for prediction.

 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.Index;
import neu.mr.cs6240.common.Utils;

public class PredictionMapper1 extends Mapper<LongWritable, Text, ShortWritable, FlightData> {
	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, ShortWritable, FlightData>.Context context)
					throws IOException, InterruptedException {
		String[] line = csvReader.parseLine(value.toString());
		if (DataValidation.isSane(line)) {
			context.write(Utils.toShortW(line[Index.MONTH.val()]), FlightData.getFields(line));
		}
	}
}
