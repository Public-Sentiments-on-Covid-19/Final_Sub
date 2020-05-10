import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;

public class Profiling {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: Profiling <input path> <output path> <filename>");
            System.exit(-1);
        }

        final Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");
        conf.set("mapreduce.output.basename", args[2]);
        Job job = Job.getInstance(conf);

        job.setJarByClass(Profiling.class);
        job.setJobName("Profiling");

        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(ProfilingMapper.class);
        job.setReducerClass(ProfilingReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
