/*

 */
package GUI;

import Formations.Formation;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Hero;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//displays the enemy formation
public class EnemyFormationPanel extends JPanel implements CreaturePanelGroup{
    
    private EnemySelectFrame frame;
    private boolean facingRight;
    private EnemyFormationMakerPanel enemyFormationMakerPanel;
    
    private EnemyFormationSinglePanel[] panels;
    
    public EnemyFormationPanel(EnemySelectFrame frame, EnemyFormationMakerPanel enemyFormationMakerPanel, boolean facingRight) {
        this.frame = frame;
        this.facingRight = facingRight;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        
        panels = new EnemyFormationSinglePanel[Formation.MAX_MEMBERS];
        if (facingRight){//condense into method?
            for (int i = panels.length - 1; i >= 0; i--){
                panels[i] = new EnemyFormationSinglePanel(frame,this,null,facingRight,true);
                add(panels[i]);
                panels[i].setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
            }
        }
        else{
            for (int i = 0; i < panels.length; i++){
                panels[i] = new EnemyFormationSinglePanel(frame,this,null,facingRight,true);
                add(panels[i]);
                panels[i].setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
            }
        }
        
        
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        setOpaque(false);
        
        
    }
    
    public void setFormation(Formation f){
        int memberNum = 0;
        
        
        for (int i = Formation.MAX_MEMBERS - f.size(); i < Formation.MAX_MEMBERS; i++){
            panels[i].setCreature(f.getEntry(memberNum));
            memberNum ++;
        }

        //remove spots if formation is not full
        for (int i = Formation.MAX_MEMBERS - f.size() - 1; i >= 0; i--){
            panels[i].setCreature(null);
        }
        
        frame.parametersChanged();
        repaint();
    }
    
    public void setFormation(Creature[] creatures){
        for (int i = 0; i < Formation.MAX_MEMBERS; i ++){
            panels[i].setCreature(creatures[i]);
        }
        revalidate();
        repaint();
    }
    
    //updateFormation if solver is not busy

    public void clear() {
        for (int i = 0; i < panels.length; i++){
            panels[i].setCreature(null);
        }
        frame.parametersChanged();
        repaint();
    }
    

    public Formation getEnemyFormation() {
        return new Formation(getEnemyArray());
    }
    
    public Creature[] getEnemyArray() {
        Creature[] creatures = new Creature[Formation.MAX_MEMBERS];
        for (int i = 0; i < panels.length; i++){
            creatures[i] = panels[i].getCreature();
        }
        return creatures;
    }
    
    public void load(){
        setFormation(CreatureFactory.loadFormation("save data/quest formation data",facingRight));
        syncHeroes();
        frame.parametersChanged();
    }
    
    //makes the heroes loaded in reference the same heroes in memory as the ones in the hero selection panel
    //this way, they change levels when you change the other's levels
    public void syncHeroes(){
        for (int i = 0; i < panels.length; i++){
            if (panels[i].getCreature() instanceof Hero){
                Hero h = (Hero)panels[i].getCreature();
                enemyFormationMakerPanel.setHeroLevel(h.getName(),h.getLevel());
                enemyFormationMakerPanel.setHeroPromotionLevel(h.getName(),h.getPromoteLevel());
                panels[i].setCreature(enemyFormationMakerPanel.getHero(h.getName()));
            }
        }
    }

    @Override
    public void removeCreature(String name) {
        for (int i = 0; i < panels.length; i++){
            if (panels[i].getCreature() != null && panels[i].getCreature().getName().equals(name)){
                panels[i].setCreature(null);
            }
        }
    }

    @Override
    public boolean containsCreature(String name) {
        for (int i = 0; i < panels.length; i++){
            if (panels[i].getCreature() != null && panels[i].getCreature().getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    
    
}
