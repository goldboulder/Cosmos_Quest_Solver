/*

 */
package Formations;

import cosmosquestsolver.OtherThings;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

//stores the state of the two battling formations at the beginning of the battle
// and the end of each turn. Also provides the replay code that can be used int the game.
public class BattleLog {
    
    private Formation leftFormation;
    private Formation rightFormation;
    private LinkedList<BattleState> states;
    
    public BattleLog(Formation thisFormation, Formation enemyFormation) {//params for both blank spaces***
        this.leftFormation = thisFormation.getCopy();
        this.rightFormation = enemyFormation.getCopy();
        states = Formation.getBattleSim(thisFormation, enemyFormation);
    }
    
    public BattleLog(){
        this(new Formation(),new Formation());
    }
    
    public BattleState getState(int i){
        return states.get(i);
    }

    public int length() {
        return states.size();
    }
    
    
//Like here input this: eyJzZXR1cCI6Wy0xMDMsLTY3LC0xMDIsLTEyMiwtNzEsLTEyMF0sInNoZXJvIjp7IjEyMCI6ODgsIjExOCI6OTksIjEwMSI6OTksIjEwMCI6OTksIjY5Ijo5OSwiNjUiOjk5fSwicGxheWVyIjpbLTI4LDExOSwxMTcsMTE3LDExOF0sInBoZXJvIjp7IjI2IjoxMDAwfX0
//Translates to {"setup":[-103,-67,-102,-122,-71,-120],"shero":{"120":88,"118":99,"101":99,"100":99,"69":99,"65":99},"player":[-28,119,117,117,118],"phero":{"26":1000}}

    //returns a string that can be copied and pasted into the game's replay system
    public String getBattleCode() throws UnsupportedEncodingException {
        String battleStr = getBattleString();
        return OtherThings.encodeBase64(battleStr);
    }
    
    //returns the unencoded string that represents the left and right formations
    public String getBattleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"left\":\"Left\",\"right\":\"Right\",\"title\":\"Simulation\",");
        sb.append("\"setup\":").append(leftFormation.idStr()).append(",");
        sb.append("\"shero\":").append(leftFormation.heroLevelStr()).append(",");
        sb.append("\"spromo\":").append(leftFormation.heroPromoStr()).append(",");
        sb.append("\"player\":").append(rightFormation.idStr()).append(",");
        sb.append("\"phero\":").append(rightFormation.heroLevelStr()).append(",");
        sb.append("\"ppromo\":").append(rightFormation.heroPromoStr()).append("}");
        return sb.toString();
    }
    //{"winner":"NA","left":"You","right":"Enemy","setup":[-98,-1,-1,-1,-1,-1],"shero":{"96":99},"spromo":{"96":5},"player":[-98,-1,-1,-1,-1,-1],"phero":{"96":99},"ppromo":{"96":5}}
    //{"left":"Left","right":"Right","title":"Simulation","setup":[-98,-1,-1,-1,-1,-1],"shero":{"96":99},"spromo":{"96":5},"player":[-98,-1,-1,-1,-1,-1],"phero":{"96":99}}"ppromo":{"96":5}}

    //{"left":"Me","right":"Enemigo","title":"Fight","setup":[47,-111,47,45,46,-104],"shero":{"109":1,"102":57},"player":[-59,-60,-21,-31,44,46],"phero":{"57":99,"58":99,"19":99,"29":99}}

    public String getSolutionText() {
        StringBuilder sb = new StringBuilder("");
        if (rightFormation.isBossFormation()){
            sb.append("<Boss> ").append(rightFormation.shortHandText());
            sb.append(" <Your Formation> ").append(leftFormation.shortHandText());
            long damage = states.getLast().rightFormation.getDamageTaken();
            sb.append(" <Damage dealt> ").append(OtherThings.intToCommaString(damage));
            if (leftFormation.getFollowers() != 0){
                sb.append(" (Followers: ").append(OtherThings.intToCommaString(leftFormation.getFollowers())).append(")");
            }
            return sb.toString();
        }
        
        sb.append("<Enemy> ").append(rightFormation.shortHandText());
        
        //determine if you won or not
        if(!states.getLast().leftFormation.isEmpty() && states.getLast().rightFormation.isEmpty()){
            sb.append(" <Solution> ");
        }
        else{
            sb.append(" <Your line> ");
        }
        
        sb.append(leftFormation.shortHandText());
        
        if (leftFormation.getFollowers() != 0){
            sb.append(" (Followers: ").append(OtherThings.intToCommaString(leftFormation.getFollowers())).append(")");
        }
        return sb.toString();
        
    }
    
    
}
