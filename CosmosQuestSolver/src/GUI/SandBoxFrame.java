/*

 */
package GUI;

import AI.AISolver;
import Formations.BattleLog;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import static GUI.QuestSolverFrame.QUEST_SOLVER_FRAME_HEIGHT;
import static GUI.QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;


public class SandBoxFrame extends JFrame implements EnemySelectFrame, MouseListener, MouseMotionListener, KeyListener, WindowListener, CreatureDragFrame{
    
    private JPanel backgroundPanel;
    private JPanel topPanel;
    private EnemyFormationMakerPanel leftSelectionPanel;
    private EnemyFormationMakerPanel rightSelectionPanel;
    
    private SimulationPanel simulationPanel;
    
    private Creature mouseCreature;
    private LayerUI<JPanel> layerUI;
    private JLayer<JPanel> jLayer;
    
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public SandBoxFrame(){
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        
        topPanel = new JPanel();
        leftSelectionPanel = new EnemyFormationMakerPanel(this,"Your Formation",true,true,false,false,false,true);
        rightSelectionPanel = new EnemyFormationMakerPanel(this,"Enemy Formation",false,false,true,true,true,false);
        
        simulationPanel = new SimulationPanel(this);
        
        topPanel.add(leftSelectionPanel);
        topPanel.add(rightSelectionPanel);
        
        backgroundPanel.add(topPanel);
        backgroundPanel.add(simulationPanel);
        
        //add(backgroundPanel);
        
        layerUI = new CreatureDrawerLayer(this);
        jLayer = new JLayer<>(backgroundPanel,layerUI);
        add(jLayer);
        
        leftSelectionPanel.setPreferredSize(new Dimension(QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_WIDTH,QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_HEIGHT));
        rightSelectionPanel.setPreferredSize(new Dimension(QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_WIDTH,QuestSolverFrame.ENEMY_FORMATION_MAKER_PANEL_HEIGHT));
        backgroundPanel.setPreferredSize(new Dimension(QUEST_SOLVER_FRAME_WIDTH,QUEST_SOLVER_FRAME_HEIGHT));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        topPanel.setOpaque(false);
        leftSelectionPanel.setOpaque(false);
        rightSelectionPanel.setOpaque(false);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
        
        setTitle("Sandbox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.requestFocusInWindow();
        
        if (CreatureFactory.getOrderType(true).equals("Strength")){
            leftSelectionPanel.sortByStrength();
        }
        if (CreatureFactory.getOrderType(false).equals("Strength")){
            rightSelectionPanel.sortByStrength();
        }
        addKeyListener(this);
        parametersChanged();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            setMouseCreature(null);
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }

    @Override
    public Creature getMouseCreature(){
        return mouseCreature;
    }
    
    @Override
    public void setMouseCreature(Creature mouseCreature){
        this.mouseCreature = mouseCreature;
    }

    @Override
    public void parametersChanged() {
        if (simulationPanel != null){
            Creature[] leftCreatures = leftSelectionPanel.getCreatureArray();
            Creature[] leftCopy = new Creature[leftCreatures.length];
            for (int i = 0; i < leftCreatures.length; i++){
                if (leftCreatures[i] != null){
                    leftCopy[i] = leftCreatures[i].getCopy();
                }
            }
            
            Creature[] rightCreatures = rightSelectionPanel.getCreatureArray();
            Creature[] rightCopy = new Creature[rightCreatures.length];
            for (int i = 0; i < rightCreatures.length; i++){
                if (rightCreatures[i] != null){
                    rightCopy[i] = rightCreatures[i].getCopy();
                }
            }
            
            Formation leftNodeF = new Formation(leftCopy);
            Formation rightNodeF = new Formation(rightCopy);
            leftNodeF.addNodeSkills(leftSelectionPanel.getNodes());
            rightNodeF.addNodeSkills(rightSelectionPanel.getNodes());
            
            simulationPanel.recieveSimulation(new BattleLog(leftNodeF, rightNodeF));
        }
    }
    
    @Override
    public void filterHeroes(String text) {
        leftSelectionPanel.filterHeroes(text);
        rightSelectionPanel.filterHeroes(text);
    }
    
    @Override
    public void redrawHero(String text) {
        leftSelectionPanel.redrawHero(text);
        rightSelectionPanel.redrawHero(text);
    }

    
    
}
