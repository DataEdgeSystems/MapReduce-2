package neu.mr.cs6240.a6_prediction;

/**
 * EMITS : <FL_NUM>_<FL_DATE>_<CRS_DEP_TIME>, logical
 * 
 * <FL_NUM>_<FL_DATE>_<CRS_DEP_TIME> : Uniquely identifies a flight
 * logical : TRUE if the flight will be late
 * 
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import neu.mr.cs6240.common.Consts;
import neu.mr.cs6240.common.Utils;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class EvaluateReducer2 extends Reducer<ShortWritable, TestData, Text, Text> {

	@Override
	protected void reduce(ShortWritable key, Iterable<TestData> value,
			Reducer<ShortWritable, TestData, Text, Text>.Context context) throws IOException, InterruptedException {
		ArrayList<Attribute> attributes = Factors.setAttrs();

		Instances test = new Instances(Consts.TEST, attributes, 0);
		test.setClassIndex(Consts.ARR_DELAY);

		try {
			Classifier cls1 = Utils.readFile(Consts.MODEL + Consts.YEAR95 + key.toString());
			Classifier cls2 = Utils.readFile(Consts.MODEL + Consts.YEAR96 + key.toString());
			Classifier cls3 = Utils.readFile(Consts.MODEL + Consts.YEAR97 + key.toString());

			for (TestData row : value) {
				classify(test, row, new Classifier[] { cls1, cls2, cls3 }, context, key.get());
			}
		} catch (Exception e1) {
			new Error("error occoured while reading model from file/classifying");
		}

	}

	/**
	 * 
	 * @param test
	 *            using which the model will be built
	 * @param row
	 *            a row from test data
	 * @param cls
	 *            classifiers, one for each year using which the test row will
	 *            be classified
	 * @param context
	 *            to write the output
	 * @param month
	 *            the month of the row
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void classify(Instances test, TestData row, Classifier[] cls, Context context, int month)
			throws IOException, ClassNotFoundException, Exception {
		Instance inst = buildInstance(test, row);
		double sum = 0.0;

		for (int i = 0; i < cls.length; i++) {
			sum += cls[i].classifyInstance(inst);
		}
		context.write(uniqueIdentifier(row), new Text((sum == 0.0 || sum == 1.0) ? "FALSE" : "TRUE"));

	}

	/**
	 * 
	 * @param row
	 *            a row from test data
	 * @return the unique identifier for the row (<FL_NUM>_<FL_DATE>_
	 *         <CRS_DEP_TIME>)
	 */
	private Text uniqueIdentifier(TestData row) {
		return new Text(row.getfNum() + "_" + row.getDate() + "_"
				+ StringUtils.leftPad(row.getCrsDepTime().toString(), Consts.TIME_LEN, '0') + ",");
	}

	/**
	 *
	 * @param test
	 *            the skeleton to which values need to be added
	 * @param row
	 *            the row which will be added to the dataset
	 */
	private Instance buildInstance(Instances test, TestData row) {

		Instance inst = new DenseInstance(Consts.NUM_FEATURES);
		Integer crsDep = Utils.toHr(row.getCrsDepTime().toString());

		inst.setDataset(test);

		inst.setValue(Consts.DAY_OF_MONTH, row.getDayOfMonth().get());
		inst.setValue(Consts.DAY_OF_WEEK, row.getDayOfWeek().get());
		inst.setValue(Consts.UNIQUE_CARRIER, row.getUniqueCarrier().toString());
		inst.setValue(Consts.ORIGIN, row.getOrigin().toString());
		inst.setValue(Consts.DEST, row.getDest().toString());
		inst.setValue(Consts.CRS_DEP_TIME, crsDep);
		inst.setValue(Consts.CRS_ARR_TIME, row.getCrsArrTime().get());
		inst.setValue(Consts.CRS_ELAPSED_TIME, row.getCrsElpTime().get());
		inst.setValue(Consts.DISTANCE, row.getDist().get());

		return inst;
	}
}
