/*

 */
package GUI;

import AI.AISolver;
import AI.NoHeroesLessSpacesWBOptimizer;
import AI.WorldBossOptimizer;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.WorldBoss;
import GUI.HeroCustomizationPanel.Priority;
import Skills.Skill;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

// gui for world boss damage optimization
public class WorldBossOptimizerFrame extends JFrame implements ISolverFrame{
    private JPanel backgroundPanel;
    private JPanel topPanel;
    private AssetPanel assetPanel;
    private WorldBossSelectionPanel worldBossSelectionPanel;
    private CalculationPanel calculationPanel;
    
    private static final int CALCULATION_PANEL_HEIGHT = 100;
    
    public WorldBossOptimizerFrame(){
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        
        topPanel = new JPanel();
        assetPanel = new AssetPanel(this,true,true,false);
        worldBossSelectionPanel = new WorldBossSelectionPanel(this);
        calculationPanel = new CalculationPanel(this);
        
        topPanel.add(assetPanel);
        topPanel.add(worldBossSelectionPanel);
        backgroundPanel.add(topPanel);
        backgroundPanel.add(calculationPanel);
        add(backgroundPanel);
        
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        
        topPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        topPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        backgroundPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        worldBossSelectionPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        assetPanel.setPreferredSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        assetPanel.setMaximumSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        calculationPanel.setPreferredSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        calculationPanel.setMinimumSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        
        getContentPane().setBackground(Color.YELLOW);
        //topPanel.setBackground(Color.YELLOW);
        //worldBossSelectionPanel.setBackground(Color.GREEN);
        //calculationPanel.setBackground(Color.BLUE);
        topPanel.setOpaque(false);
        worldBossSelectionPanel.setOpaque(false);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
        
        setTitle("World Boss Optimizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        
        
    }
    /*
    @Override
    public void recieveSolution(Formation f){
        if (calculationPanel.isSearching() || f.isEmpty()){
            long damage = Formation.damageDealt(f.getCopy(), worldBossSelectionPanel.getBossFormation());
            
            if (damage > worldBossSelectionPanel.getDamage()){
                worldBossSelectionPanel.recieveSolution(f);
                calculationPanel.updateSolutionDetails(f,worldBossSelectionPanel.getBossFormation());
            }
        }
    }
*/
    
    @Override
    public void recieveSolution(Formation f){//race condition for LOC NH!
        if (calculationPanel.isSearching() || f.isEmpty()){
            
            worldBossSelectionPanel.recieveSolution(f);
            
            calculationPanel.updateSolutionDetails(f,worldBossSelectionPanel.getBossFormation());
        }
    }
    
    public void recieveBlankSpaceSolution(LinkedList<Creature> creatureList, LinkedList<Integer> blankSpaces, boolean hasRunes){//race condition for LOC NH!
        
            
        worldBossSelectionPanel.recieveSolution(new Formation(creatureList,blankSpaces).getCreatureArray());
        Formation f = new Formation(creatureList,blankSpaces);
        if (hasRunes){
            f.addRuneSkills(this.getYourRunes());
        }
        calculationPanel.updateSolutionDetails(f,worldBossSelectionPanel.getBossFormation());
    }
    /*
    @Override
    public void recieveSolution(Formation f){
        solutionFormationPanel.updateFormation(f,false);
        
        if (!f.isEmpty()){//called by calculationPanel to clearRunes solution. not really a solution
            calculationPanel.recieveSolutionFound();
            
            calculationPanel.updateSolutionDetails(f, enemyFormationMakerPanel.getEnemyFormation());
        }
    }
    
    public void recieveBlankSpaceSolution(LinkedList<Creature> creatureList, LinkedList<Integer> blankSpaces, boolean hasRunes) {
        solutionFormationPanel.updateFormation(Formation.listBlankSpacesToArray(creatureList, blankSpaces));
        Formation f = new Formation(creatureList,blankSpaces);
        if (hasRunes){
            f.addRuneSkills(this.getYourRunes());
        }
        if (!creatureList.isEmpty()){//called by calculationPanel to clearRunes solution. not really a solution
            calculationPanel.recieveSolutionFound();
            calculationPanel.updateSolutionDetails(f, enemyFormationMakerPanel.getEnemyFormation());
        }
    }
*/

    @Override
    public long getFollowers() {
        return assetPanel.getFollowers();
    }

    @Override
    public int getMaxCreatures() {
        return assetPanel.getMaxCreatures();
    }

    @Override
    public Hero[] getHeroes() {
        return assetPanel.getHeroes();
    }
    
    @Override
    public Hero[] getHeroes(Priority p){
        return assetPanel.getHeroes(p);
    }
    
    @Override
    public Priority getHeroPriority(String name){
        return assetPanel.getPriority(name);
    }
    /*
    public Hero[] getHeroesWithoutPrioritization() {
        return assetPanel.getHeroesWithoutPrioritization();
    }
    
    public Hero[] getPrioritizedHeroes() {
        return assetPanel.getPrioritizedHeroes();
    }
*/

    public WorldBoss getBoss() {
        try{
            return (WorldBoss) worldBossSelectionPanel.getBoss().getCopy();
        }
        catch(NullPointerException e){
            return CreatureFactory.getDefaultBoss();
        }
    }

    @Override
    public void recieveDone() {
        calculationPanel.recieveDone();
    }

    @Override
    public AISolver makeSolver() {
        WorldBoss b = worldBossSelectionPanel.getBoss();
        if (b.getMainSkill().WBTryLessCreatures() && assetPanel.getEnabledHeroes().isEmpty() || (!b.getMainSkill().WBNHEasy() && worldBossSelectionPanel.hasRunes())){
            return new NoHeroesLessSpacesWBOptimizer(this);
        }
        else{
            return new WorldBossOptimizer(this);
        }
        
    }

    @Override
    public void setVisible(Boolean b) {
        super.setVisible(b);
    }
    
    @Override
    public String getDoneMessage() {
        return "Done";
    }
    
    @Override
    public String getSolutionMessage() {
        return "Solution updated";
    }
    
    public long getBestDamage(){
        return worldBossSelectionPanel.getDamage();
    }
    
    @Override
    public void recieveDamageOfBattle(long damage) {
        if (calculationPanel.isSearching() || damage == 0){
            worldBossSelectionPanel.setDamage(damage);
        }
    }
    
    @Override
    public void backToMenuAction() {
        //nothing
    }
    
    @Override
    public void parametersChanged() {
        calculationPanel.recieveStopSearching();
        calculationPanel.parametersChanged();
        worldBossSelectionPanel.parametersChanged();
    }
    
    @Override
    public void recieveProgressString(String text) {
        calculationPanel.recieveProgressString(text);//if parameters changed without stopping, a thread can submit a formation before it stops***
    }
    
    @Override
    public void recieveMessageString(String text) {
        if (calculationPanel != null){
            calculationPanel.setMessageString(text);
        }
    }
    
    @Override
    public void recieveCreatureList(LinkedList<Creature> list) {
        calculationPanel.recieveCreatureList(list);
    }
    
    @Override
    public void recieveStart() {
        
    }
    
    @Override
    public boolean showViewButton() {
        return true;
    }

    @Override
    public String getSelectSource() {
        //return "save data/hero boss select data.txt";
        return "save data/hero " + getBoss().getNickName() + " select data.txt";
    }
    
    @Override
    public String getSavePartMessage() {
        return getBoss().getName();
    }
    
    @Override
    public void filterHeroes(String text) {
        assetPanel.filterHeroes(text);
    }
    
    //@Override
    //public boolean canPauseThread() {
        //return false;
    //}

    public Skill[] getYourRunes() {
        return worldBossSelectionPanel.getRunes();
    }

    boolean hasHeroesEnabled() {
        return !assetPanel.getEnabledHeroes().isEmpty();
    }

    
    
}
