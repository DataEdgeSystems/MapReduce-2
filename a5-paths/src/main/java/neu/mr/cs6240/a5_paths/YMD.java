package neu.mr.cs6240.a5_paths;

/**
 * Wrapper around year, month and day 
 * 
 * @author Smitha Banglore Naresh 
 * @author Ajay Subramanya
 */

import java.util.Comparator;

class YMD implements Comparator<YMD> {
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public YMD(Integer year, Integer month, Integer day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public YMD() {
	}

	@Override
	public String toString() {
		return "YMD [year=" + year + ", month=" + month + ", day=" + day + "]";
	}

	Integer year;
	Integer month;
	Integer day;

	@Override
	public int compare(YMD o1, YMD o2) {

		int res = 0;
		if ((res = (o1.getYear().compareTo(o2.getYear()))) != 0) {
			return res;
		} else {
			if ((res = (o1.getMonth().compareTo(o2.getMonth()))) != 0) {
				return res;
			} else {
				if ((res = (o1.getDay().compareTo(o2.getDay()))) != 0) {
					return res;
				}
				return 0;
			}
		}
	}

}
