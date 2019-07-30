/*

 */
package GUI;

import Skills.NodeSkill;
import Skills.Skill;


public interface NodeHolder {
    Skill getNodeSkill();
    void setNodeSkill(Skill skill);
    void parametersChanged();
}
