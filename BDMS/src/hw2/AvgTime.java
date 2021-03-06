/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * @author pi
 *
 */
public class AvgTime {
	// This is the Mapper class
	// reference: //
	// http://hadoop.apache.org/docs/r2.6.0/api/org/apache/hadoop/mapreduce/Mapper.html
	public final static String SPLITOR = ",";

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {
		private DoubleWritable time = new DoubleWritable();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] s = value.toString().split("\\s+");
			if (s.length == 3) {
				try {
					double b = Double.parseDouble(s[2]);
					IntWritable one = new IntWritable(1);
					String s_d = s[0] + " " + s[1];
					// <source> <destination> <time>,1
					context.write(new Text(s_d), new Text(s[2] + SPLITOR + "1"));
				} catch (Exception e) {
				}
			}
		}
	}

	public static class AvgCombiner extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			double sum = 0.0;
			int cnt = 0;
			for (Text val : values) {
				String[] s = val.toString().split(SPLITOR);
				sum += Double.parseDouble(s[0]);
				cnt += Integer.parseInt(s[1]);
			}
			// <source> <destination> <sum_time>,cnt
			context.write(key, new Text(sum + SPLITOR + cnt));
		}
	}

	// This is the Reducer class
	// reference
	// http://hadoop.apache.org/docs/r2.6.0/api/org/apache/hadoop/mapreduce/Reducer.html
	public static class AvgReducer extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			double sum = 0.0;
			int cnt = 0;
			for (Text val : values) {
				String[] s = val.toString().split(SPLITOR);
				sum += Double.parseDouble(s[0]);
				cnt += Integer.parseInt(s[1]);
			}
			String avg = new DecimalFormat(".000").format(sum / cnt);
			// <source> <destination> cnt <avg_time>
			context.write(key, new Text(cnt + " " + avg));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err.println("Usage: avgtime <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "average time");

		job.setJarByClass(AvgTime.class);

		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(AvgCombiner.class);
		job.setReducerClass(AvgReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// add the input paths as given by command line
		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}

		// add the output path as given by the command line
		FileOutputFormat.setOutputPath(job, new Path(
				otherArgs[otherArgs.length - 1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
