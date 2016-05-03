package neu.mr.cs6240.a6_prediction;

/**
 * Class that holds all the features that are needed to
 * predict a row from the test set
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

import neu.mr.cs6240.common.Consts;
import neu.mr.cs6240.common.Index;
import neu.mr.cs6240.common.Utils;

public class TestData extends FlightData {

	private IntWritable fNum;
	private Text date;

	public IntWritable getfNum() {
		return fNum;
	}

	public void setfNum(IntWritable fNum) {
		this.fNum = fNum;
	}

	public Text getDate() {
		return date;
	}

	public void setDate(Text date) {
		this.date = date;
	}

	/**
	 * Default constructor
	 */
	public TestData() {
		this(new IntWritable(), new ShortWritable(), new ShortWritable(), new ShortWritable(), new Text(), new Text(),
				new Text(), new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(),
				new BooleanWritable(), new IntWritable(), new Text());
	}

	/**
	 * Parameterised constructor
	 */
	public TestData(IntWritable year, ShortWritable month, ShortWritable dayOfMonth, ShortWritable dayOfWeek,
			Text uniqueCarrier, Text origin, Text dest, IntWritable crsDepTime, IntWritable crsElpTime,
			IntWritable crsArrTime, IntWritable dist, BooleanWritable arrDelay, IntWritable fNum, Text date) {
		super(year, month, dayOfMonth, dayOfWeek, uniqueCarrier, origin, dest, crsDepTime, crsElpTime, crsArrTime, dist,
				arrDelay);
		this.fNum = fNum;
		this.date = date;
	}

	/**
	 *
	 * @param line
	 *            a row from the test set
	 * @return an object of this class that has all the features need to predict
	 *         the given row
	 */
	public static TestData getTestData(String[] line) {
		TestData td = new TestData();

		int offset = Consts.INDEX_SHIFT;
		td.setYear(Utils.toIntW(line[Index.YEAR.val() + offset]));
		td.setMonth(Utils.toShortW(line[Index.MONTH.val() + offset]));
		td.setDayOfMonth(Utils.toShortW(line[Index.DAY_OF_MONTH.val() + offset]));
		td.setDayOfWeek(Utils.toShortW(line[Index.DAY_OF_WEEK.val() + offset]));
		td.setUniqueCarrier(Utils.toTextW(Utils.getCarrier(line[Index.UNIQUE_CARRIER.val() + offset])));
		td.setOrigin(Utils.toTextW(Utils.getAirports(line[Index.ORIGIN.val() + offset])));
		td.setDest(Utils.toTextW(Utils.getAirports(line[Index.DEST.val() + offset])));
		td.setCrsDepTime(Utils.toIntW(line[Index.CRS_DEP_TIME.val() + offset]));
		td.setCrsArrTime(Utils.toIntW(Utils.toHr(line[Index.CRS_ARR_TIME.val() + offset])));
		td.setCrsElpTime(Utils.toIntW(Utils.minToHr(line[Index.CRS_ELAPSED_TIME.val() + offset])));
		td.setDist(Utils.toIntW(Utils.getDist(line[Index.DISTANCE.val() + offset])));
		td.setfNum(Utils.toIntW(line[Index.FLIGHT_NUM.val() + offset]));
		td.setDate(Utils.toTextW(line[Index.DATE.val() + offset]));

		return td;
	}

	/**
	 *
	 * @param line
	 *            a row from the test set
	 * @return an object of this class that has all the features need to predict
	 *         the given row
	 */
	public static TestData getNumericTestData(String[] line) {
		TestData td = new TestData();

		int offset = Consts.INDEX_SHIFT;
		td.setYear(Utils.toIntW(line[Index.YEAR.val() + offset]));
		td.setMonth(Utils.toShortW(line[Index.MONTH.val() + offset]));
		td.setDayOfMonth(Utils.toShortW(line[Index.DAY_OF_MONTH.val() + offset]));
		td.setDayOfWeek(Utils.toShortW(line[Index.DAY_OF_WEEK.val() + offset]));
		td.setUniqueCarrier(Utils.toTextW(Utils.getCarrier(line[Index.UNIQUE_CARRIER.val() + offset])));
		td.setOrigin(Utils.toTextW(Utils.getAirports(line[Index.ORIGIN.val() + offset])));
		td.setDest(Utils.toTextW(Utils.getAirports(line[Index.DEST.val() + offset])));
		td.setCrsDepTime(Utils.toIntW(line[Index.CRS_DEP_TIME.val() + offset]));
		td.setCrsArrTime(Utils.toIntW(Utils.toHr(line[Index.CRS_ARR_TIME.val() + offset])));
		td.setCrsElpTime(Utils.toIntW(line[Index.CRS_ELAPSED_TIME.val() + offset]));
		td.setDist(Utils.toIntW(line[Index.DISTANCE.val() + offset]));
		td.setfNum(Utils.toIntW(line[Index.FLIGHT_NUM.val() + offset]));
		td.setDate(Utils.toTextW(line[Index.DATE.val() + offset]));

		return td;
	}

	/**
	 * serialise the output
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		fNum.write(out);
		date.write(out);
	}

	/**
	 * deserialise the output
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		fNum.readFields(in);
		date.readFields(in);
	}

	@Override
	public String toString() {
		return "TestData [fNum=" + fNum + ", date=" + date + ", getfNum()=" + getfNum() + ", getDate()=" + getDate()
				+ ", getYear()=" + getYear() + ", getMonth()=" + getMonth() + ", getDayOfMonth()=" + getDayOfMonth()
				+ ", getDayOfWeek()=" + getDayOfWeek() + ", getUniqueCarrier()=" + getUniqueCarrier() + ", getOrigin()="
				+ getOrigin() + ", getDest()=" + getDest() + ", getCrsDepTime()=" + getCrsDepTime()
				+ ", getCrsElpTime()=" + getCrsElpTime() + ", getCrsArrTime()=" + getCrsArrTime() + ", getDist()="
				+ getDist() + ", getArrDelay()=" + getArrDelay() + ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}

}
