from sys import argv
# Find number of lines that are source code
# and not blank lines	
num_of_lines = 0
def isSourceCode(line):	
	def isComment(line):
		def s(string,p):
			return string.startswith(p)
		return s(line,"//") or s(line,"/*") or s(line,"*") or s(line,"///") or line.endswith("*") or line.endswith(" */")
	return not isComment(line) and len(line) > 0
files=[file.rstrip() for file in open('feed.txt' if len(argv) < 2 else argv[1],'r').readlines()]
total_chars=0
for e in files:
	try:
		file = open(e,'r')
		code_lines=[code.rstrip() for code in file.readlines()];
		file.close()
		for line_of_code in code_lines:
			#if the current line is empty decrease the number of total lines
			line = line_of_code
			if isSourceCode(line):
				total_chars+=len(line)
				num_of_lines+=1
	except FileNotFoundError:
		print("File {} not found".format(e))
message = "{} lines of code found in {} files ".format(num_of_lines,len(files))
if total_chars:
	message += "and {} total characters".format(total_chars)
print(message)
print("Files read {}" .format(files))

#if you want to supply an other file to load file names 
# do it like this :
#python lineCounter.py foofile.txt


