/*
 * CustomColorChooser.java
 *
 * Created on 27.09.2016, 13:35:49
 *
 * This file is part of the File Copy Manager project.
 * 
 * The File Copy Manager is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 * 
 * The File Copy Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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