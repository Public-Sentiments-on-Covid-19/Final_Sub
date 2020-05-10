# url of the data being parsed
CSVNAME="data.csv"
FILENAME="confirmed_cases.csv"
TYPE="confirmed"
NETID="dec415"

# clean up any remaining files that exist
rm *.class
rm *.jar
rm $FILENAME
hdfs dfs -rm -r -f $TYPE/cleaned
hdfs dfs -rm $FILENAME

# get the data and store inside HDFS
hdfs dfs -mkdir $TYPE/cleaned

# compile the code and run it on the hadoop cluster
javac -classpath `yarn classpath` -d . CleanMapper.java
javac -classpath `yarn classpath` -d . CleanReducer.java
javac -classpath `yarn classpath`:. -d . Clean.java
jar -cvf Clean.jar *.class
hadoop jar Clean.jar Clean /user/$NETID/$TYPE/input/$CSVNAME /user/$NETID/$TYPE/output $TYPE

# rename the output
hdfs dfs -mv $TYPE/output/$TYPE-r-00000 $TYPE/cleaned/$FILENAME
hdfs dfs -rm -r -f $TYPE/output

# compile the code to count daily increases
javac -classpath `yarn classpath` -d . DailyChangesMapper.java
javac -classpath `yarn classpath` -d . DailyChangesReducer.java
javac -classpath `yarn classpath`:. -d . DailyChanges.java
jar -cvf DailyChanges.jar *.class
hadoop jar DailyChanges.jar DailyChanges /user/$NETID/$TYPE/cleaned/$FILENAME /user/$NETID/$TYPE/output $TYPE

# store the output
hdfs dfs -mv $TYPE/output/$TYPE-r-00000 $FILENAME 
rm *.class *.jar
