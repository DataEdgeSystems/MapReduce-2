package neu.mr.cs6240.multi_thread;

import java.io.BufferedReader;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/04/2016
 * @info Assignment 4
 * Implements Callable interface to read data from the file
 * and checks if the line is sane then puts the data
 * (airline name, month, average price in cents) to HashBasedTable
 * and same is returned
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import au.com.bytecode.opencsv.CSVReader;
import neu.mr.cs6240.common.DataValidation;
import neu.mr.cs6240.common.FileUtility;
import neu.mr.cs6240.common.FlightContants;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/05/2016
 * @info Assignment 3 This class implements Callable interface which is a job(or
 *       work) done by the worker threads
 */
public class WorkerAirlineJob implements Callable<Table<String, Integer, AvgPriceLstYear>> {

	private File fileName;

	public File getFileName() {
		return fileName;
	}

	public void setFileName(File fileName) {
		this.fileName = fileName;
	}

	WorkerAirlineJob(File file) {
		this.fileName = file;
	}

	/**
	 * Implements Callable interface to read data from the file and checks if
	 * the line is sane then puts the data (airline name, month, average price
	 * in cents) to HashBasedTable and same is returned
	 *
	 * @param infile
	 * @return HashBasedTable
	 */
	public static Table<String, Integer, AvgPriceLstYear> ReadGZipFileLineByLine(File infile) {
		BufferedReader br = FileUtility.readGZipFile(infile);

		Table<String, Integer, AvgPriceLstYear> airlineMonthTable = HashBasedTable.create();

		if (br != null) {
			String[] nextLine;
			CSVReader reader = new CSVReader(br, ',', '"', 1);
			int month = 0;
			int year = 0;
			String airLineName = "";
			double avgPrice = Double.NaN;
			boolean isCarrierActive2015 = false;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine.length > 0 && DataValidation.isSane(nextLine)) {

						isCarrierActive2015 = false;
						airLineName = nextLine[8];
						month = Integer.parseInt(nextLine[2]);
						year = Integer.parseInt(nextLine[0]);
						avgPrice = Double.parseDouble(nextLine[109]);

						if (year == 2015)
							isCarrierActive2015 = true;

						if (airlineMonthTable.contains(airLineName, month)) {
							// insert avg Price if key already present
							airlineMonthTable.get(airLineName, month).getAvgPrice()
									.add((int) (avgPrice * FlightContants.CENTS));

							// set the flag airline active in 2015
							boolean existVal = airlineMonthTable.get(airLineName, month).getIsActive2015();
							airlineMonthTable.get(airLineName, month).setIsActive2015(existVal | isCarrierActive2015);
						} else {
							// insert key and value as its not already present
							ArrayList<Integer> avgPriceList = new ArrayList<Integer>();
							avgPriceList.add((int) (avgPrice * FlightContants.CENTS));
							airlineMonthTable.put(airLineName, month,
									new AvgPriceLstYear(avgPriceList, isCarrierActive2015));
						}

					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return airlineMonthTable;
	}

	@Override
	public Table<String, Integer, AvgPriceLstYear> call() {
		Table<String, Integer, AvgPriceLstYear> result = ReadGZipFileLineByLine(getFileName());
		return result;
	}

}
