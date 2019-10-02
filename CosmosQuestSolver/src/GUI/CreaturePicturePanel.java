/*

 */
package GUI;

import Formations.Creature;
import Skills.Nothing;
import Skills.Skill;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Skills.RuneSkill;

// panel for drawing creatures
public class CreaturePicturePanel extends JPanel implements RuneHolder,MouseListener{
    
    private ParameterListener frame;
    protected Creature creature;
    protected boolean adjustRunes;
    public static final Color BORDER_COLOR = new Color(30,170,220);
    public Skill runeSkill;
    
    public static final int OCCUPIED_RUNE_SIZE = 30;
    public static final int FREE_RUNE_SIZE = 100;
    
    public CreaturePicturePanel(ParameterListener frame,Creature creature, boolean adjustRunes) {
        this.frame = frame;
        this.creature = creature;
        runeSkill = new Nothing(creature);
        //setBackground(Color.DARK_GRAY);
        setOpaque(false);
        this.adjustRunes = adjustRunes;
        
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
        
        //draw rune skill
        if (adjustRunes && !(runeSkill instanceof Nothing)){
            drawRuneSkill(g,(RuneSkill) runeSkill);
        }
        if (!adjustRunes && creature != null && !(creature.getRuneSkill() instanceof Nothing)){
            drawRuneSkill(g,(RuneSkill) creature.getRuneSkill());
        }
        
        
        g.setColor(BORDER_COLOR);
        g.drawRect(0, 0, AssetPanel.CREATURE_PICTURE_SIZE-1, AssetPanel.CREATURE_PICTURE_SIZE-1);
    }
    
    private void drawRuneSkill(Graphics g, RuneSkill n){
            BufferedImage image = ImageFactory.getPicture("Skills/" + n.getImageName());
            if (creature == null){
                g.drawImage(image, (AssetPanel.CREATURE_PICTURE_SIZE - FREE_RUNE_SIZE)/2, (AssetPanel.CREATURE_PICTURE_SIZE - FREE_RUNE_SIZE)/2, FREE_RUNE_SIZE, FREE_RUNE_SIZE, null);
            }
            else if (creature.isFacingRight()){
                g.drawImage(image, AssetPanel.CREATURE_PICTURE_SIZE - OCCUPIED_RUNE_SIZE, 0, OCCUPIED_RUNE_SIZE, OCCUPIED_RUNE_SIZE, null);
            }
            else{
                g.drawImage(image, 0, 0, OCCUPIED_RUNE_SIZE, OCCUPIED_RUNE_SIZE, null);
            }
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
        if (e.getButton() == MouseEvent.BUTTON3 && adjustRunes){//right click for runes
            displayRunePanel();
        }
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
            if (adjustRunes){
                if ((runeSkill instanceof Nothing)){
                    setToolTipText(creature.toolTipText());
                }
                else{
                    StringBuilder sb = new StringBuilder(creature.toolTipText());
                    sb.delete(sb.length() - 7, sb.length());
                    sb.append("<br>").append(runeSkill.getDescription()).append("</html>");
                    setToolTipText(sb.toString());
                }
            }
            else{
                if (creature.getRuneSkill() instanceof Nothing){
                    setToolTipText(creature.toolTipText());
                }
                else{
                    StringBuilder sb = new StringBuilder(creature.toolTipText());
                    sb.delete(sb.length() - 7, sb.length());
                    sb.append("<br>").append(creature.getRuneSkill().getDescription()).append("</html>");
                    setToolTipText(sb.toString());
                }
            }
        }
        else{
            if ((!(runeSkill instanceof Nothing))){
                setToolTipText(runeSkill.getDescription());
            }
        }

    }
    
    private String addRuneText(String str){
        StringBuilder sb = new StringBuilder(str);
        sb.delete(sb.length() - 7, sb.length());
        sb.append("<br>").append(runeSkill.getDescription()).append("</html>");
        return sb.toString();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setToolTipText("");
    }

    @Override
    public void setRuneSkill(Skill skill) {
        this.runeSkill = (Skill) skill;
        repaint();
        //frame.parametersChanged();
    }

    @Override
    public Skill getRuneSkill() {
        return (Skill) runeSkill;
    }
    
    private void displayRunePanel() {
        JDialog dialog = new JDialog((JFrame)frame, "Rune", true);//getId?
        dialog.setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/Rune Selecter Background"), 0, 0, dialog.getWidth(), dialog.getHeight(), null);
            }
        };
        
        backgroundPanel.add(new RuneSelecterPanel(dialog, this));
        
        dialog.getContentPane().add(backgroundPanel);
        //centerScreen(dialog);
        
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void parametersChanged() {
        frame.parametersChanged();
    }

    
}
