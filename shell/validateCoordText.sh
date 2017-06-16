#!/bin/sh
RS_HOME=/home/gaurav/runescape/local
#inspect for text in image area
inputText=$1
x_coord=$2
y_coord=$3

rm ${RS_HOME}/scrot_images/outputfile_1.png ${RS_HOME}/out.txt

scrot_extended $x_coord $y_coord 160 40

tesseract ${RS_HOME}/scrot_images/outputfile_1.png out

searchRes=`grep ${inputText} out.txt | wc -l`

return ${searchRes}

#imageText=`cat out.txt`
#echo $imageText
#if [ "$inputText" = "$imageText" ]
#then
#	return 200
#else 
#	return 401
#fi
