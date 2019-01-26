/*

 */
package Formations;

//encapsulates a battle in progress on a specific turn
public class BattleState {
    
    public Formation leftFormation;
    public Formation rightFormation;
    public int turnNumber;
    
    public BattleState(Formation leftFormation, Formation rightFormation, int turnNumber){
        this.leftFormation = leftFormation;
        this.rightFormation = rightFormation;
        this.turnNumber = turnNumber;
    }
    
}
