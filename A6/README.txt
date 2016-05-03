################################################################################
# README-A6 PREDICTION	       													
# Ajay Subramanya,Smitha Bangalore Naresh
# subramanya{dot}a{at}husky{dot}neu{dot}edu
# bangalorenaresh{dot}s{at}husky{dot}neu{dot}edu 
################################################################################

################################  PREREQUISITES ################################

* Update perf.txt with your setup specific details. Instructions are present in
  perf.txt

* We are using Apache Maven 3.3.9 for dependency management and build automation

* Please `chmod +x runPrediction.sh` if it is not already executable

* Sample output can be found in `sampleOutput` directory

* Report is named as a6Report.pdf

* You need to have java installed on the machine you are running the script, 
  since we are performing some local computations on the output data to 
  generate the confusion matrix.

* We need around 2GB of free RAM at the time of running this.

################################## RUN SCRIPT ##################################

* `./runPrediction.sh` to run the script

* You will be prompted to key in a few values we may need. Please enter then as
  required. Instructions are in the prompt. 

* The script is interactive and will tell you the about the progress

* Once running on cloud is complete the predictions for the test file will be
  download from your cloud output folder into a6_predicted.csv and then there is
  an a6_confusion_sa.jar which will compute confusion matrix and placed in 
  ConfusionMatrix.txt

* There is also an option to run the code in pseudo-distribution mode,
  these are the commands , note this run will only `cat` results to the console
  and will not generate any reports. Use this only for testing purpose.

  > Use prof.'s makefile to start hadoop
  > Make sure you do `hadoop fs -put path/to/your/files/* input`
  > Do `make run`

################################## CAVEATS #####################################

* Main class is App.java

* Project structure is as below,

.
├── 98validate.csv.gz
├── README.txt
├── a6-validation.tar.gz
├── a6Report.Rmd
├── a6Report.pdf
├── a6_confusion_sa.jar
├── data
│   ├── airline_data.txt
│   ├── arr_time_data.txt
│   ├── crs_elapsed_time.txt
│   ├── day_of_month.txt
│   ├── day_of_week.txt
│   ├── dep_time_data.txt
│   ├── destination_data.txt
│   ├── distance.txt
│   ├── months.txt
│   └── origin_data.txt
├── makefile
├── perf.txt
├── pom.xml
├── runPrediction.sh
├── sampleOutput
│   ├── ConfusionMatrix.txt
│   ├── ConfusionMatrixJ48.txt
│   ├── a6_predicted.csv.zip
│   └── outputJ48.csv.zip
├── src
│   ├── main
│   └── test
└── uber-perf-0.0.1.jar

* We have an other java project which computes the confusion matrix, we are the 
  Jar build using that project(a6-validation.tar.gz) in the run script. 
  Note : 98validate.csv.gz is already placed in our current project folder.

* We have done some analysis on the data , and the results of this is placed in 
  a directory called data. This is used to plot the graphs in report.

* We are using the prof.'s code to upload to S3 and kick off the
  cluster[With minor changes]

* Confusion matrix for full data as well as per month is computed. 

################################### THE END ####################################
