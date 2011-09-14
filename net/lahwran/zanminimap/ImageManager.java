/**
 * 
 */
package net.lahwran.zanminimap;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

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
        if (hasGLImage && hasChanged) {
            obfhub.deleteTexture(glImage);
        }
        GL11.glPushMatrix();
        glImage = obfhub.tex(image);
        GL11.glPopMatrix();
        hasGLImage = true;
    }
}
