/*

 */
package GUI;

import Formations.CreatureFactory;
import Skills.Nothing;
import Skills.Skill;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import Skills.RuneSkill;



public class RuneSelecterPanel extends JPanel implements ActionListener{
    
    private RuneHolder runeHolder;
    private JDialog parent;
    private JPanel instructionPanel;
    private JLabel instructionLabel;
    //private JPanel noSkillPanel;
    private JButton noSkillButton;
    private JScrollPane scrollPane;
    private JPanel runePanelHolderPanel;
    private RunePanel[] runePanels;
    
    public static final int WIDTH = 300;
    public static final int INSTRUCTION_PANEL_HEIGHT = 70;
    public static final int RUNE_PANEL_HEIGHT = 80;
    public static final int NUM_SKILLS_VISIBLE = 7;
    
    
    public RuneSelecterPanel(JDialog parent, RuneHolder runeHolder){
        
        this.parent = parent;
        this.runeHolder = runeHolder;
        instructionLabel = new JLabel("Select a rune skill");
        instructionLabel.setForeground(Color.WHITE);
        instructionPanel = new JPanel();
        //noSkillPanel = new JPanel();
        noSkillButton = new JButton();
        noSkillButton.addActionListener(this);
        noSkillButton.setIcon(new ImageIcon(ImageFactory.getPicture("Others/Cross").getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH )));
        noSkillButton.setToolTipText("No skill");
        RuneSkill[] runeSkills = CreatureFactory.getRuneSkills();
        runePanels = new RunePanel[runeSkills.length];
        runePanelHolderPanel = new JPanel();
        for (int i = 0; i < runeSkills.length; i++){
            runePanels[i] = new RunePanel(this,runeSkills[i]);
            runeSkills[i].addRuneFields(runePanels[i]);
            runePanels[i].setLayout(new BoxLayout(runePanels[i],BoxLayout.X_AXIS));
            runePanels[i].setOpaque(false);
            runePanelHolderPanel.add(runePanels[i]);
            runePanels[i].setPreferredSize(new Dimension(WIDTH,RUNE_PANEL_HEIGHT));
        }
        
        
        scrollPane = new JScrollPane(runePanelHolderPanel);
        JLabel blankLabel = new JLabel("                                    ");
        JLabel blankLabel2 = new JLabel("                     ");
        blankLabel.setOpaque(false);
        blankLabel2.setOpaque(false);
        instructionPanel.add(blankLabel);
        instructionPanel.add(instructionLabel);
        instructionPanel.add(blankLabel2);
        instructionPanel.add(noSkillButton);
        //noSkillPanel.add(noSkillButton);
        add(instructionPanel);
        //add(noSkillPanel);
        //add(noSkillButton);
        add(scrollPane);
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        runePanelHolderPanel.setLayout(new BoxLayout(runePanelHolderPanel,BoxLayout.Y_AXIS));
        
        instructionLabel.setOpaque(false);
        runePanelHolderPanel.setOpaque(false);
        //runePanelHolderPanel.setBackground(Color.RED);
        //runePanelHolderPanel.setMaximumSize(new Dimension(WIDTH,RUNE_PANEL_HEIGHT * runePanels.length));
        setOpaque(false);
        scrollPane.setOpaque(false);
        instructionPanel.setOpaque(false);
        //noSkillPanel.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        instructionPanel.setPreferredSize(new Dimension(WIDTH,INSTRUCTION_PANEL_HEIGHT));
        runePanelHolderPanel.setPreferredSize(new Dimension(WIDTH,RUNE_PANEL_HEIGHT * runeSkills.length));
        scrollPane.setPreferredSize(new Dimension(WIDTH + (Integer)UIManager.get("ScrollBar.width"),RUNE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE));
        setPreferredSize(new Dimension(WIDTH + (Integer)UIManager.get("ScrollBar.width"),RUNE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE + INSTRUCTION_PANEL_HEIGHT));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        //center screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        parent.setLocation((dim.width-WIDTH)/2, (dim.height-RUNE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE)/2);
        
    }

    public void sendSkill(Skill skill) {
        runeHolder.setRuneSkill(skill);
        runeHolder.parametersChanged();
    }

    
    public void close(){
        parent.setVisible(false);
        parent.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sendSkill(new Nothing(null));
        close();
    }
    
    
}
