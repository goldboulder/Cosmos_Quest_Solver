/*

 */
package GUI;

import AI.AISolver;
import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import java.util.LinkedList;


public interface ISolverFrame {
    long getFollowers();
    int getMaxCreatures();
    Hero[] getHeroes();
    void recieveSolution(Formation f);
    void recieveStart();
    void recieveDone();
    AISolver makeSolver();
    String getDoneMessage();
    String getSolutionMessage();
    void recieveDamageOfBattle(long damage);
    void parametersChanged();
    void backToMenuAction();
    void recieveProgressString(String text);
    void recieveMessageString(String text);
    void recieveCreatureList(LinkedList<Creature> list);
    String getSelectSource();
    
    void setVisible(Boolean b);
    void repaint();
    void dispose();
    boolean requestFocusInWindow();

    public boolean showViewButton();
    
}
