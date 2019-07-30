/*

 */
package GUI;

import Formations.Creature;
import Skills.NodeSkill;
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

// panel for drawing creatures
public class CreaturePicturePanel extends JPanel implements NodeHolder,MouseListener{
    
    private ParameterListener frame;
    protected Creature creature;
    protected boolean adjustNodes;
    public static final Color BORDER_COLOR = new Color(30,170,220);
    public Skill nodeSkill;
    
    public static final int NODE_SIZE = 15;
    
    public CreaturePicturePanel(ParameterListener frame,Creature creature, boolean adjustNodes) {
        this.frame = frame;
        this.creature = creature;
        nodeSkill = new Nothing(creature);
        //setBackground(Color.DARK_GRAY);
        setOpaque(false);
        this.adjustNodes = adjustNodes;
        
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
        
        //draw node skill
        if (adjustNodes && !(nodeSkill instanceof Nothing)){
            drawNodeSkill(g,(NodeSkill) nodeSkill);
        }
        if (!adjustNodes && creature != null && !(creature.getNodeSkill() instanceof Nothing)){
            drawNodeSkill(g,(NodeSkill) creature.getNodeSkill());
        }
        
        
        g.setColor(BORDER_COLOR);
        g.drawRect(0, 0, AssetPanel.CREATURE_PICTURE_SIZE-1, AssetPanel.CREATURE_PICTURE_SIZE-1);
    }
    
    private void drawNodeSkill(Graphics g, NodeSkill n){
            BufferedImage image = ImageFactory.getPicture("Skills/" + n.getImageName());
            if (creature == null){
                g.drawImage(image, (AssetPanel.CREATURE_PICTURE_SIZE - NODE_SIZE)/2, (AssetPanel.CREATURE_PICTURE_SIZE - NODE_SIZE)/2, NODE_SIZE, NODE_SIZE, null);
            }
            else if (creature.isFacingRight()){
                g.drawImage(image, AssetPanel.CREATURE_PICTURE_SIZE - NODE_SIZE - 3, (AssetPanel.CREATURE_PICTURE_SIZE - NODE_SIZE)/2 + 14, NODE_SIZE, NODE_SIZE, null);
            }
            else{
                g.drawImage(image, 3, (AssetPanel.CREATURE_PICTURE_SIZE - NODE_SIZE)/2 + 14, NODE_SIZE, NODE_SIZE, null);
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
        if (e.getButton() == MouseEvent.BUTTON3 && adjustNodes){//right click for nodes
            displayNodePanel();
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
            if (adjustNodes){
                if ((nodeSkill instanceof Nothing)){
                    setToolTipText(creature.toolTipText());
                }
                else{
                    StringBuilder sb = new StringBuilder(creature.toolTipText());
                    sb.delete(sb.length() - 7, sb.length());
                    sb.append("<br>").append(nodeSkill.getDescription()).append("</html>");
                    setToolTipText(sb.toString());
                }
            }
            else{
                if (creature.getNodeSkill() instanceof Nothing){
                    setToolTipText(creature.toolTipText());
                }
                else{
                    StringBuilder sb = new StringBuilder(creature.toolTipText());
                    sb.delete(sb.length() - 7, sb.length());
                    sb.append("<br>").append(creature.getNodeSkill().getDescription()).append("</html>");
                    setToolTipText(sb.toString());
                }
            }
        }
        else{
            if ((!(nodeSkill instanceof Nothing))){
                setToolTipText(nodeSkill.getDescription());
            }
        }

    }
    
    private String addNodeText(String str){
        StringBuilder sb = new StringBuilder(str);
        sb.delete(sb.length() - 7, sb.length());
        sb.append("<br>").append(nodeSkill.getDescription()).append("</html>");
        return sb.toString();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setToolTipText("");
    }

    @Override
    public void setNodeSkill(Skill skill) {
        this.nodeSkill = (Skill) skill;
        repaint();
        //frame.parametersChanged();
    }

    @Override
    public Skill getNodeSkill() {
        return (Skill) nodeSkill;
    }
    
    private void displayNodePanel() {
        JDialog dialog = new JDialog((JFrame)frame, "Node", true);//getId?
        dialog.setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/Node Selecter Background"), 0, 0, dialog.getWidth(), dialog.getHeight(), null);
            }
        };
        
        backgroundPanel.add(new NodeSelecterPanel(dialog, this));
        
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
