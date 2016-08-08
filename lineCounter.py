num_of_lines = 0
# files = [g+"FileCopyManager.java",g+"StatusFrame.java",u+"Controller.java",u+"PreferencesManager.java",u+"ProgramState.java",u+"UIPreferences.java",u+"ResourceLoader.java",u+"FileHandler.java"]
files=open('feed.txt','r').readlines()
for e in files:
	#strip new line character
	e=e.rstrip()
	file = open(e,'r')
	num_of_lines+=len(file.readlines())
print("{} lines of code found in {} files ".format(num_of_lines,len(files)))	
