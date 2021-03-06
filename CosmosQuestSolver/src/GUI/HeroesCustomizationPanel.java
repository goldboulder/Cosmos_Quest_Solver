/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Hero;
import Formations.Hero.Rarity;
import static GUI.AssetPanel.CREATURE_PICTURE_SIZE;
import GUI.HeroCustomizationPanel.Priority;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HeroesCustomizationPanel extends JPanel implements HeroListPanel{
    
    private ISolverFrame frame;
    
    private final int NUM_COLUMNS;
    private HeroCustomizationPanel[] heroPanelArray;
    private HashMap<String,HeroCustomizationPanel> map;//for finding the right hero panel when loading
    private HashMap<String,HeroCustomizationPanel> shownMap;

    public HeroesCustomizationPanel(ISolverFrame frame, int numColumns, boolean facingRight, boolean includePrioritize) {//reference solver, not frame?***
        this.frame = frame;
        this.NUM_COLUMNS = numColumns;
        map = new HashMap<>();
        shownMap = new HashMap<>();
        
        setLayout(new GridLayout(0,numColumns));
        
        Hero[] heroes = CreatureFactory.getHeroes(CreatureFactory.getOrderType(true));
        heroPanelArray = new HeroCustomizationPanel[heroes.length];
        for (int i = 0; i < heroPanelArray.length; i++){
            heroes[i].setFacingRight(facingRight);
            heroPanelArray[i] = new HeroCustomizationPanel(frame,heroes[i],includePrioritize);
            //heroPanelArray[i].setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE+HeroCustomizationPanel.CHANGE_PANEL_SIZE-8));
            add(heroPanelArray[i]);
            map.put(heroes[i].getName(),heroPanelArray[i]);
            shownMap.put(heroes[i].getName(),heroPanelArray[i]);
        }
        
        int height = desiredHeight(heroPanelArray.length);
        setPreferredSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setMaximumSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setOpaque(false);
        
    }
    
    public int desiredHeight(int units){
        //int height = (AssetPanel.CREATURE_PICTURE_SIZE) * (int)((Math.ceil((heroPanelArray.length - 1) / numColumns)+1));
        return (AssetPanel.CREATURE_PICTURE_SIZE + HeroCustomizationPanel.CHANGE_PANEL_SIZE) * (int)((Math.ceil((units - 1) / NUM_COLUMNS)+1));
    }
    
    public void setPriority(Priority p){
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setPriority(p);
        }
    }
    
    public void disableAll() {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setHeroEnabled(false);
        }
    }
    /*
    public void deprioritizeAll(){
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setPrioritizeHero(false);
        }
    }
*/
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
    public void setHeroStats(String token, int level, boolean heroEnabled, boolean getPriority) {
        HeroCustomizationPanel p = map.get(token);
        if (p != null){
            p.setLevel(level);
            p.setHeroEnabled(heroEnabled);
            p.setPriority(getPriority);
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
    
    public void setHeroSelect(String name, boolean enabled, Priority priority){
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setHeroEnabled(enabled);
            p.setPriority(priority);
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
    
    public Hero[] getHeroes(Priority p) {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled() && heroPanelArray[i].getPriority() == p){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    /*
    public Hero[] getPrioritizedHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].getPriority()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    */
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
    
    public Priority getPriority(String heroName){
        return map.get(heroName).getPriority();
    }
/*
    public boolean getPriority(String hName) {
        return map.get(hName).getPriority();
    }
*/
    public LinkedList<Hero> getEnabledHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        return heroes;
    }

    private String filterText = "";
    @Override
    public String getFilterText(){
        return filterText;
    }
    
    @Override
    public void selectShown(){
        for (HeroCustomizationPanel p : heroPanelArray){
            if (shownMap.get(p.getHero().getName()) != null){
                p.setHeroEnabled(true);
            }
            else{
                p.setHeroEnabled(false);
            }
        }
    }
    
    public void filterHeroes(String text, FilterPanel.SourceFilter source, Rarity rarity, boolean includeSelected, boolean includeBosses) {
        filterText = text;
        String lowText = text.toLowerCase();
        removeAll();
        shownMap.clear();
        
        int numAdded = 0;
        for (HeroCustomizationPanel p : heroPanelArray){
            Hero h = p.getHero();
            if ((p.heroEnabled() || !includeSelected) && h.getName().toLowerCase().contains(lowText)
                    && (source == null || FilterPanel.sourceMatch(source,CreatureFactory.IDToSource(h.getID())))
                    && (rarity == null || h.getRarity() == rarity)){
                add(p);
                shownMap.put(p.getHero().getName(),p);
                numAdded ++;
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
        
        LinkedList<HeroCustomizationPanel> heroList = new LinkedList<>();
        heroList.addAll(Arrays.asList(heroPanelArray));
        Collections.sort(heroList, (HeroCustomizationPanel p1, HeroCustomizationPanel p2) -> p2.getHero().viability()-p1.getHero().viability());
        heroPanelArray = heroList.toArray(heroPanelArray);
        for (HeroCustomizationPanel p : heroPanelArray){
            add(p);
        }
        
        revalidate();
        repaint();
    }

    

    

    

    

    
    
    
    
}
