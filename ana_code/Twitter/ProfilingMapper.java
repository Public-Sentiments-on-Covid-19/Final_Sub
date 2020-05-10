import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ProfilingMapper
    extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        
        // store the line
        String[] line = value.toString().split(",");

        // extract values to be analyzed
        String score = line[0];
        String date = line[1];

        // reformat information
        String values = String.format("%s", score);

        context.write(new Text(date), new Text(values));
    }
}
