/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Hero;
import static GUI.HeroCustomizationPanel.CHANGE_PANEL_SIZE;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//lets you select the enemy hero and set their promoteLevel
public class EnemyHeroCustomizationPanel extends JPanel implements DocumentListener{

    private EnemySelectFrame frame;
    private Hero hero;
    EnemyFormationMakerPanel enemyFormationMakerPanel;
    
    private CreaturePicturePanel picturePanel;
    private JPanel editPanel;
    
    //private JLabel levelLabel;
    private JTextField levelTextField;
    private JTextField promoteLevelTextField;
    
    public static final int CHANGE_PANEL_SIZE = 22;
    
    public EnemyHeroCustomizationPanel(EnemySelectFrame frame, EnemyFormationMakerPanel enemyFormationMakerPanel, Hero hero){//load heroes?
        this.frame = frame;
        this.hero = hero;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        
        
        
        picturePanel = new CreaturePictureSelectionPanel(frame,hero);
        editPanel = new JPanel();
        
        //levelLabel = new JLabel("Lvl");
        levelTextField = new JTextField("1");
        promoteLevelTextField = new JTextField("0");
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.X_AXIS));
        
        
        //editPanel.add(levelLabel);
        editPanel.add(levelTextField);
        editPanel.add(promoteLevelTextField);
        editPanel.add(Box.createHorizontalStrut(3));
        
        add(picturePanel);
        add(editPanel);
        
        picturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        picturePanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        //addMouseMotionListener(enemyFormationMakerPanel);
        //this.setCursor(new Cursor());
        //setBackground(Color.BLUE);
        levelTextField.getDocument().addDocumentListener(this);
        promoteLevelTextField.getDocument().addDocumentListener(this);
        levelTextField.setColumns(1);
        levelTextField.setBackground(CreatureFactory.SourceToColor(CreatureFactory.IDToSource(hero.getID())));
        promoteLevelTextField.setBackground(CreatureFactory.SourceToColor(CreatureFactory.IDToSource(hero.getID())));
        //editPanel.setBackground(CreatureFactory.SourceToColor(CreatureFactory.IDToSource(hero.getID())));
        promoteLevelTextField.setColumns(1);
        promoteLevelTextField.setMaximumSize(new Dimension(10,CHANGE_PANEL_SIZE));
        editPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,CHANGE_PANEL_SIZE));
        
        
        setOpaque(false);
        
    }
    
    public JTextField getLevelTextField(){
        return levelTextField;
    }
    
    public JTextField getPromoteLevelTextField(){
        return promoteLevelTextField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();
    }
    
    public void levelHero(){//repeat code***
        
        //level 1000 exception
        if (levelTextField.getText().equals("1k") || levelTextField.getText().equals("1K")){
            hero.levelUp(1000);

            enemyFormationMakerPanel.repaint();
            levelTextField.setForeground(Color.BLACK);
            if (enemyFormationMakerPanel.getEnemyFormation().contains(hero)){
                frame.parametersChanged();
            }
            return;
        }
        
        try{
            int level = Integer.parseInt(levelTextField.getText());
            
            hero.levelUp(level);
            
            
            levelTextField.setForeground(Color.BLACK);
            enemyFormationMakerPanel.repaint();
            
            
            //hero must be level 99 to be promoted
            //if (level < Hero.MAX_NORMAL_LEVEL && hero.getPromoteLevel() != 0){
                //setPromoteLevel(0);
            //}
        }
        catch(Exception e){
            levelTextField.setForeground(Color.RED);
            
            //e.printStackTrace();
            
        }
        
        if (enemyFormationMakerPanel.getEnemyFormation().contains(hero)){
            frame.parametersChanged();
        }
        
        
        
    }
    
    public void promoteHero(){//repeat code***
        
        try{
            int promoteLevel = Integer.parseInt(promoteLevelTextField.getText());
            
            if (!Hero.validPromoteLevel(promoteLevel)){
                throw new Exception();
            }
            
            hero.promote(promoteLevel);
            
            enemyFormationMakerPanel.repaint();
            promoteLevelTextField.setForeground(Color.BLACK);
            
            //promoted heroes must be at least level 99
            //if (promoteLevel !=  0 && hero.getLevel() < Hero.MAX_NORMAL_LEVEL){
                //setLevel(Hero.MAX_NORMAL_LEVEL);
            //}
        }
        catch(Exception e){
            promoteLevelTextField.setForeground(Color.RED);
        }
        
        if (enemyFormationMakerPanel.getEnemyFormation().contains(hero)){
            frame.parametersChanged();
        }
        
    }

    public void setLevel(int level) {
        levelTextField.setText(Integer.toString(level));
        levelTextField.setForeground(Color.BLACK);
        //levelHero();
    }
    
    public void setPromoteLevel(int promoteLevel) {
        //System.out.println(promoteLevel);
        promoteLevelTextField.setText(Integer.toString(promoteLevel));
        promoteLevelTextField.setForeground(Color.BLACK);
        //promoteHero();
    }

    public Hero getHero() {
        return hero;
    }

    public void updateTextFields() {
        int pLevel = hero.getPromoteLevel();
        //System.out.println("Level: " + hero.getLevel() + "   Promote: " + hero.getPromoteLevel());
        setLevel(hero.getLevel());//why is this changing the promote level?
        //System.out.println("sss " + hero.getPromoteLevel());
        setPromoteLevel(pLevel);
    }

    
    
    
}
