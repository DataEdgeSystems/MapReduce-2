################################################################################
#			     README-A4 REGRESSION			       #
# 		Authors : Ajay Subramanya,Smitha Bangalore Naresh	       #
# 		subramanya{dot}a{at}husky{dot}neu{dot}edu		       #
# 		bangalorenaresh{dot}s{at}husky{dot}neu{dot}edu		       #
################################################################################


################################  PREREQUISITES ################################

* Update perf.txt with your setup specific details. Instructions are present in 
  perf.txt

* We are using Apache Maven 3.3.9 for dependency management and build automation

* Please `chmod +x runCloud.sh` if it is not already executable

################################## RUN SCRIPT ##################################

* `./runCloud.sh` to run the script 

* You will be prompted to key in a few values we may need, please enter then as 
  required

* The script is interactive and will tell you the about the progress

* The report (Graphsa4.pdf) will be generated in the current directory after the
  script has completed running

* There is also an option to run the code in pseudo-distribution mode, 
  these are the commands , note this run will only `cat` results to the console 
  and will not generate any reports. Use this only for testing purpose.

  > Use prof.'s makefile to start hadoop		
  > Make sure you do `hadoop fs -get path/to/your/25/files/* input`
  > Do `make run200` to run for time=200
  > Do `make run1` to run for time=1

################################## CAVEATS #####################################

* Main class is App.java

* Project structure is as below,
.
├── Graphsa4.R
├── Graphsa4.Rmd
├── README.txt
├── contributors.txt
├── makefile
├── output_for_cloud
├── perf.txt
├── pom.xml
├── runCloud.sh
├── sampleOutput
│   ├── Graphsa4.pdf
│   ├── Rplots.pdf
│   ├── a4_regression_sa_1.csv
│   └── a4_regression_sa_200.csv
├── src
│   ├── main
│   └── test
└── uber-perf-0.0.1.jar 

* We are using the prof.'s code to upload to S3 and kick off the 
  cluster[With minor changes]

* The run script will take considerable amount of time since there are two 
  runs (Time = 200 & 1)

################################## DESIGN ######################################

* Two MapReduce jobs have been used , and between them there has been a small 
  computation being done in the driver to check the cheapest airline overall.

+-----------------------------------+
|Mapper1 (M1): 			    |
|What? : Reads the csv and emits    | 
|some values                        |
|Takes : CSV                        |
|Emits : Year, Obj(carrier_name,    |
|avg_price, elapsed_time)           |
+-----------------+-----------------+
                  |
+-----------------+-----------------+
|Reducer 1 (R1):                    |
|What? : computes the cheapest 	    |
|flight for a Year                  |
|Takes : Year, Obj(carrier_name,    |
|avg_price, elapsed_time)           |
|Emits : carrier_name, year, price  |
+-----------------+-----------------+
                  |
+-----------------+-----------------+
|Driver (D) :                	    |
|What? : computes the cheapest      |
|flight over the years from all the |
|reducers                           |
|Takes : reads from the o\p of R1   |
|Emits : The cheapest for the given |
|time. Sets the cheapest flight to  |
|Configuration (conf) 		    |
+-----------------+-----------------+
				  | 
+-----------------+-----------------+
|Mapper2 (M2): 			    |
|What? : Reads the csv and the conf | 
|for the cheapest carrier and emits |
|details of the cheapest airline    |
|Takes : CSV & Configuration obj    |
|Emits : Year, Obj(avg_price,month  |
|day)           		    |
+-----------------+-----------------+
                  |
+-----------------+-----------------+
|Reducer 2 (R2):                    |
|What? : computes the median for a  |
|week for the cheapest airline and  |
|writes to context                  |
|Takes : Year, Obj(avg_price,month  |
|day)           		    |
|Emits : date, week#, price  	    |
+-----------------------------------+

* we chose to perform intermediate computation in the driver to avoid 
  having an other file write or a other job 

################################### THE END #################################### 
