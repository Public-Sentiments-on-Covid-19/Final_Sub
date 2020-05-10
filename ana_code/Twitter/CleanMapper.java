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
        String[] line = value.toString().split(",");

        // extract columns being used
        String score = line[0];
        String date = line[line.length - 1];

        // make sure that the score and date is correct
        Pattern p = Pattern.compile("-?[0-9]+\\.[0-9]+");
        Matcher m = p.matcher(score);
        Pattern loc = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        Matcher mat = loc.matcher(date);

        // if date and score is correct format, then continue
        if (m.find() && mat.find()){
            String val = String.format("%s,%s", m.group(), mat.group());
            context.write(new Text(val), new Text(""));
        }
    }
}
