package neu.mr.cs6240.multi_thread;

import java.util.ArrayList;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/05/2016
 * @info Maintains the AvgPrice List and boolean flag to check which airlines
 *       Active in 2015
 */
public class AvgPriceLstYear {

	public ArrayList<Integer> getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(ArrayList<Integer> avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Boolean getIsActive2015() {
		return isActive2015;
	}

	public void setIsActive2015(Boolean isActive2015) {
		this.isActive2015 = isActive2015;
	}

	public AvgPriceLstYear(ArrayList<Integer> avgPrice, Boolean isActive2015) {
		super();
		this.avgPrice = avgPrice;
		this.isActive2015 = isActive2015;
	}

	ArrayList<Integer> avgPrice;
	Boolean isActive2015;

}
