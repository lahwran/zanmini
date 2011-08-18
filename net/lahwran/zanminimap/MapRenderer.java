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

    /**
     * no idea what this does yet, probably a one-length name because of zan's
     * old obfuscation, and then his losing the source
     */
    public int q = 0;

    /** Direction you're facing */
    public float direction = 0.0f;

    /** Last direction you were facing */
    public float oldDir = 0.0f;

    private Menu menu;
    private ObfHub obfhub;
    private Config conf;
    private MapCalculator mapcalc;

    /**
     * @param minimap minimap instance to init with
     */
    public MapRenderer(ZanMinimap minimap) {
        menu = minimap.menu;
        obfhub = minimap.obfhub;
        conf = minimap.conf;
        mapcalc = minimap.mapcalc;
    }

    /**
     * Do rendering
     * 
     * @param scWidth screen width
     * @param scHeight screen height
     */
    public void onRenderTick(int scWidth, int scHeight) {
        if (this.oldDir != obfhub.playerAngle()) {
            this.direction += this.oldDir - obfhub.playerAngle();
            this.oldDir = obfhub.playerAngle();
        }

        if (this.direction >= 360.0f)
            this.direction %= 360.0f;

        if (this.direction < 0.0f) {
            while (this.direction < 0.0f)
                this.direction += 360.0f;
        }

        renderMap(scWidth);
        if (conf.full)
            renderMapFull(scWidth, scHeight);

        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (conf.coords)
            showCoords(scWidth);
    }

    private void renderMap(int scWidth) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!conf.hide && !conf.full) {
            if (this.q != 0)
                obfhub.glah(this.q);

            if (conf.squaremap) {
                if (conf.zoom == 3) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);
                    GL11.glPopMatrix();
                } else
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);

                obfhub.draw_startQuads();
                this.drawOnMap(scWidth);
                obfhub.draw_finish();

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                try {
                    obfhub.disp(obfhub.img("/minimap.png"));
                    obfhub.draw_startQuads();
                    this.drawOnMap(scWidth);
                    obfhub.draw_finish();
                } catch (Exception localException) {
                    menu.error = "error: minimap overlay not found!";
                }

                try {
                    GL11.glPushMatrix();
                    obfhub.disp(obfhub.img("/mmarrow.png"));
                    GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                    GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                    obfhub.draw_startQuads();
                    this.drawOnMap(scWidth);
                    obfhub.draw_finish();
                } catch (Exception localException) {
                    menu.error = "Error: minimap arrow not found!";
                } finally {
                    GL11.glPopMatrix();
                }
            } else {
                GL11.glPushMatrix();

                if (conf.zoom == 3) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 1.0f);
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);
                    GL11.glPopMatrix();
                } else
                    this.q = obfhub.tex(mapcalc.map[conf.zoom]);

                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                GL11.glRotatef(this.direction + 90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);

                if (conf.zoom == 0)
                    GL11.glTranslatef(-1.1f, -0.8f, 0.0f);
                else
                    GL11.glTranslatef(-0.5f, -0.5f, 0.0f);

                obfhub.draw_startQuads();
                this.drawOnMap(scWidth);
                obfhub.draw_finish();
                GL11.glPopMatrix();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                this.drawRound(scWidth);
                this.drawDirections(scWidth);

                for (Waypoint pt : conf.wayPoints) {
                    if (pt.enabled) {
                        int wayX = obfhub.playerXCoord() - (pt.x / (conf.netherpoints ? 8 : 1));
                        int wayY = obfhub.playerZCoord() - (pt.z / (conf.netherpoints ? 8 : 1));
                        float locate = (float) Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY))
                                / (Math.pow(2, conf.zoom) / 2);

                        if (hypot >= 31.0D) {
                            try {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                obfhub.disp(obfhub.img("/marker.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -34.0D, 0.0D);
                                obfhub.draw_startQuads();
                                this.drawOnMap(scWidth);
                                obfhub.draw_finish();
                            } catch (Exception localException) {
                                menu.error = "Error: marker overlay not found!";
                            } finally {
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }

                for (Waypoint pt : conf.wayPoints) {
                    if (pt.enabled) {
                        int wayX = obfhub.playerXCoord() - (pt.x / (conf.netherpoints ? 8 : 1));
                        int wayY = obfhub.playerZCoord() - (pt.z / (conf.netherpoints ? 8 : 1));
                        float locate = (float) Math.toDegrees(Math.atan2(wayX, wayY));
                        double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY))
                                / (Math.pow(2, conf.zoom) / 2);

                        if (hypot < 31.0D) {
                            try {
                                GL11.glPushMatrix();
                                GL11.glColor3f(pt.red, pt.green, pt.blue);
                                obfhub.disp(obfhub.img("/waypoint.png"));
                                GL11.glTranslatef(scWidth - 32.0F, 37.0F, 0.0F);
                                GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                GL11.glRotatef(-(-locate + this.direction + 180.0F),
                                        0.0F,
                                        0.0F,
                                        1.0F);
                                GL11.glTranslated(0.0D, hypot, 0.0D);
                                GL11.glTranslatef(-(scWidth - 32.0F), -37.0F, 0.0F);
                                GL11.glTranslated(0.0D, -hypot, 0.0D);
                                obfhub.draw_startQuads();
                                this.drawOnMap(scWidth);
                                obfhub.draw_finish();
                            } catch (Exception localException) {
                                menu.error = "Error: waypoint overlay not found!";
                            } finally {
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderMapFull(int scWidth, int scHeight) {
        this.q = obfhub.tex(mapcalc.map[conf.zoom]);
        obfhub.draw_startQuads();
        obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 - 128,
                (scHeight + ZanMinimap.mysteriousFive) / 2 + 128,
                1.0D,
                0.0D,
                1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 + 128,
                (scHeight + ZanMinimap.mysteriousFive) / 2 + 128,
                1.0D,
                1.0D,
                1.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 + 128,
                (scHeight + ZanMinimap.mysteriousFive) / 2 - 128,
                1.0D,
                1.0D,
                0.0D);
        obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 - 128,
                (scHeight + ZanMinimap.mysteriousFive) / 2 - 128,
                1.0D,
                0.0D,
                0.0D);
        obfhub.draw_finish();

        try {
            GL11.glPushMatrix();
            obfhub.disp(obfhub.img("/mmarrow.png"));
            GL11.glTranslatef((scWidth + ZanMinimap.mysteriousFive) / 2, (scHeight + ZanMinimap.mysteriousFive) / 2, 0.0F);
            GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-((scWidth + ZanMinimap.mysteriousFive) / 2), -((scHeight + ZanMinimap.mysteriousFive) / 2), 0.0F);
            obfhub.draw_startQuads();
            obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 - 32,
                    (scHeight + ZanMinimap.mysteriousFive) / 2 + 32,
                    1.0D,
                    0.0D,
                    1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 + 32,
                    (scHeight + ZanMinimap.mysteriousFive) / 2 + 32,
                    1.0D,
                    1.0D,
                    1.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 + 32,
                    (scHeight + ZanMinimap.mysteriousFive) / 2 - 32,
                    1.0D,
                    1.0D,
                    0.0D);
            obfhub.ldraw_addVertexWithUV((scWidth + ZanMinimap.mysteriousFive) / 2 - 32,
                    (scHeight + ZanMinimap.mysteriousFive) / 2 - 32,
                    1.0D,
                    0.0D,
                    0.0D);
            obfhub.draw_finish();
        } catch (Exception localException) {
            menu.error = "Error: minimap arrow not found!";
        } finally {
            GL11.glPopMatrix();
        }
    }

    private void showCoords(int scWidth) {
        if (!conf.hide) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            String xy = obfhub.dCoord(obfhub.playerXCoord()) + ", "
                    + obfhub.dCoord(obfhub.playerZCoord());
            int m = obfhub.calcStringLength(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
            xy = Integer.toString(obfhub.playerYCoord());
            m = obfhub.calcStringLength(xy) / 2;
            obfhub.write(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
            GL11.glPopMatrix();
        } else
            obfhub.write("(" + obfhub.dCoord(obfhub.playerXCoord()) + ", " + obfhub.playerYCoord()
                    + ", " + obfhub.dCoord(obfhub.playerZCoord()) + ") " + (int) this.direction
                    + "'", 2, 10, 0xffffff);
    }

    private void drawRound(int paramInt1) {
        try {
            obfhub.disp(obfhub.img("/roundmap.png"));
            obfhub.draw_startQuads();
            this.drawOnMap(paramInt1);
            obfhub.draw_finish();
        } catch (Exception localException) {
            menu.error = "Error: minimap overlay not found!";
        }
    }

    private void drawOnMap(int paramInt1) {
        obfhub.ldraw_addVertexWithUV(paramInt1 - 64.0D, 64.0D + ZanMinimap.mysteriousFivePointO, 1.0D, 0.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1, 64.0D + ZanMinimap.mysteriousFivePointO, 1.0D, 1.0D, 1.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1, ZanMinimap.mysteriousFivePointO, 1.0D, 1.0D, 0.0D);
        obfhub.ldraw_addVertexWithUV(paramInt1 - 64.0D, ZanMinimap.mysteriousFivePointO, 1.0D, 0.0D, 0.0D);
    }

    private void drawDirections(int scWidth) {

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
        obfhub.write("N", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-this.direction))),
                (64.0D * Math.cos(Math.toRadians(-this.direction))),
                0.0D);
        obfhub.write("E", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 90.0D)))),
                (64.0D * Math.cos(Math.toRadians(-(this.direction + 90.0D)))),
                0.0D);
        obfhub.write("S", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(this.direction + 180.0D)))),
                (64.0D * Math.cos(Math.toRadians(-(this.direction + 180.0D)))),
                0.0D);
        obfhub.write("W", scWidth * 2 - 66, 70, 0xffffff);
        GL11.glPopMatrix();
    }
}
