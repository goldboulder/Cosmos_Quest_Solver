/*

 */
package GUI;

import Formations.Creature;


public interface CreaturePanelGroup{
    boolean containsCreature(String name);
    void removeCreature(String name);
}
