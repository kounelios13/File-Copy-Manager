import os
null = None
true = True
false = False
defFile = open("cleaner.py",'r')
def checkfeed():
	temp = [f+"\n" for f in os.listdir('.') if os.path.isfile(f)]
	try:
		temp.index("feed.txt\n")
	except ValueError:
		print("Creating feed.txt")
		file = open("feed.txt","w")
		file.writelines(temp)
def shrinkFiles():
	checkfeed()
	files = [file.strip() for file in open('feed.txt','r').readlines()]	
	for file in files:
		removeEmptyLines(file)
def isComment(line):
	comments=["*","//","// ","*/","* ","/*","/**","#"," #"]
	ending=[" */","*/","**/","#"," #"]
	for i in comments:
		if line.startswith(i) :
			return True 
	for i in ending:
		if line.endswith(i):
			return True 	
	return False
def removeEmptyLines(fileName):
	file = open(fileName,'r')
	lines = file.readlines()
	file.close()
	file = open(fileName,'w')
	file.writelines([l for l in lines if len(l.strip()) > 0])		
	file.close()		
def removeComments(fileName):
	lines = []
	output = open("cleaner.py","r")
					
	try:
		lines = open(fileName,'r').readlines()
	except FileNotFoundError:
		print("{} does not exist".format(fileName))
		output.close()
		return
	output = open(fileName,'w')	
	output.writelines([line for line in lines if len(line.strip())> 0 and not isComment(line.strip())])

def getSourceCodeLines(file):
	lines = [l for l in open(file,'r').readlines() if not isComment(l.strip()) and len(l.strip())> 0]
	chars = 0
	for l in lines:
		chars+=len(l.strip())
	return [len(lines),chars]	

def evalFile(name):
	try:
		file = open(name,'r')
	except FileNotFoundError:
		return False
	return True		
def showMenu():
	os.system('clear')
	os.system('cls')
	info = "What you want to do?\n"
	info+=" 1:Remove empty lines from file\n"
	info+=" 2:Remove comments from file\n"
	info+=" 3:Show number of source code lines(comments do not count)\n"
	print(info)
def showAsApp():
	while True:
		showMenu()
		fn = int(input("Choose a number:"))	
		file = input("Enter path of file(rel or absolute) followed by its name.\nE.g.: ../demo.txt\n")	
		if evalFile(file):
			if fn is 1:
				removeEmptyLines(file)
			elif fn is 2:
				removeComments(file)	
			elif fn is 3:
				print("There are {} of code in {}".format(getSourceCodeLines(file),file))	
		else:
			print("File does not exist")	
		go = input("Press Y to continue or anything else to exit:")
		if not go =='Y':
			break 	