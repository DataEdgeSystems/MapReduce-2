package neu.mr.cs6240.multi_thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import neu.mr.cs6240.common.AirlineMonthPrice;
import neu.mr.cs6240.common.FileUtility;
import neu.mr.cs6240.common.FlightContants;
import neu.mr.cs6240.common.MeanMedianUtility;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/05/2016
 * @info Assignment 3 This class defines methods to run the program using
 *       multiple threads
 */
public class MultiThreaded {

	/* mCv Results to this output file for single threaded execution */
	private String fileOutputFileName;

	public String getFileOutputFileName() {
		return fileOutputFileName;
	}

	public void setFileOutputFileName(String fileOutputFileName) {
		this.fileOutputFileName = fileOutputFileName;
	}

	public MultiThreaded(String fileOutputFileName) {
		this.fileOutputFileName = fileOutputFileName;
	}

	public void multiThreadedPriceCal(String dirName, String meanOrMedian) {

		ExecutorService exceutor = Executors.newFixedThreadPool(FlightContants.NUM_OF_THREADS);
		// create a list to hold the Future object associated with Callable
		List<Future<Table<String, Integer, AvgPriceLstYear>>> list = new ArrayList<Future<Table<String, Integer, AvgPriceLstYear>>>();

		File[] listOfFiles = null;
		File folder = new File(dirName);

		// read each file and submit it to worker thread to process
		try {
			if (folder.isDirectory()) {
				listOfFiles = folder.listFiles();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						Future<Table<String, Integer, AvgPriceLstYear>> future = exceutor
								.submit(new WorkerAirlineJob(file));
						// add Future to the list, we can get return value using
						// Future
						list.add(future);
					}
				}
			}
		} catch (NullPointerException e) {
			System.err.println("Null Pointer Exception : Input directory not present");
		}

		// Merge all the results to get final output
		Table<String, Integer, ArrayList<Integer>> finalResults = HashBasedTable.create();
		HashMap<String, Boolean> hmIsActive2015 = new HashMap<>();
		for (Future<Table<String, Integer, AvgPriceLstYear>> fut : list) {
			Table<String, Integer, AvgPriceLstYear> perFile;
			try {
				perFile = fut.get();
				for (String flight : perFile.rowKeySet()) {
					for (int month = 1; month <= FlightContants.TOTALMONTHS_12; month++) {
						AvgPriceLstYear objPY = perFile.get(flight, month);
						if (objPY != null) {
							// merge all the results
							ArrayList<Integer> avgPLst = objPY.getAvgPrice();

							if (hmIsActive2015.containsKey(flight)) {
								boolean existVal = hmIsActive2015.get(flight);
								hmIsActive2015.put(flight, existVal | objPY.getIsActive2015());
							} else {
								hmIsActive2015.put(flight, objPY.getIsActive2015());
							}
							if (avgPLst != null) {
								Collections.sort(avgPLst);
								if (finalResults.contains(flight, month)) {
									finalResults.get(flight, month).addAll(avgPLst);
								} else {
									ArrayList<Integer> priceLst = new ArrayList<Integer>();
									priceLst.addAll(avgPLst);
									finalResults.put(flight, month, priceLst);
								}
							}
						}
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		/* Calculating mean */
		if (meanOrMedian.equals("e")) {
			ArrayList<AirlineMonthPrice> res = MeanMedianUtility.calculateMeanForAllAirlines(finalResults);
			FileUtility.printhmMonthPrice(res, "multi_mean", hmIsActive2015);
		}

		/* Calculating median */
		if (meanOrMedian.equals("d")) {
			ArrayList<AirlineMonthPrice> res = MeanMedianUtility.calculateMedianForAllAirlines(finalResults);
			FileUtility.printhmMonthPrice(res, "multi_median", hmIsActive2015);
		}

		exceutor.shutdown();
		try {
			exceutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
