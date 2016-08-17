package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import messages.Message;
@SuppressWarnings({"static-access"})
public class ResourceLoader {
	/***
	 * Handle loading of app related files
	 * */
	private FileHandler handler = null;
	private ObjectInputStream inputStream;
	private Message msg = new Message();
	private String separator = File.separator+File.separator;
	private File uiTheme=new File("app"+separator+"settings.dat"),
		listFile = new File("app"+separator+"userlist.dat");
	public ResourceLoader(FileHandler handler){
		this.handler=handler;
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
			handler.log(exc.getMessage());
		} catch (ClassNotFoundException ci){
			msg.error(null,
				"Settings come from an older version of program that is not supported.Please choose new settings and press 'Save'",
				"Invalid settings");
			handler.log(ci.getMessage());
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
			inputStream = new ObjectInputStream(new FileInputStream(listFile));
			p = (ProgramState)inputStream.readObject();
			inputStream.close();
		}catch (IOException io) {
			msg.error(null, "You haven't saved any list.");
			handler.log(io.getMessage());			
		} catch (ClassNotFoundException cn) {
			msg.error(null, "Corrupted file found");
			handler.log(cn.getMessage());
		}	
		return p;
	}
}
