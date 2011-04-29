/**
 * 
 */
package net.lahwran.zanminimap;

import java.util.HashMap;

/**
 * Represents the possible types of tint available for block colors
 * @author lahwran
 * 
 */
public enum TintType
{
    //so it turns out that redstone is actually based on the metadata ...
    NONE, GRASS, FOLIAGE, PINE, BIRCH, REDSTONE, COLORMULT;
    public static final HashMap<String, TintType> map;

    public static TintType get(String name)
    {
        return map.get(name);
    }

    static
    {
        map = new HashMap<String, TintType>();
        for (TintType t : TintType.values())
        {
            map.put(t.name(), t);
        }
    }

}
