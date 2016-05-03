#!/usr/bin/env bash

echo "Please modify the perf.txt to your setup before starting ..."
echo
echo "Enter output location on s3 (FORMAT s3://name_of_your_bucket/name_ouput_folder)"
echo "NOTE : this folder should not exist and same as that given in perf.txt"
read OUTPUT_LOCATION
echo

echo "Enter test location on s3 (FORMAT s3://name_of_your_bucket/a6test)"
read TEST
echo

echo "Creating a6_prediction_sa.jar JAR"

mvn clean package
ZIP -d target/a6_prediction_sa.jar META-INF/LICENSE || true

echo "uploading jars and running on cluster ..."

java -cp uber-perf-0.0.1.jar  neu.perf.App -num=1 -jar=./target/a6_prediction_sa.jar -kind=cloud -results=results.csv -arguments="-test=${TEST}" -name=cloud_a6

#fetching data from s3 to local
echo "ran on cluster ... "

rm -rf predicted_op || true
rm -rf a6_predicted.csv || true
mkdir predicted_op
aws s3 sync ${OUTPUT_LOCATION} ./predicted_op

#computing confusion matrix on local system
echo "computing confusion matrix..."
cat predicted_op/p* > a6_predicted.csv
java -Xmx2048m -jar a6_confusion_sa.jar 98validate.csv.gz a6_predicted.csv

echo "generating reports ... a6Report.pdf"
Rscript -e "rmarkdown::render('a6Report.Rmd')" `pwd`
