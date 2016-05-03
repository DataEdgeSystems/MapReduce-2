package neu.mr.cs6240.a4_regression;

/**
 * This class is used to wrap carrier name, average ticket
 * price and scheduled time. Implements Writable since we 
 * would be sending this object over the network(between 
 * mapper and reducer) 
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class FlightWritable implements Writable {

	private Text carrierName;
	private IntWritable avgPriceInCents;
	private IntWritable scheduledTime;

	/**
	 * default constructor
	 */
	public FlightWritable() {
		this(new Text(), new IntWritable(), new IntWritable());
	}

	/**
	 * parameterized constructor
	 * 
	 * @param carrierName
	 * @param avgPriceInCents
	 * @param scheduledTime
	 */
	public FlightWritable(Text carrierName, IntWritable avgPriceInCents, IntWritable scheduledTime) {
		this.carrierName = carrierName;
		this.avgPriceInCents = avgPriceInCents;
		this.scheduledTime = scheduledTime;
	}

	/**
	 * getters and setters
	 */

	public Text getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(Text carrierName) {
		this.carrierName = carrierName;
	}

	public IntWritable getAvgPriceInCents() {
		return avgPriceInCents;
	}

	public void setAvgPriceInCents(IntWritable avgPriceInCents) {
		this.avgPriceInCents = avgPriceInCents;
	}

	public IntWritable getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(IntWritable scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	@Override
	/**
	 * De-serialize the fields of this object from in.
	 */
	public void readFields(DataInput in) throws IOException {
		carrierName.readFields(in);
		avgPriceInCents.readFields(in);
		scheduledTime.readFields(in);
	}

	@Override
	/**
	 * Serialize the fields of this object to out.
	 */
	public void write(DataOutput out) throws IOException {
		carrierName.write(out);
		avgPriceInCents.write(out);
		scheduledTime.write(out);
	}

}
