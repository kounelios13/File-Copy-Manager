package dev;
import gui.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import advanced.CustomColorChooser;
public class PreferencesManager{
	private GUI gui = new GUI();
	private Message msg = new Message();
	private File theme_file=new File("settings\\themePreferences.mr");
	CustomColorChooser colorChooser =new CustomColorChooser(null);
	JFrame frame = new JFrame("Προτιμήσεις");
	private JButton saveSettings=new JButton("Save Settings");
	private JButton loadSettings = new JButton("Load Settings");
	private JPanel panel = new JPanel();
	private JComboBox<String> fontComboBox=new JComboBox<>();
	private JSlider labelSizeSlider=new JSlider(12,32,15);//min val,max val,current value
	private JSlider buttonFontSizeSlider=new JSlider(12,32,15);
	private JLabel labelFontSizeLabel=new JLabel("Μέγεθος ετικετών");
	private JLabel sampleLabel=new JLabel("Ετικέτα");
	private String fontName="";
	private float labelFontSize=0;
	private float textFieldFontSize=15;
	private float buttonFontSize;
	private final JButton btnApplySettings = new JButton("Apply Settings");
	private boolean settingsAreLoaded=false;
	private JButton chooseColors=new JButton("Choose color");
	public Color bgColor=colorChooser.getBackground();
	public Color fgColor=colorChooser.getForeground();
	public PreferencesManager(GUI g){
		
		gui = g;
		DefaultComboBoxModel<String> fontModel=new DefaultComboBoxModel<>();
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts=e.getAllFonts();//get all system fonts
		//add them to the combobox model
		for(Font font:fonts)
			fontModel.addElement(font.getFontName());
		fontComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontName=(String)fontComboBox.getSelectedItem();
				updatePreviewPanel();
			}
		});
		fontComboBox.setModel(fontModel);
		panel.setLayout(new MigLayout("", "[][][][][][][][][]", "[][][][][][][][][][][][][][][][][12.00][][][][][][]"));
		panel.add(fontComboBox, "cell 0 0");
		loadSettings();
		panel.add(labelFontSizeLabel, "cell 0 7");
		labelSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				labelFontSize=labelSizeSlider.getValue();
				updatePreviewPanel();
			}
		});
		panel.add(labelSizeSlider, "cell 0 9");
		JLabel label = new JLabel("Μέγεθος κουμπιών");
		panel.add(label, "cell 0 10");
		buttonFontSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				buttonFontSize=buttonFontSizeSlider.getValue();
				updatePreviewPanel();
			}
		});
		buttonFontSizeSlider.setMinimum(12);
		buttonFontSizeSlider.setMaximum(30);
		buttonFontSizeSlider.setValue(15);
		panel.add(buttonFontSizeSlider,"cell 0 11");
		panel.add(sampleLabel, "cell 0 14");
		frame.setContentPane(panel);
		saveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings s=new Settings(fontName,labelFontSize,textFieldFontSize,buttonFontSize,bgColor,fgColor);
				File f=new File("settings\\themePreferences.mr");
				try{
					ObjectOutputStream out =new ObjectOutputStream(new FileOutputStream(f));
					out.writeObject(s);
					out.flush();
					out.close();
					applySettings();
				}
				catch(Exception e){
					msg.error(null,"Can't save preferences file","Error");
					e.printStackTrace();
				}
				finally{
					frame.setVisible(false);
				}
			}
		});
		
		panel.add(saveSettings,"flowx,cell 0 20");
		loadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadSettings();
			}
		});
		panel.add(loadSettings,"cell 0 20");
		chooseColors.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				colorChooser.setVisible(true);
				bgColor=colorChooser.getBgColor();
				fgColor=colorChooser.getFgColor();
				saveSettings.setBackground(bgColor);
				updatePreviewPanel();
			}
		});
		panel.add(chooseColors, "flowx,cell 0 17");
		btnApplySettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySettings();
			}
		});		
		panel.add(btnApplySettings, "cell 0 17");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
	}
	public void updatePreviewPanel(){
		JButton[] buttons={btnApplySettings,saveSettings,loadSettings,chooseColors};
		Font buttonFont=new Font(fontName,Font.PLAIN,(int)buttonFontSize);
		Font labelFont=new Font(fontName,Font.PLAIN,(int)labelFontSize);
		sampleLabel.setFont(labelFont);
		sampleLabel.setOpaque(false);//if it is not opaque
		//bgcolor will not be applied
		sampleLabel.setBackground(bgColor);
		sampleLabel.setForeground(fgColor);
		labelSizeSlider.setValue((int)labelFontSize);
		buttonFontSizeSlider.setValue((int)buttonFontSize);
		//pack() has to be last
		frame.pack();
	}
	public void editPreferences(){
		frame.setVisible(true);
	}
	public void loadSettings(){
		File f=new File("settings\\themePreferences.mr");
		Settings s=null;
		try{
			ObjectInputStream in =new ObjectInputStream(new FileInputStream(f));
			s=(Settings)in.readObject();
			in.close();
			fontName=s.getFontName();
			labelFontSize=s.getLabelFontSize();
			buttonFontSize=s.getButtonFontSize();
			fontComboBox.setSelectedItem((Object)fontName);
			bgColor=s.getBackgroundColor();
			fgColor=s.getForegroundColor();
			updatePreviewPanel();
			settingsAreLoaded=true;
		}	
		catch(Exception e1){
			//new ErrorMsg("Αδυναμία φόρτωσης προτιμήσεων");
			settingsAreLoaded=false;
		}
	}
	public void applySettings(){
		/*if(!settingsAreLoaded)
			loadSettings();*/
		for(JButton button:gui.getButtons())
			button.setFont(null);
	}	
}
@SuppressWarnings("serial")
class Settings implements Serializable{
	private String fontName;
	private float labelFontSize;
	private float textFieldFontSize;
	public  float buttonFontSize;
	private Color backgroundColor=null;
	private Color foregroundColor=null;
	public Settings(String fontName, float labelFontSize,
			float textFieldFontSize,float buttonSize) {
		
		this.fontName = fontName;
		this.labelFontSize = labelFontSize;
		this.textFieldFontSize = textFieldFontSize;
		this.buttonFontSize=buttonSize;
	}
	public Settings(String fontName, float labelFontSize,
			float textFieldFontSize,float buttonSize,Color bgCol,Color fgCol){
		this.fontName = fontName;
		this.labelFontSize = labelFontSize;
		this.textFieldFontSize = textFieldFontSize;
		this.buttonFontSize=buttonSize;
		this.backgroundColor=bgCol;
		this.foregroundColor=fgCol;
	}
	public Color getBackgroundColor(){
		return backgroundColor;
	}
	public Color getForegroundColor(){
		return foregroundColor;
	}
	public void setForegroundColor(Color f){
		foregroundColor=f;
	}
	public void setBackgroundColor(Color b){
		backgroundColor=b;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public float getLabelFontSize() {
		return labelFontSize;
	}
	public void setLabelFontSize(float labelFontSize) {
		this.labelFontSize = labelFontSize;
	}
	public float getTextFieldFontSize() {
		return textFieldFontSize;
	}
	public float getButtonFontSize(){
		return buttonFontSize;
	}
	public void setTextFieldFontSize(float textFieldFontSize) {
		this.textFieldFontSize = textFieldFontSize;
	}
	public Font getLabelFont(){
		return new Font(fontName,Font.PLAIN,(int)labelFontSize);
	}
	public Font getTextFieldFont(){
		return new Font(fontName,Font.PLAIN,(int)textFieldFontSize);
	}
	public Font getButtonFont(){
		return new Font(fontName,Font.PLAIN,(int)buttonFontSize);
	}
}
class Message{
	public void error(JPanel panel,String msg,String title){
		JOptionPane.showMessageDialog(panel, msg,title,JOptionPane.ERROR_MESSAGE);
	}
	public void info(JPanel panel,String msg,String title){
		JOptionPane.showMessageDialog(panel, msg,title,JOptionPane.INFORMATION_MESSAGE);
	}
}