# changelog
# todo
* rework preferences manager to remove duplicates and unused code
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

main method of GUI class consists of 3 states:

	1. Create the UI (GUI gui = new GUI();)
	2. Preload(Check if the user has saved any preferences) (gui.preload())
	2. Make the UI visible	(gui.setVisible(true);)

