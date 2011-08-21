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

    /**
     * Multiply two colors by each other. Treats 0xff as 1.0.
     * 
     * Yourself came up with the algorithm, I'm sure it makes sense to someone
     * 
     * @param x Color to multiply
     * @param y Other color to multiply
     * @return multiplied color
     */
    public int colorMult(int x, int y) {
        int res = 0;
        for (int octet = 0; octet < 3; ++octet) {
            res |= (((x & 0xff) * (y & 0xff)) / 0xff) << (octet << 3);
            x >>= 8;
            y >>= 8;
        }
        return res;
    }

    private final boolean blockIsSolid(lm chunk, int x, int y, int z) {
        if (y > 127)
            return false;
        if (y < 0)
            return true;
        int id = chunk.a(x, y, z);
        int meta = chunk.b(x, y, z);
        return conf.getBlockColor(id, meta).alpha > 0;
    }

    private final float distance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private final int getBlockHeight(fd world, int x, int z) {
        if (conf.cavemap) {
            lm chunk = world.b(x, z);
            cmdist.setSeed((x & 0xffff) | ((z & 0xffff) << 16));
            float dist = distance((int)obfhub.playerXCoord(), (int)obfhub.playerZCoord(), x, z);
            int y = (int) obfhub.playerYCoord();
            if (dist > 5)
                y -= (cmdist.nextInt((int) (dist)) - ((int) dist / 2));
            x &= 0xf;
            z &= 0xf;

            if (y < 0)
                y = 0;
            else if (y > 127)
                y = 127;

            if (blockIsSolid(chunk, x, y, z)) {
                int itery = y;
                while (true) {
                    itery++;
                    if (itery > y + 10)
                        return y + 10;
                    if (!blockIsSolid(chunk, x, itery, z)) {
                        return itery - 1;
                    }
                }
            }
            while (y > -1) {
                y--;
                if (blockIsSolid(chunk, x, y, z)) {
                    return y;
                }
            }
            return -1;
        }

        return world.d(x, z) - 1;

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

    private int shadeBlock(fd world, int x, int z) {
        int color24 = 0;
        int height = getBlockHeight(world, x, z);

        if (conf.color && !conf.cavemap) {
            if ((world.f(x, height + 1, z) == ln.s) || (world.f(x, height + 1, z) == ln.t))
                color24 = 0xffffff;
            else {
                BlockColor col = conf.getBlockColor(world.a(x, height, z), world.e(x, height, z));
                color24 = obfhub.getBlockTint(world, col.color, x, height, z, col.tintType);
            }

        } else
            color24 = 0x808080;

        if ((color24 != 0xff00ff) && (color24 != 0)) {
            if (conf.heightmap) {
                int i2 = height;
                //if offsetByZloc
                i2 -= obfhub.playerYCoord();
                //else
                //i2 -= 64;
                double sc = Math.log10(Math.abs(i2) / 8.0D + 1.0D) / 1.3D;
                int r = color24 / 0x10000;
                int g = (color24 - r * 0x10000) / 0x100;
                int b = (color24 - r * 0x10000 - g * 0x100);

                if (i2 >= 0) {
                    r = (int) (sc * (0xff - r)) + r;
                    g = (int) (sc * (0xff - g)) + g;
                    b = (int) (sc * (0xff - b)) + b;
                } else {
                    i2 = Math.abs(i2);
                    r = r - (int) (sc * r);
                    g = g - (int) (sc * g);
                    b = b - (int) (sc * b);
                }

                color24 = r * 0x10000 + g * 0x100 + b;
            }

            int i3 = 255;

            if (conf.lightmap || conf.cavemap)
                i3 = world.a(x, height + 1, z, false) * 17;
            int min = 32;
            if (i3 < min) {
                i3 = min;
                if (conf.cavemap)
                    color24 = 0x222222;
            }
            if (!conf.lightmap) {
                i3 *= 0.5;
                i3 += 64;
            } else if (conf.cavemap)
                i3 *= 1.3f;

            if (i3 > 255)
                i3 = 255;
            color24 = i3 * 0x1000000 + color24;
        }
        return color24;
    }

    private void mapCalc() {
        if (!obfhub.safeToRun())
            return;
        try {
            synchronized (map) {
                fd data = obfhub.getWorld();
                map.zoom = conf.zoom;
                map.update(obfhub.playerXCoord(), obfhub.playerZCoord());
                int startX = (int) (map.getPlayerX() - map.renderOff);
                int startZ = (int) (map.getPlayerZ() - map.renderOff);
                int color24 = 0;

                for (int worldX = startX; worldX < startX + map.renderSize; worldX++) {
                    for (int worldZ = startZ; worldZ < startZ + map.renderSize; worldZ++) {
                        if (!obfhub.safeToRun())
                            return;
                        color24 = shadeBlock(data, worldX, worldZ);
                        map.setMapPixel(worldX, worldZ, color24);
                    }
                }
            }
        } catch (Throwable whatever) {
            whatever.printStackTrace();
        }
    }

    /**
     * Check if a render is necessary, and if so, do one.
     */
    private void tryARender() {
        if (!obfhub.playerExists())
            return;
        try {
            if (conf.enabled && !conf.hide && map.isDirty(obfhub.playerXCoord(), obfhub.playerZCoord())) {
                mapCalc();
                map.timer = 1;
            }
        } catch (RuntimeException e) {
            throw e;
        } finally {
            map.timer++;
        }
    }

    /**
     * the run() to implement runnable - the main function of the other thread.
     * when threading is disabled, this simply idles and the actual work is
     * done in onRenderTick().
     */
    public void run() {
        if (obfhub.game == null)
            return;
        while (true) {
            try {
                if (conf.threading) {
                    tryARender();
                    Thread.sleep(100);
                } else {
                        synchronized(zCalc) {
                            this.zCalc.wait(10000);
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Wake the thread up if it's sleeping.
     */
    void pokeThread() {
        synchronized (zCalc) {
            zCalc.notify();
        }
    }

    /**
     * Called each tick of the render.
     */
    void onRenderTick() {
        if (conf.threading) {
            if (zCalc == null || !zCalc.isAlive()) {
                zCalc = new Thread(this);
                zCalc.start();
            }
        } else if (!conf.threading) {
            tryARender();
        }
    }

    /**
     * Map calculation thread
     */
    public Thread zCalc = new Thread(this);

    /**
     * Random used to distort cave map
     */
    public Random cmdist = new Random();

    private Config conf;
    private ObfHub obfhub;

    private Map map;

    /**
     * This constructor inits state, but does not start the thread.
     * 
     * @param minimap Minimap instance to initialize off
     */
    public MapCalculator(ZanMinimap minimap) {
        conf = minimap.conf;
        obfhub = minimap.obfhub;
        map = minimap.map;
    }

    /**
     * Start up the other thread. The thread may return early at this point,
     * as there might not be a Minecraft instance available yet. if that occurs,
     * the thread will be restarted by the keep-alive in onRenderTick().
     */
    public void start() {
        zCalc.start();
    }
}
