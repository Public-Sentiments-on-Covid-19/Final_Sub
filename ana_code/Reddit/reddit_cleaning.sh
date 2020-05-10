# files and variables to be filled
CSVNAME="reddit_data.csv"
FILENAME="reddit.csv"
TYPE="reddit"
NETID="dec415"

# clean up any remaining files that exist
rm *.class
rm *.jar
rm $FILENAME
hdfs dfs -rm -r -f $TYPE

# get the data and store inside HDFS
hdfs dfs -mkdir $TYPE 
hdfs dfs -mkdir $TYPE/input
hdfs dfs -mkdir $TYPE/cleaned
hdfs dfs -put $CSVNAME $TYPE/input
hdfs dfs -ls $TYPE/input

# compile the code and run it on the hadoop cluster
javac -classpath `yarn classpath` -d . CleanMapper.java
javac -classpath `yarn classpath` -d . CleanReducer.java
javac -classpath `yarn classpath`:. -d . Clean.java
jar -cvf Clean.jar *.class
hadoop jar Clean.jar Clean /user/$NETID/$TYPE/input/$CSVNAME /user/$NETID/$TYPE/output $TYPE

# rename the output
hdfs dfs -mv $TYPE/output/$TYPE-r-00000 $TYPE/cleaned/$FILENAME
hdfs dfs -rm -r -f $TYPE/output

# add header to CSV
hdfs dfs -get $TYPE/cleaned/$FILENAME
sed -i '1 i\Publish Date, Comment_VS, Title_VS' $FILENAME
hdfs dfs -put $FILENAME input/$FILENAME
