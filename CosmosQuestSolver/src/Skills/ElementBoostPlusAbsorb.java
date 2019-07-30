/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used by Nerissa
public class ElementBoostPlusAbsorb extends Skill{
    
    private double absorbPercent;
    private double boost;
    private Elements.Element element;

    public ElementBoostPlusAbsorb(Creature owner, Elements.Element element, double boost, double absorbPercent) {
        super(owner);
        this.absorbPercent = absorbPercent;
        this.boost = boost;
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ElementBoostPlusAbsorb(newOwner,element,boost,absorbPercent);
    }
    
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorPercentBoost(absorbPercent);
    }
    
    @Override
    public double getElementDamageBoost(Elements.Element elementAttacked) {//rounding?
        if (elementAttacked == this.element ||(owner.getElement() == Elements.Element.VOID && !(elementAttacked == Elements.Element.VOID))){
            return boost;
        }
        else{
            return 0;
        }
    }
    
    @Override
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        return Math.round(damage);
    }
    
    @Override
    public String getDescription() {
        String elementStr;
        if (element == null){
            elementStr = "all non-void";
        }
        else{
            elementStr = Elements.getString(element).toLowerCase();
        }
        double baseElementBoost = owner.getElement() == Elements.Element.VOID ? 1.0 : Elements.DAMAGE_BOOST;
        
        if ((boost*100) % 1 == 0){
            return "+" + ((int)(100*boost)) + "% elemental damage to " + elementStr + " creatures (x " + (baseElementBoost + boost) + ")";
        }
        return "<html>+" + (100*boost) + "% elemental damage to " + elementStr + " creatures (x " + (baseElementBoost + boost) + ")<br>" + 
        "Absorbs " + OtherThings.nicePercentString(1 - absorbPercent) + " direct damage</html>";
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + Elements.getString(element) + " " + boost + " " + absorbPercent;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)((owner.getBaseHP()/(absorbPercent + 0.1)) * owner.getBaseAtt() * (1+(boost/Elements.numElements())));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
