/*

 */
package GUI;

import Formations.Creature;


public interface EnemySelectFrame {
    
    Creature getMouseCreature();
    void setMouseCreature(Creature c);
    boolean requestFocusInWindow();
    void parametersChanged();
    
}
