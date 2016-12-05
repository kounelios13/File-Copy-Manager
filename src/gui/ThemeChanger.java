package gui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
@SuppressWarnings("all")
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Nimbus","Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	private String[] defaultThemes= {"Default","Small-Font","Large-Font","Giant-Font"};
	private Controller controller = new Controller();
	private DefaultComboBoxModel<String> lookAndFeelModel = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> themeModel       = new DefaultComboBoxModel<String>();
	private JComboBox<String> combo      = new JComboBox<>();
	private JComboBox<String> themeCombo = new JComboBox<>();
	private JLabel label = new JLabel("Look And Feel"),
			  themeLabel = new JLabel("Theme List");
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
		boolean standardThemeList = false;
		boolean isNimbus = false;
		switch(look){
			case "Nimbus":
				isNimbus = true;
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
				standardThemeList = true;
				AluminiumLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="aluminium.AluminiumLookAndFeel";
				break;
			case "Bernstein":
				standardThemeList = true;
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
				standardThemeList = true;
				HiFiLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="hifi.HiFiLookAndFeel";
				break;
			case "Luna":
				standardThemeList = true;
				LunaLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="luna.LunaLookAndFeel";
				break;
			case "McWin":
				McWinLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="mcwin.McWinLookAndFeel";
				break;
			case "Mint":
				standardThemeList = true;
				MintLookAndFeel.setCurrentTheme(props);
				lookAndFeelName+="mint.MintLookAndFeel";
				break;
			case "Noire":
				standardThemeList = true;
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
		if(standardThemeList){
			themeCombo.setModel(new DefaultComboBoxModel<String>(defaultThemes));
		}else{
			ArrayList<String> themes =new ArrayList<>(Arrays.asList("Default","Small-Font","Large-Font","Giant-Font"));
			if(look.equals("Acryl")){
				themes.addAll(Arrays.asList("Green","Green-Small-Font","Green-Large-Font","Green-Giant-Font"
						,"Lemmmon","Lemmon-Small-Font","Lemmon-Large-Font","Lemmon-Giant-Font","Red",
						"Red-Small-Font","Red-Large-Font","Red-Giant-Font"));
			}
			else if(look.equals("Aero")){
				themes.addAll(Arrays.asList("Gold","Gold-Small-Font","Gold-Large-Font","Gold-Giant-Font","Green",
						"Green-Small-Font","Green-Large-Font","Green-Giant-Font"));
			}
			else if(look.equals("Fast")){
				themes.addAll(Arrays.asList("Blue","Blue-Small-Font","Blue-Large-Font","Blue-Giant-Font",
					"Green","Green-Small-Font","Green-Large-Font","Green-Giant-Font"));
			}
			else if(look.equals("Graphite")){
				themes.addAll(Arrays.asList("Green","Green-Small-Font","Green-Large-Font","Green-Giant-Font",
					"Blue","Blue-Small-Font","Blue-Large-Font","Blue-Giant-Font"));
			}
			else if(look.equals("McWin")){
				themes.addAll(Arrays.asList("Modern","Modern-Small-Font","Modern-Large-Font","Modern-Giant-Font","Pink",
					"Pink-Small-Font","Pink-Large-Font","Pink-Giant-Font"));
			}
			else if(look.equals("Smart")){
				themes.addAll(Arrays.asList("Gold","Gold-Small-Font","Gold-Large-Font","Gold-Giant-Font",
					"Green","Green-Small-Font","Green-Large-Font","Green-Giant-Font","Brown",
					"Brown-Small-Font","Brown-Large-Font","Brown-Giant-Font","Lemmmon",
					"Lemmon-Small-Font","Lemmon-Large-Font","Lemmon-Giant-Font","Gray",
					"Gray-Small-Font","Gray-Large-Font","Gray-Giant-Font"));
			}
			else{
				themes.addAll(Arrays.asList("Rock","Rock-Small-Font","Rock-Large-Font","Rock-Giant-Font",
					"Textile","Textile-Small-Font","Textile-Large-Font","Textile-Giant-Font","Snow",
					"Snow-Small-Font","Snow-Large-Font","Snow-Giant-Font"));
			}
			if(!isNimbus){
				/**
				 * Nimbus does not need any themes
				 * */
				for(String theme :themes)
					themeModel.addElement(theme);
				themeCombo.setModel(themeModel);
				themeCombo.setVisible(true);
				themeLabel.setVisible(true);
				this.pack();
			}
			else
			{
				themeLabel.setVisible(false);
				themeCombo.setVisible(false);
			}
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
			lookAndFeelModel.addElement(theme);
		combo.setModel(lookAndFeelModel);
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
		panel.add(themeLabel,"cell 1 1");
		saveBtn.addActionListener(e->{
			info.setThemeName(currentTheme);
			controller.saveLookAndFeel(info);
			Message.warning("In order to properly apply the new theme File Copy Manager will restart.\nPress OK");
			frame.restart();
		});
		themeCombo.addActionListener(e->{
			SwingUtilities.invokeLater(()->{
				String theme = (String)themeCombo.getSelectedItem(),
						look = (String)combo.getSelectedItem();
				updateTheme(look,theme);
			});
		});
		panel.add(themeCombo,"cell 5 1");
		panel.add(saveBtn, "cell 1 4");
		closeBtn.addActionListener(e->deactivate());
		panel.add(closeBtn,"cell 5 4"); 
		panel.add(combo,"cell 5 0");
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	private void updateTheme(String look,String theme) {
		switch(look){
			case "Acryl":
				AcrylLookAndFeel.setTheme(theme,"","");
				break;
			case "Aero":
				AeroLookAndFeel.setTheme(theme,"","");
				break;
			case "Aluminium":
				AluminiumLookAndFeel.setTheme(theme,"","");
				break;
			case "Bernstein":
				BernsteinLookAndFeel.setTheme(theme,"","");
				break;
			case "Fast":
				FastLookAndFeel.setTheme(theme,"","");
				break;
			case "Graphite":
				GraphiteLookAndFeel.setTheme(theme,"","");
				break;
			case "HiFi":
				HiFiLookAndFeel.setTheme(theme,"","");
				break;
			case "Luna":
				LunaLookAndFeel.setTheme(theme,"","");
				break;
			case "McWin":
				McWinLookAndFeel.setTheme(theme,"","");
				break;
			case "Mint":
				MintLookAndFeel.setTheme(theme,"","");
				break;
			case "Noire":
				NoireLookAndFeel.setTheme(theme,"","");
				break;
			case "Smart":
				SmartLookAndFeel.setTheme(theme,"","");
				break;
			case "Texture":
				TextureLookAndFeel.setTheme(theme,"","");
				break;
			
		}
		try{
			UIManager.setLookAndFeel(lookAndFeelName);
		    SwingUtilities.updateComponentTreeUI(this);
		    pack();
		   
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}
	public void activate(){
		this.setVisible(true);
	}
	public void deactivate(){
		this.setVisible(false);
	}
	@SuppressWarnings("unused")
	//For future use
	private boolean isNimbusTheme(String theme){
		return theme.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	}
}
