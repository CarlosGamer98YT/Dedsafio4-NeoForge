package com.eric.revill.d.e;

import com.eric.revill.RevillMod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class h extends Screen {
   private static final String[] a = new String[]{"INICIO", "CAMBIOS", "WAYPOINTS", "REVILL"};
   private static final String[] b = new String[]{"roja", "verde", "naranja", "rosada"};
   private static final String[] c = new String[]{"star", "bunker", "home", "flag", "warning"};
   private static final com.eric.revill.j.c[] d = com.eric.revill.j.c.a();
   private final List<com.eric.revill.g.a.DifficultyChange> e = new ArrayList<>();
   private int f = 0;
   private int g = 0;
   private int h = 0;
   private int i;
   private int j = 0;
   private double k = 0.0;
   private int l = 0;
   private String m = "";
   private EditBox n;
   private EditBox o;
   private Button p;
   private Button q;
   private EditBox r;
   private EditBox s;
   private EditBox t;
   private EditBox u;
   private Button v;
   private String w = null;
   private EditBox x;
   private EditBox y;
   private EditBox z;
   private EditBox A;
   private final List<h.a> B = new ArrayList<>();
   private EditBox revillDurationField;
   private Button revillMusicButton;
   private boolean revillMusicEnabled = true;
   private final Map<String, String> revillTaskMessages = new HashMap<>();

   public h() {
      super(Component.literal("DEDSAFIO 4"));
   }

   private Font a() {
      if (this.font != null) {
         return this.font;
      } else {
         Minecraft client = Minecraft.getInstance();
         return client != null ? client.font : null;
      }
   }

   protected void init() {
      super.init();
      this.clearWidgets();
      this.B.clear();
      Font tr = this.a();
      if (tr != null) {
         int centerX = this.width / 2;
         int navGap = 6;
         int navWidth = Math.min(126, (this.width - 24 - navGap * (a.length - 1)) / a.length);
         int navStartX = centerX - (navWidth * a.length + navGap * (a.length - 1)) / 2;

         for (int i2 = 0; i2 < a.length; i2++) {
            int pageIndex = i2;
            String label = (i2 == this.f ? "[ " : "") + a[i2] + (i2 == this.f ? " ]" : "");
            this.addRenderableWidget(Button.builder(Component.literal(label), button -> {
               this.f = pageIndex;
               this.k = 0.0;
               this.w = null;
               this.e();
               this.init();
            }).bounds(navStartX + i2 * (navWidth + navGap), 12, navWidth, 20).build());
         }

         if (this.b()) {
            this.b(tr, centerX);
         } else if (this.c()) {
            this.b(centerX);
            this.a(tr);
         } else if (this.d()) {
            this.a(centerX);
         } else {
            this.a(tr, centerX);
         }

         this.e();
      }
   }

   private boolean b() {
      return this.f == 2;
   }

   private boolean c() {
      return this.f == 1;
   }

   private boolean d() {
      return this.f == 3;
   }

   private void a(int centerX) {
      int buttonWidth = Math.min(280, this.width - 32);
      int buttonLeft = centerX - buttonWidth / 2;
      this.addRenderableWidget(
         Button.builder(Component.literal("PONER EL CIELO EN ROJO"), button -> this.c(0)).bounds(buttonLeft, 82, buttonWidth, 24).build()
      );
      this.addRenderableWidget(Button.builder(Component.literal("ABRIR LA GRIETA"), button -> this.c(1)).bounds(buttonLeft, 118, buttonWidth, 24).build());
      this.addRenderableWidget(Button.builder(Component.literal("CERRAR LA GRIETA"), button -> this.c(2)).bounds(buttonLeft, 154, buttonWidth, 24).build());
      this.addRenderableWidget(Button.builder(Component.literal("QUITAR EL CIELO ROJO"), button -> this.c(3)).bounds(buttonLeft, 190, buttonWidth, 24).build());
   }

   private void b(int centerX) {
      int gap = 4;
      int dayCount = 7;
      int buttonWidth = Math.min(74, (this.width - 28 - gap * (dayCount - 1)) / dayCount);
      int startX = centerX - (buttonWidth * dayCount + gap * (dayCount - 1)) / 2;

      for (int day = 0; day < dayCount; day++) {
         int dayIndex = day;
         String label = (day == this.g ? "[" : "") + "DÍA " + (day + 1) + (day == this.g ? "]" : "");
         this.addRenderableWidget(Button.builder(Component.literal(label), button -> {
            this.g = dayIndex;
            this.k = 0.0;
            this.e();
            this.init();
         }).bounds(startX + day * (buttonWidth + gap), 41, buttonWidth, 20).build());
      }
   }

   private void a(Font tr) {
      int searchWidth = Math.min(360, this.width - 32);
      this.o = new EditBox(tr, (this.width - searchWidth) / 2, 65, searchWidth, 18, Component.literal("Buscar cambios"));
      this.o.setMaxLength(80);
      this.o.setHint(Component.literal("Buscar cambios de dificultad..."));
      this.o.setValue(this.m);
      this.o.setResponder(value -> {
         this.m = value;
         this.k = 0.0;
         this.e();
      });
      this.addRenderableWidget(this.o);
   }

   private void a(Font tr, int centerX) {
      int formWidth = Math.min(380, this.width - 32);
      int formLeft = centerX - formWidth / 2;
      this.n = new EditBox(tr, formLeft, 70, formWidth, 20, Component.literal("Mensaje"));
      this.n.setMaxLength(240);
      this.n.setHint(Component.literal("Mensaje personalizado para la misión"));
      this.n
         .setValue(
            this.revillTaskMessages
               .getOrDefault(d[this.i].b(), RevillMod.c.revillTaskMessages == null ? "" : RevillMod.c.revillTaskMessages.getOrDefault(d[this.i].b(), ""))
         );
      this.addRenderableWidget(this.n);
      this.q = Button.builder(
            Component.literal("Tarea REVILL: " + d[this.i].c()),
            button -> {
               this.revillTaskMessages.put(d[this.i].b(), this.n.getValue());
               this.i = (this.i + 1) % d.length;
               this.n
                  .setValue(
                     this.revillTaskMessages
                        .getOrDefault(
                           d[this.i].b(), RevillMod.c.revillTaskMessages == null ? "" : RevillMod.c.revillTaskMessages.getOrDefault(d[this.i].b(), "")
                        )
                  );
               this.q.setMessage(Component.literal("Tarea REVILL: " + d[this.i].c()));
            }
         )
         .bounds(formLeft, 96, formWidth, 20)
         .build();
      this.addRenderableWidget(this.q);
      int groupWidth = Math.min(260, this.width - 24);
      int groupLeft = centerX - groupWidth / 2;
      this.p = Button.builder(Component.literal("Color: " + b[this.h].toUpperCase()), button -> {
         this.h = (this.h + 1) % b.length;
         this.p.setMessage(Component.literal("Color: " + b[this.h].toUpperCase()));
      }).bounds(groupLeft, 128, 112, 20).build();
      this.addRenderableWidget(this.p);
      this.addRenderableWidget(
         Button.builder(Component.literal("LANZAR RULETA"), button -> this.g()).bounds(groupLeft + 118, 128, groupWidth - 118, 20).build()
      );
      int eventButtonWidth = (groupWidth - 6) / 2;
      this.addRenderableWidget(Button.builder(Component.literal("INICIAR REVILL"), button -> this.h()).bounds(groupLeft, 158, eventButtonWidth, 20).build());
      this.addRenderableWidget(
         Button.builder(Component.literal("PARAR REVILL"), button -> this.i())
            .bounds(groupLeft + eventButtonWidth + 6, 158, eventButtonWidth, 20)
            .build()
      );
      this.createDurationField(tr, groupLeft, 194);
      this.revillMusicButton = Button.builder(this.musicLabel(), button -> {
         this.revillMusicEnabled = !this.revillMusicEnabled;
         this.revillMusicButton.setMessage(this.musicLabel());
      }).bounds(groupLeft + 118, 194, groupWidth - 118, 20).build();
      this.addRenderableWidget(this.revillMusicButton);
   }

   private void createDurationField(Font tr, int x, int y) {
      this.revillDurationField = new EditBox(tr, x, y, 112, 20, Component.literal("Duración (segundos)"));
      this.revillDurationField.setMaxLength(5);
      this.revillDurationField.setValue(Integer.toString(Math.max(1, Math.min(86400, RevillMod.c.d))));
      this.revillDurationField.setHint(Component.literal("Segundos"));
      this.addRenderableWidget(this.revillDurationField);
   }

   private Component musicLabel() {
      return Component.literal("Música: " + (this.revillMusicEnabled ? "SÍ" : "NO"));
   }

   public void applyRevillSettings(int durationSeconds, boolean musicEnabled, Map<String, String> messages) {
      RevillMod.c.d = Math.max(1, Math.min(86400, durationSeconds));
      RevillMod.c.revillMusicEnabled = musicEnabled;
      if (RevillMod.c.revillTaskMessages == null) {
         RevillMod.c.revillTaskMessages = new HashMap<>();
      }

      RevillMod.c.revillTaskMessages.clear();
      RevillMod.c.revillTaskMessages.putAll(messages);
      this.revillTaskMessages.clear();
      this.revillTaskMessages.putAll(messages);
      this.revillMusicEnabled = musicEnabled;
      if (this.revillDurationField != null) {
         this.revillDurationField.setValue(Integer.toString(RevillMod.c.d));
      }

      if (this.revillMusicButton != null) {
         this.revillMusicButton.setMessage(this.musicLabel());
      }

      if (this.n != null) {
         this.n.setValue(this.revillTaskMessages.getOrDefault(d[this.i].b(), ""));
      }
   }

   private void b(Font tr, int centerX) {
      Minecraft client = Minecraft.getInstance();
      BlockPos playerPos = client != null && client.player != null ? client.player.blockPosition() : BlockPos.ZERO;
      int rowY = 54;
      this.r = new EditBox(tr, centerX - 240, rowY, 120, 18, Component.literal("Nombre"));
      this.r.setMaxLength(32);
      this.r.setValue("Punto " + (com.eric.revill.d.b.c.b().size() + 1));
      this.addRenderableWidget(this.r);
      this.v = Button.builder(Component.literal("Icono: " + c[this.j].toUpperCase()), button -> {
         this.j = (this.j + 1) % c.length;
         this.v.setMessage(Component.literal("Icono: " + c[this.j].toUpperCase()));
      }).bounds(centerX - 115, rowY, 75, 18).build();
      this.addRenderableWidget(this.v);
      this.s = new EditBox(tr, centerX - 35, rowY, 40, 18, Component.literal("X"));
      this.s.setValue(String.valueOf(playerPos.getX()));
      this.addRenderableWidget(this.s);
      this.t = new EditBox(tr, centerX + 10, rowY, 35, 18, Component.literal("Y"));
      this.t.setValue(String.valueOf(playerPos.getY()));
      this.addRenderableWidget(this.t);
      this.u = new EditBox(tr, centerX + 50, rowY, 40, 18, Component.literal("Z"));
      this.u.setValue(String.valueOf(playerPos.getZ()));
      this.addRenderableWidget(this.u);
      this.addRenderableWidget(Button.builder(Component.literal("+ AQUÍ"), button -> this.j()).bounds(centerX + 95, rowY, 55, 18).build());
      this.addRenderableWidget(Button.builder(Component.literal("+ CON COORDENADAS"), button -> this.k()).bounds(centerX + 155, rowY, 95, 18).build());
   }

   private void e() {
      this.e.clear();
      if (this.c()) {
         String query = this.o == null ? "" : this.o.getValue().trim().toLowerCase(Locale.ROOT);

         for (com.eric.revill.g.a.DifficultyChange change : query.isEmpty() ? com.eric.revill.g.a.a(this.g + 1) : com.eric.revill.g.a.a()) {
            String searchable = (change.c() + " " + change.d() + " " + change.f()).toLowerCase(Locale.ROOT);
            if (query.isEmpty() || searchable.contains(query)) {
               this.e.add(change);
            }
         }
      }

      this.f();
   }

   private void f() {
      int listTop = this.b() ? 82 : (this.c() ? 106 : this.height);
      int listBottom = this.height - 15;
      int contentHeight = 0;
      int cardWidth = Math.min(540, this.width - 60);
      if (this.b()) {
         for (com.eric.revill.t.a wp : com.eric.revill.d.b.c.b()) {
            int h2 = wp.a().equals(this.w) ? 75 : 44;
            contentHeight += h2 + 8;
         }
      } else if (this.c()) {
         for (com.eric.revill.g.a.DifficultyChange change : this.e) {
            contentHeight += this.a(change, cardWidth) + 8;
         }
      }

      int visibleHeight = Math.max(50, listBottom - listTop);
      this.l = Math.max(0, contentHeight - visibleHeight);
      this.k = Mth.clamp(this.k, 0.0, (double)this.l);
   }

   private int a(com.eric.revill.g.a.DifficultyChange change, int cardWidth) {
      List<String> wrapped = this.a(change.d(), cardWidth - 110);
      int textHeight = Math.max(1, wrapped.size()) * 10;
      return Math.max(45, 24 + textHeight);
   }

   private List<String> a(String text, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();
      if (text != null && !text.isBlank()) {
         Font tr = this.a();
         if (tr == null) {
            lines.add(text);
            return lines;
         } else {
            String[] words = text.split("\\s+");
            StringBuilder current = new StringBuilder();

            for (String word : words) {
               String candidate = current.length() == 0 ? word : current + " " + word;
               if (tr.width(candidate) > maxWidth && current.length() > 0) {
                  lines.add(current.toString());
                  current = new StringBuilder(word);
               } else {
                  current = new StringBuilder(candidate);
               }
            }

            if (current.length() > 0) {
               lines.add(current.toString());
            }

            return lines;
         }
      } else {
         return lines;
      }
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.fill(0, 0, this.width, this.height, -1207959552);
      super.render(context, mouseX, mouseY, delta);
      Font tr = this.a();
      if (tr != null) {
         int centerX = this.width / 2;
         if (this.b()) {
            this.b(context, tr, centerX, mouseX, mouseY);
         } else if (this.c()) {
            this.a(context, tr, centerX, mouseX, mouseY);
         } else if (this.d()) {
            context.drawCenteredString(tr, Component.literal("CONTROL DE REVILL"), centerX, 48, -1);
            context.drawCenteredString(tr, Component.literal("Los controles afectan a todos los jugadores conectados."), centerX, 65, -4473925);
         } else {
            this.a(context, tr, centerX);
         }
      }
   }

   private void a(GuiGraphics context, Font tr, int centerX) {
      context.drawCenteredString(tr, Component.literal("CENTRO DE CONTROL"), centerX, 44, -1);
      context.fill(centerX - 132, 61, centerX + 132, 62, -6474700);
      int groupWidth = Math.min(260, this.width - 24);
      int groupLeft = centerX - groupWidth / 2;
      context.drawString(tr, "Duración (segundos)", (float)groupLeft, 182.0F, 16777215, false);
      context.drawString(tr, "Música del evento", (float)(groupLeft + 118), 182.0F, 16777215, false);
   }

   private void a(GuiGraphics context, Font tr, int centerX, int mouseX, int mouseY) {
      String heading = this.m.isBlank() ? "CAMBIOS DE DIFICULTAD · DÍA " + (this.g + 1) : "RESULTADOS DE BÚSQUEDA";
      context.drawCenteredString(tr, Component.literal(heading), centerX, 88, -1);
      int listTop = 106;
      int listBottom = this.height - 15;
      int cardWidth = Math.min(540, this.width - 60);
      int cardLeft = centerX - cardWidth / 2;
      this.B.clear();
      int currentY = listTop - (int)this.k;
      context.enableScissor(cardLeft - 10, listTop, cardLeft + cardWidth + 10, listBottom);

      for (com.eric.revill.g.a.DifficultyChange change : this.e) {
         int cardHeight = this.a(change, cardWidth);
         if (currentY + cardHeight >= listTop && currentY <= listBottom) {
            boolean active = com.eric.revill.g.a.a(change.a());
            int bgColor = active ? 1712995104 : 1428301098;
            int borderColor = active ? -12331410 : -11184811;
            context.fill(cardLeft, currentY, cardLeft + cardWidth, currentY + cardHeight, bgColor);
            context.renderOutline(cardLeft, currentY, cardWidth, cardHeight, borderColor);
            String statusText = active ? "[ACTIVO]" : "[INACTIVO]";
            String title = change.c() + " " + statusText;
            context.drawString(tr, title, cardLeft + 10, currentY + 6, 16777215, true);
            String explanation = change.d() != null && !change.d().isBlank() ? change.d() : change.f();
            List<String> wrapped = this.a(explanation, cardWidth - 110);
            int lineY = currentY + 18;

            for (String line : wrapped) {
               if (lineY + 9 <= currentY + cardHeight) {
                  context.drawString(tr, "§f" + line, cardLeft + 10, lineY, 16777215, false);
                  lineY += 10;
               }
            }

            int btnWidth = 77;
            int btnHeight = 18;
            int btnX = cardLeft + cardWidth - btnWidth - 8;
            int btnY = currentY + cardHeight / 2 - btnHeight / 2;
            boolean hovered = mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight;
            int btnBg = hovered ? -14324936 : -15252186;
            context.fill(btnX, btnY, btnX + btnWidth, btnY + btnHeight, btnBg);
            context.renderOutline(btnX, btnY, btnWidth, btnHeight, -12331410);
            context.drawCenteredString(tr, Component.literal(active ? "DESACTIVAR" : "ACTIVAR"), btnX + btnWidth / 2, btnY + 5, 16777215);
            String changeId = change.a();
            boolean nextState = !active;
            this.B.add(new h.a(btnX, btnY, btnWidth, btnHeight, () -> {
               if (active) {
                  this.a(changeId, false);
               } else {
                  this.b(changeId);
               }
            }));
         }

         currentY += cardHeight + 8;
      }

      context.disableScissor();
      this.a(context, cardLeft + cardWidth + 4, listTop, listBottom);
   }

   private void b(GuiGraphics context, Font tr, int centerX, int mouseX, int mouseY) {
      context.drawCenteredString(tr, Component.literal("GESTIÓN DE WAYPOINTS"), centerX, 36, -1);
      int listTop = 82;
      int listBottom = this.height - 15;
      int cardWidth = Math.min(540, this.width - 60);
      int cardLeft = centerX - cardWidth / 2;
      this.B.clear();
      int currentY = listTop - (int)this.k;
      List<com.eric.revill.t.a> waypoints = com.eric.revill.d.b.c.b();
      context.enableScissor(cardLeft - 10, listTop, cardLeft + cardWidth + 10, listBottom);
      if (waypoints.isEmpty()) {
         context.drawCenteredString(tr, Component.literal("No hay waypoints creados. Crea uno usando el panel de arriba."), centerX, listTop + 20, 16777215);
      }

      for (com.eric.revill.t.a wp : waypoints) {
         boolean isEditing = wp.a().equals(this.w);
         int cardHeight = isEditing ? 75 : 44;
         if (currentY + cardHeight >= listTop && currentY <= listBottom) {
            context.fill(cardLeft, currentY, cardLeft + cardWidth, currentY + cardHeight, 1712726574);
            context.renderOutline(cardLeft, currentY, cardWidth, cardHeight, -16711732);
            String iconSymbol = this.a(wp.d());
            String title = "§f" + iconSymbol + " §f§l" + wp.b() + " §f(" + wp.f() + ")";
            context.drawString(tr, title, cardLeft + 10, currentY + 6, 16777215, true);
            BlockPos pos = wp.c();
            String posText = String.format("§fPosición: X: %d  Y: %d  Z: %d", pos.getX(), pos.getY(), pos.getZ());
            context.drawString(tr, posText, cardLeft + 10, currentY + 20, 16777215, false);
            int btnWidth = 60;
            int btnHeight = 18;
            int editBtnX = cardLeft + cardWidth - 135;
            int deleteBtnX = cardLeft + cardWidth - 68;
            int btnY = currentY + 10;
            boolean editHovered = mouseX >= editBtnX && mouseX <= editBtnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight;
            context.fill(editBtnX, btnY, editBtnX + btnWidth, btnY + btnHeight, editHovered ? -16750968 : -16759706);
            context.renderOutline(editBtnX, btnY, btnWidth, btnHeight, -16724737);
            context.drawCenteredString(tr, Component.literal(isEditing ? "CANCELAR" : "EDITAR"), editBtnX + btnWidth / 2, btnY + 5, 16777215);
            String wpId = wp.a();
            this.B.add(new h.a(editBtnX, btnY, btnWidth, btnHeight, () -> {
               if (isEditing) {
                  this.w = null;
                  this.init();
               } else {
                  this.a(wp);
               }
            }));
            boolean deleteHovered = mouseX >= deleteBtnX && mouseX <= deleteBtnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight;
            context.fill(deleteBtnX, btnY, deleteBtnX + btnWidth, btnY + btnHeight, deleteHovered ? -6750208 : -10092544);
            context.renderOutline(deleteBtnX, btnY, btnWidth, btnHeight, -52429);
            context.drawCenteredString(tr, Component.literal("BORRAR"), deleteBtnX + btnWidth / 2, btnY + 5, 16777215);
            this.B.add(new h.a(deleteBtnX, btnY, deleteBtnX + btnWidth, btnY + btnHeight, () -> this.c(wpId)));
            if (isEditing) {
               context.drawString(tr, "Editar Nombre y Coordenadas:", cardLeft + 10, currentY + 36, 16777215, false);
               int saveBtnX = cardLeft + cardWidth - 85;
               int saveBtnY = currentY + 48;
               boolean saveHovered = mouseX >= saveBtnX && mouseX <= saveBtnX + 75 && mouseY >= saveBtnY && mouseY <= saveBtnY + 18;
               context.fill(saveBtnX, saveBtnY, saveBtnX + 75, saveBtnY + 18, saveHovered ? -16733628 : -16746701);
               context.renderOutline(saveBtnX, saveBtnY, 75, 18, -11141291);
               context.drawCenteredString(tr, Component.literal("GUARDAR"), saveBtnX + 37, saveBtnY + 5, 16777215);
               this.B.add(new h.a(saveBtnX, saveBtnY, 75, 18, () -> this.b(wp)));
            }
         }

         currentY += cardHeight + 8;
      }

      context.disableScissor();
      this.a(context, cardLeft + cardWidth + 4, listTop, listBottom);
   }

   private void a(com.eric.revill.t.a wp) {
      this.w = wp.a();
      Font tr = this.a();
      if (tr != null) {
         int cardWidth = Math.min(540, this.width - 60);
         int centerX = this.width / 2;
         int cardLeft = centerX - cardWidth / 2;
         int listTop = 82;
         int currentY = listTop - (int)this.k;

         for (com.eric.revill.t.a item : com.eric.revill.d.b.c.b()) {
            if (item.a().equals(wp.a())) {
               int fieldsY = currentY + 48;
               this.x = new EditBox(tr, cardLeft + 10, fieldsY, 110, 18, Component.literal("Nombre"));
               this.x.setValue(wp.b());
               this.addRenderableWidget(this.x);
               BlockPos pos = wp.c();
               this.y = new EditBox(tr, cardLeft + 125, fieldsY, 40, 18, Component.literal("X"));
               this.y.setValue(String.valueOf(pos.getX()));
               this.addRenderableWidget(this.y);
               this.z = new EditBox(tr, cardLeft + 170, fieldsY, 35, 18, Component.literal("Y"));
               this.z.setValue(String.valueOf(pos.getY()));
               this.addRenderableWidget(this.z);
               this.A = new EditBox(tr, cardLeft + 210, fieldsY, 40, 18, Component.literal("Z"));
               this.A.setValue(String.valueOf(pos.getZ()));
               this.addRenderableWidget(this.A);
               break;
            }

            currentY += (item.a().equals(this.w) ? 75 : 44) + 8;
         }

         this.f();
      }
   }

   private void b(com.eric.revill.t.a oldWp) {
      if (this.x != null) {
         String name = this.x.getValue().isBlank() ? oldWp.b() : this.x.getValue();
         int x = this.b(this.y != null ? this.y.getValue() : "", oldWp.c().getX());
         int y = this.b(this.z != null ? this.z.getValue() : "", oldWp.c().getY());
         int z = this.b(this.A != null ? this.A.getValue() : "", oldWp.c().getZ());
         this.a(oldWp.a(), name, oldWp.d(), new BlockPos(x, y, z), oldWp.e(), oldWp.f());
         this.w = null;
         this.init();
      }
   }

   private String a(String icon) {
      return switch (icon) {
         case "bunker" -> "[\ud83d\udee1]";
         case "home" -> "[\ud83c\udfe0]";
         case "flag" -> "[\ud83d\udea9]";
         case "warning" -> "[⚠]";
         default -> "[★]";
      };
   }

   private int b(String text, int defaultValue) {
      try {
         return Integer.parseInt(text.trim());
      } catch (Exception var4) {
         return defaultValue;
      }
   }

   private void a(GuiGraphics context, int scrollBarX, int listTop, int listBottom) {
      if (this.l > 0) {
         int barHeight = listBottom - listTop;
         int thumbHeight = Math.max(15, barHeight * barHeight / (barHeight + this.l));
         int thumbY = listTop + (int)(this.k * (double)(barHeight - thumbHeight) / (double)this.l);
         context.fill(scrollBarX, listTop, scrollBarX + 3, listBottom, 1157627903);
         context.fill(scrollBarX, thumbY, scrollBarX + 3, thumbY + thumbHeight, -12285697);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         for (h.a btn : this.B) {
            if (mouseX >= (double)btn.a() && mouseX <= (double)(btn.a() + btn.c()) && mouseY >= (double)btn.b() && mouseY <= (double)(btn.b() + btn.d())) {
               btn.e().run();
               return true;
            }
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      if (this.l > 0) {
         this.k = Mth.clamp(this.k - scrollY * 20.0, 0.0, (double)this.l);
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
      }
   }

   private void g() {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(b[this.h]);
      buf.writeUtf(this.n != null ? this.n.getValue() : "");
      ClientPlayNetworking.send(com.eric.revill.g.b.e, buf);
      this.onClose();
   }

   private void h() {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(d[this.i].b());
      String message = this.n != null ? this.n.getValue() : "";
      this.revillTaskMessages.put(d[this.i].b(), message);
      buf.writeUtf(message, 240);
      buf.writeUtf(b[this.h], 32);
      buf.writeVarInt(this.readDurationSeconds());
      buf.writeBoolean(this.revillMusicEnabled);
      ClientPlayNetworking.send(com.eric.revill.g.b.f, buf);
      this.onClose();
   }

   private int readDurationSeconds() {
      int configured = RevillMod.c.d;
      if (this.revillDurationField == null) {
         return configured;
      } else {
         try {
            return Math.max(1, Math.min(86400, Integer.parseInt(this.revillDurationField.getValue().trim())));
         } catch (NumberFormatException var3) {
            return configured;
         }
      }
   }

   private void i() {
      ClientPlayNetworking.send(com.eric.revill.g.b.g, PacketByteBufs.empty());
      this.onClose();
   }

   private void c(int action) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeVarInt(action);
      ClientPlayNetworking.send(com.eric.revill.g.b.h, buf);
   }

   private void b(String id) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(id);
      ClientPlayNetworking.send(com.eric.revill.g.b.b, buf);
      this.onClose();
   }

   private void a(String id, boolean enabled) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(id, 64);
      buf.writeBoolean(enabled);
      ClientPlayNetworking.send(com.eric.revill.g.b.c, buf);
   }

   private void j() {
      FriendlyByteBuf buf = PacketByteBufs.create();
      String name = this.r != null && !this.r.getValue().isBlank() ? this.r.getValue() : "Punto de Interés";
      String icon = c[this.j];
      buf.writeUtf(name);
      buf.writeUtf(icon);
      ClientPlayNetworking.send(com.eric.revill.t.b.b, buf);
      this.init();
   }

   private void k() {
      Minecraft client = Minecraft.getInstance();
      BlockPos playerPos = client != null && client.player != null ? client.player.blockPosition() : BlockPos.ZERO;
      String dim = client != null && client.player != null ? client.player.level().dimension().location().toString() : "minecraft:overworld";
      String name = this.r != null && !this.r.getValue().isBlank() ? this.r.getValue() : "Punto Personalizado";
      String icon = c[this.j];
      int x = this.b(this.s != null ? this.s.getValue() : "", playerPos.getX());
      int y = this.b(this.t != null ? this.t.getValue() : "", playerPos.getY());
      int z = this.b(this.u != null ? this.u.getValue() : "", playerPos.getZ());
      String id = "wp_" + System.currentTimeMillis();
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(id);
      buf.writeUtf(name);
      buf.writeUtf(icon);
      buf.writeBlockPos(new BlockPos(x, y, z));
      buf.writeInt(-16711732);
      buf.writeUtf(dim);
      ClientPlayNetworking.send(com.eric.revill.t.b.d, buf);
      this.init();
   }

   private void a(String id, String name, String icon, BlockPos pos, int color, String dim) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(id);
      buf.writeUtf(name);
      buf.writeUtf(icon);
      buf.writeBlockPos(pos);
      buf.writeInt(color);
      buf.writeUtf(dim);
      ClientPlayNetworking.send(com.eric.revill.t.b.d, buf);
   }

   private void c(String id) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(id);
      ClientPlayNetworking.send(com.eric.revill.t.b.c, buf);
      this.init();
   }

   @Environment(EnvType.CLIENT)
   private static record a(int a, int b, int c, int d, Runnable e) {
   }
}
