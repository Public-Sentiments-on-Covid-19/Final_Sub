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
        String date = line[0].split(" ")[0];
        String comment = line[1];
        String title = line[2];

        // reformat information
        String values = String.format("%s,%s", comment, title);

        context.write(new Text(date), new Text(values));
    }
}
