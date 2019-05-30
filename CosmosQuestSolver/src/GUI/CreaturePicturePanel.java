/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

// panel for drawing creatures
public class CreaturePicturePanel extends JPanel implements MouseListener{
    
    protected Creature creature;
    public static final Color BORDER_COLOR = new Color(30,170,220);
    
    public CreaturePicturePanel(Creature creature) {
        this.creature = creature;
        //setBackground(Color.DARK_GRAY);
        setOpaque(false);
        addMouseListener(this);
        //setMouseOverText();
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(ImageFactory.getPicture("Backgrounds/Creature Background"), 0, 0, AssetPanel.CREATURE_PICTURE_SIZE, AssetPanel.CREATURE_PICTURE_SIZE, null);
        super.paintComponent(g);
        if (creature != null){
            creature.draw(g);
        }
        g.setColor(BORDER_COLOR);
        g.drawRect(0, 0, AssetPanel.CREATURE_PICTURE_SIZE-1, AssetPanel.CREATURE_PICTURE_SIZE-1);
    }
    
    public Creature getCreature(){
        return creature;
    }
    
    public void setCreature(Creature c){
        this.creature = c;
        //setMouseOverText();
    }
    
    private void setMouseOverText(){//toolTip overwrites mouseListener!!!
        if (creature != null){
            setToolTipText(creature.getName() + ": " + creature.getMainSkill().getDescription());
        }
        else{
            this.setToolTipText("");
        }
    }
    
    /*label.addMouseListener ( new MouseAdapter ()
{
    public void mousePressed ( MouseEvent e )
    {
        lpane.dispatchEvent ( SwingUtilities.convertMouseEvent ( e.getComponent (), e, lpane ) );
    }
} );
*/

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (creature != null){
            setToolTipText(creature.toolTipText());
            /*
        setToolTipText("<html>"
                              + creature.getName()
                              +"<br>"
                              + creature.getMainSkill().getDescription()
                              + "</html>");
*/
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setToolTipText("");
    }
    
}
