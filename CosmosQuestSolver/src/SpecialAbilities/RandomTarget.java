/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import java.util.ArrayList;
import java.util.Collections;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Luxurius Maximus
public class RandomTarget extends SpecialAbility{

    private int turn = 0;
    private long seed = -1;
    private boolean ignoreFirst;
    
    public RandomTarget(Creature owner, boolean ignoreFirst) {
        super(owner);
        this.ignoreFirst = ignoreFirst;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RandomTarget(newOwner,ignoreFirst);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        //enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
        seed = Formation.getTurnSeed(0,turn);
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
    }
    
    //Bubbles currently dampens lux damage if not targeting first according to game code, interaction should be added if this doesn't change
    //does element advantage apply to target or front monster? I think front monster
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {//attacks a "random" enemy
        //int position = (int)(Formation.getTurnSeed(seed,turn+1) % enemyFormation.size());
        //int position = (int)((enemyFormation.getTurnSeed(turn)+1) % enemyFormation.size());
        int position;
        if (enemyFormation.size() == 1){
            position = 0;
        }
        else if (ignoreFirst){
            position = getRandomIndex(Formation.getTurnSeed(seed,Formation.STALEMATE_CUTOFF_POINT-1-turn),enemyFormation.size()-1) + 1;
        }
        else{
            position = getRandomIndex(Formation.getTurnSeed(seed,Formation.STALEMATE_CUTOFF_POINT-1-turn),enemyFormation.size());
        }
        
        if (position < 0 || position >= enemyFormation.size()){
            System.out.println("out of bounds!");
        }
        
        enemyFormation.takeHit(owner,thisFormation,position);
        //System.out.println("\n\n");
    }
    
    private int getRandomIndex(long seed, int size){//turn seed?
        
        ArrayList<Integer> indecies = new ArrayList();
        for (int i = 0; i < size; i++){
            indecies.add(i);
        }
        
        //get map of indecies for shuffling
        int[] map = new int[size];
        long seedCopy = seed;
        for (int i = 0; i < size; i++){
            seedCopy = (seedCopy * 9301 + 49297) % 233280;
            map[i] = (int)(seedCopy*size/233280.0);
        }
        /*
        System.out.println("map:");
        for (int i = 0; i < size; i++){
            System.out.print(map[i] + ",");
            
        }
        
        System.out.println("");
        */
        //shuffle
        for (int i = size - 1; i > 0; i--){
            Collections.swap(indecies, map[size-1-i], i);
        }
        
        //look at dev code. this code compensates for lux's promoted skill
        //if (ignoreFirst && )
        
        
        
        /*
        System.out.println("indecies: ");
        for (int i = 0; i < size; i++){
            System.out.print(indecies.get(i) + ",");
            
        }
        System.out.println(" return " + indecies.get(0));
*/
        return indecies.get(0);
        
    }
    
    @Override
    public String getDescription() {
        if (ignoreFirst){
            return "Attacks a random unit (not front creature)";
        }
        else{
            return "Attacks a random unit";
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
    //turnSeed = (opposingCondition.seed + (101 - turncounter)*(101 - turncounter)*(101 - turncounter)) % (int64_t)round((double)opposingCondition.seed / (101 - turncounter) + (101 - turncounter)*(101 - turncounter));

    @Override
    public int positionBias() {
        return 2;
    }
    
    
    
}
