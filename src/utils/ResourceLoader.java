package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import com.Messages.Message;
import extra.XString;
import serializable.ProgramState;
import serializable.Settings;
import serializable.ThemeInfo;
@SuppressWarnings({"static-access"})
public class ResourceLoader {
	/***
	 * Handle loading of app related files
	 * */
	private File lookAndFeel = new File("app/lookAndFeel.dat");
	private ObjectInputStream inputStream;
	private FileHandler handler = null;
	private String  separator   = File.separator+File.separator;
	private Message msg 		= new Message();
	private File appDir		    = new File("app"), 
				 uiTheme 		= new File("app"+separator+"settings.dat"),
				 listFile 		= new File("app"+separator+"userlist.dat");
	public static  boolean checkFileExistence(ArrayList<File>files){
		//new addition to changelog
		// See if any of the files saved ,has been deleted
		int initSize = files.size();
		// Remove any file from the arraylist if this file does not exist
		XString buffer = new XString("");
		//Find the files that have been deleted since last time
		files.stream().filter(f->!f.exists())
			.sorted()
				.forEach(f->{
					buffer.append("File missing:"+f.getName());
					buffer.appendNewLine();
		});
		files.removeIf(f->!f.exists());
		files.trimToSize();
		//If there are deleted files find their name and log it
		if(files.size() != initSize){
			XString message 
						= new XString(!files.isEmpty()?
						"Some of the files you saved last time do not exist and have been deleted from your list."
						:"None of the files you saved last time is available.");
				message.append("\n See log file for more");
				if(!files.isEmpty())
					Message.warning(message.toString());
				else
					Message.error(message.toString());
			buffer.append("Number of missing files:"+(initSize-files.size()));
			FileHandler.log(buffer.toString());
		}
		return initSize != files.size();
	}
	public ResourceLoader(FileHandler handler){
		// Maybe a null FileHandler is passed as an argument to the constructor
		// Doesn't hurt checking it
		if(handler==null)
			throw new IllegalArgumentException("Provide a valid instance of FileHanlder");
		this.handler = handler;
	}	
	public Settings getPreferences(){
		/*
		 * Return ui preferences of the program
		 * **/
		Settings settings=null;
		if(!appDir.exists())
			return null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(uiTheme));
			settings=(Settings)inputStream.readObject();
			inputStream.close();
		} catch (IOException exc) {
			//handler.log(exc);
		} catch (ClassNotFoundException ci){
			msg.error(
				"Settings come from an older version of program that is not supported.Please choose new settings and press 'Save'",
				"Invalid settings");
			handler.log(ci);
		}
		return settings;
	}
	public ProgramState getAppState(){
		/**
		 * 
		 * Returns the last saved state of the main program
		 * (list of files added,selected file and destination folder)
		 * */
		ProgramState programState = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(listFile));
			programState = (ProgramState)inputStream.readObject();
			/**
			* If you don't close an I/O stream when you finish
			* you won't be able to make any edits to the files you are using
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
	public ThemeInfo getThemeInfo(){
		ThemeInfo info = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(lookAndFeel));
			try {
				info =(ThemeInfo)inputStream.readObject();
				inputStream.close();
			} catch (ClassNotFoundException exc) {
				// TODO Auto-generated catch block
				handler.log(exc);
			}
		} catch (FileNotFoundException exc) {
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			handler.log(exc);
		}
		return info;
	}
}
