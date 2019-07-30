/*

 */
package GUI;

import Formations.CreatureFactory;
import Skills.NodeSkill;
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



public class NodeSelecterPanel extends JPanel implements ActionListener{
    
    private NodeHolder nodeHolder;
    private JDialog parent;
    private JPanel instructionPanel;
    private JLabel instructionLabel;
    //private JPanel noSkillPanel;
    private JButton noSkillButton;
    private JScrollPane scrollPane;
    private JPanel nodePanelHolderPanel;
    private NodePanel[] nodePanels;
    
    public static final int WIDTH = 300;
    public static final int NODE_PANEL_HEIGHT = 80;
    public static final int NUM_SKILLS_VISIBLE = 7;
    
    public NodeSelecterPanel(JDialog parent, NodeHolder nodeHolder){
        
        this.parent = parent;
        this.nodeHolder = nodeHolder;
        instructionLabel = new JLabel("Select a skill");
        instructionLabel.setForeground(Color.WHITE);
        instructionPanel = new JPanel();
        //noSkillPanel = new JPanel();
        noSkillButton = new JButton();
        noSkillButton.addActionListener(this);
        noSkillButton.setIcon(new ImageIcon(ImageFactory.getPicture("Others/Cross").getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH )));
        noSkillButton.setToolTipText("No skill");
        NodeSkill[] nodeSkills = CreatureFactory.getNodeSkills();
        nodePanels = new NodePanel[nodeSkills.length];
        nodePanelHolderPanel = new JPanel();
        for (int i = 0; i < nodeSkills.length; i++){
            nodePanels[i] = new NodePanel(this,nodeSkills[i]);
            nodeSkills[i].addNodeFields(nodePanels[i]);
            nodePanels[i].setLayout(new BoxLayout(nodePanels[i],BoxLayout.X_AXIS));
            nodePanels[i].setOpaque(false);
            nodePanelHolderPanel.add(nodePanels[i]);
            nodePanels[i].setPreferredSize(new Dimension(WIDTH,NODE_PANEL_HEIGHT));
        }
        
        
        scrollPane = new JScrollPane(nodePanelHolderPanel);
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
        nodePanelHolderPanel.setLayout(new BoxLayout(nodePanelHolderPanel,BoxLayout.Y_AXIS));
        
        instructionLabel.setOpaque(false);
        nodePanelHolderPanel.setOpaque(false);
        //nodePanelHolderPanel.setBackground(Color.RED);
        //nodePanelHolderPanel.setMaximumSize(new Dimension(WIDTH,NODE_PANEL_HEIGHT * nodePanels.length));
        setOpaque(false);
        scrollPane.setOpaque(false);
        instructionPanel.setOpaque(false);
        //noSkillPanel.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        nodePanelHolderPanel.setPreferredSize(new Dimension(WIDTH,NODE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE));
        scrollPane.setPreferredSize(new Dimension(WIDTH + (Integer)UIManager.get("ScrollBar.width"),NODE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE + 30));
        setPreferredSize(new Dimension(WIDTH + (Integer)UIManager.get("ScrollBar.width"),NODE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE + 40));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        //center screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        parent.setLocation((dim.width-WIDTH)/2, (dim.height-NODE_PANEL_HEIGHT * NUM_SKILLS_VISIBLE)/2);
        
    }

    public void sendSkill(Skill skill) {
        nodeHolder.setNodeSkill(skill);
        nodeHolder.parametersChanged();
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
