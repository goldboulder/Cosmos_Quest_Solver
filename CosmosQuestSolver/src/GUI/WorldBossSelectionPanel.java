/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.WorldBoss;
import Skills.Nothing;
import Skills.Skill;
import cosmosquestsolver.OtherThings;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//displays and lets you select a world boss
public class WorldBossSelectionPanel extends JPanel implements ActionListener{
    
    private WorldBossOptimizerFrame frame;
    
    private JPanel titlePanel;
    private JLabel worldBossLabel;
    private WorldBossPicturePanel worldBossPicturePanel;
    private JPanel buttonPanel;
    private JPanel solutionTitlePanel;
    private JLabel solutionLabel;
    private JPanel damagePanel;
    private JPanel solutionTextPanel;
    private JPanel followersTextPanel;
    private JButton analysisButton;
    private JLabel damageLabel;
    private long damage;
    private JLabel followersLabel;
    private SolutionFormationPanel solutionFormationPanel;
    private JPanel solutionPlusClearPanel;
    private JButton clearButton;
    
    private HashMap<String,WorldBoss> map;
    
    public static final int BOSS_DRAW_WIDTH = 512;
    public static final int BOSS_DRAW_HEIGHT = 450;
    
    public WorldBossSelectionPanel(WorldBossOptimizerFrame frame){
        this.frame = frame;
        
        titlePanel = new JPanel();
        worldBossLabel = new JLabel("World Boss",SwingConstants.CENTER);
        worldBossPicturePanel = new WorldBossPicturePanel();
        buttonPanel = new JPanel();
        solutionTitlePanel = new JPanel();
        solutionLabel = new JLabel("Solution");
        damagePanel = new JPanel();
        analysisButton = new JButton("AS / hit");
        analysisButton.addActionListener(this);
        analysisButton.setActionCommand("analysis");
        solutionTextPanel = new JPanel();
        followersTextPanel = new JPanel();
        damageLabel = new JLabel("Damage: 0");
        damage = 0;
        followersLabel = new JLabel(" ");
        solutionPlusClearPanel = new JPanel();
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        clearButton.setActionCommand("clear");
        solutionFormationPanel = new SolutionFormationPanel(frame,true,true);
        
        setBoss(CreatureFactory.getDefaultBoss());
        
        titlePanel.add(worldBossLabel);
        solutionTextPanel.add(Box.createRigidArea(new Dimension(300, 0)));
        solutionTextPanel.add(solutionLabel);
        damagePanel.add(Box.createRigidArea(new Dimension(215, 0)));
        damagePanel.add(analysisButton);
        damagePanel.add(Box.createRigidArea(new Dimension(12, 0)));
        damagePanel.add(damageLabel);
        followersTextPanel.add(Box.createRigidArea(new Dimension(300, 0)));
        followersTextPanel.add(followersLabel);
        solutionTitlePanel.add(solutionTextPanel);
        solutionTitlePanel.add(damagePanel);
        solutionTitlePanel.add(followersTextPanel);
        solutionPlusClearPanel.add(solutionFormationPanel);
        solutionPlusClearPanel.add(clearButton);
        add(titlePanel);
        add(worldBossPicturePanel);
        add(buttonPanel);
        add(solutionTitlePanel);
        add(solutionPlusClearPanel);
        
        initiateButtons();
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        damagePanel.setLayout(new BoxLayout(damagePanel,BoxLayout.X_AXIS));
        solutionTextPanel.setLayout(new BoxLayout(solutionTextPanel,BoxLayout.X_AXIS));
        followersTextPanel.setLayout(new BoxLayout(followersTextPanel,BoxLayout.X_AXIS));
        solutionTitlePanel.setLayout(new BoxLayout(solutionTitlePanel,BoxLayout.Y_AXIS));
        worldBossPicturePanel.setPreferredSize(new Dimension(BOSS_DRAW_WIDTH,BOSS_DRAW_HEIGHT));
        worldBossPicturePanel.setMaximumSize(new Dimension(BOSS_DRAW_WIDTH,BOSS_DRAW_HEIGHT));
        worldBossPicturePanel.setMinimumSize(new Dimension(BOSS_DRAW_WIDTH,BOSS_DRAW_HEIGHT));
        solutionFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        buttonPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        buttonPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        buttonPanel.setMinimumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        damagePanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        solutionTextPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        followersTextPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        
        worldBossLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        damageLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        followersLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        //worldBossLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //solutionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        titlePanel.setOpaque(false);
        solutionTextPanel.setOpaque(false);
        damagePanel.setOpaque(false);
        followersTextPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        solutionTitlePanel.setOpaque(false);
        solutionPlusClearPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionPlusClearPanel.setOpaque(false);
        //solutionFormationPanel.setOpaque(false);
    }
    
    public WorldBoss getBoss(){
        return worldBossPicturePanel.boss;
    }
    
    public Formation getBossFormation() {
        LinkedList<Creature> list = new LinkedList<>();
        list.add(getBoss().getCopy());
        return new Formation(list);
    }
    
    public void setBoss(WorldBoss boss){
        worldBossPicturePanel.setBoss(boss);
        worldBossLabel.setText(boss.getName());
        repaint();
    }
    
    private void initiateButtons(){
        map = new HashMap<>();
        for (WorldBoss boss : CreatureFactory.getWorldBosses("ID")){
            map.put(boss.getName(),boss);
            JButton button = new JButton(boss.getName());
            button.addActionListener(this);
            button.setActionCommand(boss.getName());
            buttonPanel.add(button);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("clear")){
            solutionFormationPanel.clearRunes();
            parametersChanged();
        }
        else if (e.getActionCommand().equals("analysis")){
            displayASPerHitPanel();
        }
        else{
            if (!worldBossPicturePanel.getBoss().getName().equals(e.getActionCommand())){
                frame.parametersChanged();
                setBoss(map.get(e.getActionCommand()));
            }
        }
        
        
    }

    public void setDamage(long damage) {
        this.damage = damage;
        damageLabel.setText("Damage: " + OtherThings.intToCommaString(damage));
        revalidate();
        repaint();
    }

    public void recieveSolution(Formation f) {
        solutionFormationPanel.updateFormation(f,true);
        long followersUsed = solutionFormationPanel.getFormation().getFollowers();
        if (followersUsed == 0){
            followersLabel.setText(" ");
        }
        else{
            followersLabel.setText("Followers Used: " + OtherThings.intToCommaString(followersUsed));
        }
        
        if (f.isEmpty()){//resets damage if clicking search again without changing anything
            setDamage(0);
        }
        
    }
    
    public void recieveSolution(Creature[] creatures) {
        solutionFormationPanel.updateFormation(creatures);
        long followersUsed = solutionFormationPanel.getFormation().getFollowers();
        if (followersUsed == 0){
            followersLabel.setText(" ");
        }
        else{
            followersLabel.setText("Followers Used: " + OtherThings.intToCommaString(followersUsed));
        }
        
        if (new Formation(creatures).isEmpty()){//resets damage if clicking search again without changing anything
            setDamage(0);
        }
        
    }

    public void parametersChanged() {
        solutionFormationPanel.updateFormation(new Formation(),true);
        damageLabel.setText("Damage: 0");
        setDamage(0);
        followersLabel.setText(" ");
    }

    public long getDamage() {
        return damage;
    }

    Skill[] getRunes() {
        return solutionFormationPanel.getRunes();
    }

    boolean hasRunes() {
        Skill[] runes = getRunes();
        for (int i = 0; i < runes.length; i ++){
            if (runes[i] != null && !(runes[i] instanceof Nothing)){
                return true;
            }
        }
        return false;
    }
    
    private void displayASPerHitPanel() {
        JDialog dialog = new JDialog((JFrame)frame, "AS / hit", true);//getId?
        dialog.setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, dialog.getWidth(), dialog.getHeight(), null);
            }
        };
        
        backgroundPanel.add(new WorldBossASPerHitPanel(dialog, damage, getBoss(), heroesEnabled()));
        
        dialog.getContentPane().add(backgroundPanel);
        //centerScreen(dialog);
        
        dialog.pack();
        dialog.setVisible(true);
    }
    
    private boolean heroesEnabled(){
        return frame.hasHeroesEnabled();
    }

    private class WorldBossPicturePanel extends JPanel{
        
        private WorldBoss boss;
        
        public WorldBossPicturePanel(){
            
        }
        
        public WorldBossPicturePanel(WorldBoss boss){
            this.boss = boss;
            setMouseOverText();
        }
        
        public WorldBoss getBoss(){
            return boss;
        }
        
        public void setBoss(WorldBoss boss){
            this.boss = boss;
            setMouseOverText();
            repaint();
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(ImageFactory.getPicture("Backgrounds/World Boss Background"), 0, 0, BOSS_DRAW_WIDTH, BOSS_DRAW_HEIGHT, null);
            if (boss != null){
                if (!boss.isFacingRight()){
                    g.drawImage(ImageFactory.getPicture(boss.getImageAddress()), BOSS_DRAW_WIDTH, 0, -BOSS_DRAW_WIDTH, BOSS_DRAW_HEIGHT, null);
                }
                else{
                    g.drawImage(ImageFactory.getPicture(boss.getImageAddress()), 0, 0, BOSS_DRAW_WIDTH, BOSS_DRAW_HEIGHT, null);
                }
            }
        }
        
        private void setMouseOverText(){
            if (boss != null){
                setToolTipText(boss.toolTipText());
            }
        }
        
    }
    
}
