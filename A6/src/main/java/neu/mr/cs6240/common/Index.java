package neu.mr.cs6240.common;

/**
 * enum to store the values coming in from the CSV
 * 
 * @author Smitha Naresh Bangalore
 * @author Ajay Subramanya
 *
 */

public enum Index {
	YEAR(0), QUARTER(1), MONTH(2), DAY_OF_MONTH(3), DAY_OF_WEEK(4), DATE(5), UNIQUE_CARRIER(8), FLIGHT_NUM(10), ORIGIN(
			14), DEST(23),

	CRS_DEP_TIME(29), CRS_ELAPSED_TIME(50), DISTANCE(54), CRS_ARR_TIME(40), DEP_TIME(30), ARR_TIME(41), ARR_DELAY(
			42), CANCELLED(47);

	private int value;

	private Index(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}
};
