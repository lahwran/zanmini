import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import net.lahwran.zanminimap.BlockColor;
import net.lahwran.zanminimap.TintType;
import net.lahwran.zanminimap.Waypoint;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ZanMinimap implements Runnable {
    // TODO: update
    /** Polygon creation class */
    public ns lDraw = ns.a;

    /** Font rendering class */
    public se lang;

    /** Render texture */
    public jf renderEngine;

    public static File getAppDir(String app)
    {
        return Minecraft.a(app);
    }

    public void chatInfo(String s)
    {
        game.v.a(s);
    }

    public String getMapName()
    {
        return game.f.x.j();
        //return game.f.s.j();
    }

    public String getServerName()
    {
        return game.z.C;
    }

    public void drawPre()
    {
        lDraw.b();
    }

    public void drawPost()
    {
        lDraw.a();
    }

    public void glah(int g)
    {
        renderEngine.a(g);
    }

    public void ldrawone(int a, int b, double c, double d, double e)
    {
        lDraw.a(a, b, c, d, e);
    }

    public void ldrawtwo(double a, double b, double c)
    {
        lDraw.a(a, b, c);
    }

    public void ldrawthree(double a, double b, double c, double d, double e)
    {
        lDraw.a(a, b, c, d, e);
    }

    public int getMouseX(int scWidth)
    {
        return Mouse.getX() * (scWidth + 5) / game.d;
    }

    public int getMouseY(int scHeight)
    {
        return (scHeight + 5) - Mouse.getY() * (scHeight + 5) / this.game.e - 1;
    }

    public void setMenuNull()
    {
        game.r = null;
    }

    public Object getMenu()
    {
        return game.r;
    }

    public void OnTickInGame(Minecraft mc)
    {
        if (game == null) game = mc;

        if (threading)
        {

            if (!zCalc.isAlive() && threading)
            {
                zCalc = new Thread(this);
                zCalc.start();
            }
            if (this.game.r != null && !(this.game.r instanceof cf) && !(this.game.r instanceof qd))
                try
                {
                    this.zCalc.notify();
                }
                catch (Exception local)
                {}
        }
        else if (!threading)
        {
            if (this.enabled && !this.hide)
                if (((this.lastX != this.xCoord()) || (this.lastZ != this.yCoord()) || (this.timer > 300)))
                    mapCalc();
        }

        if (lang == null) lang = this.game.q;

        if (renderEngine == null) renderEngine = this.game.p;

        qm scSize = new qm(game.z, game.d, game.e);
        int scWidth = scSize.a();
        int scHeight = scSize.b();

        if (Keyboard.isKeyDown(menuKey) && this.game.r == null)
        {
            this.iMenu = 2;
            this.game.a(new cy());
        }

        if (Keyboard.isKeyDown(zoomKey) && this.game.r == null)
        {
            this.SetZoom();
        }

        loadWaypoints();

        if (this.iMenu == 1)
        {
            if (!welcome) this.iMenu = 0;
        }
        if (this.game.r == null && this.iMenu > 1) this.iMenu = 0;

        if ((this.game.r instanceof ov) ^ (Keyboard.isKeyDown(Keyboard.KEY_F6)))
            this.enabled = false;
        else
            this.enabled = true;


        scWidth -= 5;
        scHeight -= 5;

        if (this.oldDir != this.radius())
        {
            this.direction += this.oldDir - this.radius();
            this.oldDir = this.radius();
        }

        if (this.direction >= 360.0f) while (this.direction >= 360.0f)
            this.direction -= 360.0f;

        if (this.direction < 0.0f)
        {
            while (this.direction < 0.0f)
                this.direction += 360.0f;
        }

        if ((!this.error.equals("")) && (this.ztimer == 0)) this.ztimer = 500;

        if (this.ztimer > 0) this.ztimer -= 1;

        if (this.fudge > 0) this.fudge -= 1;

        if ((this.ztimer == 0) && (!this.error.equals(""))) this.error = "";

        if (this.enabled)
        {
            renderMap(scWidth);

            if (this.full) renderMapFull(scWidth, scHeight);

            if (ztimer > 0) this.write(this.error, 20, 20, 0xffffff);

            if (this.iMenu > 0) showMenu(scWidth, scHeight);

            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (coords) showCoords(scWidth, scHeight);
        }

        // while (active) try {
        // Thread.currentThread().sleep(1);
        // } catch (Exception local) {}
        // TODO: what the fuck is this for? :P
    }

    private int chkLen(String paramStr)
    {
        return this.lang.a(paramStr);
    }

    private void write(String paramStr, int paramInt1, int paramInt2, int paramInt3)
    {
        this.lang.a(paramStr, paramInt1, paramInt2, paramInt3);
    }

    private int xCoord()
    {
        return (int)(this.game.h.aM < 0.0D ? this.game.h.aM - 1 : this.game.h.aM);
    }

    private int yCoord()
    {
        return (int)(this.game.h.aO < 0.0D ? this.game.h.aO - 1 : this.game.h.aO);
    }

    private int zCoord()
    {
        return (int)this.game.h.aN;
    }

    private float radius()
    {
        return this.game.h.aS;
    }

    private String dCoord(int paramInt1)
    {
        if (paramInt1 < 0)
            return "-" + Math.abs(paramInt1 + 1);
        else
            return "+" + paramInt1;
    }

    private int tex(BufferedImage paramImg)
    {
        return this.renderEngine.a(paramImg);
    }

    private int img(String paramStr)
    {
        //TODO: is this correct?
        return this.renderEngine.b(paramStr);
    }

    private void disp(int paramInt)
    {
        this.renderEngine.b(paramInt);
    }

    public fb getWorld()
    {
        return game.f;
    }

    // Yourself came up with this, I'm sure it makes sense to someone
    public int colorMult(int x, int y)
    {
        int res = 0;
        for (int octet = 0; octet < 3; ++octet)
        {
            res |= (((x & 0xff) * (y & 0xff)) / 0xff) << (octet << 3);
            x >>= 8;
            y >>= 8;
        }
        return res;
    }

    public int getBlockTint(fb world, int original, int x, int y, int z, TintType ttype)
    {
        if (true) return original; //blarg :<
        double temperature = 0.0;
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
        }
    }

    private final int getBlockHeight(fb world, int x, int z, int starty)
    {
        return world.d(x, z);
        // return world.b(x, z).b(x & 0xf, z & 0xf);
        /*
        li chunk = world.b(x, z);
        int y = (int)(game.h.aM); //starty;
        x &= 0xf;
        z &= 0xf;

        //while (y > 0)
       // {
            int id = chunk.a(x, y, z);
            int meta = chunk.b(x, y, z);

            if (getBlockColor(id, meta).alpha == 0)
                return -1;//y--;
            else
                return y + 1; // what
       //}
*/
        //return -1;
    }

    private final int getBlockHeight(fb world, int x, int z)
    {
        return getBlockHeight(world, x, z, 127);
    }

    private void mapCalc()
    {
        try
        {
            fb data = getWorld();
            this.lZoom = this.zoom;
            int multi = (int)Math.pow(2, this.lZoom);
            int startX = this.xCoord();
            int startZ = this.yCoord();
            this.lastX = startX;
            this.lastZ = startZ;
            startX -= 16 * multi;
            startZ += 16 * multi;
            int color24 = 0;

            for (int imageY = 0; imageY < 32 * multi; imageY++)
            {
                for (int imageX = 0; imageX < 32 * multi; imageX++)
                {
                    color24 = 0;
                    boolean check = false;

                    if (Math.sqrt((16 * multi - imageY) * (16 * multi - imageY) + (16 * multi - imageX) * (16 * multi - imageX)) < ((16 * multi) - ((int)Math.sqrt(multi))))
                        check = true;

                    int height = getBlockHeight(data, startX + imageY, startZ - imageX);

                    if ((check) || (showmap) || (this.full))
                    {
                        if (this.color)
                        {
                            if ((data.f(startX + imageY, height, startZ - imageX) == lj.s) || (data.f(startX + imageY, height, startZ - imageX) == lj.t))
                                color24 = 0xffffff;
                            else
                            {
                                BlockColor col = getBlockColor(data.a(startX + imageY, height - 1, startZ - imageX), data.e(startX + imageY, height - 1, startZ - imageX));
                                color24 = getBlockTint(data, col.color, startX + imageY, height - 1, startZ - imageX, col.tintType);
                            }

                        }
                        else
                            color24 = 0xFFFFFF;
                    }

                    if ((color24 != 0xff00ff) && (color24 != 0) && ((check) || (showmap) || (this.full)))
                    {
                        if (heightmap)
                        {
                            int i2 = height - this.zCoord();
                            double sc = Math.log10(Math.abs(i2) / 8.0D + 1.0D) / 1.3D;
                            int r = color24 / 0x10000;
                            int g = (color24 - r * 0x10000) / 0x100;
                            int b = (color24 - r * 0x10000 - g * 0x100);

                            if (i2 >= 0)
                            {
                                r = (int)(sc * (0xff - r)) + r;
                                g = (int)(sc * (0xff - g)) + g;
                                b = (int)(sc * (0xff - b)) + b;
                            }
                            else
                            {
                                i2 = Math.abs(i2);
                                r = r - (int)(sc * r);
                                g = g - (int)(sc * g);
                                b = b - (int)(sc * b);
                            }

                            color24 = r * 0x10000 + g * 0x100 + b;
                        }

                        int i3 = 255;

                        if (lightmap)
                            i3 = data.a(startX + imageY, height, startZ - imageX, false) * 17;

                        if (i3 > 255) i3 = 255;

                        if (i3 < 32) i3 = 32;

                        color24 = i3 * 0x1000000 + color24;
                    }

                    this.map[this.lZoom].setRGB(imageX, imageY, color24);
                }
            }
        }
        catch (Throwable whatever)
        {
            whatever.printStackTrace();
        }
    }

    public void run()
    {
        if (this.game == null) return;
        while (true)
        {
            if (threading)
            {
                this.active = true;
                while (this.game.h != null && active)
                {
                    if (this.enabled && !this.hide)
                        if (((this.lastX != this.xCoord()) || (this.lastZ != this.yCoord()) || (this.timer > 300)))
                            try
                            {
                                this.mapCalc();
                                this.timer = 1;
                            }
                            catch (Exception local)
                            {}
                    this.timer++;
                    this.active = false;
                }
                active = false;
                try
                {
                    Thread.sleep(10);
                }
                catch (Exception exc)
                {}
                try
                {
                    this.zCalc.wait(0);
                }
                catch (Exception exc)
                {}
            }
            else
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception exc)
                {}
                try
                {
                    this.zCalc.wait(0);
                }
                catch (Exception exc)
                {}
            }
        }
    }

    // END UPDATE TODO SECTION

    public Minecraft game;

    /** Textures for each zoom level */
    public BufferedImage[] map = new BufferedImage[4];

    /** Block color array */
    private final BlockColor[] blockColors = new BlockColor[4096];

    public int q = 0;
    public Random generator = new Random();
    /** Current Menu Loaded */
    public int iMenu = 1;

    /** Display anything at all, menu, etc.. */
    public boolean enabled = true;

    /** Hide just the minimap */
    public boolean hide = false;

    /** Was mouse down last render? */
    public boolean lfclick = false;

    /** Toggle full screen map */
    public boolean full = false;

    /** Is map calc thread still executing? */
    public boolean active = false;

    /** Current level of zoom */
    public int zoom = 2;

    /** Current build version */
    public String zmodver = "v0.9.8";

    /** Minecraft version that we'll work with */
    public static final String mcvers = "1.5_01";

    /** Menu input string */
    public String inStr = "";

    /** Waypoint name temporary input */
    public String way = "";

    /** Waypoint X coord temp input */
    public int wayX = 0;

    /** Z coord temp input */
    public int wayZ = 0;

    /** Colour or black and white minimap? */
    public boolean color = true;

    /** Holds error exceptions thrown */
    public String error = "";

    /** Main Menu Option Count */
    public int mmOptCount = 7;

    /** Strings to show for menu */
    public String[][] sMenu = new String[2][10];

    /** Time remaining to show error thrown for */
    public int ztimer = 0;

    /** Minimap update interval */
    public int timer = 0;

    /** Key entry interval */
    public int fudge = 0;

    /** Last X coordinate rendered */
    public int lastX = 0;

    /** Last Z coordinate rendered */
    public int lastZ = 0;

    /** Last zoom level rendered at */
    public int lZoom = 0;

    /** Menu level for next render */
    public int next = 0;

    /** Cursor blink interval */
    public int blink = 0;

    /** Last key down on previous render */
    public int lastKey = 0;

    /** Direction you're facing */
    public float direction = 0.0f;

    /** Last direction you were facing */
    public float oldDir = 0.0f;

    /** Setting file access */
    public File settingsFile;

    /** World currently loaded */
    public String world = "";

    /** Is the scrollbar being dragged? */
    public boolean scrClick = false;

    /** Scrollbar drag start position */
    public int scrStart = 0;

    /** Scrollbar offset */
    public int sMin = 0;

    /** Scrollbar size */
    public int sMax = 67;

    /** 1st waypoint entry shown */
    public int min = 0;

    /** Zoom key index */
    public int zoomKey = Keyboard.KEY_Z;

    /** Menu key index */
    public int menuKey = Keyboard.KEY_M;

    /** Square map toggle */
    public boolean showmap = false;

    /** Show coordinates toggle */
    public boolean coords = false;

    /** Dynamic lighting toggle */
    public boolean lightmap = true;

    /** Terrain depth toggle */
    public boolean heightmap = true;

    /** Show welcome message toggle */
    public boolean welcome = true;

    /** Waypoint names and data */
    public ArrayList<Waypoint> wayPts;

    /** Map calculation thread */
    public Thread zCalc = new Thread(this);

    /** should we be running the calc thread? */
    public static boolean threading;

    // public boolean aprilfools = false;

    public boolean haveLoadedBefore;

    public static ZanMinimap instance;

    public ZanMinimap()
    {

        //ModLoader.SetInGameHook(this, true, false);

        instance = this;
        threading = false;
        zCalc.start();
        this.map[0] = new BufferedImage(32, 32, 2);
        this.map[1] = new BufferedImage(64, 64, 2);
        this.map[2] = new BufferedImage(128, 128, 2);
        this.map[3] = new BufferedImage(256, 256, 2);

        for (int m = 0; m < 2; m++)
            for (int n = 0; n < 10; n++)
                this.sMenu[m][n] = "";

        this.sMenu[0][0] = "§4Zan's§F Mod! " + this.zmodver;
        this.sMenu[0][1] = "Welcome to Zan's Minimap, there are a";
        this.sMenu[0][2] = "number of features and commands available to you.";
        this.sMenu[0][3] = "- Press §B" + Keyboard.getKeyName(zoomKey) + " §Fto zoom in/out, or §B" + Keyboard.getKeyName(menuKey) + "§F for options.";
        this.sMenu[1][0] = "Options";
        this.sMenu[1][1] = "Display Coordinates:";
        this.sMenu[1][2] = "Hide Minimap:";
        this.sMenu[1][3] = "Dynamic Lighting:";
        this.sMenu[1][4] = "Terrain Depth:";
        this.sMenu[1][5] = "Square Map:";
        this.sMenu[1][6] = "Welcome Screen:";
        this.sMenu[1][7] = "Threading:";
        settingsFile = new File(getAppDir("minecraft"), "zan.settings");

        try
        {
            if (settingsFile.exists())
            {
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine;
                // APRF: haveLoadedBefore=false;
                while ((sCurrentLine = in.readLine()) != null)
                {
                    String[] curLine = sCurrentLine.split(":");

                    if (curLine[0].equals("Show Minimap"))
                        showmap = Boolean.parseBoolean(curLine[1]);
                    else if (curLine[0].equals("Show Coordinates"))
                        coords = Boolean.parseBoolean(curLine[1]);
                    else if (curLine[0].equals("Dynamic Lighting"))
                        lightmap = Boolean.parseBoolean(curLine[1]);
                    else if (curLine[0].equals("Terrain Depth"))
                        heightmap = Boolean.parseBoolean(curLine[1]);
                    else if (curLine[0].equals("Welcome Message"))
                        welcome = Boolean.parseBoolean(curLine[1]);
                    else if (curLine[0].equals("Zoom Key"))
                        zoomKey = Keyboard.getKeyIndex(curLine[1]);
                    else if (curLine[0].equals("Menu Key"))
                        menuKey = Keyboard.getKeyIndex(curLine[1]);
                    else if (curLine[0].equals("Threading"))
                        threading = Boolean.parseBoolean(curLine[1]);

                }
                in.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < blockColors.length; i++)
            blockColors[i] = null;

        settingsFile = new File(getAppDir("minecraft"), "minimapcolors_099");

        try
        {
            blockColors[blockColorID(0, 0)] = new BlockColor(0xff00ff, 0, TintType.NONE); //air
            int wood = 0xbc9862; //reused colors
            int water = 0x3256ff;
            int lava = 0xd96514;
            blockColors[blockColorID(1, 0)] = new BlockColor(0x686868, 0xff, TintType.NONE); //stone
            blockColors[blockColorID(2, 0)] = new BlockColor(0x74b44a, 0xff, TintType.GRASS); //grass
            blockColors[blockColorID(3, 0)] = new BlockColor(0x79553a, 0xff, TintType.NONE); //dirt
            blockColors[blockColorID(4, 0)] = new BlockColor(0x959595, 0xff, TintType.NONE); //cobble
            blockColors[blockColorID(5, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood
            blockColors[blockColorID(6, 0)] = new BlockColor(0xa2c978, 0x80, TintType.FOLIAGE); //sapling 1
            blockColors[blockColorID(6, 1)] = new BlockColor(0xa2c978, 0x80, TintType.PINE);    //sapling 2
            blockColors[blockColorID(6, 2)] = new BlockColor(0xa2c978, 0x80, TintType.BIRCH);   //sapling 3
            blockColors[blockColorID(7, 0)] = new BlockColor(0x333333, 0xff, TintType.NONE); //bedrock
            blockColors[blockColorID(8, 0)] = new BlockColor(water, 0xc0, TintType.NONE); //water
            blockColors[blockColorID(9, 0)] = new BlockColor(water, 0xb0, TintType.NONE); //moving water
            blockColors[blockColorID(10, 0)] = new BlockColor(lava, 0xff, TintType.NONE); //lava
            blockColors[blockColorID(11, 0)] = new BlockColor(lava, 0xff, TintType.NONE); //moving lava
            blockColors[blockColorID(12, 0)] = new BlockColor(0xddd7a0, 0xff, TintType.NONE); //sand
            blockColors[blockColorID(13, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //gravel
            blockColors[blockColorID(14, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //gold ore
            blockColors[blockColorID(15, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //iron ore
            blockColors[blockColorID(16, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //coal ore
            
            blockColors[blockColorID(17, 0)] = new BlockColor(0x675132, 0xff, TintType.NONE); //log 1
            blockColors[blockColorID(17, 1)] = new BlockColor(0x342919, 0xff, TintType.NONE); //log 2
            blockColors[blockColorID(17, 2)] = new BlockColor(0xc8c29f, 0xff, TintType.NONE); //log 3
            
            blockColors[blockColorID(18, 0)] = new BlockColor(0x164d0c, 0xa0, TintType.NONE); //leaf
            blockColors[blockColorID(19, 0)] = new BlockColor(0xe5e54e, 0xff, TintType.NONE); //sponge
            blockColors[blockColorID(20, 0)] = new BlockColor(0xffffff, 0x80, TintType.NONE); //glass
            blockColors[blockColorID(21, 0)] = new BlockColor(0x6d7484, 0xff, TintType.NONE); //lapis ore
            blockColors[blockColorID(22, 0)] = new BlockColor(0x1542b2, 0xff, TintType.NONE); //lapis
            blockColors[blockColorID(23, 0)] = new BlockColor(0x585858, 0xff, TintType.NONE); //dispenser
            blockColors[blockColorID(24, 0)] = new BlockColor(0xc6bd6d, 0xff, TintType.NONE); //sandstone
            blockColors[blockColorID(25, 0)] = new BlockColor(0x784f3a, 0xff, TintType.NONE); //noteblock
            blockColors[blockColorID(26, 0)] = new BlockColor(0xa95d5d, 0xff, TintType.NONE); //bed
            
            //skip 27, 28, 30, 31, and 32 as they are all nonsolid and
            //notch's height map skips them
            
            blockColors[blockColorID(35, 0)] = new BlockColor(0xe1e1e1, 0xff, TintType.NONE); //colored wool
            blockColors[blockColorID(35, 1)] = new BlockColor(0xeb8138, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 2)] = new BlockColor(0xc04cca, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 3)] = new BlockColor(0x698cd5, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 4)] = new BlockColor(0xc5b81d, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 5)] = new BlockColor(0x3cbf30, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 6)] = new BlockColor(0xda859c, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 7)] = new BlockColor(0x434343, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 8)] = new BlockColor(0x9fa7a7, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 9)] = new BlockColor(0x277697, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 10)] = new BlockColor(0x7f33c1, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 11)] = new BlockColor(0x26339b, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 12)] = new BlockColor(0x57331c, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 13)] = new BlockColor(0x384e18, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 14)] = new BlockColor(0xa52d28, 0xff, TintType.NONE);
            blockColors[blockColorID(35, 15)] = new BlockColor(0x1b1717, 0xff, TintType.NONE); //end colored wool
            
            blockColors[blockColorID(37, 0)] = new BlockColor(0xf1f902, 0xff, TintType.NONE); //yellow flower
            blockColors[blockColorID(38, 0)] = new BlockColor(0xf7070f, 0xff, TintType.NONE); //red flower
            blockColors[blockColorID(39, 0)] = new BlockColor(0x916d55, 0xff, TintType.NONE); //brown mushroom
            blockColors[blockColorID(40, 0)] = new BlockColor(0x9a171c, 0xff, TintType.NONE); //red mushroom
            blockColors[blockColorID(41, 0)] = new BlockColor(0xfefb5d, 0xff, TintType.NONE); //gold block
            blockColors[blockColorID(42, 0)] = new BlockColor(0xe9e9e9, 0xff, TintType.NONE); //iron block
            
            blockColors[blockColorID(43, 0)] = new BlockColor(0xa8a8a8, 0xff, TintType.NONE); //double slabs
            blockColors[blockColorID(43, 1)] = new BlockColor(0xe5ddaf, 0xff, TintType.NONE);
            blockColors[blockColorID(43, 2)] = new BlockColor(0x94794a, 0xff, TintType.NONE);
            blockColors[blockColorID(43, 3)] = new BlockColor(0x828282, 0xff, TintType.NONE);

            blockColors[blockColorID(44, 0)] = new BlockColor(0xa8a8a8, 0xff, TintType.NONE); //single slabs
            blockColors[blockColorID(44, 1)] = new BlockColor(0xe5ddaf, 0xff, TintType.NONE);
            blockColors[blockColorID(44, 2)] = new BlockColor(0x94794a, 0xff, TintType.NONE);
            blockColors[blockColorID(44, 3)] = new BlockColor(0x828282, 0xff, TintType.NONE);
            
            blockColors[blockColorID(45, 0)] = new BlockColor(0xaa543b, 0xff, TintType.NONE); //brick
            blockColors[blockColorID(46, 0)] = new BlockColor(0xdb441a, 0xff, TintType.NONE); //tnt
            blockColors[blockColorID(47, 0)] = new BlockColor(0xb4905a, 0xff, TintType.NONE); //bookshelf
            blockColors[blockColorID(48, 0)] = new BlockColor(0x1f471f, 0xff, TintType.NONE); //mossy cobble
            blockColors[blockColorID(49, 0)] = new BlockColor(0x101018, 0xff, TintType.NONE); //obsidian
            blockColors[blockColorID(50, 0)] = new BlockColor(0xffd800, 0xff, TintType.NONE); //torch
            blockColors[blockColorID(51, 0)] = new BlockColor(0xc05a01, 0xff, TintType.NONE); //fire
            blockColors[blockColorID(52, 0)] = new BlockColor(0x265f87, 0xff, TintType.NONE); //spawner
            blockColors[blockColorID(53, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood steps
            blockColors[blockColorID(54, 0)] = new BlockColor(0x8f691d, 0xff, TintType.NONE); //chest
            blockColors[blockColorID(55, 0)] = new BlockColor(0x480000, 0xff, TintType.NONE); //redstone wire
            blockColors[blockColorID(56, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //diamond ore
            blockColors[blockColorID(57, 0)] = new BlockColor(0x82e4e0, 0xff, TintType.NONE); //diamond block
            blockColors[blockColorID(58, 0)] = new BlockColor(0xa26b3e, 0xff, TintType.NONE); //craft table
            blockColors[blockColorID(59, 0)] = new BlockColor(0x00e210, 0xff, TintType.NONE); //crops
            blockColors[blockColorID(60, 0)] = new BlockColor(0x633f24, 0xff, TintType.NONE); //cropland
            blockColors[blockColorID(61, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //furnace
            blockColors[blockColorID(62, 0)] = new BlockColor(0x808080, 0xff, TintType.NONE); //furnace, powered
            blockColors[blockColorID(63, 0)] = new BlockColor(0xb4905a, 0xff, TintType.NONE); //fence
            blockColors[blockColorID(64, 0)] = new BlockColor(0x7a5b2b, 0xff, TintType.NONE); //door
            blockColors[blockColorID(65, 0)] = new BlockColor(0xac8852, 0xff, TintType.NONE); //ladder
            blockColors[blockColorID(66, 0)] = new BlockColor(0xa4a4a4, 0xff, TintType.NONE); //track
            blockColors[blockColorID(67, 0)] = new BlockColor(0x9e9e9e, 0xff, TintType.NONE); //cobble steps
            blockColors[blockColorID(68, 0)] = new BlockColor(0x9f844d, 0xff, TintType.NONE); //sign
            blockColors[blockColorID(69, 0)] = new BlockColor(0x695433, 0xff, TintType.NONE); //lever
            blockColors[blockColorID(70, 0)] = new BlockColor(0x8f8f8f, 0xff, TintType.NONE); //stone pressureplate
            blockColors[blockColorID(71, 0)] = new BlockColor(0xc1c1c1, 0xff, TintType.NONE); //iron door
            blockColors[blockColorID(72, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood pressureplate
            blockColors[blockColorID(73, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //redstone ore
            blockColors[blockColorID(74, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //glowing redstone ore
            blockColors[blockColorID(75, 0)] = new BlockColor(0x290000, 0xff, TintType.NONE); //redstone torch, off
            blockColors[blockColorID(76, 0)] = new BlockColor(0xfd0000, 0xff, TintType.NONE); //redstone torch, lit
            blockColors[blockColorID(77, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //button
            blockColors[blockColorID(78, 0)] = new BlockColor(0xfbffff, 0xff, TintType.NONE); //snow
            blockColors[blockColorID(79, 0)] = new BlockColor(0x8ebfff, 0xff, TintType.NONE); //ice
            blockColors[blockColorID(80, 0)] = new BlockColor(0xffffff, 0xff, TintType.NONE); //snow block
            blockColors[blockColorID(81, 0)] = new BlockColor(0x11801e, 0xff, TintType.NONE); //cactus
            blockColors[blockColorID(82, 0)] = new BlockColor(0xbbbbcc, 0xff, TintType.NONE); //clay
            blockColors[blockColorID(83, 0)] = new BlockColor(0xa1a7b2, 0xff, TintType.NONE); //reeds
            blockColors[blockColorID(84, 0)] = new BlockColor(0xaadb74, 0xff, TintType.NONE); //record player
            blockColors[blockColorID(85, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //fence
            blockColors[blockColorID(86, 0)] = new BlockColor(0xa25b0b, 0xff, TintType.NONE); //pumpkin
            blockColors[blockColorID(87, 0)] = new BlockColor(0x582218, 0xff, TintType.NONE); //netherrack
            blockColors[blockColorID(88, 0)] = new BlockColor(0x996731, 0xff, TintType.NONE); //slow sand
            blockColors[blockColorID(89, 0)] = new BlockColor(0xcda838, 0xff, TintType.NONE); //glowstone
            blockColors[blockColorID(90, 0)] = new BlockColor(0x732486, 0xff, TintType.NONE); //portal
            blockColors[blockColorID(91, 0)] = new BlockColor(0xa25b0b, 0xff, TintType.NONE); //jackolantern
            Pattern colorline = Pattern.compile("^([0-9]*)\\.([0-9]*): color=([0-9a-fA-F]*).alpha=([0-9a-fA-F]*) tint=(.*)$");
            if (settingsFile.exists())
            {
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine;

                while ((sCurrentLine = in.readLine()) != null)
                {
                    if (sCurrentLine.startsWith("#")) continue;
                    Matcher match = colorline.matcher(sCurrentLine);
                    // new
                    if (match.matches())
                    {
                        int id = Integer.parseInt(match.group(1));
                        int meta = Integer.parseInt(match.group(2));
                        int col = Integer.parseInt(match.group(3), 16);
                        int alpha = Integer.parseInt(match.group(4), 16);
                        TintType tint = TintType.get(match.group(5));
                        if (tint == null) tint = TintType.NONE;
                        //System.out.println("MMMMATCH! "+id+" "+meta+" "+col+" "+alpha+" "+tint);
                        blockColors[blockColorID(id, meta)] = new BlockColor(col, alpha, tint);
                    }
                    else
                    {
                        // old
                        String[] curLine = sCurrentLine.split(":");
                        try
                        {
                            if (curLine[0].equals("Block") && curLine.length == 3)
                            {
                                int newcol = Integer.parseInt(curLine[2], 16);
                                int id = Integer.parseInt(curLine[1]);
                                if (getBlockColor(id, 0).color != newcol) // only
                                                                          // act
                                                                          // if
                                                                          // it's
                                                                          // not
                                                                          // default
                                    blockColors[blockColorID(id, 0)] = new BlockColor(newcol, 0xff, TintType.NONE);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                            // just keep on trucking ...
                        }
                    }
                }
                
                in.close();
            }
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
            out.print("#Available tints: ");
            TintType[] availtints = TintType.values();
            for (int i = 0; i < availtints.length; i++)
            {
                out.print(availtints[i].name());
                if (i < availtints.length - 1) out.print(", ");
            }
            out.println();
            out.println("#format: blockid.metadata: color=RRGGBB/alpha=AA tint=TINTTYPE");
            for (int key = 1; key < blockColors.length; key++)
            {
                if (blockColors[key] == null) continue;
                int meta = key >> 8;
                int id = key & 0xff;
                out.println("" + id + "." + meta + ": color=" + Integer.toHexString(blockColors[key].color) + "/alpha=" + Integer.toHexString(blockColors[key].alpha) + " tint=" + blockColors[key].tintType.name());
            }
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private final int blockColorID(int blockid, int meta)
    {
        return (blockid) | (meta << 8);
    }

    private final BlockColor getBlockColor(int blockid, int meta)
    {
        try
        {
            BlockColor col = blockColors[blockColorID(blockid, meta)];
            if (col != null) return col;
            col = blockColors[blockColorID(blockid, 0)];
            if (col != null) return col;
            col = blockColors[0];
            if (col != null) return col;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.err.println("BlockID: " + blockid + " - Meta: " + meta);
            throw e;
        }
        System.err.println("Unable to find a block color for blockid: " + blockid + " blockmeta: " + meta);
        return new BlockColor(0xff00ff, 0xff, TintType.NONE);
    }

    private void saveAll()
    {
        settingsFile = new File(getAppDir("minecraft"), "zan.settings");

        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
            out.println("Show Minimap:" + Boolean.toString(showmap));
            out.println("Show Coordinates:" + Boolean.toString(coords));
            out.println("Dynamic Lighting:" + Boolean.toString(lightmap));
            out.println("Terrain Depth:" + Boolean.toString(heightmap));
            out.println("Welcome Message:" + Boolean.toString(welcome));
            out.println("Zoom Key:" + Keyboard.getKeyName(zoomKey));
            out.println("Menu Key:" + Keyboard.getKeyName(menuKey));
            out.println("Threading:" + Boolean.toString(threading));
            // out.println("AprF:" +
            // Boolean.toString(haveLoadedBefore?aprilfools:true));
            out.close();
        }
        catch (Exception local)
        {
            chatInfo("§EError Saving Settings");
        }
    }

    private void saveWaypoints()
    {
        settingsFile = new File(getAppDir("minecraft"), world + ".points");

        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));

            for (Waypoint pt : wayPts)
            {
                if (!pt.name.startsWith("^"))
                    out.println(pt.name + ":" + pt.x + ":" + pt.z + ":" + Boolean.toString(pt.enabled) + ":" + pt.red + ":" + pt.green + ":" + pt.blue);
            }

            out.close();
        }
        catch (Exception local)
        {
            chatInfo("§EError Saving Waypoints");
        }
    }

    private void loadWaypoints()
    {
        String j;
        String mapName = getMapName();
        if (mapName.equals("MpServer"))
        {
            String[] i = getServerName().toLowerCase().split(":");
            j = i[0];
        }
        else
            j = mapName;

        if (!world.equals(j))
        {
            world = j;
            iMenu = 1;
            wayPts = new ArrayList<Waypoint>();
            settingsFile = new File(getAppDir("minecraft"), world + ".points");

            try
            {
                if (settingsFile.exists())
                {
                    BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                    String sCurrentLine;

                    while ((sCurrentLine = in.readLine()) != null)
                    {
                        String[] curLine = sCurrentLine.split(":");

                        if (curLine.length == 4)
                            wayPts.add(new Waypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Boolean.parseBoolean(curLine[3])));
                        else
                            wayPts.add(new Waypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Boolean.parseBoolean(curLine[3]), Float.parseFloat(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6])));
                    }

                    in.close();
                    chatInfo("§EWaypoints loaded for " + world);
                }
                else
                    chatInfo("§EError: No waypoints exist for this world/server.");
            }
            catch (Exception local)
            {
                chatInfo("§EError Loading Waypoints");
            }
        }
    }

    private void renderMap(int scWidth)
    {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!this.hide && !this.full)
        {
            if (this.q != 0) glah(this.q);

            if (showmap)
            {
                if (this.zoom == 3)
                {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = this.tex(this.map[this.zoom]);
                    GL11.glPopMatrix();
                }
                else
                    this.q = this.tex(this.map[this.zoom]);

                drawPre();
                this.setMap(scWidth);
                drawPost();

                try
                {
                    this.disp(this.img("/minimap.png"));
                    drawPre();
                    this.setMap(scWidth);
                    drawPost();
                }
                catch (Exception localException)
                {
                    this.error = "error: minimap overlay not found!";
                }

                try
                {
                    GL11.glPushMatrix();
                    this.disp(this.img("/mmarrow.png"));
                    GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                    GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                    drawPre();
                    this.setMap(scWidth);
                    drawPost();
                }
                catch (Exception localException)
                {
                    this.error = "Error: minimap arrow not found!";
                }
                finally
                {
                    GL11.glPopMatrix();
                }
            }
            else
            {
                GL11.glPushMatrix();

                if (this.zoom == 3)
                {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = this.tex(this.map[this.zoom]);
                    GL11.glPopMatrix();
                }
                else
                    this.q = this.tex(this.map[this.zoom]);

                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                GL11.glRotatef(this.direction + 90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);

                if (this.zoom == 0)
                    GL11.glTranslatef(-1.1f, -0.8f, 0.0f);
                else
                    GL11.glTranslatef(-0.5f, -0.5f, 0.0f);

                drawPre();
                this.setMap(scWidth);
                drawPost();
                GL11.glPopMatrix();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                this.drawRound(scWidth);
                this.drawDirections(scWidth);

                for (Waypoint pt : wayPts)
                {
                    if (pt.enabled)
                    {
                        int wayX = this.xCoord() - pt.x;
                        int wayY = this.yCoord() - pt.z;
                        float locate = (float)Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY)) / (Math.pow(2, this.zoom) / 2);

                        if (hypot >= 31.0D)
                        {
                            try
                            {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                this.disp(this.img("/marker.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -34.0D, 0.0D);
                                drawPre();
                                this.setMap(scWidth);
                                drawPost();
                            }
                            catch (Exception localException)
                            {
                                this.error = "Error: marker overlay not found!";
                            }
                            finally
                            {
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }

                for (Waypoint pt : wayPts)
                {
                    if (pt.enabled)
                    {
                        int wayX = this.xCoord() - pt.x;
                        int wayY = this.yCoord() - pt.z;
                        float locate = (float)Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY)) / (Math.pow(2, this.zoom) / 2);

                        if (hypot < 31.0D)
                        {
                            try
                            {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                this.disp(this.img("/waypoint.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                GL11.glRotatef(-(-locate + this.direction + 180.0F), 0.0F, 0.0F, 1.0F);
                                GL11.glTranslated(0.0D, hypot, 0.0D);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                drawPre();
                                this.setMap(scWidth);
                                drawPost();
                            }
                            catch (Exception localException)
                            {
                                this.error = "Error: waypoint overlay not found!";
                            }
                            finally
                            {
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderMapFull(int scWidth, int scHeight)
    {
        this.q = this.tex(this.map[this.zoom]);
        drawPre();
        ldrawone((scWidth + 5) / 2 - 128, (scHeight + 5) / 2 + 128, 1.0D, 0.0D, 1.0D);
        ldrawone((scWidth + 5) / 2 + 128, (scHeight + 5) / 2 + 128, 1.0D, 1.0D, 1.0D);
        ldrawone((scWidth + 5) / 2 + 128, (scHeight + 5) / 2 - 128, 1.0D, 1.0D, 0.0D);
        ldrawone((scWidth + 5) / 2 - 128, (scHeight + 5) / 2 - 128, 1.0D, 0.0D, 0.0D);
        drawPost();

        try
        {
            GL11.glPushMatrix();
            this.disp(this.img("/mmarrow.png"));
            GL11.glTranslatef((scWidth + 5) / 2, (scHeight + 5) / 2, 0.0F);
            GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-((scWidth + 5) / 2), -((scHeight + 5) / 2), 0.0F);
            drawPre();
            ldrawone((scWidth + 5) / 2 - 32, (scHeight + 5) / 2 + 32, 1.0D, 0.0D, 1.0D);
            ldrawone((scWidth + 5) / 2 + 32, (scHeight + 5) / 2 + 32, 1.0D, 1.0D, 1.0D);
            ldrawone((scWidth + 5) / 2 + 32, (scHeight + 5) / 2 - 32, 1.0D, 1.0D, 0.0D);
            ldrawone((scWidth + 5) / 2 - 32, (scHeight + 5) / 2 - 32, 1.0D, 0.0D, 0.0D);
            drawPost();
        }
        catch (Exception localException)
        {
            this.error = "Error: minimap arrow not found!";
        }
        finally
        {
            GL11.glPopMatrix();
        }
    }

    private void showMenu(int scWidth, int scHeight)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int height;
        int maxSize = 0;
        int border = 2;
        boolean set = false;
        boolean click = false;
        int MouseX = getMouseX(scWidth);
        int MouseY = getMouseY(scHeight);

        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0)
            if (!this.lfclick)
            {
                set = true;
                this.lfclick = true;
            }
            else
                click = true;
        else if (this.lfclick) this.lfclick = false;

        String head = "Waypoints";
        String opt1 = "Exit Menu";
        String opt2 = "Waypoints";
        String opt3 = "Remove";

        if (this.iMenu < 3)
        {
            head = this.sMenu[this.iMenu - 1][0];

            for (height = 1; height <= mmOptCount; height++)
                if (this.chkLen(sMenu[iMenu - 1][height]) > maxSize)
                    maxSize = this.chkLen(sMenu[iMenu - 1][height]);
        }
        else
        {
            opt1 = "Back";

            if (this.iMenu == 4)
                opt2 = "Cancel";
            else
                opt2 = "Add";

            maxSize = 80;

            for (int i = 0; i < wayPts.size(); i++)
                if (chkLen((i + 1) + ") " + wayPts.get(i).name) > maxSize)
                    maxSize = chkLen((i + 1) + ") " + wayPts.get(i).name) + 32;

            height = 10;
        }

        int title = this.chkLen(head);
        int centerX = (int)((scWidth + 5) / 2.0D);
        int centerY = (int)((scHeight + 5) / 2.0D);
        String hide = "§7Press §F" + Keyboard.getKeyName(zoomKey) + "§7 to hide.";
        int footer = this.chkLen(hide);
        GL11.glDisable(3553);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        double leftX = centerX - title / 2.0D - border;
        double rightX = centerX + title / 2.0D + border;
        double topY = centerY - (height - 1) / 2.0D * 10.0D - border - 20.0D;
        double botY = centerY - (height - 1) / 2.0D * 10.0D + border - 10.0D;
        this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu == 1)
        {
            leftX = centerX - maxSize / 2.0D - border;
            rightX = centerX + maxSize / 2.0D + border;
            topY = centerY - (height - 1) / 2.0D * 10.0D - border;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border;
            this.drawBox(leftX, rightX, topY, botY);
            leftX = centerX - footer / 2.0D - border;
            rightX = centerX + footer / 2.0D + border;
            topY = centerY + (height - 1) / 2.0D * 10.0D - border + 10.0D;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border + 20.0D;
            this.drawBox(leftX, rightX, topY, botY);
        }
        else
        {
            leftX = centerX - maxSize / 2.0D - 25 - border;
            rightX = centerX + maxSize / 2.0D + 25 + border;
            topY = centerY - (height - 1) / 2.0D * 10.0D - border;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border;
            this.drawBox(leftX, rightX, topY, botY);
            this.drawOptions(rightX - border, topY + border, MouseX, MouseY, set, click);
            footer = this.drawFooter(centerX, centerY, height, opt1, opt2, opt3, border, MouseX, MouseY, set, click);
        }

        GL11.glEnable(3553);
        this.write(head, centerX - title / 2, (centerY - (height - 1) * 10 / 2) - 19, 0xffffff);

        if (this.iMenu == 1)
        {
            for (int n = 1; n < height; n++)
                this.write(this.sMenu[iMenu - 1][n], centerX - maxSize / 2, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 9, 0xffffff);

            this.write(hide, centerX - footer / 2, ((scHeight + 5) / 2 + (height - 1) * 10 / 2 + 11), 0xffffff);
        }
        else
        {
            if (this.iMenu == 2)
            {
                for (int n = 1; n < height; n++)
                {
                    this.write(this.sMenu[iMenu - 1][n], (int)leftX + border + 1, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 9, 0xffffff);

                    if (this.chkOptions(n - 1))
                        hide = "On";
                    else
                        hide = "Off";

                    this.write(hide, (int)rightX - border - 15 - this.chkLen(hide) / 2, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 8, 0xffffff);
                }
            }
            else
            {
                int max = min + 9;

                if (max > wayPts.size())
                {
                    max = wayPts.size();

                    if (min >= 0)
                    {
                        if (max - 9 > 0)
                            min = max - 9;
                        else
                            min = 0;
                    }
                }

                for (int n = min; n < max; n++)
                {
                    int yTop = ((centerY - (height - 1) * 10 / 2) + ((n + 1 - min) * 10));
                    int leftTxt = (int)leftX + border + 1;
                    this.write((n + 1) + ") " + wayPts.get(n).name, leftTxt, yTop - 9, 0xffffff);

                    if (this.iMenu == 4)
                    {
                        hide = "X";
                    }
                    else
                    {
                        if (wayPts.get(n).enabled)
                            hide = "On";
                        else
                            hide = "Off";
                    }

                    this.write(hide, (int)rightX - border - 29 - this.chkLen(hide) / 2, yTop - 8, 0xffffff);

                    if (MouseX > leftTxt && MouseX < (rightX - border - 77) && MouseY > yTop - 10 && MouseY < yTop - 1)
                    {
                        String out = wayPts.get(n).x + ", " + wayPts.get(n).z;
                        int len = chkLen(out) / 2;
                        GL11.glDisable(3553);
                        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
                        this.drawBox(MouseX - len - 1, MouseX + len + 1, MouseY - 11, MouseY - 1);
                        GL11.glEnable(3553);
                        this.write(out, MouseX - len, MouseY - 10, 0xffffff);
                    }
                }
            }

            int footpos = ((scHeight + 5) / 2 + (height - 1) * 10 / 2 + 11);

            if (this.iMenu == 2)
            {
                this.write(opt1, centerX - 5 - border - footer - this.chkLen(opt1) / 2, footpos, 16777215);
                this.write(opt2, centerX + border + 5 + footer - this.chkLen(opt2) / 2, footpos, 16777215);
            }
            else
            {
                if (this.iMenu != 4)
                    this.write(opt1, centerX - 5 - border * 2 - footer * 2 - this.chkLen(opt1) / 2, footpos, 16777215);

                this.write(opt2, centerX - this.chkLen(opt2) / 2, footpos, 16777215);

                if (this.iMenu != 4)
                    this.write(opt3, centerX + 5 + border * 2 + footer * 2 - this.chkLen(opt3) / 2, footpos, 16777215);
            }
        }

        if (this.iMenu > 4)
        {
            String verify = " !\"#$%&'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";

            if (this.iMenu > 5 && this.inStr.equals(""))
                verify = "-0123456789";
            else if (this.iMenu > 5) verify = "0123456789";

            if (Keyboard.getEventKeyState())
            {
                do
                {
                    if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && this.lastKey != Keyboard.KEY_RETURN)
                        if (this.inStr.equals(""))
                            this.next = 3;
                        else if (this.iMenu == 5)
                        {
                            this.next = 6;
                            this.way = this.inStr;
                            this.inStr = Integer.toString(this.xCoord());
                        }
                        else if (this.iMenu == 6)
                        {
                            this.next = 7;

                            try
                            {
                                this.wayX = Integer.parseInt(this.inStr);
                            }
                            catch (Exception localException)
                            {
                                this.next = 3;
                            }

                            this.inStr = Integer.toString(this.yCoord());
                        }
                        else
                        {
                            this.next = 3;

                            try
                            {
                                this.wayZ = Integer.parseInt(this.inStr);
                            }
                            catch (Exception localException)
                            {
                                this.inStr = "";
                            }

                            if (!this.inStr.equals(""))
                            {
                                wayPts.add(new Waypoint(this.way, wayX, wayZ, true));
                                this.saveWaypoints();

                                if (wayPts.size() > 9) min = wayPts.size() - 9;
                            }
                        }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_BACK && this.lastKey != Keyboard.KEY_BACK)
                        if (this.inStr.length() > 0)
                            this.inStr = this.inStr.substring(0, this.inStr.length() - 1);

                    if (verify.indexOf(Keyboard.getEventCharacter()) >= 0 && Keyboard.getEventKey() != this.lastKey)
                        if (this.chkLen(this.inStr + Keyboard.getEventCharacter()) < 148)
                            this.inStr = this.inStr + Keyboard.getEventCharacter();

                    this.lastKey = Keyboard.getEventKey();
                }
                while (Keyboard.next());
            }
            else
                this.lastKey = 0;

            GL11.glDisable(3553);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
            leftX = centerX - 75 - border;
            rightX = centerX + 75 + border;
            topY = centerY - 10 - border;
            botY = centerY + 10 + border;
            this.drawBox(leftX, rightX, topY, botY);
            leftX = leftX + border;
            rightX = rightX - border;
            topY = topY + 11;
            botY = botY - border;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            this.drawBox(leftX, rightX, topY, botY);
            GL11.glEnable(3553);
            String out = "Please enter a name:";

            if (this.iMenu == 6)
                out = "Enter X coordinate:";
            else if (this.iMenu == 7) out = "Enter Z coordinate:";

            this.write(out, (int)leftX + border, (int)topY - 11 + border, 0xffffff);

            if (this.blink > 60) this.blink = 0;

            if (this.blink < 30)
                this.write(this.inStr + "|", (int)leftX + border, (int)topY + border, 0xffffff);
            else
                this.write(this.inStr, (int)leftX + border, (int)topY + border, 0xffffff);

            if (this.iMenu == 6)
                try
                {
                    if (Integer.parseInt(this.inStr) == this.xCoord())
                        this.write("(Current)", (int)leftX + border + this.chkLen(this.inStr) + 5, (int)topY + border, 0xa0a0a0);
                }
                catch (Exception localException)
                {}
            else if (this.iMenu == 7)
                try
                {
                    if (Integer.parseInt(this.inStr) == this.yCoord())
                        this.write("(Current)", (int)leftX + border + this.chkLen(this.inStr) + 5, (int)topY + border, 0xa0a0a0);
                }
                catch (Exception localException)
                {}

            this.blink++;
        }

        if (this.next != 0)
        {
            this.iMenu = this.next;
            this.next = 0;
        }
    }

    private void showCoords(int scWidth, int scHeight)
    {
        if (!this.hide)
        {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            String xy = this.dCoord(xCoord()) + ", " + this.dCoord(yCoord());
            int m = this.chkLen(xy) / 2;
            this.write(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
            xy = Integer.toString(this.zCoord());
            m = this.chkLen(xy) / 2;
            this.write(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
            GL11.glPopMatrix();
        }
        else
            this.write("(" + this.dCoord(xCoord()) + ", " + this.zCoord() + ", " + this.dCoord(yCoord()) + ") " + (int)this.direction + "'", 2, 10, 0xffffff);
    }

    private void drawRound(int paramInt1)
    {
        try
        {
            this.disp(this.img("/roundmap.png"));
            drawPre();
            this.setMap(paramInt1);
            drawPost();
        }
        catch (Exception localException)
        {
            this.error = "Error: minimap overlay not found!";
        }
    }

    private void drawBox(double leftX, double rightX, double topY, double botY)
    {
        drawPre();
        ldrawtwo(leftX, botY, 0.0D);
        ldrawtwo(rightX, botY, 0.0D);
        ldrawtwo(rightX, topY, 0.0D);
        ldrawtwo(leftX, topY, 0.0D);
        drawPost();
    }

    private void drawOptions(double rightX, double topY, int MouseX, int MouseY, boolean set, boolean click)
    {
        if (this.iMenu > 2)
        {
            if (min < 0) min = 0;

            if (!Mouse.isButtonDown(0) && scrClick) scrClick = false;

            if (MouseX > (rightX - 10) && MouseX < (rightX - 2) && MouseY > (topY + 1) && MouseY < (topY + 10))
            {
                if (set || click)
                {
                    if (set && min > 0) min--;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            drawPre();
            ldrawtwo(rightX - 10, topY + 10, 0.0D);
            ldrawtwo(rightX - 2, topY + 10, 0.0D);
            ldrawtwo(rightX - 6, topY + 1, 0.0D);
            ldrawtwo(rightX - 6, topY + 1, 0.0D);
            drawPost();

            if (wayPts.size() > 9)
            {
                sMax = (int)(9.0D / wayPts.size() * 67.0D);
            }
            else
            {
                sMin = 0;
                sMax = 67;
            }

            if (MouseX > rightX - 10 && MouseX < rightX - 2 && MouseY > topY + 12 + sMin && MouseY < topY + 12 + sMin + sMax || scrClick)
            {
                if (Mouse.isButtonDown(0) && !scrClick)
                {
                    scrClick = true;
                    scrStart = MouseY;
                }
                else if (scrClick && wayPts.size() > 9)
                {
                    int offset = MouseY - scrStart;

                    if (sMin + offset < 0)
                        sMin = 0;
                    else if (sMin + offset + sMax > 67)
                        sMin = 67 - sMax;
                    else
                    {
                        sMin = sMin + offset;
                        scrStart = MouseY;
                    }

                    min = (int)((sMin / (67.0D - sMax)) * (wayPts.size() - 9));
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
            {
                if (wayPts.size() > 9)
                    sMin = (int)((double)min / (double)(wayPts.size() - 9) * (67.0D - sMax));

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
            }

            this.drawBox(rightX - 10, rightX - 2, topY + 12 + sMin, topY + 12 + sMin + sMax);

            if (MouseX > rightX - 10 && MouseX < rightX - 2 && MouseY > topY + 81 && MouseY < topY + 90)
            {
                if (set || click)
                {
                    if (set && min < wayPts.size() - 9) min++;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            drawPre();
            ldrawtwo(rightX - 6, topY + 90, 0.0D);
            ldrawtwo(rightX - 6, topY + 90, 0.0D);
            ldrawtwo(rightX - 2, topY + 81, 0.0D);
            ldrawtwo(rightX - 10, topY + 81, 0.0D);
            drawPost();
        }

        double leftX = rightX - 30;
        double botY = 0;
        topY += 1;
        int max = min + 9;

        if (max > wayPts.size())
        {
            max = wayPts.size();

            if (min > 0)
            {
                if (max - 9 > 0)
                    min = max - 9;
                else
                    min = 0;
            }
        }

        double leftCl = 0;
        double rightCl = 0;

        if (this.iMenu > 2)
        {
            leftX = leftX - 14;
            rightX = rightX - 14;
            rightCl = rightX - 32;
            leftCl = rightCl - 9;
        }
        else
        {
            min = 0;
            max = mmOptCount;
        }

        for (int i = min; i < max; i++)
        {
            if (i > min) topY += 10;

            botY = topY + 9;

            if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 5)
                if (set || click)
                {
                    if (set)
                    {
                        if (this.iMenu == 2)
                            this.setOptions(i);
                        else if (this.iMenu == 3)
                        {
                            wayPts.get(i).enabled = !wayPts.get(i).enabled;
                            this.saveWaypoints();
                        }
                        else
                        {
                            this.delWay(i);
                            this.next = 3;
                        }
                    }

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f);
            else
            {
                if (this.iMenu == 2)
                {
                    if (this.chkOptions(i))
                        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
                    else
                        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.6f);
                }
                else if (this.iMenu == 4)
                {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
                }
                else
                {
                    if (wayPts.get(i).enabled)
                    {
                        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
                    }
                    else
                        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.6f);
                }
            }

            this.drawBox(leftX, rightX, topY, botY);

            if (iMenu > 2 && !(iMenu == 4 && this.next == 3))
            {
                if (MouseX > leftCl && MouseX < rightCl && MouseY > topY && MouseY < botY && this.iMenu == 3)
                    if (set)
                    {
                        wayPts.get(i).red = generator.nextFloat();
                        wayPts.get(i).green = generator.nextFloat();
                        wayPts.get(i).blue = generator.nextFloat();
                        saveWaypoints();
                    }

                GL11.glColor3f(wayPts.get(i).red, wayPts.get(i).green, wayPts.get(i).blue);
                this.drawBox(leftCl, rightCl, topY, botY);
            }
        }
    }

    private void delWay(int i)
    {
        wayPts.remove(i);
        this.saveWaypoints();
    }

    private int drawFooter(int centerX, int centerY, int m, String opt1, String opt2, String opt3, int border, int MouseX, int MouseY, boolean set, boolean click)
    {
        int footer = this.chkLen(opt1);

        if (this.chkLen(opt2) > footer) footer = this.chkLen(opt2);

        double leftX = centerX - footer - border * 2 - 5;
        double rightX = centerX - 5;
        double topY = centerY + (m - 1) / 2.0D * 10.0D - border + 10.0D;
        double botY = centerY + (m - 1) / 2.0D * 10.0D + border + 20.0D;

        if (this.iMenu > 2)
        {
            if (this.chkLen(opt3) > footer) footer = this.chkLen(opt3);

            leftX = centerX - border * 3 - footer * 1.5 - 5;
            rightX = centerX - footer / 2 - border - 5;
        }

        if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 4)
            if (set || click)
            {
                if (set)
                {
                    if (this.iMenu == 2)
                        setMenuNull();
                    else
                        this.next = 2;
                }

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else
                GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
        else
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

        if (this.iMenu != 4) this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu == 2)
        {
            leftX = centerX + 5;
            rightX = centerX + footer + border * 2 + 5;
        }
        else
        {
            leftX = centerX - footer / 2 - border;
            rightX = centerX + footer / 2 + border;
        }

        if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 5)
            if (set || click)
            {
                if (set)
                {
                    if (this.iMenu == 2 || this.iMenu == 4)
                        this.next = 3;
                    else
                    {
                        this.next = 5;
                        this.inStr = "";
                    }
                }

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else
                GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
        else
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

        this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu > 2)
        {
            rightX = centerX + border * 3 + footer * 1.5 + 5;
            leftX = centerX + footer / 2 + border + 5;

            if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 4)
                if (set || click)
                {
                    if (set) this.next = 4;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                else
                    GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
            else
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

            if (this.iMenu != 4) this.drawBox(leftX, rightX, topY, botY);
        }

        return footer / 2;
    }

    private boolean chkOptions(int i)
    {
        if (i == 0)
            return coords;
        else if (i == 1)
            return this.hide;
        else if (i == 2)
            return lightmap;
        else if (i == 3)
            return heightmap;
        else if (i == 4)
            return showmap;
        else if (i == 5)
            return welcome;
        else if (i == 6) return threading;
        // TODO: else if (i==7) return aprilfools;
        throw new IllegalArgumentException("bad option number " + i);
    }

    private void setOptions(int i)
    {
        if (i == 0)
            coords = !coords;
        else if (i == 1)
            this.hide = !this.hide;
        else if (i == 2)
            lightmap = !lightmap;
        else if (i == 3)
            heightmap = !heightmap;
        else if (i == 4)
            showmap = !showmap;
        else if (i == 5)
            welcome = !welcome;
        else if (i == 6)
            threading = !threading;
        // TODO: else if (i==7) aprilfools = !aprilfools;
        else
            throw new IllegalArgumentException("bad option number " + i);
        this.saveAll();
        this.timer = 50000;

    }

    private void setMap(int paramInt1)
    {
        ldrawthree(paramInt1 - 64.0D, 64.0D + 5.0D, 1.0D, 0.0D, 1.0D);
        ldrawthree(paramInt1, 64.0D + 5.0D, 1.0D, 1.0D, 1.0D);
        ldrawthree(paramInt1, 5.0D, 1.0D, 1.0D, 0.0D);
        ldrawthree(paramInt1 - 64.0D, 5.0D, 1.0D, 0.0D, 0.0D);
    }

    private void drawDirections(int scWidth)
    {

        /*
         * int wayX = this.xCoord();
         * int wayY = this.yCoord();
         * float locate = (float)Math.toDegrees(Math.atan2(wayX, wayY));
         * double hypot =
         * Math.sqrt((wayX*wayX)+(wayY*wayY))/(Math.pow(2,this.zoom)/2);
         * 
         * 
         * try
         * {
         * GL11.glPushMatrix();
         * GL11.glColor3f(1.0f, 1.0f, 1.0f);
         * this.disp(this.img("/compass.png"));
         * GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
         * GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
         * GL11.glTranslated(0.0D,-hypot,0.0D);
         * GL11.glRotatef(-(-locate + this.direction + 180.0F), 0.0F, 0.0F,
         * 1.0F);
         * GL11.glTranslated(0.0D,hypot,0.0D);
         * GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
         * GL11.glTranslated(0.0D,-hypot,0.0D);
         * drawPre();
         * this.setMap(scWidth);
         * drawPost();
         * } catch (Exception localException)
         * {
         * this.error = "Error: compass overlay not found!";
         * } finally
         * {
         * GL11.glPopMatrix();
         * }
         */

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction - 90.0D)))), (64.0D * Math.cos(Math.toRadians(-(this.direction - 90.0D)))), 0.0D);
        this.write("N", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-this.direction))), (64.0D * Math.cos(Math.toRadians(-this.direction))), 0.0D);
        this.write("E", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 90.0D)))), (64.0D * Math.cos(Math.toRadians(-(this.direction + 90.0D)))), 0.0D);
        this.write("S", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 180.0D)))), (64.0D * Math.cos(Math.toRadians(-(this.direction + 180.0D)))), 0.0D);
        this.write("W", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
    }

    private void SetZoom()
    {
        if (this.fudge > 0) return;

        if (this.iMenu != 0)
        {
            this.iMenu = 0;

            if (getMenu() != null) setMenuNull();
        }
        else
        {
            if (this.zoom == 3)
            {
                if (!this.full)
                    this.full = true;
                else
                {
                    this.zoom = 2;
                    this.full = false;
                    this.error = "Zoom Level: (1.0x)";
                }
            }
            else if (this.zoom == 0)
            {
                this.zoom = 3;
                this.error = "Zoom Level: (0.5x)";
            }
            else if (this.zoom == 2)
            {
                this.zoom = 1;
                this.error = "Zoom Level: (2.0x)";
            }
            else
            {
                this.zoom = 0;
                this.error = "Zoom Level: (4.0x)";
            }
            this.timer = 500;
        }

        this.fudge = 20;
    }

}