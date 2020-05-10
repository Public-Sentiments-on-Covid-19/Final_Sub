# url of the data being parsed
CSVNAME="data.csv"
FILENAME="confirmed_cases.csv"
TYPE="confirmed"
NETID="dec415"

# clean up any remaining files that exist
rm *.class
rm *.jar
hdfs dfs -rm $FILENAME

# compile the code to count daily increases
javac -classpath `yarn classpath` -d . DailyChangesMapper.java
javac -classpath `yarn classpath` -d . DailyChangesReducer.java
javac -classpath `yarn classpath`:. -d . DailyChanges.java
jar -cvf DailyChanges.jar *.class
hadoop jar DailyChanges.jar DailyChanges /user/$NETID/$TYPE/cleaned/$FILENAME /user/$NETID/$TYPE/output $TYPE

# store the output
hdfs dfs -mv $TYPE/output/$TYPE-r-00000 $FILENAME 
rm *.class *.jar
