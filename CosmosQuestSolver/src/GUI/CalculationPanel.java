/*

 */
package GUI;

import AI.AISolver;
import Formations.BattleLog;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

// gui for controlling when the solver starts and stops. also has a button to
// go back to the main menu
public class CalculationPanel extends JPanel implements ActionListener{
    
    private ISolverFrame frame;
    AISolver solver;
    
    private JPanel buttonAndInfoPanel;
    private JPanel buttonPanel;
    private JButton findButton;
    private JButton stopSearchButton;
    private JButton solutionDetailsButton;
    private JButton backButton;
    private JLabel searchingLabel;
    private JLabel messageLabel;
    private int resultStatus;// -1: no solution, 0: searching, 1: found solution. polling to change labels because they weren't updating with commands
    private ChatBox entireListChatBox;
    private BattleLog log;
    
    
    private Timer timer;
    
    public CalculationPanel(ISolverFrame frame){
        this.frame = frame;
        resultStatus = 0;
        
        solver = frame.makeSolver();
        log = new BattleLog();
        
        buttonPanel = new JPanel();
        findButton = new JButton("Find");
        stopSearchButton = new JButton("Stop Searching");
        solutionDetailsButton = new JButton("View Solution");
        backButton = new JButton("Back to Menu");
        searchingLabel = new JLabel("");
        messageLabel = new JLabel("");
        buttonAndInfoPanel = new JPanel();
        entireListChatBox = new ChatBox();
        
        
        findButton.addActionListener(this);
        stopSearchButton.addActionListener(this);
        solutionDetailsButton.addActionListener(this);
        backButton.addActionListener(this);
        
        findButton.setActionCommand("find");
        stopSearchButton.setActionCommand("stop searching");
        solutionDetailsButton.setActionCommand("view solution");
        backButton.setActionCommand("back");
        
        buttonPanel.add(findButton);
        buttonPanel.add(stopSearchButton);
        if (frame.showViewButton()){
            buttonPanel.add(solutionDetailsButton);
        }
        buttonPanel.add(backButton);
        
        buttonAndInfoPanel.add(buttonPanel);
        buttonAndInfoPanel.add(searchingLabel);
        buttonAndInfoPanel.add(messageLabel);
        
        buttonAndInfoPanel.setLayout(new BoxLayout(buttonAndInfoPanel,BoxLayout.Y_AXIS));
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        add(buttonAndInfoPanel);
        add(entireListChatBox);
        
        setOpaque(false);
        buttonPanel.setOpaque(false);
        buttonAndInfoPanel.setOpaque(false);
        buttonAndInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT );
        entireListChatBox.setPreferredSize(new Dimension(300,150));
        entireListChatBox.setMaximumSize(new Dimension(300,150));
        entireListChatBox.setMinimumSize(new Dimension(300,150));
        
        searchingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        solutionDetailsButton.setEnabled(false);
        
        timer = new Timer(200,this);
        timer.setActionCommand("timer");
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "find": 
                if (!solver.isSearching()){
                    resultStatus = 0;
                    frame.recieveSolution(new Formation());//erases any previous solutions
                    frame.recieveStart();
                    entireListChatBox.clear();
                    solutionDetailsButton.setEnabled(false);
                    solver = frame.makeSolver();//resets solver
                    messageLabel.setText("");
                    new Thread(solver).start();
                    
                    revalidate();
                    repaint();
                }
                frame.requestFocusInWindow();
            break;
            case "stop searching": 
                recieveStopSearching();
                frame.requestFocusInWindow();
            break;
            case "view solution":
                showSimulation();
            break;
            case "back":
                solver.stopSearching();
                frame.backToMenuAction();
                new MenuFrame();
                frame.setVisible(false);
                frame.dispose();
            break;
            case "timer":
                
                if (!solver.isSearching()){
                    searchingLabel.setText("");
                    if(resultStatus == -1){
                        searchingLabel.setText(frame.getDoneMessage());
                    }
                    else if (resultStatus == 0){
                        searchingLabel.setText("");
                    }
                    else{
                        searchingLabel.setText(frame.getSolutionMessage());
                    }
                }
                
                
                
                
                
                repaint();
                revalidate();
            break;
            default:
                System.out.println("CalculationPanel: unknown action command");
        }
        
        
    }

    public void recieveFailure() {
        resultStatus = -1;
        searchingLabel.setText("");
    }
    
    public void recieveStopSearching(){
        solver.stopSearching();
        searchingLabel.setText("");
        resultStatus = 0;
    }
    
    public void recieveSolutionFound(){
        searchingLabel.setText("");
        solutionDetailsButton.setEnabled(true);
        resultStatus = 1;
    }

    public void recieveProgressString(String text) {
        searchingLabel.setText(text);
        
    }
    
    public void setMessageString(String text){
        messageLabel.setText(text);
    }
    
    
    public void recieveCreatureList(LinkedList<Creature> list) {
        entireListChatBox.addText("Search Order");
        int index = 0;
        for (Creature c : list){
            index ++;
            entireListChatBox.addText(Integer.toString(index) + ": " + c.getName());
        }
    }

    public void recieveRefine() {
        solver.recieveRefine();
    }
    
    public void parametersChanged(){
        solutionDetailsButton.setEnabled(false);
    }

    public void updateSolutionDetails(Formation left, Formation right) {
        solutionDetailsButton.setEnabled(true);
        log = new BattleLog(left.getCopy(),right.getCopy());
    }

    private void showSimulation() {
        JDialog dialog = new JDialog((JFrame)frame, "Battle Replay", true);//getId?
        dialog.setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageFactory.getPicture("Backgrounds/CQ Background"), 0, 0, dialog.getWidth(), dialog.getHeight(), null);
            }
        };
        
        backgroundPanel.add(new SimulationOneTimePanel(dialog, log));
        
        dialog.getContentPane().add(backgroundPanel);
        centerScreen(dialog);
        
        dialog.pack();
        dialog.setVisible(true);
        
    }
    
    private void centerScreen(JDialog dialog) 
    {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        dialog.setLocation(x, y);
    }
    
    public AISolver getSolver(){
        return solver;
    }
    
}
