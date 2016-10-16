package voxSpell.guiViews;

import javax.swing.JPanel;

/**
 * Interface to be implemented by every card view to ensure they have a method that returns their corresponding
 * card panel
 * @author chen
 */
public interface Card {
	abstract JPanel createAndGetPanel();
}
