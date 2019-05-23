/*

 */
package Formations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//made as an idea to save memory, did not show any improvement. not used right now
public class RestorableFormation extends Formation{
    
    private final ArrayList<Creature> START_STATE;
    
    //empty formation
    public RestorableFormation(){
        super();
        START_STATE = new ArrayList<>();
    }
    
    public RestorableFormation(List<Creature> creatures){
        super(creatures);
        START_STATE = new ArrayList<>();
        for (Creature creature: creatures){
            START_STATE.add(creature);
        }
    }
    
    public RestorableFormation(Formation f){
        members = new LinkedList<>();
        START_STATE = new ArrayList<>();
        
        for (Creature creature: f.getMembers()){
            Creature copy = creature.getCopy();
            members.add(copy);
            START_STATE.add(copy);
        }
    }
    
    public RestorableFormation(LinkedList<Creature> creatureList, LinkedList<Integer> blankSpaces) {
        super(creatureList,blankSpaces);
        START_STATE = new ArrayList();
        for (Creature creature: creatureList){
            START_STATE.add(creature);
        }
        
    }
    
    public RestorableFormation(Creature[] creatures){
        super(creatures);
        START_STATE = new ArrayList();
        for (Creature creature: creatures){
            START_STATE.add(creature);
        }
    }
    
    //this method should not be used, defeats the purpose of the sub-class
    @Override
    public RestorableFormation getCopy() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void prepareForFight(Formation enemyFormation){
       restore();
       super.prepareForFight(enemyFormation);
    }
    
    public void restore(){
        members.clear();
        for (int i = 0; i < START_STATE.size(); i++){
            members.add(START_STATE.get(i));
            START_STATE.get(i).restore();
        }
        totalDamageTaken = 0;
        AOEResistance = 0;
    }
    
}
