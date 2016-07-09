package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
public class CustomColorChooser extends JDialog {

    /**
	 * 
	 */

	private static final long serialVersionUID = -7374804525631377356L;
	JComponent targetComponent;
    JColorChooser colorChooser;
    JButton backgroundButton;
    JButton foregroundButton;
    JButton okButton;
    Color bgColor;
    Color fgColor;
    public Color getBgColor(){
    	return bgColor;
    }
    public Color getFgColor(){
    	return fgColor;
    }
    public CustomColorChooser(){
    	this(null);
    }
    public CustomColorChooser(JComponent targetComponent) {
        this.targetComponent = targetComponent;
        colorChooser = new JColorChooser();
        backgroundButton = new JButton("Background Color");
        backgroundButton.addActionListener((e)->bgColor = colorChooser.getColor());
        foregroundButton = new JButton("Foreground Color");
        foregroundButton.addActionListener((e)->fgColor = colorChooser.getColor());
        okButton = new JButton("Apply");
        okButton.addActionListener((e)->dispose());
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