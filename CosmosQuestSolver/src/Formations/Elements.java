/*

 */
package Formations;


public class Elements {

    
    public static enum Element {AIR,WATER,EARTH,FIRE,VOID}
    public static final double DAMAGE_BOOST = 1.5;
    public static final Element[] MONSTER_ELEMENTS = new Element[]{Element.AIR,Element.WATER,Element.EARTH,Element.FIRE};
    
    private static final double[][] ELEMENT_MULTIPLIER_ARRAY = new double[][]{
        /*a   */{1,DAMAGE_BOOST,1,1,1,1},/*air is strong against water*/
        /*w   */{1,1,1,DAMAGE_BOOST,1,1},/*water is strong against fire*/
        /*e   */{DAMAGE_BOOST,1,1,1,1,1},/*earth is strong against air*/
        /*f   */{1,1,DAMAGE_BOOST,1,1,1},/*fire is strong against earth*/
        /*v   */{1,1,1,1,1,1},/*void is neutral*/
    };
    
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
            case VOID: return "Void";
            default: return "";
        }
    }

    static Element parseElement(String str) {
        switch (str){
            case "Air": return Element.AIR;
            case "Water": return Element.WATER;
            case "Earth": return Element.EARTH;
            case "Fire": return Element.FIRE;
            case "Void": return Element.VOID;
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
            case VOID: return 'V';
            default: return 'N';
        }
    }
    
    public static Element charToElement(char c){
        switch(c){
            case 'A': case 'a': return Element.AIR;
            case 'W': case 'w': return Element.WATER;
            case 'E': case 'e': return Element.EARTH;
            case 'F': case 'f': return Element.FIRE;
            case 'V': case 'v': return Element.VOID;
            default: return null;
        }
    }
    
    
    
    
    public static double damageFromElement(Creature attacker, double baseDamage,Element elementAttacked){//use creatureAttacked instead?
        return baseDamage * (ELEMENT_MULTIPLIER_ARRAY[attacker.getElement().ordinal()][elementAttacked.ordinal()] + attacker.getMainSkill().getElementDamageBoost(elementAttacked));
    }
    
    public static double elementDamageMultiplier(Creature attacker, Element elementAttacked){//change if more elements come?
        return ELEMENT_MULTIPLIER_ARRAY[attacker.getElement().ordinal()][elementAttacked.ordinal()] + attacker.getMainSkill().getElementDamageBoost(elementAttacked);
    }

}
