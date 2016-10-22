package voxSpell.views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ButtonFactory {

	public JButton getButton(String imagePath, String hoverImagePath){
		final JButton button;
		final ImageIcon image = new ImageIcon(imagePath);
		final ImageIcon hover = new ImageIcon(hoverImagePath);
		button  = new JButton( image );
		
		button .setPreferredSize(new Dimension(image.getIconWidth(),image.getIconHeight()));
		button.setBorderPainted(false);
		button.setFocusable(false);
		
		button.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					button.setIcon(hover);
				
				}else{
					button.setIcon(image);
				}
			}
		});
		
		return button;
	}
}
