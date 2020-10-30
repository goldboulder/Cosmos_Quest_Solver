/*

 */
package GUI;

import Formations.WorldBoss;
import cosmosquestsolver.OtherThings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class WorldBossASPerHitPanel extends JPanel implements ActionListener, DocumentListener{
    
    private JDialog parent;
    
    private JPanel topPanel;
    private JPanel gridPanel;
    private JPanel bottomPanel;
    
    private JLabel bossNameLabel;
    private JLabel heroesAllowedLabel;
    private JLabel damagePerHitLabel;
    private JTextField damageTextField;
    private JButton calcButton;
    
    
    
    private JLabel hitsLeftLabel;
    
    private long damagePerHit;
    private String bossName;
    private boolean heroesAllowed;
    private boolean isSuper = false;
    private WorldBoss boss;
    private boolean youHaveHeroes;
    private int maxHits;
    private int hitsLeft;
    private int totalHits;
    private Timer timer;
    //cooldown?
    public WorldBossASPerHitPanel(JDialog parent, long damagePerHit, WorldBoss boss, boolean youHaveHeroes){
        this.parent = parent;
        this.damagePerHit = damagePerHit;
        this.boss = boss;
        this.youHaveHeroes = youHaveHeroes;
        
        topPanel = new JPanel();
        gridPanel = new JPanel();
        bottomPanel = new JPanel();
        
        bossNameLabel = new JLabel();
        heroesAllowedLabel = new JLabel();
        damagePerHitLabel = new JLabel("Damage per hit: ");
        damageTextField = new JTextField();
        damageTextField.getDocument().addDocumentListener(this);
        calcButton = new JButton("Calculate");
        calcButton.addActionListener(this);
        calcButton.setActionCommand("update");
        calcButton.setEnabled(false);
        hitsLeftLabel = new JLabel();
        
        int seperation = 20;
        topPanel.add(bossNameLabel);
        topPanel.add(Box.createRigidArea(new Dimension(seperation, 0)));
        topPanel.add(heroesAllowedLabel);
        topPanel.add(Box.createRigidArea(new Dimension(seperation, 0)));
        topPanel.add(damagePerHitLabel);
        topPanel.add(Box.createRigidArea(new Dimension(seperation, 0)));
        topPanel.add(damageTextField);
        topPanel.add(Box.createRigidArea(new Dimension(seperation, 0)));
        topPanel.add(calcButton);
        
        bottomPanel.add(hitsLeftLabel);
        
        add(topPanel);
        add(Box.createRigidArea(new Dimension(0,seperation)));
        add(gridPanel);
        add(Box.createRigidArea(new Dimension(0,seperation)));
        add(bottomPanel);
        
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.X_AXIS));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //damageTextField.setMinimumSize(new Dimension(150,20));
        damageTextField.setColumns(12);
        setMinimumSize(new Dimension(400,400));
        gridPanel.setMaximumSize(new Dimension(600,400));
        
        topPanel.setOpaque(false);
        //gridPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        setOpaque(false);
        
        timer = new Timer(5000,this);
        timer.setActionCommand("timer");
        timer.start();
        
        updateChart();
        
    }
    
    private void updateChart(){
        String[][] data = getHitData();
        StringBuilder s = new StringBuilder(bossName);
        for (int i = 1; i < s.length(); i++){
            if (bossName.charAt(i) != ' '){
                s.setCharAt(i, Character.toLowerCase(s.charAt(i)));
            }
        }
        if (isSuper){
            bossNameLabel.setText("Boss: Super " + s.toString());
        }
        else{
            bossNameLabel.setText("Boss: " + s.toString());
        }
        heroesAllowedLabel.setText(heroesAllowed ? "Mode: Heroes Allowed" : "Mode: No Heroes");
        
        StringBuilder s2 = new StringBuilder("Hits left: " + hitsLeft  + "   ");
        if (hitsLeft < 20){
            s2.append(" Caution: boss is close to death and might change.");
        }
        else if ((double)hitsLeft/totalHits > 0.5){
            s2.append(" The boss has not been hit much. Data may be unreliable");
        }
        hitsLeftLabel.setText(s2.toString());
        
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(0,6,10,10));
        
        for (int i = 0; i < data.length; i++){
            for (int j = 0; j < data[0].length; j++){
                gridPanel.add(new JLabel(data[i][j]));
            }
        }
        revalidate();
    }
    
    public static String getHTML(String address){
        String content = null;
        URLConnection connection = null;
        try {
          connection =  new URL(address).openConnection();
          Scanner scanner = new Scanner(connection.getInputStream());
          scanner.useDelimiter("\\Z");
          content = scanner.next();
          scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return content;
    }
    
    public String[][] getHitData(){
        //find html of current boss
        String WBHTML = getHTML("https://cosmosquest.net/wb.php");
        int startIndexOfId = WBHTML.indexOf("wb.php?id=") + 10;
        int endIndexOfId = startIndexOfId;
        while (Character.isDigit(WBHTML.charAt(endIndexOfId))){
            endIndexOfId ++;
        }
        int id = Integer.parseInt(WBHTML.substring(startIndexOfId, endIndexOfId));
        WBHTML = getHTML("https://cosmosquest.net/wb.php?id=" + Integer.toString(id));
        
        //get hero mode
        int modeIndex = WBHTML.indexOf("<td>Mode</td>") + 30;
        heroesAllowed = WBHTML.charAt(modeIndex) == 'A';
        
        //get boss name and if it is super
        int startBossIndex = WBHTML.indexOf("<td>Boss</td>") + 30;
        int endBossIndex = startBossIndex;
        while (WBHTML.charAt(endBossIndex) != '<'){
            endBossIndex ++;
        }
        
        double multiplier = 0;
        totalHits = 1600;
        
        if (WBHTML.contains("SUPER: ")){
            startBossIndex += 7;
            isSuper = true;
        }
        
        bossName = WBHTML.substring(startBossIndex, endBossIndex);
        if (bossName.equals(boss.getName().toUpperCase())){
            bossNameLabel.setForeground(Color.BLACK);
        }
        else{
            bossNameLabel.setForeground(Color.RED);
        }
        
        
        if (heroesAllowed != youHaveHeroes){
            heroesAllowedLabel.setForeground(Color.RED);
        }
        else{
            heroesAllowedLabel.setForeground(Color.BLACK);
        }
        
        if (!heroesAllowed){
            multiplier = 1700;
        }
        else{
            switch(bossName){
                case "LORD OF CHAOS": case "MOTHER OF ALL KODAMAS": case "KRYTON":
                    multiplier = 750;
                break;
                case "DOYENNE": case "BORNAG":
                    multiplier = 1700;//?***good for Bornag
                break;
                default:
                    System.out.println("Unknown boss!");
            }
        }
        if (isSuper){
            multiplier *= 2;
            totalHits = 1200;
        }
        
        //calculate prize pool
        int startIndexOfDamage = WBHTML.indexOf("<td>Damage</td>") + 32;
        
        int endIndexOfDamage = startIndexOfDamage;
        
        while (Character.isDigit(WBHTML.charAt(endIndexOfDamage)) || WBHTML.charAt(endIndexOfDamage) == '.'){
            endIndexOfDamage ++;
        }
        String damageStr = WBHTML.substring(startIndexOfDamage, endIndexOfDamage);
        damageStr = damageStr.replaceAll("\\.", "");
        
        
        //damageList and currentHits
        LinkedList<Long> damageList = new LinkedList<>();
        int currentHits = 0;
        int minIndex = 0;
        int startUserIndex = WBHTML.indexOf("</a></td><td class=\"small\">",minIndex);
        int endUserIndex = 0;
        int startHitIndex = 0;
        int endHitIndex = 0;
        while(startUserIndex != -1){
            startUserIndex += 27;
            endUserIndex = startUserIndex;
            while(WBHTML.charAt(endUserIndex) != '<'){
                endUserIndex ++;
            }
            String damageString = WBHTML.substring(startUserIndex, endUserIndex);
            damageString = damageString.replaceAll("\\.", "");
            damageList.add(Long.parseLong(damageString));
            
            //hits
            startHitIndex = endUserIndex + 23;
            endHitIndex = startHitIndex;
            while(WBHTML.charAt(endHitIndex) != '<'){
                endHitIndex ++;
            }
            currentHits += Integer.parseInt(WBHTML.substring(startHitIndex,endHitIndex));
            
            minIndex = endUserIndex;
            startUserIndex = WBHTML.indexOf("</a></td><td class=\"small\">",minIndex);
        }
        Long[] damageArray = new Long[damageList.size()];
        damageArray = damageList.toArray(damageArray);
        
        //results
        
        maxHits = isSuper ? 5 : 9;
        String[][] results = new String[maxHits+1][6];
        results[0][0] = "Hits";
        results[0][1] = "Damage";
        results[0][2] = "Rank";
        results[0][3] = "Est. end rank";
        results[0][4] = "AS";
        results[0][5] = "AS / hit";
        
        int rank = 0;
        int endRank = 0;
        int reward = 0;
        double rewardPerHit = 0;
        hitsLeft = totalHits - currentHits;
        for (int hits = 1; hits <= maxHits; hits++){
            rank = reverseBinarySearch(damageArray,damagePerHit * hits,0,damageArray.length) + 1;
            endRank = (int)Math.round(rank * ((double)totalHits / currentHits));
            double participantMultiplier = ((double)totalHits/currentHits);
            int prizePool = (int)(Math.log(Long.parseLong(damageStr) * participantMultiplier) * multiplier);
            reward = WBPayout(prizePool,endRank,(int)(damageArray.length * ((participantMultiplier-1)*0.8+1)));
            
            rewardPerHit = (double)reward / hits;
            
            results[hits][0] = Integer.toString(hits);
            results[hits][1] = OtherThings.intToCommaString(damagePerHit * hits);
            results[hits][2] = Integer.toString(rank);
            results[hits][3] = Integer.toString(endRank);
            results[hits][4] = Integer.toString(reward);
            results[hits][5] = OtherThings.intOrNiceDecimalFormat(rewardPerHit);
            
        }
        
        return results;
    }
    
    public static int reverseBinarySearch(Comparable[] array, Comparable key, int start, int end){
        if (start == end){
            return start;
        }
        
        int middle = (start + end) / 2;
        if (array[middle].compareTo(key) < 0){
            return reverseBinarySearch(array,key,start,middle);
        }
        else if (array[middle].compareTo(key) > 0){
            return reverseBinarySearch(array,key,middle + 1,end);
        }
        else{//equals
            int sameStart = middle;
            int sameEnd = middle;
            
            while (sameStart != 0 && array[sameStart-1] == array[middle]){
                sameStart -= 1;
            }
            while (sameEnd != array.length - 1 && array[sameEnd+1] == array[middle]){
                sameEnd += 1;
            }
            
            return (int)Math.round((sameStart + sameEnd) / 2);
            
        }
    }
    
    
    
    
    public static int WBPayout(int pool, int rank, int participants){
        double sum = 0;
        for (int k = 1; k < participants; k ++){
            sum += 1.0 / (k + 20);
        }
        
        return (int)((double)pool/(rank+20)/sum);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("update")){
            updateChart();
            calcButton.setEnabled(false);
            timer.start();
        }
        else if (e.getActionCommand().equals("timer")){
            calcButton.setEnabled(true);
            timer.stop();
            timer.restart();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateDPH();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateDPH();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateDPH();
    }
    
    private void updateDPH(){
        String text = damageTextField.getText();
        text = text.replaceAll(",", "");
        long damage = -1;
        try{
            damage = Long.parseLong(text);
        }
        catch(NumberFormatException e){
            damageTextField.setForeground(Color.RED);
            return;
        }
        if (damage < 0){
            damageTextField.setForeground(Color.RED);
        }
        else{
            damagePerHit = damage;
            damageTextField.setForeground(Color.BLACK);
        }
    }
    
}