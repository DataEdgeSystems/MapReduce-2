run200:
	-hadoop fs -rm -r intermediate_output
	-hadoop fs -rm -r output 
	mvn clean package
	-ZIP -d target/a4_regression_sa.jar META-INF/LICENSE
	time hadoop jar target/a4_regression_sa.jar -input=input -output=output -time=200
	-hadoop fs -getmerge -nl output output.txt
	-cat output.txt
run1:
	-hadoop fs -rm -r intermediate_output
	-hadoop fs -rm -r output
	-hadoop fs -rm -r output1
	mvn clean package
	-ZIP -d target/a4_regression_sa.jar META-INF/LICENSE
	time hadoop jar target/a4_regression_sa.jar -input=input -output=output1 -time=1
	-hadoop fs -getmerge -nl output1 output1.txt
	-cat output1.txt

