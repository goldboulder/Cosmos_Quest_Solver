/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.LayerUI;

public class HeroCustomizationPanel extends JPanel implements ActionListener, DocumentListener{
    
    private ISolverFrame frame;
    private Hero hero;
    private boolean includeForce;
    
    private CreaturePicturePanel picturePanel;
    private JPanel editPanel;
    private JCheckBox includeCheckBox;
    private JButton prioritizeButton;
    //private JLabel levelLabel;
    private JTextField levelTextField;
    private JTextField promoteLevelTextField;
    
    public enum Priority{NORMAL,ALWAYS,TOP,BOTTOM};
    private Priority priority = Priority.NORMAL;
    public static final Color forceColor = new Color(128,0,145);
    
    public static final int CHANGE_PANEL_SIZE = 22;
    
    public HeroCustomizationPanel(ISolverFrame frame, Hero hero, boolean includePrioritize){
        this.frame = frame;
        this.hero = hero;
        this.includeForce = includePrioritize;
        
        picturePanel = new CreaturePicturePanel(frame,hero,false);
        editPanel = new JPanel();
        includeCheckBox = new JCheckBox();
        prioritizeButton = new JButton();
        //levelLabel = new JLabel("Lvl");
        levelTextField = new JTextField("1");
        promoteLevelTextField = new JTextField("0");
        
        LayerUI<JComponent> layerUI = new DisableUI();
        JLayer<JComponent> jlayer = new JLayer<JComponent>(picturePanel, layerUI);
    
        add (jlayer);
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.X_AXIS));
        
        editPanel.add(includeCheckBox);
        //if (includePrioritize){
            editPanel.add(prioritizeButton);
        //}
        editPanel.add(levelTextField);
        editPanel.add(promoteLevelTextField);
        editPanel.add(Box.createHorizontalStrut(1));
        
        //add(picturePanel);
        add(editPanel);
        
        picturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        picturePanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        includeCheckBox.setToolTipText("Include this hero in calculations");
        //prioritizeCheckBox.setToolTipText("Always have hero in formations");
        includeCheckBox.addActionListener(this);
        includeCheckBox.setActionCommand("include");
        prioritizeButton.addActionListener(this);
        prioritizeButton.setActionCommand("prioritize");
        prioritizeButton.setBackground(Color.LIGHT_GRAY);//for new heroes
        prioritizeButton.setToolTipText("Normal search order");
        levelTextField.getDocument().addDocumentListener(this);
        promoteLevelTextField.getDocument().addDocumentListener(this);
        levelTextField.setColumns(1);
        levelTextField.setBackground(CreatureFactory.sourceToColor(CreatureFactory.IDToSource(hero.getID())));
        promoteLevelTextField.setBackground(CreatureFactory.sourceToColor(CreatureFactory.IDToSource(hero.getID())));
        levelTextField.setToolTipText("Level");
        //editPanel.setBackground(CreatureFactory.sourceToColor(CreatureFactory.IDToSource(hero.getID())));
        //levelTextField.setMaximumSize(new Dimension(30,CHANGE_PANEL_SIZE));
        promoteLevelTextField.setColumns(1);
        promoteLevelTextField.setMaximumSize(new Dimension(10,CHANGE_PANEL_SIZE));
        promoteLevelTextField.setToolTipText("Promotion");
        editPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,CHANGE_PANEL_SIZE));
        
        setOpaque(false);
        //includeCheckBox.setForeground(Color.BLUE);
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();//make more efficient?***
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        levelHero();
        promoteHero();
    }
    
    private void levelHero(){
        
        //level 1000 exception
        if (levelTextField.getText().equals("1k") || levelTextField.getText().equals("1K")){
            hero.levelUp(1000);

            frame.repaint();
            levelTextField.setForeground(Color.BLACK);
            if (heroEnabled()){
                ISolverFrame i = (ISolverFrame) frame;
                i.parametersChanged();
            }
            return;
        }
        
        try{
            int level = Integer.parseInt(levelTextField.getText());
            
            
            hero.levelUp(level);
            frame.repaint();
            
            
            
            levelTextField.setForeground(Color.BLACK);
            if (heroEnabled()){
                ISolverFrame i = (ISolverFrame) frame;
                i.parametersChanged();
            }
        }
        catch(Exception e){
            levelTextField.setForeground(Color.RED);
        }
    }
    
    private void promoteHero(){
        
        try{
            int promoteLevel = Integer.parseInt(promoteLevelTextField.getText());
            
            if (!Hero.validPromoteLevel(promoteLevel)){
                throw new Exception();
            }
            
            
            hero.promote(promoteLevel);
            frame.repaint();
            
            
            
            promoteLevelTextField.setForeground(Color.BLACK);
            if (heroEnabled()){
                ISolverFrame i = (ISolverFrame) frame;
                i.parametersChanged();
            }
        }
        catch(Exception e){
            promoteLevelTextField.setForeground(Color.RED);
        }
    }
/*
    public void disableCreature() {
        includeCheckBox.setSelected(false);
        if (prioritizeCheckBox.isSelected()){
            prioritizeCheckBox.setSelected(false);
        }
        frame.repaint();// too many repaints?
    }
    */
    
    public void setHeroEnabled(boolean b) {
        includeCheckBox.setSelected(b);
        //if (!b && prioritizeCheckBox.isSelected()){
            //prioritizeCheckBox.setSelected(false);
        //}
        repaint();
    }
    
    public Priority getPriority(){
        return priority;
    }
    
    public void setPriority(Priority p){
        if (p == Priority.ALWAYS && !includeForce){
            setPrioritizeHeroInternal(Priority.TOP);
        }
        else{
            setPrioritizeHeroInternal(p);
        }
    }
    
    private void setPrioritizeHeroInternal(Priority p){
        this.priority = p;
        switch(p){
            case NORMAL:
                prioritizeButton.setBackground(Color.LIGHT_GRAY);
                prioritizeButton.setToolTipText("Normal search order");
                break;
            case ALWAYS:
                prioritizeButton.setBackground(forceColor);
                prioritizeButton.setToolTipText("Force solutions to have this hero");
                break;
            case TOP:
                prioritizeButton.setBackground(Color.GREEN);
                prioritizeButton.setToolTipText("Check combinations with this hero first");
                break;
            case BOTTOM:
                prioritizeButton.setBackground(Color.RED);
                prioritizeButton.setToolTipText("Only include hero if all other combinations have been tried");
                break;
        }
        repaint();
    }

    public void setLevel(int level) {
        hero.levelUp(level);
        levelTextField.setText(Integer.toString(level));
        levelTextField.setForeground(Color.BLACK);
        
        //hero must be level 99 to be promoted
        //if (level != Hero.MAX_NORMAL_LEVEL){
            //setPromoteLevel(0);
        //}
        
    }
    
    public void setPromoteLevel(int promoteLevel) {
        hero.promote(promoteLevel);
        promoteLevelTextField.setText(Integer.toString(promoteLevel));
        promoteLevelTextField.setForeground(Color.BLACK);
        
        //if (promoteLevel != 0 && hero.getLevel() != Hero.MAX_NORMAL_LEVEL){
            //setLevel(Hero.MAX_NORMAL_LEVEL);
        //}
        
    }
    
    public static Priority numToPriority(int num){
        switch(num){
            case 0: return Priority.NORMAL;
            case 1: return Priority.ALWAYS;
            case 2: return Priority.TOP;
            case 3: return Priority.BOTTOM;
        }
        System.out.println("wrong number in numToPriority in HeroCustomizationPanel");
        return Priority.NORMAL;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //ensure that if a hero is disabled, deprioritize it. If prioritized, enable it
        /*
        if (e.getActionCommand().equals("include") && !includeCheckBox.isSelected() && prioritizeCheckBox.isSelected()){
            prioritizeCheckBox.doClick();
        }
        else if (e.getActionCommand().equals("prioritize") && !includeCheckBox.isSelected() && prioritizeCheckBox.isSelected()){
            includeCheckBox.setSelected(true);
        }
        */
        if (e.getActionCommand().equals("prioritize")){
            setPriority(numToPriority((priority.ordinal()+1)%Priority.values().length));//toggle between different options
        }
        
        try{
            ISolverFrame i = (ISolverFrame) frame;
            i.parametersChanged();
        }
        catch(Exception ex){
            
        }
        frame.repaint();
        //repaint();
    }

    public void writeHeroLevelString(PrintWriter file) {
        file.print(hero.getName() + "," + Integer.toString(hero.getLevel()) + "," + Integer.toString(hero.getPromoteLevel()));
    }

    public void writeHeroSelectString(PrintWriter file) {
        int priorityNum = priority == null ? 0 : priority.ordinal();
        file.print(hero.getName() + "," + Boolean.toString(includeCheckBox.isSelected()) + "," + priorityNum);
    }

    public boolean heroEnabled() {
        return includeCheckBox.isSelected();
    }

    public Hero getHero() {
        return hero;
    }

    

    
    
    private class DisableUI extends LayerUI<JComponent>{
        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            if (!includeCheckBox.isSelected()){
                g.setColor(new Color(128,128,128,128));
                g.fillRect(0, 0, AssetPanel.CREATURE_PICTURE_SIZE, AssetPanel.CREATURE_PICTURE_SIZE);
            }
        }
    }
    
}
