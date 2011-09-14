// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst safe
// Source File Name:   SourceFile

package deobf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import net.lahwran.zanminimap.ZanMinimap;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package deobf:
//            lf, aan, ab, ae,
//            et, fv, hw, iw,
//            kc, kh, kq, lr,
//            ow, pj, qs, rv,
//            so, ud, ui, ul,
//            ur, wa, wt, wv,
//            xe, ys, za

public class abj extends lf {

    private static pj d = new pj();
    private java.util.List f;
    private Random g;
    private Minecraft h;
    public String a;
    private int i;
    private String j;
    private int k;
    private boolean l;
    public float b;
    float c;

    public ZanMinimap zanminimap = new ZanMinimap(); //TODO: update

    public abj(Minecraft minecraft) {
        f = ((java.util.List) (new ArrayList()));
        g = new Random();
        a = null;
        i = 0;
        j = "";
        k = 0;
        l = false;
        c = 1.0F;
        h = minecraft;
    }

    public void a(float f1, boolean flag, int i1, int j1) {
        za za1 = new za(h.z, h.d, h.e);
        int k1 = za1.a();
        int l1 = za1.b();
        kh kh1 = h.q;
        h.t.b();
        GL11.glEnable(3042);

        if(Minecraft.u())
            a(h.h.b(f1), k1, l1);

        ul ul1 = h.h.as.e(3);

        if(!h.z.D && ul1 != null && ul1.c == lr.bb.bA)
            a(k1, l1);

        if(!h.h.a(ud.k)) {
            float f2 = h.h.aV + (h.h.aU - h.h.aV) * f1;

            if(f2 > 0.0F)
                b(f2, k1, l1);
        }

        if(!h.c.e()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(3553, h.p.b("/gui/gui.png"));
            ui ui1 = h.h.as;
            e = -90F;
            b(k1 / 2 - 91, l1 - 22, 0, 0, 182, 22);
            b((k1 / 2 - 91 - 1) + ui1.c * 20, l1 - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(3553, h.p.b("/gui/icons.png"));
            GL11.glEnable(3042);
            GL11.glBlendFunc(775, 769);
            b(k1 / 2 - 7, l1 / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(3042);
            boolean flag1 = (h.h.aa / 3) % 2 == 1;

            if(h.h.aa < 10)
                flag1 = false;

            int k2 = h.h.bz;
            int i4 = h.h.bA;
            g.setSeed(i * 0x4c627);
            boolean flag3 = false;
            kc kc1 = h.h.at();
            int j5 = kc1.a();
            int i6 = kc1.b();

            if(h.c.d()) {
                int l6 = k1 / 2 - 91;
                int k7 = k1 / 2 + 91;
                int i8 = h.h.as();

                if(i8 > 0) {
                    char c1 = '\266';
                    int j9 = (h.h.aX * (c1 + 1)) / h.h.as();
                    int i10 = (l1 - 32) + 3;
                    b(l6, i10, 0, 64, ((int) (c1)), 5);

                    if(j9 > 0)
                        b(l6, i10, 0, 69, j9, 5);
                }

                int l8 = l1 - 39;
                int k9 = l8 - 10;
                int j10 = h.h.V();
                int k10 = -1;

                if(h.h.a(ud.l))
                    k10 = i % 25;

                for(int i11 = 0; i11 < 10; i11++) {
                    if(j10 > 0) {
                        int l11 = l6 + i11 * 8;

                        if(i11 * 2 + 1 < j10)
                            b(l11, k9, 34, 9, 9, 9);

                        if(i11 * 2 + 1 == j10)
                            b(l11, k9, 25, 9, 9, 9);

                        if(i11 * 2 + 1 > j10)
                            b(l11, k9, 16, 9, 9, 9);
                    }

                    int i12 = 16;

                    if(h.h.a(ud.u))
                        i12 += 36;

                    int l12 = 0;

                    if(flag1)
                        l12 = 1;

                    int k13 = l6 + i11 * 8;
                    int l13 = l8;

                    if(k2 <= 4)
                        l13 += g.nextInt(2);

                    if(i11 == k10)
                        l13 -= 2;

                    b(k13, l13, 16 + l12 * 9, 0, 9, 9);

                    if(flag1) {
                        if(i11 * 2 + 1 < i4)
                            b(k13, l13, i12 + 54, 0, 9, 9);

                        if(i11 * 2 + 1 == i4)
                            b(k13, l13, i12 + 63, 0, 9, 9);
                    }

                    if(i11 * 2 + 1 < k2)
                        b(k13, l13, i12 + 36, 0, 9, 9);

                    if(i11 * 2 + 1 == k2)
                        b(k13, l13, i12 + 45, 0, 9, 9);
                }

                for(int j11 = 0; j11 < 10; j11++) {
                    int j12 = l8;
                    int i13 = 16;
                    byte byte4 = 0;

                    if(h.h.a(ud.s)) {
                        i13 += 36;
                        byte4 = 13;
                    }

                    if(h.h.at().d() <= 0.0F && i % (j5 * 3 + 1) == 0)
                        j12 += g.nextInt(3) - 1;

                    if(flag3)
                        byte4 = 1;

                    int i14 = k7 - j11 * 8 - 9;
                    b(i14, j12, 16 + byte4 * 9, 27, 9, 9);

                    if(flag3) {
                        if(j11 * 2 + 1 < i6)
                            b(i14, j12, i13 + 54, 27, 9, 9);

                        if(j11 * 2 + 1 == i6)
                            b(i14, j12, i13 + 63, 27, 9, 9);
                    }

                    if(j11 * 2 + 1 < j5)
                        b(i14, j12, i13 + 36, 27, 9, 9);

                    if(j11 * 2 + 1 == j5)
                        b(i14, j12, i13 + 45, 27, 9, 9);
                }

                if(h.h.a(wa.g)) {
                    int k11 = (int)Math.ceil(((double)(h.h.ab - 2) * 10D) / 300D);
                    int k12 = (int)Math.ceil(((double)h.h.ab * 10D) / 300D) - k11;

                    for(int j13 = 0; j13 < k11 + k12; j13++)
                        if(j13 < k11)
                            b(k7 - j13 * 8 - 9, k9, 16, 18, 9, 9);
                        else
                            b(k7 - j13 * 8 - 9, k9, 25, 18, 9, 9);
                }
            }

            GL11.glDisable(3042);
            GL11.glEnable(32826);
            GL11.glPushMatrix();
            GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
            ow.b();
            GL11.glPopMatrix();

            for(int i7 = 0; i7 < 9; i7++) {
                int l7 = (k1 / 2 - 90) + i7 * 20 + 2;
                int j8 = l1 - 16 - 3;
                a(i7, l7, j8, f1);
            }

            ow.a();
            GL11.glDisable(32826);
        }

        if(h.h.ap() > 0) {
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            int i2 = h.h.ap();
            float f4 = (float)i2 / 100F;

            if(f4 > 1.0F)
                f4 = 1.0F - (float)(i2 - 100) / 10F;

            int i3 = (int)(220F * f4) << 24 | 0x101020;
            a(0, 0, k1, l1, i3);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
        }
        zanminimap.onRenderTick(h); //TODO: update

        if(h.c.f())
            if(h.h.aY <= 0);

        if(h.z.E) {
            GL11.glPushMatrix();

            if(Minecraft.I > 0L)
                GL11.glTranslatef(0.0F, 32F, 0.0F);

            kh1.a((new StringBuilder()).append("Minecraft Beta 1.8 (").append(h.L).append(")").toString(), 2, 2, 0xffffff);
            kh1.a(h.o(), 2, 12, 0xffffff);
            kh1.a(h.p(), 2, 22, 0xffffff);
            kh1.a(h.r(), 2, 32, 0xffffff);
            kh1.a(h.q(), 2, 42, 0xffffff);
            long l2 = Runtime.getRuntime().maxMemory();
            long l3 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l5 = l3 - l4;
            String s = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / l2).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(l2 / 1024L / 1024L).append("MB").toString();
            b(kh1, s, k1 - kh1.a(s) - 2, 2, 0xe0e0e0);
            s = (new StringBuilder()).append("Allocated memory: ").append((l3 * 100L) / l2).append("% (").append(l3 / 1024L / 1024L).append("MB)").toString();
            b(kh1, s, k1 - kh1.a(s) - 2, 12, 0xe0e0e0);
            b(kh1, (new StringBuilder()).append("x: ").append(h.h.o).toString(), 2, 64, 0xe0e0e0);
            b(kh1, (new StringBuilder()).append("y: ").append(h.h.p).toString(), 2, 72, 0xe0e0e0);
            b(kh1, (new StringBuilder()).append("z: ").append(h.h.q).toString(), 2, 80, 0xe0e0e0);
            b(kh1, (new StringBuilder()).append("f: ").append(et.b((double)((h.h.u * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            b(kh1, (new StringBuilder()).append("Seed: ").append(h.f.s()).toString(), 2, 104, 0xe0e0e0);
            GL11.glPopMatrix();
        } else {
            kh1.a("Minecraft Beta 1.8", 2, 2, 0xffffff);
        }

        if(k > 0) {
            float f3 = (float)k - f1;
            int j2 = (int)((f3 * 256F) / 20F);

            if(j2 > 255)
                j2 = 255;

            if(j2 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(k1 / 2, l1 - 48, 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                int j3 = 0xffffff;

                if(l)
                    j3 = Color.HSBtoRGB(f3 / 50F, 0.7F, 0.6F) & 0xffffff;

                kh1.b(j, -kh1.a(j) / 2, -4, j3 + (j2 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }

        byte byte0 = 10;
        boolean flag2 = false;

        if(h.r instanceof so) {
            byte0 = 20;
            flag2 = true;
        }

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, l1 - 48, 0.0F);

        for(int k3 = 0; k3 < f.size() && k3 < byte0; k3++) {
            if(((kq)f.get(k3)).b >= 200 && !flag2)
                continue;

            double d1 = (double)((kq)f.get(k3)).b / 200D;
            d1 = 1.0D - d1;
            d1 *= 10D;

            if(d1 < 0.0D)
                d1 = 0.0D;

            if(d1 > 1.0D)
                d1 = 1.0D;

            d1 *= d1;
            int k4 = (int)(255D * d1);

            if(flag2)
                k4 = 255;

            if(k4 > 0) {
                byte byte1 = 2;
                int j6 = -k3 * 9;
                String s1 = ((kq)f.get(k3)).a;
                a(((int) (byte1)), j6 - 1, byte1 + 320, j6 + 8, k4 / 2 << 24);
                GL11.glEnable(3042);
                kh1.a(s1, ((int) (byte1)), j6, 0xffffff + (k4 << 24));
            }
        }

        GL11.glPopMatrix();

        if((h.h instanceof aan) && h.z.x.e) {
            wt wt1 = ((aan)h.h).ci;
            java.util.List list = wt1.c;
            int j4 = wt1.d;
            int i5 = j4;
            int k5 = 1;

            for(; i5 > 20; i5 = ((j4 + k5) - 1) / k5)
                k5++;

            int k6 = 300 / k5;

            if(k6 > 150)
                k6 = 150;

            int j7 = (k1 - k5 * k6) / 2;
            byte byte2 = 10;
            a(j7 - 1, byte2 - 1, j7 + k6 * k5, byte2 + 9 * i5, 0x80000000);

            for(int k8 = 0; k8 < j4; k8++) {
                int i9 = j7 + (k8 % k5) * k6;
                int l9 = byte2 + (k8 / k5) * 9;
                a(i9, l9, (i9 + k6) - 1, l9 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(3008);

                if(k8 >= list.size())
                    continue;

                ab ab1 = (ab)list.get(k8);
                kh1.a(ab1.a, i9, l9, 0xffffff);
                h.p.b(h.p.b("/gui/icons.png"));
                int l10 = 0;
                byte byte3 = 0;
                l10 = 0;
                byte3 = 0;

                if(ab1.b < 0)
                    byte3 = 5;
                else if(ab1.b < 150)
                    byte3 = 0;
                else if(ab1.b < 300)
                    byte3 = 1;
                else if(ab1.b < 600)
                    byte3 = 2;
                else if(ab1.b < 1000)
                    byte3 = 3;
                else
                    byte3 = 4;

                e += 100F;
                b((i9 + k6) - 12, l9, 0 + l10 * 10, 176 + byte3 * 8, 10, 8);
                e -= 100F;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
        GL11.glEnable(3008);
    }

    private void a(int i1, int j1) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, h.p.b("%blur%/misc/pumpkinblur.png"));
        xe xe1 = xe.a;
        xe1.b();
        xe1.a(0.0D, j1, -90D, 0.0D, 1.0D);
        xe1.a(i1, j1, -90D, 1.0D, 1.0D);
        xe1.a(i1, 0.0D, -90D, 1.0D, 0.0D);
        xe1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        xe1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(float f1, int i1, int j1) {
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
        GL11.glBindTexture(3553, h.p.b("%blur%/misc/vignette.png"));
        xe xe1 = xe.a;
        xe1.b();
        xe1.a(0.0D, j1, -90D, 0.0D, 1.0D);
        xe1.a(i1, j1, -90D, 1.0D, 1.0D);
        xe1.a(i1, 0.0D, -90D, 1.0D, 0.0D);
        xe1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        xe1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
    }

    private void b(float f1, int i1, int j1) {
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
        GL11.glBindTexture(3553, h.p.b("/terrain.png"));
        float f2 = (float)(lr.bf.bz % 16) / 16F;
        float f3 = (float)(lr.bf.bz / 16) / 16F;
        float f4 = (float)(lr.bf.bz % 16 + 1) / 16F;
        float f5 = (float)(lr.bf.bz / 16 + 1) / 16F;
        xe xe1 = xe.a;
        xe1.b();
        xe1.a(0.0D, j1, -90D, f2, f5);
        xe1.a(i1, j1, -90D, f4, f5);
        xe1.a(i1, 0.0D, -90D, f4, f3);
        xe1.a(0.0D, 0.0D, -90D, f2, f3);
        xe1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(int i1, int j1, int k1, float f1) {
        ul ul1 = h.h.as.a[i1];

        if(ul1 == null)
            return;

        float f2 = (float)ul1.b - f1;

        if(f2 > 0.0F) {
            GL11.glPushMatrix();
            float f3 = 1.0F + f2 / 5F;
            GL11.glTranslatef(j1 + 8, k1 + 12, 0.0F);
            GL11.glScalef(1.0F / f3, (f3 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(j1 + 8), -(k1 + 12), 0.0F);
        }

        d.a(h.q, h.p, ul1, j1, k1);

        if(f2 > 0.0F)
            GL11.glPopMatrix();

        d.b(h.q, h.p, ul1, j1, k1);
    }

    public void a() {
        if(k > 0)
            k--;

        i++;

        for(int i1 = 0; i1 < f.size(); i1++)
            ((kq)f.get(i1)).b++;
    }

    public void b() {
        f.clear();
    }

    public void a(String s) {
        int i1;

        for(; h.q.a(s) > 320; s = s.substring(i1)) {
            for(i1 = 1; i1 < s.length() && h.q.a(s.substring(0, i1 + 1)) <= 320; i1++);

            a(s.substring(0, i1));
        }

        f.add(0, ((Object) (new kq(s))));

        for(; f.size() > 50; f.remove(f.size() - 1));
    }

    public void b(String s) {
        j = (new StringBuilder()).append("Now playing: ").append(s).toString();
        k = 60;
        l = true;
    }

    public void c(String s) {
        wv wv1 = wv.a();
        String s1 = wv1.a(s);
        a(s1);
    }

}
