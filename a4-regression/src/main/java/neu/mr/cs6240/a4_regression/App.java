package neu.mr.cs6240.a4_regression;

/**
 * This is the main file of the project 
 * 
 * Validates the passed arguments and invokes the Job Driver
 * 
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 */
import neu.mr.cs6240.common.DataValidation;

public class App {
	public static void main(String[] preArgs) throws Exception {
		String[] args = DataValidation.validateCLIArgs(preArgs);

		if (args.length != 3) {
			System.err.println("Usage:  -input=<input path> -output=<output path> -time=N");
			System.exit(-1);
		}

		RegressionDriver.validateArgsAndRun(args);
	}
}
