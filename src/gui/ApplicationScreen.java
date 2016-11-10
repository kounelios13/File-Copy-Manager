package gui;
import javax.swing.JButton;
import javax.swing.JLabel;
@SuppressWarnings("serial")
public abstract class ApplicationScreen extends View {
	public ApplicationScreen(String title, int width, int height) {
		super(title, width, height);
		// TODO Auto-generated constructor stub
	}
	public ApplicationScreen (String title,int width,int height,boolean resizable) {
		super(title,width,height,resizable);
	}
	public abstract JLabel[] getLabels();
	public abstract JButton[] getButtons();
	/*
	 * Restart method is useful when you apply new settings or you want to 'unload' any files
	 * loaded*/
	public abstract void restart();
}
