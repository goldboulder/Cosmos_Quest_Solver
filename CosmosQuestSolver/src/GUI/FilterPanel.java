/*

 */
package GUI;

import Formations.CreatureFactory.Source;
import Formations.Hero.Rarity;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class FilterPanel extends JPanel implements ActionListener{

    
    
    private HeroListPanel heroListPanel;
    private boolean includeBosses;
    
    private JPanel sourcePanel;
    private JPanel rarityPanel;
    private JPanel otherPanel;
    
    private JRadioButton allSourceButton;
    private JRadioButton chestButton;
    private JRadioButton questButton;
    private JRadioButton seasonButton;
    private JRadioButton eventButton;
    private JRadioButton purchasedButton;
    
    private JRadioButton allRarityButton;
    private JRadioButton commonButton;
    private JRadioButton rareButton;
    private JRadioButton legendaryButton;
    private JRadioButton ascendedButton;
    
    private JCheckBox selectedBox;
    private JCheckBox bossBox;
    private JButton selectShownButton;
    
    private ButtonGroup sourceGroup;
    private ButtonGroup rarityGroup;

    

    
    public enum SourceFilter{CHEST,QUEST,SEASON,EVENT,PURCHASED};
    public static boolean sourceMatch(SourceFilter filter, Source source) {
        if (filter == SourceFilter.CHEST && (source == Source.CHEST || source == Source.ASCEND)){
            return true;
        }
        if (filter == SourceFilter.QUEST && source == Source.QUEST){
            return true;
        }
        if (filter == SourceFilter.SEASON && source == Source.SEASON){
            return true;
        }
        if (filter == SourceFilter.EVENT && source == Source.EVENT){
            return true;
        }
        if (filter == SourceFilter.PURCHASED && (source == Source.AUCTION || source == Source.SHOP || source == Source.SPECIAL)){
            return true;
        }
        return false;
    }
    
    
    public FilterPanel(HeroListPanel heroListPanel, boolean includeSelected, boolean includeBosses, boolean selectShown){
        this.heroListPanel = heroListPanel;
        this.includeBosses = includeBosses;
        
        sourcePanel = new JPanel();
        rarityPanel = new JPanel();
        otherPanel = new JPanel();
        
        allSourceButton = new JRadioButton();
        chestButton = new JRadioButton();
        questButton = new JRadioButton();
        seasonButton = new JRadioButton();
        eventButton = new JRadioButton();
        purchasedButton = new JRadioButton();

        allRarityButton = new JRadioButton();
        commonButton = new JRadioButton();
        rareButton = new JRadioButton();
        legendaryButton = new JRadioButton();
        ascendedButton = new JRadioButton();
        
        bossBox = new JCheckBox();
        selectedBox = new JCheckBox();
        selectShownButton = new JButton("Select Shown");
        selectShownButton.setActionCommand("select shown");
        selectShownButton.addActionListener(this);

        sourceGroup = new ButtonGroup();
        rarityGroup = new ButtonGroup();
        
        allSourceButton.setText("All");
        chestButton.setText("Chest");
        questButton.setText("Quest");
        seasonButton.setText("Season");
        eventButton.setText("Event");
        purchasedButton.setText("Purchased");
        
        allRarityButton.setText("All");
        commonButton.setText("Common");
        rareButton.setText("Rare");
        legendaryButton.setText("Legendary");
        ascendedButton.setText("Ascended");
        
        bossBox.setText("Boss");
        selectedBox.setText("Selected");
        
        sourceGroup.add(allSourceButton);
        sourceGroup.add(chestButton);
        sourceGroup.add(questButton);
        sourceGroup.add(seasonButton);
        sourceGroup.add(eventButton);
        sourceGroup.add(purchasedButton);
        
        rarityGroup.add(allRarityButton);
        rarityGroup.add(commonButton);
        rarityGroup.add(rareButton);
        rarityGroup.add(legendaryButton);
        rarityGroup.add(ascendedButton);
        
        allSourceButton.addActionListener(this);
        chestButton.addActionListener(this);
        questButton.addActionListener(this);
        seasonButton.addActionListener(this);
        eventButton.addActionListener(this);
        purchasedButton.addActionListener(this);
        
        allRarityButton.addActionListener(this);
        commonButton.addActionListener(this);
        rareButton.addActionListener(this);
        legendaryButton.addActionListener(this);
        ascendedButton.addActionListener(this);
        
        bossBox.addActionListener(this);
        selectedBox.addActionListener(this);
        bossBox.setActionCommand("boss");
        
        add(sourcePanel);
        add(rarityPanel);
        add(otherPanel);
        
        
        sourcePanel.add(allSourceButton);
        sourcePanel.add(chestButton);
        sourcePanel.add(questButton);
        sourcePanel.add(seasonButton);
        sourcePanel.add(eventButton);
        sourcePanel.add(purchasedButton);
        
        rarityPanel.add(allRarityButton);
        rarityPanel.add(commonButton);
        rarityPanel.add(rareButton);
        rarityPanel.add(legendaryButton);
        rarityPanel.add(ascendedButton);
        
        if (includeSelected){
            otherPanel.add(selectedBox);
        }
        if (includeBosses){
            otherPanel.add(bossBox);
        }
        if (selectShown){
            otherPanel.add(selectShownButton);
        }
        
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        sourcePanel.setLayout(new BoxLayout(sourcePanel,BoxLayout.X_AXIS));
        rarityPanel.setLayout(new BoxLayout(rarityPanel,BoxLayout.X_AXIS));
        otherPanel.setLayout(new BoxLayout(otherPanel,BoxLayout.X_AXIS));
        
        allSourceButton.setSelected(true);
        allRarityButton.setSelected(true);
        
        setOpaque(false);
        sourcePanel.setOpaque(false);
        rarityPanel.setOpaque(false);
        otherPanel.setOpaque(false);
        allSourceButton.setOpaque(false);
        chestButton.setOpaque(false);
        questButton.setOpaque(false);
        seasonButton.setOpaque(false);
        eventButton.setOpaque(false);
        purchasedButton.setOpaque(false);
        allRarityButton.setOpaque(false);
        commonButton.setOpaque(false);
        rareButton.setOpaque(false);
        legendaryButton.setOpaque(false);
        ascendedButton.setOpaque(false);
        selectedBox.setOpaque(false);
        bossBox.setOpaque(false);
    }
    
    public SourceFilter getSourceFilter(){
        SourceFilter s = null;
        //if (allSourceButton.isSelected()){
            //s = null;
        //}
        if (chestButton.isSelected()){
            s = SourceFilter.CHEST;
        }
        else if (questButton.isSelected()){
            s = SourceFilter.QUEST;
        }
        else if (seasonButton.isSelected()){
            s = SourceFilter.SEASON;
        }
        else if (eventButton.isSelected()){
            s = SourceFilter.EVENT;
        }
        else if (purchasedButton.isSelected()){
            s = SourceFilter.PURCHASED;
        }
        return s;
    }
    
    public Rarity getRarity(){
        Rarity r = null;
        //if (allRarityButton.isSelected()){
            //r = null;
        //}
        if (commonButton.isSelected()){
            r = Rarity.COMMON;
        }
        else if (rareButton.isSelected()){
            r = Rarity.RARE;
        }
        else if (legendaryButton.isSelected()){
            r = Rarity.LEGENDARY;
        }
        else if (ascendedButton.isSelected()){
            r = Rarity.ASCENDED;
        }
        return r;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("select shown")){
            heroListPanel.selectShown();
        }
        else if (e.getActionCommand().equals("boss") && bossBox.isSelected()){
            allSourceButton.setSelected(true);
            allRarityButton.setSelected(true);
        }
        else{
            bossBox.setSelected(false);
        }
        heroListPanel.filterHeroes(heroListPanel.getFilterText(),getSourceFilter(),getRarity(),selectedBox.isSelected(),bossBox.isSelected());
    }
    
    public boolean includeSelected() {
        return selectedBox.isSelected();
    }
    
}
