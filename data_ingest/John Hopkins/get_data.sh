# url of the data being parsed
URL="https://github.com/CSSEGISandData/COVID-19.git"
PWD=`pwd`

# data info
CSVNAME="data.csv"
FILENAME="confirmed_cases.csv"
TYPE="confirmed"
NETID="dec415"

# remove any existing files
rm data.csv
rm -rf data
hdfs dfs -rm -rf $TYPE

# get the data from the repository
git clone $URL
git -C COVID-19 pull


# copy the files over to local dir
mkdir data
LOC="${PWD}/COVID-19/csse_covid_19_data/csse_covid_19_daily_reports/*.csv"
for file in $LOC;
do
    f=${file##*/}
    dos2unix -n $file "${PWD}/data/${f}"
done

LOC="${PWD}/data/*.csv"
for file in $LOC;
do
    f=${file##*/}
    sed -e "1d;s/$/,${f%.csv}/" $file >> "${PWD}/data.csv"
    echo "" >> "${PWD}/data.csv"
done

# remove any remaining files
rm -rf COVID-19
rm -rf data

# add data and store inside HDFS
hdfs dfs -mkdir $TYPE 
hdfs dfs -mkdir $TYPE/input
hdfs dfs -put $CSVNAME $TYPE/input
