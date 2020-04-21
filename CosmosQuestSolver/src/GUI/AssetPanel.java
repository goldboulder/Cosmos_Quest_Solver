/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import GUI.HeroCustomizationPanel.Priority;
import static GUI.QuestSolverFrame.ASSET_PANEL_WIDTH;
import cosmosquestsolver.OtherThings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// GUI for the panel that contains all of the user's assets, incuding heroes, number of 
// followers, and max creatures per row
public class AssetPanel extends JPanel implements ActionListener, DocumentListener{
    
    private ISolverFrame frame;
    
    
    //private JLabel solutionLabel;
    //private SolutionFormationPanel solutionFormationPanel;
    private JLabel followersLabel;
    private JTextField followersTextField;
    private JLabel maxCreaturesLabel;
    private JTextField maxCreaturesTextField;
    private FilterPanel filterPanel;
    private HeroesCustomizationPanel heroesCustomizationPanel;
    private JScrollPane heroesCustomizationScrollPane;
    private JPanel assetTitlePanel;// for making the two hero panels start at the same y
    private JLabel assetLabel;
    private JPanel optionsPanel;
    private PlaceholderTextField searchTextField;
    private JButton disableAllButton;
    private JButton deprioritizeAllButton;
    private JButton setLevelButton;
    private JButton setPromoteLevelButton;
    private JButton saveButton;
    private JButton loadButton;
    
    private long followers = 0;
    private int maxCreatures = Formation.MAX_MEMBERS;
    
    private JPanel followersPanel;
    private JPanel maxCreaturesPanel;
    
    public static final int CREATURE_PICTURE_SIZE = 100;
    public static final int TEXTBOX_HEIGHT = 25;
    public static final int HERO_SELECTION_COLUMNS = 7;
    public static final int HERO_SELECTION_PANEL_WIDTH = CREATURE_PICTURE_SIZE * HERO_SELECTION_COLUMNS + (Integer)UIManager.get("ScrollBar.width");
    public static final int HERO_SELECTION_PANEL_HEIGHT = (HeroCustomizationPanel.CHANGE_PANEL_SIZE + CREATURE_PICTURE_SIZE) * 6;
    public static final int ASSET_PANEL_HEIGHT = 33;
    public static final int SCROLL_BAR_SPEED = 16;
    public static final Font TITLE_FONT = new Font("Courier", Font.PLAIN, 22);
    
    
    
    public AssetPanel(ISolverFrame frame,boolean includePrioritize, boolean includeSearch, boolean includeSelectShow){
        this.frame = frame;
        
        
        followersLabel = new JLabel("Followers");
        followersTextField = new JTextField("0");
        maxCreaturesLabel = new JLabel("Max creatures in row");
        maxCreaturesTextField = new JTextField(Integer.toString(Formation.MAX_MEMBERS));
        heroesCustomizationPanel = new HeroesCustomizationPanel(frame,HERO_SELECTION_COLUMNS,true,includePrioritize);
        heroesCustomizationScrollPane = new JScrollPane(heroesCustomizationPanel);
        filterPanel = new FilterPanel(heroesCustomizationPanel,true,false,includeSelectShow);
        assetTitlePanel = new JPanel();
        assetLabel = new JLabel("Your assets");
        optionsPanel = new JPanel();
        searchTextField = new PlaceholderTextField("Search Hero");
        disableAllButton = new JButton("Disable All");
        deprioritizeAllButton = new JButton("De-prioritize All");
        setLevelButton = new JButton("Set Level All");
        setPromoteLevelButton = new JButton("Promote All");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        searchTextField.getDocument().addDocumentListener(this);
        disableAllButton.addActionListener(this);
        deprioritizeAllButton.addActionListener(this);
        setLevelButton.addActionListener(this);
        setPromoteLevelButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        disableAllButton.setActionCommand("disable all");
        deprioritizeAllButton.setActionCommand("deprioritize all");
        setLevelButton.setActionCommand("set level all");
        setPromoteLevelButton.setActionCommand("promote all");
        saveButton.setActionCommand("save");
        loadButton.setActionCommand("load");
        
        
        followersPanel = new JPanel();
        maxCreaturesPanel = new JPanel();
        
        followersPanel.setLayout(new BoxLayout(followersPanel,BoxLayout.X_AXIS));
        maxCreaturesPanel.setLayout(new BoxLayout(maxCreaturesPanel,BoxLayout.X_AXIS));
        
        followersPanel.add(followersLabel);
        followersPanel.add(followersTextField);
        maxCreaturesPanel.add(maxCreaturesLabel);
        maxCreaturesPanel.add(maxCreaturesTextField);
        assetTitlePanel.add(assetLabel);
        
        optionsPanel.add(disableAllButton);
        if (includePrioritize){
            optionsPanel.add(deprioritizeAllButton);
        }
        optionsPanel.add(setLevelButton);
        optionsPanel.add(setPromoteLevelButton);
        optionsPanel.add(saveButton);
        optionsPanel.add(loadButton);
        if (includeSearch){
            optionsPanel.add(searchTextField);
        }
        //add(solutionLabel);
        //add(solutionFormationPanel);
        add(assetTitlePanel);
        add(followersPanel);
        add(maxCreaturesPanel);
        add(optionsPanel);
        add(filterPanel);
        add(heroesCustomizationScrollPane);
        
        //solutionFormationPanel.setPreferredSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        //solutionFormationPanel.setMaximumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        //solutionFormationPanel.setMinimumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        followersTextField.setMaximumSize(new Dimension(110,TEXTBOX_HEIGHT));
        maxCreaturesTextField.setMaximumSize(new Dimension(15,TEXTBOX_HEIGHT));
        searchTextField.setMaximumSize(new Dimension(70,TEXTBOX_HEIGHT));
        searchTextField.setColumns(10);
        filterPanel.setMaximumSize(new Dimension(ASSET_PANEL_WIDTH,100));
        
        heroesCustomizationScrollPane.setPreferredSize(new Dimension(HERO_SELECTION_PANEL_WIDTH + 200,HERO_SELECTION_PANEL_HEIGHT));
        heroesCustomizationScrollPane.setMaximumSize(new Dimension(HERO_SELECTION_PANEL_WIDTH + 200,HERO_SELECTION_PANEL_HEIGHT));
        heroesCustomizationScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_SPEED);
        
        assetTitlePanel.setPreferredSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        assetTitlePanel.setMaximumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        assetTitlePanel.setMinimumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        
        followersPanel.setPreferredSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,42));
        maxCreaturesPanel.setPreferredSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,42));
        //maxCreaturesPanel.setMaximumSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,EnemyFormationSinglePanel.TEXT_FIELD_HEIGHT + 36));
        //maxCreaturesPanel.setMinimumSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,EnemyFormationSinglePanel.TEXT_FIELD_HEIGHT + 36));
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        //solutionFormationPanel.setBackground(Color.CYAN);
        //heroesCustomizationPanel.setBackground(Color.MAGENTA);
        heroesCustomizationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        heroesCustomizationScrollPane.setOpaque(false);
        //heroesCustomizationScrollPane.getViewport().setOpaque(false);
        
        //followersPanel.setBackground(Color.red);
        //maxCreaturesPanel.setBackground(Color.ORANGE);
        setOpaque(false);
        assetTitlePanel.setOpaque(false);
        optionsPanel.setOpaque(false);
        followersPanel.setOpaque(false);
        maxCreaturesPanel.setOpaque(false);
        
        //solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        assetLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        followersLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        maxCreaturesLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        //assetLabel.setHorizontalAlignment(JLabel.CENTER);
        //followersLabel.setHorizontalAlignment(JLabel.CENTER);
        //maxCreaturesLabel.setHorizontalAlignment(JLabel.CENTER);
        
        followersTextField.getDocument().addDocumentListener(this);
        maxCreaturesTextField.getDocument().addDocumentListener(this);
        
        load(frame.getSelectSource());
        
    }
    
    public long getFollowers() {
        return followers;
    }

    public int getMaxCreatures() {
        return maxCreatures;
    }
    
    public Hero getHero(String name) {
        return heroesCustomizationPanel.getHero(name);
    }

    public Hero[] getHeroes() {
        return heroesCustomizationPanel.getHeroes();
    }
    
    public Hero[] getHeroes(Priority p) {
        return heroesCustomizationPanel.getHeroes(p);
    }
    /*
    public Hero[] getPrioritizedHeroes() {
        return heroesCustomizationPanel.getPrioritizedHeroes();
    }
*/
    public boolean heroEnabled(String heroName) {
        return heroesCustomizationPanel.heroEnabled(heroName);
    }
    
    public LinkedList<Hero> getEnabledHeroes(){
        return heroesCustomizationPanel.getEnabledHeroes();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }
    
    public void textFieldChanged(DocumentEvent e){
        if (e.getDocument() == followersTextField.getDocument()){
            followersTextFieldChanged();
        }
        else if (e.getDocument() == maxCreaturesTextField.getDocument()){
            maxCreaturesTextFieldChanged();
        }
        else if (e.getDocument() == searchTextField.getDocument()){
            frame.filterHeroes(searchTextField.getText());
        }
        else{
            System.out.println("Unknown document in assetPanel!");
        }
        
    }
    
    private void followersTextFieldChanged(){
        try{
            long followersEntered = parseNumAbbr(followersTextField.getText());
            followersTextField.setForeground(Color.BLACK);
            this.followers = followersEntered;
            /*long tempFollowers = //Long.parseLong(followersTextField.getText());
            if (tempFollowers >= 0){
                followersTextField.setForeground(Color.BLACK);
                this.followers = tempFollowers;
                
            }
            else{
                throw new Exception();
            }
*/
        }
        catch (Exception ex){
            followersTextField.setForeground(Color.RED);
        }
        try{
            ISolverFrame f = (ISolverFrame) frame;//have ISolverFrame be an abstract class instead?
            f.parametersChanged();
        }
        catch(Exception ex){
            
        }
    }
    
    //parses the entry into a number, with comma and k,m,b functionality
    public static long parseNumAbbr(String str) throws Exception{
        //remove commas
        str = str.replaceAll(",","");
        
        //remove k,m,b, adjust multiplier accordingly
        long multiplier = 1;
        char lastChar = str.charAt(str.length()-1);
        while (Character.isLetter(lastChar)){
            str = str.substring(0, str.length()-1);
            multiplier *= letterToNum(lastChar);
            lastChar = str.charAt(str.length()-1);
        }
        
        
        
        //parse, multiply by multiplier, and return
        double ans = Double.parseDouble(str);
        ans *= multiplier;
        if(ans < 0){
            throw new Exception();
        }
        return (long) ans;
    }
    
    public static long letterToNum(char c) throws Exception{
        switch (c){
            case 'k': case 'K': return 1000;
            case 'm': case 'M': return 1000000;
            case 'b': case 'B': return 1000000000;
            case 't': case 'T': return 1000000000000L;
        }
        throw new Exception();
    }
    
    private void maxCreaturesTextFieldChanged(){
        try{
            int tempMaxCreatures = Integer.parseInt(maxCreaturesTextField.getText());
            if (tempMaxCreatures > 0 && tempMaxCreatures <= Formation.MAX_MEMBERS){
                maxCreaturesTextField.setForeground(Color.BLACK);
                this.maxCreatures = tempMaxCreatures;
                
            }
            else{
                throw new Exception();
            }
        }
        catch (Exception ex){
            maxCreaturesTextField.setForeground(Color.RED);
        }
        try{
            ISolverFrame f = (ISolverFrame) frame;//have ISolverFrame be an abstract class instead?
            f.parametersChanged();
        }
        catch(Exception ex){
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "disable all":
                heroesCustomizationPanel.disableAll();
            break;
            case "deprioritize all":
                heroesCustomizationPanel.setPriority(Priority.NORMAL);
            break;
            case "set level all":
                int level = 1;
                try{
                    String input = JOptionPane.showInputDialog((Component) frame, "Enter level", 1);
                    if (input.equals("1k") || input.equals("1K")){
                        level = 1000;
                    }
                    else{
                        level = Integer.parseInt(input);
                    }
                    if (Hero.validHeroLevel(level)){
                        heroesCustomizationPanel.setLevelAll(level);
                    }
                    else{
                        JOptionPane.showMessageDialog((Component) frame, "Invalid level", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog((Component) frame, "Level needs to be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            break;
            case "promote all":
                int promoteLevel = 0;
                try{
                    String input = JOptionPane.showInputDialog((Component) frame, "Enter level", 0);
                    promoteLevel = Integer.parseInt(input);
                    
                    if (Hero.validPromoteLevel(promoteLevel)){
                        heroesCustomizationPanel.promoteAll(promoteLevel);
                    }
                    else{
                        JOptionPane.showMessageDialog((Component) frame, "Invalid level", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog((Component) frame, "Level needs to be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            break;
            case "save":
                save(frame.getSelectSource());
            break;
            case "load":
                load(frame.getSelectSource());
            break;
            default: System.out.println("AssetPanel actionCommand is different");
        }
        ISolverFrame f = (ISolverFrame) frame;
        f.parametersChanged();
        frame.requestFocusInWindow();
    }

    private void save(String fileSource) {
        
        if (JOptionPane.showConfirmDialog(this,"Save current heroes for " + frame.getSavePartMessage() + "?","",JOptionPane.YES_NO_OPTION) != 0){
            return;
        }
        
        try{
            PrintWriter creatureFollowersFile = new PrintWriter("save data/follower creature data.txt");
            PrintWriter heroLevelsFile = new PrintWriter("save data/hero level data.txt");
            PrintWriter heroSelectFile = new PrintWriter(fileSource);
            
            
            creatureFollowersFile.println(followers);
            creatureFollowersFile.println(maxCreatures);
            
            writeLevelString(heroLevelsFile);
            
            writeSelectString(heroSelectFile);
            
            creatureFollowersFile.close();
            heroLevelsFile.close();
            heroSelectFile.close();
            frame.recieveMessageString("Saved!");
            
        }
        catch(Exception e){
            frame.recieveMessageString("Failed to save!");
        }
        
        
    }
    
    private void load(String fileSource){
        try{
            Scanner followersCreaturesScanner = new Scanner(new File("save data/follower creature data.txt"));
            Scanner heroLevelsScanner = new Scanner(new File("save data/hero level data.txt"));
            Scanner heroSelectScanner = new Scanner(new File(fileSource));
            setFollowers(Long.parseLong(followersCreaturesScanner.nextLine()));
            setMaxCreatures(Integer.parseInt(followersCreaturesScanner.nextLine()));
            
            String[] tokens;
            while (heroLevelsScanner.hasNext()){
                tokens = heroLevelsScanner.nextLine().split(",");
                setHeroLevel(tokens[0],Integer.parseInt(tokens[1]));
                setHeroPromotionLevel(tokens[0],Integer.parseInt(tokens[2]));
                //heroesCustomizationPanel.setHeroSelect(tokens[0],Boolean.parseBoolean(tokens[2]),Boolean.parseBoolean(tokens[3]));
            }
            while (heroSelectScanner.hasNext()){
                tokens = heroSelectScanner.nextLine().split(",");
                setHeroSelect(tokens[0],Boolean.parseBoolean(tokens[1]),HeroCustomizationPanel.numToPriority((Integer.parseInt(tokens[2]))));
                //heroesCustomizationPanel.setHeroSelect(tokens[0],Boolean.parseBoolean(tokens[2]),Boolean.parseBoolean(tokens[3]));
            }
            
            followersCreaturesScanner.close();
            heroLevelsScanner.close();
            heroSelectScanner.close();
            if (frame != null){
                frame.recieveMessageString("Loaded from save file");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            if (frame != null){
                frame.recieveMessageString("Failed to Load");
            }
        }
        
        if (CreatureFactory.getOrderType(true).equals("Strength")){
            heroesCustomizationPanel.sortByStrength();
        }

    }
    
    public void setMaxCreatures(int maxCreatures){
        this.maxCreatures = maxCreatures;
        maxCreaturesTextField.setText(Integer.toString(maxCreatures));
    }
    
    public void setFollowers(long followers){
        this.followers = followers;
        followersTextField.setText(OtherThings.intToCommaString(followers));
    }
    
    public void setHeroLevel(String name, int level){
        heroesCustomizationPanel.setHeroLevel(name,level);
    }
    
    public void setHeroPromotionLevel(String name, int promoteLevel){
        heroesCustomizationPanel.setHeroPromoteLevel(name,promoteLevel);
    }
    
    public void setHeroSelect(String name, boolean selected, Priority p){
        heroesCustomizationPanel.setHeroSelect(name,selected,p);
    }

    public Priority getPriority(String hName) {
        return heroesCustomizationPanel.getPriority(hName);
    }

    public void writeLevelString(PrintWriter heroLevelsFile) {
        heroesCustomizationPanel.writeLevelString(heroLevelsFile);
    }

    public void writeSelectString(PrintWriter heroSelectFile) {
        heroesCustomizationPanel.writeSelectString(heroSelectFile);
    }

    public void filterHeroes(String text) {
        heroesCustomizationPanel.filterHeroes(text,filterPanel.getSourceFilter(),filterPanel.getRarity(),filterPanel.includeSelected(),false);
    }


    

    
}
