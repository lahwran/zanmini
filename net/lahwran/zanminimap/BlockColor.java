/**
 * 
 */
package net.lahwran.zanminimap;

/**
 * Represents a block color.
 * @author lahwran
 */
public class BlockColor {
    public final int color;
    public final short alpha;
    public final TintType tintType;
    public BlockColor(int color, int alpha, TintType tintType)
    {
        this.color=color;
        this.alpha=(short)alpha;
        this.tintType=tintType;
    }
}
