/*

 */
package Formations;


public class Elements {

    
    public static enum Element {AIR,WATER,EARTH,FIRE}
    public static final double DAMAGE_BOOST = 1.5;
    public static final Element[] MONSTER_ELEMENTS = new Element[]{Element.AIR,Element.WATER,Element.EARTH,Element.FIRE};
    
    public static int numElements(){
        return Element.values().length;
    }
    
    public static String getString(Element element) {
        if (element == null){
            return "All";
        }
        switch (element){
            case AIR: return "Air";
            case WATER: return "Water";
            case EARTH: return "Earth";
            case FIRE: return "Fire";
            default: return "";
        }
    }

    static Element parseElement(String str) {
        switch (str){
            case "Air": return Element.AIR;
            case "Water": return Element.WATER;
            case "Earth": return Element.EARTH;
            case "Fire": return Element.FIRE;
            default: return null;
        }
    }
    
    
    public static Element[] elementArray(){
        return Element.values();
    }
    
    
    public static char getElementChar(Element element){
        switch (element){
            case AIR: return 'A';
            case WATER: return 'W';
            case EARTH: return 'E';
            case FIRE: return 'F';
            default: return 'A';
        }
    }
    
    public static Element charToElement(char c){
        switch(c){
            case 'A': return Element.AIR;
            case 'W': return Element.WATER;
            case 'E': return Element.EARTH;
            case 'F': return Element.FIRE;
            default: return null;
        }
    }
    
    private static Element elementWeakness(Element e){//private in case more elements come out that are weak to more than one element. replace eventually?
        switch(e){
            case AIR: return Element.EARTH;
            case WATER: return Element.AIR;
            case EARTH: return Element.FIRE;
            case FIRE: return Element.WATER;
            default: return null;
        }
    }

    
    public static double damageFromElement(Creature attacker, double baseDamage,Element elementAttacked){//use creatureAttacked instead?
        if (Elements.elementWeakness(elementAttacked) == attacker.getElement()){
            return baseDamage * (Elements.DAMAGE_BOOST + attacker.getSpecialAbility().getElementDamageBoost());
        }
        else{
            return baseDamage;
        }
    }
    
    public static double elementDamageMultiplier(Creature attacker, Element elementAttacked){//change if more elements come?
        if (Elements.elementWeakness(elementAttacked) == attacker.getElement()){
            return Elements.DAMAGE_BOOST + attacker.getSpecialAbility().getElementDamageBoost();
        }
        else{
            return 1;
        }
    }
}
