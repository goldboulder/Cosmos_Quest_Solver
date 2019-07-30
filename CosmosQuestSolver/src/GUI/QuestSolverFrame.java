/*

 */
package GUI;

import AI.AISolver;
import AI.WeirdHeroQuestSolver;
import Formations.Creature;
import AI.QuestSolver;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import Skills.Skill;
import cosmosquestsolver.OtherThings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

//layerUI and JLayer for dragging?
public class QuestSolverFrame extends JFrame implements ISolverFrame, EnemySelectFrame, MouseListener, MouseMotionListener, KeyListener, WindowListener, CreatureDragFrame, ActionListener{
    private JPanel backgroundPanel;
    private AssetPanel assetPanel;
    private EnemyFormationMakerPanel enemyFormationMakerPanel;
    private CalculationPanel calculationPanel;
    private JPanel topPanel;
    private JLabel solutionLabel;
    private SolutionFormationPanel solutionFormationPanel;
    private JPanel solutionPlusClearPanel;
    private JButton clearButton;
    private JPanel yourPanel;
    
    public static final int QUEST_SOLVER_FRAME_WIDTH = 1500;
    public static final int QUEST_SOLVER_FRAME_HEIGHT = 1000;
    
    public static final int QUEST_SOLVER_TOP_PANEL_WIDTH = QUEST_SOLVER_FRAME_WIDTH;
    public static final int QUEST_SOLVER_TOP_PANEL_HEIGHT = 900;
    
    public static final int ASSET_PANEL_WIDTH = QUEST_SOLVER_FRAME_WIDTH / 2;
    public static final int ASSET_PANEL_HEIGHT = QUEST_SOLVER_TOP_PANEL_HEIGHT;
    
    public static final int ENEMY_FORMATION_MAKER_PANEL_WIDTH = QUEST_SOLVER_FRAME_WIDTH - ASSET_PANEL_WIDTH;
    public static final int ENEMY_FORMATION_MAKER_PANEL_HEIGHT = QUEST_SOLVER_TOP_PANEL_HEIGHT;
    
    public static final int CALCULATION_PANEL_WIDTH = QUEST_SOLVER_FRAME_WIDTH;
    public static final int CALCULATION_PANEL_HEIGHT = QUEST_SOLVER_FRAME_HEIGHT - QUEST_SOLVER_TOP_PANEL_HEIGHT;
    
    private Creature mouseCreature;
    private LayerUI<JPanel> layerUI;
    private JLayer<JPanel> jLayer;
    
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public QuestSolverFrame(){
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        
        assetPanel = new AssetPanel(this,true,true);
        enemyFormationMakerPanel = new EnemyFormationMakerPanel(this,"Enemy Formation",false,false,true,false,true,false);
        enemyFormationMakerPanel.setDefaultLevels();
        if (CreatureFactory.getOrderType(false).equals("Strength")){
            enemyFormationMakerPanel.sortByStrength();
        }
        
        calculationPanel = new CalculationPanel(this);
        topPanel = new JPanel();
        solutionLabel = new JLabel("Solution");
        solutionFormationPanel = new SolutionFormationPanel(this,true,true);
        solutionPlusClearPanel = new JPanel();
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        yourPanel = new JPanel();
        
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        yourPanel.setLayout(new BoxLayout(yourPanel,BoxLayout.Y_AXIS));
        
        setPreferredSize(new Dimension(QUEST_SOLVER_FRAME_WIDTH,QUEST_SOLVER_FRAME_HEIGHT));
        backgroundPanel.setPreferredSize(new Dimension(QUEST_SOLVER_FRAME_WIDTH,QUEST_SOLVER_FRAME_HEIGHT));
        topPanel.setPreferredSize(new Dimension(QUEST_SOLVER_TOP_PANEL_WIDTH,QUEST_SOLVER_TOP_PANEL_HEIGHT));
        assetPanel.setPreferredSize(new Dimension(ASSET_PANEL_WIDTH,ASSET_PANEL_HEIGHT));
        enemyFormationMakerPanel.setPreferredSize(new Dimension(ENEMY_FORMATION_MAKER_PANEL_WIDTH,ENEMY_FORMATION_MAKER_PANEL_HEIGHT));
        calculationPanel.setPreferredSize(new Dimension(CALCULATION_PANEL_WIDTH,CALCULATION_PANEL_HEIGHT));
        
        //topPanel.setBackground(Color.RED);
        //assetPanel.setBackground(Color.YELLOW);
        //enemyFormationMakerPanel.setBackground(Color.GREEN);
        //calculationPanel.setBackground(Color.BLUE);
        getContentPane().setBackground(Color.YELLOW);
        
        solutionPlusClearPanel.add(solutionFormationPanel);
        solutionPlusClearPanel.add(clearButton);
        
        yourPanel.add(solutionLabel);
        yourPanel.add(solutionPlusClearPanel);
        yourPanel.add(assetPanel);
        topPanel.add(yourPanel);
        topPanel.add(enemyFormationMakerPanel);
        backgroundPanel.add(topPanel);
        backgroundPanel.add(calculationPanel);
        //add(backgroundPanel);
        
        layerUI = new CreatureDrawerLayer(this);
        jLayer = new JLayer<>(backgroundPanel,layerUI);
        add(jLayer);
        
        
        solutionFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        
        solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        
        addKeyListener(this);
        addWindowListener(this);
        
        topPanel.setOpaque(false);
        yourPanel.setOpaque(false);
        solutionPlusClearPanel.setOpaque(false);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");//do this before setting visible so the icon doesn't flash in the taskbar
        setIconImage(img.getImage());
        
        setTitle("Quest Solver");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.requestFocusInWindow();
        
        
    }
    
    @Override
    public void windowClosing(WindowEvent windowEvent) {
        saveEnemyFormation();
        setVisible(false);
        dispose();
        System.exit(0);
    }
    
    @Override
    public void backToMenuAction() {
        saveEnemyFormation();
    }
    
    public void saveEnemyFormation(){
        try{
            PrintWriter file = new PrintWriter("save data/quest formation data.txt");
            Formation f = enemyFormationMakerPanel.getEnemyFormation();
            LinkedList<Creature> list = f.getMembers();
            for (Creature c : list){
                if (c instanceof Monster){
                    Monster m = (Monster) c;
                    file.println("M," + m.getElementChar() + "," + m.getTier());
                }
                
                    
                else if (c instanceof Hero){
                    file.println("H," + c.getName() + "," + c.getLevel() + "," + c.getPromoteLevel());
                }
                
            }
            file.close();
        }
        catch(Exception e){
            System.out.println("failed to save");
        }
    }
    
    
    @Override
    public Creature getMouseCreature(){
        return mouseCreature;
    }
    
    @Override
    public void setMouseCreature(Creature mouseCreature){
        this.mouseCreature = mouseCreature;
        /*
        if (mouseCreature != null){
        BufferedImage image = createRotated(createFlipped(CreatureFactory.getPicture(mouseCreature.getImageAddress())));
        Cursor c = toolkit.createCustomCursor(image , new Point(getContentPane().getX(), getContentPane().getY()), "img");
        setCursor (c);
        }
        else{
            setCursor(Cursor.getDefaultCursor());
        }
        */
    }
    /*
    protected static BufferedImage createFlipped(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return createTransformed(image, at);
    }
    
    protected static BufferedImage createRotated(BufferedImage image)
    {
        AffineTransform at = AffineTransform.getRotateInstance(
            Math.PI, image.getWidth()/2, image.getHeight()/2.0);
        return createTransformed(image, at);
    }
    
    protected static BufferedImage createTransformed(BufferedImage image, AffineTransform at){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    
    public void setCursor(Creature c){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CreatureFactory.getPicture("Creatures/Monsters/Ra"));
        Cursor c = toolkit.createCustomCursor(image , new Point(mainPane.getX(), mainPane.getY()), "img");
        mainPane.setCursor (c);
    }
*/
    

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseCreature = null;
        repaint();
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
    public void recieveSolution(Formation f){
        solutionFormationPanel.updateFormation(f,false);
        
        if (!f.isEmpty()){//called by calculationPanel to clearNodes solution. not really a solution
            calculationPanel.recieveSolutionFound();
            
            calculationPanel.updateSolutionDetails(f, enemyFormationMakerPanel.getEnemyFormation());
        }
    }
    
    public void recieveBlankSpaceSolution(LinkedList<Creature> creatureList, LinkedList<Integer> blankSpaces, boolean hasNodes) {
        solutionFormationPanel.updateFormation(Formation.listBlankSpacesToArray(creatureList, blankSpaces));
        Formation f = new Formation(creatureList,blankSpaces).getCopy();//same memory slot
        if (hasNodes){
            f.addNodeSkills(this.getYourNodes());
        }
        if (!creatureList.isEmpty()){//called by calculationPanel to clearNodes solution. not really a solution
            calculationPanel.recieveSolutionFound();
            calculationPanel.updateSolutionDetails(f, enemyFormationMakerPanel.getEnemyFormation());
        }
    }
    

    @Override
    public long getFollowers() {
        return assetPanel.getFollowers();
    }

    @Override
    public int getMaxCreatures() {
        return assetPanel.getMaxCreatures();
    }
    
    public Hero getHero(String name) {
        return assetPanel.getHero(name);
    }

    @Override
    public Hero[] getHeroes() {
        return assetPanel.getHeroes();
    }
    
    public Hero[] getHeroesWithoutPrioritization() {
        return assetPanel.getHeroesWithoutPrioritization();
    }
    
    public Hero[] getPrioritizedHeroes() {
        return assetPanel.getPrioritizedHeroes();
    }
    
    public Formation getSolution() {
        return solutionFormationPanel.getFormation();
    }

    public Formation getEnemyFormation() {
        return enemyFormationMakerPanel.getEnemyFormation();
    }

    @Override
    public void recieveDone() {
        calculationPanel.recieveDone();
        calculationPanel.recieveFailure();
    }
    

    @Override//make a lessCreatures solver like the world boss subclass?***
    public AISolver makeSolver() {//if has leprecaun, return subclass****
        if (assetPanel.heroEnabled("Leprechaun")){//hard coded value. currently the only hero that needs this is lep.
            return new WeirdHeroQuestSolver(this,"Leprechaun");//difference in demo sollution and shown solution***
        }
        return new QuestSolver(this);
    }

    @Override
    public void setVisible(Boolean b) {//abstract class instead? this should already be overwritten by JFrame...
        super.setVisible(b);
    }

    @Override
    public String getDoneMessage() {
        return "No solution found";
    }

    @Override
    public String getSolutionMessage() {
        Formation solution = solutionFormationPanel.getFormation();
        if (solution.getFollowers() == 0){
            return "Solution found";
        }
        else{
            return "Solution found. Followers used: " + OtherThings.intToCommaString(solution.getFollowers());
        }
        
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
    public void recieveDamageOfBattle(long damage) {
        //nothing
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
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
    public void parametersChanged() {
        if (calculationPanel != null){
            calculationPanel.recieveStopSearching();
            calculationPanel.parametersChanged();
        }
        if (solutionFormationPanel != null){
            solutionFormationPanel.updateFormation(new Formation(),false);
        }
    }

    @Override
    public void recieveProgressString(String text) {
        calculationPanel.recieveProgressString(text);
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

    public boolean heroPrioritized(String hName) {
        return assetPanel.heroPrioritized(hName);
    }

    @Override
    public boolean showViewButton() {
        return true;
    }

    @Override
    public String getSelectSource() {
        return "save data/hero quest select data.txt";
    }

    @Override
    public void filterHeroes(String text) {
        assetPanel.filterHeroes(text);
        enemyFormationMakerPanel.filterHeroes(text);
    }

    @Override
    public void redrawHero(String text) {
        enemyFormationMakerPanel.redrawHero(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        solutionFormationPanel.clearNodes();
        parametersChanged();
    }

    public Skill[] getYourNodes() {
        return solutionFormationPanel.getNodes();
    }
    
    public Skill[] getEnemyNodes(){
        return enemyFormationMakerPanel.getNodes();
    }

    
}
