/**
 * 
 */
package net.lahwran.zanminimap;

import org.lwjgl.opengl.GL11;

/**
 * @author lahwran
 * 
 */
public class MapRenderer {

    /** Direction you're facing */
    public float direction = 0.0f;

    /** Last direction you were facing */
    public float oldDir = 0.0f;

    private Menu menu;
    private ObfHub obfhub;
    private Config conf;
    private Map map;
    private TextureManager texman;

    /**
     * @param minimap minimap instance to init with
     */
    public MapRenderer(ZanMinimap minimap) {
        menu = minimap.menu;
        obfhub = minimap.obfhub;
        conf = minimap.conf;
        map = minimap.map;
        texman = minimap.texman;
    }

    /**
     * Do rendering
     * 
     * @param scWidth screen width
     * @param scHeight screen height
     */
    public void onRenderTick(int scWidth, int scHeight) {
        if (this.oldDir != obfhub.getPlayerYaw()) {
            this.direction += this.oldDir - obfhub.getPlayerYaw();
            this.oldDir = obfhub.getPlayerYaw();
        }

        if (this.direction >= 360.0f)
            this.direction %= 360.0f;

        if (this.direction < 0.0f) {
            while (this.direction < 0.0f)
                this.direction += 360.0f;
        }


        GL11.glPushMatrix();
        GL11.glTranslatef(scWidth, 0.0f, 0.0f);
        renderMap();
        if (conf.full)
            renderMapFull(0, scHeight);

        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (conf.coords)
            showCoords(0);
        GL11.glPopMatrix();
    }

    private void renderMap() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!conf.hide && !conf.full) {

            if (conf.squaremap) {
                if (conf.zoom == 3) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    map.loadColorImage(obfhub);
                    GL11.glPopMatrix();
                } else
                    map.loadColorImage(obfhub);

                drawMap();

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                try {
                    texman.loadMinimap();
                    drawOnMap();
                } catch (Exception e) {
                    menu.error = "error: minimap overlay not found!";
                    e.printStackTrace();
                }

                try {
                    GL11.glPushMatrix();
                    texman.loadMMArrow();
                    GL11.glTranslatef(- 32.0F, 32.0F + ZanMinimap.heightOffset, 0.0F);
                    GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(32.0F, -(32.0F + ZanMinimap.heightOffset), 0.0F);
                    drawOnMap();
                } catch (Exception e) {
                    menu.error = "Error: minimap arrow not found!";
                    e.printStackTrace();
                } finally {
                    GL11.glPopMatrix();
                }
            } else {
                GL11.glPushMatrix();

                if (conf.zoom == 3) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    map.loadColorImage(obfhub);
                    GL11.glPopMatrix();
                } else
                    map.loadColorImage(obfhub);

                GL11.glTranslatef(-32.0f, 32.0F + ZanMinimap.heightOffset, 0.0F);
                GL11.glRotatef(this.direction + 90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef( 32.0F, -(32.0F + ZanMinimap.heightOffset), 0.0F);

                if (conf.zoom == 0)
                    GL11.glTranslatef(-1.1f, -0.8f, 0.0f);
                else
                    GL11.glTranslatef(-0.5f, -0.5f, 0.0f);

                drawMap();
                GL11.glPopMatrix();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                renderWaypoints();
                drawRound();
                drawDirections();

            }
        }
    }

    private void renderWaypoints() {
        for (Waypoint pt : conf.wayPoints) {
            if (pt.enabled) {
                double wayX = obfhub.getPlayerX() - (pt.x / (conf.netherpoints ? 8 : 1));
                double wayY = obfhub.getPlayerZ() - (pt.z / (conf.netherpoints ? 8 : 1));
                float locate = (float) Math.toDegrees(Math.atan2(wayX, wayY));
                double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY))
                        / (Math.pow(2.0, conf.zoom) / 2.0);

                if (hypot >= 28.0D) {
                    try {
                        GL11.glPushMatrix();
                        GL11.glColor3f(pt.red, pt.green, pt.blue);
                        texman.loadMarker();
                        GL11.glTranslatef(-32.0F, 32.0F + ZanMinimap.heightOffset, 0.0F);
                        GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(32.0F, -(32.0F + ZanMinimap.heightOffset), 0.0F);
                        GL11.glTranslated(0.0D, -34.0D, 0.0D);
                        drawOnMap();
                    } catch (Exception e) {
                        menu.error = "Error: marker overlay not found!";
                        e.printStackTrace();
                    } finally {
                        GL11.glPopMatrix();
                    }
                } else {
                    try {
                        GL11.glPushMatrix();
                        GL11.glColor3f(pt.red, pt.green, pt.blue);
                        texman.loadWaypoint();
                        GL11.glTranslatef(- 32.0F, 32.0F + ZanMinimap.heightOffset, 0.0F);
                        GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslated(0.0D, -hypot, 0.0D);
                        GL11.glRotatef(-(-locate + this.direction + 180.0F),
                                0.0F,
                                0.0F,
                                1.0F);
                        GL11.glTranslated(0.0D, hypot, 0.0D);
                        GL11.glTranslatef(32.0F, -(32.0F + ZanMinimap.heightOffset), 0.0F);
                        GL11.glTranslated(0.0D, -hypot, 0.0D);
                        drawOnMap();
                    } catch (Exception e) {
                        menu.error = "Error: waypoint overlay not found!";
                        e.printStackTrace();
                    } finally {
                        GL11.glPopMatrix();
                    }
                }
            }
        }
        GL11.glColor3f(1f, 1f, 1f);
    }

    private void renderMapFull(int scWidth, int scHeight) {
        map.loadColorImage(obfhub);
        obfhub.draw_startQuads();
        obfhub.ldraw_addVertexWithUV((scWidth) / 2 - 128,
                (scHeight + ZanMinimap.heightOffset) / 2 + 128,
                1.0D,
                0.0D,
                1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth) / 2 + 128,
                (scHeight + ZanMinimap.heightOffset) / 2 + 128,
                1.0D,
                1.0D,
                1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth) / 2 + 128,
                (scHeight + ZanMinimap.heightOffset) / 2 - 128,
                1.0D,
                1.0D,
                0.0D);
        obfhub.ldraw_addVertexWithUV((scWidth) / 2 - 128,
                (scHeight + ZanMinimap.heightOffset) / 2 - 128,
                1.0D,
                0.0D,
                0.0D);
        obfhub.draw_finish();

        try {
            GL11.glPushMatrix();
            texman.loadMMArrow();
            GL11.glTranslatef((scWidth) / 2, (scHeight + ZanMinimap.heightOffset) / 2, 0.0F);
            GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-((scWidth) / 2), -((scHeight + ZanMinimap.heightOffset) / 2), 0.0F);
            obfhub.draw_startQuads();
            obfhub.ldraw_addVertexWithUV((scWidth) / 2 - 32,
                    (scHeight + ZanMinimap.heightOffset) / 2 + 32,
                    1.0D,
                    0.0D,
                    1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth) / 2 + 32,
                    (scHeight + ZanMinimap.heightOffset) / 2 + 32,
                    1.0D,
                    1.0D,
                    1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth) / 2 + 32,
                    (scHeight + ZanMinimap.heightOffset) / 2 - 32,
                    1.0D,
                    1.0D,
                    0.0D);
            obfhub.ldraw_addVertexWithUV((scWidth) / 2 - 32,
                    (scHeight + ZanMinimap.heightOffset) / 2 - 32,
                    1.0D,
                    0.0D,
                    0.0D);
            obfhub.draw_finish();
        } catch (Exception e) {
            menu.error = "Error: minimap arrow not found!";
            e.printStackTrace();
        } finally {
            GL11.glPopMatrix();
        }
    }

    private void showCoords(int scWidth) {
        if (!conf.hide) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            String xy = obfhub.dCoord((int) obfhub.getPlayerX()) + ", "
                    + obfhub.dCoord((int) obfhub.getPlayerZ());
            int m = obfhub.calcStringLength(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
            xy = Integer.toString((int) obfhub.getPlayerY());
            m = obfhub.calcStringLength(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
            GL11.glPopMatrix();
        } else
            obfhub.write("(" + obfhub.dCoord((int) obfhub.getPlayerX()) + ", " + obfhub.getPlayerY()
                    + ", " + obfhub.dCoord((int) obfhub.getPlayerZ()) + ") " + (int) this.direction
                    + "'", 2, 10, 0xffffff);
    }

    private void drawRound() {
        try {
            texman.loadRoundmap();
            drawOnMap();
        } catch (Exception localException) {
            menu.error = "Error: minimap overlay not found!";
        }
    }

    private void drawOnMap() {
        obfhub.draw_startQuads();
        obfhub.ldraw_addVertexWithUV(-64.0D, 64.0D + ZanMinimap.heightOffset, 1.0D, 0.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(     0, 64.0D + ZanMinimap.heightOffset, 1.0D, 1.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(     0,         ZanMinimap.heightOffset, 1.0D, 1.0D, 0.0D);
        obfhub.ldraw_addVertexWithUV(-64.0D,         ZanMinimap.heightOffset, 1.0D, 0.0D, 0.0D);
        obfhub.draw_finish();
    }

    private void drawMap() {
        float renderwidth = 64;
        GL11.glPushMatrix();
        GL11.glTranslatef(-32.0F, 32.0F + ZanMinimap.heightOffset, 0);
        GL11.glScalef(renderwidth, renderwidth, 1.0f);
        GL11.glScalef(1.0f/map.imageSize, 1.0f/map.imageSize, 1.0f);
        float renderscale = map.getRenderScale();
        GL11.glTranslated(map.imageSize-1, 0, 0);
        GL11.glScalef(1.0f/renderwidth, 1.0f/renderwidth, 1.0f);
        GL11.glScalef(map.imageSize, map.imageSize, 1.0f);
        //GL11.glTranslated(map.getCurrOffsetX(obfhub.getPlayerZ()), map.getCurrOffsetY(obfhub.getPlayerX()), 0);
        //GL11.glScalef(blockscale, blockscale, 1.0f);
        
        //float renderscale = map.getRenderScale();
        GL11.glScalef(renderscale, renderscale, 1.0f);
        obfhub.draw_startQuads();
        obfhub.ldraw_addVertexWithUV(-32.0D,  32.0D, 1.0D, 0.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV( 32.0D,  32.0D, 1.0D, 1.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV( 32.0D, -32.0D, 1.0D, 1.0D, 0.0D);
        obfhub.ldraw_addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.0D, 0.0D);

        obfhub.draw_finish();
        GL11.glPopMatrix();
    }

    private void drawDirections() {

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
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction - 90.0D)))),
                (64.0D * Math.cos(Math.toRadians(-(this.direction - 90.0D)))),
                0.0D);
        obfhub.write("N", - 66, 60 + ZanMinimap.heightOffset * 2, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-this.direction))),
                (64.0D * Math.cos(Math.toRadians(-this.direction))),
                0.0D);
        obfhub.write("E", - 66, 60 + ZanMinimap.heightOffset * 2, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 90.0D)))),
                (64.0D * Math.cos(Math.toRadians(-(this.direction + 90.0D)))),
                0.0D);
        obfhub.write("S", - 66, 60 + ZanMinimap.heightOffset * 2, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 180.0D)))),
                (64.0D * Math.cos(Math.toRadians(-(this.direction + 180.0D)))),
                0.0D);
        obfhub.write("W", - 66, 60 + ZanMinimap.heightOffset * 2, 0xffffff);
        GL11.glPopMatrix();
    }
}
