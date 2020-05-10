import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class DailyChangesMapper
    extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        
        // store the line
        String[] line = value.toString().split(",");
        String day = line[2];
        String loc = line[1];

        String val = String.format("%s,%s", line[3], line[4]);

        context.write(new Text(String.format("%s,%s", day, loc)), new Text(val));
    }
}
