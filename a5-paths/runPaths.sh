#!/usr/bin/env bash

echo "Please modify the perf.txt to your setup before starting ..."
echo
echo "Enter output location on s3 (FORMAT s3://name_of_your_bucket/name_ouput_folder)"
echo "NOTE : this folder should not exist and same as that given in perf.txt"
read OUTPUT_LOCATION
echo

echo "Creating a5_paths_sa.jar JAR"

mvn clean package
ZIP -d target/a5_paths_sa.jar META-INF/LICENSE || true

java -cp uber-perf-0.0.1.jar  neu.perf.App -num=1 -jar=./target/a5_paths_sa.jar -kind=cloud -main=neu.mr.cs6240.a5_paths.App -results=results.csv -name=cloud_a5

#fetching data from s3 to local
echo "ran on cluster ... "

rm -rf output || true
rm -rf a5_paths_sa.csv || true
mkdir output_for_cloud
aws s3 sync ${OUTPUT_LOCATION} ./output_for_cloud
cat output_for_cloud/p* > a5_paths_sa.csv

echo "generating reports ... a5Report.pdf"
Rscript -e "rmarkdown::render('a5Report.Rmd')" `pwd`
