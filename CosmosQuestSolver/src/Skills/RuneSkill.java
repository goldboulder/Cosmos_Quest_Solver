/*

 */
package Skills;

import GUI.RunePanel;
import GUI.RuneSelecterPanel;


public interface RuneSkill {
    void addRuneFields(RunePanel panel);
    String getImageName();
    String getDescription();
    String getName();
}
