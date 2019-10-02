/*

 */
package GUI;

import Skills.Skill;


public interface RuneHolder {
    Skill getRuneSkill();
    void setRuneSkill(Skill skill);
    void parametersChanged();
}
