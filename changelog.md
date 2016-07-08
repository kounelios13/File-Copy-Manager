# changelog

# Write changelog for new version in the morning	

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

