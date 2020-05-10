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
        
        // store the line and replace variations
        String line = value.toString();
        line = line.replace("\"Korea, South\"", "South Korea");
        line = line.replace("Mainland China", "China");
        line = line.replace("*", "");

        // find location
        Pattern loc = Pattern.compile("[A-Za-z-* ]*,[A-Za-z-* ]*,(\\d{4}|\\d{1})");
        Matcher match = loc.matcher(line);
        if (match.find()){
            String location = match.group();
            String[] vals = location.split(",");
            location = String.format("%s,%s", vals[0], vals[1]);

            // compile regular expression to get the numbers 
            String pattern = ",[0-9]+,";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(line);
            if (m.find()){
                String text = m.group();

                // store the values
                int pos = line.indexOf(text) + text.indexOf(",");
                String[] values = line.substring(pos + 1).split(",");

                // fill in any blank lines
                for(int i = 0; i < 3; i++)
                    if(values[i].length() == 0)
                        values[i] = "0";

                String numbers = String.format("%s,%s,%s", values[0], values[1], values[2]);
                String date = values[values.length - 1];
                location = String.format("%s,%s,%s", location, date, numbers);

                context.write(new Text(location), new Text("ok"));
            }
        }
    }
}
