/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Hero;
import Formations.WorldBoss;
import static GUI.AssetPanel.CREATURE_PICTURE_SIZE;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnemyHeroesCustomizationPanel extends JPanel{
    
    private EnemySelectFrame frame;
    private EnemyFormationMakerPanel parent;
    
    private EnemyHeroCustomizationPanel[] heroPanelArray;
    private EnemyBossCustomizationPanel[] bossPanelArray;
    
    private boolean includeWorldBosses;
    
    private final int NUM_COLUMNS;
    
    private HashMap<String,EnemyHeroCustomizationPanel> map;//for finding the right hero panel when loading

    public EnemyHeroesCustomizationPanel(EnemySelectFrame frame, EnemyFormationMakerPanel parent, int numColumns, boolean facingRight, boolean includeBosses, boolean load) {//include boolean for world bosses?
        this.frame = frame;
        this.parent = parent;
        this.NUM_COLUMNS = numColumns;
        this.includeWorldBosses = includeBosses;
        
        setLayout(new GridLayout(0,numColumns));
        
        Hero[] heroes = CreatureFactory.getHeroes(CreatureFactory.getOrderType(false));
        heroPanelArray = new EnemyHeroCustomizationPanel[heroes.length];
        map = new HashMap<>();
        
        for (int i = 0; i < heroPanelArray.length; i++){
            heroes[i].setFacingRight(facingRight);
            heroPanelArray[i] = new EnemyHeroCustomizationPanel(frame,parent,heroes[i]);
            add(heroPanelArray[i]);
            map.put(heroes[i].getName(), heroPanelArray[i]);
        }
        if (includeBosses){
            WorldBoss[] bosses = CreatureFactory.getWorldBosses("ID");
            
            bossPanelArray = new EnemyBossCustomizationPanel[bosses.length];
            for (int i = 0; i < bossPanelArray.length; i++){
                bosses[i].setFacingRight(facingRight);
                bossPanelArray[i] = new EnemyBossCustomizationPanel(frame,bosses[i]);
                add(bossPanelArray[i]);
            }
        }
        
        int height = desiredHeight(heroPanelArray.length);
        setPreferredSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setMaximumSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setOpaque(false);
        
        if (load){
            load();
        }
        
        
    }
    
    public int desiredHeight(int units){
        //int height = (AssetPanel.CREATURE_PICTURE_SIZE) * (int)((Math.ceil((heroPanelArray.length - 1) / numColumns)+1));
        return (AssetPanel.CREATURE_PICTURE_SIZE + HeroCustomizationPanel.CHANGE_PANEL_SIZE) * (int)((Math.ceil((units - 1) / NUM_COLUMNS)+1));
    }
    
    private void load(){
        try{
            
            Scanner sc = new Scanner(new File("save data/hero level data.txt"));
            sc.nextLine();//followers
            sc.nextLine();//max creatures
            
            String[] tokens;
            while (sc.hasNext()){
                
                tokens = sc.nextLine().split(",");
                map.get(tokens[0]).getLevelTextField().setText(tokens[1]);
                map.get(tokens[0]).getPromoteLevelTextField().setText(tokens[2]);
                
                map.get(tokens[0]).levelHero();
            }
        }
        catch(Exception e){
            System.out.println("failed to load in EnemyHeroesCustomizationPanel");
        }
    }

    public void setLevelAll(int level) {
        for (EnemyHeroCustomizationPanel panel : heroPanelArray){
            panel.setLevel(level);
        }
    }
    
    public void setPromoteLevelAll(int promoteLevel) {
        for (EnemyHeroCustomizationPanel panel : heroPanelArray){
            panel.setPromoteLevel(promoteLevel);
        }
    }

    public void setHeroLevel(String name, int level) {
        map.get(name).setLevel(level);
    }
    
    public void setHeroPromoteLevel(String name, int promoteLevel) {
        map.get(name).setPromoteLevel(promoteLevel);
    }

    public Hero getHero(String name) {
        return map.get(name).getHero();
    }

    public void setDefaultLevels() {
        for (EnemyHeroCustomizationPanel panel : heroPanelArray){
            if (!parent.getEnemyFormation().contains(panel.getHero())){
                panel.setLevel(1000);
                panel.setPromoteLevel(0);
            }
        }
        if (CreatureFactory.getOrderType(false).equals("Strength")){
            sortByStrength();
        }
    }

    public void filterHeroes(String text) {
        String lowText = text.toLowerCase();
        removeAll();
        
        int numAdded = 0;
        for (EnemyHeroCustomizationPanel p : heroPanelArray){
            if (p.getHero().getName().toLowerCase().contains(lowText)){
                add(p);
                numAdded ++;
            }
        }
        if (bossPanelArray != null){
            for (EnemyBossCustomizationPanel p : bossPanelArray){
                if (p.getBoss().getName().toLowerCase().contains(lowText)){
                    add(p);
                    numAdded ++;
                }
            }
        }
        if (numAdded == 0){
            JLabel noHeroes = new JLabel("No heroes found");
            add(noHeroes);
            revalidate();
            repaint();
            return;
        }
        
        //fix demensions
        int width = AssetPanel.HERO_SELECTION_PANEL_WIDTH;
        if (numAdded < NUM_COLUMNS){
            width = CREATURE_PICTURE_SIZE * numAdded;
        }
        
        
        int height = desiredHeight(numAdded);
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
        
        revalidate();
        repaint();
    }
    
    public void sortByStrength() {
        removeAll();
        
        LinkedList<EnemyHeroCustomizationPanel> heroList = new LinkedList<>();
        heroList.addAll(Arrays.asList(heroPanelArray));
        Collections.sort(heroList, (EnemyHeroCustomizationPanel p1, EnemyHeroCustomizationPanel p2) -> p2.getHero().viability()-p1.getHero().viability());
        heroPanelArray = heroList.toArray(heroPanelArray);
        for (EnemyHeroCustomizationPanel p : heroPanelArray){
            add(p);
        }
        if (includeWorldBosses){
            for (EnemyBossCustomizationPanel p : bossPanelArray){
                add(p);
            }
        }
        
        
        revalidate();
        repaint();
    }

    public void redrawHero(String text) {
        EnemyHeroCustomizationPanel p = map.get(text);
        p.updateTextFields();
        p.repaint();
        
    }
    
    
    
}
