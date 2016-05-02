package neu.mr.cs6240.pseudo_cloud;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 01/29/2016
 * @info Assignment 3
 * A2 - Distribution. As data sizes will increase the single machine version of your program
 	will not scale. Develop a version of A1 using the Hadoop Map Reduce API.
	Fine print: (0) Group assignment, two students.
	(1) Provide code that can run in pseudo-distributed mode as well as on EMR.
	(3) Produce a graph that plots the average ticket price for each month for each airline.
	Use R. No other output is required. (3) Include a script that executes everything and produces
	the graph. For example, if you use the Unix make command, you should have two targets pseudo
 	and cloud such that typing make pseudo will create a HDFS file system, start hadoop,
  	run your job, get the output, and produce the graph. Typing  make pseudo will run your
  	code on EMR. (4) Only plot airlines with flights in 2015, limit yourself to the 10 airlines
  	with the most flights overall. (5) Information on how to setup AWS is here.
  	(6) Write a one page report that documents your implementation and that describes your
  	results. The report should be automatically constructed as part of running the project to
  	include the plot. (Hint: use LaTeX or Markdown) (7) Submit a tar.gz file which unpacks
  	into a directory name "LastName1_LastName2_A2". That directory should contain a README
  	file that explains how to build and run your code. Make sure that the code is portable.
  	Document what it requires.
  	(8) The reference solution builds off A1, adding 154 lines of Java code and 15 lines of
  	R code.
 *
 * Driver class -
 * Configure and submit MapReduce job
 * */
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

import neu.mr.cs6240.common.FileUtility;

public class AvgPricePerMonthCal extends Configured implements Tool {

	public void validateArgsAndRun(String[] args) throws Exception {
		if (args.length != 4) {
			System.err.println("Usage: AvgPricePerMonthCal <input path> <output path> <mean|median>");
			System.exit(-1);
		}
		ToolRunner.run(new AvgPricePerMonthCal(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Job job = Job.getInstance(new Configuration());

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(FileUtility.appendTimeStampTo(args[1])));

		job.setJarByClass(AvgPricePerMonthCal.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(AirlineMapper.class);
		setReducer(args, job);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.submit();

		return (job.waitForCompletion(true) ? 0 : 1);
	}

	private void setReducer(String[] args, Job job) {
		switch (args[3]) {
		case "e":
			job.setReducerClass(MeanPerMonthReducer.class);
			break;
		case "d":
			job.setReducerClass(MedianPerMonthReducer.class);
			break;
		case "f":
			job.setReducerClass(FastMedianPerMonthReducer.class);
			break;
		default:
			System.err.println("Invalid calculate option");
			break;
		}
	}
}
