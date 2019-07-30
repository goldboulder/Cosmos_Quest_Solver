/*

 */
package GUI;

import Formations.BattleLog;
import Formations.BattleState;
import Formations.CreatureFactory;
import Formations.Formation;
import cosmosquestsolver.OtherThings;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SimulationPanel extends JPanel implements ActionListener{
    
    protected JFrame frame;
    
    private JPanel battlePanel;
    private JPanel buttonPanel;
    
    private JPanel leftDamageLabelPanel;
    private JPanel rightDamageLabelPanel;
    private JPanel roundLabelPanel;
    private JLabel leftDamageTitleLabel;
    private JLabel rightDamageTitleLabel;
    private JLabel leftDamageLabel;
    private JLabel rightDamageLabel;
    private JLabel roundTitleLabel;
    private JLabel roundLabel;
    private SolutionFormationPanel leftFormationPanel;
    private SolutionFormationPanel rightFormationPanel;
    private JLabel infoLabel;
    
    
    private JButton firstButton;
    private JButton lastButton;
    private JButton previousButton;
    private JButton nextButton;
    protected JButton menuButton;
    protected JButton getBattleCodeButton;
    protected JButton getSolutionTextButton;
    
    protected BattleLog log;
    private int roundNum;
    
    
    public SimulationPanel(JFrame frame){
        this.frame = frame;
        
        battlePanel = new JPanel();
        buttonPanel = new JPanel();
        
        leftDamageLabelPanel = new JPanel();
        rightDamageLabelPanel = new JPanel();
        leftDamageLabel = new JLabel("0");
        rightDamageLabel = new JLabel("0");
        roundLabelPanel = new JPanel();
        leftDamageTitleLabel = new JLabel("Damage Dealt");
        rightDamageTitleLabel = new JLabel("Damage Dealt");
        leftFormationPanel = new SolutionFormationPanel((ParameterListener)frame,true,false);
        rightFormationPanel = new SolutionFormationPanel((ParameterListener)frame,false,false);
        roundTitleLabel = new JLabel("Round");
        roundLabel = new JLabel("0");
        firstButton = new JButton("<<");
        lastButton = new JButton(">>");
        previousButton = new JButton("<");
        nextButton = new JButton(">");
        menuButton = new JButton("Menu");
        getBattleCodeButton = new JButton("Get battle code");
        getSolutionTextButton = new JButton("Get solution text");
        infoLabel = new JLabel("");
        
        firstButton.addActionListener(this);
        lastButton.addActionListener(this);
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);
        menuButton.addActionListener(this);
        getBattleCodeButton.addActionListener(this);
        getSolutionTextButton.addActionListener(this);
        
        firstButton.setActionCommand("first");
        lastButton.setActionCommand("last");
        previousButton.setActionCommand("previous");
        nextButton.setActionCommand("next");
        menuButton.setActionCommand("menu");
        getBattleCodeButton.setActionCommand("getCode");
        getSolutionTextButton.setActionCommand("getText");
        
        
        leftDamageLabelPanel.add(leftDamageTitleLabel);
        leftDamageLabelPanel.add(leftDamageLabel);
        rightDamageLabelPanel.add(rightDamageTitleLabel);
        rightDamageLabelPanel.add(rightDamageLabel);
        roundLabelPanel.add(roundTitleLabel);
        roundLabelPanel.add(roundLabel);
        
        battlePanel.add(leftDamageLabelPanel);
        battlePanel.add(leftFormationPanel);
        battlePanel.add(roundLabelPanel);
        battlePanel.add(rightFormationPanel);
        battlePanel.add(rightDamageLabelPanel);
        
        buttonPanel.add(new JLabel("                           "));//hack to get it centered
        buttonPanel.add(firstButton);
        buttonPanel.add(previousButton);
        
        buttonPanel.add(getBattleCodeButton);
        buttonPanel.add(getSolutionTextButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(lastButton);
        buttonPanel.add(new JLabel("                           "));//hack to get some space between the menu button and the others
        buttonPanel.add(menuButton);
        
        add(battlePanel);
        add(buttonPanel);
        add(infoLabel);
        
        setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        setMinimumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        leftFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftDamageLabelPanel.setPreferredSize(new Dimension(100,100));
        leftDamageLabelPanel.setMaximumSize(new Dimension(100,100));
        leftDamageLabelPanel.setMinimumSize(new Dimension(100,100));
        rightDamageLabelPanel.setPreferredSize(new Dimension(100,100));
        rightDamageLabelPanel.setMaximumSize(new Dimension(100,100));
        rightDamageLabelPanel.setMinimumSize(new Dimension(100,100));
        roundLabelPanel.setPreferredSize(new Dimension(50,50));
        roundLabelPanel.setMaximumSize(new Dimension(50,50));
        roundLabelPanel.setMinimumSize(new Dimension(50,50));
        
        battlePanel.setLayout(new BoxLayout(battlePanel,BoxLayout.X_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        leftDamageLabelPanel.setLayout(new BoxLayout(leftDamageLabelPanel,BoxLayout.Y_AXIS));
        rightDamageLabelPanel.setLayout(new BoxLayout(rightDamageLabelPanel,BoxLayout.Y_AXIS));
        roundLabelPanel.setLayout(new BoxLayout(roundLabelPanel,BoxLayout.Y_AXIS));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        roundLabelPanel.setOpaque(false);
        leftDamageLabelPanel.setOpaque(false);
        rightDamageLabelPanel.setOpaque(false);
        battlePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        setOpaque(false);
        
        log = new BattleLog();
        roundNum = 0;
        
    }
    
    public void recieveSimulation(BattleLog log){
        this.log = log;
        changeRound(log.length()-1);
        infoLabel.setText("");
        revalidate();
        repaint();
    }

    
    protected void changeRound(int round){
        if (round < 0){
            round = 0;
        }
        else if (round >= log.length()){
            round = log.length() - 1;
        }
        
        roundNum = round;
        
        BattleState state = log.getState(round);
        
        leftFormationPanel.updateFormation(state.leftFormation,true);
        rightFormationPanel.updateFormation(state.rightFormation,true);
        leftDamageLabel.setText(OtherThings.intToCommaString(state.rightFormation.getDamageTaken()));
        rightDamageLabel.setText(OtherThings.intToCommaString(state.leftFormation.getDamageTaken()));
        roundLabel.setText(Integer.toString(round));
        
        revalidate();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "first":
                changeRound(0);
            break;
            case "last":
                changeRound(log.length()-1);
            break;
            case "previous":
                changeRound(roundNum - 1);
            break;
            case "next":
                changeRound(roundNum + 1);
            break;
            case "menu":
                exit();
            break;
            case "getCode":
                try {
                    //showCode(log.getBattleCode());
                    copyToClipboard(log.getBattleCode());
                    infoLabel.setText("Copied to Clipboard");
                } 
                catch (UnsupportedEncodingException ex) {
                    infoLabel.setText("Error: missing decoder");
                }           
                revalidate();
                repaint();
            
            break;
            case "getText":
                copyToClipboard(log.getSolutionText());
                infoLabel.setText("Copied to Clipboard");
                
                revalidate();
                repaint();
            
            break;
            default:
                System.out.println("Error: unknown action command in SimulationPanel");
        }
        if (frame != null){
            frame.requestFocusInWindow();
        }
    }
    
    public void exit(){
        new MenuFrame();
        frame.setVisible(false);
        frame.dispose();
    }
    
    private void showCode(String code) {
        JDialog dialog = new JDialog((JFrame)frame, "Battle Code", true);//getId?
        dialog.setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, dialog.getWidth(), dialog.getHeight(), null);
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        backgroundPanel.add(new JLabel ("Instructions"));
        JTextArea codeTextArea = new JTextArea(code);
        //codeTextArea.setColumns(50);
        codeTextArea.setMaximumSize(new Dimension(300,300));
        codeTextArea.setEditable(false);
        codeTextArea.setLineWrap(true);
        codeTextArea.setWrapStyleWord(true);
        backgroundPanel.add(codeTextArea);
        backgroundPanel.setMinimumSize(new Dimension(300,400));
        dialog.setMaximumSize(new Dimension(300,400));
        dialog.setMinimumSize(new Dimension(300,400));
        
        dialog.getContentPane().add(backgroundPanel);
        //centerScreen(dialog);
        
        dialog.pack();
        dialog.setVisible(true);
        
    }
    
    
    public static void copyToClipboard(String s){
        StringSelection stringSelection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    
}
