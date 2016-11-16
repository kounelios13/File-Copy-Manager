package gui;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
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
	private Controller controller = new Controller();
	private JComboBox<String> combo = new JComboBox(lookAndFeelArray);
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
			System.out.println("index:"+temp.getThemeIndex());
			System.out.println("name:"+temp.getThemeName());
		}
		else{
			System.out.println("No theme was found");
		}
	}
	public void update(String look){
		String feel="com.jtattoo.plaf.";
		switch(look){
			case "Nimbus":
				feel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
				break;
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
			info.setThemeName(feel);
			FileCopyManager f = (FileCopyManager) frame;
			f.updateLookAndFeel(feel);
			UIManager.setLookAndFeel(feel);
			//SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}	
	public void initUIElements(){
		searchTheme();
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][][]"));
		this.setContentPane(panel);
		combo.addActionListener(e->{
			update((String)combo.getSelectedItem());
			info.setThemeIndex(combo.getSelectedIndex());
		});
		panel.add(label, "cell 1 0");
		combo.setSelectedIndex(info.getThemeIndex());
		panel.add(combo,"cell 5 0");
		saveBtn.addActionListener(e->{
			System.out.println("Index to save:"+info.getThemeIndex());
			System.out.println("Theme name:"+info.getThemeName());
			controller.saveLookAndFeel(info);
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
