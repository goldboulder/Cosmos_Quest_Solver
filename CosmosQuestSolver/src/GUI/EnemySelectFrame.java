/*

 */
package GUI;

import Formations.Creature;
import Formations.Hero;


public interface EnemySelectFrame extends ParameterListener{
    
    Creature getMouseCreature();
    void setMouseCreature(Creature c);
    boolean requestFocusInWindow();
    
    void filterHeroes(String text);
    void redrawHero(String text);
    void updateLaneText(Hero h);
    public void updateCustomizationText(Hero h);
    
}
