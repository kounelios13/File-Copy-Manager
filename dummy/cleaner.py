from lineCounter import *
def emptyLineRemove(fileName):
	file  = open(fileName,'w')
	lines = file.readlines()
	for l in line:
		l = l.strip()
		if len(l)>0:
			file.write(l+"\n")
	file.close()		
def removeComments(fileName):
	pass
def showMenu():
	while True:
		print("What you want to do? Select a number")					
		print("1:Remove empty lines")
		print("2:Remove comments")
		ans = int(input("Type a number:"))
		fileName = input("Type a filename")
		if ans is 1:
			emptyLineRemove(fileName)
		elif ans is 2:
			removeComments(fileName)
		go = input("Press Y to continue or N to exit")
		if go is not Y:
			break 	