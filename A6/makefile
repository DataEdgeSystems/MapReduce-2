run:
	-rm -f output.csv
	-hadoop fs -rm -r output
	-hadoop fs -rm -r temp_file
	mvn clean package
	ZIP -d target/a6_prediction_sa.jar META-INF/LICENSE
	hadoop jar target/a6_prediction_sa.jar -train=input -output=output -test=test
	-hadoop fs -getmerge -nl output output.csv
	java -Xmx4096m -jar a6_confusion_sa.jar 98validate.csv.gz output.csv
	#cat output.txt

run_test:
	-rm -f test.csv
	-hadoop fs -rm -r output_test
	mvn clean package
	ZIP -d target/a6_prediction_sa.jar META-INF/LICENSE
	hadoop jar target/a6_prediction_sa.jar -train=input -output=output -test=test
	-hadoop fs -getmerge -nl output_test test.csv
	#cat output.txt
cloud: 
	mvn clean package 
	ZIP -d target/a6_prediction_sa.jar META-INF/LICENSE
	aws s3 cp target/a6_prediction_sa.jar s3://a6-prediction1/
	aws s3 rm s3://a6-prediction1/output6 --recursive
	aws emr create-cluster --applications Name=Hadoop --ec2-attributes '{"InstanceProfile":"EMR_EC2_DefaultRole","SubnetId":"subnet-a4b6aec1","EmrManagedSlaveSecurityGroup":"sg-e252af85","EmrManagedMasterSecurityGroup":"sg-e352af84"}' --service-role EMR_DefaultRole --enable-debugging --release-label emr-4.3.0 --log-uri 's3n://a6-prediction1/' --steps '[{"Args":["-train=s3://a6-prediction1/input","-test=s3://a6-prediction1/test","-output=s3://a6-prediction1/output6","-validate=s3://a6-prediction1/98validate.csv.gz"],"Type":"CUSTOM_JAR","ActionOnFailure":"CONTINUE","Jar":"s3://a6-prediction1/a6_prediction_sa.jar","Properties":"","Name":"Custom JAR"}]' --name 'a6 cluster' --instance-groups '[{"InstanceCount":5,"InstanceGroupType":"CORE","InstanceType":"m3.xlarge","Name":"Core Instance Group"},{"InstanceCount":1,"InstanceGroupType":"MASTER","InstanceType":"m3.xlarge","Name":"Master Instance Group"}]' --configurations '[{"Classification":"spark","Properties":{"maximizeResourceAllocation":"true"},"Configurations":[]}]' --auto-terminate --region us-west-2
