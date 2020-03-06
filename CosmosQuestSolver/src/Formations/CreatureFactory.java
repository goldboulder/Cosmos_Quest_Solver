/*

 */
package Formations;

import Skills.*;
import Formations.Elements.Element;
import Formations.Hero.Rarity;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//csv file for heroes?

//uses the flyweight and prototype patterns to generate new objects.
//these include monsters, heroes, world bosses, and images
//these assets are generated when loading a frame (not the main menu) for the 
//first time.
public class CreatureFactory {
    
    private static Monster[][] monsters;
    private static HashMap<String,Hero> heroes;
    private static HashMap<String,WorldBoss> worldBosses;
    private static RuneSkill[] RuneSkills;
    private static ArrayList<String> monsterNames;
    private static ArrayList<String> heroNames;
    private static ArrayList<String> worldBossNames;
    private static HashMap<Integer,String> IDToNameMap;
    private static HashMap<String,String> nickNameToNameMap;
    private static HashMap<String,String> nameToNickNameMap;
    private static HashMap<Integer,Source> IDToSourceMap;
    
    private static Creature nullCreature;
    

    public static int MAX_QUESTS;

    
    public static enum Source{CHEST,ASCEND, QUEST, SEASON, EVENT, SPECIAL, AUCTION, SHOP};


    public static void initiate(){
        IDToNameMap = new HashMap<>();
        initiateMonsters();
        initiateHeroes();
        initiateWorldBosses();
        initiateNickNames();
        initiateRuneSkills();
        
        int sum = 0;
        File[] files = new File("quests").listFiles();
        for (File file : files){
            if (file.isFile() && file.getName().endsWith(".txt")) {
                sum ++;
            }
        }
        
        for (Element e : Elements.Element.values()){
            elementCharacters += Character.toLowerCase(Elements.getElementChar(e));
        }
        monsterPattern = Pattern.compile("[" + elementCharacters + "]\\d+");
        
        
        MAX_QUESTS = sum;
    }
    
    private static void initiateNickNames(){
        //nicknames
        try{
            Scanner s = new Scanner(new File("creature_data/nicknames.csv"));
            s.nextLine();//ignore first row. It's only labels
            while(s.hasNext()){
                String[] tokens = s.nextLine().split(",");
                
                for (int i = 0; i < tokens.length; i ++){
                    nickNameToNameMap.put(tokens[i].toLowerCase(), tokens[0]);
                }
                if (tokens.length > 1){
                    nameToNickNameMap.put(tokens[0], tokens[1]);
                }
                else{// hero has no nickname
                    nameToNickNameMap.put(tokens[0], tokens[0]);
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("Error reading nickname file");
        }
    }
    
    public static Creature getNullCreature(){
        return nullCreature;
    }
    
    public static String getCreatureName(int ID){
        return IDToNameMap.get(ID);
    }
    
    public static String getCreatureNickName(int ID){
        return nameToNickNameMap.get(IDToNameMap.get(ID));
    }
    
    public static Monster getMonster(Element element, int tier){
        return (Monster) monsters[element.ordinal()][tier - 1].getCopy();
    }
    
    private static Pattern heroLevelPromotePattern = Pattern.compile("[^:.]+:[\\d|k]+\\.\\d+");
    private static Pattern heroLevelPattern = Pattern.compile("[^:.]+:[\\d|k]+");
    private static Pattern heroPromotePattern = Pattern.compile("[^:.]+\\.\\d+");
    private static Pattern monsterPattern;//no hardcoding element characters?**
    private static String elementCharacters;
    
    public static Creature parseCreature(String s){
        String str = s.toLowerCase();
        String realName = s.split(":|\\.")[0];
        String[] tokens;
        
        //hero level promo
        Matcher matcher = heroLevelPromotePattern.matcher(str);
        if (matcher.find()){
        
            tokens = matcher.group().split(":|\\.");
            //System.out.println("HeroLevelPromote");
            return getHero(realName,parseLevel(tokens[1]),Math.min(Integer.parseInt(tokens[2]),Hero.MAX_PROMOTE_LEVEL));
            
        }
        
        //hero level
        matcher = heroLevelPattern.matcher(str);
        if (matcher.find()){
        
            tokens = matcher.group().split(":");
            return getHero(realName,parseLevel(tokens[1]),0);
        }
        
        //hero promo
        matcher = heroPromotePattern.matcher(str);
        if (matcher.find()){
            tokens = matcher.group().split("\\.");
            return getHero(realName,Hero.MAX_NORMAL_LEVEL,Math.min(Integer.parseInt(tokens[1]),Hero.MAX_PROMOTE_LEVEL));
        }
        
        //monster
        matcher = monsterPattern.matcher(str);
        if (matcher.find()){
            return getMonster(Elements.charToElement(str.charAt(0)),Integer.parseInt(str.substring(1)));
        }
        
        //just the hero
        //System.out.println("hero");
        return getHero(str,1000,0);
        
    }
    
    private static int parseLevel(String str){
        if (str.equalsIgnoreCase("1k")){
            return 1000;
        }
        try{
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e){
            return 1;
        }
    }
    
    //returns an array of all monsters of a givin element in order of tier.
    // if element is null, returns all monsters
    public static Monster[] getMonsters(Element element) {
        Monster[] monsterArray;
        boolean ascending;
        
        try {
            Scanner sc = new Scanner(new File("save data/monster sort.txt"));
            ascending = sc.next().equals("Ascending");
        } catch (FileNotFoundException ex) {
            ascending = true;
        }
        
        
        if (element != null){
            monsterArray = new Monster[Monster.TOTAL_TIERS];
            if (ascending){
                for (int i = 0; i < monsterArray.length; i++){
                    monsterArray[i] = (Monster) monsters[element.ordinal()][i].getCopy();
                }
            }
            else{
                for (int i = 0; i < monsterArray.length; i++){
                    monsterArray[i] = (Monster) monsters[element.ordinal()][Monster.TOTAL_TIERS - i - 1].getCopy();
                }
            }
            return monsterArray;
        }
        else{
            monsterArray = new Monster[Monster.TOTAL_TIERS * Elements.MONSTER_ELEMENTS.length];
            for (int i = 0; i < Elements.MONSTER_ELEMENTS.length; i++){
                if (ascending){
                    for (int j = 0; j < Monster.TOTAL_TIERS; j++){
                        monsterArray[j + Monster.TOTAL_TIERS*i] = (Monster) monsters[i][j].getCopy();
                    }
                }
                else{
                      for (int j = 0; j < Monster.TOTAL_TIERS; j++){
                        monsterArray[j + Monster.TOTAL_TIERS*i] = (Monster) monsters[i][Monster.TOTAL_TIERS - j - 1].getCopy();
                    }  
                }
            }
            return monsterArray;
        }
        
    }
    
    public static long getMinFollowersForTier1Monsters() {
        long ans = 0;
        for (int i = 0; i < Elements.MONSTER_ELEMENTS.length; i++){
            long followers = getMonster(Elements.MONSTER_ELEMENTS[i],1).getFollowers();
            if (followers > ans){
                ans = followers;
            }
        }
        return ans;
    }
    
    public static Monster getCheapestMonster() {
        LinkedList<Monster> monsters = new LinkedList<>();
        for (Elements.Element element : Elements.MONSTER_ELEMENTS){
            monsters.add(getMonster(element,1));
        }
        
        long cheapestCost = Long.MAX_VALUE;
        for (Monster m : monsters){
            if (m.getFollowers() < cheapestCost){
                cheapestCost = m.getFollowers();
            }
        }
        
        while(true){//this returns a random monster if there is a tie for the cheapest monster
            int randomIndex = (int)(Math.random() * monsters.size());
            if (monsters.get(randomIndex).getFollowers() == cheapestCost){
                return monsters.get(randomIndex).getCopy();
            }
            else{
                monsters.remove(randomIndex);
            }
        }
        
    }
    
    public static Monster getStrongestMonster() {//strongest monster is always the same. just return that?
        LinkedList<Monster> monsters = new LinkedList<>();
        for (Elements.Element element : Elements.MONSTER_ELEMENTS){
            monsters.add(getMonster(element,Monster.TOTAL_TIERS));
        }
        
        Monster strongestMonster = getMonster(Elements.Element.AIR,Monster.TOTAL_TIERS);
        for (Monster m : monsters){
            if (m.getFollowers() > strongestMonster.getFollowers()){
                strongestMonster = m;
            }
        }
        return strongestMonster;
        
    }
    
    //returns a list of all followers that are available with a given amount of followers
    public static LinkedList<Monster> getAvailableMonsters(long followers){
        LinkedList<Monster> list = new LinkedList<>();
        for (int i = 0; i < Elements.MONSTER_ELEMENTS.length; i++){
            for (int j = 0; j < Monster.TOTAL_TIERS; j++){
                if (monsters[i][j].getFollowers() < followers){
                    list.add((Monster) monsters[i][j].getCopy());
                }
            }
        }
        return list;
    }
    
    public static Hero getHero(String name, int level, int promoteLevel){
        
        Hero hero = heroes.get(name);
        if (hero == null){//check nicknames
            
            hero = heroes.get(nickNameToNameMap.get(name.toLowerCase()));
            
        }
        Hero newHero = (Hero) hero.getCopy();
        //System.out.println("5");
        newHero.levelUp(level);
        
        newHero.promote(promoteLevel);
        return newHero;
    }
    
    public static Hero[] getHeroes(String address) {
        try{
            LinkedList<Hero> heroList = new LinkedList<>();
            Scanner sc = new Scanner(new File("creature_data/hero orders/" + address + ".txt"));
            while(sc.hasNext()){
                String name = sc.nextLine();
                Hero tempHero = (Hero)heroes.get(name);
                if (tempHero != null){
                    heroList.add((Hero)heroes.get(name).getCopy());
                }
                else{
                    System.out.println("Error with name in hero order list: " + name);
                }
            }
            sc.close();

            //check for heroes that may have been missed
            Hero[] defaultOrderHeroes = getHeroesDefaultOrder();
            for (Hero h : defaultOrderHeroes){
                if (!containsHero(h.getName(),heroList)){
                    heroList.add(h);
                }
            }
            Hero[] ans = new Hero[heroList.size()];
            return heroList.toArray(ans);
        }
        catch(FileNotFoundException ex){
            return getHeroesDefaultOrder();
        }
        
    }
    
    private static boolean containsHero(String name, LinkedList<Hero> heroList){
        for (Hero h : heroList){
            if (h.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    
    private static boolean containsBoss(String name, LinkedList<WorldBoss> worldBossList){
        for (WorldBoss h : worldBossList){
            if (h.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    
    
    public static Hero[] getHeroesDefaultOrder() {
        Hero[] heroArray = new Hero[heroNames.size()];
        int i = 0;
        for (String str : heroNames){
            heroArray[i] = (Hero) heroes.get(str).getCopy();
            i ++;
        }
        return heroArray;
    }
    
    public static WorldBoss[] getBossesDefaultOrder() {
        WorldBoss[] bossArray = new WorldBoss[worldBossNames.size()];
        int i = 0;
        for (String str : worldBossNames){
            bossArray[i] = (WorldBoss) worldBosses.get(str).getCopy();
            i ++;
        }
        return bossArray;
    }
    
    
    public static WorldBoss getWorldBoss(String name){
        WorldBoss wb = (WorldBoss) worldBosses.get(name).getCopy();
        return wb;
    }
    
    public static WorldBoss[] getWorldBosses(String address) {
        try{
        LinkedList<WorldBoss> bossList = new LinkedList<>();
        Scanner sc = new Scanner(new File("creature_data/world boss orders/" + address + ".txt"));
        while(sc.hasNext()){
            String name = sc.nextLine();
            WorldBoss tempBoss = (WorldBoss)worldBosses.get(name);
            if (tempBoss != null){
                bossList.add((WorldBoss)worldBosses.get(name).getCopy());
            }
            else{
                System.out.println("Error with name in boss order list: " + name);
            }
        }
        sc.close();
        
        //check for heroes that may have been missed
        WorldBoss[] defaultOrderBosses = getBossesDefaultOrder();
        for (WorldBoss b : defaultOrderBosses){
            if (!containsBoss(b.getName(),bossList)){
                bossList.add(b);
            }
        }
        WorldBoss[] ans = new WorldBoss[bossList.size()];
        return bossList.toArray(ans);
        }
        catch(FileNotFoundException ex){
            return getBossesDefaultOrder();
        }
        
    }
    
    public static WorldBoss getDefaultBoss() {
        return getWorldBoss("Lord of Chaos");
    }
    
    public static WorldBoss[] getWorldBosses(){
        WorldBoss[] bossArray = new WorldBoss[worldBossNames.size()];
        int i = 0;
        for (String str : worldBossNames){
            bossArray[i] = (WorldBoss) worldBosses.get(str).getCopy();
            i ++;
        }
        return bossArray;
        
    }
    
    public static Formation loadFormation(String address, boolean facingRight) {
        try{
            LinkedList<Creature> list = new LinkedList<>();
            Scanner sc = new Scanner(new File(address + ".txt"));
            String[] tokens;
            while (sc.hasNext()){
                tokens = sc.nextLine().split(",");
                if(tokens[0].equals("M")){//signifies monsters
                    Monster m = getMonster(Elements.charToElement(tokens[1].charAt(0)),Integer.parseInt(tokens[2]));
                    m.setFacingRight(facingRight);
                    list.add(m);
                }
                else if (tokens[0].equals("H")){//heroes
                    Hero h = getHero(tokens[1],Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
                    h.setFacingRight(facingRight);
                    
                    list.add(h);
                }
            }
            return new Formation(list);
        }
        catch(Exception e){
            return new Formation();
        }
        
    }
    
    
    
    private static void initiateMonsters(){
        monsters = new Monster[Elements.MONSTER_ELEMENTS.length][Monster.TOTAL_UNIQUE_TIERS * Monster.getNumTimesRepeat()];
        monsterNames = new ArrayList();
        
        
        try{
            Scanner s = new Scanner(new File("creature_data/monster_data.csv"));
            s.nextLine();//ignore first row. It's only labels
            while(s.hasNext()){
                String[] tokens = s.nextLine().split(",");
                String name = tokens[0];
                Element element = Elements.parseElement(tokens[1]);
                int att = Integer.parseInt(tokens[2]);
                int HP = Integer.parseInt(tokens[3]);
                int tier = Integer.parseInt(tokens[4]);
                
                long followers;
                try{
                    followers = Long.parseLong(tokens[5]);
                }
                catch (NumberFormatException e){
                    double tempFollowers = Double.parseDouble(tokens[5]);
                    followers = (long)tempFollowers;
                }
                
                Skill skill = parseSkill(tokens[6]);

                Monster m = new Monster(element,att,HP,tier,followers,skill);
                m.attatchSkill();
                m.setRuneSkill(new Nothing(m));

                monsterNames.add(name);
                monsters[m.getElement().ordinal()][m.getTier()-1] = m;//duplicate entries?
                IDToNameMap.put(m.getID(),name);
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("Error reading monster data file");
        }
        
    }
    
    private static void initiateHeroes() {
        
        heroes = new HashMap<>();
        heroNames = new ArrayList<>();
        nickNameToNameMap = new HashMap<>();
        nameToNickNameMap = new HashMap<>();
        IDToSourceMap = new HashMap<>();
        
        try{
            Scanner s = new Scanner(new File("creature_data/hero_data.csv"));
            s.nextLine();//ignore first row. It's only labels
            while(s.hasNext()){
                String[] tokens = s.nextLine().split(",");
                
                String name = tokens[0];
                int ID = Integer.parseInt(tokens[1]);
                Rarity rarity = parseRarity(tokens[3]);
                Element element = Elements.parseElement(tokens[4]);
                int baseAtt = Integer.parseInt(tokens[5]);
                int baseHP = Integer.parseInt(tokens[6]);
                Skill skill = parseSkill(tokens[7]);
                int p1Health = Integer.parseInt(tokens[8]);
                int p2Att = Integer.parseInt(tokens[9]);
                int p4Stats = Integer.parseInt(tokens[10]);
                Skill p5Skill = parseSkill(tokens[11]);
                Skill p6Skill = parseSkill(tokens[12]);

                Hero h = new Hero(element,baseAtt,baseHP,rarity,ID,skill,p1Health,p2Att,p4Stats,p5Skill,p6Skill);
                
                h.attatchSkill();
                h.setRuneSkill(new Nothing(h));
                    h.levelUp(1);
                    h.promote(0);
                    
                       
                    
                    IDToSourceMap.put(h.getID(), parseSource(tokens[2]));
                    heroes.put(name,h);
                    heroes.put(name.toLowerCase(),h);
                    heroNames.add(name);
                    IDToNameMap.put(h.getID(),name);
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("Error reading hero data file");
        }
        //nullCreature
        nullCreature = new Hero(Element.VOID,0,0,Rarity.COMMON,-1,new Nothing(null),0,0,0, new Nothing(null), new Nothing(null));//use instead of multiple threads?
        nullCreature.attatchSkill();
        //heroes.put("Nothing",nullCreature);
        //heroes.put(name.toLowerCase(),nullCreature);
        //heroNames.add("Nothing");
        IDToNameMap.put(nullCreature.getID(),"Nothing");
        
        
    }
    
    private static Source parseSource(String str){
        
        switch(str){
            case "Chest": return Source.CHEST;
            case "Ascend": return Source.ASCEND;
            case "Quest": return Source.QUEST;
            case "Season": return Source.SEASON;
            case "Event": return Source.EVENT;
            case "Special": return Source.SPECIAL;
            case "Auction": return Source.AUCTION;
            case "Shop": return Source.SHOP;
            default: return null;
        }
    }
    
    private static Color chestColor = new Color(255,255,150);
    private static Color ascendColor = new Color(165,225,225);
    private static Color questColor = new Color(225,183,236);
    private static Color seasonColor = new Color(255,190,135);
    private static Color eventColor = new Color(172,255,172);
    private static Color specialColor = new Color(255,180,180);
    private static Color auctionColor = new Color(200,200,200);
    private static Color shopColor = new Color(180,180,255);
    private static Color defaultColor = Color.WHITE;
    
    
    public static Color sourceToColor(Source s){
        switch(s){
            case CHEST: return chestColor;
            case ASCEND: return ascendColor;
            case QUEST: return questColor;
            case SEASON: return seasonColor;
            case EVENT: return eventColor;
            case SPECIAL: return specialColor;
            case AUCTION: return auctionColor;
            case SHOP: return shopColor;
            default: return defaultColor;
        }
    }
    
    public static Source IDToSource(int ID){
        return IDToSourceMap.get(ID);
    }
    
    public static Skill parseSkill(String str){
        String[] tokens = str.split(" ");
        
        switch (tokens[0]){
            case "Absorb": return new Absorb(null,Double.parseDouble(tokens[1]));
            case "Affinity": return new Affinity(null,Double.parseDouble(tokens[1]));
            case "AOE": return new AOE(null,Integer.parseInt(tokens[1]));
            case "AntiAOE": return new AntiAOE(null,Double.parseDouble(tokens[1]));
            case "AntiAOESelf": return new AntiAOESelf(null,Double.parseDouble(tokens[1]));
            case "ArmorAura": return new ArmorAura(null,Integer.parseInt(tokens[1]),Elements.parseElement(tokens[2]));
            case "AttackPercentBoost": return new AttackPercentBoost(null,Double.parseDouble(tokens[1]));
            case "AttackBoostAura": return new AttackBoostAura(null,Integer.parseInt(tokens[1]),Elements.parseElement(tokens[2]));
            case "AttackPercentAura": return new AttackPercentAura(null,Double.parseDouble(tokens[1]));
            case "Berserk": return new Berserk(null,Double.parseDouble(tokens[1]),Integer.parseInt(tokens[2]),Boolean.parseBoolean(tokens[3]));
            case "BloodBomb": return new BloodBomb(null,Integer.parseInt(tokens[1]));
            case "BloodLust": return new BloodLust(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
            case "CriticalHit": return new CriticalHit(null,Double.parseDouble(tokens[1]));
            case "DamageDodge": return new DamageDodge(null,Integer.parseInt(tokens[1]));
            case "EasterStatLevelBoost": return new EasterStatLevelBoost(null,Double.parseDouble(tokens[1]));
            case "ElementBoostPlusAbsorb": return new ElementBoostPlusAbsorb(null,Elements.parseElement(tokens[1]),Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]));
            case "ElementDamageBoost": return new ElementDamageBoost(null,Elements.parseElement(tokens[1]),Double.parseDouble(tokens[2]));
            case "EvenField": return new EvenField(null,Integer.parseInt(tokens[1]));
            case "Execute": return new Execute(null,Double.parseDouble(tokens[1]));
            case "ExtraArmorBoost": return new ExtraArmorBoost(null,Double.parseDouble(tokens[1]));
            case "ExtraAttackBoost": return new ExtraAttackBoost(null,Double.parseDouble(tokens[1]));
            case "ExtraHeal": return new ExtraHeal(null,Double.parseDouble(tokens[1]));
            case "FinishingBlow": return new FinishingBlow(null,Integer.parseInt(tokens[1]));
            case "Fortify": return new Fortify(null,Integer.parseInt(tokens[1]));
            case "Heal": return new Heal(null,Integer.parseInt(tokens[1]));
            case "HealFirst": return new HealFirst(null,Integer.parseInt(tokens[1]));
            case "HealthyAOE": return new HealthyAOE(null,Double.parseDouble(tokens[1]));
            case "HealthyAttack": return new HealthyAttack(null,Double.parseDouble(tokens[1]));
            case "HPBoost": return new HPBoost(null,Double.parseDouble(tokens[1]));
            case "IncreasingAOE": return new IncreasingAOE(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
            case "Inferno": return new Inferno(null,Double.parseDouble(tokens[1]));
            case "Intercept": return new Intercept(null,Double.parseDouble(tokens[1]));
            case "LifeSteal": return new LifeSteal(null,Integer.parseInt(tokens[1]));
            case "MonsterBuff": return new MonsterBuff(null,Double.parseDouble(tokens[1]));
            case "NoHeroes": return new NoHeroes(null);
            case "Nothing": return new Nothing(null);
            case "NoUnits": return new NoUnits(null);
            case "PartingGift": return new PartingGift(null,Double.parseDouble(tokens[1]));
            case "PositionAttToHealth": return new PositionAttToHealth(null,Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
            case "Purity": return new Purity(null,Double.parseDouble(tokens[1]));
            case "Rainbow": return new Rainbow(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
            case "RandomStatBoost": return new RandomStatBoost(null,Integer.parseInt(tokens[1]));
            case "RandomTarget": return new RandomTarget(null,Boolean.parseBoolean(tokens[1]));
            case "RangedAttack": return new RangedAttack(null,Integer.parseInt(tokens[1]));
            case "Recover": return new Recover(null,Integer.parseInt(tokens[1]));
            case "RecoverPercent": return new RecoverPercent(null,Double.parseDouble(tokens[1]));
            case "Reflect": return new Reflect(null,Double.parseDouble(tokens[1]));
            case "Revenge": return new Revenge(null,Double.parseDouble(tokens[1]));
            case "Revive": return new Revive(null,Double.parseDouble(tokens[1]));
            case "Ricochet": return new Ricochet(null,Double.parseDouble(tokens[1]),Integer.parseInt(tokens[2]));
            case "ScaleableAOEReflect": return new ScaleableAOEReflect(null,Double.parseDouble(tokens[1]));
            case "ScaleableAbsorbPercent": return new ScaleableAbsorbPercent(null,Double.parseDouble(tokens[1]));
            case "ScaleableAOE": return new ScaleableAOE(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableAntiAOE": return new ScaleableAntiAOE(null,Double.parseDouble(tokens[1]));
            case "ScaleableArmorAura": return new ScaleableArmorAura(null,Integer.parseInt(tokens[1]),Elements.parseElement(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableAttackBoostAura": return new ScaleableAttackBoostAura(null,Integer.parseInt(tokens[1]),Elements.parseElement(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableBloodBomb": return new ScaleableBloodBomb(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableFlatField": return new ScaleableFlatField(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableHeal": return new ScaleableHeal(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableHiddenCharger": return new ScaleableHiddenCharger(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Double.parseDouble(tokens[4]));
            case "ScaleableGrowingAttAura": return new ScaleableGrowingAttAura(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableLifeSteal": return new ScaleableLifeSteal(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableOneTimeAbsorb": return new ScaleableOneTimeAbsorb(null,Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleablePercentAtt": return new ScaleablePercentAtt(null,Double.parseDouble(tokens[1]));
            case "ScaleablePredator": return new ScaleablePredator(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableSacrifice": return new ScaleableSacrifice(null,Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableStartingDamage": return new ScaleableStartingDamage(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableStatAura": return new ScaleableStatAura(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Elements.parseElement(tokens[3]),Double.parseDouble(tokens[4]));
            case "ScaleableStatAuraLifeSteal": return new ScaleableStatAuraLifeSteal(null,Integer.parseInt(tokens[1]),Double.parseDouble(tokens[2]));
            case "ScaleableUnitBuff": return new ScaleableUnitBuff(null,Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
            case "SelfArmor": return new SelfArmor(null,Integer.parseInt(tokens[1]));
            case "StartingDamage": return new StartingDamage(null,Integer.parseInt(tokens[1]));
            case "StatAura": return new StatAura(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Elements.parseElement(tokens[3]));
            case "StatAuraLifeSteal": return new StatAuraLifeSteal(null,Integer.parseInt(tokens[1]));
            case "StatLevelBoost": return new StatLevelBoost(null,Double.parseDouble(tokens[1]));
            case "Simmer": return new Simmer(null,Double.parseDouble(tokens[1]));
            case "TargetedReflect": return new TargetedReflect(null,Double.parseDouble(tokens[1]));
            case "Thorns": return new Thorns(null,Integer.parseInt(tokens[1]));
            case "ThornsAll": return new ThornsAll(null,Integer.parseInt(tokens[1]));
            case "Train": return new Train(null,Integer.parseInt(tokens[1]));
            case "UnitBuff": return new UnitBuff(null,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
            case "Vampyrism": return new Vampyrism(null,Double.parseDouble(tokens[1]));
            case "WildCard": return new WildCard(null,Double.parseDouble(tokens[1]));
            case "Wither": return new Wither(null,Double.parseDouble(tokens[1]));
            case "Wrath": return new Wrath(null,Integer.parseInt(tokens[1]));
            case "WrathPercentAtt": return new WrathPercentAtt(null,Double.parseDouble(tokens[1]));
            default: return null;
        }
    }
    
    private static Rarity parseRarity(String str) {
        switch (str){
            case "Common": return Rarity.COMMON;
            case "Rare": return Rarity.RARE;
            case "Legendary": return Rarity.LEGENDARY;
            case "Ascended": return Rarity.ASCENDED;
            default: return null;
        }
    }
    
    public static String RarityToString(Rarity rarity) {
        switch (rarity){
            case COMMON: return "Common";
            case RARE: return "Rare";
            case LEGENDARY: return "Legendary";
            case ASCENDED: return "Ascended";
            default: return null;
        }
    }
    
    private static void initiateWorldBosses() {
        worldBosses = new HashMap<>();
        worldBossNames = new ArrayList<>();
        
        try{
            Scanner s = new Scanner(new File("creature_data/boss_data.csv"));
            s.nextLine();//ignore first row. It's only labels
            while(s.hasNext()){
                String[] tokens = s.nextLine().split(",");
                String name = tokens[0];
                int ID = Integer.parseInt(tokens[1]);
                Element element = Elements.parseElement(tokens[2]);
                int baseAtt = Integer.parseInt(tokens[3]);
                Skill skill = parseSkill(tokens[4]);
                

                WorldBoss b = new WorldBoss(element,baseAtt,ID,skill);
                b.attatchSkill();
                b.setRuneSkill(new Nothing(b));
                worldBosses.put(name,b);
                worldBossNames.add(name);
                IDToNameMap.put(b.getID(),name);
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("Error reading monster data file");
        }
        
        
    }
    
    public static RuneSkill[] getRuneSkills() {
        return Arrays.copyOf(RuneSkills, RuneSkills.length);
    }

    private static void initiateRuneSkills() {
        RuneSkills = new RuneSkill[]{
            new Affinity(null,0.3),
            new Revive(null,0.5),
            new AntiAOESelf(null,0.5),
            new AttackPercentBoost(null,1.5),
            new ExtraArmorBoost(null,2),
            new ExtraAttackBoost(null,2),
            new ExtraHeal(null,2),
            new HPBoost(null,1.5),
            
            new NoHeroes(null),
            new NoUnits(null)};
    }

    public static String getOrderType(boolean yourData) {
        try{
            Scanner sc;
            if (yourData){
                sc = new Scanner(new File("save data/your sort.txt"));
            }
            else{
                sc = new Scanner(new File("save data/enemy sort.txt"));
            }
            String ans = sc.nextLine();
            sc.close();
            return ans;
        }
        catch(FileNotFoundException e){
            
        }
        return "Source";
    }

    
    
}
