package neu.mr.cs6240.a6_prediction;

import neu.mr.cs6240.common.DataValidation;

/**
 * Main class : invoked the driver after perform validation on the passed
 * command line arguments
 *
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */
public class App {
	/**
	 *
	 * @param args
	 *            args0 : training set, args1 : output folder, args2 :test set
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.err.println("Usage:  -train=<train dataset> -output=<results> -test=<test dataset>");
			System.exit(-1);
		}
		String[] vArgs = DataValidation.validateCLIArgs(args);
		PredictionDriver.toolRunnerWrapper(vArgs);
	}
}
