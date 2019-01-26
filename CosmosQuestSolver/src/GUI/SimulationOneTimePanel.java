/*

 */
package GUI;

import Formations.BattleLog;
import javax.swing.JDialog;


public class SimulationOneTimePanel extends SimulationPanel{
    
    private JDialog parent;
    
    public SimulationOneTimePanel(JDialog parent, BattleLog log) {
        super(null);
        
        this.log = log;
        this.parent = parent;
        menuButton.setText("Close");
        changeRound(0);
    }
    
    @Override
    public void exit(){
        parent.setVisible(false);
        parent.dispose();
    }
    
    
    
}
