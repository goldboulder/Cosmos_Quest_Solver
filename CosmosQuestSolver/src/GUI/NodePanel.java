/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Elements;
import Skills.NodeSkill;
import Skills.Skill;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class NodePanel extends JPanel implements ActionListener{
    
    private NodeSelecterPanel parent;
    private String skillName;
    
    private JPanel imagePanel;
    private JLabel imageLabel;
    private JLabel image;
    private JPanel leftPanel;
    private JButton selectButton;
    private ArrayList<FieldPanel> fieldPanels;
    

    public NodePanel(NodeSelecterPanel parent, NodeSkill skill) {
        this.parent = parent;
        this.skillName = skill.getClass().getSimpleName();
        leftPanel = new JPanel();
        selectButton = new JButton();
        selectButton.addActionListener(this);
        selectButton.setToolTipText("Set skill");
        fieldPanels = new ArrayList<>();
        image = new JLabel(new ImageIcon(ImageFactory.getPicture("Skills/" + skill.getImageName()).getScaledInstance(40,40,java.awt.Image.SCALE_SMOOTH )));
        imagePanel = new JPanel();
        imageLabel = new JLabel(skill.getName());
        imageLabel.setForeground(Color.WHITE);
        imagePanel.setLayout(new BoxLayout(imagePanel,BoxLayout.Y_AXIS));
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.X_AXIS));
        
        selectButton.setIcon(new ImageIcon(ImageFactory.getPicture("Others/Checkmark").getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH )));
        
        imagePanel.add(image);
        imagePanel.add(imageLabel);
        
        add(imagePanel);
        add(leftPanel);
        add(selectButton);
        leftPanel.setOpaque(false);
        imageLabel.setOpaque(false);
        imagePanel.setOpaque(false);
        
        imagePanel.setMinimumSize(new Dimension(120,NodeSelecterPanel.NODE_PANEL_HEIGHT));
        imagePanel.setMaximumSize(new Dimension(120,NodeSelecterPanel.NODE_PANEL_HEIGHT));
        leftPanel.setMinimumSize(new Dimension(140,NodeSelecterPanel.NODE_PANEL_HEIGHT));
        leftPanel.setMaximumSize(new Dimension(140,NodeSelecterPanel.NODE_PANEL_HEIGHT));
        
        Skill a = (Skill) skill;
        image.setToolTipText(a.getDescription());
        
    }

    public void addDoubleTextField(String parameterName, double num) {
        FieldPanel panel = new DoubleFieldPanel(parameterName,num);
        fieldPanels.add(panel);
        leftPanel.add(panel);
        panel.setOpaque(false);
    }
    
    public void addIntegerTextField(String parameterName, int num) {
        FieldPanel panel = new IntegerFieldPanel(parameterName,num);
        fieldPanels.add(panel);
        leftPanel.add(panel);
        panel.setOpaque(false);
    }
    
    public void addBooleanTextField(String parameterName, boolean bool) {
        FieldPanel panel = new BooleanFieldPanel(parameterName,bool);
        fieldPanels.add(panel);
        leftPanel.add(panel);
        panel.setOpaque(false);
    }
    
    public void addElementTextField(String parameterName, String str) {
        FieldPanel panel = new ElementFieldPanel(parameterName,str);
        fieldPanels.add(panel);
        leftPanel.add(panel);
        panel.setOpaque(false);
    }
    
    public String getSkillString(){
        StringBuilder sb = new StringBuilder(skillName);
        for (FieldPanel panel : fieldPanels){
            sb.append(" ").append(panel.textField.getText());
        }
        
        return sb.toString();
        
    }
    
    public Skill getSkill(){
        return CreatureFactory.parseSkill(getSkillString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //update node and close
        try{
            //getSkill();
            parent.sendSkill(getSkill());
            parent.close();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog((Component) parent, "Invalid skill parameters", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
    private abstract class FieldPanel extends JPanel implements DocumentListener{
        
        
        private JLabel label;
        protected JTextField textField;
        
        public FieldPanel(String parameterName){
            label = new JLabel(parameterName);
            textField = new JTextField();
            
            add(label);
            add(textField);
            label.setForeground(Color.WHITE);
            
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            textField.getDocument().addDocumentListener(this);
            textField.setMaximumSize(new Dimension(50,18));
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            keyTyped();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            keyTyped();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            keyTyped();
        }
        
        private void keyTyped(){
            if (hasValidEntry()){
                textField.setForeground(Color.BLACK);
                try{
                    image.setToolTipText(getSkill().getDescription());
                }
                catch(Exception e){
                    
                }
            }
            else{
                textField.setForeground(Color.RED);
            }
        }
        
        protected abstract boolean hasValidEntry();
        
        
    }
    
    private class DoubleFieldPanel extends FieldPanel{

        public DoubleFieldPanel(String parameterName, double num) {
            super(parameterName);
            textField.setText(Double.toString(num));//event trigger?
        }

        @Override
        protected boolean hasValidEntry() {
            try{
                Double.parseDouble(textField.getText());
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        
    }
    
    private class IntegerFieldPanel extends FieldPanel{

        public IntegerFieldPanel(String parameterName, int num) {
            super(parameterName);
            textField.setText(Integer.toString(num));
        }

        @Override
        protected boolean hasValidEntry() {
            try{
                Integer.parseInt(textField.getText());
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        
    }
    
    private class BooleanFieldPanel extends FieldPanel{

        public BooleanFieldPanel(String parameterName, boolean bool) {
            super(parameterName);
            textField.setText(Boolean.toString(bool));
        }

        @Override
        protected boolean hasValidEntry() {
            try{
                Boolean.parseBoolean(textField.getText());
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        
    }
    
    private class ElementFieldPanel extends FieldPanel{

        public ElementFieldPanel(String parameterName,String element) {
            super(parameterName);
            textField.setText(element);
        }

        @Override
        protected boolean hasValidEntry() {
            if (Elements.parseElement(textField.getText()) == null){
                return false;
            }
            
            return true;
        }
        
    }
    
    
    
}
