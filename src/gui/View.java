package gui;
import interfaces.IView;
import javax.swing.JFrame;
@SuppressWarnings("serial")
public  class View extends JFrame implements IView{
	public View(String title,int width,int height){
		super(title);
		this.setSize(width, height);
	}
	public void toggleUI(){
		this.setVisible(!this.isVisible());
	}
}
