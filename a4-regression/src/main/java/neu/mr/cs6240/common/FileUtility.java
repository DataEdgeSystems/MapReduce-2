package neu.mr.cs6240.common;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

/**
 * @author ajay subramanya & smitha bangalore naresh
 * @date 02/05/2016
 * @info General file utility methods for reading GZip files and printing mCv
 */
public class FileUtility {
	/**
	 * @author Matthew Rathbone [
	 *         http://blog.matthewrathbone.com/2013/12/28/Reading-data-from-HDFS
	 *         -even-if-it-is-compressed.html ]
	 * @param location
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public static List<String> readLines(Path location, Configuration conf) throws Exception {
		FileSystem fileSystem = FileSystem.get(location.toUri(), conf);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		FileStatus[] items = fileSystem.listStatus(location);
		if (items == null)
			return new ArrayList<String>();
		List<String> results = new ArrayList<String>();
		for (FileStatus item : items) {

			// ignoring files like _SUCCESS
			if (item.getPath().getName().startsWith("_")) {
				continue;
			}

			CompressionCodec codec = factory.getCodec(item.getPath());
			InputStream stream = null;

			// check if we have a compression codec we need to use
			if (codec != null) {
				stream = codec.createInputStream(fileSystem.open(item.getPath()));
			} else {
				stream = fileSystem.open(item.getPath());
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(stream, writer, "UTF-8");
			String raw = writer.toString();
			for (String str : raw.split("\n")) {
				results.add(str);
			}
		}
		return results;
	}
}
