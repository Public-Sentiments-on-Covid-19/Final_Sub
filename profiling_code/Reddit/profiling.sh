# name of files and variables
CSVNAME="profiling.csv"
FILENAME="reddit.csv"
TYPE="profiling"
NETID="dec415"

# clean up any remaining files that exist
rm Profiling.class ProfilingMapper.class ProfilingReducer.class
rm Profiling.jar
rm $CSVNAME
hdfs dfs -rm -r -f $TYPE

# get the data and store inside HDFS
hdfs dfs -mkdir $TYPE 

# compile the code and run it on the hadoop cluster
javac -classpath `yarn classpath` -d . ProfilingMapper.java
javac -classpath `yarn classpath` -d . ProfilingReducer.java
javac -classpath `yarn classpath`:. -d . Profiling.java
jar -cvf Profiling.jar *.class
hadoop jar Profiling.jar Profiling /user/$NETID/confirmed/cleaned/$FILENAME /user/$NETID/$TYPE/output $TYPE

# rename the output
hdfs dfs -mv $TYPE/output/$TYPE-r-00000 $TYPE/$FILENAME
hdfs dfs -get $TYPE/$FILENAME $CSVNAME
sed -i '1 i\Date,Comment_VS Average, Comment_VS Median, Comment_VS_SD, Title_VS Average, Title_VS Median, Title_VS_SD' $CSVNAME

# delete any remaining files
rm *.class
rm *.jar
