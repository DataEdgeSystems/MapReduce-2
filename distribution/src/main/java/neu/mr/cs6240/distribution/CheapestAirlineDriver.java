package neu.mr.cs6240.distribution;

import java.io.File;

import org.apache.commons.io.FileUtils;
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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import neu.mr.cs6240.distribution.mapper.CheapestAirlineMapper;
import neu.mr.cs6240.distribution.reducer.CheapestAirlineReducer;

/**
 * Assignment 3 A2 - Distribution. As data sizes will increase the single
 * machine version of your program will not scale. Develop a version of A1 using
 * the Hadoop Map Reduce API. Fine print: (0) Group assignment, two students.
 * (1) Provide code that can run in pseudo-distributed mode as well as on EMR.
 * (3) Produce a graph that plots the average ticket price for each month for
 * each airline. Use R. No other output is required. (3) Include a script that
 * executes everything and produces the graph. For example, if you use the Unix
 * make command, you should have two targets pseudo and cloud such that typing
 * make pseudo will create a HDFS file system, start hadoop, run your job, get
 * the output, and produce the graph. Typing make pseudo will run your code on
 * EMR. (4) Only plot airlines with flights in 2015, limit yourself to the 10
 * airlines with the most flights overall. (5) Information on how to setup AWS
 * is here. (6) Write a one page report that documents your implementation and
 * that describes your results. The report should be automatically constructed
 * as part of running the project to include the plot. (Hint: use LaTeX or
 * Markdown) (7) Submit a tar.gz file which unpacks into a directory name
 * "LastName1_LastName2_A2". That directory should contain a README file that
 * explains how to build and run your code. Make sure that the code is portable.
 * Document what it requires. (8) The reference solution builds off A1, adding
 * 154 lines of Java code and 15 lines of R code.
 *
 * Driver class - Configure and submit MapReduce job
 * 
 * @author ajay subramanya
 * @author smitha bangalore naresh
 * @date 01/29/2016
 * 
 */
public class CheapestAirlineDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: CheapestAirlineDriver <input path> <output path>");
			System.exit(-1);
		}
		ToolRunner.run(new CheapestAirlineDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Job job = Job.getInstance(new Configuration());

		job.setJarByClass(CheapestAirlineDriver.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(CheapestAirlineMapper.class);
		job.setReducerClass(CheapestAirlineReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.submit();

		if (job.waitForCompletion(true)) {
			Multimap<String, Result> res = ArrayListMultimap.create();
			String[] resFile = FileUtils.readFileToString(new File(args[1] + ".txt")).split("\n");

			for (String row : resFile) {
				System.out.println("each row " + row);
				String[] items = row.split(",");
				res.put(items[0], new Result(Short.parseShort(items[1]), Double.parseDouble(items[2]),
				        Boolean.parseBoolean(items[3])));
			}

			for (String carrier : res.keySet()) {
				boolean active = false;
				for (Result r : res.get(carrier)) {
					active = r.isActive();
				}
				if (!active) res.removeAll(carrier);
			}

			System.out.println("final " + res.toString());

		}

		return job.waitForCompletion(true) ? 0 : 1;
	}

}

class Result {
	short month;
	double avg;
	boolean active;

	public Result(short month, double avg, boolean active) {
		super();
		this.month = month;
		this.avg = avg;
		this.active = active;
	}

	public short getMonth() {
		return month;
	}

	public void setMonth(short month) {
		this.month = month;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
