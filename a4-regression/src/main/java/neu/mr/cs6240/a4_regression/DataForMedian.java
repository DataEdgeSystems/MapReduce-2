package neu.mr.cs6240.a4_regression;

/**
 * This class is used to wrap data before finding the median price
 * 
 * @author Ajay Subramanya
 * @author Smitha Bangalore Naresh
 *
 */

public class DataForMedian implements Comparable<DataForMedian> {
	private String date;
	private Integer price;

	/**
	 * default constructor
	 */
	public DataForMedian() {
	}

	/**
	 * getters and setters
	 */
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getMedianP() {
		return price;
	}

	public void setMedianP(Integer avgP) {
		this.price = avgP;
	}

	public DataForMedian(String date, Integer avgP) {
		this.date = date;
		this.price = avgP;
	}

	/**
	 * overriding comapareTo
	 */

	@Override
	public int compareTo(DataForMedian o) {
		return this.getMedianP().compareTo(o.getMedianP());
	}

}
