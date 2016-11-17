package gui;
import javax.swing.JButton;
import javax.swing.JLabel;
@SuppressWarnings("serial")
//This class is responsible for creating the main GUI of a program
public abstract class ApplicationScreen extends View {
	public ApplicationScreen(String title, int width, int height) {
		super(title, width, height);
	}
	public ApplicationScreen (String title,int width,int height,boolean resizable) {
		super(title,width,height,resizable);
	}
	public abstract JLabel[] getLabels();
	public abstract JButton[] getButtons();
	/*
	 * Restart method is useful when you apply new settings or you want to 'unload' any files
	 * loaded*/
	public abstract ApplicationScreen restart();
}
