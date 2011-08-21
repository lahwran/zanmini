/**
 * 
 */
package net.lahwran.zanminimap;

import java.awt.image.BufferedImage;

/**
 * @author lahwran
 *
 */
public class ImageManager {

    private final BufferedImage image;
    
    private int glImage = 0;

    private boolean hasGLImage = false;

    private boolean hasChanged = true;

    /**
     * @param imageSize
     * @param imageSize2
     * @param type
     */
    public ImageManager(int sizeX, int sizeY, int type) {
        image = new BufferedImage(sizeX, sizeY, type);
    }

    public void setRGB(int X, int Y, int color) {
        image.setRGB(X, Y, 0xff000000 | color);
        hasChanged = true;
    }

    public void loadGLImage(ObfHub obfhub) {
        if (hasGLImage) {
            obfhub.deleteTexture(glImage);
        }
        glImage = obfhub.tex(image);
        hasGLImage = true;
    }
}
