package neu.mr.cs6240.a4_regression;

/**
 * This class is a used to wrap avg_price, month and day before
 * sending it to reducer2 from mapper2, since it is being sent over
 * the network (Between mapper and reducer) it implements Writable
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class CheapFlightData implements Writable {

	private IntWritable avgPriceCents;
	private IntWritable month;
	private IntWritable day;

	/**
	 * default constructor
	 */
	public CheapFlightData() {
		this(new IntWritable(), new IntWritable(), new IntWritable());
	}

	/**
	 * parameterized constructor
	 *
	 * @param week
	 * @param avgPriceCents
	 */
	public CheapFlightData(IntWritable avgPriceCents, IntWritable month, IntWritable day) {
		this.avgPriceCents = avgPriceCents;
		this.month = month;
		this.day = day;
	}

	/**
	 * getters and setters
	 */
	public IntWritable getAvgPriceCents() {
		return avgPriceCents;
	}

	public void setAvgPriceCents(IntWritable avgPriceCents) {
		this.avgPriceCents = avgPriceCents;
	}

	public IntWritable getMonth() {
		return month;
	}

	public void setMonth(IntWritable month) {
		this.month = month;
	}

	public IntWritable getDay() {
		return day;
	}

	public void setDay(IntWritable day) {
		this.day = day;
	}

	@Override
	/**
	 * Serialize the fields of this object to out.
	 */
	public void write(DataOutput out) throws IOException {
		avgPriceCents.write(out);
		month.write(out);
		day.write(out);
	}

	@Override
	/**
	 * De-serialize the fields of this object from in.
	 */
	public void readFields(DataInput in) throws IOException {
		avgPriceCents.readFields(in);
		month.readFields(in);
		day.readFields(in);
	}

}
