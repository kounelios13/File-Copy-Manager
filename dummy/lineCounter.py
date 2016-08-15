from sys import argv
# Improve comment check
# check if a line ends with a comment terminating char
def isComment(line):
	comments=["*","//","// ","*/","* ","/*","/**","#"," #"]
	ending=[" */","*/","**/","#"," #"]
	for i in comments:
		if line.startswith(i):
			return True 
	for i in ending:
		if line.endswith(i):
			return True 	
	return False 		
files=[file.rstrip() for file in open('feed.txt' if len(argv) < 2 else argv[1],'r').readlines()]
lines = 0
chars = 0
for f in files:
	temp_lines = open(f,'r').readlines()
	for t_line in temp_lines:
		t_line = t_line.strip()
		if len(t_line) > 0 and not isComment(t_line):
			lines+=1
			chars+=len(t_line.strip())	
print("Total source code lines {}. Number of source code chars {}".format(lines,chars))			