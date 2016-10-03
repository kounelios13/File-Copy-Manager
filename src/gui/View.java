package gui;
import interfaces.IView;
import javax.swing.JFrame;
@SuppressWarnings("serial")
public abstract class View extends JFrame implements IView{
	public View(String title,int width,int height){
		super(title);
		this.setSize(width, height);
	}
	public View(String title,int width,int height,boolean resizable){
		this.setResizable(resizable);
	}
	public abstract String toString();
	public void toggleUI(){
		this.setVisible(!this.isVisible());
	}
}
