package gui;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import utilities.PreferencesManager;
@SuppressWarnings("unused")
public class CustomColorChooser extends JDialog {
	private static final long serialVersionUID = -7374804525631377356L;
	private GUI gui;
	private PreferencesManager pMan;
	private JComponent targetComponent;
    private JColorChooser colorChooser;
    private JButton backgroundButton,
    		foregroundButton,
    		okButton;
    private Color bgColor,
    			  fgColor;
    public Color getBgColor(){
    	return bgColor;
    }
    public Color getFgColor(){
    	return fgColor; 
    }
    public CustomColorChooser(GUI g,PreferencesManager p){
    	this((JComponent)null);
    	gui = g;
    	pMan = p;
    	
    }
    private void init(){
    	colorChooser = new JColorChooser();
        backgroundButton = new JButton("Background Color");       
        foregroundButton = new JButton("Foreground Color");
        okButton = new JButton("OK");
    	backgroundButton.addActionListener((e)->pMan.setBg(colorChooser.getColor()));
    	 foregroundButton.addActionListener((e)->pMan.setFg(colorChooser.getColor()));
    	 okButton.addActionListener((e)->{
    		 dispose();
    		 pMan.updatePreview();
    	 });
    }
    public CustomColorChooser(JComponent targetComponent) {
        this.targetComponent = targetComponent;
        init();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backgroundButton);
        buttonPanel.add(foregroundButton);
        buttonPanel.add(okButton);
        getContentPane().add(colorChooser, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        pack();
        setModal(true);
        setLocationRelativeTo(targetComponent);
    }
   
}