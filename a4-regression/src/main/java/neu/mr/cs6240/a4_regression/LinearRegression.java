package neu.mr.cs6240.a4_regression;

/**
 * This class is used to house methods that are 
 * used to compute linear regression 
 * 
 * @author Smitha Bangalore
 * @author Ajay Subramanya
 */
import java.util.ArrayList;

public class LinearRegression {
	/**
	 * 
	 * @param dpLst
	 *            a list of Objects of @see
	 *            {neu.mr.cs6240.a4_regression.DataPoint}
	 * @param N
	 *            a value of time for which we need to find the average price
	 * @return the value of price for the given air time
	 */
	public static Double getRegressionValue(ArrayList<DataPoint> dpLst, int N) {
		double sumScheduledTime = 0;
		double xBar = 0;
		double x2Bar = 0;
		double sumAvgPrices = 0;
		double yBar = 0;
		double xyBar = 0;
		double x = 0;
		double y = 0;
		double slope = 0;
		double intercept = 0;
		int counter = 0;

		/**
		 * Iterate over the data points and compute the sum of all x's and sum
		 * of all y's
		 */
		for (DataPoint dp : dpLst) {
			counter++;
			sumScheduledTime += dp.getsTime();
			sumAvgPrices += dp.getAvgP();
		}

		/**
		 * computing the mean of x and mean of y
		 */
		xBar = sumScheduledTime / counter;
		yBar = sumAvgPrices / counter;

		/**
		 * computing squares of xbar and ybar
		 */
		for (DataPoint dp : dpLst) {
			x = dp.getsTime();
			y = dp.getAvgP();

			x2Bar += (x - xBar) * (x - xBar);
			xyBar += (x - xBar) * (y - yBar);
		}
		/**
		 * finally computing the slope and intercept
		 */
		slope = xyBar / x2Bar;
		intercept = yBar - slope * xBar;
		/**
		 * returning the regression value for N
		 */
		return (intercept + slope * N);
	}

}
