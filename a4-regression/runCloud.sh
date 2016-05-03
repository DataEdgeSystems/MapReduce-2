#!/usr/bin/env bash

echo "Please modify the perf.txt to your setup before starting…"
echo
echo "Enter output location on s3 (FORMAT s3://name_of_your_bucket/name_ouput_folder)"
echo "NOTE : this folder should not exist and same as that given in perf.txt"
read OUTPUT_LOCATION
echo

echo "Creating a4_regression_sa.jar JAR"

mvn clean package
ZIP -d target/a4_regression_sa.jar META-INF/LICENSE || true

echo "Uploading to jar to S3... Creating cluster ...."

echo "running for N=200 on cluster ... might take a while ..."

java -cp uber-perf-0.0.1.jar  neu.perf.App -num=1 -jar=./target/a4_regression_sa.jar -kind=cloud -main=neu.mr.cs6240.a4_regression.App -arguments="-time=200" -results=results.csv -name=cloud_a4

#fetching data from s3 to local
echo "ran on cluster ... processing output for N=200..."

rm -rf output_for_cloud_200 || true
rm -rf a4_regression_sa_200.csv || true
mkdir output_for_cloud_200
aws s3 sync ${OUTPUT_LOCATION} ./output_for_cloud_200
cat output_for_cloud_200/p* > a4_regression_sa_200.csv

echo "running for N=1 on cluster ... might take a while ..."

java -cp uber-perf-0.0.1.jar  neu.perf.App -num=1 -jar=./target/a4_regression_sa.jar -kind=cloud -main=neu.mr.cs6240.a4_regression.App -arguments="-time=1" -results=results.csv -name=cloud_a4

#fetching data from s3 to local
echo "ran on cluster ... processing output for N=1..."
rm -rf output_for_cloud_1 || true
rm -rf a4_regression_sa_1.csv || true
mkdir output_for_cloud_1
aws s3 sync ${OUTPUT_LOCATION} ./output_for_cloud_1
cat output_for_cloud_1/p* > a4_regression_sa_1.csv


echo "generating reports ... Graphsa4.pdf"
RScript Graphsa4.R
Rscript -e "rmarkdown::render('Graphsa4.Rmd')" `pwd`
