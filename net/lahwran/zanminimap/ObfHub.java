/**
 * 
 */
package net.lahwran.zanminimap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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

    
    /** last dimension we rendered the map in */
    public int lastdim = 0;
    public Minecraft game;

    /** Polygon creation class */
    public nw lDraw = nw.a;

    /** Font rendering class */
    public sj lang;

    /** Render texture */
    public ji renderEngine;

    private ZanMinimap minimap;

    ObfHub(ZanMinimap minimap) {
        this.minimap = minimap;
    }

    void chatInfo(String s)
    {
        game.v.a(s);
    }

    String getMapName()
    {
        try {
            fd world = getWorld();
            Class worldclass = world.getClass();
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
            worlddatafield.setAccessible(true);
            
            ei worldata;
            
            worldata = (ei) worlddatafield.get(world);
            return worldata.j();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getServerName()
    {
        return game.z.C;
    }

    void draw_startQuads()
    {
        lDraw.b();
    }

    void draw_finish()
    {
        lDraw.a();
    }

    void glah(int g)
    {
        renderEngine.a(g);
    }

    void ldraw_addVertexWithUV(int x, int y, double z, double u, double v)
    {
        ldraw_addVertexWithUV((double) x, (double) y, z, u, v);
    }

    void ldraw_setColor(double a, double b, double c)
    {
        lDraw.a(a, b, c);
    }

    void ldraw_addVertexWithUV(double x, double y, double z, double u, double v)
    {
        lDraw.a(x, y, z, u, v);
    }

    int getMouseX(int scWidth)
    {
        return Mouse.getX() * (scWidth + 5) / game.d;
    }

    int getMouseY(int scHeight)
    {
        return (scHeight + 5) - Mouse.getY() * (scHeight + 5) / this.game.e - 1;
    }

    void setMenuNull()
    {
        game.r = null;
    }

    Object getMenu()
    {
        return game.r;
    }
    
    boolean isGameOver()
    {
        return getMenu() instanceof ch;
    }

    boolean isConflictWarning()
    {
        return getMenu() instanceof qh;
    }

    boolean isMenuShowing()
    {
        return this.game.r != null;
    }

    void updateLang()
    {
        if (lang == null) lang = this.game.q;
    }

    void updateRenderEngine()
    {
        if (renderEngine == null) renderEngine = this.game.p;
    }

    int[] getScreenSize()
    {
        qq scSize = new qq(game.z, game.d, game.e);
        return new int[] {scSize.a(), scSize.b()};
    }

    void showPlaceholderGui()
    {
        this.game.a(new da());
    }
    
    boolean isIngameMenuUp()
    {
        return this.game.r instanceof oz;
    }
    
    boolean playerExists()
    {
        return this.game.h != null;
    }
    
    boolean safeToRun()
    {
        return game.i != null;
    }
    
    int getCurrentDimension()
    {
        if (!playerExists())
            return lastdim;
        return game.h.m;
    }

    int chkLen(String paramStr)
    {
        return this.lang.a(paramStr);
    }

    void write(String paramStr, int paramInt1, int paramInt2, int paramInt3)
    {
        this.lang.a(paramStr, paramInt1, paramInt2, paramInt3);
    }

    int playerXCoord()
    {
        double posX = this.game.h.aM;
        return (int)(posX < 0.0D ? posX - 1 : posX);
    }

    int playerZCoord()
    {
        double posZ = this.game.h.aO;
        return (int)(posZ < 0.0D ? posZ - 1 : posZ);
    }

    int playerYCoord()
    {
        double posY = this.game.h.aN;
        return (int)posY;
    }

    float radius()
    {
        float rotationYaw = this.game.h.aS;
        return rotationYaw;
    }

    String dCoord(int paramInt1)
    {
        if (paramInt1 < 0)
            return (minimap.conf.netherpoints ? "n" : "") + "-" + Math.abs(paramInt1 + 1);
        else
            return (minimap.conf.netherpoints ? "n" : "") + "+" + paramInt1;
    }

    int tex(BufferedImage paramImg)
    {
        if (paramImg == null) throw new NullPointerException();
        if (renderEngine == null) throw new NullPointerException();
        return this.renderEngine.a(paramImg);
    }

    int img(String paramStr)
    {
        //TODO: is this correct?
        return this.renderEngine.b(paramStr);
    }

    void disp(int paramInt)
    {
        this.renderEngine.b(paramInt);
    }

    fd getWorld()
    {
        return game.f;
    }
    
    static File getAppDir(String app)
    {
        return Minecraft.a(app);
    }
    
    int getBlockTint(fd world, int original, int x, int y, int z, TintType ttype)
    {
        /*if (true)*/ return original; //blarg :<
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
    public boolean worldIsNull() {
        return game.r == null;
    }
    
    public String getWorldName() {
        String worldname = getMapName();
        if (worldname.equals("MpServer"))
        {
            //This removes the port
            String[] split = getServerName().toLowerCase().split(":");
            worldname = split[0];
        }
        return worldname;
    }
    
    public void tick() {
        updateLang();
        updateRenderEngine();
    }
}
