/**
 * 
 */
package net.lahwran.zanminimap;

/**
 * @author lahwran
 *
 */
public class TextureManager {

    private int glRoundmap = 0;
    private int glMMArrow = 0;
    private int glWaypoint = 0;
    private int glMarker = 0;
    private int glMinimap = 0;

    private ObfHub obfhub;
    /**
     * @param zanMinimap
     */
    public TextureManager(ZanMinimap zanminimap) {
        obfhub = zanminimap.obfhub;
    }

    public void loadRoundmap() {
        if (glRoundmap == 0)
            glRoundmap = obfhub.img("/roundmap.png");
        obfhub.disp(glRoundmap);
    }

    public void loadMMArrow() {
        if (glMMArrow == 0)
            glMMArrow = obfhub.img("/mmarrow.png");
        obfhub.disp(glMMArrow);
    }

    public void loadWaypoint() {
        if (glWaypoint == 0)
            glWaypoint = obfhub.img("/waypoint.png");
        obfhub.disp(glWaypoint);
    }

    public void loadMarker() {
        if (glMarker == 0)
            glMarker = obfhub.img("/marker.png");
        obfhub.disp(glMarker);
    }

    public void loadMinimap() {
        if (glMinimap == 0)
            glMinimap = obfhub.img("/minimap.png");
        obfhub.disp(glMinimap);
    }
}
