from cleaner import *
# Improve comment check
# check if a line ends with a comment terminating char
exportFile = open("../notes.txt",'w')
checkfeed()
files =[i.strip() for i in open('feed.txt','r').readlines()]
info = ""
chars = 0
for file in files:
	source=getSourceCodeLines(file)
	chars+=source[1]
	info+= "\t{} contains {} lines of source code \n".format(file,source[0])
exportFile.write(info)
exportFile.write("\tTotal characters {}".format(chars))	
removeComments("./lineCounter.py")