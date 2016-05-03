package neu.mr.cs6240.a5_paths;

/**
 * to unpack FlightInfo while can not be used as is , because it implements
 * Writable
 * 
 * @author Ajay Subramanya
 * @author Smitha Bangalore Naresh
 *
 */

public class UnpackFlightInfo {
	private int orgAirportId;
	private int destAirportId;
	private int crsDepTime;
	private int crsArrTime;
	private int year;
	private int month;
	private int day;
	private int arrTime;
	private int depTime;
	private int cancelled;

	public int getOrgAirportId() {
		return orgAirportId;
	}

	public void setOrgAirportId(int orgAirportId) {
		this.orgAirportId = orgAirportId;
	}

	public int getDestAirportId() {
		return destAirportId;
	}

	public void setDestAirportId(int destAirportId) {
		this.destAirportId = destAirportId;
	}

	public int getCrsDepTime() {
		return crsDepTime;
	}

	public void setCrsDepTime(int crsDepTime) {
		this.crsDepTime = crsDepTime;
	}

	public int getCrsArrTime() {
		return crsArrTime;
	}

	public void setCrsArrTime(int crsArrTime) {
		this.crsArrTime = crsArrTime;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getArrTime() {
		return arrTime;
	}

	public void setArrTime(int arrTime) {
		this.arrTime = arrTime;
	}

	public int getDepTime() {
		return depTime;
	}

	public void setDepTime(int depTime) {
		this.depTime = depTime;
	}

	public int getCancelled() {
		return cancelled;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public String toString() {
		return getYear() + " " + getMonth() + " " + getDay() + " " + getCrsDepTime() + " " + getCrsArrTime() + " "
				+ getOrgAirportId() + " " + getDestAirportId();
	}

}
