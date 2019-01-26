/*

 */
package AI;

import GUI.WorldBossOptimizerFrame;
import java.util.LinkedList;

//uses composition to search for solution including those without the maximum number of followers
//this is needed for Lord of Chaos, who's ability makes non-full
//formations potentially have the best chance of beating a formation, since his
//aoe will usually kill the last units before they attack anyway
public class NoHeroesLessSpacesWBOptimizer extends AISolver{
    
    
    private WorldBossOptimizerFrame frame;
    private LinkedList<SpecialWorldBossOptimizer> solverList;
    
    public NoHeroesLessSpacesWBOptimizer(WorldBossOptimizerFrame frame){
        this.frame = frame;
    }
    
    //creates one thread for each possible size of solution
    @Override
    protected void search() {
        solverList = new LinkedList<>();
        for (int i = frame.getMaxCreatures(); i > 0; i--){
            SpecialWorldBossOptimizer solver = new SpecialWorldBossOptimizer(frame,this,i);
            solverList.add(solver);
            new Thread(solver).start();
        }
        
        
    }
    
    //stops all threads
    @Override
    public void stopSearching() {
        if (solverList != null){
            for (SpecialWorldBossOptimizer solver: solverList){
                solver.stopSearching();
            }
        }
        searching = false;
    }
    
}
