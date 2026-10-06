package entities;


public class SpriteEntity {
    String imageName;
    
    public void setImagePath(String s) {
        imageName = s;
    }
    
    public String getImagePath(String folder) {
        if (imageName == "") {
            return null;
        }
        return "/images/" + folder + "/" + imageName;
    }
}
