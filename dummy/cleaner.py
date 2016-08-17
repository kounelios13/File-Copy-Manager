from lineCounter import *
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