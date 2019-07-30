/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;


//at start of battle, increaces health by x% of attack per position in line (1 is front)
//used by Lili
public class PositionAttToHealth extends Skill{//*activates before leprechaun*
    
    private double percentPerPosition;
    private double transferRate;

    public PositionAttToHealth(Creature owner, double percentPerPosition, double transferRate) {
        super(owner);
        this.percentPerPosition = percentPerPosition;
        this.transferRate = transferRate;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new PositionAttToHealth(newOwner,percentPerPosition,transferRate);
    }
    
    
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        
        Creature[] formation = Formation.listBlankSpacesToArray(thisFormation.getMembers(), thisFormation.getBlankSpaces());
        
        int position = 0;
        for (int i = 0; i < Formation.MAX_MEMBERS; i++){
            if (formation[i] == owner){
                position = i+1;
                break;
            }
        }
        
        //System.out.println("position: " + position);
                
        int transfer = (int)(Math.round(owner.getBaseAtt() * percentPerPosition * position));
        //System.out.println("transfer: " + transfer);
        owner.setMaxHP(owner.getBaseHP()+(int)(transfer*transferRate));//rounding?
        owner.setCurrentHP(owner.getMaxHP());
        owner.setCurrentAtt(owner.getCurrentAtt() - transfer);
        
    }

    
    
    @Override
    public String getDescription() {
        return "At start, turns " + OtherThings.nicePercentString(percentPerPosition) + " of attack to HP per position in line at " + OtherThings.nicePercentString(transferRate) + " transferrence";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentPerPosition;
    }
    
    @Override
    public int viability() {
        return (int)((owner.getBaseHP() + owner.getBaseAtt() * percentPerPosition * Formation.MAX_MEMBERS / 2) * (owner.getBaseAtt() * (1-percentPerPosition * Formation.MAX_MEMBERS / 2)));
    }
    
    
    

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
