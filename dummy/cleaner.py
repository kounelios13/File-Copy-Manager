from lineCounter import *
# for file in files:
# 	for line in open(file,'r').readlines():
# 		if isComment(line):
# 			print(line)
msg = ""
for f in files:
	for l in getLines(f):
		if not isComment(l):
			msg+=l 
print(msg)			