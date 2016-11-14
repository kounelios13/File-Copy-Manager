package gui;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.jtattoo.plaf.mint.MintLookAndFeel;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import com.jtattoo.plaf.texture.TextureLookAndFeel;
@SuppressWarnings("all")
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	private JComboBox<String> combo = new JComboBox(lookAndFeelArray);
	private Properties props = new Properties();
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
	public void update(String look){
		String feel="com.jtattoo.plaf.";
		switch(look){
			case "Acryl":
				AcrylLookAndFeel.setTheme(props);
				feel+="acryl.AcrylLookAndFeel";
				break;
			case "Aero":
				AeroLookAndFeel.setTheme(props);
				feel+="aero.AeroLookAndFeel";
				break;
			case "Aluminium":
				AluminiumLookAndFeel.setTheme(props);
				feel+="aluminium.AluminiumLookAndFeel";
				break;
			case "Bernstein":
				BernsteinLookAndFeel.setTheme(props);
				feel+="bernstein.BernsteinLookAndFeel";
				break;
			case "Fast":
				FastLookAndFeel.setTheme(props);
				feel+="fast.FastLookAndFeel";
				break;
			case "Graphite":
				GraphiteLookAndFeel.setTheme(props);
				feel+="graphite.GraphiteLookAndFeel";
				break;
			case "HiFi":
				HiFiLookAndFeel.setTheme(props);
				feel+="hifi.HiFiLookAndFeel";
				break;
			case "Luna":
				LunaLookAndFeel.setTheme(props);
				feel+="luna.LunaLookAndFeel";
				break;
			case "McWin":
				McWinLookAndFeel.setTheme(props);
				feel+="mcwin.McWinLookAndFeel";
				break;
			case "Mint":
				MintLookAndFeel.setTheme(props);
				feel+="mint.MintLookAndFeel";
				break;
			case "Noire":
				NoireLookAndFeel.setTheme(props);
				feel+="noire.NoireLookAndFeel";
				break;
			case "Smart":
				SmartLookAndFeel.setTheme(props);
				feel+="smart.SmartLookAndFeel";
				break;
			case "Texture":
				TextureLookAndFeel.setTheme(props);
				feel+="texture.TextureLookAndFeel";
				break;
		}
		try{
			UIManager.setLookAndFeel(feel);
			SwingUtilities.updateComponentTreeUI( this );
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}	
	public void initUIElements(){
		props.put("logoString","");
		props.put("licenseKey","");
		combo.addActionListener(e->{
			update((String)combo.getSelectedItem());
		});
		combo.setSelectedIndex(0);
		JPanel panel = new JPanel();
		JButton btn = new JButton("Sample");
		panel.add(btn);
		panel.add(combo);
		this.setContentPane(panel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			 try {
				UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
				new ThemeChanger("Theme Changer",600,400);
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
