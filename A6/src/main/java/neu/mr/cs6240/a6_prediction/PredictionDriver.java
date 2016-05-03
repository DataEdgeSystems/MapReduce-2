package neu.mr.cs6240.a6_prediction;

/**
* the driver of MR jobs, use the submit jobs to create any more jobs you may need
*
* Job 1 : takes the training set and creates the random forest model.
* Job 2 : takes the test set and classifies each row based on the model from job 1
*
* @author Smitha Bangalore Naresh
* @author Ajay Subramanya
*
*/

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import neu.mr.cs6240.common.Consts;

public class PredictionDriver extends Configured implements Tool {

	/**
	 * wrapper around the overridden method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void toolRunnerWrapper(String[] args) throws Exception {
		ToolRunner.run(new PredictionDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job job1 = sumbitJob(args[0], Consts.TEMP_FILE, conf, PredictionMapper1.class, PredictionReducer1.class,
				FlightData.class);
		waitForJob(job1);

		Job job2 = sumbitJob(args[2], args[1], conf, EvaluateMapper2.class, EvaluateReducer2.class, TestData.class);
		waitForJob(job2);

		return 0;
	}

	/**
	 * submits a job application to YARN
	 * 
	 * @param inputPath
	 *            path that contains the input files
	 * @param outputPath
	 *            path that contains the input files
	 * @param conf
	 *            the Configuration object
	 * @param map
	 *            the mapper class
	 * @param reduce
	 *            the reducer class
	 * @param outputVal
	 *            the output value class that the job will emit
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Job sumbitJob(String inputPath, String outputPath, Configuration conf, Class map, Class reduce,
			Class outputVal) throws IOException, InterruptedException, ClassNotFoundException {
		Job job = Job.getInstance(conf);
		job.setJarByClass(PredictionDriver.class);
		job.setOutputKeyClass(ShortWritable.class);
		job.setOutputValueClass(outputVal);
		FileInputFormat.addInputPath(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		job.setMapperClass(map);
		job.setReducerClass(reduce);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.submit();
		return job;
	}

	/**
	 * waits for the supplied job to finish, if it does not then error is thrown
	 * 
	 * @param job
	 *            the supplied job
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	private void waitForJob(Job job) throws IOException, InterruptedException, ClassNotFoundException {
		if (!job.waitForCompletion(true)) {
			new Error("Aborted due to failure of job" + job.getJobName());
			System.exit(-1);
		}
	}

}
