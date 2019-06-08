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
    private long tempAttack;
    
    public RandomTarget(Creature owner, boolean ignoreFirst) {
        super(owner);
        this.ignoreFirst = ignoreFirst;
    }
    
    @Override
    public void restore(){
        turn = 0;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RandomTarget(newOwner,ignoreFirst);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        //enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
        seed = Formation.getTurnSeed(enemyFormation.getSeed(),turn);
        //System.out.println("prepareForFight seed: " + seed);
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
        owner.setCurrentAtt(tempAttack);
    }
    
    //Bubbles dampening lux damage?
    //does element advantage apply to target or front monster? I think front monster
    @Override
    public int chooseTarget(Formation thisFormation, Formation enemyFormation) {//attacks a "random" enemy
        int position;
        if (enemyFormation.size() == 1){
            position = 0;
        }
        else{
            position = getRandomIndex(Formation.getTurnSeed(seed,Formation.STALEMATE_CUTOFF_POINT-1-turn),enemyFormation.size());
        }
        
        if (position < 0 || position >= enemyFormation.size()){
            System.out.println("out of bounds!");
        }
        
        if (ignoreFirst && position == 0 && enemyFormation.size() > 1){//ignore the first unit if igroreFirst is true
            position = 1;
        }
        
        tempAttack = owner.getCurrentAtt();
        if (position != 0){//bubbles affects if target is not first
            owner.setCurrentAtt((long)Math.round(owner.getCurrentAtt() * Math.pow(1-enemyFormation.getAOEResistance(),position)));
        }
        //enemyFormation.takeHit(owner,thisFormation,position);
        owner.setCurrentAtt(tempAttack);
        return position;
        //System.out.println("\n\n");
    }
    
    
    private int getRandomIndex(long seed, int size){//turn seed?
        
        int[] arr = new int[size];
        arr[0] = 1;
        for (int i = 1; i < size; i++){
            arr[i] = 0;
        }
        
        int[] mapa = new int[size];
        
        for (int x = 0; x < size; x++){
            seed = (9301 * seed + 49297) % 233280;
            double temp = seed;
            temp = temp / 233280.0;
            temp = temp * size;
            //temp = (double)((long)(temp) | 0);//bitwise expression not needed
            mapa[x] = (int)temp;
        }
        
        
        for (int i = size - 1; i > 0; i--){
            int mapa_index = size - 1 - i;
            int index_to_remove = mapa[mapa_index];
            int removed_value = arr[index_to_remove];
            
            arr[index_to_remove] = arr[i];
            arr[i] = removed_value;
        }
        
        int return_value = 0;
        for (int i = 0; i < size; i++){
            if (arr[i] > 0){
                return_value = i;
            }
        }
        
        return return_value;
        
        
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
        String s = ignoreFirst ? "True" : "False";
        return this.getClass().getSimpleName() + " " + s;
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
