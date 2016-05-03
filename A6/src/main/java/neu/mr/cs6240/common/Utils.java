package neu.mr.cs6240.common;

/**
* Class that holds general utility functions
*
* @author Smitha Bangalore Naresh
* @author Ajay Subramanya
*
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;

import neu.mr.cs6240.a6_prediction.Airports;
import neu.mr.cs6240.a6_prediction.Carriers;
import neu.mr.cs6240.a6_prediction.Factors;
import weka.classifiers.Classifier;

public class Utils {
	/**
	 *
	 * @param s
	 *            string that we are going to convert
	 * @return an IntWritable of the String
	 */
	public static IntWritable toIntW(String s) {
		return new IntWritable(Integer.parseInt(s));
	}

	/**
	 *
	 * @param s
	 *            string that we are going to convert
	 * @return an IntWritable of the String
	 */
	public static IntWritable toIntW(int s) {
		return new IntWritable(s);
	}

	/**
	 *
	 * @param s
	 *            string that we are going to convert
	 * @return an ShortWritable of the String
	 */
	public static ShortWritable toShortW(String s) {
		return new ShortWritable(Short.parseShort(s));
	}

	/**
	 *
	 * @param s
	 *            string that we are going to convert
	 * @return an Text of the String
	 */
	public static Text toTextW(String s) {
		return new Text(s);
	}

	/**
	 *
	 * @param b
	 *            boolean that we are going to convert
	 * @return an BooleanWritable of the Boolean
	 */
	public static BooleanWritable toBoolW(boolean b) {
		return new BooleanWritable(b);
	}

	/**
	 *
	 * @param time
	 *            in HHMM
	 * @return time in HH
	 */
	public static int toHr(String time) {
		return Integer.parseInt(time) / Consts.HOUR_CONVERTER;
	}

	/**
	 *
	 * @param time
	 *            in MM
	 * @return time in HH
	 */
	public static int minToHr(String time) {
		return Integer.parseInt(time) / Consts.MINS_IN_HOURS;
	}

	/**
	 *
	 * @param delay
	 * @return boolean which suggest if there was a delay or not
	 */
	public static boolean isDelay(String delay) {
		return Double.parseDouble(delay) > 0;
	}

	/**
	 *
	 * @param airport
	 * @return the value of airport from "neu.mr.cs6240.a6_prediction.Airports"
	 *         , if not present then OTHER
	 */
	public static String getAirports(String airport) {
		Airports dest = Airports.OTHER;
		try {
			dest = Airports.valueOf(airport);
		} catch (IllegalArgumentException ex) {
			dest = Airports.OTHER;
		}
		return dest.toString();
	}

	/**
	 *
	 * @param carrier
	 * @return the value of the carrier from
	 *         "neu.mr.cs6240.a6_prediction.Carriers" , if not present then
	 *         OTHER
	 */
	public static String getCarrier(String carrier) {
		Carriers dest = Carriers.OTHER;
		try {
			dest = Carriers.valueOf(carrier);
		} catch (IllegalArgumentException ex) {
			dest = Carriers.OTHER;
		}
		return dest.toString();
	}

	/**
	 *
	 * @param dist
	 * @return checks if distance is above threshold we return the threshold
	 *         else we return the value of the bucket to which the distance
	 *         would fall into
	 */
	public static String getDist(String dist) {
		int d = Integer.parseInt(dist);
		return d < Consts.DIST_THRESHOLD ? Factors.getDist().get(d / Consts.DIST_BUCKET_SIZE)
				: Consts.DIST_THRESHOLD.toString();
	}

	/**
	 *
	 * @param name
	 *            the name of the file we want to read
	 * @return the classifier object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Classifier readFile(String name) throws IOException, ClassNotFoundException {
		FileSystem fs = FileSystem.get(new Configuration());
		InputStream in = fs.open(new Path(name));
		ObjectInputStream objReader = new ObjectInputStream(in);
		return (Classifier) objReader.readObject();
	}

	/**
	 *
	 * @param obj
	 *            the classifier object which we need to write to file on HDFS
	 * @param name
	 *            the name of the file in HDFS
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void writeFile(Classifier obj, String name) throws IOException, URISyntaxException {
		FileSystem hdfs = FileSystem.get(new Configuration());
		Path path = new Path(name);
		if (hdfs.exists(path)) hdfs.delete(path, true);
		OutputStream os = hdfs.create(path);
		os.write(Serializer.serialize(obj));
		hdfs.close();
	}

}
