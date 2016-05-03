run:	
	-rm -rf output
	-hadoop fs -rm -r output 
	-hadoop fs -rm -r temp_file
	mvn clean package
	-ZIP -d target/a5_paths_sa.jar META-INF/LICENSE
	time hadoop jar target/a5_paths_sa.jar -input=input -output=output
	-hadoop fs -getmerge -nl output output.txt
	cat output.txt

toy:
	-rm -rf toyoutput
	-hadoop fs -rm -r toyoutput
	-hadoop fs -rm -r temp_file
	mvn clean package
	-ZIP -d target/a5_paths_sa.jar META-INF/LICENSE
	time hadoop jar target/a5_paths_sa.jar -input=toy -output=toyoutput
	-hadoop fs -getmerge -nl toyoutput outputtoy.txt
	cat outputtoy.txt
