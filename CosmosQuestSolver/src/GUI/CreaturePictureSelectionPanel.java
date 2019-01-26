/*

 */
package GUI;

import Formations.Creature;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

//draws panel and lets you select a creature
public class CreaturePictureSelectionPanel extends CreaturePicturePanel implements MouseListener{
    
    private EnemySelectFrame frame;
    

    public CreaturePictureSelectionPanel(EnemySelectFrame frame, Creature creature) {
        super(creature);
        this.frame = frame;
        
        
        setOpaque(false);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (frame.getMouseCreature() == creature){
            frame.setMouseCreature(null);
        }
        else{
            frame.setMouseCreature(creature);
        }
        frame.requestFocusInWindow();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
    }

    


    
    
}
