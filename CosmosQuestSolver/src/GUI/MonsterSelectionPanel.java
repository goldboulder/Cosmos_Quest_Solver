/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Elements.Element;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MonsterSelectionPanel extends JPanel implements ActionListener, DocumentListener{
    
    private EnemySelectFrame frame;
    private EnemyFormationMakerPanel enemyFormationMakerPanel;
    
    private ElementMonsterSelectionPanel airMonsterSelectionPanel;
    private ElementMonsterSelectionPanel waterMonsterSelectionPanel;
    private ElementMonsterSelectionPanel earthMonsterSelectionPanel;
    private ElementMonsterSelectionPanel fireMonsterSelectionPanel;
    
    private JScrollPane airMonsterSelectionScrollPane;
    private JScrollPane waterMonsterSelectionScrollPane;
    private JScrollPane earthMonsterSelectionScrollPane;
    private JScrollPane fireMonsterSelectionScrollPane;
    
    private ElementSelectorPanel elementSelectorPanel;
    
    private JLabel questLabel;
    private JTextField questTextField;
    private JButton questButton;
    private boolean includeQuests;
    
    public static final int ELEMENT_SELECTOR_PANEL_SIZE = 80;
    public static final int SCROLL_AMOUNT = 33;
    

    public MonsterSelectionPanel(EnemySelectFrame frame, EnemyFormationMakerPanel enemyFormationMakerPanel, boolean facingRight, boolean includeQuests) {
        this.frame = frame;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        
        airMonsterSelectionPanel = new ElementMonsterSelectionPanel(frame,Element.AIR,facingRight);
        waterMonsterSelectionPanel = new ElementMonsterSelectionPanel(frame,Element.WATER,facingRight);
        earthMonsterSelectionPanel = new ElementMonsterSelectionPanel(frame,Element.EARTH,facingRight);
        fireMonsterSelectionPanel = new ElementMonsterSelectionPanel(frame,Element.FIRE,facingRight);
        
        airMonsterSelectionScrollPane = new JScrollPane(airMonsterSelectionPanel);
        waterMonsterSelectionScrollPane = new JScrollPane(waterMonsterSelectionPanel);
        earthMonsterSelectionScrollPane = new JScrollPane(earthMonsterSelectionPanel);
        fireMonsterSelectionScrollPane = new JScrollPane(fireMonsterSelectionPanel);
        
        elementSelectorPanel = new ElementSelectorPanel(frame, this);
        
        questLabel = new JLabel("Quest");
        questTextField = new JTextField();
        questButton = new JButton("Set");
        
        int scrollPaneWidth = AssetPanel.CREATURE_PICTURE_SIZE * EnemyFormationMakerPanel.MONSTER_SCROLL_VIEW_AMOUNT;
        int scrollPaneHeight = AssetPanel.CREATURE_PICTURE_SIZE + (Integer)UIManager.get("ScrollBar.width");
        
        airMonsterSelectionScrollPane.setPreferredSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        waterMonsterSelectionScrollPane.setPreferredSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        earthMonsterSelectionScrollPane.setPreferredSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        fireMonsterSelectionScrollPane.setPreferredSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        airMonsterSelectionScrollPane.setMaximumSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        waterMonsterSelectionScrollPane.setMaximumSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        earthMonsterSelectionScrollPane.setMaximumSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        fireMonsterSelectionScrollPane.setMaximumSize(new Dimension(scrollPaneWidth,scrollPaneHeight));
        elementSelectorPanel.setPreferredSize(new Dimension(ELEMENT_SELECTOR_PANEL_SIZE,ELEMENT_SELECTOR_PANEL_SIZE+35));
        elementSelectorPanel.setMaximumSize(new Dimension(ELEMENT_SELECTOR_PANEL_SIZE,ELEMENT_SELECTOR_PANEL_SIZE+35));
        
        airMonsterSelectionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        waterMonsterSelectionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        earthMonsterSelectionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        fireMonsterSelectionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        airMonsterSelectionScrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_AMOUNT);
        waterMonsterSelectionScrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_AMOUNT);
        earthMonsterSelectionScrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_AMOUNT);
        fireMonsterSelectionScrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_AMOUNT);
        
        questButton.addActionListener(this);
        questButton.setActionCommand("quest");
        
        add(elementSelectorPanel);
        add(airMonsterSelectionScrollPane);
        
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        this.includeQuests = includeQuests;
        if (includeQuests){
            add(questLabel);
            add(questTextField);
            add(questButton);
        }
        
        
        questTextField.setColumns(3);
        questTextField.setPreferredSize(new Dimension(30,20));
        questTextField.setMaximumSize(new Dimension(30,20));
        questTextField.getDocument().addDocumentListener(this);
        
        setOpaque(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("quest")){
            try{
                int questNum = Integer.parseInt(questTextField.getText());
                if (questNum > 0 && questNum <= CreatureFactory.MAX_QUESTS){
                    if(enemyFormationMakerPanel != null){
                        enemyFormationMakerPanel.setQuest(questNum);
                    }
                    questTextField.setForeground(Color.BLACK);
                    
                }
            }
            catch (Exception ex){
                
            }
            return;
        }
        
        removeAll();
        add(elementSelectorPanel);
        switch(e.getActionCommand()){
            case "Air": add(airMonsterSelectionScrollPane); break;
            case "Water": add(waterMonsterSelectionScrollPane); break;
            case "Earth": add(earthMonsterSelectionScrollPane); break;
            case "Fire": add(fireMonsterSelectionScrollPane); break;
        }
        
        if (includeQuests){
            add(questLabel);
            add(questTextField);
            add(questButton);
        }
        
        revalidate();
        repaint();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setTextFieldColor();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setTextFieldColor();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setTextFieldColor();
    }
    
    private void setTextFieldColor(){
        try{
            int questNum = Integer.parseInt(questTextField.getText());
            if (questNum > 0 && questNum <= CreatureFactory.MAX_QUESTS){
                questTextField.setForeground(Color.BLACK);
            }
            else{
                questTextField.setForeground(Color.RED);
            }
        }
        catch (Exception ex){
            questTextField.setForeground(Color.RED);
        }
    }
    
    
    
}
