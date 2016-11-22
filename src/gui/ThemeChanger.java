package gui;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import messages.Message;
import net.miginfocom.swing.MigLayout;
import serializable.ThemeInfo;
import utils.Controller;
import utils.FileHandler;
import utils.ResourceLoader;
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Nimbus","Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	private Controller controller = new Controller();
	private DefaultComboBoxModel<String> themeModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> combo = new JComboBox<>();
	private JLabel label = new JLabel("Look And Feel");
	private JButton saveBtn = new JButton("Save and Apply Theme"),
				   closeBtn = new JButton("Close");
	private ApplicationScreen frame;
	private String lookAndFeelName = null,
			currentTheme = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	private ThemeInfo info = new ThemeInfo("javax.swing.plaf.nimbus.NimbusLookAndFeel",0);
	@Override
	public String toString() {
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
	public ThemeChanger(FileCopyManager fileCopyManager) {
		super("Change Look And feel",600,400);
		this.frame = fileCopyManager;
		initUIElements();
	}
	public void searchTheme(){
		FileHandler fh = controller.getFileHandler();
		ResourceLoader rc = new ResourceLoader(fh);
		ThemeInfo temp = rc.getThemeInfo();
		if(temp != null ){
			((FileCopyManager)frame).updateLookAndFeel(temp.getThemeName());
			combo.setSelectedIndex(temp.getThemeIndex());
			info = temp;
			currentTheme = info.getThemeName();
		}
	}
	public void update(String look){
		Properties props = new Properties();
		props.put("logoString", "");
		props.put("licensekey", "");
		lookAndFeelName="com.jtattoo.plaf.";
		switch(look){
			case "Nimbus":
				lookAndFeelName = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
				break;
			case "Acryl":
				AcrylLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="acryl.AcrylLookAndFeel";
				break;
			case "Aero":
				AeroLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="aero.AeroLookAndFeel";
				break;
			case "Aluminium":
				AluminiumLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="aluminium.AluminiumLookAndFeel";
				break;
			case "Bernstein":
				BernsteinLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="bernstein.BernsteinLookAndFeel";
				break;
			case "Fast":
				FastLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="fast.FastLookAndFeel";
				break;
			case "Graphite":
				GraphiteLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="graphite.GraphiteLookAndFeel";
				break;
			case "HiFi":
				HiFiLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="hifi.HiFiLookAndFeel";
				break;
			case "Luna":
				LunaLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="luna.LunaLookAndFeel";
				break;
			case "McWin":
				McWinLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="mcwin.McWinLookAndFeel";
				break;
			case "Mint":
				MintLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="mint.MintLookAndFeel";
				break;
			case "Noire":
				NoireLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="noire.NoireLookAndFeel";
				break;
			case "Smart":
				SmartLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="smart.SmartLookAndFeel";
				break;
			case "Texture":
				TextureLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="texture.TextureLookAndFeel";
				break;
		}
		try{
			currentTheme = lookAndFeelName;
			FileCopyManager f = (FileCopyManager) frame;
			f.updateLookAndFeel(lookAndFeelName);
			UIManager.setLookAndFeel(lookAndFeelName);
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e){
		}
	}	
	public void initUIElements(){
		for(String theme:lookAndFeelArray)
			themeModel.addElement(theme);
		combo.setModel(themeModel);
		searchTheme();
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][][]"));
		this.setContentPane(panel);
		combo.addActionListener(e->{
			SwingUtilities.invokeLater(()->{
				update((String)combo.getSelectedItem());
				info.setThemeIndex(combo.getSelectedIndex());
			});
		});
		panel.add(label, "cell 1 0");
		combo.setSelectedIndex(info.getThemeIndex());
		panel.add(combo,"cell 5 0");
		saveBtn.addActionListener(e->{
			info.setThemeName(currentTheme);
			controller.saveLookAndFeel(info);
			Message.warning("In order to properly apply the new theme File Copy Manager will restart.\nPress OK");
			frame.restart();
		});
		closeBtn.addActionListener(e->deactivate());
		panel.add(saveBtn, "cell 1 1");
		panel.add(closeBtn,"cell 5 1"); 
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	public void activate(){
		this.setVisible(true);
	}
	public void deactivate(){
		this.setVisible(false);
	}
	private boolean isNimbusTheme(String theme){
		return theme.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	}
}
