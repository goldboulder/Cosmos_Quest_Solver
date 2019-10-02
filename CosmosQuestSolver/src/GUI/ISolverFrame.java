/*

 */
package GUI;

import AI.AISolver;
import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import GUI.HeroCustomizationPanel.Priority;
import java.util.LinkedList;


public interface ISolverFrame extends ParameterListener{
    long getFollowers();
    int getMaxCreatures();
    Hero[] getHeroes();
    Hero[] getHeroes(Priority p);
    Priority getHeroPriority(String heroName);
    void recieveSolution(Formation f);
    void recieveStart();
    void recieveDone();
    AISolver makeSolver();
    String getDoneMessage();
    String getSolutionMessage();
    String getSavePartMessage();
    void recieveDamageOfBattle(long damage);
    
    void backToMenuAction();
    void recieveProgressString(String text);
    void recieveMessageString(String text);
    void recieveCreatureList(LinkedList<Creature> list);
    String getSelectSource();
    void filterHeroes(String text);
    
    void setVisible(Boolean b);
    void repaint();
    void dispose();
    boolean requestFocusInWindow();

    public boolean showViewButton();

}
