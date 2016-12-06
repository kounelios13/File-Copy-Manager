# <center>changelog</center>
## v1.6.5.4
	* Apply custom look and feel with themes(For more see JTatto Demo application)
	* Current release fixes #19, #21
## v1.6.5.3
	* Apply new fonts and colors without saving them
	* Change look and feel of your program
	* New behaviour when there is only one file to copy and is a directory
	* Settings can be applied  even if preferences file can't be created
	* Added ```About``` page
	* Inform user about if any of the files they saved have been deleted
	* Current release closes #15 , #16 and #18
## v1.6.4.0
	*   `destinationPath` set to null by default
	*  `selectedFileIndex` set to -1 by default
	*  Current release fixes #8 issue(Can't delete app settings)
	* Refactored code
	* Added python script that removes empty lines and writes the amount of total lines for each java file in notes.txt
## v1.6.3.0
	* Each time you copy one or multiple files a new Thread is used to to prevent UI from freezing when you copy large files
	* If you save preferences without choosing bg anf fg colors a default color for each  will be saved.
## v1.6.1.2
	* Renamed folder "ex_jars" to "libs"
	* Now when you delete app settings the app will restart
	* Refactored code
## v1.6.1.1
	* Fixed messages displayed during errors
	* Changed method signature of getLabels() and getButtons() from ArrayList to simple array
	* If you save preferences for first time without selecting a desired font "Arial" font will be selected as default
	* renamed main class to FileCopyManager
## v1.6.1.0
	* Changed behaviour of program when trying to load a user saved list.
## v1.6.0.8
	* When opening Preferences each slider will have its corresponding value(from last saved preferences file)
## v1.6.0.7
	* Fixed Array Index Out Of Bounds exception when after every file is deleted you press delete selected file
	* Added option to export app preferences to a 'txt' file
	* Added option to delete app related files on user's demand
	* When user settings are loaded the saved font name will be sellected on the coresponding combo box
## v1.6.0.6
	* Added 'Exit' option to File menu
## v1.6.0.5
	* Now you can set colors for the buttons and labels available in the main GUI.
	* These colors will be saved and be available next time you open the program. 
## v1.6.0.0
	* Removed XProgressBar class and relevant constructor in FileHandler class
	* Now using File.seperator instead of "\\" or "//"
	* Removed DragFrame Class.
	* Still working on PreferencesManager class.
	* Planning to remove FontManager and assign all variables and methods to Settings class.
## v1.5.0.9
	* Change font size for buttons and labels
	* Setting bgcolor and fgColor partially working
## v1.5.0.7
	*Changed Look and Feel class to Nimbus
## v1.5.0.6
	* Copy multiple directories and files
	* When you press "Add files to copy" you can select directories and files
## v1.5.0.4
	* When deleting all files from list the combobox vanishes and then the success message appears
	* When drag n drop a folder the name "(Folder) " appears after its name in the combobox 	

## v1.5.0.3
	*file list will be visible only if there are any file for copy
	*fixed bug that caused IOException to the preferences file
	*Editing preferences is still in beta


