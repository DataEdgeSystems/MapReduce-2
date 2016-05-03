package neu.mr.cs6240.common;

/**
 * This class is used to hold any comparison util methods
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CompareUtils {
	/* sort the output list for Mean price of flights */
	public static Map<String, Double> sortHashMapVals(Map<String, Double> hm, final CompareUtilsEnum order) {
		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(hm.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				if (order == CompareUtilsEnum.INCREASING) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}

			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}