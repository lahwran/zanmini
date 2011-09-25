package deobf;

import deobf.adf;
import deobf.ax;
import deobf.bf;
import deobf.bl;
import deobf.bz;
import deobf.dl;
import deobf.et;
import deobf.fx;
import deobf.i;
import deobf.jo;
import deobf.kj;
import deobf.km;
import deobf.lr;
import deobf.ma;
import deobf.nl;
import deobf.nv;
import deobf.o;
import deobf.ow;
import deobf.qs;
import deobf.rp;
import deobf.rv;
import deobf.sz;
import deobf.ud;
import deobf.vh;
import deobf.wa;
import deobf.wd;
import deobf.xe;
import deobf.za;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class iw {

   public static boolean a = false;
   public static int b;
   private Minecraft r;
   private float s = 0.0F;
   public jo c;
   private int t;
   private kj u = null;
   private bl v = new bl();
   private bl w = new bl();
   private bl x = new bl();
   private bl y = new bl();
   private bl z = new bl();
   private bl A = new bl();
   private float B = 4.0F;
   private float C = 4.0F;
   private float D = 0.0F;
   private float E = 0.0F;
   private float F = 0.0F;
   private float G = 0.0F;
   private float H = 0.0F;
   private float I = 0.0F;
   private float J = 0.0F;
   private float K = 0.0F;
   public int d;
   private int[] L;
   private float M;
   private float N;
   private float O;
   private boolean P = false;
   private double Q = 1.0D;
   private double R = 0.0D;
   private double S = 0.0D;
   private long T = System.currentTimeMillis();
   private long U = 0L;
   private boolean V = false;
   float e = 0.0F;
   float f = 0.0F;
   float g = 0.0F;
   float h = 0.0F;
   private Random W = new Random();
   private int X = 0;
   float[] i;
   float[] j;
   volatile int k = 0;
   volatile int l = 0;
   FloatBuffer m = dl.e(16);
   float n;
   float o;
   float p;
   private float Y;
   private float Z;
   public int q;


   public iw(Minecraft var1) {
      this.r = var1;
      this.c = new jo(var1);
      this.d = var1.p.a(new BufferedImage(16, 16, 1));
      this.L = new int[256];
   }

   public void a() {
      this.c();
      this.d();
      this.Y = this.Z;
      this.C = this.B;
      this.E = this.D;
      this.G = this.F;
      this.I = this.H;
      this.K = this.J;
      if(this.r.i == null) {
         this.r.i = this.r.h;
      }

      float var1 = this.r.f.c(et.b(this.r.i.o), et.b(this.r.i.p), et.b(this.r.i.q));
      float var2 = (float)(3 - this.r.z.e) / 3.0F;
      float var3 = var1 * (1.0F - var2) + var2;
      this.Z += (var3 - this.Z) * 0.1F;
      ++this.t;
      this.c.a();
      this.f();
   }

   public void a(float var1) {
      if(this.r.i != null) {
         if(this.r.f != null) {
            double var2 = (double)this.r.c.b();
            this.r.y = this.r.i.a(var2, var1);
            double var4 = var2;
            ax var6 = this.r.i.g(var1);
            if(this.r.y != null) {
               var4 = this.r.y.f.d(var6);
            }

            if(this.r.c.i()) {
               var2 = 32.0D;
               var4 = 32.0D;
            } else {
               if(var4 > 3.0D) {
                  var4 = 3.0D;
               }

               var2 = var4;
            }

            ax var7 = this.r.i.h(var1);
            ax var8 = var6.c(var7.a * var2, var7.b * var2, var7.c * var2);
            this.u = null;
            float var9 = 1.0F;
            List var10 = this.r.f.b((kj)this.r.i, this.r.i.y.a(var7.a * var2, var7.b * var2, var7.c * var2).b((double)var9, (double)var9, (double)var9));
            double var11 = 0.0D;

            for(int var13 = 0; var13 < var10.size(); ++var13) {
               kj var14 = (kj)var10.get(var13);
               if(var14.d_()) {
                  float var15 = var14.i_();
                  rp var16 = var14.y.b((double)var15, (double)var15, (double)var15);
                  ma var17 = var16.a(var6, var8);
                  if(var16.a(var6)) {
                     if(0.0D < var11 || var11 == 0.0D) {
                        this.u = var14;
                        var11 = 0.0D;
                     }
                  } else if(var17 != null) {
                     double var18 = var6.d(var17.f);
                     if(var18 < var11 || var11 == 0.0D) {
                        this.u = var14;
                        var11 = var18;
                     }
                  }
               }
            }

            if(this.u != null) {
               this.r.y = new ma(this.u);
            }

         }
      }
   }

   private void c() {
      qs var1 = (qs)this.r.i;
      this.O = var1.u_();
      this.N = this.M;
      this.M += (this.O - this.M) * 0.5F;
   }

   private float a(float var1, boolean var2) {
      if(this.q > 0) {
         return 90.0F;
      } else {
         sz var3 = (sz)this.r.i;
         float var4 = 70.0F;
         if(var2) {
            var4 += this.r.z.L * 40.0F;
            var4 *= this.N + (this.M - this.N) * var1;
         }

         if(var3.bz <= 0) {
            float var5 = (float)var3.bE + var1;
            var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
         }

         if(var3.a(wa.g)) {
            var4 = var4 * 60.0F / 70.0F;
         }

         return var4 + this.I + (this.H - this.I) * var1;
      }
   }

   private void d(float var1) {
      wd var2 = this.r.i;
      float var3 = (float)var2.bB - var1;
      float var4;
      if(var2.bz <= 0) {
         var4 = (float)var2.bE + var1;
         GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
      }

      if(var3 >= 0.0F) {
         var3 /= (float)var2.bC;
         var3 = et.a(var3 * var3 * var3 * var3 * 3.1415927F);
         var4 = var2.bD;
         GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
      }
   }

   private void e(float var1) {
      if(this.r.i instanceof sz) {
         sz var2 = (sz)this.r.i;
         float var3 = var2.L - var2.K;
         float var4 = -(var2.L + var3 * var1);
         float var5 = var2.az + (var2.aA - var2.az) * var1;
         float var6 = var2.bG + (var2.bH - var2.bG) * var1;
         GL11.glTranslatef(et.a(var4 * 3.1415927F) * var5 * 0.5F, -Math.abs(et.b(var4 * 3.1415927F) * var5), 0.0F);
         GL11.glRotatef(et.a(var4 * 3.1415927F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(Math.abs(et.b(var4 * 3.1415927F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
      }
   }

   private void f(float var1) {
      wd var2 = this.r.i;
      float var3 = var2.H - 1.62F;
      double var4 = var2.l + (var2.o - var2.l) * (double)var1;
      double var6 = var2.m + (var2.p - var2.m) * (double)var1 - (double)var3;
      double var8 = var2.n + (var2.q - var2.n) * (double)var1;
      GL11.glRotatef(this.K + (this.J - this.K) * var1, 0.0F, 0.0F, 1.0F);
      if(var2.an()) {
         var3 = (float)((double)var3 + 1.0D);
         GL11.glTranslatef(0.0F, 0.3F, 0.0F);
         if(!this.r.z.I) {
            int var10 = this.r.f.a(et.b(var2.o), et.b(var2.p), et.b(var2.q));
            if(var10 == lr.T.bA) {
               int var11 = this.r.f.e(et.b(var2.o), et.b(var2.p), et.b(var2.q));
               int var12 = var11 & 3;
               GL11.glRotatef((float)(var12 * 90), 0.0F, 1.0F, 0.0F);
            }

            GL11.glRotatef(var2.w + (var2.u - var2.w) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(var2.x + (var2.v - var2.x) * var1, -1.0F, 0.0F, 0.0F);
         }
      } else if(this.r.z.D) {
         double var27 = (double)(this.C + (this.B - this.C) * var1);
         float var13;
         float var28;
         if(this.r.z.I) {
            var28 = this.E + (this.D - this.E) * var1;
            var13 = this.G + (this.F - this.G) * var1;
            GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
            GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
         } else {
            var28 = var2.u;
            var13 = var2.v;
            double var14 = (double)(-et.a(var28 / 180.0F * 3.1415927F) * et.b(var13 / 180.0F * 3.1415927F)) * var27;
            double var16 = (double)(et.b(var28 / 180.0F * 3.1415927F) * et.b(var13 / 180.0F * 3.1415927F)) * var27;
            double var18 = (double)(-et.a(var13 / 180.0F * 3.1415927F)) * var27;

            for(int var20 = 0; var20 < 8; ++var20) {
               float var21 = (float)((var20 & 1) * 2 - 1);
               float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
               float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
               var21 *= 0.1F;
               var22 *= 0.1F;
               var23 *= 0.1F;
               ma var24 = this.r.f.a(ax.b(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), ax.b(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
               if(var24 != null) {
                  double var25 = var24.f.d(ax.b(var4, var6, var8));
                  if(var25 < var27) {
                     var27 = var25;
                  }
               }
            }

            GL11.glRotatef(var2.v - var13, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.u - var28, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
            GL11.glRotatef(var28 - var2.u, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var13 - var2.v, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GL11.glTranslatef(0.0F, 0.0F, -0.1F);
      }

      if(!this.r.z.I) {
         GL11.glRotatef(var2.x + (var2.v - var2.x) * var1, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var2.w + (var2.u - var2.w) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, var3, 0.0F);
      var4 = var2.l + (var2.o - var2.l) * (double)var1;
      var6 = var2.m + (var2.p - var2.m) * (double)var1 - (double)var3;
      var8 = var2.n + (var2.q - var2.n) * (double)var1;
      this.P = this.r.g.a(var4, var6, var8, var1);
   }

   private void a(float var1, int var2) {
      this.s = (float)(256 >> this.r.z.e);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float var3 = 0.07F;
      if(this.r.z.g) {
         GL11.glTranslatef((float)(-(var2 * 2 - 1)) * var3, 0.0F, 0.0F);
      }

      if(this.Q != 1.0D) {
         GL11.glTranslatef((float)this.R, (float)(-this.S), 0.0F);
         GL11.glScaled(this.Q, this.Q, 1.0D);
         GLU.gluPerspective(this.a(var1, true), (float)this.r.d / (float)this.r.e, 0.05F, this.s * 2.0F);
      } else {
         GLU.gluPerspective(this.a(var1, true), (float)this.r.d / (float)this.r.e, 0.05F, this.s * 2.0F);
      }

      float var4;
      if(this.r.c.e()) {
         var4 = 0.6666667F;
         GL11.glScalef(1.0F, var4, 1.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if(this.r.z.g) {
         GL11.glTranslatef((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.d(var1);
      if(this.r.z.f) {
         this.e(var1);
      }

      var4 = this.r.h.aV + (this.r.h.aU - this.r.h.aV) * var1;
      if(var4 > 0.0F) {
         byte var5 = 20;
         if(this.r.h.a(ud.k)) {
            var5 = 7;
         }

         float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
         var6 *= var6;
         GL11.glRotatef(((float)this.t + var1) * (float)var5, 0.0F, 1.0F, 1.0F);
         GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
         GL11.glRotatef(-((float)this.t + var1) * (float)var5, 0.0F, 1.0F, 1.0F);
      }

      this.f(var1);
      if(this.q > 0) {
         int var7 = this.q - 1;
         if(var7 == 1) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }

         if(var7 == 2) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         if(var7 == 3) {
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         }

         if(var7 == 4) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         }

         if(var7 == 5) {
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
         }
      }

   }

   private void b(float var1, int var2) {
      if(this.q <= 0) {
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         float var3 = 0.07F;
         if(this.r.z.g) {
            GL11.glTranslatef((float)(-(var2 * 2 - 1)) * var3, 0.0F, 0.0F);
         }

         if(this.Q != 1.0D) {
            GL11.glTranslatef((float)this.R, (float)(-this.S), 0.0F);
            GL11.glScaled(this.Q, this.Q, 1.0D);
            GLU.gluPerspective(this.a(var1, false), (float)this.r.d / (float)this.r.e, 0.05F, this.s * 2.0F);
         } else {
            GLU.gluPerspective(this.a(var1, false), (float)this.r.d / (float)this.r.e, 0.05F, this.s * 2.0F);
         }

         if(this.r.c.e()) {
            float var4 = 0.6666667F;
            GL11.glScalef(1.0F, var4, 1.0F);
         }

         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         if(this.r.z.g) {
            GL11.glTranslatef((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         GL11.glPushMatrix();
         this.d(var1);
         if(this.r.z.f) {
            this.e(var1);
         }

         if(!this.r.z.D && !this.r.i.an() && !this.r.z.C && !this.r.c.e()) {
            this.b((double)var1);
            this.c.a(var1);
            this.a((double)var1);
         }

         GL11.glPopMatrix();
         if(!this.r.z.D && !this.r.i.an()) {
            this.c.b(var1);
            this.d(var1);
         }

         if(this.r.z.f) {
            this.e(var1);
         }

      }
   }

   public void a(double var1) {
      GL13.glClientActiveTexture('\u84c1');
      GL13.glActiveTexture('\u84c1');
      GL11.glDisable(3553);
      GL13.glClientActiveTexture('\u84c0');
      GL13.glActiveTexture('\u84c0');
   }

   public void b(double var1) {
      GL13.glClientActiveTexture('\u84c1');
      GL13.glActiveTexture('\u84c1');
      GL11.glMatrixMode(5890);
      GL11.glLoadIdentity();
      float var3 = 0.00390625F;
      GL11.glScalef(var3, var3, var3);
      GL11.glTranslatef(8.0F, 8.0F, 8.0F);
      GL11.glMatrixMode(5888);
      this.r.p.b(this.d);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10242, 10496);
      GL11.glTexParameteri(3553, 10243, 10496);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL13.glClientActiveTexture('\u84c0');
      GL13.glActiveTexture('\u84c0');
   }

   private void d() {
      this.f = (float)((double)this.f + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.h = (float)((double)this.h + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.f = (float)((double)this.f * 0.9D);
      this.h = (float)((double)this.h * 0.9D);
      this.e += (this.f - this.e) * 1.0F;
      this.g += (this.h - this.g) * 1.0F;
      this.V = true;
   }

   private void e() {
      rv var1 = this.r.f;
      if(var1 != null) {
         for(int var2 = 0; var2 < 256; ++var2) {
            float var3 = var1.b(1.0F) * 0.95F + 0.05F;
            float var4 = var1.y.f[var2 / 16] * var3;
            float var5 = var1.y.f[var2 % 16] * (this.e * 0.1F + 1.5F);
            if(var1.s > 0) {
               var4 = var1.y.f[var2 / 16];
            }

            float var6 = var4 * (var1.b(1.0F) * 0.65F + 0.35F);
            float var7 = var4 * (var1.b(1.0F) * 0.65F + 0.35F);
            float var10 = var5 * ((var5 * 0.6F + 0.4F) * 0.6F + 0.4F);
            float var11 = var5 * (var5 * var5 * 0.6F + 0.4F);
            float var12 = var6 + var5;
            float var13 = var7 + var10;
            float var14 = var4 + var11;
            var12 = var12 * 0.96F + 0.03F;
            var13 = var13 * 0.96F + 0.03F;
            var14 = var14 * 0.96F + 0.03F;
            float var15 = this.r.z.M;
            if(var12 > 1.0F) {
               var12 = 1.0F;
            }

            if(var13 > 1.0F) {
               var13 = 1.0F;
            }

            if(var14 > 1.0F) {
               var14 = 1.0F;
            }

            float var16 = 1.0F - var12;
            float var17 = 1.0F - var13;
            float var18 = 1.0F - var14;
            var16 = 1.0F - var16 * var16 * var16 * var16;
            var17 = 1.0F - var17 * var17 * var17 * var17;
            var18 = 1.0F - var18 * var18 * var18 * var18;
            var12 = var12 * (1.0F - var15) + var16 * var15;
            var13 = var13 * (1.0F - var15) + var17 * var15;
            var14 = var14 * (1.0F - var15) + var18 * var15;
            var12 = var12 * 0.96F + 0.03F;
            var13 = var13 * 0.96F + 0.03F;
            var14 = var14 * 0.96F + 0.03F;
            if(var12 > 1.0F) {
               var12 = 1.0F;
            }

            if(var13 > 1.0F) {
               var13 = 1.0F;
            }

            if(var14 > 1.0F) {
               var14 = 1.0F;
            }

            if(var12 < 0.0F) {
               var12 = 0.0F;
            }

            if(var13 < 0.0F) {
               var13 = 0.0F;
            }

            if(var14 < 0.0F) {
               var14 = 0.0F;
            }

            short var19 = 255;
            int var20 = (int)(var12 * 255.0F);
            int var21 = (int)(var13 * 255.0F);
            int var22 = (int)(var14 * 255.0F);
            this.L[var2] = var19 << 24 | var20 << 16 | var21 << 8 | var22;
         }

         this.r.p.a(this.L, 16, 16, this.d);
      }
   }

   public void b(float var1) {
      if(this.V) {
         this.e();
      }

      if(!Display.isActive()) {
         if(System.currentTimeMillis() - this.T > 500L) {
            this.r.i();
         }
      } else {
         this.T = System.currentTimeMillis();
      }

      if(this.r.O) {
         this.r.C.c();
         float var2 = this.r.z.c * 0.6F + 0.2F;
         float var3 = var2 * var2 * var2 * 8.0F;
         float var4 = (float)this.r.C.a * var3;
         float var5 = (float)this.r.C.b * var3;
         byte var6 = 1;
         if(this.r.z.d) {
            var6 = -1;
         }

         if(this.r.z.H) {
            var4 = this.v.a(var4, 0.05F * var3);
            var5 = this.w.a(var5, 0.05F * var3);
         }

         this.r.h.c(var4, var5 * (float)var6);
      }

      if(!this.r.w) {
         a = this.r.z.g;
         za var13 = new za(this.r.z, this.r.d, this.r.e);
         int var14 = var13.a();
         int var15 = var13.b();
         int var16 = Mouse.getX() * var14 / this.r.d;
         int var17 = var15 - Mouse.getY() * var15 / this.r.e - 1;
         short var7 = 200;
         if(this.r.z.i == 1) {
            var7 = 120;
         }

         if(this.r.z.i == 2) {
            var7 = 40;
         }

         long var8;
         if(this.r.f != null) {
            if(this.r.z.i == 0) {
               this.a(var1, 0L);
            } else {
               this.a(var1, this.U + (long)(1000000000 / var7));
            }

            if(this.r.z.i == 2) {
               var8 = (this.U + (long)(1000000000 / var7) - System.nanoTime()) / 1000000L;
               if(var8 > 0L && var8 < 500L) {
                  try {
                     Thread.sleep(var8);
                  } catch (InterruptedException var12) {
                     var12.printStackTrace();
                  }
               }
            }

            this.U = System.nanoTime();
            if(!this.r.z.C || this.r.r != null) {
               this.r.v.a(var1, this.r.r != null, var16, var17);
            }
         } else {
            GL11.glViewport(0, 0, this.r.d, this.r.e);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            this.b();
            if(this.r.z.i == 2) {
               var8 = (this.U + (long)(1000000000 / var7) - System.nanoTime()) / 1000000L;
               if(var8 < 0L) {
                  var8 += 10L;
               }

               if(var8 > 0L && var8 < 500L) {
                  try {
                     Thread.sleep(var8);
                  } catch (InterruptedException var11) {
                     var11.printStackTrace();
                  }
               }
            }

            this.U = System.nanoTime();
         }

         if(this.r.r != null) {
            GL11.glClear(256);
            this.r.r.a(var16, var17, var1);
            if(this.r.r != null && this.r.r.r != null) {
               this.r.r.r.a(var1);
            }
         }

      }
   }

   public void a(float var1, long var2) {
      GL11.glEnable(2884);
      GL11.glEnable(2929);
      if(this.r.i == null) {
         this.r.i = this.r.h;
      }

      this.a(var1);
      wd var4 = this.r.i;
      i var5 = this.r.g;
      bz var6 = this.r.j;
      double var7 = var4.N + (var4.o - var4.N) * (double)var1;
      double var9 = var4.O + (var4.p - var4.O) * (double)var1;
      double var11 = var4.P + (var4.q - var4.P) * (double)var1;
      bf var13 = this.r.f.w();
      int var16;
      if(var13 instanceof fx) {
         fx var14 = (fx)var13;
         int var15 = et.d((float)((int)var7)) >> 4;
         var16 = et.d((float)((int)var11)) >> 4;
         var14.d(var15, var16);
      }

      for(int var19 = 0; var19 < 2; ++var19) {
         if(this.r.z.g) {
            b = var19;
            if(b == 0) {
               GL11.glColorMask(false, true, true, false);
            } else {
               GL11.glColorMask(true, false, false, false);
            }
         }

         GL11.glViewport(0, 0, this.r.d, this.r.e);
         this.g(var1);
         GL11.glClear(16640);
         GL11.glEnable(2884);
         this.a(var1, var19);
         deobf.o.a();
         if(this.r.z.e < 2) {
            this.a(-1, var1);
            var5.a(var1);
         }

         GL11.glEnable(2912);
         this.a(1, var1);
         if(this.r.z.k) {
            GL11.glShadeModel(7425);
         }

         km var18 = new km();
         var18.a(var7, var9, var11);
         this.r.g.a(var18, var1);
         if(var19 == 0) {
            while(!this.r.g.a(var4, false) && var2 != 0L) {
               long var21 = var2 - System.nanoTime();
               if(var21 < 0L || var21 > 1000000000L) {
                  break;
               }
            }
         }

         this.a(0, var1);
         GL11.glEnable(2912);
         GL11.glBindTexture(3553, this.r.p.b("/terrain.png"));
         ow.a();
         var5.a(var4, 0, (double)var1);
         GL11.glShadeModel(7424);
         sz var20;
         if(this.q == 0) {
            ow.b();
            var5.a(var4.g(var1), var18, var1);
            this.b((double)var1);
            var6.b(var4, var1);
            ow.a();
            this.a(0, var1);
            var6.a(var4, var1);
            this.a((double)var1);
            if(this.r.y != null && var4.a(wa.g) && var4 instanceof sz) {
               var20 = (sz)var4;
               GL11.glDisable(3008);
               var5.a(var20, this.r.y, 0, var20.as.b(), var1);
               var5.b(var20, this.r.y, 0, var20.as.b(), var1);
               GL11.glEnable(3008);
            }
         }

         GL11.glDisable(3042);
         GL11.glEnable(2884);
         GL11.glBlendFunc(770, 771);
         GL11.glDepthMask(true);
         this.a(0, var1);
         GL11.glEnable(3042);
         GL11.glDisable(2884);
         GL11.glBindTexture(3553, this.r.p.b("/terrain.png"));
         if(this.r.z.j) {
            if(this.r.z.k) {
               GL11.glShadeModel(7425);
            }

            GL11.glColorMask(false, false, false, false);
            var16 = var5.a(var4, 1, (double)var1);
            if(this.r.z.g) {
               if(b == 0) {
                  GL11.glColorMask(false, true, true, true);
               } else {
                  GL11.glColorMask(true, false, false, true);
               }
            } else {
               GL11.glColorMask(true, true, true, true);
            }

            if(var16 > 0) {
               var5.a(1, (double)var1);
            }

            GL11.glShadeModel(7424);
         } else {
            var5.a(var4, 1, (double)var1);
         }

         GL11.glDepthMask(true);
         GL11.glEnable(2884);
         GL11.glDisable(3042);
         if(this.Q == 1.0D && var4 instanceof sz && this.r.y != null && !var4.a(wa.g)) {
            var20 = (sz)var4;
            GL11.glDisable(3008);
            var5.a(var20, this.r.y, 0, var20.as.b(), var1);
            var5.b(var20, this.r.y, 0, var20.as.b(), var1);
            GL11.glEnable(3008);
         }

         this.c(var1);
         GL11.glDisable(2912);
         if(this.u != null) {
            ;
         }

         GL11.glPushMatrix();
         this.a(0, var1);
         GL11.glEnable(2912);
         var5.b(var1);
         GL11.glDisable(2912);
         this.a(1, var1);
         GL11.glPopMatrix();
         if(this.Q == 1.0D) {
            GL11.glClear(256);
            this.b(var1, var19);
         }

         if(!this.r.z.g) {
            return;
         }
      }

      GL11.glColorMask(true, true, true, false);
   }

   private void f() {
      float var1 = this.r.f.i(1.0F);
      if(!this.r.z.j) {
         var1 /= 2.0F;
      }

      if(var1 != 0.0F) {
         this.W.setSeed((long)this.t * 312987231L);
         wd var2 = this.r.i;
         rv var3 = this.r.f;
         int var4 = et.b(var2.o);
         int var5 = et.b(var2.p);
         int var6 = et.b(var2.q);
         byte var7 = 10;
         double var8 = 0.0D;
         double var10 = 0.0D;
         double var12 = 0.0D;
         int var14 = 0;

         for(int var15 = 0; var15 < (int)(100.0F * var1 * var1); ++var15) {
            int var16 = var4 + this.W.nextInt(var7) - this.W.nextInt(var7);
            int var17 = var6 + this.W.nextInt(var7) - this.W.nextInt(var7);
            int var18 = var3.e(var16, var17);
            int var19 = var3.a(var16, var18 - 1, var17);
            if(var18 <= var5 + var7 && var18 >= var5 - var7 && var3.a().a(var16, var17).c()) {
               float var20 = this.W.nextFloat();
               float var21 = this.W.nextFloat();
               if(var19 > 0) {
                  if(lr.m[var19].bN == wa.h) {
                     this.r.j.a((nv)(new adf(var3, (double)((float)var16 + var20), (double)((float)var18 + 0.1F) - lr.m[var19].bG, (double)((float)var17 + var21), 0.0D, 0.0D, 0.0D)));
                  } else {
                     ++var14;
                     if(this.W.nextInt(var14) == 0) {
                        var8 = (double)((float)var16 + var20);
                        var10 = (double)((float)var18 + 0.1F) - lr.m[var19].bG;
                        var12 = (double)((float)var17 + var21);
                     }

                     this.r.j.a((nv)(new nl(var3, (double)((float)var16 + var20), (double)((float)var18 + 0.1F) - lr.m[var19].bG, (double)((float)var17 + var21))));
                  }
               }
            }
         }

         if(var14 > 0 && this.W.nextInt(3) < this.X++) {
            this.X = 0;
            if(var10 > var2.p + 1.0D && var3.e(et.b(var2.o), et.b(var2.q)) > et.b(var2.p)) {
               this.r.f.a(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F);
            } else {
               this.r.f.a(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F);
            }
         }

      }
   }

   protected void c(float var1) {
      float var2 = this.r.f.i(var1);
      if(var2 > 0.0F) {
         this.b((double)var1);
         if(this.i == null) {
            this.i = new float[1024];
            this.j = new float[1024];

            for(int var3 = 0; var3 < 32; ++var3) {
               for(int var4 = 0; var4 < 32; ++var4) {
                  float var5 = (float)(var4 - 16);
                  float var6 = (float)(var3 - 16);
                  float var7 = et.c(var5 * var5 + var6 * var6);
                  this.i[var3 << 5 | var4] = -var6 / var7;
                  this.j[var3 << 5 | var4] = var5 / var7;
               }
            }
         }

         wd var41 = this.r.i;
         rv var42 = this.r.f;
         int var43 = et.b(var41.o);
         int var44 = et.b(var41.p);
         int var45 = et.b(var41.q);
         xe var8 = xe.a;
         GL11.glDisable(2884);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glAlphaFunc(516, 0.01F);
         GL11.glBindTexture(3553, this.r.p.b("/environment/snow.png"));
         double var9 = var41.N + (var41.o - var41.N) * (double)var1;
         double var11 = var41.O + (var41.p - var41.O) * (double)var1;
         double var13 = var41.P + (var41.q - var41.P) * (double)var1;
         int var15 = et.b(var11);
         byte var16 = 5;
         if(this.r.z.j) {
            var16 = 10;
         }

         vh[] var17 = var42.a().a(var43 - var16, var45 - var16, var16 * 2 + 1, var16 * 2 + 1);
         boolean var18 = false;
         byte var19 = -1;
         float var20 = (float)this.t + var1;
         if(this.r.z.j) {
            var16 = 10;
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         int var46 = 0;

         for(int var21 = var43 - var16; var21 <= var43 + var16; ++var21) {
            for(int var22 = var45 - var16; var22 <= var45 + var16; ++var22) {
               int var23 = (var22 - var45 + 16) * 32 + var21 - var43 + 16;
               float var24 = this.i[var23] * 0.5F;
               float var25 = this.j[var23] * 0.5F;
               vh var26 = var17[var46++];
               if(var26.c() || var26.b()) {
                  int var27 = var42.e(var21, var22);
                  int var28 = var44 - var16;
                  int var29 = var44 + var16;
                  if(var28 < var27) {
                     var28 = var27;
                  }

                  if(var29 < var27) {
                     var29 = var27;
                  }

                  float var30 = 1.0F;
                  int var31 = var27;
                  if(var27 < var15) {
                     var31 = var15;
                  }

                  if(var28 != var29) {
                     this.W.setSeed((long)(var21 * var21 * 3121 + var21 * 45238971 ^ var22 * var22 * 418711 + var22 * 13761));
                     double var35;
                     float var32;
                     if(var26.c()) {
                        if(var19 != 0) {
                           if(var19 >= 0) {
                              var8.a();
                           }

                           var19 = 0;
                           GL11.glBindTexture(3553, this.r.p.b("/environment/rain.png"));
                           var8.b();
                        }

                        var32 = ((float)(this.t + var21 * var21 * 3121 + var21 * 45238971 + var22 * var22 * 418711 + var22 * 13761 & 31) + var1) / 32.0F * (3.0F + this.W.nextFloat());
                        double var33 = (double)((float)var21 + 0.5F) - var41.o;
                        var35 = (double)((float)var22 + 0.5F) - var41.q;
                        float var37 = et.a(var33 * var33 + var35 * var35) / (float)var16;
                        float var38 = 1.0F;
                        var8.b(var42.b(var21, var31, var22, 0));
                        var8.a(var38, var38, var38, ((1.0F - var37 * var37) * 0.5F + 0.5F) * var2);
                        var8.b(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                        var8.a((double)((float)var21 - var24) + 0.5D, (double)var28, (double)((float)var22 - var25) + 0.5D, (double)(0.0F * var30), (double)((float)var28 * var30 / 4.0F + var32 * var30));
                        var8.a((double)((float)var21 + var24) + 0.5D, (double)var28, (double)((float)var22 + var25) + 0.5D, (double)(1.0F * var30), (double)((float)var28 * var30 / 4.0F + var32 * var30));
                        var8.a((double)((float)var21 + var24) + 0.5D, (double)var29, (double)((float)var22 + var25) + 0.5D, (double)(1.0F * var30), (double)((float)var29 * var30 / 4.0F + var32 * var30));
                        var8.a((double)((float)var21 - var24) + 0.5D, (double)var29, (double)((float)var22 - var25) + 0.5D, (double)(0.0F * var30), (double)((float)var29 * var30 / 4.0F + var32 * var30));
                        var8.b(0.0D, 0.0D, 0.0D);
                     } else {
                        if(var19 != 1) {
                           if(var19 >= 0) {
                              var8.a();
                           }

                           var19 = 1;
                           GL11.glBindTexture(3553, this.r.p.b("/environment/snow.png"));
                           var8.b();
                        }

                        var32 = ((float)(this.t & 511) + var1) / 512.0F;
                        float var47 = this.W.nextFloat() + var20 * 0.01F * (float)this.W.nextGaussian();
                        float var34 = this.W.nextFloat() + var20 * (float)this.W.nextGaussian() * 0.0010F;
                        var35 = (double)((float)var21 + 0.5F) - var41.o;
                        double var48 = (double)((float)var22 + 0.5F) - var41.q;
                        float var39 = et.a(var35 * var35 + var48 * var48) / (float)var16;
                        float var40 = 1.0F;
                        var8.b((var42.b(var21, var31, var22, 0) * 3 + 15728880) / 4);
                        var8.a(var40, var40, var40, ((1.0F - var39 * var39) * 0.3F + 0.5F) * var2);
                        var8.b(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                        var8.a((double)((float)var21 - var24) + 0.5D, (double)var28, (double)((float)var22 - var25) + 0.5D, (double)(0.0F * var30 + var47), (double)((float)var28 * var30 / 4.0F + var32 * var30 + var34));
                        var8.a((double)((float)var21 + var24) + 0.5D, (double)var28, (double)((float)var22 + var25) + 0.5D, (double)(1.0F * var30 + var47), (double)((float)var28 * var30 / 4.0F + var32 * var30 + var34));
                        var8.a((double)((float)var21 + var24) + 0.5D, (double)var29, (double)((float)var22 + var25) + 0.5D, (double)(1.0F * var30 + var47), (double)((float)var29 * var30 / 4.0F + var32 * var30 + var34));
                        var8.a((double)((float)var21 - var24) + 0.5D, (double)var29, (double)((float)var22 - var25) + 0.5D, (double)(0.0F * var30 + var47), (double)((float)var29 * var30 / 4.0F + var32 * var30 + var34));
                        var8.b(0.0D, 0.0D, 0.0D);
                     }
                  }
               }
            }
         }

         if(var19 >= 0) {
            var8.a();
         }

         GL11.glEnable(2884);
         GL11.glDisable(3042);
         GL11.glAlphaFunc(516, 0.1F);
         this.a((double)var1);
      }
   }

   public void b() {
      za var1 = new za(this.r.z, this.r.d, this.r.e);
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, var1.a, var1.b, 0.0D, 1000.0D, 3000.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
   }

   private void g(float var1) {
      rv var2 = this.r.f;
      wd var3 = this.r.i;
      float var4 = 1.0F / (float)(4 - this.r.z.e);
      var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
      ax var5 = var2.a(this.r.i, var1);
      float var6 = (float)var5.a;
      float var7 = (float)var5.b;
      float var8 = (float)var5.c;
      ax var9 = var2.f(var1);
      this.n = (float)var9.a;
      this.o = (float)var9.b;
      this.p = (float)var9.c;
      float var11;
      if(this.r.z.e < 2) {
         ax var10 = et.a(var2.d(var1)) > 0.0F?ax.b(0.0D, 0.0D, 1.0D):ax.b(0.0D, 0.0D, -1.0D);
         var11 = (float)var3.h(var1).b(var10);
         if(var11 < 0.0F) {
            var11 = 0.0F;
         }

         if(var11 > 0.0F) {
            float[] var12 = var2.y.a(var2.c(var1), var1);
            if(var12 != null) {
               var11 *= var12[3];
               this.n = this.n * (1.0F - var11) + var12[0] * var11;
               this.o = this.o * (1.0F - var11) + var12[1] * var11;
               this.p = this.p * (1.0F - var11) + var12[2] * var11;
            }
         }
      }

      this.n += (var6 - this.n) * var4;
      this.o += (var7 - this.o) * var4;
      this.p += (var8 - this.p) * var4;
      float var18 = var2.i(var1);
      float var19;
      if(var18 > 0.0F) {
         var11 = 1.0F - var18 * 0.5F;
         var19 = 1.0F - var18 * 0.4F;
         this.n *= var11;
         this.o *= var11;
         this.p *= var19;
      }

      var11 = var2.h(var1);
      if(var11 > 0.0F) {
         var19 = 1.0F - var11 * 0.5F;
         this.n *= var19;
         this.o *= var19;
         this.p *= var19;
      }

      if(this.P) {
         ax var20 = var2.e(var1);
         this.n = (float)var20.a;
         this.o = (float)var20.b;
         this.p = (float)var20.c;
      } else if(var3.a(wa.g)) {
         this.n = 0.02F;
         this.o = 0.02F;
         this.p = 0.2F;
      } else if(var3.a(wa.h)) {
         this.n = 0.6F;
         this.o = 0.1F;
         this.p = 0.0F;
      }

      var19 = this.Y + (this.Z - this.Y) * var1;
      this.n *= var19;
      this.o *= var19;
      this.p *= var19;
      double var13 = 1.0d;//(var3.O + (var3.p - var3.O) * (double)var1) / 32.0D;
      if(var13 < 1.0D) {
         if(var13 < 0.0D) {
            var13 = 0.0D;
         }

         var13 *= var13;
         this.n = (float)((double)this.n * var13);
         this.o = (float)((double)this.o * var13);
         this.p = (float)((double)this.p * var13);
      }

      if(this.r.z.g) {
         float var15 = (this.n * 30.0F + this.o * 59.0F + this.p * 11.0F) / 100.0F;
         float var16 = (this.n * 30.0F + this.o * 70.0F) / 100.0F;
         float var17 = (this.n * 30.0F + this.p * 70.0F) / 100.0F;
         this.n = var15;
         this.o = var16;
         this.p = var17;
      }

      GL11.glClearColor(this.n, this.o, this.p, 0.0F);
   }

   private void a(int var1, float var2) {
      wd var3 = this.r.i;
      if(var1 == 999) {
         GL11.glFog(2918, this.a(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glFogi(2917, 9729);
         GL11.glFogf(2915, 0.0F);
         GL11.glFogf(2916, 8.0F);
         if(GLContext.getCapabilities().GL_NV_fog_distance) {
            GL11.glFogi('\u855a', '\u855b');
         }

         GL11.glFogf(2915, 0.0F);
      } else {
         GL11.glFog(2918, this.a(this.n, this.o, this.p, 1.0F));
         GL11.glNormal3f(0.0F, -1.0F, 0.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         float var4;
         float var5;
         float var6;
         float var7;
         float var8;
         float var9;
         if(this.P) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1F);
            var4 = 1.0F;
            var5 = 1.0F;
            var6 = 1.0F;
            if(this.r.z.g) {
               var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
               var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
               var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
         } else if(var3.a(wa.g)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1F);
            var4 = 0.4F;
            var5 = 0.4F;
            var6 = 0.9F;
            if(this.r.z.g) {
               var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
               var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
               var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
         } else if(var3.a(wa.h)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 2.0F);
            var4 = 0.4F;
            var5 = 0.3F;
            var6 = 0.3F;
            if(this.r.z.g) {
               var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
               var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
               var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
         } else {
            var4 = this.s;
            double var10 = 1.0d; //(double)((var3.a(var2) & 15728640) >> 20) / 16.0D + (var3.O + (var3.p - var3.O) * (double)var2 + 4.0D) / 32.0D;
            if(var10 < 1.0D) {
               if(var10 < 0.0D) {
                  var10 = 0.0D;
               }

               var10 *= var10;
               var7 = 100.0F * (float)var10;
               if(var7 < 5.0F) {
                  var7 = 5.0F;
               }

               if(var4 > var7) {
                  var4 = var7;
               }
            }

            GL11.glFogi(2917, 9729);
            GL11.glFogf(2915, var4 * 0.25F);
            GL11.glFogf(2916, var4);
            if(var1 < 0) {
               GL11.glFogf(2915, 0.0F);
               GL11.glFogf(2916, var4 * 0.8F);
            }

            if(GLContext.getCapabilities().GL_NV_fog_distance) {
               GL11.glFogi('\u855a', '\u855b');
            }

            if(this.r.f.y.c) {
               GL11.glFogf(2915, 0.0F);
            }
         }

         GL11.glEnable(2903);
         GL11.glColorMaterial(1028, 4608);
      }
   }

   private FloatBuffer a(float var1, float var2, float var3, float var4) {
      this.m.clear();
      this.m.put(var1).put(var2).put(var3).put(var4);
      this.m.flip();
      return this.m;
   }

}
