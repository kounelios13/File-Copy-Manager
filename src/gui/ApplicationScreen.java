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
	/*@Override
	public JComponent[] getComponents(){
		JButton[] btns = getButtons();
		JLabel[] lbls = getLabels();
		JComponent[] objects = new JComponent[btns.length+lbls.length];
		int index=0;
		for(int i = 0;i<btns.length;i++)
		{
			objects[index] = btns[index++];
		}
		for(int i=0;i<lbls.length;i++)
			objects[index]=lbls[index++];
		return objects;
	}*/
	public abstract void restart();
}
