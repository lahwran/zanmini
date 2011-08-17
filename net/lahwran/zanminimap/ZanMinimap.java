package net.lahwran.zanminimap;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import deobf.*;

/**
 * Main Zanminimap class where everything happens
 * 
 * @author lahwran
 */
public class ZanMinimap {

    /** Current build version */
    public static final String zmodver = "v0.10.5";

    /** Minecraft version that we'll work with */
    public static final String mcvers = "1.7.3";

    /** World currently loaded */
    public String world = "";

    public boolean haveLoadedBefore;
    
    ObfHub obfhub;
    Config conf;
    MapCalculator mapcalc;
    MapRenderer renderer;
    Menu menu;

    public static ZanMinimap instance;

    public ZanMinimap()
    {

        this.obfhub = new ObfHub(this);
        this.conf = new Config(this);
        this.mapcalc = new MapCalculator(this);
        this.menu = new Menu(this);
        this.renderer = new MapRenderer(this);
        
        conf.initializeEverything();
        mapcalc.start();

        instance = this;
    }

    public void OnTickInGame(Minecraft mc)
    {
        if (obfhub.game == null) obfhub.game = mc;

        if (!obfhub.safeToRun()) return;
        
        int dim = obfhub.getCurrentDimension();
        if(dim != obfhub.lastdim)
        {
            conf.cavemap = dim < 0;
            conf.lightmap = true;
            conf.heightmap = !conf.cavemap;
            conf.netherpoints = conf.cavemap;
            obfhub.lastdim = dim;
            conf.saveConfig();
            if(conf.cavemap)
            {
                conf.full = false;
                conf.zoom=1;
                menu.error = "Cavemap zoom (2.0x)";
            }
        }

        mapcalc.tick();
        String worldName = obfhub.getWorldName();

        if (!world.equals(worldName))
        {
            world = worldName;
            menu.iMenu = 1;
            conf.loadWaypoints();
        }
        
        int[] scSize = obfhub.getScreenSize();
        int scWidth = scSize[0];
        int scHeight = scSize[1];

        if (!obfhub.isMenuShowing()) {
            if (Keyboard.isKeyDown(conf.menuKey)) {
                menu.iMenu = 2;
                obfhub.showPlaceholderGui();
            }
            if (Keyboard.isKeyDown(conf.zoomKey)) {
                conf.nextZoom();
            }
        }

        if ((obfhub.isIngameMenuUp()) ^ (Keyboard.isKeyDown(Keyboard.KEY_F6)))
            conf.enabled = false;
        else
            conf.enabled = true;

        scWidth -= 5;
        scHeight -= 5;

        menu.tick(scWidth, scHeight);
        
    }
}