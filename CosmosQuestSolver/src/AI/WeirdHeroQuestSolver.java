/*

 */
package AI;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import GUI.QuestSolverFrame;
import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;

//uses composition to search for solution including those without the maximum number of followers
//this is needed for the Leprechaun hero, who's ability makes him the only hero that makes non-full
//formations potentially have the best chance of beating a formation
public class WeirdHeroQuestSolver extends AISolver{// if more than one hero, solve higher up? currently broken because of prioritizeHeroes
    
    //private String weirdHeroName;//use ID instead?
    private QuestSolverFrame frame;
    private Hero weirdHero;
    private LinkedList<SpecialQuestSolver> solverList;
    
    public WeirdHeroQuestSolver(QuestSolverFrame frame, String weirdHeroName){//parameter for certain heroes? Or make specifically for one hero (leprechaun)?
        this.frame = frame;
        
        //make sure weird hero is not null. get from frame
        weirdHero = frame.getHero(weirdHeroName);
    }
    
    //creates one thread for each possible size of solution
    @Override
    protected void search() {
        solverList = new LinkedList<>();
        for (int i = frame.getMaxCreatures(); i > 0; i--){
            if (i == frame.getMaxCreatures() || (i < frame.getEnemyFormation().size() && weirdHero.getPromoteLevel()!= 5) || (i <= frame.getEnemyFormation().size() && weirdHero.getPromoteLevel()== 5) ){//don't do non-full formations if lep's ability won't take effect
                SpecialQuestSolver solver = new SpecialQuestSolver(frame,this,i,weirdHero);
                solverList.add(solver);
                new Thread(solver).start();
            }
        }
        
        
    }
    
    //stops all threads
    @Override
    public void stopSearching() {
        if (solverList != null){
            for (SpecialQuestSolver solver: solverList){

                solver.stopSearching();
            }
        }
        searching = false;
    }
    
}
