package com.eric.revill.d.e;

import java.util.ArrayList;
import java.util.List;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class a extends Screen {
   private final Screen a;
   private int b;

   public a(Screen parent) {
      super(Component.literal("Cambios de dificultad"));
      this.a = parent;
   }

   protected void init() {
      this.addRenderableWidget(new f(this.width / 2 - 48, this.height - 34, 96, 22, Component.literal("Volver"), button -> this.minecraft.setScreen(this.a)));
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.fill(0, 0, this.width, this.height, -402125549);
      super.render(context, mouseX, mouseY, delta);
      context.drawCenteredString(this.font, this.title, this.width / 2, 18, -9900033);
      ArrayList<com.eric.revill.g.a.DifficultyChange> activeChanges = new ArrayList<>();

      for (com.eric.revill.g.a.DifficultyChange change : com.eric.revill.g.a.a()) {
         if (com.eric.revill.g.a.a(change.a())) {
            activeChanges.add(change);
         }
      }

      int left = Math.max(16, this.width / 2 - 250);
      int right = Math.min(this.width - 16, this.width / 2 + 250);
      int top = 42;
      int bottom = this.height - 44;
      int y = top - this.b;
      int contentHeight = 0;
      context.enableScissor(left, top, right, bottom);
      if (activeChanges.isEmpty()) {
         context.drawCenteredString(this.font, Component.literal("No hay cambios de dificultad activos."), this.width / 2, top + 18, -4602416);
      }

      for (com.eric.revill.g.a.DifficultyChange changex : activeChanges) {
         String description = changex.d() != null && !changex.d().isBlank() ? changex.d() : changex.f();
         List<String> lines = this.a(description, right - left - 24);
         int cardHeight = 28 + Math.max(1, lines.size()) * 10;
         context.fill(left, y, right, y + cardHeight, -871096020);
         context.renderOutline(left, y, right - left, cardHeight, -14196109);
         context.drawString(this.font, Component.literal(changex.c()), left + 10, y + 7, -9900033);
         int lineY = y + 21;

         for (String line : lines) {
            context.drawString(this.font, line, left + 10, lineY, -3090723, false);
            lineY += 10;
         }

         y += cardHeight + 8;
         contentHeight += cardHeight + 8;
      }

      context.disableScissor();
      int maxScroll = Math.max(0, contentHeight - (bottom - top));
      this.b = Math.min(this.b, maxScroll);
      if (maxScroll > 0) {
         context.drawString(this.font, Component.literal("Desplázate para ver más"), this.width / 2 - 58, this.height - 45, -4602416, false);
      }
   }

   private List<String> a(String value, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();
      if (value != null && !value.isBlank()) {
         StringBuilder line = new StringBuilder();

         for (String word : value.split("\\s+")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (this.font.width(candidate) > maxWidth && !line.isEmpty()) {
               lines.add(line.toString());
               line = new StringBuilder(word);
            } else {
               line = new StringBuilder(candidate);
            }
         }

         if (!line.isEmpty()) {
            lines.add(line.toString());
         }

         return lines;
      } else {
         return lines;
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      int total = 0;

      for (com.eric.revill.g.a.DifficultyChange change : com.eric.revill.g.a.a()) {
         if (com.eric.revill.g.a.a(change.a())) {
            String description = change.d() != null && !change.d().isBlank() ? change.d() : change.f();
            total += 38 + Math.max(1, this.a(description, this.width / 2 + 240).size()) * 10;
         }
      }

      int maxScroll = Math.max(0, total - (this.height - 86));
      this.b = Math.max(0, Math.min(maxScroll, this.b - (int)(amount * 16.0)));
      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }
}
