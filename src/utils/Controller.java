package utils;
import gui.View;


import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
@SuppressWarnings("all")
public class Controller {
	private FileHandler handler = new FileHandler();
	private View currentView = null;
	public Controller(){
		
	}
	public Controller(View view){
		
	}
	public View getView(){
		return currentView;
	}
	public void setView(View v){
		currentView = v;
	}
	public void saveList(ProgramState ps,File d){
		handler.saveList(ps,d);
	}
	public void openDestination(String dPath){
		handler.openDestination(dPath);
	}
	public ProgramState loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage){
		return handler.loadList(mod,storage);
	}
	public void openAppDirectory() {
		handler.openAppDirectory();
	}
	
}
