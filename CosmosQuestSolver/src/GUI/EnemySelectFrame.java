/*

 */
package GUI;

import Formations.Creature;


public interface EnemySelectFrame extends ParameterListener{
    
    Creature getMouseCreature();
    void setMouseCreature(Creature c);
    boolean requestFocusInWindow();
    
    void filterHeroes(String text);
    void redrawHero(String text);
    
}
