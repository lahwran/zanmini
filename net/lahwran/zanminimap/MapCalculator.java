/**
 * 
 */
package net.lahwran.zanminimap;

import java.awt.image.BufferedImage;
import java.util.Random;

import deobf.fd;
import deobf.lm;
import deobf.ln;

/**
 * @author lahwran
 *
 */
public class MapCalculator implements Runnable {

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

    private final boolean blockIsSolid(lm chunk, int x, int y, int z)
    {
        if (y>127) return false;
        if (y<0) return true;
        int id = chunk.a(x, y, z);
        int meta = chunk.b(x, y, z);
        return conf.getBlockColor(id, meta).alpha > 0;
    }
    
    private final float distance(int x1, int y1, int x2, int y2)
    {
        return (float)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1,2));
    }

    private final int getBlockHeight(fd world, int x, int z, int starty)
    {
        if (conf.cavemap)
        {
            lm chunk = world.b(x, z);
            cmdist.setSeed((x & 0xffff) | ((z & 0xffff) << 16));
            float dist = distance(obfhub.playerXCoord(),obfhub.playerZCoord(), x, z);
            int y = obfhub.playerYCoord();
            if (dist > 5)
                y -= (cmdist.nextInt((int)(dist)) - ((int)dist/2));
            x &= 0xf;
            z &= 0xf;
            
            
            if(y < 0)
                y = 0;
            else if (y>127)
                y=127;
            
            if(blockIsSolid(chunk, x, y, z))
            {
                int itery = y;
                while(true)
                {
                    itery++;
                    if(itery > y+10)
                        return y+10;
                    if(!blockIsSolid(chunk, x, itery, z))
                    {
                        return itery-1;
                    }
                }
            }
            int origy = y;
            while(y > -1)
            {
                y--;
                if(blockIsSolid(chunk, x, y, z))
                {
                    return y;
                }
            }
            return -1;
        }
        else
        {
            return world.d(x, z)-1;
        }
        // return world.b(x, z).b(x & 0xf, z & 0xf);
        /*
        li chunk = world.b(x, z);
        int y = (int)(game.h.aM); //starty;
        x &= 0xf;
        z &= 0xf;

        //while (y > 0)
       // {
            

            if (getBlockColor(id, meta).alpha == 0)
                return -1;//y--;
            else
                return y + 1; // what
       //}
*/
        //return -1;
    }

    private final int getBlockHeight(fd world, int x, int z)
    {
        return getBlockHeight(world, x, z, 127);
    }


    private int shadeBlock(fd world, int x, int z) {
        int color24 = 0;
        int height = getBlockHeight(world, x, z);
        
        if (conf.color && !conf.cavemap)
        {
            if ((world.f(x, height+1, z) == ln.s) || (world.f(x, height+1, z) == ln.t))
                color24 = 0xffffff;
            else
            {
                BlockColor col = conf.getBlockColor(world.a(x, height, z), world.e(x, height, z));
                color24 = obfhub.getBlockTint(world, col.color, x, height, z, col.tintType);
            }

        }
        else
            color24 = 0x808080;

        if ((color24 != 0xff00ff) && (color24 != 0))
        {
            if (conf.heightmap)
            {
                int i2 = height;
                //if offsetByZloc
                i2 -= obfhub.playerYCoord();
                //else
                //i2 -= 64;
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

            if (conf.lightmap || conf.cavemap)
                i3 = world.a(x, height+1, z, false) * 17;
            int min = 32;
            if(i3 < min)
            {
                i3 = min;
                if (conf.cavemap)
                    color24 = 0x222222;
            }
            if(!conf.lightmap)
            {
                i3 *= 0.5;
                i3 += 64;
            }
            else if(conf.cavemap)
                i3 *= 1.3f;
            
            if (i3 > 255) i3 = 255;
            color24 = i3 * 0x1000000 + color24;
        }
        return color24;
    }
    private void mapCalc()
    {
        if(!obfhub.safeToRun()) return;
        try
        {
            fd data = obfhub.getWorld();
            this.lZoom = conf.zoom;
            int multi = (int)Math.pow(2, this.lZoom);
            int startX = obfhub.playerXCoord();
            int startZ = obfhub.playerZCoord();
            this.lastX = startX;
            this.lastZ = startZ;
            startX -= 16 * multi;
            startZ += 16 * multi;
            int color24 = 0;

            for (int imageY = 0; imageY < 32 * multi; imageY++)
            {
                for (int imageX = 0; imageX < 32 * multi; imageX++)
                {
                    if(!obfhub.safeToRun()) return;
                    color24 = 0;
                    boolean check = false;

                    int a = (16 * multi - imageY);
                    int c = (16 * multi - imageX);
                    int e = (16 * multi);
                    int f = (int) Math.sqrt(multi);
                    if (Math.sqrt(a * a + c * c) < (e - f))
                        check = true;

                    if (check || conf.squaremap || conf.full) {
                        color24 = shadeBlock(data, startX + imageY, startZ - imageX);
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
        if (obfhub.game == null) return;
        while (true)
        {
            if (conf.threading)
            {
                this.active = true;
                while (obfhub.playerExists() && active)
                {
                    if (conf.enabled && !conf.hide)
                        if (((this.lastX != obfhub.playerXCoord()) || (this.lastZ != obfhub.playerZCoord()) || (this.timer > 300)) && obfhub.safeToRun())
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

    void tick() {
        if (conf.threading)
        {

            if (zCalc == null || !zCalc.isAlive() && conf.threading)
            {
                zCalc = new Thread(this);
                zCalc.start();
            }
            if (obfhub.isMenuShowing() && !obfhub.isGameOver() && !obfhub.isConflictWarning())
                try
                {
                    this.zCalc.notify();
                }
                catch (Exception local)
                {}
        }
        else if (!conf.threading)
        {
            if (conf.enabled && !conf.hide )
                if (((this.lastX != obfhub.playerXCoord()) || (this.lastZ != obfhub.playerZCoord()) || (this.timer > 300)))
                    mapCalc();
        }
    }

    /** Is map calc thread still executing? */
    public boolean active = false;

    /** Map calculation thread */
    public Thread zCalc = new Thread(this);

    /** Minimap update interval */
    public int timer = 0;

    /** Last X coordinate rendered */
    public int lastX = 0;

    /** Last Z coordinate rendered */
    public int lastZ = 0;

    /** Last zoom level rendered at */
    public int lZoom = 0;
    
    public Random cmdist = new Random();
    /** Textures for each zoom level */
    public BufferedImage[] map = new BufferedImage[4];

    private ZanMinimap minimap;
    private Config conf;
    private ObfHub obfhub;

    public MapCalculator(ZanMinimap minimap) {
        this.minimap = minimap;
        conf = minimap.conf;
        obfhub = minimap.obfhub;
        this.map[0] = new BufferedImage(32, 32, 2);
        this.map[1] = new BufferedImage(64, 64, 2);
        this.map[2] = new BufferedImage(128, 128, 2);
        this.map[3] = new BufferedImage(256, 256, 2);
    }

    public void start() {
        zCalc.start();
    }
}
