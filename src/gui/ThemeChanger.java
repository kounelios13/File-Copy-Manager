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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
@SuppressWarnings("all")
public class ThemeChanger extends View{
	private String[] lookAndFeelArray ={"Nimbus","Acryl","Aero","Aluminium",
		"Bernstein","Fast","Graphite","HiFi","Luna","McWin","Mint",
		"Noire","Smart","Texture"};
	private Controller controller = new Controller();
	private DefaultComboBoxModel<String> themeModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> combo = new JComboBox();
	private JLabel label = new JLabel("Look And Feel");
	private JButton saveBtn = new JButton("Save and Apply Theme"),
				   closeBtn = new JButton("Close");
	private ApplicationScreen frame;
	private String lookAndFeelName = null;
	private ThemeInfo info = new ThemeInfo("javax.swing.plaf.nimbus.NimbusLookAndFeel",0);
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
		}
	}
	public void update(String look){
		Properties props = new Properties();
		props.put("logoString", "");
		props.put("licensekey", "");
		String feel="com.jtattoo.plaf.";
		switch(look){
			case "Nimbus":
				feel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
				break;
			case "Acryl":
				AcrylLookAndFeel.setCurrentTheme(props);
				feel+="acryl.AcrylLookAndFeel";
				break;
			case "Aero":
				AeroLookAndFeel.setCurrentTheme(props);
				feel+="aero.AeroLookAndFeel";
				break;
			case "Aluminium":
				AluminiumLookAndFeel.setCurrentTheme(props);
				feel+="aluminium.AluminiumLookAndFeel";
				break;
			case "Bernstein":
				BernsteinLookAndFeel.setCurrentTheme(props);
				feel+="bernstein.BernsteinLookAndFeel";
				break;
			case "Fast":
				FastLookAndFeel.setCurrentTheme(props);
				feel+="fast.FastLookAndFeel";
				break;
			case "Graphite":
				GraphiteLookAndFeel.setCurrentTheme(props);
				feel+="graphite.GraphiteLookAndFeel";
				break;
			case "HiFi":
				HiFiLookAndFeel.setCurrentTheme(props);
				feel+="hifi.HiFiLookAndFeel";
				break;
			case "Luna":
				LunaLookAndFeel.setCurrentTheme(props);
				feel+="luna.LunaLookAndFeel";
				break;
			case "McWin":
				McWinLookAndFeel.setCurrentTheme(props);
				feel+="mcwin.McWinLookAndFeel";
				break;
			case "Mint":
				MintLookAndFeel.setCurrentTheme(props);
				feel+="mint.MintLookAndFeel";
				break;
			case "Noire":
				NoireLookAndFeel.setCurrentTheme(props);
				feel+="noire.NoireLookAndFeel";
				break;
			case "Smart":
				SmartLookAndFeel.setCurrentTheme(props);
				feel+="smart.SmartLookAndFeel";
				break;
			case "Texture":
				TextureLookAndFeel.setCurrentTheme(props);
				feel+="texture.TextureLookAndFeel";
				break;
		}
		try{
			info.setThemeName(feel);
			FileCopyManager f = (FileCopyManager) frame;
			f.updateLookAndFeel(feel);
			UIManager.setLookAndFeel(feel);
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
			controller.saveLookAndFeel(info);
			Message.warning("If you notice some graphic corruption or glitch select 'Edit->Restart application'", "New theme applied");
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
}
