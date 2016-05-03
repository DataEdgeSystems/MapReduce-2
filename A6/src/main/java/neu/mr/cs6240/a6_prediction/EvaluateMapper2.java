package neu.mr.cs6240.a6_prediction;

/**
 * EMITS : {Quarter, TestData}
 * 
 * Quarter : the quarter the flight flew in
 * TestData : the features from the test dataset we are concerned about
 * 
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
import neu.mr.cs6240.common.Consts;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.Index;
import neu.mr.cs6240.common.Utils;

public class EvaluateMapper2 extends Mapper<LongWritable, Text, ShortWritable, TestData> {

	CSVParser csvReader = new CSVParser(',', '"');

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, ShortWritable, TestData>.Context context)
					throws IOException, InterruptedException {
		String[] line = csvReader.parseLine(value.toString());

		if (DataValidation.isTestSane(line)) {
			context.write(Utils.toShortW(line[Index.MONTH.val() + Consts.INDEX_SHIFT]),
					TestData.getNumericTestData(line));
		}
	}
}
