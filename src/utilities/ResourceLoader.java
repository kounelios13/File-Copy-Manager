package utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import messages.Message;
@SuppressWarnings({"unused","static-access"})
public class ResourceLoader {

	/***
	 * Handle loading of app related files
	 * 
	 * 
	 * */
	private FileHandler handler = null;
	private ObjectInputStream inputStream;
	private FileInputStream fileStream;
	private Message msg = new Message();
	private String seperator = File.separator+File.separator;
	private File uiTheme=PreferencesManager.settingsFile,
		listFile = new File("app"+seperator+"userlist.dat");
	public ResourceLoader(FileHandler handler){
		this.handler=handler;
	}	
	public Settings getPreferences(){
		//Settings class of preferences Manager
		//may be invisible but Settings class of java.util is not.
		//That's how you fool the compiler :)
		Settings s=null;
		try {
			fileStream = new FileInputStream(uiTheme);
			inputStream = new ObjectInputStream(fileStream);
			s=(Settings)inputStream.readObject();
		} catch (FileNotFoundException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (ClassNotFoundException cnf){
			
		}
		return s;
		
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
		}catch(FileNotFoundException fexc){
			msg.error(null, "You haven't saved any list.");
			handler.log(fexc.getMessage());
		}catch (IOException io) {
			msg.error(null, "IO exception occured", "Error");
			handler.log(io.getMessage());			
		} catch (ClassNotFoundException cn) {
			msg.error(null, "Corrupted file found");
			handler.log(cn.getMessage());
		}	
		return p;
	}
}
