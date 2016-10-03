from cleaner import *
# Improve comment check
# check if a line ends with a comment terminating char
exportFile = open("../notes.txt",'w')
checkfeed()
files =[i.strip() for i in open('feed.txt','r').readlines()]
info  = ""
chars = 0
sum	  = 0
for file in files:
	source=getSourceCodeLines(file)
	#found a file that contains no lines or FileNotFound exception was thrown
	if source[0] < 1:
		continue
	chars+=source[1]
	sum+=source[0]
	info+= "\t{} contains {} lines of source code \n".format(file,source[0])
exportFile.write(info)
exportFile.write("\tTotal characters {}. Total lines {}".format(chars,sum))	
print(info+"Total lines {}".format(sum))
for file in files:
	removeEmptyLines(file)