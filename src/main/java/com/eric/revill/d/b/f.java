package com.eric.revill.d.b;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class f {
   private static final int a = 280;
   private static final int b = 163;
   private static final int c = 98;
   private static final int d = 164;
   private boolean e;
   private boolean f;
   private boolean g;
   private boolean h;
   private boolean i;
   private boolean j;
   private String k = "roja";
   private String l = "";
   private boolean m;
   private boolean n;
   private boolean o;
   private String p = "";
   private String q = "naranja";
   private String r = "";
   private String s = "";
   private boolean t;
   private int u;
   private int v;
   private int w;
   private int x;
   private int y;

   public void a(String color, String result) {
      this.e = true;
      this.k = color != null ? color.toLowerCase() : "roja";
      this.l = result;
      this.u = 0;
   }

   public void a(boolean active, int remainingSeconds) {
      this.f = active;
      this.x = Math.max(0, remainingSeconds);
   }

   public void a() {
      this.e = false;
   }

   public void b() {
      this.i = true;
      this.j = true;
      this.w = 0;
      com.eric.revill.d.c.a.c();
   }

   public void a(String message) {
      this.b(message, "naranja");
   }

   public void b(String message, String color) {
      this.p = message == null ? "" : message;
      this.q = color == null ? "naranja" : color.toLowerCase();
      this.n = !this.p.isBlank();
   }

   public void a(Minecraft client) {
      boolean keyDown = client != null && client.screen == null && InputConstants.isKeyDown(client.getWindow().getWindow(), 79);
      if (keyDown && !this.o && this.n) {
         this.n = false;
      }

      this.o = keyDown;
   }

   public void c() {
      this.g = true;
      this.h = false;
      this.v = 0;
   }

   public void d() {
      this.e = false;
      this.g = true;
      this.h = true;
      this.v = 0;
      com.eric.revill.d.c.a.e();
   }

   public void a(String playerName, String reason, boolean self) {
      this.m = true;
      this.r = playerName == null ? "Jugador" : playerName;
      this.s = reason != null && !reason.isBlank() ? reason : "muerte";
      this.t = self;
      this.y = 0;
   }

   public void e() {
      if (this.e) {
         this.u++;
         if (this.u >= 280) {
            this.e = false;
         }
      }

      if (this.g) {
         this.v++;
         if (this.v >= 163 && (!this.h || !com.eric.revill.d.c.a.f())) {
            this.g = false;
            if (this.h) {
               this.h = false;
               com.eric.revill.d.c.a.a();
               ClientPlayNetworking.send(com.eric.revill.p.a.i, PacketByteBufs.empty());
            }
         }
      }

      if (this.i && ++this.w >= 164) {
         this.i = false;
      }

      if (this.j && this.w > 1 && !com.eric.revill.d.c.a.d()) {
         this.j = false;
         ClientPlayNetworking.send(com.eric.revill.p.a.g, PacketByteBufs.empty());
      }

      if (this.m && ++this.y > 80) {
         this.m = false;
      }
   }

   public void a(GuiGraphics context) {
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.font != null) {
         if (this.f) {
            String timer = String.format("%02d:%02d", this.x / 60, this.x % 60);
            int x = (context.guiWidth() - client.font.width(timer)) / 2;
            context.drawString(client.font, Component.literal(timer), x, 8, -43691);
         }

         if (this.n) {
            this.a(context, client);
         }

         int centerX = context.guiWidth() / 2;
         int centerY = context.guiHeight() / 2;
         if (this.e) {
            int frameCount = this.b(this.k);
            int frame2 = Math.max(1, Math.min(frameCount, 1 + this.u * frameCount / 280));
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/roulette/" + this.k + "/" + this.k + "-" + frame2 + ".png");
            int scale = 175;
            int x = centerX - 87;
            int y = centerY - 87 - 18;
            context.blit(texture, x, y, 0.0F, 0.0F, 175, 175, 175, 175);
         }

         if (this.i) {
            int frame = Math.min(98, 1 + this.w * 98 / 164);
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/nutria/nutria-" + frame + ".png");
            int width = context.guiWidth();
            int height = context.guiHeight();
            context.blit(texture, 0, 0, 0.0F, 0.0F, width, height, width, height);
         }

         if (this.g) {
            int frame = Math.max(1, Math.min(163, this.v));
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/death/muerte-" + frame + ".png");
            context.blit(texture, centerX - 128, centerY - 128, 0.0F, 0.0F, 256, 256, 256, 256);
         }

         if (this.m) {
            String message = this.r + " murió por " + this.s;
            int textColor = -48060;
            int y = 12;
            context.drawCenteredString(client.font, Component.literal(message), centerX, y, textColor);
         }
      }
   }

   private void a(GuiGraphics context, Minecraft client) {
      int left = 8;
      int top = 8;
      int maxWidth = Math.min(320, context.guiWidth() - 24);
      List<String> lines = this.a(this.p, client, maxWidth - 16);
      int cardWidth = 16;

      for (String line : lines) {
         cardWidth = Math.max(cardWidth, client.font.width(line) + 16);
      }

      int cardHeight = lines.size() * 10 + 12;
      int textColor = this.c(this.q);
      context.fill(left, top, left + cardWidth, top + cardHeight, Integer.MIN_VALUE);

      for (int index = 0; index < lines.size(); index++) {
         context.drawString(client.font, Component.literal(lines.get(index)), left + 8, top + 6 + index * 10, textColor);
      }

      String hideText = "Ocultar tarjeta O";
      int hideWidth = client.font.width(hideText);
      context.drawString(client.font, Component.literal(hideText), left + (cardWidth - hideWidth) / 2, top + cardHeight + 3, textColor);
   }

   private List<String> a(String message, Minecraft client, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();

      for (String paragraph : message.split("\\n", -1)) {
         if (paragraph.isEmpty()) {
            lines.add("");
         } else {
            StringBuilder line = new StringBuilder();

            for (String word : paragraph.split("\\s+")) {
               String candidate = line.length() == 0 ? word : line + " " + word;
               if (client.font.width(candidate) > maxWidth && line.length() > 0) {
                  lines.add(line.toString());
                  line = new StringBuilder(word);
               } else {
                  line = new StringBuilder(candidate);
               }
            }

            if (line.length() > 0) {
               lines.add(line.toString());
            }
         }
      }

      return lines;
   }

   private int b(String color) {
      return switch (color) {
         case "naranja" -> 413;
         case "roja" -> 418;
         case "verde" -> 415;
         case "rosada" -> 389;
         default -> 413;
      };
   }

   private int c(String color) {
      String var2 = color == null ? "naranja" : color.toLowerCase();

      return switch (var2) {
         case "roja" -> -43691;
         case "verde" -> -11141291;
         case "rosada" -> -34868;
         case "naranja" -> -22016;
         default -> -10662;
      };
   }
}
