/**
 * 
 */
package net.lahwran.zanminimap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;

import org.lwjgl.input.Mouse;

import deobf.ch;
import deobf.da;
import deobf.ei;
import deobf.fd;
import deobf.ji;
import deobf.nw;
import deobf.oz;
import deobf.qh;
import deobf.qq;
import deobf.sj;

import net.minecraft.client.Minecraft;

/**
 * @author lahwran
 * 
 */
public class ObfHub {

    /**
     * last dimension we rendered the map in
     */
    public int lastdim = 0;

    /**
     * Minecraft instance
     */
    public Minecraft game;

    /**
     * Tesselator
     */
    public nw lDraw = nw.a;

    /**
     * Font renderer
     */
    public sj lang;

    /**
     * Render texture
     */
    public ji renderEngine;

    private ZanMinimap minimap;

    /**
     * @param minimap minimap instance to initialize with
     */
    public ObfHub(ZanMinimap minimap) {
        this.minimap = minimap;
    }

    /**
     * Add a string to the chat buffer
     * 
     * @param s String to add
     */
    void chatInfo(String s) {
        game.v.a(s);
    }

    /**
     * Get a world name. Returns MpServer or something for multiplayer servers
     * 
     * @return world name
     */
    String getMapName() {
        try {
            fd world = getWorld();
            Class<?> worldclass = world.getClass();
            Field worlddatafield = null;
            while (true) { //

                try {
                    worlddatafield = worldclass.getDeclaredField("x");
                    break;
                } catch (NoSuchFieldException e) {
                    worldclass = worldclass.getSuperclass();
                    continue;
                }
            }
            if (worlddatafield == null)
                return null;
            worlddatafield.setAccessible(true);

            ei worldata;

            worldata = (ei) worlddatafield.get(world);
            return worldata.j();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the server's address
     * 
     * @return server address
     */
    String getServerName() {
        return game.z.C;
    }

    /**
     * Start drawing quads
     */
    void draw_startQuads() {
        lDraw.b();
    }

    /**
     * finish drawing
     */
    void draw_finish() {
        lDraw.a();
    }

    /**
     * Free a GL texture
     * @param texID gl texture to free
     */
    void deleteTexture(int texID) {
        renderEngine.a(texID);
    }

    /**
     * Add a vertex
     * 
     * @param x vertex X coord
     * @param y vertex Y coord
     * @param z vertex Z coord
     */
    void ldraw_addVertex(double x, double y, double z) {
        lDraw.a(x, y, z);
    }

    /**
     * Add a vertex and a corrosponding UV
     * 
     * @param x vertex X coord
     * @param y vertex Y coord
     * @param z vertex Z coord
     * @param u texture U coord
     * @param v texture V coord
     */
    void ldraw_addVertexWithUV(double x, double y, double z, double u, double v) {
        lDraw.a(x, y, z, u, v);
    }

    /**
     * Get mouse X scaled to screen width and screen scale
     * 
     * @param scWidth screen width
     * @return mouse X
     */
    int getMouseX(int scWidth) {
        return Mouse.getX() * (scWidth + ZanMinimap.heightOffset) / game.d;
    }

    /**
     * Get mouse Y scaled to screen height and screen scale
     * 
     * @param scHeight screen height
     * @return mouse Y
     */
    int getMouseY(int scHeight) {
        return (scHeight + ZanMinimap.heightOffset) - Mouse.getY() * (scHeight + ZanMinimap.heightOffset) / this.game.e - 1;
    }

    /**
     * Set the minecraft menu display to null (thereby hiding any shown menu)
     */
    void setMenuNull() {
        game.r = null;
    }

    /**
     * Get minecraft menu as an object (for testing null and such)
     * 
     * @return menu object
     */
    Object getMenu() {
        return game.r;
    }

    /**
     * Check if the menu is showing GuiGameOver
     * 
     * @return true if showing GuiGameOver
     */
    boolean isGameOver() {
        return getMenu() instanceof ch;
    }

    /**
     * Check if the menu is showing GuiConflictWarning or such
     * 
     * @return true if showing GuiConflictWarning
     */
    boolean isConflictWarning() {
        return getMenu() instanceof qh;
    }

    /**
     * Check if any minecraft menus are showing
     * 
     * @return true if menu object is not null
     */
    boolean isMenuShowing() {
        return game.r != null;
    }

    /**
     * Ensure we have a copy of the lang object in Minecraft
     */
    void updateLang() {
        if (lang == null)
            lang = game.q;
    }

    /**
     * Ensure we have a copy of the RenderENgine object in Minecraft
     */
    void updateRenderEngine() {
        if (renderEngine == null)
            renderEngine = this.game.p;
    }

    /**
     * Get the scaled screen size
     * 
     * @return scaled screen size
     */
    int[] getScreenSize() {
        qq scSize = new qq(game.z, game.d, game.e);
        return new int[] { scSize.a(), scSize.b() };
    }

    /**
     * Set the minecraft gui to an instance of GuiScreen, so that while we're
     * drawing our menu, minecraft doesn't try to do menu-related stuff
     */
    void showPlaceholderGui() {
        this.game.a(new da());
    }

    /**
     * Check if showing the ingame menu (esc menu)
     * 
     * @return true if showing the ingame menu
     */
    boolean isIngameMenuUp() {
        return this.game.r instanceof oz;
    }

    /**
     * Check if a player currently exists
     * 
     * @return true if a player existss
     */
    boolean playerExists() {
        return this.game.h != null;
    }

    /**
     * Check if it's currently safe to run (to prevent crashes when attempting
     * to calculate the map when no world exists)
     * 
     * @return true if safe to run
     */
    boolean safeToRun() {
        return game.i != null;
    }

    /**
     * Get the current dimension int
     * 
     * @return current dimension value
     */
    int getCurrentDimension() {
        if (!playerExists())
            return lastdim;
        return game.h.m;
    }

    /**
     * Calculate the render pixel length of a string
     * 
     * @param str string to calculate
     * @return pixel width of string
     */
    int calcStringLength(String str) {
        return this.lang.a(str);
    }

    /**
     * draw a string on the screen
     * 
     * @param str string to render
     * @param x X position to render at
     * @param y Y position to render at
     * @param color color to render the string as
     */
    void write(String str, int x, int y, int color) {
        this.lang.a(str, x, y, color);
    }

    /**
     * @return player current X coord
     */
    double getPlayerX() {
        return this.game.h.aM;
    }

    /**
     * @return player current Z coord
     */
    double getPlayerZ() {
        return this.game.h.aO;
    }

    /**
     * @return player current Y coord
     */
    double getPlayerY() {
        return this.game.h.aN;
    }

    /**
     * @return player angle
     */
    float getPlayerYaw() {
        float rotationYaw = this.game.h.aS;
        return rotationYaw;
    }

    /**
     * Format a coord for display
     * 
     * @param coord integer to format
     * @return formatted string
     */
    String dCoord(int coord) {
        if (coord < 0)
            return (minimap.conf.netherpoints ? "n" : "") + "-" + Math.abs(coord + 1);
        else
            return (minimap.conf.netherpoints ? "n" : "") + "+" + coord;
    }

    /**
     * Not entirely sure what this does, but I think it sets a bufferedimage
     * as a GL texture
     * 
     * @param image image to do stuff with
     * @return gl texture index?
     */
    int tex(BufferedImage image) {
        return this.renderEngine.a(image);
    }

    /**
     * Set a path in the jar as a gl texture
     * 
     * @param path path to image in the jar
     * @return gl texture index?
     */
    int img(String path) {
        //TODO: is this correct?
        return this.renderEngine.b(path);
    }

    /**
     * Loads a gl texture index as the texture to render
     * 
     * @param index gl texture index
     */
    void disp(int index) {
        this.renderEngine.b(index);
    }

    /**
     * Get current world
     * 
     * @return world instance
     */
    fd getWorld() {
        return game.f;
    }

    /**
     * Get app dir - finds the dir for .someapp if you call it with someapp
     * generally call it with "minecraft"
     * 
     * @param app app to get
     * @return File of app dir
     */
    static File getAppDir(String app) {
        return Minecraft.a(app);
    }

    /**
     * Tint a color with a tint type. Doesn't currently work.
     * 
     * @param world unused
     * @param original returned as-is
     * @param x unused
     * @param y unused
     * @param z unused
     * @param ttype unused
     * @return original color
     */
    int getBlockTint(fd world, int original, int x, int y, int z, TintType ttype) {
        /*if (true)*/return original; //blarg :<
        /*double temperature = 0.0;
        double humidity = 0.0;
        switch (ttype)
        {
            case GRASS:
                // world.getWorldChunkManager().func_4069_a(i, k, 1, 1);
                synchronized (world)
                {
                    world.a().a(x, z, 1, 1);
                // double d =
                // iblockaccess.getWorldChunkManager().temperature[0];
                
                    temperature = world.a().a[0];
                    // double humidity =
                    // iblockaccess.getWorldChunkManager().humidity[0];
                    humidity = world.a().b[0];
                }
                // return ColorizerGrass.
                try
                {
                    return colorMult(original, hx.a(temperature, humidity));
                }
                catch (Throwable t)
                {
                    System.err.println("err on temp " + temperature + " and humid " + humidity);
                    t.printStackTrace();
                }
            case FOLIAGE:
                synchronized(world)
                {
                    world.a().a(x, z, 1, 1);
                    temperature = world.a().a[0];
                    humidity = world.a().b[0];
                }
                try
                {
                    // return ColorizerFoliage.getFoliageColor
                    return colorMult(original, je.a(temperature, humidity));
                }
                catch (Throwable t)
                {
                    System.err.println("err on temp " + temperature + " and humid " + humidity);
                    t.printStackTrace();
                }
            case PINE:
                return colorMult(original, je.a());
            case BIRCH:
                return colorMult(original, je.b());
            case REDSTONE:
                
                int blockmeta = world.e(x, y, z);
                // TODO: this could be integer math, instead of going to/from
                // float
                float floatmeta = (float)blockmeta / 15F;
                float r = floatmeta * 0.6F + 0.4F;
                if (blockmeta == 0)
                {
                    r = 0.0F;
                }
                float g = floatmeta * floatmeta * 0.7F - 0.5F;
                float b = floatmeta * floatmeta * 0.6F - 0.7F;
                if (g < 0.0F)
                {
                    g = 0.0F;
                }
                if (b < 0.0F)
                {
                    b = 0.0F;
                }
                int tint = ((int)(b * 0xff)) + (((int)(g * 0xff)) << 8) + (((int)(r * 0xff)) << 16);
                return colorMult(original, tint);
            default:
                return original;
        }*/
    }

    /**
     * @return true if world is null
     */
    public boolean worldIsNull() {
        return game.r == null;
    }

    /**
     * Get world name. checks both MapName and ServerName.
     * 
     * @return worldname
     */
    public String getWorldName() {
        String worldname = getMapName();
        if (worldname.equals("MpServer")) {
            //This removes the port
            String[] split = getServerName().toLowerCase().split(":");
            worldname = split[0];
        }
        return worldname;
    }

    /**
     * called on render ticks - calls updateLang and updateRenderEngine
     * 
     * @see updateLang
     * @see updateRenderEngine
     */
    public void onRenderTick() {
        updateLang();
        updateRenderEngine();
    }
}
