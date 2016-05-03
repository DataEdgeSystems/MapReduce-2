package neu.mr.cs6240.a4_regression;

/**
 * This class is used to wrap Average ticket price and CRSElapsed time into an
 * object, we would be using the two variables as data point pairs (x,y)
 * 
 * @author Smitha Bangalore
 * @author Ajay Subramanya
 *
 */
public class DataPoint {
	double avgP;
	double sTime;

	/**
	 * Parameterized constructor
	 * 
	 * @param avgP
	 * @param sTime
	 */
	public DataPoint(double avgP, double sTime) {
		this.avgP = avgP;
		this.sTime = sTime;
	}

	/**
	 * getters and setters
	 */
	public double getAvgP() {
		return avgP;
	}

	public void setAvgP(double avgP) {
		this.avgP = avgP;
	}

	public double getsTime() {
		return sTime;
	}

	public void setsTime(double sTime) {
		this.sTime = sTime;
	}
}
