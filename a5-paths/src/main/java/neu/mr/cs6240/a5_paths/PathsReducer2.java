package neu.mr.cs6240.a5_paths;

/**
 * Reducer 2: Reducer 2 sums up Results{connections, missed} for a year and
 * airline
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PathsReducer2 extends Reducer<Text, Result, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Result> results, Reducer<Text, Result, Text, Text>.Context context)
			throws IOException, InterruptedException {
		int connections = 0;
		int missed = 0;
		for (Result r : results) {
			connections += r.getConnections().get();
			missed += r.getMissed().get();
		}
		context.write(key, new Text(connections + " " + missed));
	}

}
