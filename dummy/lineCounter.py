num_of_lines = 0
# files = [g+"FileCopyManager.java",g+"StatusFrame.java",u+"Controller.java",u+"PreferencesManager.java",u+"ProgramState.java",u+"UIPreferences.java",u+"ResourceLoader.java",u+"FileHandler.java"]
files=[file.rstrip() for file in open('feed.txt','r').readlines()]
for e in files:
	try:
		file = open(e,'r')
		num_of_lines+=len(file.readlines())
	except FileNotFoundError:
		print("File {} not found".format(e))

print("{} lines of code found in {} files ".format(num_of_lines,len(files)))	
total_chars=0
for line in files:
	#open each file
	cur_file = open(line.rstrip(),'r').readlines()
	for code in cur_file:
		#for each line of code in each file count the total characters(not including new line character)
		total_chars+=len(code.rstrip())
	
if total_chars:
	print("There are {} characters in these {} files".format(total_chars,len(files)))	
