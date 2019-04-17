/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Formation;
import Formations.WorldBoss;
import cosmosquestsolver.OtherThings;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
    private JLabel damageLabel;
    private long damage;
    private JLabel followersLabel;
    private SolutionFormationPanel solutionFormationPanel;
    
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
        damageLabel = new JLabel("Damage: 0");
        damage = 0;
        followersLabel = new JLabel(" ");
        solutionFormationPanel = new SolutionFormationPanel(true);
        
        setBoss(CreatureFactory.getDefaultBoss());
        
        titlePanel.add(worldBossLabel);
        solutionTitlePanel.add(solutionLabel);
        solutionTitlePanel.add(damageLabel);
        solutionTitlePanel.add(followersLabel);
        add(titlePanel);
        add(worldBossPicturePanel);
        add(buttonPanel);
        add(solutionTitlePanel);
        add(solutionFormationPanel);
        
        initiateButtons();
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
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
        
        worldBossLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        damageLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        followersLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        //worldBossLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //solutionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        titlePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        solutionTitlePanel.setOpaque(false);
        //solutionFormationPanel.setOpaque(false);
    }
    
    public WorldBoss getBoss(){
        return worldBossPicturePanel.boss;
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
        if (!worldBossPicturePanel.getBoss().getName().equals(e.getActionCommand())){
            frame.parametersChanged();
            setBoss(map.get(e.getActionCommand()));
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

    public void parametersChanged() {
        solutionFormationPanel.updateFormation(new Formation(),true);
        damageLabel.setText("Damage: 0");
        setDamage(0);
        followersLabel.setText(" ");
    }

    public long getDamage() {
        return damage;
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
