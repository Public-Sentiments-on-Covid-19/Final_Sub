import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class ProfilingReducer
    extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {

        // store values to be calculated
        double c_sum = 0.0, c_sd = 0.0, d_sum = 0.0, d_sd = 0.0;
        int size = 0;
        double num_case, num_death, d_mean, d_median, c_mean, c_median;

        // convert the iterable to a list
        ArrayList<Double> cases = new ArrayList<Double>();
        ArrayList<Double> deaths = new ArrayList<Double>();
        for (Text value: values){
            // get the values
            num_case = Double.parseDouble(value.toString().split(",")[0]);
            num_death = Double.parseDouble(value.toString().split(",")[1]);

            // add to the list
            cases.add(num_case);
            deaths.add(num_death);

            // calculate the sum and the size
            c_sum += num_case;
            d_sum += num_death;
            size++;
        }

        // calculate the median
        Collections.sort(cases);
        Collections.sort(deaths);
        if (size % 2 != 0){
            c_median = cases.get(size / 2);
            d_median = deaths.get(size / 2);
        }
        else{
            c_median = (cases.get((size - 1)/ 2) + cases.get(size / 2)) / 2;
            d_median = (deaths.get((size - 1)/ 2) + deaths.get(size / 2)) / 2;
        }

        // calculate the mean
        c_mean = c_sum / size;
        d_mean = d_sum / size;

        // calculate the standard deviation
        for (double score: cases){
            c_sd += Math.pow(score - c_mean, 2);
        }
        c_sd = Math.sqrt(c_sd/size);

        for (double score: deaths){
            d_sd += Math.pow(score - d_mean, 2);
        }
        d_sd = Math.sqrt(d_sd/size);

        String line = String.format("%f,%f,%f,%f,%f,%f", c_mean, c_median, c_sd, d_mean, d_median, d_sd);
        context.write(key, new Text(line));
    }
}
