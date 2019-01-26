/*

 */
package GUI;

import Formations.CreatureFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;

//lets you choose which ElementMonsterSelectionPanel to show
public class ElementSelectorPanel extends JPanel{
    
    private EnemySelectFrame frame;
    
    private MonsterSelectionPanel monsterSelectionPanel;
    
    private JButton airButton;
    private JButton waterButton;
    private JButton earthButton;
    private JButton fireButton;
    
    private static final Color BACKGROUND_COLOR = new Color(200,150,100);

    public ElementSelectorPanel(EnemySelectFrame frame,MonsterSelectionPanel monsterSelectionPanel) {
        this.frame = frame;
        this.monsterSelectionPanel = monsterSelectionPanel;
        
        airButton = new JButton(new ImageIcon(ImageFactory.getPicture("Others/Air Symbol")));
        waterButton = new JButton(new ImageIcon(ImageFactory.getPicture("Others/Water Symbol")));
        earthButton = new JButton(new ImageIcon(ImageFactory.getPicture("Others/Earth Symbol")));
        fireButton = new JButton(new ImageIcon(ImageFactory.getPicture("Others/Fire Symbol")));
        
        airButton.addActionListener(monsterSelectionPanel);
        waterButton.addActionListener(monsterSelectionPanel);
        earthButton.addActionListener(monsterSelectionPanel);
        fireButton.addActionListener(monsterSelectionPanel);
        
        airButton.setActionCommand("Air");
        waterButton.setActionCommand("Water");
        earthButton.setActionCommand("Earth");
        fireButton.setActionCommand("Fire");
        
        airButton.setBackground(BACKGROUND_COLOR);
        waterButton.setBackground(BACKGROUND_COLOR);
        earthButton.setBackground(BACKGROUND_COLOR);
        fireButton.setBackground(BACKGROUND_COLOR);
        
        add(earthButton);
        add(airButton);
        add(fireButton);
        add(waterButton);
        
        setLayout(new GridLayout(2,2,1,1));
        //setOpaque(false);
        //addMouseListener(frame);
        //addMouseMotionListener(frame);
        //airButton.addMouseListener(frame);
        //airButton.addMouseMotionListener(frame);
        
    }
    /*
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(CreatureFactory.getPicture("Others/"), WIDTH, WIDTH, WIDTH, HEIGHT, this)
    }
*/
    
}
