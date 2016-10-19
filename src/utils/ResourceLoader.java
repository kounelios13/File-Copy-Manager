package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import messages.Message;
@SuppressWarnings({"static-access"})
public class ResourceLoader {
	/***
	 * Handle loading of app related files
	 * */
	private ObjectInputStream inputStream;
	private FileHandler handler = null;
	private String  separator   = File.separator+File.separator;
	private Message msg 		= new Message();
	private File uiTheme 		= new File("app"+separator+"settings.dat"),
				 listFile 		= new File("app"+separator+"userlist.dat");
	public static  boolean filesRemoved(ArrayList<File>files){
		//new addition to changelog
		// See if any of the files saved ,has been deleted
		int initSize = files.size();
		//files.size() must be re executed every time the loop runs to avoid index out of bounds
		// Remove any file from the arraylist if this file does not exist
		files.removeIf(f->!f.exists());
		files.trimToSize();
		return initSize != files.size();
	}
	public ResourceLoader(FileHandler handler){
		this.handler = handler;
		// Maybe a null FileHandler is passed as an argument to the constructor
		// Doesn't hurt checking it
		this.handler = this.handler == null? new FileHandler():handler;
	}	
	public Settings getPreferences(){
		/** 
		*	Settings class of Preferences Manager
		*	may be invisible but Settings class of java.util is not.
		*	That's how you fool the compiler :)
		*/
		Settings s=null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(uiTheme));
			s=(Settings)inputStream.readObject();
			inputStream.close();
		} catch (IOException exc) {
			//handler.log(exc);
		} catch (ClassNotFoundException ci){
			msg.error(
				"Settings come from an older version of program that is not supported.Please choose new settings and press 'Save'",
				"Invalid settings");
			handler.log(ci);
		}
		return s;
	}
	public ProgramState getAppState(){
		/**
		 * 
		 * Returns the last saved state of the main program
		 * */
		ProgramState programState = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(listFile));
			programState = (ProgramState)inputStream.readObject();
			/**
			* Always close an I/O stream
			*/
			inputStream.close();
		}catch (IOException io) {
			msg.error("You haven't saved any list.");
			handler.log(io);			
		} catch (ClassNotFoundException cn) {
			msg.error("Corrupted file found");
			handler.log(cn);
		}	
		return programState;
	}
}
