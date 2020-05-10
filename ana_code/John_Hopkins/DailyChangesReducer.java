import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DailyChangesReducer
    extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {

        // calculate the daily totals
        int cases = 0, deaths = 0;
        for (Text value: values){
            String[] line = value.toString().split(",");
            cases += Integer.parseInt(line[0]);
            deaths += Integer.parseInt(line[1]);
        }

        String counts = String.format("%s,%s", cases, deaths);
        context.write(key, new Text(counts));
    }
}
