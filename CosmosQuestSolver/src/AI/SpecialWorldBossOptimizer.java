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
        Collections.sort(creatureList, (Creature c1, Creature c2) -> followerMinderViability(c2,followers)-followerMinderViability(c1,followers));
        sendCreatureList(creatureList);
        //testList(creatureList);
        tryCombinations(creatureList);
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
