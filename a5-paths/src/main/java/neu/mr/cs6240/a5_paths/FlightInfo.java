package neu.mr.cs6240.a5_paths;

/**
 * the value which is sent over the network from the mapper to reducer. Holds almost all info about the flight
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class FlightInfo implements Writable {
	private IntWritable orgAirportId;
	private IntWritable destAirportId;
	private IntWritable crsDepTime;
	private IntWritable crsArrTime;
	private IntWritable year;
	private IntWritable month;
	private IntWritable day;
	private IntWritable arrTime;
	private IntWritable depTime;
	private IntWritable cancelled;
	private IntWritable index;

	/**
	 * default constructor
	 */
	public FlightInfo() {
		this(new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(),
				new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(), new IntWritable(),
				new IntWritable());
	}

	/**
	 * parameterized constructor
	 */
	public FlightInfo(IntWritable orgAirportId, IntWritable destAirportId, IntWritable crsDepTime,
			IntWritable crsArrTime, IntWritable year, IntWritable month, IntWritable day, IntWritable arrTime,
			IntWritable depTime, IntWritable cancelled, IntWritable index) {

		this.orgAirportId = orgAirportId;
		this.destAirportId = destAirportId;
		this.crsDepTime = crsDepTime;
		this.crsArrTime = crsArrTime;
		this.year = year;
		this.month = month;
		this.day = day;
		this.arrTime = arrTime;
		this.depTime = depTime;
		this.cancelled = cancelled;
		this.index = index;
	}

	@Override
	/**
	 * Serialize the fields of this object to out.
	 */
	public void write(DataOutput out) throws IOException {
		orgAirportId.write(out);
		destAirportId.write(out);
		crsDepTime.write(out);
		crsArrTime.write(out);
		year.write(out);
		month.write(out);
		day.write(out);
		arrTime.write(out);
		depTime.write(out);
		cancelled.write(out);
		index.write(out);
	}

	@Override
	/**
	 * DeSerialize the fields of this object to out.
	 */
	public void readFields(DataInput in) throws IOException {
		orgAirportId.readFields(in);
		destAirportId.readFields(in);
		crsDepTime.readFields(in);
		crsArrTime.readFields(in);
		year.readFields(in);
		month.readFields(in);
		day.readFields(in);
		arrTime.readFields(in);
		depTime.readFields(in);
		cancelled.readFields(in);
		index.readFields(in);
	}

	public IntWritable getOrgAirportId() {
		return orgAirportId;
	}

	public void setOrgAirportId(IntWritable orgAirportId) {
		this.orgAirportId = orgAirportId;
	}

	public IntWritable getDestAirportId() {
		return destAirportId;
	}

	public void setDestAirportId(IntWritable destAirportId) {
		this.destAirportId = destAirportId;
	}

	public IntWritable getCrsDepTime() {
		return crsDepTime;
	}

	public void setCrsDepTime(IntWritable crsDepTime) {
		this.crsDepTime = crsDepTime;
	}

	public IntWritable getCrsArrTime() {
		return crsArrTime;
	}

	public void setCrsArrTime(IntWritable crsArrTime) {
		this.crsArrTime = crsArrTime;
	}

	public IntWritable getYear() {
		return year;
	}

	public void setYear(IntWritable year) {
		this.year = year;
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

	public IntWritable getArrTime() {
		return arrTime;
	}

	public void setArrTime(IntWritable arrTime) {
		this.arrTime = arrTime;
	}

	public IntWritable getDepTime() {
		return depTime;
	}

	public void setDepTime(IntWritable depTime) {
		this.depTime = depTime;
	}

	public IntWritable getCancelled() {
		return cancelled;
	}

	public void setCancelled(IntWritable cancelled) {
		this.cancelled = cancelled;
	}

	public IntWritable getIndex() {
		return index;
	}

	public void setIndex(IntWritable index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return getYear().toString() + " " + getMonth().toString() + " " + getDay().toString() + " "
				+ getCrsDepTime().toString() + " " + getCrsArrTime().toString();
	}

}
