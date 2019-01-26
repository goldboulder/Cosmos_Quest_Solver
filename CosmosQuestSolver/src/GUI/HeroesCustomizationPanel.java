/*

 */
package GUI;

import AI.AISolver;
import Formations.CreatureFactory;
import Formations.Hero;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class HeroesCustomizationPanel extends JPanel{
    
    private ISolverFrame frame;
    
    
    private HeroCustomizationPanel[] heroPanelArray;
    private HashMap<String,HeroCustomizationPanel> map;//for finding the right hero panel when loading

    public HeroesCustomizationPanel(ISolverFrame frame, int numColumns, boolean facingRight, boolean includePrioritize) {//reference solver, not frame?***
        this.frame = frame;
        
        map = new HashMap<>();
        
        setLayout(new GridLayout(0,numColumns));
        
        Hero[] heroes = CreatureFactory.getHeroes(CreatureFactory.getOrderType(true));
        heroPanelArray = new HeroCustomizationPanel[heroes.length];
        for (int i = 0; i < heroPanelArray.length; i++){
            heroes[i].setFacingRight(facingRight);
            heroPanelArray[i] = new HeroCustomizationPanel(frame,heroes[i],includePrioritize);
            add(heroPanelArray[i]);
            map.put(heroes[i].getName(),heroPanelArray[i]);
        }
        
        int height = (AssetPanel.CREATURE_PICTURE_SIZE + HeroCustomizationPanel.CHANGE_PANEL_SIZE) * (int)(Math.ceil((heroPanelArray.length - 1) / numColumns));
        setPreferredSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setMaximumSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        //setOpaque(false);
        
    }

    public void disableAll() {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setHeroEnabled(false);
        }
    }
    
    public void deprioritizeAll(){
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setPrioritizeHero(false);
        }
    }

    public void setLevelAll(int level) {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setLevel(level);
        }
    }
    
    public void promoteAll(int promoteLevel) {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setPromoteLevel(promoteLevel);
        }
    }
    
    public void writeLevelString(PrintWriter file) {
        for (int i = 0; i < heroPanelArray.length - 1; i++){
            heroPanelArray[i].writeHeroLevelString(file);
            file.print("\n");
        }
        heroPanelArray[heroPanelArray.length - 1].writeHeroLevelString(file);
    }

    public void writeSelectString(PrintWriter file) {
        for (int i = 0; i < heroPanelArray.length - 1; i++){
            heroPanelArray[i].writeHeroSelectString(file);
            file.print("\n");
        }
        heroPanelArray[heroPanelArray.length - 1].writeHeroSelectString(file);
    }
/*
    public void setHeroStats(String token, int level, boolean heroEnabled, boolean heroPrioritized) {
        HeroCustomizationPanel p = map.get(token);
        if (p != null){
            p.setLevel(level);
            p.setHeroEnabled(heroEnabled);
            p.setPrioritizeHero(heroPrioritized);
        }
    }
   */ 
    public void setHeroLevel(String name, int level){
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setLevel(level);
        }
    }
    
    
    
    public void setHeroPromoteLevel(String name, int promoteLevel) {
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setPromoteLevel(promoteLevel);
        }
    }
    
    public void setHeroSelect(String name, boolean enabled, boolean prioritized){
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setHeroEnabled(enabled);
            p.setPrioritizeHero(prioritized);
        }
    }
    
    public Hero getHero(String name) {
        return map.get(name).getHero();
    }
    
    public Hero[] getHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    
    public Hero[] getHeroesWithoutPrioritization() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled() && !heroPanelArray[i].heroPrioritized()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    
    public Hero[] getPrioritizedHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroPrioritized()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    
    public static Hero[] convertToArray(LinkedList<Hero> heroes){
        int i = 0;
        Hero[] ans = new Hero[heroes.size()];
        for (Hero h : heroes){
            ans[i] = h;//already copied
            i ++;
        }
        return ans;
    }

    public boolean heroEnabled(String heroName) {
        return map.get(heroName).heroEnabled();
    }

    public boolean heroPrioritized(String hName) {
        return map.get(hName).heroPrioritized();
    }

    public LinkedList<Hero> getEnabledHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        return heroes;
    }

    

    

    

    

    
    
    
    
}
