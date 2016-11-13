package gui;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Theme Changer";
	}
	public ThemeChanger(String title, int width, int height) {
		super(title, width, height);
		initUIElements();
	}
	public ThemeChanger(String title, int width, int height,
			boolean resizable) {
		super(title, width, height, resizable);
		initUIElements();
	}
	public void initUIElements(){
		JPanel panel = new JPanel();
		JButton btn = new JButton("Sample");
		panel.add(btn);
		this.setContentPane(panel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			 try {
				UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
				new ThemeChanger("e",600,400);
			 } catch (ClassNotFoundException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			} catch (InstantiationException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			} catch (IllegalAccessException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			} catch (UnsupportedLookAndFeelException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			}
		});
	}
}
