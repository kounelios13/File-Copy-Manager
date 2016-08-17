import os
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
def removeEmptyLines(fileName):
	file = open(fileName,'r')
	lines = file.readlines()
	file.close()
	file = open(fileName,'w')
	for l in lines:
		if len(l.strip())>0:
			file.write(l)
	file.close()		
def removeComments(fileName):
	pass
shrinkFiles()
