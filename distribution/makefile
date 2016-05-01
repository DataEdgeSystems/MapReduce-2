runsmall:
	-hadoop fs -rm -r output
	-rm -rf outputsmall.txt
	mvn clean package
	ZIP -d target/a2_distribution.jar META-INF/LICENSE
	hadoop jar target/a2_distribution.jar inputsmall output
	hadoop fs -getmerge -nl output outputsmall.txt
runbig:
	-hadoop fs -rm -r output
	-rm -rf outputbig.txt
	mvn clean package
	ZIP -d target/a2_distribution.jar META-INF/LICENSE
	hadoop jar target/a2_distribution.jar input output
	hadoop fs -getmerge -nl output outputbig.txt
