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
        double sum = 0.0, sd = 0.0;
        int size = 0;
        double score, mean, median;

        // convert the iterable to a list
        ArrayList<Double> scores = new ArrayList<Double>();
        for (Text value: values){
            // get the values
            score = Double.parseDouble(value.toString());

            // add to the list
            scores.add(score);

            // calculate the sum and the size
            sum += score;
            size++;
        }

        // calculate the median
        Collections.sort(scores);
        if (size % 2 != 0){
            median = scores.get(size / 2);
        }
        else{
            median = (scores.get((size - 1)/ 2) + scores.get(size / 2)) / 2;
        }

        // calculate the mean
        mean = sum / size;

        // calculate the standard deviation
        for (double score: scores){
            sd += Math.pow(score - mean, 2);
        }
        sd = Math.sqrt(sd/size);

        String line = String.format("%f,%f,%f", mean, median, sd);
        context.write(key, new Text(line));
    }
}
