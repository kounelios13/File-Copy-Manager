package utils;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import serializable.ProgramState;
import serializable.ThemeInfo;
public class Controller {
	private FileHandler handler = new FileHandler();
	public FileHandler getFileHandler(){
		return handler;
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
	public void saveLookAndFeel(ThemeInfo info) {
		handler.saveLookAndFeel(info);
	}
	public ThemeInfo getThemeInfo(){
		return handler.getThemeInfo();
	}
}
