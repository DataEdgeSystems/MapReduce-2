single_mean:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=plain  -main=neu.mr.cs6240.a3_redux.App -input=input -output=.  -arguments="-prog=s -calculate=e" -results=mean_results.csv -name=seq_mean

single_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=plain  -main=neu.mr.cs6240.a3_redux.App -input=input -output=.  -arguments="-prog=s -calculate=d" -results=median_results.csv -name=seq_median

multi_mean:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=plain  -main=neu.mr.cs6240.a3_redux.App -input=input -output=.  -arguments="-prog=m -calculate=e" -results=mean_results.csv -name=multi_mean

multi_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=plain  -main=neu.mr.cs6240.a3_redux.App -input=input -output=.  -arguments="-prog=m -calculate=d" -results=median_results.csv -name=multi_median

pseudo_mean:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=local  -main=neu.mr.cs6240.a3_redux.App -input=input -output=output  -arguments="-prog=p -calculate=e" -results=mean_results.csv -name=pseudo_mean

pseudo_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=local  -main=neu.mr.cs6240.a3_redux.App -input=input -output=output  -arguments="-prog=p -calculate=d" -results=median_results.csv -name=pseudo_median

pseudo_fast_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar neu.perf.App -num=5 -jar=target/a3_redux_sa.jar  -kind=local  -main=neu.mr.cs6240.a3_redux.App -input=input -output=output  -arguments="-prog=p -calculate=f" -results=median_results.csv -name=pseudo_fast

cloud_mean:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar  neu.perf.App -num=5 -jar=s3n://a3-redux/code/a3_redux_sa.jar -kind=cloud -main=neu.mr.cs6240.a3_redux.App -input=s3n://a3-redux/input/ -output=s3n://a3-redux/output -arguments="-prog=p -calculate=d" -results=mean_results.csv -name=cloud_mean 

cloud_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar  neu.perf.App -num=5 -jar=s3n://a3-redux/code/a3_redux_sa.jar -kind=cloud -main=neu.mr.cs6240.a3_redux.App -input=s3n://a3-redux/input/ -output=s3n://a3-redux/output -arguments="-prog=p -calculate=d" -results=median_results.csv -name=cloud_median 

cloud_fast_median:
	mvn clean package
	-ZIP -d target/a3_redux_sa.jar META-INF/LICENSE
	java -cp uber-perf-0.0.1.jar  neu.perf.App -num=5 -jar=s3n://a3-redux/code/a3_redux_sa.jar -kind=cloud -main=neu.mr.cs6240.a3_redux.App -input=s3n://a3-redux/input/ -output=s3n://a3-redux/output -arguments="-prog=p -calculate=d" -results=median_results.csv -name=cloud_fast

graphs: RScript Graphs.R

single: single_mean single_median

multi: multi_mean multi_median

pseudo: pseudo_mean pseudo_mean pseudo_median

cloud: cloud_mean cloud_median cloud_fast_median

all: single multi pseudo cloud graphs 
