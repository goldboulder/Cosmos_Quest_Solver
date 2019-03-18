/*

 */
package GUI;


import static GUI.MenuFrame.frameBackground;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SettingsFrame extends JFrame implements ActionListener{

    private JPanel optionsPanel;
    private JPanel backToMenuPanel;
    
    private JPanel orderYourHeroesPanel;
    private JPanel orderEnemyHeroesPanel;
    private JPanel orderMonstersPanel;
    private JButton menuButton;
    
    private JLabel orderYourHeroesLabel;
    private JLabel orderEnemyHeroesLabel;
    private JLabel orderMonstersLabel;
    private JComboBox orderYourHeroesComboBox;
    private JComboBox orderEnemyHeroesComboBox;
    private JComboBox orderMonstersComboBox;
    
    private String[] sortOptions;
    
    public static final int FRAME_WIDTH = 480;
    public static final int FRAME_HEIGHT = 150;
    
    public SettingsFrame(){
        
        try{//not using image pool because it would trigger loading everything else
            frameBackground = ImageIO.read(new File("pictures/Creator Background.png"));
        }
        catch(IOException e){
            System.out.println("error");
        }
        
        setContentPane(new DrawPane());
        
        sortOptions = getSortOptions();
        
        optionsPanel = new JPanel();
        backToMenuPanel = new JPanel();
        orderYourHeroesPanel = new JPanel();
        orderEnemyHeroesPanel = new JPanel();
        orderMonstersPanel = new JPanel();
        menuButton = new JButton("Menu");
        orderYourHeroesLabel = new JLabel("Order your heroes");
        orderEnemyHeroesLabel = new JLabel("Order enemy heroes");
        orderMonstersLabel = new JLabel("Order monsters");
        orderYourHeroesComboBox = new JComboBox(sortOptions);
        orderEnemyHeroesComboBox = new JComboBox(sortOptions);
        orderMonstersComboBox = new JComboBox(new String[]{"Ascending","Decending"});
        
        orderYourHeroesPanel.add(orderYourHeroesLabel);
        orderYourHeroesPanel.add(orderYourHeroesComboBox);
        orderEnemyHeroesPanel.add(orderEnemyHeroesLabel);
        orderEnemyHeroesPanel.add(orderEnemyHeroesComboBox);
        orderMonstersPanel.add(orderMonstersLabel);
        orderMonstersPanel.add(orderMonstersComboBox);
        
        optionsPanel.add(orderYourHeroesPanel);
        optionsPanel.add(orderEnemyHeroesPanel);
        optionsPanel.add(orderMonstersPanel);
        
        backToMenuPanel.add(menuButton);
        
        add(optionsPanel);
        add(backToMenuPanel);
        
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        optionsPanel.setLayout(new BoxLayout(optionsPanel,BoxLayout.X_AXIS));
        orderYourHeroesPanel.setLayout(new BoxLayout(orderYourHeroesPanel,BoxLayout.Y_AXIS));
        orderEnemyHeroesPanel.setLayout(new BoxLayout(orderEnemyHeroesPanel,BoxLayout.Y_AXIS));
        orderMonstersPanel.setLayout(new BoxLayout(orderMonstersPanel,BoxLayout.Y_AXIS));
        
        //setPreferredSize(new Dimension(300,300));
        //orderYourHeroesPanel.setMaximumSize(new Dimension(50,50));
        //orderEnemyHeroesPanel.setMaximumSize(new Dimension(50,50));
        
        
        
        //set option to what is saved, then add actionListener
        orderYourHeroesComboBox.setSelectedItem(getCurrentYourHeroesOrder());
        orderEnemyHeroesComboBox.setSelectedItem(getCurrentEnemyHeroesOrder());
        orderMonstersComboBox.setSelectedItem(getCurrentMonstersOrder());
        
        
        orderYourHeroesComboBox.addActionListener(this);
        orderEnemyHeroesComboBox.addActionListener(this);
        orderMonstersComboBox.addActionListener(this);
        menuButton.addActionListener(this);
        orderYourHeroesComboBox.setActionCommand("your heroes order");
        orderEnemyHeroesComboBox.setActionCommand("enemy heroes order");
        orderMonstersComboBox.setActionCommand("monsters order");
        menuButton.setActionCommand("menu");
        
        optionsPanel.setOpaque(false);
        backToMenuPanel.setOpaque(false);
        orderYourHeroesPanel.setOpaque(false);
        orderEnemyHeroesPanel.setOpaque(false);
        orderMonstersPanel.setOpaque(false);
        //optionsPanel.setOpaque(false);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
        
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        
    }
    
    private String[] getSortOptions(){
        LinkedList<String> fileList = new LinkedList<>();
        File[] files = new File("creature_data/hero orders").listFiles();
        for (File file : files){
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String name = file.getName();
                fileList.add(name.substring(0, name.length()-4));
            }
        }
        fileList.add("Strength");
        String[] ans = new String[fileList.size()];
        return fileList.toArray(ans);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "your heroes order":
                changeYourHeroSort();
            break;
            case "enemy heroes order": 
                changeEnemyHeroSort();
            break;
            case "monsters order": 
                changeMonsterSort();
            break;
            case "menu": 
                new MenuFrame();
                setVisible(false);
                dispose();
            break;
            
            default: System.out.println("MenuPanel encountered unknown actionCommand");
        }
    }

    private String getCurrentYourHeroesOrder() {
        
        try {
            Scanner sc = new Scanner(new File("save data/your sort.txt"));
            return sc.next();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Source";
        
    }

    private String getCurrentEnemyHeroesOrder() {
        try {
            Scanner sc = new Scanner(new File("save data/enemy sort.txt"));
            return sc.next();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Source";
    }

    private void changeYourHeroSort() {
        String sort = (String)orderYourHeroesComboBox.getSelectedItem();
        try {
            PrintWriter pw = new PrintWriter("save data/your sort.txt");
            pw.println(sort);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeEnemyHeroSort() {
        String sort = (String)orderEnemyHeroesComboBox.getSelectedItem();
        try {
            PrintWriter pw = new PrintWriter("save data/enemy sort.txt");
            pw.println(sort);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getCurrentMonstersOrder() {
        try {
            Scanner sc = new Scanner(new File("save data/monster sort.txt"));
            return sc.next();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Ascending";
    }

    private void changeMonsterSort() {
        String sort = (String)orderMonstersComboBox.getSelectedItem();
        try {
            PrintWriter pw = new PrintWriter("save data/monster sort.txt");
            pw.println(sort);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static class DrawPane extends JPanel {//for drawing a background on a JFrame

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(frameBackground, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
            g.setColor(new Color(255,255,180,128));
            g.fillRect(0, 0, getWidth(), getHeight());//does not scale**
         }
        
    }
    
}
