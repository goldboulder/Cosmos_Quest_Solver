/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

// gui for making a formation for the quest solver to find a solution to
public class EnemyFormationMakerPanel extends JPanel implements ActionListener, MouseListener{
    
    private EnemySelectFrame frame;
    
    private JLabel titleLabel;
    private JPanel enemyFormationAndButtonPanel;
    private boolean facingRight;
    
    private JButton clearFormationButton;
    private EnemyFormationPanel enemyFormationPanel;
    private MonsterSelectionPanel monsterSelectionPanel;
    private EnemyHeroesCustomizationPanel enemyHeroesCustomizationPanel;
    private JScrollPane enemyHeroesCustomizationScrollPane;
    
    
    
    public static final int MONSTER_SCROLL_VIEW_AMOUNT = 5;
    
    public EnemyFormationMakerPanel(EnemySelectFrame frame, String title, boolean facingRight, boolean loadHeroes, boolean loadQuest, boolean includeBosses, boolean includeQuests){
        this.frame = frame;
        this.facingRight = facingRight;
        
        titleLabel = new JLabel(title);
        enemyFormationAndButtonPanel = new JPanel();
        clearFormationButton = new JButton("Clear");
        enemyFormationPanel = new EnemyFormationPanel(frame,this,facingRight);
        monsterSelectionPanel = new MonsterSelectionPanel(frame,this,facingRight,includeQuests);
        enemyHeroesCustomizationPanel = new EnemyHeroesCustomizationPanel(frame,this,AssetPanel.HERO_SELECTION_COLUMNS,facingRight,includeBosses,loadHeroes);
        enemyHeroesCustomizationScrollPane = new JScrollPane(enemyHeroesCustomizationPanel);
        
        enemyFormationAndButtonPanel.add(enemyFormationPanel);
        enemyFormationAndButtonPanel.add(clearFormationButton);
        
        add(titleLabel);
        add(enemyFormationAndButtonPanel);
        add(monsterSelectionPanel);
        add(enemyHeroesCustomizationScrollPane);
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        titleLabel.setFont(AssetPanel.TITLE_FONT);
        
        enemyFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        enemyFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        enemyFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        monsterSelectionPanel.setPreferredSize(new Dimension(QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_WIDTH - 40,AssetPanel.CREATURE_PICTURE_SIZE + (Integer)UIManager.get("ScrollBar.width")));//onstants?**
        monsterSelectionPanel.setMaximumSize(new Dimension(QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_WIDTH - 40,AssetPanel.CREATURE_PICTURE_SIZE + (Integer)UIManager.get("ScrollBar.width")));
        monsterSelectionPanel.setMinimumSize(new Dimension(QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_WIDTH - 40,AssetPanel.CREATURE_PICTURE_SIZE + (Integer)UIManager.get("ScrollBar.width")));
        enemyHeroesCustomizationScrollPane.setPreferredSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH + 200,AssetPanel.HERO_SELECTION_PANEL_HEIGHT));
        enemyHeroesCustomizationScrollPane.setMaximumSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH + 200,AssetPanel.HERO_SELECTION_PANEL_HEIGHT));
        enemyHeroesCustomizationScrollPane.getVerticalScrollBar().setUnitIncrement(AssetPanel.SCROLL_BAR_SPEED);
        enemyHeroesCustomizationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        //enemyHeroesCustomizationPanel.setBackground(Color.ORANGE);
        //addKeyListener(this);
        //addMouseListener(this);
        //addMouseMotionListener(this);
        clearFormationButton.addActionListener(this);
        clearFormationButton.setActionCommand("clear formation");
        setOpaque(false);
        enemyFormationAndButtonPanel.setOpaque(false);
        if (loadQuest){
            enemyFormationPanel.load();
        }
        
    }
    
    public void setQuest(int questNum) {
        enemyFormationPanel.setFormation(CreatureFactory.loadFormation("quests/Quest " + Integer.toString(questNum),facingRight));
        enemyFormationPanel.syncHeroes();
    }

    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        frame.setMouseCreature(null);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("clear formation")){
            enemyFormationPanel.clear();
        }
    }

    public Formation getEnemyFormation() {
        return enemyFormationPanel.getEnemyFormation();
    }

    public void setHeroLevels(int level) {
        enemyHeroesCustomizationPanel.setLevelAll(level);
    }
    
    public void setHeroPromotionLevels(int promoteLevel) {
        enemyHeroesCustomizationPanel.setPromoteLevelAll(promoteLevel);
    }

    public void setHeroLevel(String name, int level) {
        enemyHeroesCustomizationPanel.setHeroLevel(name,level);
    }
    
    
    
    public void setHeroPromotionLevel(String name, int promoteLevel) {
        enemyHeroesCustomizationPanel.setHeroPromoteLevel(name,promoteLevel);
    }

    public Hero getHero(String name) {
        return enemyHeroesCustomizationPanel.getHero(name);
    }

    public void setDefaultLevels() {
        enemyHeroesCustomizationPanel.setDefaultLevels();
    }

    

    

    

    
    
    
    
    
}
