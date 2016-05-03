################################################################################
#			     README-A5 PATHS				       #
# 		Authors : Ajay Subramanya,Smitha Bangalore Naresh	       #
# 		subramanya{dot}a{at}husky{dot}neu{dot}edu		       #
# 		bangalorenaresh{dot}s{at}husky{dot}neu{dot}edu		       #
################################################################################

################################  PREREQUISITES ################################

* Update perf.txt with your setup specific details. Instructions are present in
  perf.txt

* We are using Apache Maven 3.3.9 for dependency management and build automation

* Please `chmod +x runPaths.sh` if it is not already executable

* Sample output and report can be found in `sampleOutput` directory

################################## RUN SCRIPT ##################################

* `./runPaths.sh` to run the script

* You will be prompted to key in a few values we may need, please enter then as
  required

* The script is interactive and will tell you the about the progress

* There is also an option to run the code in pseudo-distribution mode,
  these are the commands , note this run will only `cat` results to the console
  and will not generate any reports. Use this only for testing purpose.

  > Use prof.'s makefile to start hadoop
  > Make sure you do `hadoop fs -get path/to/your/files/* input`
  > Do `make run`

################################## CAVEATS #####################################

* Main class is App.java

* Project structure is as below,
.
├── README.txt
├── a5Report.Rmd
├── makefile
├── perf.txt
├── pom.xml
├── runPaths.sh
├── sampleOutput
│   ├── a5Report.pdf
│   ├── a5_paths_sa.csv
│   └── output_for_cloud
│       ├── _SUCCESS
│       └── part-r-00000
├── src
│   ├── main
│   │   └── java
│   └── test
│       └── java
└── uber-perf-0.0.1.jar


* We are using the prof.'s code to upload to S3 and kick off the
  cluster[With minor changes]

* The run script will take considerable amount of time since there are two
  runs

################################## DESIGN ######################################

* Most important design choice we had to make is to chose an optimal Data 
  structure for our reducer. We intially started off working on a day's data. 
  Here the obvious choice was a HashMap to hold the data for a day. But as we 
  scaled we realised that the HashMap alone wont be able to do the heavy lifting

* We made use of a TreeMap, which sorts the keys on its own, so we have a TreeMap 
 of ListMultiMap. This enabled us to have all the data for a year, month and day 
 in an atomic place. Our ListMultiMap is sorted on by the hour, so the first item 
 in the map would be the first hour in the day and so on. We do this to limit the 
 number of comparisions. For instance we have a flight arriving at 9am, all we 
 need to compare this flight with is the keys in the LinkedMultiMap that have 
 keys 9-15. This would be hugh win when we take on 337 files.

* Other than this, the design of most parts of the code is pretty much vanilla 
  hadoop. 

* We have implemented reducer side join on the airport id. Mapper1 will emit twice
  for each row, one serves as an arriving flight and the other as departing.
################################### THE END ####################################
