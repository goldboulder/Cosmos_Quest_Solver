/*

 */
package GUI;

import Formations.Hero;
import Formations.WorldBoss;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//lets you select the enemy boss and set their level
public class EnemyBossCustomizationPanel extends JPanel{

    private EnemySelectFrame frame;
    private WorldBoss boss;
    
    
    private CreaturePicturePanel picturePanel;
    //private JPanel editPanel;
    
    //private JLabel levelLabel;
    //private JTextField levelTextField;
    
    public static final int CHANGE_PANEL_SIZE = 30;
    
    public EnemyBossCustomizationPanel(EnemySelectFrame frame, WorldBoss boss){
        this.frame = frame;
        
        this.boss = boss;
        
        
        picturePanel = new CreaturePictureSelectionPanel(frame,boss);
        //editPanel = new JPanel();
        
        //levelLabel = new JLabel("Lvl");
        //levelTextField = new JTextField("1");
        
        //setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.X_AXIS));
        
        
        //editPanel.add(levelLabel);
        //editPanel.add(levelTextField);
        
        add(picturePanel);
        //add(editPanel);
        
        picturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        //addMouseMotionListener(enemyFormationMakerPanel);
        //this.setCursor(new Cursor());
        //setBackground(Color.BLUE);
        //levelTextField.getDocument().addDocumentListener(this);
        setOpaque(false);
        
    }

    
    
    
    
    
}
