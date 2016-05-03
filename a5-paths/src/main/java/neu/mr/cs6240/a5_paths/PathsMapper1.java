package neu.mr.cs6240.a5_paths;

/**
 * Mapper 1:
 * The mapper emits two values for a row from the CSV. Once for the origin airport
 * and again for the destination airport. We package all the values into a class
 * which implements Writable and send it over to the reducer
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
import neu.mr.cs6240.common.CsvConstants;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.FlightContants;

public class PathsMapper1 extends Mapper<LongWritable, Text, Text, FlightInfo> {
	CSVParser csvReader = new CSVParser(',', '"');

	/**
	 * @param key
	 *            CSV offset
	 * @param value
	 *            the CSV row
	 * @param context
	 *            Context
	 * @return key as "airline+(origin|destination)ID" & value as FlightInfo
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlightInfo>.Context context)
			throws IOException, InterruptedException {
		String[] line = csvReader.parseLine(value.toString());
		String airline = line[CsvConstants.UNIQUE_CARRIER.val()];
		if (DataValidation.isSane(line)) {
			// one for origin
			context.write(new Text(airline + " " + line[CsvConstants.ORIGIN_AIRPORT_ID.val()]),
					getFlightInfo(line, FlightContants.ORIGIN));
			// one for destination
			context.write(new Text(airline + " " + line[CsvConstants.DEST_AIRPORT_ID.val()]),
					getFlightInfo(line, FlightContants.DEST));
		}
	}

	/**
	 * Converts time given in string format to minutes
	 *
	 * @param time
	 *            in String (Ex.: 1823, 700)
	 * @return time in IntWritale Ex.: 1103, 420
	 */
	public IntWritable convertMins(String time) {
		int len = time.length();
		if (len == 1 || len == 2)
			return toIntW(time);

		int hours = 0;
		int mins = 0;
		hours = Integer.parseInt(time.substring(0, len - 2));
		mins = Integer.parseInt(time.substring(len - 2));

		return new IntWritable(hours * FlightContants.MINS60 + mins);
	}

	/**
	 *
	 * @param line
	 *            from CSV reader
	 * @param index
	 *            either 0 or 1 - we use this for deciding the airport
	 * @return an object of FlightInfo
	 */
	private FlightInfo getFlightInfo(String[] line, int index) {
		FlightInfo fi = new FlightInfo();
		fi.setYear(toIntW(line[CsvConstants.YEAR.val()]));
		fi.setMonth(toIntW(line[CsvConstants.MONTH.val()]));
		fi.setDay(toIntW(line[CsvConstants.DAY_OF_MONTH.val()]));
		fi.setCrsArrTime(convertMins(line[CsvConstants.CRS_ARR_TIME.val()]));
		fi.setCrsDepTime(convertMins(line[CsvConstants.CRS_DEP_TIME.val()]));
		fi.setArrTime(convertMins(line[CsvConstants.ARR_TIME.val()]));
		fi.setDepTime(convertMins(line[CsvConstants.DEP_TIME.val()]));
		fi.setCancelled(toIntW(line[CsvConstants.CANCELLED.val()]));
		fi.setOrgAirportId(toIntW(line[CsvConstants.ORIGIN_AIRPORT_ID.val()]));
		fi.setDestAirportId(toIntW(line[CsvConstants.DEST_AIRPORT_ID.val()]));
		fi.setIndex(new IntWritable(index));
		return fi;
	}

	/**
	 *
	 * @param s
	 *            string that we are going to convert
	 * @return an IntWritable of the String
	 */
	private IntWritable toIntW(String s) {
		return new IntWritable(Integer.parseInt(s));
	}

}
