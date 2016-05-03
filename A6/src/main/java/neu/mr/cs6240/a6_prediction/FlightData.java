package neu.mr.cs6240.a6_prediction;

/**
 * Class that holds all the features that are needed to build the prediction model
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import neu.mr.cs6240.common.Index;
import neu.mr.cs6240.common.Utils;

public class FlightData implements Writable {

	private IntWritable year;
	private ShortWritable month;
	private ShortWritable dayOfMonth;
	private ShortWritable dayOfWeek;
	private Text uniqueCarrier;
	private Text origin;
	private Text dest;
	private IntWritable crsDepTime;
	private IntWritable crsElpTime;
	private IntWritable crsArrTime;
	private IntWritable dist;
	private BooleanWritable arrDelay;

	/**
	 * default constructor
	 */
	public FlightData() {
		this(new IntWritable(), new ShortWritable(), new ShortWritable(), new ShortWritable(), new Text(), new Text(),
				new Text(), new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(),
				new BooleanWritable());
	}

	/**
	 * Parameterised constructor
	 */
	public FlightData(IntWritable year, ShortWritable month, ShortWritable dayOfMonth, ShortWritable dayOfWeek,
			Text uniqueCarrier, Text origin, Text dest, IntWritable crsDepTime, IntWritable crsElpTime,
			IntWritable crsArrTime, IntWritable dist, BooleanWritable arrDelay) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.uniqueCarrier = uniqueCarrier;
		this.origin = origin;
		this.dest = dest;
		this.crsDepTime = crsDepTime;
		this.crsElpTime = crsElpTime;
		this.crsArrTime = crsArrTime;
		this.dist = dist;
		this.arrDelay = arrDelay;
	}

	/**
	 * getters and setters
	 */

	public IntWritable getYear() {
		return year;
	}

	public void setYear(IntWritable year) {
		this.year = year;
	}

	public ShortWritable getMonth() {
		return month;
	}

	public void setMonth(ShortWritable month) {
		this.month = month;
	}

	public ShortWritable getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(ShortWritable dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public ShortWritable getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(ShortWritable dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Text getUniqueCarrier() {
		return uniqueCarrier;
	}

	public void setUniqueCarrier(Text uniqueCarrier) {
		this.uniqueCarrier = uniqueCarrier;
	}

	public Text getOrigin() {
		return origin;
	}

	public void setOrigin(Text origin) {
		this.origin = origin;
	}

	public Text getDest() {
		return dest;
	}

	public void setDest(Text dest) {
		this.dest = dest;
	}

	public IntWritable getCrsDepTime() {
		return crsDepTime;
	}

	public void setCrsDepTime(IntWritable crsDepTime) {
		this.crsDepTime = crsDepTime;
	}

	public IntWritable getCrsElpTime() {
		return crsElpTime;
	}

	public void setCrsElpTime(IntWritable crsElpTime) {
		this.crsElpTime = crsElpTime;
	}

	public IntWritable getCrsArrTime() {
		return crsArrTime;
	}

	public void setCrsArrTime(IntWritable crsArrTime) {
		this.crsArrTime = crsArrTime;
	}

	public IntWritable getDist() {
		return dist;
	}

	public void setDist(IntWritable dist) {
		this.dist = dist;
	}

	public BooleanWritable getArrDelay() {
		return arrDelay;
	}

	public void setArrDelay(BooleanWritable arrDelay) {
		this.arrDelay = arrDelay;
	}

	public static FlightData getFields(String[] line) {
		FlightData fd = new FlightData();
		fd.setYear(Utils.toIntW(line[Index.YEAR.val()]));
		fd.setMonth(Utils.toShortW(line[Index.MONTH.val()]));
		fd.setDayOfMonth(Utils.toShortW(line[Index.DAY_OF_MONTH.val()]));
		fd.setDayOfWeek(Utils.toShortW(line[Index.DAY_OF_WEEK.val()]));
		fd.setUniqueCarrier(Utils.toTextW(Utils.getCarrier(line[Index.UNIQUE_CARRIER.val()])));
		fd.setOrigin(Utils.toTextW(Utils.getAirports(line[Index.ORIGIN.val()])));
		fd.setDest(Utils.toTextW(Utils.getAirports(line[Index.DEST.val()])));
		fd.setCrsDepTime(Utils.toIntW(Utils.toHr(line[Index.CRS_DEP_TIME.val()])));
		fd.setCrsArrTime(Utils.toIntW(Utils.toHr(line[Index.CRS_ARR_TIME.val()])));
		fd.setCrsElpTime(Utils.toIntW(line[Index.CRS_ELAPSED_TIME.val()]));
		fd.setArrDelay(Utils.toBoolW(Utils.isDelay(line[Index.ARR_DELAY.val()])));
		fd.setDist(Utils.toIntW(line[Index.DISTANCE.val()]));

		return fd;
	}

	/**
	 * Serialise the output
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		year.write(out);
		month.write(out);
		dayOfMonth.write(out);
		dayOfWeek.write(out);
		uniqueCarrier.write(out);
		origin.write(out);
		dest.write(out);
		crsDepTime.write(out);
		crsElpTime.write(out);
		crsArrTime.write(out);
		dist.write(out);
		arrDelay.write(out);
	}

	/**
	 * deserialise the output
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		year.readFields(in);
		month.readFields(in);
		dayOfMonth.readFields(in);
		dayOfWeek.readFields(in);
		uniqueCarrier.readFields(in);
		origin.readFields(in);
		dest.readFields(in);
		crsDepTime.readFields(in);
		crsElpTime.readFields(in);
		crsArrTime.readFields(in);
		dist.readFields(in);
		arrDelay.readFields(in);
	}

	@Override
	public String toString() {
		return "FlightData [year=" + year + ", month=" + month + ", dayOfMonth=" + dayOfMonth + ", dayOfWeek="
				+ dayOfWeek + ", uniqueCarrier=" + uniqueCarrier + ", origin=" + origin + ", dest=" + dest
				+ ", crsDepTime=" + crsDepTime + ", crsElpTime=" + crsElpTime + ", crsArrTime=" + crsArrTime + ", dist="
				+ dist + ", arrDelay=" + arrDelay + "]";
	}
}
