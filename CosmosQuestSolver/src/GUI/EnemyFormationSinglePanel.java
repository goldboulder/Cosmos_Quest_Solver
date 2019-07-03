/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Hero;
import Formations.Monster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//displays creature and lets you change it
public class EnemyFormationSinglePanel extends JPanel implements MouseListener, MouseWheelListener, DocumentListener{
    
    private EnemySelectFrame frame;
    
    private CreaturePanelGroup creatureGroup;
    private boolean facingRight;
    private boolean allowHeroTweak;
    
    private CreaturePicturePanel picPanel;
    private JTextField textField;

    public static final int TEXT_FIELD_HEIGHT = 20;
    public EnemyFormationSinglePanel(EnemySelectFrame frame, CreaturePanelGroup group, Creature creature, boolean facingRight, boolean allowHeroTweak, boolean hasTextField) {
        this.frame = frame;
        this.creatureGroup = group;
        this.facingRight = facingRight;
        this.allowHeroTweak = allowHeroTweak;
        picPanel = new CreaturePicturePanel(creature);
        textField = new JTextField();
        add(picPanel);
        if (hasTextField){
            add(textField);
        }
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        picPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        textField.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,TEXT_FIELD_HEIGHT));
        //setOpaque(false);
        addMouseListener(this);
        addMouseWheelListener(this);
        picPanel.removeMouseListener(picPanel);//replaces mouse actions with this classe's actions. problems with multiple mouseListeners
        //setBackground(Color.BLUE);
        textField.getDocument().addDocumentListener(this);
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
            frame.setMouseCreature(getCreature());
        }
        else{//pick up creature
            if (frame.getMouseCreature() == null){
                frame.setMouseCreature(getCreature());
                setCreature(null);
                autoSetText(null);
            }
            else{//drop off creature
                Creature c = frame.getMouseCreature();
                if (!(c instanceof Monster) && creatureGroup.containsCreature(c.getName())){
                    creatureGroup.removeCreature(c.getName());
                }
                setCreature(c);
                autoSetText(c);
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
        
        tweakUnit(-notches);
    }
    
    private void tweakUnit(int tierDifference){
        if (picPanel.getCreature() instanceof Monster){
            
            Monster m = (Monster)picPanel.getCreature();
            int newTier = m.getTier() + tierDifference;
            
            if (Monster.validTier(newTier)){
                Monster newMonster = CreatureFactory.getMonster(m.getElement(), newTier);
                newMonster.setFacingRight(facingRight);
                setCreature(newMonster);
                autoSetText(newMonster);
                
            }
        }
        
        else if (picPanel.getCreature() instanceof Hero && allowHeroTweak){
            Hero h = (Hero)picPanel.getCreature();
            int level = h.getLevel();
            int promo = h.getPromoteLevel();
            
            if (tierDifference > 0){
                if (level < Hero.MAX_NORMAL_LEVEL && level >= 1){
                    h.levelUp(level + tierDifference);
                    setCreature(h);
                    autoSetText(h);
                    frame.redrawHero(h.getName());
                }
                else if (promo < Hero.MAX_PROMOTE_LEVEL){
                    h.promote(promo + tierDifference);
                    setCreature(h);
                    autoSetText(h);
                    frame.redrawHero(h.getName());
                }
            }
            else if (tierDifference < 0){
                if (promo > 0){
                    h.promote(promo +tierDifference);
                    setCreature(h);
                    autoSetText(h);
                    frame.redrawHero(h.getName());
                }
                else if (level > 1 && level <= Hero.MAX_NORMAL_LEVEL){
                    h.levelUp(level + tierDifference);
                    setCreature(h);
                    autoSetText(h);
                    frame.redrawHero(h.getName());
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        heroTyped();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        heroTyped();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        heroTyped();
    }
    
    private boolean manual;//stops documentListener from changing anything on auto-fills
    public void autoSetText(Creature c){
        manual = false;
        
        if (c == null){
            textField.setText("");
        }
        else{
            textField.setText(c.getFormationText());
        }
        manual = true;
        
        textField.setForeground(Color.BLACK);
    }
    
    private void heroTyped(){
        if (!manual){
            return;
        }
        String text = textField.getText();
        //System.out.println("heroTyped     " + text);
        if (text.isEmpty()){
            setCreature(null);
            return;
        }
        //get parsed creature
        try{
            Creature c = CreatureFactory.parseCreature(text);
            //System.out.println("parsed creature: " + c);
            c.setFacingRight(facingRight);
            setCreature(c);
            textField.setForeground(Color.BLACK);
            //System.out.println(c);
        }
        catch(Exception e){
            //System.out.println("error parsing " + text);
            textField.setForeground(Color.RED);
            return;
        }
        
        
        //sync
        
    }

    public void clear() {
        setCreature(null);
        textField.setText("");
    }
    
}
