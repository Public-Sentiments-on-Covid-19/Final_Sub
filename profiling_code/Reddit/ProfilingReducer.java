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
        double c_sum = 0.0, c_sd = 0.0, t_sum = 0.0, t_sd = 0.0;
        int size = 0;
        double num_comment, num_title, t_mean, t_median, t_mean, t_median;

        // convert the iterable to a list
        ArrayList<Double> comment = new ArrayList<Double>();
        ArrayList<Double> title = new ArrayList<Double>();
        for (Text value: values){
            // get the values
            num_comment = Double.parseDouble(value.toString().split(",")[0]);
            num_title = Double.parseDouble(value.toString().split(",")[1]);

            // add to the list
            comment.add(num_comment);
            title.add(num_title);

            // calculate the sum and the size
            c_sum += num_comment;
            t_sum += num_title;
            size++;
        }

        // calculate the median
        Collections.sort(comment);
        Collections.sort(title);
        if (size % 2 != 0){
            c_median = comment.get(size / 2);
            t_median = title.get(size / 2);
        }
        else{
            c_median = (comment.get((size - 1)/ 2) + comment.get(size / 2)) / 2;
            t_median = (title.get((size - 1)/ 2) + title.get(size / 2)) / 2;
        }

        // calculate the mean
        c_mean = c_sum / size;
        t_mean = t_sum / size;

        // calculate the standard deviation
        for (double score: comment){
            c_sd += Math.pow(score - c_mean, 2);
        }
        c_sd = Math.sqrt(c_sd/size);

        for (double score: title){
            t_sd += Math.pow(score - t_mean, 2);
        }
        t_sd = Math.sqrt(t_sd/size);

        String line = String.format("%f,%f,%f,%f,%f,%f", c_mean, c_median, c_sd, t_mean, t_median, t_sd);
        context.write(key, new Text(line));
    }
}
