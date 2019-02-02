/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Monster;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//displays creature and lets you change it
public class EnemyFormationSinglePanel extends JPanel implements MouseListener, MouseWheelListener{
    
    private EnemySelectFrame frame;
    
    private CreaturePanelGroup creatureGroup;
    private boolean facingRight;
    
    private CreaturePicturePanel picPanel;

    public EnemyFormationSinglePanel(EnemySelectFrame frame, CreaturePanelGroup group, Creature creature, boolean facingRight) {
        this.frame = frame;
        this.creatureGroup = group;
        this.facingRight = facingRight;
        
        picPanel = new CreaturePicturePanel(creature);
        add(picPanel);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        picPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        //setOpaque(false);
        addMouseListener(this);
        addMouseWheelListener(this);
        picPanel.removeMouseListener(picPanel);//replaces mouse actions with this classe's actions. problems with multiple mouseListeners
        //setBackground(Color.BLUE);
        
    }
    
    public Creature getCreature(){
        return picPanel.getCreature();
    }
    
    public void setCreature(Creature c){
        if (c == null && picPanel.getCreature() == null){
            return;
        }
        
        if (c == null || c.isFacingRight() == facingRight){//all creatures need to be facing the same
            picPanel.setCreature(c);
            frame.parametersChanged();
            repaint();
        }
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2){//middle mouse to "copy"
            //System.out.println("hi");
            frame.setMouseCreature(getCreature());
        }
        else{//pick up creature
            if (frame.getMouseCreature() == null){
                frame.setMouseCreature(getCreature());
                setCreature(null);
            }
            else{//drop off creature
                Creature c = frame.getMouseCreature();
                if (!(c instanceof Monster) && creatureGroup.containsCreature(c.getName())){
                    creatureGroup.removeCreature(c.getName());
                }
                setCreature(c);
                frame.setMouseCreature(null);
            }
        }
        
    }
    
    

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Creature creature = picPanel.getCreature();
        if (creature != null){
            setToolTipText(creature.toolTipText());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setToolTipText("");
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        
        changeTierOfMonster(-notches);
    }
    
    private void changeTierOfMonster(int tierDifference){
        if (picPanel.getCreature() instanceof Monster){
            
            Monster m = (Monster)picPanel.getCreature();
            int newTier = m.getTier() + tierDifference;
            
            if (Monster.validTier(newTier)){
                Monster newMonster = CreatureFactory.getMonster(m.getElement(), newTier);
                newMonster.setFacingRight(facingRight);
                setCreature(newMonster);
                
            }
        }
    }
    
}
