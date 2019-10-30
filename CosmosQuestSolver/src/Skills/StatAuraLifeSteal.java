/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//combines lifesteal and statArua
//
public class StatAuraLifeSteal extends Skill{
    
    protected int amount;

    public StatAuraLifeSteal(Creature owner, int amount) {
        super(owner);
        this.amount = amount;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new StatAuraLifeSteal(newOwner,amount);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            creature.addAttConstantBoost(amount);
            creature.addArmor(amount);
        }
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(amount);
    }
    
    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        thisFormation.AOEHeal(amount, enemyFormation);
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            creature.addAttConstantBoost(-amount);
            creature.addArmor(-amount);
        }
    }
    
    @Override
    public String getDescription() {
        return "+ " + amount + "attack, armor, AOE, and healing to all creatures";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " ";
    }
    
    @Override
    public int viability() {
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/3) * amount) * (owner.getBaseHP() + (Formation.MAX_MEMBERS/3) * amount)  + (owner.getBaseHP() * amount * Formation.MAX_MEMBERS * 2);
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
    
}
