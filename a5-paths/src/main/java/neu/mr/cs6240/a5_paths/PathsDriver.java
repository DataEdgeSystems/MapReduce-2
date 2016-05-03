package neu.mr.cs6240.a5_paths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import neu.mr.cs6240.common.FlightContants;

/**
 * Driver of MapReduce jobs
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */
public class PathsDriver extends Configured implements Tool {

	public static void toolRunnerWrapper(String[] args) throws Exception {
		ToolRunner.run(new PathsDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		Job job1 = Job.getInstance(conf);

		job1.setJarByClass(PathsDriver.class);

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(FlightInfo.class);

		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, new Path(FlightContants.TEMP_FILE));

		job1.setMapperClass(PathsMapper1.class);
		job1.setReducerClass(PathsReducer1.class);

		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);

		job1.submit();

		/**
		 * If job 1 aborts we do not go any further
		 */
		if (!job1.waitForCompletion(true)) {
			System.err.println("Aborted due to failure of job1");
			System.exit(-1);
		}

		Job job2 = Job.getInstance(conf);
		job2.setJarByClass(PathsDriver.class);

		job2.setMapperClass(PathsMapper2.class);
		job2.setReducerClass(PathsReducer2.class);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Result.class);

		FileInputFormat.addInputPath(job2, new Path(FlightContants.TEMP_FILE));
		FileOutputFormat.setOutputPath(job2, new Path(args[1]));

		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);

		job2.setNumReduceTasks(1);

		job2.submit();

		return job2.waitForCompletion(true) ? 0 : 1;
	}

}
