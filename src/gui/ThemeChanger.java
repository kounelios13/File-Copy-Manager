package gui;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
import net.miginfocom.swing.MigLayout;
import utils.Controller;
@SuppressWarnings("all")
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	private Controller controller = new Controller();
	private JComboBox<String> combo = new JComboBox(lookAndFeelArray);
	private JLabel label = new JLabel("Look And Feel");
	private Properties props = new Properties();
	private JButton saveBtn = new JButton("Save and Apply Theme");
	private ApplicationScreen frame;
	private String lookAndFeelName = null;
	private void initThemes(){
		AcrylLookAndFeel.setTheme(props);
		AeroLookAndFeel.setTheme(props);
		AluminiumLookAndFeel.setTheme(props);
		BernsteinLookAndFeel.setTheme(props);
		FastLookAndFeel.setTheme(props);
		GraphiteLookAndFeel.setTheme(props);
		HiFiLookAndFeel.setTheme(props);
		LunaLookAndFeel.setTheme(props);
		McWinLookAndFeel.setTheme(props);
		MintLookAndFeel.setTheme(props);
		NoireLookAndFeel.setTheme(props);
		SmartLookAndFeel.setTheme(props);
		TextureLookAndFeel.setTheme(props);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Theme Changer";
	}
	/**
	 * @wbp.parser.constructor
	 */
	public ThemeChanger(ApplicationScreen frame,String title, int width, int height) {
		super(title, width, height);
		this.frame = frame;
		initUIElements();
	}
	public ThemeChanger(ApplicationScreen frame,String title, int width, int height,
			boolean resizable) {
		super(title, width, height, resizable);
		this.frame = frame;
		initUIElements();
	}
	public void update(String look){
		String feel="com.jtattoo.plaf.";
		switch(look){
			case "Acryl":
				feel+="acryl.AcrylLookAndFeel";
				break;
			case "Aero":
				feel+="aero.AeroLookAndFeel";
				break;
			case "Aluminium":
				feel+="aluminium.AluminiumLookAndFeel";
				break;
			case "Bernstein":
				feel+="bernstein.BernsteinLookAndFeel";
				break;
			case "Fast":
				feel+="fast.FastLookAndFeel";
				break;
			case "Graphite":
				feel+="graphite.GraphiteLookAndFeel";
				break;
			case "HiFi":
				feel+="hifi.HiFiLookAndFeel";
				break;
			case "Luna":
				feel+="luna.LunaLookAndFeel";
				break;
			case "McWin":
				feel+="mcwin.McWinLookAndFeel";
				break;
			case "Mint":
				feel+="mint.MintLookAndFeel";
				break;
			case "Noire":
				feel+="noire.NoireLookAndFeel";
				break;
			case "Smart":
				feel+="smart.SmartLookAndFeel";
				break;
			case "Texture":
				feel+="texture.TextureLookAndFeel";
				break;
		}
		try{
			UIManager.setLookAndFeel(feel);
			SwingUtilities.updateComponentTreeUI( this.frame==null?this:frame );
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}	
	public void initUIElements(){
		props.put("logoString","");
		props.put("licenseKey","");
		initThemes();
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][][]"));
		this.setContentPane(panel);
		combo.addActionListener(e->{
			update((String)combo.getSelectedItem());
		});
		saveBtn.addActionListener(e->{
			controller.saveLookAndFeel(lookAndFeelName);
		});
		panel.add(label, "cell 1 0");
		combo.setSelectedIndex(0);
		panel.add(combo,"cell 5 0");
		panel.add(saveBtn, "cell 2 1");
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	public void activate(){
		this.setVisible(true);
	}
	public void deactivate(){
		this.setVisible(false);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			 try {
				UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
				new ThemeChanger(null,"Theme Changer",600,400);
			 } 
			catch(Exception npe){
				System.out.println("Exception occured");
			}
		});
	}
}
