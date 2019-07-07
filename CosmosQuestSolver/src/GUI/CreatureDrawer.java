/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Hero;
import Formations.Monster;
import Formations.WorldBoss;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

//this class is responsible for drawing all creatures, including their pictures,
//stands, stats, auras, and element symbols
public class CreatureDrawer {
    
    //all constants are assuming the dimensions of the picture are 100X100 pixels
    //exact numbers determined through experimentation and what looked good
    public static final int CREATURE_WIDTH = 74;
    public static final int CREATURE_HEIGHT = 73;
    public static final int CREATURE_X = (AssetPanel.CREATURE_PICTURE_SIZE - CREATURE_WIDTH)/2 + 1;
    public static final int CREATURE_Y = 8;
    public static final int ATTACK_STRING_X = 24;
    public static final int HP_STRING_X = 60;
    public static final int STATS_STRING_Y = 89;
    public static final Font STATS_FONT = new Font("Courier", Font.BOLD, 10);
    public static final Font HIGH_STATS_FONT = new Font("Courier", Font.BOLD, 9);
    public static final Font SUPER_STATS_FONT = new Font("Courier", Font.BOLD, 8);
    public static final Color NORMAL_STATS_COLOR = Color.WHITE;
    public static final Color BUFFED_STATS_COLOR = Color.GREEN;
    public static final Color NERFED_STATS_COLOR = Color.RED;
    
    public static void drawFuriousAura(Creature c, Graphics g){
        BufferedImage image = null;
        switch(c.getElement()){
            case AIR: g.drawImage(ImageFactory.getPicture("Stands/Air Aura"), 10, 46, 80, 34, null); break;
            case WATER: g.drawImage(ImageFactory.getPicture("Stands/Water Aura"), 10, 55, 80, 29, null); break;
            case EARTH: g.drawImage(ImageFactory.getPicture("Stands/Earth Aura"), 10, 44, 80, 41, null); break;
            case FIRE: g.drawImage(ImageFactory.getPicture("Stands/Fire Aura"), 10, 50, 80, 36, null); break;
            case VOID: g.drawImage(ImageFactory.getPicture("Stands/Raging Aura"), 10, 48, 80, 36, null); break;
            default: image = ImageFactory.getDefaultImage();
        }
        g.drawImage(image, MONSTER_ELEMENT_X, MONSTER_ELEMENT_Y, MONSTER_ELEMENT_SIZE, MONSTER_ELEMENT_SIZE, null);
    }
    
    public static void drawNumbers(Creature c, Graphics g){
        drawAttackNumbers(c,g);
        drawHPNumbers(c,g);
    }
    
    public static void drawAttackNumbers(Creature c, Graphics g){
        int textCenterNum = 0;//horizontal centering for big numbers
        if (c.getCurrentAtt() < c.getBaseAtt()){
            g.setColor(NERFED_STATS_COLOR);
        }
        else if (c.getCurrentAtt()== c.getBaseAtt()){
            g.setColor(NORMAL_STATS_COLOR);
        }
        else{
            g.setColor(BUFFED_STATS_COLOR);
        }
        
        if (c.getCurrentAtt() >= 10000){
            g.setFont(SUPER_STATS_FONT);
            textCenterNum = -2;
        }
        else if (c.getCurrentAtt() >= 1000){
            g.setFont(HIGH_STATS_FONT);
            textCenterNum = -1;
        }
        else if (c.getCurrentAtt() >= 100){
            g.setFont(STATS_FONT);
        }
        else if (c.getCurrentAtt() >= 10){
            g.setFont(STATS_FONT);
            textCenterNum = 2;
        }
        else{
            g.setFont(STATS_FONT);
            textCenterNum = 5;
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.drawString(Long.toString(c.getCurrentAtt()), ATTACK_STRING_X + textCenterNum, STATS_STRING_Y);
    }
    
    public static void drawHPNumbers(Creature c, Graphics g){
        int textCenterNum = 0;//horizontal centering for big numbers
        if (c.getCurrentHP() < c.getBaseHP()){
            g.setColor(NERFED_STATS_COLOR);
        }
        else if (c.getCurrentHP() == c.getBaseHP()){
            g.setColor(NORMAL_STATS_COLOR);
        }
        else{
            g.setColor(BUFFED_STATS_COLOR);
        }
        
        if (c.getCurrentHP() >= 10000){
            g.setFont(SUPER_STATS_FONT);
            textCenterNum = -2;
        }
        else if (c.getCurrentHP() >= 1000){
            g.setFont(HIGH_STATS_FONT);
            textCenterNum = -1;
        }
        else if (c.getCurrentHP() >= 100){
            g.setFont(STATS_FONT);
        }
        else if (c.getCurrentHP() >= 10){
            g.setFont(STATS_FONT);
            textCenterNum = 2;
        }
        else{
            g.setFont(STATS_FONT);
            textCenterNum = 5;
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.drawString(Integer.toString(c.getCurrentHP()), HP_STRING_X + textCenterNum, STATS_STRING_Y);
    }
    
    
    
    
//__________________Hero__________________________________________________
    public static final Font LEVEL_FONT = new Font("Courier", Font.BOLD, 9);
    
    public static final int LEVEL_STRING_X = 46;
    
    public static final int COMMON_STAND_WIDTH = 83;
    public static final int COMMON_STAND_HEIGHT = 30;
    public static final int COMMON_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - COMMON_STAND_WIDTH)/2;
    public static final int COMMON_STAND_Y = 65;
    
    public static final int RARE_STAND_WIDTH = 83;
    public static final int RARE_STAND_HEIGHT = 39;
    public static final int RARE_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - RARE_STAND_WIDTH)/2;
    public static final int RARE_STAND_Y = 56;
    
    public static final int LEGENDARY_STAND_WIDTH = 83;
    public static final int LEGENDARY_STAND_HEIGHT = 41;
    public static final int LEGENDARY_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - LEGENDARY_STAND_WIDTH)/2;
    public static final int LEGENDARY_STAND_Y = 52;
    
    public static final int ASCENDED_STAND_WIDTH = 85;
    public static final int ASCENDED_STAND_HEIGHT = 56;
    public static final int ASCENDED_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - ASCENDED_STAND_WIDTH)/2;
    public static final int ASCENDED_STAND_Y = 39;
    
    public static final int HERO_ELEMENT_SIZE = 18;
    public static final int HERO_ELEMENT_X = (AssetPanel.CREATURE_PICTURE_SIZE - HERO_ELEMENT_SIZE)/2 + 1;
    public static final int HERO_ELEMENT_Y = 75;
    
    public static void drawCreature(Hero h, Graphics g){
        drawStand(h,g);
        drawHero(h,g);
        drawPromotion(h,g);
        drawElement(h,g);
        drawNumbers(h,g);
        drawLevelNumber(h,g);
    }

    
    
    private static void drawStand(Hero h, Graphics g){
        BufferedImage image = null;
        switch(h.getRarity()){
            case COMMON:
                image = ImageFactory.getPicture("Stands/Common Stand");
                g.drawImage(image, COMMON_STAND_X, COMMON_STAND_Y, COMMON_STAND_WIDTH, COMMON_STAND_HEIGHT, null);
                
            break;
            case RARE: 
                image = ImageFactory.getPicture("Stands/Rare Stand"); 
                g.drawImage(image, RARE_STAND_X, RARE_STAND_Y, RARE_STAND_WIDTH, RARE_STAND_HEIGHT, null);
            break;
            case LEGENDARY: 
                image = ImageFactory.getPicture("Stands/Legendary Stand"); 
                g.drawImage(image, LEGENDARY_STAND_X, LEGENDARY_STAND_Y, LEGENDARY_STAND_WIDTH, LEGENDARY_STAND_HEIGHT, null);
            break;
            case ASCENDED: 
                image = ImageFactory.getPicture("Stands/Ascended Stand"); 
                g.drawImage(image, ASCENDED_STAND_X, ASCENDED_STAND_Y, ASCENDED_STAND_WIDTH, ASCENDED_STAND_HEIGHT, null);
            break;
            default: image = ImageFactory.getDefaultImage();
        }
        
        
        
    }
    
    protected static void drawHero(Hero h, Graphics g){
        
        if (h.getName().equals("Guy")){
            BufferedImage fireworks = ImageFactory.getPicture("Stands/Firework Aura");
            g.drawImage(fireworks, AssetPanel.CREATURE_PICTURE_SIZE - CREATURE_X, CREATURE_Y + 4, -CREATURE_WIDTH, CREATURE_HEIGHT - 8, null);
        }
        
        BufferedImage image = ImageFactory.getPicture(h.getImageAddress());
        
        if (h.isFacingRight()){
            g.drawImage(image, CREATURE_X, CREATURE_Y, CREATURE_WIDTH, CREATURE_HEIGHT, null);
        }
        else{
            g.drawImage(image, AssetPanel.CREATURE_PICTURE_SIZE - CREATURE_X, CREATURE_Y, -CREATURE_WIDTH, CREATURE_HEIGHT, null);
        }
        
        if (CreatureDrawer.drawFuriousAura(h)){
            drawFuriousAura(h,g);
        }
        
        
    }
    
    private static boolean drawFuriousAura(Hero h){
        String name = h.getName();
        if (name.equals("Ascended Shygu") || 
                name.equals("Ascended Thert") || 
                name.equals("Ascended Lord Kirk") || 
                name.equals("Ascended Neptunius") || 
                name.equals("Ascended Hosokawa") || 
                name.equals("Ascended Takeda") || 
                name.equals("Ascended Hirate") || 
                name.equals("Ascended Hattori") ||
                name.equals("Ascended Defile") ||
                name.equals("Ascended Seethe") ||
                name.equals("Ascended Mahatma") || 
                name.equals("Ascended Jade") || 
                name.equals("Ascended Edana") || 
                name.equals("Ascended Dybbuk") ||
                name.equals("Ascended Nerissa") ||
                name.equals("Ascended Wanderer") ||
                name.equals("45c3nd3d R31")){
            return true;
        }
        else{
            return false;
        }
    }
    
    
    public static final int PROMOTE_STAR_SIZE = 20;
    public static final int P5_STAR_SIZE = 28;
    public static final int P6_STAR_SIZE = 35;
    private static void drawPromotion(Hero h,Graphics g){
        BufferedImage promoteStar = ImageFactory.getPicture("Stands/PromoteStar");
        BufferedImage p5Star = ImageFactory.getPicture("Stands/P5Star");
        BufferedImage p6Star = ImageFactory.getPicture("Stands/P6Star");
        int xOffset = h.isFacingRight() ? 2 : AssetPanel.CREATURE_PICTURE_SIZE - PROMOTE_STAR_SIZE - 2;
        g.translate(xOffset, 2);
        switch(h.getPromoteLevel()){
            case 0: 
            break;
            case 1:
                g.drawImage(promoteStar, 0, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
            break;
            case 2:
                g.drawImage(promoteStar, 0, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
            break;
            case 3:
                g.drawImage(promoteStar, 0, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE*2, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
            break;
            case 4:
                g.drawImage(promoteStar, 0, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE*2, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
                g.drawImage(promoteStar, 0, PROMOTE_STAR_SIZE*3, PROMOTE_STAR_SIZE, PROMOTE_STAR_SIZE, null);
            break;
            case 5:
                if (h.isFacingRight()){//5th star is slightly bigger
                    g.drawImage(p5Star, 0, 0, P5_STAR_SIZE, P5_STAR_SIZE, null);
                }
                else{
                    g.drawImage(p5Star, PROMOTE_STAR_SIZE - P5_STAR_SIZE, 0, P5_STAR_SIZE, P5_STAR_SIZE, null);
                }
            break;
            case 6:
                if (h.isFacingRight()){//6th star is even bigger
                    g.drawImage(p6Star, 0, 0, P6_STAR_SIZE - 2, P6_STAR_SIZE, null);
                }
                else{
                    g.drawImage(p6Star, PROMOTE_STAR_SIZE - P6_STAR_SIZE - 2, 0, P6_STAR_SIZE - 2, P6_STAR_SIZE, null);
                }
            break;
            
        }
        g.translate(-xOffset, -2);
        
    }
    
    private static void drawElement(Hero h, Graphics g){
        BufferedImage image = null;
        switch(h.getElement()){
            case AIR: image = ImageFactory.getPicture("Stands/HeroElementAir"); break;
            case WATER: image = ImageFactory.getPicture("Stands/HeroElementWater"); break;
            case EARTH: image = ImageFactory.getPicture("Stands/HeroElementEarth"); break;
            case FIRE: image = ImageFactory.getPicture("Stands/HeroElementFire"); break;
            case VOID: image = ImageFactory.getPicture("Stands/HeroElementVoid"); break;
            default: image = ImageFactory.getDefaultImage();
        }
        
        g.drawImage(image, HERO_ELEMENT_X, HERO_ELEMENT_Y, HERO_ELEMENT_SIZE, HERO_ELEMENT_SIZE, null);
        
    }
    
    public static void drawLevelNumber(Hero h, Graphics g){
        if (!Hero.validHeroLevel(h.getLevel())/* || (h.getLevel() < Hero.MAX_NORMAL_LEVEL && h.getPromoteLevel() != 0)*/){
            g.setColor(NERFED_STATS_COLOR);//for showing level is not seen in-game
        }
        else{
            g.setColor(NORMAL_STATS_COLOR);
        }
        g.setFont(LEVEL_FONT);
        if (h.getLevel() == 1000){
            g.drawString("1k", LEVEL_STRING_X, STATS_STRING_Y - 3);
        }
        else if (h.getLevel() < 10){
            g.drawString(Integer.toString(h.getLevel()), LEVEL_STRING_X + 2, STATS_STRING_Y - 3);
        }
        else{
            g.drawString(Integer.toString(h.getLevel()), LEVEL_STRING_X, STATS_STRING_Y - 3);
        }
    }
    
    
//__________________Monster__________________________________________________
    public static final int NORMAL_STAND_WIDTH = 83;
    public static final int NORMAL_STAND_HEIGHT = 30;
    public static final int NORMAL_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - NORMAL_STAND_WIDTH)/2;
    public static final int NORMAL_STAND_Y = 65;
    
    public static final int FURIOUS_STAND_WIDTH = 86;
    public static final int FURIOUS_STAND_HEIGHT = 56;
    public static final int FURIOUS_STAND_X = (AssetPanel.CREATURE_PICTURE_SIZE - FURIOUS_STAND_WIDTH)/2;
    public static final int FURIOUS_STAND_Y = 39;
    
    
    public static final int MONSTER_ELEMENT_SIZE = 13;
    public static final int MONSTER_ELEMENT_X = (AssetPanel.CREATURE_PICTURE_SIZE - MONSTER_ELEMENT_SIZE)/2;
    public static final int MONSTER_ELEMENT_Y = 78;
    
    public static void drawCreature(Monster m, Graphics g){
        drawStand(m,g);
        drawMonster(m,g);
        drawElement(m,g);
        drawNumbers(m,g);
    }
    
    private static void drawStand(Monster m, Graphics g){
        BufferedImage image = null;
        
        if (m.getTier() > Monster.TOTAL_UNIQUE_TIERS){
            image = ImageFactory.getPicture("Stands/Furious Monster Stand");
                g.drawImage(image, FURIOUS_STAND_X, FURIOUS_STAND_Y, FURIOUS_STAND_WIDTH, FURIOUS_STAND_HEIGHT, null);
        }
        else{
            image = ImageFactory.getPicture("Stands/Monster Stand");
                g.drawImage(image, NORMAL_STAND_X, NORMAL_STAND_Y, NORMAL_STAND_WIDTH, NORMAL_STAND_HEIGHT, null);
        }
         
        
    }
    
    protected static void drawMonster(Monster m, Graphics g){
        BufferedImage image;
        
        int strIndex = (m.getTier()-1)/Monster.TOTAL_UNIQUE_TIERS;
        
        String subName = m.getName().substring(Monster.REPEAT_TIER_STRING[strIndex].length());
        
        image = ImageFactory.getPicture("Creatures/Monsters/" + subName);
        
        if (m.isFacingRight()){
            g.drawImage(image, CREATURE_X, CREATURE_Y, CREATURE_WIDTH, CREATURE_HEIGHT, null);
        }
        else{
            g.drawImage(image, AssetPanel.CREATURE_PICTURE_SIZE - CREATURE_X, CREATURE_Y, -CREATURE_WIDTH, CREATURE_HEIGHT, null);
        }
        
        //draw aura
        if (m.getTier() > Monster.TOTAL_UNIQUE_TIERS && m.getTier() <= Monster.TOTAL_UNIQUE_TIERS * 2){
            drawFuriousAura(m,g);
        }
        else if (m.getTier() > Monster.TOTAL_UNIQUE_TIERS * 2){
            g.drawImage(ImageFactory.getPicture("Stands/Raging Aura"), 10, 46, 80, 34, null);
        }
        
    }
    
    private static void drawElement(Monster m, Graphics g){
        BufferedImage image = null;
        switch(m.getElement()){
            case AIR: image = ImageFactory.getPicture("Stands/MonsterElementAir"); break;
            case WATER: image = ImageFactory.getPicture("Stands/MonsterElementWater"); break;
            case EARTH: image = ImageFactory.getPicture("Stands/MonsterElementEarth"); break;
            case FIRE: image = ImageFactory.getPicture("Stands/MonsterElementFire"); break;
            case VOID: image = ImageFactory.getPicture("Stands/MonsterElementVoid"); break;
            default: image = ImageFactory.getDefaultImage();
        }
        
        if (m.getTier() > Monster.TOTAL_UNIQUE_TIERS){
            g.drawImage(image, MONSTER_ELEMENT_X, MONSTER_ELEMENT_Y, MONSTER_ELEMENT_SIZE, MONSTER_ELEMENT_SIZE, null);
        }
        else{
            g.drawImage(image, MONSTER_ELEMENT_X+2, MONSTER_ELEMENT_Y+1, MONSTER_ELEMENT_SIZE-1, MONSTER_ELEMENT_SIZE-1, null);
        }
        
    }
    
    
    
    
//__________________World Boss__________________________________________________
    public static void drawCreature(WorldBoss b, Graphics g){
        if (!b.isFacingRight()){
            g.drawImage(ImageFactory.getPicture(b.getImageAddress()), 0, 0, AssetPanel.CREATURE_PICTURE_SIZE, AssetPanel.CREATURE_PICTURE_SIZE, null);
        }
        else{
            g.drawImage(ImageFactory.getPicture(b.getImageAddress()), AssetPanel.CREATURE_PICTURE_SIZE, 0, -AssetPanel.CREATURE_PICTURE_SIZE, AssetPanel.CREATURE_PICTURE_SIZE, null);
        }
    }
    
}
