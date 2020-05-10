import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CleanMapper
    extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        
        // store the line 
        String line = value.toString();

        // find date
        Pattern loc = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher match = loc.matcher(line);
        if (match.find()){
            String date = match.group();
            String[] vals = line.split(",");

            String comment = vals[1];
            String title = vals[2];

            String newLine = String.format("%s,%s,%s", date, comment, title);

            context.write(new Text(newLine), new Text("ok"));
        }
    }
}
