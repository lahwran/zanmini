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


    public int q = 0;
    /** Direction you're facing */
    public float direction = 0.0f;

    /** Last direction you were facing */
    public float oldDir = 0.0f;

    /** World currently loaded */
    public String world = "";

    public boolean haveLoadedBefore;
    
    ObfHub obfhub;
    Config conf;
    MapCalculator mapcalc;
    Menu menu;

    public static ZanMinimap instance;

    public ZanMinimap()
    {

        this.obfhub = new ObfHub(this);
        this.conf = new Config(this);
        this.mapcalc = new MapCalculator(this);
        this.menu = new Menu(this);
        
        conf.initializeEverything();
        mapcalc.start();
        

        //ModLoader.SetInGameHook(this, true, false);

        instance = this;

    }
    

    
    void OnTickInGame(Minecraft mc)
    {
        if (obfhub.game == null) obfhub.game = mc;

        if (!safeToRun()) return;
        
        int dim = obfhub.getCurrentDimension();
        if(dim != obfhub.lastdim)
        {
            conf.cavemap = dim < 0;
            conf.lightmap = true;
            conf.heightmap = !conf.cavemap;
            conf.netherpoints = conf.cavemap;
            obfhub.lastdim = dim;
            conf.saveAll();
            if(conf.cavemap)
            {
                conf.full = false;
                conf.zoom=1;
                menu.error = "Cavemap zoom (2.0x)";
            }
        }
        
        mapcalc.tick();

        obfhub.updateLang();

        obfhub.updateRenderEngine();

        
        int[] scSize = obfhub.getScreenSize();
        int scWidth = scSize[0];
        int scHeight = scSize[1];

        if (Keyboard.isKeyDown(conf.menuKey) && !obfhub.isMenuShowing())
        {
            menu.iMenu = 2;
            obfhub.showGenericGui();
        }

        if (Keyboard.isKeyDown(conf.zoomKey) && !obfhub.isMenuShowing())
        {
            conf.setZoom();
        }

        conf.loadWaypoints();

        if (menu.iMenu == 1)
        {
            if (!conf.welcome) menu.iMenu = 0;
        }
        if (obfhub.game.r == null && menu.iMenu > 1) menu.iMenu = 0;

        if ((obfhub.isIngameMenuUp()) ^ (Keyboard.isKeyDown(Keyboard.KEY_F6)))
            conf.enabled = false;
        else
            conf.enabled = true;


        scWidth -= 5;
        scHeight -= 5;

        if (this.oldDir != obfhub.radius())
        {
            this.direction += this.oldDir - obfhub.radius();
            this.oldDir = obfhub.radius();
        }

        if (this.direction >= 360.0f) while (this.direction >= 360.0f)
            this.direction -= 360.0f;

        if (this.direction < 0.0f)
        {
            while (this.direction < 0.0f)
                this.direction += 360.0f;
        }
        
        menu.tick(scWidth, scHeight);

        if (conf.enabled)
        {
            renderMap(scWidth);

            if (conf.full) renderMapFull(scWidth, scHeight);

            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (conf.coords) showCoords(scWidth, scHeight);
        }
    }

    boolean safeToRun() {
        return obfhub.safeToRun();
    }

    private void renderMap(int scWidth)
    {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!conf.hide && !conf.full)
        {
            if (this.q != 0) obfhub.glah(this.q);

            if (conf.showmap)
            {
                if (conf.zoom == 3)
                {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);
                    GL11.glPopMatrix();
                }
                else
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);

                obfhub.draw_startQuads();
                this.setMap(scWidth);
                obfhub.draw_finish();

                try
                {
                    obfhub.disp(obfhub.img("/minimap.png"));
                    obfhub.draw_startQuads();
                    this.setMap(scWidth);
                    obfhub.draw_finish();
                }
                catch (Exception localException)
                {
                    menu.error = "error: minimap overlay not found!";
                }

                try
                {
                    GL11.glPushMatrix();
                    obfhub.disp(obfhub.img("/mmarrow.png"));
                    GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                    GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                    obfhub.draw_startQuads();
                    this.setMap(scWidth);
                    obfhub.draw_finish();
                }
                catch (Exception localException)
                {
                    menu.error = "Error: minimap arrow not found!";
                }
                finally
                {
                    GL11.glPopMatrix();
                }
            }
            else
            {
                GL11.glPushMatrix();

                if (conf.zoom == 3)
                {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);
                    GL11.glPopMatrix();
                }
                else
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);

                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                GL11.glRotatef(this.direction + 90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);

                if (conf.zoom == 0)
                    GL11.glTranslatef(-1.1f, -0.8f, 0.0f);
                else
                    GL11.glTranslatef(-0.5f, -0.5f, 0.0f);

                obfhub.draw_startQuads();
                this.setMap(scWidth);
                obfhub.draw_finish();
                GL11.glPopMatrix();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                this.drawRound(scWidth);
                this.drawDirections(scWidth);

                for (Waypoint pt : conf.wayPts)
                {
                    if (pt.enabled)
                    {
                        int wayX = obfhub.playerXCoord() - (pt.x / (conf.netherpoints ? 8 : 1));
                        int wayY = obfhub.playerZCoord() - (pt.z / (conf.netherpoints ? 8 : 1));
                        float locate = (float)Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY)) / (Math.pow(2, conf.zoom) / 2);

                        if (hypot >= 31.0D)
                        {
                            try
                            {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                obfhub.disp(obfhub.img("/marker.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -34.0D, 0.0D);
                                obfhub.draw_startQuads();
                                this.setMap(scWidth);
                                obfhub.draw_finish();
                            }
                            catch (Exception localException)
                            {
                                menu.error = "Error: marker overlay not found!";
                            }
                            finally
                            {
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }

                for (Waypoint pt : conf.wayPts)
                {
                    if (pt.enabled)
                    {
                        int wayX = obfhub.playerXCoord() - (pt.x / (conf.netherpoints ? 8 : 1));
                        int wayY = obfhub.playerZCoord() - (pt.z / (conf.netherpoints ? 8 : 1));
                        float locate = (float)Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY)) / (Math.pow(2, conf.zoom) / 2);

                        if (hypot < 31.0D)
                        {
                            try
                            {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                obfhub.disp(obfhub.img("/waypoint.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                GL11.glRotatef(-(-locate + this.direction + 180.0F), 0.0F, 0.0F, 1.0F);
                                GL11.glTranslated(0.0D, hypot, 0.0D);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                obfhub.draw_startQuads();
                                this.setMap(scWidth);
                                obfhub.draw_finish();
                            }
                            catch (Exception localException)
                            {
                                menu.error = "Error: waypoint overlay not found!";
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
        this.q = obfhub.tex(mapcalc.map[conf.zoom]);
        obfhub.draw_startQuads();
        obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 - 128, (scHeight + 5) / 2 + 128, 1.0D, 0.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 + 128, (scHeight + 5) / 2 + 128, 1.0D, 1.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 + 128, (scHeight + 5) / 2 - 128, 1.0D, 1.0D, 0.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 - 128, (scHeight + 5) / 2 - 128, 1.0D, 0.0D, 0.0D);
        obfhub.draw_finish();

        try
        {
            GL11.glPushMatrix();
            obfhub.disp(obfhub.img("/mmarrow.png"));
            GL11.glTranslatef((scWidth + 5) / 2, (scHeight + 5) / 2, 0.0F);
            GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-((scWidth + 5) / 2), -((scHeight + 5) / 2), 0.0F);
            obfhub.draw_startQuads();
            obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 - 32, (scHeight + 5) / 2 + 32, 1.0D, 0.0D, 1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 + 32, (scHeight + 5) / 2 + 32, 1.0D, 1.0D, 1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 + 32, (scHeight + 5) / 2 - 32, 1.0D, 1.0D, 0.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + 5) / 2 - 32, (scHeight + 5) / 2 - 32, 1.0D, 0.0D, 0.0D);
            obfhub.draw_finish();
        }
        catch (Exception localException)
        {
            menu.error = "Error: minimap arrow not found!";
        }
        finally
        {
            GL11.glPopMatrix();
        }
    }



    private void showCoords(int scWidth, int scHeight)
    {
        if (!conf.hide)
        {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            String xy = obfhub.dCoord(obfhub.playerXCoord()) + ", " + obfhub.dCoord(obfhub.playerZCoord());
            int m = obfhub.chkLen(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
            xy = Integer.toString(obfhub.playerYCoord());
            m = obfhub.chkLen(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
            GL11.glPopMatrix();
        }
        else
            obfhub.write("(" + obfhub.dCoord(obfhub.playerXCoord()) + ", " + obfhub.playerYCoord() + ", " + obfhub.dCoord(obfhub.playerZCoord()) + ") " + (int)this.direction + "'", 2, 10, 0xffffff);
    }

    private void drawRound(int paramInt1)
    {
        try
        {
            obfhub.disp(obfhub.img("/roundmap.png"));
            obfhub.draw_startQuads();
            this.setMap(paramInt1);
            obfhub.draw_finish();
        }
        catch (Exception localException)
        {
            menu.error = "Error: minimap overlay not found!";
        }
    }



    private void setMap(int paramInt1)
    {
        obfhub.ldraw_addVertexWithUV(paramInt1 - 64.0D, 64.0D + 5.0D, 1.0D, 0.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1, 64.0D + 5.0D, 1.0D, 1.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1, 5.0D, 1.0D, 1.0D, 0.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1 - 64.0D, 5.0D, 1.0D, 0.0D, 0.0D);
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
        obfhub.write("N", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-this.direction))), (64.0D * Math.cos(Math.toRadians(-this.direction))), 0.0D);
        obfhub.write("E", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 90.0D)))), (64.0D * Math.cos(Math.toRadians(-(this.direction + 90.0D)))), 0.0D);
        obfhub.write("S", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 180.0D)))), (64.0D * Math.cos(Math.toRadians(-(this.direction + 180.0D)))), 0.0D);
        obfhub.write("W", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
    }

}