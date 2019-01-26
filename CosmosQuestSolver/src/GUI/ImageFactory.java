/*

 */
package GUI;

import Formations.CreatureFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class ImageFactory {
    private static HashMap<String,BufferedImage> pictures;
    private static BufferedImage defaultImage;
    
    public static void initiate() {
        pictures = new HashMap<>();
        
        addDefaultImage();
        
        attemptToAddPictures("pictures/Creatures/Monsters");
        attemptToAddPictures("pictures/Creatures/Heroes");
        attemptToAddPictures("pictures/Creatures/World Bosses");
        attemptToAddPictures("pictures/Stands");
        attemptToAddPictures("pictures/Backgrounds");
        attemptToAddPictures("pictures/Others");
    }
    
    public static BufferedImage getPicture(String address){
        BufferedImage img = pictures.get("pictures/" + address + ".png");
        if (img == null){
            img = pictures.get("pictures/" + address + ".PNG");
            if (img == null){
                return defaultImage;
            }
        }
        return img;
    }
    
    public static BufferedImage getDefaultImage(){
        return defaultImage;
    }

    
    
    private static void attemptToAddPictures(String directory){
        File[] files = new File(directory).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                attemptToAddPicture(directory + "/" + file.getName());
            }
        }
    }
    
    
    
    private static void attemptToAddPicture(String address){
        try {
            pictures.put(address, ImageIO.read(new File(address)));
        } catch (IOException ex) {
            
        }
    }
    
    
    private static void addDefaultImage(){
        try {
            defaultImage = ImageIO.read(new File("pictures/Default Image.png"));
        } catch (IOException ex) {
            Logger.getLogger(CreatureFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
