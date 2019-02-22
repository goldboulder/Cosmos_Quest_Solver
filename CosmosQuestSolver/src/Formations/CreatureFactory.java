/*

 */
package Formations;

import Formations.Elements.Element;
import Formations.Hero.Rarity;
import SpecialAbilities.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

//uses the flyweight and prototype patterns to generate new objects.
//these include monsters, heroes, world bosses, and images
//these assets are generated when loading a frame (not the main menu) for the 
//first time.
public class CreatureFactory {
    
    private static Monster[][] monsters;
    private static HashMap<String,Hero> heroes;
    private static HashMap<String,WorldBoss> worldBosses;
    private static ArrayList<String> monsterNames;
    private static ArrayList<String> heroNames;
    private static ArrayList<String> worldBossNames;
    private static HashMap<Integer,String> IDToNameMap;
    private static HashMap<String,String> nickNameMap;
    

    public static final int MAX_QUESTS = 160;


    public static void initiate(){
        IDToNameMap = new HashMap<>();
        initiateMonsters();
        initiateHeroes();
        initiateWorldBosses();
        loadNickNames();
    }
    
    private static void loadNickNames(){
        nickNameMap = new HashMap<>();
        
        //set default nicknames(same as normal name)
        for (String name : IDToNameMap.values()){
            nickNameMap.put(name,name);
        }
        
        try {
            Scanner s = new Scanner(new File("creature_data/hero nicknames.txt"));
            
            while(s.hasNext()){
                String[] tokens = s.nextLine().split("->");
                nickNameMap.put(tokens[0],tokens[1]);
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println("Hero nickname file not found");
        }
    }
    
    public static String getCreatureName(int ID){
        return IDToNameMap.get(ID);
    }
    
    public static String getCreatureNickName(int ID){
        return nickNameMap.get(IDToNameMap.get(ID));
    }
    
    public static Monster getMonster(Element element, int tier){
        return (Monster) monsters[element.ordinal()][tier - 1].getCopy();
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
        Hero hero = (Hero) heroes.get(name).getCopy();
        hero.levelUp(level);
        
        hero.promote(promoteLevel);
        return hero;
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
        
        File[] files = new File("creature_data/monsters").listFiles();
        

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try{
                    Monster m = getMonsterFromFile(file);
                    m.attatchSpecialAbility();
                    
                    String name = file.getName().substring(0, file.getName().length()-4);
                    
                    monsterNames.add(name);
                    monsters[m.getElement().ordinal()][m.getTier()-1] = m;//duplicate entries?
                    IDToNameMap.put(m.getID(),name);
                }
                catch(FileNotFoundException ex){
                    System.out.println("Error reading monster file: " + file.getName());
                }
            }
        }
        
        
    }

    private static void initiateHeroes() {
        
        heroes = new HashMap<>();
        heroNames = new ArrayList<>();
        
        File[] files = new File("creature_data/heroes").listFiles();
        

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try{
                    Hero hero = getHeroFromFile(file);
                    hero.attatchSpecialAbility();
                    hero.levelUp(1);
                    hero.promote(0);
                    
                    String name = file.getName().substring(0, file.getName().length()-4);
                    
                    heroes.put(name,hero);
                    heroNames.add(name);
                    IDToNameMap.put(hero.getID(),name);
                }
                catch(FileNotFoundException ex){
                    System.out.println("Error reading hero file: " + file.getName());
                }
            }
        }
        
        
    }
    
    public static SpecialAbility parseAbility(String str){
        String[] tokens = str.split(" ");
        
        switch (tokens[1]){
            case "AOE": return new AOE(null,Integer.parseInt(tokens[2]));
            case "AntiAOE": return new AntiAOE(null,Double.parseDouble(tokens[2]));
            case "Berserk": return new Berserk(null,Double.parseDouble(tokens[2]));
            case "BloodBomb": return new BloodBomb(null,Integer.parseInt(tokens[2]));
            case "CriticalHit": return new CriticalHit(null,Double.parseDouble(tokens[2]));
            case "DamageDodge": return new DamageDodge(null,Integer.parseInt(tokens[2]));
            case "ElementDamageBoost": return new ElementDamageBoost(null,Elements.parseElement(tokens[2]),Double.parseDouble(tokens[3]));
            case "EvenField": return new EvenField(null,Integer.parseInt(tokens[2]));
            case "Execute": return new Execute(null,Double.parseDouble(tokens[2]));
            case "Heal": return new Heal(null,Integer.parseInt(tokens[2]));
            case "Inferno": return new Inferno(null,Double.parseDouble(tokens[2]));
            case "Intercept": return new Intercept(null,Double.parseDouble(tokens[2]));
            case "LifeSteal": return new LifeSteal(null,Integer.parseInt(tokens[2]));
            case "MonsterBuff": return new MonsterBuff(null,Double.parseDouble(tokens[2]));
            case "Nothing": return new Nothing(null);
            case "PartingGift": return new PartingGift(null,Double.parseDouble(tokens[2]));
            case "Purity": return new Purity(null,Double.parseDouble(tokens[2]));
            case "Rainbow": return new Rainbow(null,Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
            case "RandomStatBoost": return new RandomStatBoost(null,Integer.parseInt(tokens[2]));
            case "RandomTarget": return new RandomTarget(null,Boolean.parseBoolean(tokens[2]));
            case "Reflect": return new Reflect(null,Double.parseDouble(tokens[2]));
            case "Revenge": return new Revenge(null,Double.parseDouble(tokens[2]));
            case "Ricochet": return new Ricochet(null,Double.parseDouble(tokens[2]),Integer.parseInt(tokens[3]));
            case "ScaleableAOEReflect": return new ScaleableAOEReflect(null,Double.parseDouble(tokens[2]));
            case "ScaleableAbsorbPercent": return new ScaleableAbsorbPercent(null,Double.parseDouble(tokens[2]));
            case "ScaleableAOE": return new ScaleableAOE(null,Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableAntiAOE": return new ScaleableAntiAOE(null,Double.parseDouble(tokens[2]));
            case "ScaleableBloodBomb": return new ScaleableBloodBomb(null,Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableHeal": return new ScaleableHeal(null,Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableLifeSteal": return new ScaleableLifeSteal(null,Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleablePercentAtt": return new ScaleablePercentAtt(null,Double.parseDouble(tokens[2]));
            case "ScaleableSacrifice": return new ScaleableSacrifice(null,Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]),Double.parseDouble(tokens[4]));
            case "ScaleableStartingDamage": return new ScaleableStartingDamage(null,Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
            case "ScaleableStatAura": return new ScaleableStatAura(null,Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Elements.parseElement(tokens[4]),Double.parseDouble(tokens[5]));
            case "ScaleableUnitBuff": return new ScaleableUnitBuff(null,Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]));
            case "TargetedReflect": return new TargetedReflect(null,Double.parseDouble(tokens[2]));
            case "StartingDamage": return new StartingDamage(null,Integer.parseInt(tokens[2]));
            case "StatAura": return new StatAura(null,Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Elements.parseElement(tokens[4]));
            case "StatLevelBoost": return new StatLevelBoost(null,Double.parseDouble(tokens[2]));
            case "Simmer": return new Simmer(null,Double.parseDouble(tokens[2]));
            case "Train": return new Train(null,Integer.parseInt(tokens[2]));
            case "UnitBuff": return new UnitBuff(null,Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
            case "Vampyrism": return new Vampyrism(null,Double.parseDouble(tokens[2]));
            case "Wither": return new Wither(null,Double.parseDouble(tokens[2]));
            case "Wrath": return new Wrath(null,Integer.parseInt(tokens[2]));
            case "WrathPercentAtt": return new WrathPercentAtt(null,Double.parseDouble(tokens[2]));
            default: return null;
        }
    }
    
    private static Monster getMonsterFromFile(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        
        Element element = Elements.parseElement(s.nextLine().split(" ")[1]);
        int att = Integer.parseInt(s.nextLine().split(" ")[1]);
        int HP = Integer.parseInt(s.nextLine().split(" ")[1]);
        int tier = Integer.parseInt(s.nextLine().split(" ")[1]);
        long followers = Long.parseLong(s.nextLine().split(" ")[1]);
        SpecialAbility ability = parseAbility(s.nextLine());
        
        return new Monster(element,att,HP,tier,followers,ability);
        
    }
    
    private static Hero getHeroFromFile(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        int id = Integer.parseInt(s.nextLine().split(" ")[1]);
        Rarity rarity = ParseRarity(s.nextLine().split(" ")[1]);
        Element element = Elements.parseElement(s.nextLine().split(" ")[1]);
        int baseAtt = Integer.parseInt(s.nextLine().split(" ")[1]);
        int baseHP = Integer.parseInt(s.nextLine().split(" ")[1]);
        SpecialAbility ability = parseAbility(s.nextLine());
        int tier1HP = Integer.parseInt(s.nextLine().split(" ")[1]);
        int tier2Att = Integer.parseInt(s.nextLine().split(" ")[1]);
        int tier4Stats = Integer.parseInt(s.nextLine().split(" ")[1]);
        SpecialAbility tier5Ability = parseAbility(s.nextLine());
        
        return new Hero(element,baseAtt,baseHP,rarity,id,ability,tier1HP,tier2Att,tier4Stats,tier5Ability);
        
    }
    
    private static Rarity ParseRarity(String str) {
        switch (str){
            case "Common": return Rarity.COMMON;
            case "Rare": return Rarity.RARE;
            case "Legendary": return Rarity.LEGENDARY;
            case "Ascended": return Rarity.ASCENDED;
            default: return null;
        }
    }
    
    private static void initiateWorldBosses() {
        worldBosses = new HashMap<>();
        worldBossNames = new ArrayList<>();
        
        File[] files = new File("creature_data/world bosses").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try{
                    WorldBoss boss = getBossFromFile(file);
                    boss.attatchSpecialAbility();
                    
                    String name = file.getName().substring(0, file.getName().length()-4);
                    
                    worldBosses.put(name,boss);
                    worldBossNames.add(name);
                    IDToNameMap.put(boss.getID(),name);
                }
                catch(FileNotFoundException ex){
                    System.out.println("Error reading boss file: " + file.getName());
                }
            }
        }
        
        
    }
    
    private static WorldBoss getBossFromFile(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        int id = Integer.parseInt(s.nextLine().split(" ")[1]);
        Element element = Elements.parseElement(s.nextLine().split(" ")[1]);
        int baseAtt = Integer.parseInt(s.nextLine().split(" ")[1]);
        SpecialAbility ability = parseAbility(s.nextLine());
        
        return new WorldBoss(element,baseAtt,id,ability);
        
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
