/*

 */
package AI;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import GUI.WorldBossOptimizerFrame;
import java.util.Collections;
import java.util.LinkedList;


public class SpecialWorldBossOptimizer extends WorldBossOptimizer{
        
    private int originalMaxCreatures;
    private NoHeroesLessSpacesWBOptimizer parentSolver;
        
    public SpecialWorldBossOptimizer(WorldBossOptimizerFrame frame, NoHeroesLessSpacesWBOptimizer parentSolver, int maxCreatures) {
        super(frame);
        this.parentSolver = parentSolver;
        this.maxCreatures = maxCreatures;
        originalMaxCreatures = frame.getMaxCreatures();
        
    }
        
    protected void obtainProblem(){
        followers = frame.getFollowers();
        heroes = new Hero[0];
        prioritizedHeroes = new Hero[0];
        LinkedList<Creature> list = new LinkedList<>();
        list.add(frame.getBoss());
        bossFormation = new Formation(list);
    }
        
    @Override
    protected void search() {
        obtainProblem();
        bestComboPermu();
        if (searching){
            if (originalMaxCreatures == maxCreatures){
                frame.recieveDone();
            }
            searching = false;
        }
    }
        
    @Override
    protected void bestComboPermu(){
        LinkedList<Creature> creatureList = getCreatureList();
        Collections.sort(creatureList, (Creature c1, Creature c2) -> strengthViability(c2)-strengthViability(c1));
        
        switch(maxCreatures){
            case 1: creatureList = cutList(creatureList,150);break;
            case 2: creatureList = cutList(creatureList,130);break;
            case 3: creatureList = cutList(creatureList,110);break;
            case 4: creatureList = cutList(creatureList,90);break;
            case 5: creatureList = cutList(creatureList,70);break;
            case 6: creatureList = cutList(creatureList,74);break;
        }
        
        
        sendCreatureList(creatureList);
        //testList(creatureList);
        tryCombinations(creatureList);
    }
    
    public static LinkedList<Creature> cutList(LinkedList<Creature> list, int cutOff){//switch everything to arrayList
        if (list.size() <= cutOff){
            return list;
        }
        LinkedList<Creature> newList = new LinkedList<>();
        for (int i = 0; i < cutOff; i++){
            newList.add(list.get(i));
        }
        
        return newList;
        
    }
        
        
    @Override
    protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
        if (originalMaxCreatures == maxCreatures){
            frame.recieveProgressString("Biggest search now including " + (listNum+1) + ": " + creatureList.get(listNum).getName());
        }
    }
        
        
        
    //only send the list if it is the largest search (most max creatures)
    @Override
    protected void sendCreatureList(LinkedList<Creature> creatureList) {
        if (originalMaxCreatures == maxCreatures){
            frame.recieveCreatureList(creatureList);
        }
    }
        
        
        
}
