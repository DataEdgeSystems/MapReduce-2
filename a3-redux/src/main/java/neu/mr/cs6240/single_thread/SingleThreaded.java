
package neu.mr.cs6240.single_thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import au.com.bytecode.opencsv.CSVReader;
import neu.mr.cs6240.common.AirlineMonthPrice;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.FileUtility;
import neu.mr.cs6240.common.FlightContants;
import neu.mr.cs6240.common.MeanMedianUtility;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/05/2016
 * @info Assignment 3 This class defines methods to run the program sequentially
 *       (i.e. in Single Threaded environment)
 */
public class SingleThreaded {

	/**
	 * HashBasedTable to store average price values in cents. Key to
	 * HashBasedTable is airline name as row and month as column
	 */
	private static Table<String, Integer, ArrayList<Integer>> airlineMonthTable = HashBasedTable.create();

	/**
	 * HashTable which maintains if flight is active in 2015
	 */
	private static HashMap<String, Boolean> hmIsActive2015 = new HashMap<>();
	/* mCv Results to this output file for single threaded execution */
	private String fileOutputFileName;

	public String getFileOutputFileName() {
		return fileOutputFileName;
	}

	public void setFileOutputFileName(String fileOutputFileName) {
		this.fileOutputFileName = fileOutputFileName;
	}

	public SingleThreaded(String fileOutputFileName) {
		this.fileOutputFileName = fileOutputFileName;
	}

	/**
	 * Method reads all files in the given directory and computes mean or median
	 *
	 * @param dirName
	 *            - Input directory from where files are read
	 * @param meanOrMedian
	 *            - To calculate Mean or Median based on command line arguments
	 *            (i.e. -e for mean, -d for median, -f for fast median)
	 */
	public void singleThreadedPriceCal(String dirName, String meanOrMedian) {

		File[] listOfFiles = null;
		File folder = new File(dirName);

		try {
			if (folder.isDirectory()) {
				listOfFiles = folder.listFiles();
				// read each file and submit it to worker thread to process
				for (File file : listOfFiles) {
					if (file.isFile()) {
						processDataInFile(file);
					}
				}
			}
		} catch (NullPointerException e) {
			System.err.println("Null Pointer Exception : Input directory not present");
		}

		/* Calculating mean */
		if (meanOrMedian.equals("e")) {
			ArrayList<AirlineMonthPrice> res = MeanMedianUtility.calculateMeanForAllAirlines(airlineMonthTable);
			FileUtility.printhmMonthPrice(res, "seq_mean", hmIsActive2015);
		}

		/* Calculating median */
		if (meanOrMedian.equals("d")) {
			ArrayList<AirlineMonthPrice> res = MeanMedianUtility.calculateMedianForAllAirlines(airlineMonthTable);
			FileUtility.printhmMonthPrice(res, "seq_median", hmIsActive2015);
		}
	}

	/**
	 * Method reads the file line by line using CSVReader and stores the sane
	 * data in HashTable where airline name is the row and month is the column
	 * and average price in cents is the value
	 *
	 * @param file
	 *            - input file to read
	 */

	private void processDataInFile(File file) {
		BufferedReader br = FileUtility.readGZipFile(file);

		if (br != null) {
			String[] nextLine;
			CSVReader reader = new CSVReader(br, ',', '"', 1);
			try {
				boolean isCarrierActive2015 = false;
				String airLineName = "";
				int month = 0;
				int year = 0;
				double avgPrice = 0.0;

				while ((nextLine = reader.readNext()) != null) {
					if (nextLine.length > 0 && DataValidation.isSane(nextLine)) {

						airLineName = nextLine[8];
						month = Integer.parseInt(nextLine[2]);
						year = Integer.parseInt(nextLine[0]);
						avgPrice = Double.parseDouble(nextLine[109]);
						isCarrierActive2015 = false;
						if (airlineMonthTable.contains(airLineName, month)) {
							// insert avg Price if key already present
							airlineMonthTable.get(airLineName, month).add((int) (avgPrice * FlightContants.CENTS));
						} else {
							// insert key and value as its not already present
							ArrayList<Integer> avgPriceList = new ArrayList<>();
							avgPriceList.add((int) (avgPrice * FlightContants.CENTS));
							airlineMonthTable.put(airLineName, month, avgPriceList);
						}

						if (year == 2015)
							isCarrierActive2015 = true;

						if (!hmIsActive2015.containsKey(airLineName)) {
							hmIsActive2015.put(airLineName, isCarrierActive2015);
						} else {
							boolean newVal = hmIsActive2015.get(airLineName) | isCarrierActive2015;
							hmIsActive2015.put(airLineName, newVal);
						}

					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
