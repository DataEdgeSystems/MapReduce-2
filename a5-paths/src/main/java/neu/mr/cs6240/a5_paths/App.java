package neu.mr.cs6240.a5_paths;

/**
 * The main class of the project. Does not do much , just does some 
 * CLI argument validation and invokes run from the job driver
 * 
 * @author Smitha Bangalore Naresh 
 * @author Ajay Subramanya
 */

import neu.mr.cs6240.common.DataValidation;

public class App {
	public static void main(String[] preArgs) throws Exception {
		String[] args = DataValidation.validateCLIArgs(preArgs);

		if (args.length != 2) {
			System.err.println("Usage:  -input=<input path> -output=<output path>");
			System.exit(-1);
		}

		PathsDriver.toolRunnerWrapper(args);
	}
}
