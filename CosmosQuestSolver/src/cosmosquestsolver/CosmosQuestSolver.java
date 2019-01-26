/*

 */
package cosmosquestsolver;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Elements;
import Formations.Elements.Element;
import Formations.Formation;
import Formations.Monster;
import GUI.ImageFactory;
import GUI.MenuFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

// starts the program. Also has some monsterFile code for debugging
public class CosmosQuestSolver {


    public static void main(String[] args) throws FileNotFoundException {
        
        CreatureFactory.initiate();
        ImageFactory.initiate();
        //test();
        new MenuFrame();
        //testFactory();
        //testFormation();
        //testSameMonsters();
    }
    
    public static void monsterFile(){
        for (Element element : Element.values()){
            Monster[] monsters = CreatureFactory.getMonsters(element);
            for (Monster m : monsters){
                try{
                    PrintWriter pw = new PrintWriter(new File("creature_data/monsters/" + m.getName() + ".txt"));
                    pw.println("element " + Elements.getString(element));
                    pw.println("att " + m.getBaseAtt());
                    pw.println("hp " + m.getBaseHP());
                    pw.println("tier " + m.getTier());
                    pw.println("followers " + m.getFollowers());
                    pw.println("skill " + "Nothing");
                    pw.close();
                }
                catch(FileNotFoundException e){
                    System.out.println("Error!");
                }
            }
        }
        
        
        //Collections.sort(heroList, (Hero c1, Hero c2) -> c1.getRawID()-c2.getRawID());
        
    }
    
    public static void test(){
        LinkedList<Creature> list1 = new LinkedList<>();
        list1.add(CreatureFactory.getHero("Ascended TR0N1X",99,4).getCopy());
        list1.add(CreatureFactory.getHero("Ascended Neptunius",99,3).getCopy());
        list1.add(CreatureFactory.getHero("Pokerface",99,3).getCopy());
        list1.add(CreatureFactory.getHero("Leprechaun",1,0).getCopy());
        
        LinkedList<Creature> list2 = new LinkedList<>();
        list2.add(CreatureFactory.getMonster(Elements.Element.AIR, 30).getCopy());
        list2.add(CreatureFactory.getHero("Luxurius Maximus",1000,0).getCopy());
        list2.add(CreatureFactory.getHero("Geron",1000,0).getCopy());
        list2.add(CreatureFactory.getHero("Xarth",1000,0).getCopy());
        list2.add(CreatureFactory.getHero("Pokerface",1000,0).getCopy());
        
        Formation f1 = new Formation(list1);
        Formation f2 = new Formation(list2);
        System.out.println(f1);
        System.out.println(f2);
        
        System.out.println(Formation.passed(f1.getCopy(),f2.getCopy()));
        //Formation.passedTest(f1.getCopy(), f2.getCopy());
    }
    
    

    
    
}
