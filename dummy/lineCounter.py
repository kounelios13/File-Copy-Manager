import subprocess
num_of_lines = 0
files=[file.rstrip() for file in open('feed.txt','r').readlines()]
total_chars=0
for e in files:
	try:
		file = open(e,'r')
		code_lines=[code.rstrip() for code in file.readlines()];
		num_of_lines+=len(code_lines)
		for line_of_code in code_lines:
			total_chars+=len(line_of_code)
	except FileNotFoundError:
		print("File {} not found".format(e))
message = "{} lines of code found in {} files ".format(num_of_lines,len(files))
if total_chars:
	message += "and {} total characters".format(total_chars)
print(message)
print("Files read {}" .format(files))
