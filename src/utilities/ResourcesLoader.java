package utilities;
import java.io.*;
import java.util.*;

public class ResourcesLoader {

	/***
	 * Handle loading of app related files
	 * 
	 * 
	 * */
	private ObjectInputStream inputStream;
	private FileInputStream fileStream;

	private String seperator = File.separator+File.separator;
	private File uiTheme=PreferencesManager.settingsFile,
		listFile = new File("app"+seperator+"userlist.dat");
	public Settings getPreferences(){
		//Needs to be casted on PreferencesManager sinces Settings class is not visible
		return null;
	}
	public ProgramState getAppState(){
		/**
		 * 
		 * Returns the last saved state of the main program
		 * */
		ProgramState p=null;
		try {
			fileStream = new FileInputStream(listFile);
			inputStream = new ObjectInputStream(fileStream);
			p = (ProgramState)inputStream.readObject();
			inputStream.close();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
		return p;
	}
}
