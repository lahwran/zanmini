// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst safe
// Source File Name:   SourceFile

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class uk extends tw {

    private static ba d = new ba();
    private java.util.List e;
    private Random f;
    private Minecraft g;
    public String a;
    private int h;
    private String i;
    private int j;
    private boolean l;
    public float b;
    float c;

    private ZanMinimap minimap = new ZanMinimap();

    public uk(Minecraft minecraft) {
        e = ((java.util.List) (new ArrayList()));
        f = new Random();
        a = null;
        h = 0;
        i = "";
        j = 0;
        l = false;
        c = 1.0F;
        g = minecraft;
    }

    public void a(float f1, boolean flag, int k, int i1) {
        qm qm1 = new qm(g.z, g.d, g.e);
        int j1 = qm1.a();
        int k1 = qm1.b();
        se se1 = g.q;
        g.t.b();
        GL11.glEnable(3042);

        if(Minecraft.u())
            a(g.h.a(f1), j1, k1);

        iw iw1 = g.h.c.d(3);

        if(!g.z.A && iw1 != null && iw1.c == un.bb.bn)
            a(j1, k1);

        float f2 = g.h.C + (g.h.B - g.h.C) * f1;

        if(f2 > 0.0F)
            b(f2, j1, k1);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, g.p.b("/gui/gui.png"));
        iu iu1 = g.h.c;
        this.k = -90F;
        b(j1 / 2 - 91, k1 - 22, 0, 0, 182, 22);
        b((j1 / 2 - 91 - 1) + iu1.c * 20, k1 - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture(3553, g.p.b("/gui/icons.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(775, 769);
        b(j1 / 2 - 7, k1 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(3042);
        boolean flag1 = (g.h.by / 3) % 2 == 1;

        if(g.h.by < 10)
            flag1 = false;

        int l1 = g.h.Y;
        int i2 = g.h.Z;
        f.setSeed(h * 0x4c627);

        if(g.c.d()) {
            int j2 = g.h.s();

            for(int i3 = 0; i3 < 10; i3++) {
                int j4 = k1 - 32;

                if(j2 > 0) {
                    int i6 = (j1 / 2 + 91) - i3 * 8 - 9;

                    if(i3 * 2 + 1 < j2)
                        b(i6, j4, 34, 9, 9, 9);

                    if(i3 * 2 + 1 == j2)
                        b(i6, j4, 25, 9, 9, 9);

                    if(i3 * 2 + 1 > j2)
                        b(i6, j4, 16, 9, 9, 9);
                }

                int j6 = 0;

                if(flag1)
                    j6 = 1;

                int l6 = (j1 / 2 - 91) + i3 * 8;

                if(l1 <= 4)
                    j4 += f.nextInt(2);

                b(l6, j4, 16 + j6 * 9, 0, 9, 9);

                if(flag1) {
                    if(i3 * 2 + 1 < i2)
                        b(l6, j4, 70, 0, 9, 9);

                    if(i3 * 2 + 1 == i2)
                        b(l6, j4, 79, 0, 9, 9);
                }

                if(i3 * 2 + 1 < l1)
                    b(l6, j4, 52, 0, 9, 9);

                if(i3 * 2 + 1 == l1)
                    b(l6, j4, 61, 0, 9, 9);
            }

            if(g.h.a(lj.g)) {
                int j3 = (int)Math.ceil(((double)(g.h.bz - 2) * 10D) / 300D);
                int k4 = (int)Math.ceil(((double)g.h.bz * 10D) / 300D) - j3;

                for(int k6 = 0; k6 < j3 + k4; k6++)
                    if(k6 < j3)
                        b((j1 / 2 - 91) + k6 * 8, k1 - 32 - 9, 16, 18, 9, 9);
                    else
                        b((j1 / 2 - 91) + k6 * 8, k1 - 32 - 9, 25, 18, 9, 9);
            }
        }

        GL11.glDisable(3042);
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        t.b();
        GL11.glPopMatrix();

        for(int k2 = 0; k2 < 9; k2++) {
            int k3 = (j1 / 2 - 90) + k2 * 20 + 2;
            int l4 = k1 - 16 - 3;
            a(k2, k3, l4, f1);
        }

        t.a();
        GL11.glDisable(32826);

        if(g.h.P() > 0) {
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            int l2 = g.h.P();
            float f4 = (float)l2 / 100F;

            if(f4 > 1.0F)
                f4 = 1.0F - (float)(l2 - 100) / 10F;

            int i5 = (int)(220F * f4) << 24 | 0x101020;
            a(0, 0, j1, k1, i5);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
        }
        minimap.OnTickInGame(g);

        if(g.z.B) {
            GL11.glPushMatrix();

            if(Minecraft.H > 0L)
                GL11.glTranslatef(0.0F, 32F, 0.0F);

            se1.a((new StringBuilder()).append("Minecraft Beta 1.6.6 (").append(g.K).append(")").toString(), 2, 2, 0xffffff);
            se1.a(g.o(), 2, 12, 0xffffff);
            se1.a(g.p(), 2, 22, 0xffffff);
            se1.a(g.r(), 2, 32, 0xffffff);
            se1.a(g.q(), 2, 42, 0xffffff);
            long l3 = Runtime.getRuntime().maxMemory();
            long l5 = Runtime.getRuntime().totalMemory();
            long l7 = Runtime.getRuntime().freeMemory();
            long l8 = l5 - l7;
            String s = (new StringBuilder()).append("Used memory: ").append((l8 * 100L) / l3).append("% (").append(l8 / 1024L / 1024L).append("MB) of ").append(l3 / 1024L / 1024L).append("MB").toString();
            b(se1, s, j1 - se1.a(s) - 2, 2, 0xe0e0e0);
            s = (new StringBuilder()).append("Allocated memory: ").append((l5 * 100L) / l3).append("% (").append(l5 / 1024L / 1024L).append("MB)").toString();
            b(se1, s, j1 - se1.a(s) - 2, 12, 0xe0e0e0);
            b(se1, (new StringBuilder()).append("x: ").append(g.h.aM).toString(), 2, 64, 0xe0e0e0);
            b(se1, (new StringBuilder()).append("y: ").append(g.h.aN).toString(), 2, 72, 0xe0e0e0);
            b(se1, (new StringBuilder()).append("z: ").append(g.h.aO).toString(), 2, 80, 0xe0e0e0);
            GL11.glPopMatrix();
        }

        if(j > 0) {
            float f3 = (float)j - f1;
            int i4 = (int)((f3 * 256F) / 20F);

            if(i4 > 255)
                i4 = 255;

            if(i4 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(j1 / 2, k1 - 48, 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                int j5 = 0xffffff;

                if(l)
                    j5 = Color.HSBtoRGB(f3 / 50F, 0.7F, 0.6F) & 0xffffff;

                se1.b(i, -se1.a(i) / 2, -4, j5 + (i4 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }

        byte byte0 = 10;
        boolean flag2 = false;

        if(g.r instanceof ga) {
            byte0 = 20;
            flag2 = true;
        }

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, k1 - 48, 0.0F);

        for(int k5 = 0; k5 < e.size() && k5 < byte0; k5++) {
            if(((sr)e.get(k5)).b >= 200 && !flag2)
                continue;

            double d1 = (double)((sr)e.get(k5)).b / 200D;
            d1 = 1.0D - d1;
            d1 *= 10D;

            if(d1 < 0.0D)
                d1 = 0.0D;

            if(d1 > 1.0D)
                d1 = 1.0D;

            d1 *= d1;
            int i7 = (int)(255D * d1);

            if(flag2)
                i7 = 255;

            if(i7 > 0) {
                byte byte1 = 2;
                int j7 = -k5 * 9;
                String s1 = ((sr)e.get(k5)).a;
                a(((int) (byte1)), j7 - 1, byte1 + 320, j7 + 8, i7 / 2 << 24);
                GL11.glEnable(3042);
                se1.a(s1, ((int) (byte1)), j7, 0xffffff + (i7 << 24));
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void a(int k, int i1) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, g.p.b("%blur%/misc/pumpkinblur.png"));
        ns ns1 = ns.a;
        ns1.b();
        ns1.a(0.0D, i1, -90D, 0.0D, 1.0D);
        ns1.a(k, i1, -90D, 1.0D, 1.0D);
        ns1.a(k, 0.0D, -90D, 1.0D, 0.0D);
        ns1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        ns1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(float f1, int k, int i1) {
        f1 = 1.0F - f1;

        if(f1 < 0.0F)
            f1 = 0.0F;

        if(f1 > 1.0F)
            f1 = 1.0F;

        c += ((float) ((double)(f1 - c) * 0.01D));
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(0, 769);
        GL11.glColor4f(c, c, c, 1.0F);
        GL11.glBindTexture(3553, g.p.b("%blur%/misc/vignette.png"));
        ns ns1 = ns.a;
        ns1.b();
        ns1.a(0.0D, i1, -90D, 0.0D, 1.0D);
        ns1.a(k, i1, -90D, 1.0D, 1.0D);
        ns1.a(k, 0.0D, -90D, 1.0D, 0.0D);
        ns1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        ns1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
    }

    private void b(float f1, int k, int i1) {
        if(f1 < 1.0F) {
            f1 *= f1;
            f1 *= f1;
            f1 = f1 * 0.8F + 0.2F;
        }

        GL11.glDisable(3008);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
        GL11.glBindTexture(3553, g.p.b("/terrain.png"));
        float f2 = (float)(un.bf.bm % 16) / 16F;
        float f3 = (float)(un.bf.bm / 16) / 16F;
        float f4 = (float)(un.bf.bm % 16 + 1) / 16F;
        float f5 = (float)(un.bf.bm / 16 + 1) / 16F;
        ns ns1 = ns.a;
        ns1.b();
        ns1.a(0.0D, i1, -90D, f2, f5);
        ns1.a(k, i1, -90D, f4, f5);
        ns1.a(k, 0.0D, -90D, f4, f3);
        ns1.a(0.0D, 0.0D, -90D, f2, f3);
        ns1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(int k, int i1, int j1, float f1) {
        iw iw1 = g.h.c.a[k];

        if(iw1 == null)
            return;

        float f2 = (float)iw1.b - f1;

        if(f2 > 0.0F) {
            GL11.glPushMatrix();
            float f3 = 1.0F + f2 / 5F;
            GL11.glTranslatef(i1 + 8, j1 + 12, 0.0F);
            GL11.glScalef(1.0F / f3, (f3 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(i1 + 8), -(j1 + 12), 0.0F);
        }

        d.a(g.q, g.p, iw1, i1, j1);

        if(f2 > 0.0F)
            GL11.glPopMatrix();

        d.b(g.q, g.p, iw1, i1, j1);
    }

    public void a() {
        if(j > 0)
            j--;

        h++;

        for(int k = 0; k < e.size(); k++)
            ((sr)e.get(k)).b++;
    }

    public void b() {
        e.clear();
    }

    public void a(String s) {
        int k;

        for(; g.q.a(s) > 320; s = s.substring(k)) {
            for(k = 1; k < s.length() && g.q.a(s.substring(0, k + 1)) <= 320; k++);

            a(s.substring(0, k));
        }

        e.add(0, ((Object) (new sr(s))));

        for(; e.size() > 50; e.remove(e.size() - 1));
    }

    public void b(String s) {
        i = (new StringBuilder()).append("Now playing: ").append(s).toString();
        j = 60;
        l = true;
    }

    public void c(String s) {
        nd nd1 = nd.a();
        String s1 = nd1.a(s);
        a(s1);
    }

}
