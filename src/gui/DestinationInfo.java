package gui;
import javax.swing.*;
@SuppressWarnings("all")
public class DestinationInfo extends View {

	private JPanel panel = new JPanel();
	public DestinationInfo(String title, int width, int height) {
		super(title, width, height);
		initUIElements();
		// TODO Auto-generated constructor stub
	}

	public DestinationInfo(String title, int width, int height,
			boolean resizable) {
		super(title, width, height, resizable);
		initUIElements();
		// TODO Auto-generated constructor stub
	}

	public DestinationInfo(String title,int width,int height,boolean resizable,int op){
		super(title,width,height,resizable,op);
		initUIElements();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initUIElements() {
		this.setContentPane(panel);
		
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		new DestinationInfo("Destination",400,400,false,EXIT_ON_CLOSE);
	}
}
