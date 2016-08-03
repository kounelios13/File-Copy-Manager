# changelog
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


