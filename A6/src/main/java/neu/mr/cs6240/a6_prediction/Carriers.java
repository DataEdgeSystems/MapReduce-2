package neu.mr.cs6240.a6_prediction;

/**
 * Top 10 carriers picked after running a MR job and finding the airports with
 * most flights. These were the only airports we found in the provided dataset
 * 
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

public enum Carriers {

	AA(1), AS(2), CO(3), DL(4), HP(5), NW(6), TW(7), UA(8), US(9), WN(10), OTHER(11);

	private int value;

	private Carriers(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}
};
