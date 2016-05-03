package neu.mr.cs6240.common;

/**
 * enum to store the values coming in from the CSV
 * 
 * @author Smitha Naresh Bangalore
 * @author Ajay Subramanya
 *
 */

public enum CsvConstants {

	YEAR(0), MONTH(2), DAY_OF_MONTH(3), UNIQUE_CARRIER(8), ORIGIN_AIRPORT_ID(11), DEST_AIRPORT_ID(20),

	CRS_DEP_TIME(29), CRS_ARR_TIME(40), DEP_TIME(30), ARR_TIME(41), CANCELLED(47);

	private int value;

	private CsvConstants(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}
};
