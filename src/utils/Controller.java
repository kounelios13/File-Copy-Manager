package utils;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
@SuppressWarnings({})
public class Controller {
	private FileHandler handler= new FileHandler();
	public void saveList(ProgramState ps,File d){
		handler.saveList(ps,d);
	}
	public ProgramState loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage){
		return handler.loadList(mod,storage);
	}
}
