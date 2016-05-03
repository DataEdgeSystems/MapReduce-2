package neu.mr.cs6240.a5_paths;

/**
 * Packs connections and missed in an Writable object
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class Result implements Writable {
	private IntWritable connections;
	private IntWritable missed;

	public Result() {
		this(new IntWritable(), new IntWritable());
	}

	public Result(IntWritable connections, IntWritable missed) {
		this.connections = connections;
		this.missed = missed;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		connections.write(out);
		missed.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		connections.readFields(in);
		missed.readFields(in);
	}

	public IntWritable getConnections() {
		return connections;
	}

	public void setConnections(IntWritable connections) {
		this.connections = connections;
	}

	public IntWritable getMissed() {
		return missed;
	}

	public void setMissed(IntWritable missed) {
		this.missed = missed;
	}
}
