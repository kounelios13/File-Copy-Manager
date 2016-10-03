package gui;
import interfaces.UIPreferences;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
public class CustomColorChooser extends JDialog {
    private static final long serialVersionUID = -7374804525631377356L;
    private UIPreferences pMan;
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
    public CustomColorChooser(UIPreferences settingsManager){
        this((JComponent)null);
        pMan = settingsManager;
    }
    private void initUIComponents(){
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
    private CustomColorChooser(JComponent targetComponent) {
        initUIComponents();
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
