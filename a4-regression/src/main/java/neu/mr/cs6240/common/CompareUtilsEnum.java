/**
 * 
 */
package neu.mr.cs6240.common;

/**
 * enum for sorting either in increasing or decreasing order
 * 
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */
public enum CompareUtilsEnum {
	INCREASING(1), DECREASING(2);
	@SuppressWarnings("unused")
	private int value;

	private CompareUtilsEnum(int value) {
		this.value = value;
	}
};
