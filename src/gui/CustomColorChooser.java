package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public CustomColorChooser(JComponent targetComponent) {
        this.targetComponent = targetComponent;
        colorChooser = new JColorChooser();
        ButtonActionListener listener = new ButtonActionListener();
        backgroundButton = new JButton("Χρώμα  υποβαθρου");
        backgroundButton.addActionListener(listener);
        foregroundButton = new JButton("Χρώμα κειμένου");
        foregroundButton.addActionListener(listener);
        okButton = new JButton("Εφαρμογή ρυθμίσεων");
        okButton.addActionListener(listener);
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
    

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(backgroundButton)) {
                //targetComponent.setBackground(colorChooser.getColor());
                bgColor=colorChooser.getColor();
            } else if (e.getSource().equals(foregroundButton)) {
                //targetComponent.setForeground(colorChooser.getColor());
                fgColor=colorChooser.getColor();
            } else if (e.getSource().equals(okButton)) {           	
                dispose();
            }
        }
    }
    

}