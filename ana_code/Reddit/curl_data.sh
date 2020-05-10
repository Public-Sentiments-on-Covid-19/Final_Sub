#!/bin/bash

month=1
day=20
dir="reddit_Data"

[ ! -d "$dir" ] && mkdir -p "$dir"
cd reddit_Data


while (( $month <= 3 ))
do
  curl https://raw.githubusercontent.com/Public-Sentiments-on-Covid-19/r-coronavirus_ingest/master/Data/$month-$day.csv --output $month-$day.csv
  day=$(( day+1 ))
  if [ $day -eq 32 ]
  then
    month=$(( month+1 ))
    day=1
  fi
done

rm 2-30.csv
rm 2-31.csv
