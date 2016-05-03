package neu.mr.cs6240.a6_prediction;

/**
 * EMITS : nothing
 * WRITES : the model to HDFS
 *
 * the model is built here based on the features send in from
 * Prediction mapper , and the model is written to HDFS
 *
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import neu.mr.cs6240.common.Consts;
import neu.mr.cs6240.common.Utils;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class PredictionReducer1 extends Reducer<ShortWritable, FlightData, NullWritable, Text> {

	@Override
	protected void reduce(ShortWritable key, Iterable<FlightData> value,
			Reducer<ShortWritable, FlightData, NullWritable, Text>.Context context)
					throws IOException, InterruptedException {
		ArrayList<Attribute> attributes = Factors.setAttrs();

		Instances train95 = new Instances(Consts.TRAINING, attributes, 0);
		Instances train96 = new Instances(Consts.TRAINING + "1", attributes, 0);
		Instances train97 = new Instances(Consts.TRAINING + "2", attributes, 0);
		train95.setClassIndex(Consts.ARR_DELAY);
		train96.setClassIndex(Consts.ARR_DELAY);
		train97.setClassIndex(Consts.ARR_DELAY);

		for (FlightData row : value) {
			switch (row.getYear().get()) {
			case 1995:
				buildDataSet(train95, row);
				break;
			case 1996:
				buildDataSet(train96, row);
				break;
			case 1997:
				buildDataSet(train97, row);
				break;
			}
		}
		buildModel(new Instances[] { train95, train96, train97 }, key.get());
	}

	/**
	 *
	 * @param training
	 *            using which the model will be built
	 * @param key
	 *            used to append to the file we write, so we know the model is
	 *            for which quarter
	 */
	private void buildModel(Instances[] training, Short key) {

		RandomForest randomForest = new RandomForest();
		randomForest.setMaxDepth(4);
		randomForest.setNumTrees(15);
		randomForest.setNumFeatures(5);

		try {
			randomForest.buildClassifier(training[0]);
			Utils.writeFile(randomForest, Consts.MODEL + Consts.YEAR95 + key.toString());
			randomForest.buildClassifier(training[1]);
			Utils.writeFile(randomForest, Consts.MODEL + Consts.YEAR96 + key.toString());
			randomForest.buildClassifier(training[2]);
			Utils.writeFile(randomForest, Consts.MODEL + Consts.YEAR97 + key.toString());
		} catch (Exception e) {
			new Error("Error while building model / writing model to HDFS");
		}
	}

	/**
	 *
	 * @param training
	 *            meta-data about the columns
	 * @param row
	 *            the row which will be added to the dataset
	 */
	private void buildDataSet(Instances training, FlightData row) {
		Instance inst = new DenseInstance(Consts.NUM_FEATURES);

		inst.setDataset(training);
		inst.setValue(Consts.DAY_OF_MONTH, row.getDayOfMonth().get());
		inst.setValue(Consts.DAY_OF_WEEK, row.getDayOfWeek().get());
		inst.setValue(Consts.UNIQUE_CARRIER, row.getUniqueCarrier().toString());
		inst.setValue(Consts.ORIGIN, row.getOrigin().toString());
		inst.setValue(Consts.DEST, row.getDest().toString());
		inst.setValue(Consts.CRS_DEP_TIME, row.getCrsDepTime().get());
		inst.setValue(Consts.CRS_ARR_TIME, row.getCrsArrTime().get());
		inst.setValue(Consts.CRS_ELAPSED_TIME, row.getCrsElpTime().get());
		inst.setValue(Consts.ARR_DELAY, row.getArrDelay().toString());
		inst.setValue(Consts.DISTANCE, row.getDist().get());

		training.add(inst);
	}
}
