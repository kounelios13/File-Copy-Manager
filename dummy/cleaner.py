import os

temp = [f+"\n" for f in os.listdir('.') if os.path.isfile(f)]
try:
	temp.index("feed.txt\n")
except ValueError:
	print("Creating feed.txt")
	file = open("feed.txt","w")
	file.writelines(files)
files = [file.strip() for file in open('feed.txt','r').readlines()]	
def emptyLineRemove(fileName):
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
def shrinkFiles():
	for file in files:
		emptyLineRemove(file)
shrinkFiles()
