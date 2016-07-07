# changelog

## v1.5.0.3
	*file list will be visible only if there are any file for copy
	*fixed bug that caused IOException to the preferences file
	*Editing preferences is still in beta

main method of GUI class consists of 3 states:

	1. Create the UI (GUI gui = new GUI();)
	2. Preload(Check if the user has saved any preferences) (gui.preload())
	2. Make the UI visible	(gui.setVisible(true);)

## v1.5.0.4
	* When deleting all files from list the combobox vanishes and then the success message appears
	* When drag n drop a folder the name "(Folder) " appears after its name in the combobox 	