package com.eric.revill.d.e;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@Environment(EnvType.CLIENT)
public final class p extends Screen {
   private static final float a = 0.38F;
   private static final int b = 58;
   private static final int c = 10;
   private static final int d = 46;
   private static final int e = -16051942;
   private static final int f = -770894820;
   private static final int g = -9207928;
   private static final int h = -14076867;
   private static final int i = -14998484;
   private static final int j = -10163;
   private static final int k = -10098433;
   private static final int l = -19891;
   private static final int m = -4602416;
   private static final int n = -10098551;
   private static final int o = -13616573;
   private static final int p = -22976;
   private final Screen q;
   private final List<JsonObject> r = new ArrayList<>();
   private final List<JsonObject> s = new ArrayList<>();
   private String t = "";
   private UUID u;
   private int v;
   private int w;
   private String x = "";
   private EditBox y;
   private String z = "";
   private LivingEntity A;
   private ScreenMode B = ScreenMode.a;
   private boolean C;
   private String D = "";
   private EditBox E;
   private EditBox F;
   private EditBox G;
   private EditBox H;
   private EditBox I;
   private EditBox J;
   private EditBox K;
   private EditBox L;
   private EditBox M;
   private EditBox N;
   private Button O;
   private Button P;
   private String Q = "KILL";
   private boolean R;

   public p(Screen parent) {
      super(Component.literal("Misiones"));
      this.q = parent;
      this.a();
      com.eric.revill.d.a.c.c();
   }

   public void a() {
      JsonObject data = com.eric.revill.d.a.c.b();
      this.r.clear();
      this.s.clear();
      JsonArray ma = data.getAsJsonArray("missions");
      if (ma != null) {
         for (int i2 = 0; i2 < ma.size(); i2++) {
            if (ma.get(i2).isJsonObject()) {
               this.r.add(ma.get(i2).getAsJsonObject());
            }
         }
      }

      JsonArray pa;
      if ((pa = data.getAsJsonArray("players")) != null) {
         for (int i3 = 0; i3 < pa.size(); i3++) {
            if (pa.get(i3).isJsonObject()) {
               this.s.add(pa.get(i3).getAsJsonObject());
            }
         }
      }

      if (this.r.stream().noneMatch(m2 -> this.t.equals(m2.get("id").getAsString()))) {
         String var6 = this.t = this.r.isEmpty() ? "" : this.r.get(0).get("id").getAsString();
      }

      if (this.minecraft != null && this.minecraft.screen == this && this.B != ScreenMode.b) {
         this.init();
      }
   }

   protected void init() {
      this.clearWidgets();
      JsonObject data = com.eric.revill.d.a.c.b();
      boolean operator = data.has("operator") && data.get("operator").getAsBoolean();
      if (operator) {
         this.addRenderableWidget(Button.builder(Component.literal("Misiones"), b2 -> {
            this.B = ScreenMode.a;
            this.C = false;
            this.init();
         }).bounds(this.width / 2 - 150, 10, 100, 20).build());
         this.addRenderableWidget(Button.builder(Component.literal("Jugadores"), b2 -> {
            this.B = ScreenMode.c;
            this.init();
         }).bounds(this.width / 2 - 46, 10, 100, 20).build());
         this.addRenderableWidget(Button.builder(Component.literal("Crear"), b2 -> this.d()).bounds(this.width / 2 + 58, 10, 78, 20).build());
      }

      this.addRenderableWidget(new f(this.width - 88, 10, 76, 20, Component.literal("Volver"), b2 -> this.minecraft.setScreen(this.q)));
      if (!this.D.isEmpty()) {
         this.addRenderableWidget(
            Button.builder(Component.literal("Confirmar"), b2 -> this.g()).bounds(this.width / 2 - 86, this.height / 2 + 18, 82, 20).build()
         );
         this.addRenderableWidget(Button.builder(Component.literal("Cancelar"), b2 -> {
            this.D = "";
            this.init();
         }).bounds(this.width / 2 + 4, this.height / 2 + 18, 82, 20).build());
      } else {
         if (this.B == ScreenMode.b) {
            this.b();
         } else if (this.B == ScreenMode.c) {
            this.c();
         } else {
            this.a(operator);
         }
      }
   }

   private void a(boolean operator) {
      int leftW = this.h();
      int searchX = 14;
      int searchW = leftW - 8;
      this.y = new EditBox(this.font, searchX, 48, searchW - 4, 18, Component.empty());
      this.y.setMaxLength(80);
      this.y.setHint(Component.literal("Buscar mision..."));
      this.y.setValue(this.x);
      this.y.setResponder(v -> {
         this.x = v;
         this.w = 0;
         List<JsonObject> f2 = this.j();
         this.t = f2.isEmpty() ? "" : f2.get(0).get("id").getAsString();
      });
      this.addRenderableWidget(this.y);
      if (operator && this.k() != null) {
         this.addRenderableWidget(Button.builder(Component.literal("Editar"), b2 -> this.e()).bounds(this.width - 166, this.height - 28, 65, 20).build());
         this.addRenderableWidget(Button.builder(Component.literal("Eliminar"), b2 -> {
            this.D = "delete";
            this.init();
         }).bounds(this.width - 94, this.height - 28, 72, 20).build());
      }

      List<JsonObject> shown = this.j();
      int vis = this.i();
      this.w = Math.min(this.w, Math.max(0, shown.size() - vis));
   }

   private void b() {
      int left = this.width / 2 - 210;
      int cw = this.width - 40;
      int fw = (cw - 10) / 2;
      int colW = (cw - 10) / 3;
      this.E = this.a("ID", left, 50, fw);
      this.F = this.a("Titulo", left + fw + 10, 50, fw);
      this.G = this.a("Descripcion", left, 80, cw);
      this.O = Button.builder(Component.literal("Tipo: " + this.Q), b2 -> {
         String var2x = this.Q;

         this.Q = switch (var2x) {
            case "KILL" -> "COLLECT";
            case "COLLECT" -> "BREAK";
            default -> "KILL";
         };
         this.O.setMessage(Component.literal("Tipo: " + this.Q));
      }).bounds(left, 108, colW, 20).build();
      this.addRenderableWidget(this.O);
      this.H = this.a("Objetivo (ID)", left + colW + 5, 108, colW);
      this.L = this.a("Icono opcional", left + (colW + 5) * 2, 108, colW);
      this.I = this.a("Cantidad", left, 136, fw);
      this.J = this.a("Recompensa deditas", left + fw + 10, 136, fw);
      this.K = this.a("Dia de mision", left, 164, fw);
      this.P = Button.builder(Component.literal("Esencial: " + (this.R ? "si" : "no")), b2 -> {
         this.R = !this.R;
         this.P.setMessage(Component.literal("Esencial: " + (this.R ? "si" : "no")));
      }).bounds(left + fw + 10, 164, fw, 20).build();
      this.addRenderableWidget(this.P);
      this.M = this.a("Objeto recompensa", left, 192, fw);
      this.N = this.a("Cantidad objeto", left + fw + 10, 192, fw);
      this.addRenderableWidget(Button.builder(Component.literal("Guardar"), b2 -> this.f()).bounds(this.width / 2 - 84, this.height - 24, 80, 20).build());
      this.addRenderableWidget(Button.builder(Component.literal("Cancelar"), b2 -> {
         this.B = ScreenMode.a;
         this.init();
      }).bounds(this.width / 2 + 4, this.height - 24, 80, 20).build());
   }

   private EditBox a(String lbl, int x, int y, int w) {
      EditBox f2 = new EditBox(this.font, x, y, w, 17, Component.literal(lbl));
      f2.setMaxLength(128);
      f2.setHint(Component.literal(lbl));
      this.addRenderableWidget(f2);
      return f2;
   }

   private void c() {
      int vis = Math.min(6, Math.max(0, this.s.size() - this.v * 6));
      int left = Math.max(10, this.width / 2 - 222);

      for (int row = 0; row < vis; row++) {
         JsonObject pl = this.s.get(this.v * 6 + row);
         String uuid = pl.get("uuid").getAsString();
         String st = pl.get("online").getAsBoolean() ? " *" : " o";
         this.addRenderableWidget(Button.builder(Component.literal(this.b(pl.get("name").getAsString(), 20) + st), b2 -> {
            this.u = UUID.fromString(uuid);
            this.init();
         }).bounds(left, 54 + row * 23, 176, 20).build());
      }

      if (this.v > 0) {
         this.addRenderableWidget(Button.builder(Component.literal("Anterior"), b2 -> {
            this.v--;
            this.init();
         }).bounds(10, this.height - 28, 82, 20).build());
      }

      if ((this.v + 1) * 6 < this.s.size()) {
         this.addRenderableWidget(Button.builder(Component.literal("Siguiente"), b2 -> {
            this.v++;
            this.init();
         }).bounds(100, this.height - 28, 86, 20).build());
      }

      if (this.u != null) {
         this.addRenderableWidget(Button.builder(Component.literal("Reiniciar todo"), b2 -> {
            this.D = "reset-player";
            this.init();
         }).bounds(this.width / 2 + 42, this.height - 28, 95, 20).build());
         this.addRenderableWidget(Button.builder(Component.literal("Reiniciar mision"), b2 -> {
            if (!this.t.isBlank()) {
               this.D = "reset-mission";
               this.init();
            }
         }).bounds(this.width / 2 + 143, this.height - 28, 108, 20).build());
      }
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.fill(0, 0, this.width, this.height, -16051942);
      if (this.B == ScreenMode.a && this.D.isEmpty()) {
         this.a(context);
      }

      super.render(context, mouseX, mouseY, delta);
      if (!this.D.isEmpty()) {
         context.fill(this.width / 2 - 155, this.height / 2 - 28, this.width / 2 + 155, this.height / 2 + 16, -266326986);
         context.renderOutline(this.width / 2 - 155, this.height / 2 - 28, 310, 44, -10098433);
         String var6 = this.D;

         String pr = switch (var6) {
            case "delete" -> "Eliminar esta mision?";
            case "reset-player" -> "Reiniciar todo el progreso del jugador?";
            default -> "Reiniciar el progreso de esta mision?";
         };
         context.drawCenteredString(this.font, Component.literal(pr), this.width / 2, this.height / 2 - 12, -1);
      } else {
         if (this.B == ScreenMode.b) {
            this.c(context);
         } else if (this.B == ScreenMode.c) {
            this.d(context);
         } else {
            context.drawCenteredString(this.font, Component.literal("MISIONES"), this.width / 2, 36, -10098433);
            this.a(context, mouseX, mouseY);
            this.b(context);
         }
      }
   }

   private void a(GuiGraphics ctx) {
      int lw = this.h();
      int lx = 10;
      int rx = lx + lw + 10;
      int rw = this.width - rx - 10;
      int bh = this.height - 46 - 10;
      ctx.fill(lx, 46, lx + lw, 46 + bh, -770894820);
      ctx.renderOutline(lx, 46, lw, bh, -9207928);
      ctx.fill(lx, 46, lx + lw, 49, -10163);
      ctx.fill(rx, 46, rx + rw, 46 + bh, -770894820);
      ctx.renderOutline(rx, 46, rw, bh, -9207928);
      ctx.fill(rx, 46, rx + rw, 49, -10163);
      ctx.fill(lx + 4, 68, lx + lw - 4, 46 + bh - 4, -15656928);
   }

   private void a(GuiGraphics ctx, int mouseX, int mouseY) {
      int lw = this.h();
      int cl = 16;
      int cr = 10 + lw - 6;
      int cw = cr - cl;
      int ct = 70;
      int cb = this.height - 10;
      int vis = this.i();
      List<JsonObject> shown = this.j();
      this.w = Math.min(this.w, Math.max(0, shown.size() - vis));
      ctx.enableScissor(cl, ct, cr, cb);

      for (int row = 0; row < vis && this.w + row < shown.size(); row++) {
         this.a(ctx, shown.get(this.w + row), cl, ct + row * 58, cw);
      }

      ctx.disableScissor();
      if (shown.isEmpty()) {
         ctx.drawCenteredString(this.font, Component.literal("No hay misiones."), cl + cw / 2, ct + 20, -4602416);
      }

      int total;
      if ((total = shown.size()) > vis) {
         int tx = cr - 3;
         ctx.fill(tx, ct, tx + 2, cb, -11576475);
         int th = Math.max(20, (cb - ct) * vis / total);
         int ty = ct + (cb - ct - th) * this.w / Math.max(1, total - vis);
         ctx.fill(tx - 1, ty, tx + 4, ty + th, -10163);
      }
   }

   private void a(GuiGraphics ctx, JsonObject m2, int x, int y, int cw) {
      boolean done = m2.has("completed") && m2.get("completed").getAsBoolean();
      boolean sel = m2.get("id").getAsString().equals(this.t);
      boolean ess = m2.has("essential") && m2.get("essential").getAsBoolean();
      ctx.fill(x, y, x + cw, y + 58 - 3, sel ? -14076867 : -14998484);
      ctx.renderOutline(x, y, cw, 55, ess ? -10163 : -9207928);
      ctx.fill(x + 5, y + 7, x + 11, y + 13, done ? -10098551 : -19386);
      this.a(ctx, m2, x + cw - 26, y + 18);
      ctx.drawString(this.font, Component.literal(this.b(m2.get("title").getAsString(), 26)), x + 16, y + 5, ess ? -10163 : -19891);
      String reward = "+" + m2.get("reward").getAsInt();
      ctx.drawString(this.font, Component.literal(reward), x + 16, y + 17, -10098433, false);
      ctx.renderItem(new ItemStack((ItemLike)com.eric.revill.l.g.c.get()), x + 19 + this.font.width(reward), y + 13);
      ctx.drawString(this.font, Component.literal(this.b(m2.get("description").getAsString(), 38)), x + 16, y + 29, -4602416, false);
      int req = Math.max(1, m2.get("required").getAsInt());
      int prog = Math.min(req, m2.has("progress") ? m2.get("progress").getAsInt() : 0);
      int bx = x + 16;
      int by = y + 43;
      int bw = cw - 50;
      ctx.fill(bx, by, bx + bw, by + 4, -13616573);
      ctx.fill(bx, by, bx + bw * prog / req, by + 4, done ? -10098551 : -22976);
      ctx.drawString(this.font, Component.literal(prog + "/" + req), bx + bw + 4, by - 2, -1840916, false);
   }

   private void b(GuiGraphics ctx) {
      int lw = this.h();
      int rx = 10 + lw + 10;
      int rw = this.width - rx - 10;
      int ph = this.height - 46 - 10;
      ctx.enableScissor(rx + 2, 50, rx + rw - 2, 46 + ph - 2);
      JsonObject m2 = this.k();
      if (m2 == null) {
         ctx.drawCenteredString(
            this.font,
            Component.literal(com.eric.revill.d.a.c.b().has("missions") ? "No hay misiones configuradas." : "Cargando..."),
            rx + rw / 2,
            74,
            -4602416
         );
         ctx.disableScissor();
      } else {
         int y = 60;
         if (m2.get("essential").getAsBoolean()) {
            ctx.drawString(this.font, Component.literal("** MISION ESENCIAL **"), rx + 14, y, -10163);
            y += 14;
         }

         String type = m2.get("type").getAsString().toUpperCase(Locale.ROOT);
         String hdr = this.a(type) + "  |  " + m2.get("title").getAsString();
         String rew = "+" + m2.get("reward").getAsInt();
         ctx.drawString(this.font, Component.literal(hdr), rx + 14, y, -19891);
         int rewardX = rx + rw - 34 - this.font.width(rew);
         ctx.drawString(this.font, Component.literal(rew), rewardX, y, -10098433, false);
         ctx.renderItem(new ItemStack((ItemLike)com.eric.revill.l.g.c.get()), rx + rw - 30, y - 4);
         int var10001 = rx + 8;
         y += 18;
         ctx.fill(var10001, y, rx + rw - 8, y + 1, -9207928);
         int ps = Math.min(100, Math.max(60, rw / 4));
         int pl = rx + (rw - ps) / 2;
         y += 6;
         ctx.fill(pl - 6, y - 4, pl + ps + 6, y + ps + 4, -15459035);
         ctx.renderOutline(pl - 6, y - 4, ps + 12, ps + 8, -10917773);
         String tid = m2.get("target").getAsString();
         if (!this.b(ctx, m2, pl + ps / 2, y + ps, ps / 2)) {
            String pid = m2.has("icon") ? m2.get("icon").getAsString() : (type.equals("COLLECT") ? tid : null);
            if (pid != null) {
               this.a(ctx, pid, pl + ps / 2 - 8, y + ps / 2 - 8);
            } else {
               ctx.drawCenteredString(this.font, Component.literal(this.a(type)), pl + ps / 2, y + ps / 2 - 4, -19891);
            }
         }

         y = y + ps + 14;
         ctx.drawString(this.font, Component.literal("OBJETIVO: " + tid), rx + 14, y, -10098433);
         y += 14;

         for (String line : this.a(m2.get("description").getAsString(), rw - 28)) {
            ctx.drawString(this.font, Component.literal(line), rx + 14, y, -3155744);
            y += 11;
         }

         int req = m2.get("required").getAsInt();
         int prog = Math.min(req, m2.has("progress") ? m2.get("progress").getAsInt() : 0);
         boolean done = m2.has("completed") && m2.get("completed").getAsBoolean();
         Font var33 = this.font;
         MutableComponent var10002 = Component.literal(done ? "COMPLETADA" : "PROGRESO");
         int var10003 = rx + 14;
         y += 6;
         ctx.drawString(var33, var10002, var10003, y, done ? -10098551 : -1840916);
         ctx.drawString(this.font, Component.literal(prog + "/" + req), rx + rw - 50, y, -1, false);
         int bx = rx + 14;
         int bw = rw - 28;
         y += 12;
         ctx.fill(bx, y, bx + bw, y + 10, -13616573);
         ctx.fill(bx, y, bx + bw * prog / req, y + 10, done ? -10098551 : -22976);
         ctx.renderOutline(bx, y, bw, 10, -11906464);
         Font var34 = this.font;
         var10002 = Component.literal("Recompensa: " + m2.get("reward").getAsInt() + " deditas");
         var10003 = rx + 14;
         y += 18;
         ctx.drawString(var34, var10002, var10003, y, -10098433);
         y += 13;
         if (m2.get("essential").getAsBoolean()) {
            ctx.drawString(this.font, Component.literal("Dedita rosa - dia " + m2.get("day").getAsInt()), rx + 14, y, -24598);
            y += 13;
         }

         if (m2.has("rewardItem")) {
            String rid = m2.get("rewardItem").getAsString();
            ctx.drawString(this.font, Component.literal("Objeto: " + rid + " x" + m2.get("rewardItemCount").getAsInt()), rx + 14, y, -1);
            this.a(ctx, rid, rx + rw - 30, y - 6);
         }

         ctx.disableScissor();
      }
   }

   private void c(GuiGraphics ctx) {
      int left = this.width / 2 - 210;
      int cw = this.width - 40;
      int fw = (cw - 10) / 2;
      int colW = (cw - 10) / 3;
      ctx.drawCenteredString(this.font, Component.literal(this.C ? "CREAR MISION" : "EDITAR MISION"), this.width / 2, 34, -10163);
      ctx.drawString(this.font, Component.literal("ID"), left, 40, -4602416);
      ctx.drawString(this.font, Component.literal("Titulo"), left + fw + 10, 40, -4602416);
      ctx.drawString(this.font, Component.literal("Descripcion"), left, 70, -4602416);
      ctx.drawString(this.font, Component.literal("Objetivo (ID)"), left + colW + 5, 98, -4602416);
      ctx.drawString(this.font, Component.literal("Icono (opcional)"), left + (colW + 5) * 2, 98, -4602416);
      ctx.drawString(this.font, Component.literal("Cantidad"), left, 126, -4602416);
      ctx.drawString(this.font, Component.literal("Recompensa deditas"), left + fw + 10, 126, -4602416);
      ctx.drawString(this.font, Component.literal("Dia"), left, 154, -4602416);
      ctx.drawString(this.font, Component.literal("Obj. recompensa"), left, 182, -4602416);
      ctx.drawString(this.font, Component.literal("Cantidad objeto"), left + fw + 10, 182, -4602416);
   }

   private void d(GuiGraphics ctx) {
      JsonObject sel = this.l();
      int left = this.width / 2 - 25;
      int pw = Math.min(240, this.width / 2 + 5);
      ctx.fill(left, 52, left + pw, this.height - 38, -921164239);
      ctx.renderOutline(left, 52, pw, this.height - 90, -12800062);
      if (sel == null) {
         ctx.drawCenteredString(this.font, Component.literal("Selecciona un jugador."), left + pw / 2, 68, -4602416);
      } else {
         int y = 63;
         ctx.drawString(this.font, Component.literal(sel.get("name").getAsString()), left + 10, y, -1);
         Font var10001 = this.font;
         MutableComponent var10002 = Component.literal(sel.get("online").getAsBoolean() ? "Conectado" : "Desconectado");
         int var10003 = left + 10;
         y += 14;
         ctx.drawString(var10001, var10002, var10003, y, sel.get("online").getAsBoolean() ? -10098551 : -4602416);
         JsonObject sm = this.k();
         var10001 = this.font;
         var10002 = Component.literal("Mision: " + (sm == null ? "ninguna" : this.b(sm.get("title").getAsString(), 22)));
         var10003 = left + 10;
         y += 18;
         ctx.drawString(var10001, var10002, var10003, y, -10163);
         var10001 = this.font;
         var10002 = Component.literal("Misiones completadas:");
         var10003 = left + 10;
         y += 18;
         ctx.drawString(var10001, var10002, var10003, y, -10098433);
         y += 12;
         JsonArray comp = sel.getAsJsonArray("completed");
         if (comp != null && !comp.isEmpty()) {
            for (int i2 = 0; i2 < comp.size() && y < this.height - 56; i2++) {
               ctx.drawString(this.font, Component.literal("- " + comp.get(i2).getAsString()), left + 10, y, -3155744);
               y += 11;
            }
         } else {
            ctx.drawString(this.font, Component.literal("Ninguna"), left + 10, y, -4602416);
         }
      }
   }

   private void d() {
      this.B = ScreenMode.b;
      this.C = true;
      this.Q = "KILL";
      this.R = false;
      this.init();
      this.E.setValue("mission_" + System.currentTimeMillis());
      this.F.setValue("");
      this.G.setValue("");
      this.H.setValue("minecraft:zombie");
      this.L.setValue("");
      this.I.setValue("1");
      this.J.setValue("1");
      this.K.setValue("1");
      this.M.setValue("");
      this.N.setValue("0");
   }

   private void e() {
      JsonObject m2 = this.k();
      if (m2 != null) {
         this.B = ScreenMode.b;
         this.C = false;
         this.Q = m2.get("type").getAsString();
         this.R = m2.get("essential").getAsBoolean();
         this.init();
         this.E.setValue(m2.get("id").getAsString());
         this.F.setValue(m2.get("title").getAsString());
         this.G.setValue(m2.get("description").getAsString());
         this.H.setValue(m2.get("target").getAsString());
         this.L.setValue(m2.has("icon") ? m2.get("icon").getAsString() : "");
         this.I.setValue(m2.get("required").getAsString());
         this.J.setValue(m2.get("reward").getAsString());
         this.K.setValue(m2.get("day").getAsString());
         this.M.setValue(m2.has("rewardItem") ? m2.get("rewardItem").getAsString() : "");
         this.N.setValue(m2.has("rewardItemCount") ? m2.get("rewardItemCount").getAsString() : "0");
      }
   }

   private void f() {
      try {
         JsonObject m2 = new JsonObject();
         m2.addProperty("id", this.E.getValue().trim());
         m2.addProperty("title", this.F.getValue().trim());
         m2.addProperty("description", this.G.getValue().trim());
         m2.addProperty("type", this.Q);
         m2.addProperty("target", this.H.getValue().trim());
         m2.addProperty("required", Integer.parseInt(this.I.getValue()));
         m2.addProperty("reward", Integer.parseInt(this.J.getValue()));
         m2.addProperty("day", Integer.parseInt(this.K.getValue()));
         m2.addProperty("essential", this.R);
         String ic = this.L.getValue().trim();
         if (!ic.isEmpty()) {
            m2.addProperty("icon", ic);
         }

         String ri;
         if (!(ri = this.M.getValue().trim()).isEmpty()) {
            m2.addProperty("rewardItem", ri);
         }

         m2.addProperty("rewardItemCount", Integer.parseInt(this.N.getValue()));
         com.eric.revill.d.a.c.a(m2);
         this.B = ScreenMode.a;
         this.t = m2.get("id").getAsString();
         this.init();
      } catch (NumberFormatException var4) {
         if (this.minecraft.player != null) {
            this.minecraft.player.displayClientMessage(Component.literal("Cantidad, recompensa y dia deben ser numeros enteros."), false);
         }
      }
   }

   private void g() {
      String var1 = this.D;
      switch (var1) {
         case "delete":
            com.eric.revill.d.a.c.a(this.t);
            this.t = "";
            break;
         case "reset-player":
            com.eric.revill.d.a.c.a(this.u);
            break;
         case "reset-mission":
            com.eric.revill.d.a.c.a(this.u, this.t);
      }

      this.D = "";
      this.init();
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.B == ScreenMode.a && button == 0) {
         int cl = 16;
         int ct = 70;
         JsonObject m2;
         if (mouseX >= (double)cl
            && mouseX < (double)(10 + this.h() - 2)
            && mouseY >= (double)ct
            && mouseY < (double)(this.height - 10)
            && (m2 = this.a(((int)mouseY - ct) / 58)) != null) {
            this.t = m2.get("id").getAsString();
            this.init();
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      if (this.B == ScreenMode.a && mouseX >= 10.0 && mouseX < (double)(10 + this.h()) && mouseY >= 70.0 && mouseY < (double)(this.height - 10)) {
         int vis = this.i();
         this.w = Math.max(0, Math.min(Math.max(0, this.j().size() - vis), this.w - (int)Math.signum(scrollY)));
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
      }
   }

   private int h() {
      return (int)((float)(this.width - 30) * 0.38F);
   }

   private int i() {
      return Math.max(1, (this.height - 46 - 10 - 24) / 58);
   }

   private List<JsonObject> j() {
      String q2 = this.x.toLowerCase(Locale.ROOT).trim();
      return this.r
         .stream()
         .filter(
            m2 -> q2.isEmpty()
                  || m2.get("title").getAsString().toLowerCase(Locale.ROOT).contains(q2)
                  || m2.get("description").getAsString().toLowerCase(Locale.ROOT).contains(q2)
                  || m2.get("target").getAsString().toLowerCase(Locale.ROOT).contains(q2)
         )
         .toList();
   }

   private JsonObject a(int row) {
      List<JsonObject> shown = this.j();
      int idx = this.w + row;
      return row >= 0 && idx < shown.size() ? shown.get(idx) : null;
   }

   private JsonObject k() {
      for (JsonObject m2 : this.r) {
         if (this.t.equals(m2.get("id").getAsString())) {
            return m2;
         }
      }

      return null;
   }

   private JsonObject l() {
      if (this.u == null) {
         return null;
      } else {
         for (JsonObject p2 : this.s) {
            if (this.u.toString().equals(p2.get("uuid").getAsString())) {
               return p2;
            }
         }

         return null;
      }
   }

   private boolean b(GuiGraphics ctx, JsonObject m2, int cx, int baseY, int size) {
      if (m2.get("type").getAsString().equalsIgnoreCase("KILL") && this.minecraft.level != null) {
         String id = m2.get("target").getAsString();
         if (!id.equals(this.z)) {
            ResourceLocation ident = ResourceLocation.tryParse(id);
            EntityType type = ident == null ? null : (EntityType)BuiltInRegistries.ENTITY_TYPE.getOptional(ident).orElse(null);
            Entity entity = type == null ? null : type.create(this.minecraft.level);
            LivingEntity l2;
            this.A = entity instanceof LivingEntity ? (l2 = (LivingEntity)entity) : null;
            if (this.A instanceof Mob mob) {
               mob.setNoAi(true);
            }

            this.z = id;
         }

         if (this.A == null) {
            return false;
         } else {
            InventoryScreen.renderEntityInInventoryFollowsMouse(ctx, cx - size, baseY - (int)(size * 1.5F), cx + size, baseY, size, 0.0625F, 0.0F, 0.0F, this.A);
            return true;
         }
      } else {
         return false;
      }
   }

   private void a(GuiGraphics ctx, JsonObject m2, int x, int y) {
      String type = m2.get("type").getAsString().toUpperCase(Locale.ROOT);
      if (type.equals("KILL")) {
         ctx.drawCenteredString(this.font, Component.literal("X"), x + 8, y + 2, -19891);
      } else {
         String itemId = m2.has("icon") ? m2.get("icon").getAsString() : (type.equals("COLLECT") ? m2.get("target").getAsString() : "");
         if (!itemId.isEmpty()) {
            this.a(ctx, itemId, x, y);
         } else {
            ctx.drawCenteredString(this.font, Component.literal(type.equals("BREAK") ? "#" : "+"), x + 8, y + 2, -19891);
         }
      }
   }

   private String a(String type) {
      return switch (type) {
         case "KILL" -> "Derrotar";
         case "COLLECT" -> "Recoger";
         case "BREAK" -> "Romper";
         case "CRAFT" -> "Fabricar";
         default -> type;
      };
   }

   private void a(GuiGraphics ctx, String idTxt, int x, int y) {
      ResourceLocation id = ResourceLocation.tryParse(idTxt);
      if (id != null) {
         Item item = (Item)BuiltInRegistries.ITEM.getOptional(id).orElse(null);
         if (item != null) {
            ctx.renderItem(new ItemStack(item), x, y);
         }
      }
   }

   private List<String> a(String text, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();
      StringBuilder line = new StringBuilder();

      for (String word : text.split("\\s+")) {
         String c2 = line.isEmpty() ? word : line + " " + word;
         if (this.font.width(c2) > maxWidth && !line.isEmpty()) {
            lines.add(line.toString());
            line = new StringBuilder(word);
         } else {
            line = new StringBuilder(c2);
         }
      }

      if (!line.isEmpty()) {
         lines.add(line.toString());
      }

      return lines;
   }

   private String b(String v, int max) {
      return v.length() <= max ? v : v.substring(0, max - 1) + "...";
   }

   public void onClose() {
      this.minecraft.setScreen(this.q);
   }

   public boolean isPauseScreen() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   private static enum ScreenMode {
      a,
      b,
      c;

      public static ScreenMode[] a() {
         return values();
      }

      public static ScreenMode a(String name) {
         return valueOf(name);
      }
   }
}
