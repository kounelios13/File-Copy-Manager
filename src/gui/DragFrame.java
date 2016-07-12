package gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.FileDrop;
@SuppressWarnings({"serial"})
public class DragFrame extends JFrame{

	private GUI gui;
	private JPanel panel = new JPanel();
	public DragFrame(GUI g,ArrayList<File> storage,DefaultComboBoxModel<String> model){
		super("Drag Files here");
		new FileDrop(panel,new FileDrop.Listener() {			
			@Override
			public void filesDropped(File[] draggedFiles) {
				try{
					for(File f:draggedFiles){
						//Check if any file dropped here is a directory to get its inner files
						storage.add(f);
						String name = f.isDirectory()?" (Folder)":"";
						model.addElement(f.getName()+name);
					}
				}
				catch(NullPointerException n){
					System.out.println("Hit an end.");
				}
				finally{
					//fileNames.setVisible(files.size() > 0);
					gui.pack();
					gui.showFiles();
					//System.out.println("Total files:"+files.size());
				}
				//Can't use this.pack()
				//cause 'this' refers to FileDrop class 	
			}
		});
		this.setContentPane(panel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(200,200);
		this.setVisible(true);	
	}
}
