package com.eric.revill.d.e;

/**
 * Pantalla de Catálogo de Ítems y Herramientas del Mod.
 * 
 * Ofrece un catálogo visual interactivo de los objetos de DEDSAFIO 4 (monedas, mochilas,
 * buzones, corazones, palitomágico) con sus recetas y explicaciones de uso.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag.Default;
import net.minecraft.world.level.ItemLike;

@Environment(EnvType.CLIENT)
public final class l extends Screen {
   private static final int a = 7;
   private final List<Item> b = a();
   private final List<Item> c = new ArrayList<>();
   private EditBox d;
   private String e = "";
   private int f;
   private Item g;
   private int h;
   private int i;
   private int j;
   private int k;
   private int l;
   private float m;

   public l() {
      super(Component.literal("Catálogo"));
      this.b();
   }

   private static List<Item> a() {
      List items = BuiltInRegistries.ITEM
         .stream()
         .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("dedsafio-4-mechanics"))
         .filter(
            item -> !List.of("mailbox", "palitomagico", "cajero_spawn_egg", "daily_mission_pedestal", "admin_guide")
                  .contains(BuiltInRegistries.ITEM.getKey(item).getPath())
         )
         .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
         .collect(Collectors.toCollection(ArrayList::new));
      items.add(Items.TOTEM_OF_UNDYING);
      return List.copyOf(items);
   }

   protected void init() {
      this.clearWidgets();
      if (this.g == null && !this.c.isEmpty()) {
         this.g = this.c.get(0);
      } else if (this.g != null && !this.c.contains(this.g)) {
         this.g = this.c.isEmpty() ? null : this.c.get(0);
      }

      int sidebarTop = this.height / 2 - 67;
      this.addRenderableWidget(new l.a(22, sidebarTop, Items.WRITABLE_BOOK, "Misiones", button -> this.minecraft.setScreen(new p(this))));
      this.addRenderableWidget(new l.a(22, sidebarTop + 50, Items.PLAYER_HEAD, "Hermandad", button -> {
         com.eric.revill.d.a.b.c();
         this.minecraft.setScreen(new k());
      }));
      this.addRenderableWidget(new l.a(22, sidebarTop + 100, Items.ENCHANTED_BOOK, "Cambios dificultad", button -> this.minecraft.setScreen(new com.eric.revill.d.e.a(this))));
      int gridStartX = 96;
      int detailWidth = Math.min(220, Math.max(150, this.width / 4));
      int listWidth = Math.max(240, this.width - gridStartX - detailWidth - 40);
      this.h = gridStartX + 12;
      this.k = Math.min(50, Math.max(30, (listWidth - 36) / 7));
      this.i = 104;
      int searchWidth = 7 * this.k + 4;
      this.d = new EditBox(this.font, gridStartX + 12, 72, searchWidth, 20, Component.literal("Buscar objetos"));
      this.d.setMaxLength(100);
      this.d.setHint(Component.literal("Buscar por nombre..."));
      this.d.setValue(this.e);
      this.d.setResponder(value -> {
         this.e = value;
         this.f = 0;
         this.b();
         if (!this.c.contains(this.g)) {
            this.g = this.c.isEmpty() ? null : this.c.get(0);
         }

         this.c();
      });
      this.addRenderableWidget(this.d);
      this.c();
   }

   private void b() {
      this.c.clear();
      String normalized = this.e.toLowerCase(Locale.ROOT).trim();

      for (Item item : this.b) {
         if (normalized.isEmpty()) {
            this.c.add(item);
         } else {
            String id = BuiltInRegistries.ITEM.getKey(item).toString().toLowerCase(Locale.ROOT);
            String name = item.getDescription().getString().toLowerCase(Locale.ROOT);
            if (id.contains(normalized) || name.contains(normalized)) {
               this.c.add(item);
            }
         }
      }
   }

   private void c() {
      int visibleRows = Math.max(1, (this.height - this.i - 32) / this.k);
      this.l = Math.max(0, (this.c.size() + 7 - 1) / 7 - visibleRows);
      this.f = Math.min(this.f, this.l);
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      this.m = (this.m + delta * 0.025F) % 1.0F;
      context.fill(0, 0, this.width, this.height, -16249581);
      int outerLeft = 10;
      int outerTop = 10;
      int outerWidth = this.width - 20;
      int outerHeight = this.height - 20;
      context.fill(outerLeft, outerTop, outerLeft + outerWidth, outerTop + outerHeight, -14340811);
      context.fill(outerLeft + 5, outerTop + 5, outerLeft + outerWidth - 5, outerTop + outerHeight - 5, -15722464);
      context.renderOutline(outerLeft, outerTop, outerWidth, outerHeight, -6641998);
      this.a(context, this.width / 2 - 72, outerTop + 2, 144, 5, true);
      this.a(context, outerLeft + 2, outerTop + 24, 4, outerHeight - 48, false);
      this.a(context, outerLeft + outerWidth - 6, outerTop + 24, 4, outerHeight - 48, false);
      this.a(context, this.width / 2 - 34, outerTop + outerHeight - 7, 68, 4, true);

      for (int cornerX : new int[]{outerLeft + 12, outerLeft + outerWidth - 20}) {
         for (int cornerY : new int[]{outerTop + 12, outerTop + outerHeight - 20}) {
            context.fill(cornerX, cornerY, cornerX + 8, cornerY + 8, -12391937);
         }
      }

      super.render(context, mouseX, mouseY, delta);
      int panelRight;
      this.j = panelRight = Math.min(this.width - 30, this.h + 7 * this.k + 16);
      int detailLeft = panelRight + 10;
      context.drawCenteredString(this.font, Component.literal("DEDSAFÍO 4 · CATÁLOGO"), this.width / 2, 28, -9900033);
      context.fill(this.h - 9, this.i - 8, panelRight, this.height - 26, -16051429);
      context.renderOutline(this.h - 9, this.i - 8, panelRight - this.h + 9, this.height - this.i - 18, -14196109);

      for (int gx = this.h; gx < panelRight; gx += 16) {
         context.fill(gx, this.i - 4, gx + 1, this.height - 31, 405128931);
      }

      for (int gy = this.i; gy < this.height - 30; gy += 16) {
         context.fill(this.h - 5, gy, panelRight - 4, gy + 1, 405128931);
      }

      int start = this.f * 7;
      int visibleRows = Math.max(1, (this.height - this.i - 32) / this.k);

      for (int index = start; index < Math.min(this.c.size(), start + visibleRows * 7); index++) {
         int local = index - start;
         int x = this.h + local % 7 * this.k;
         int y = this.i + local / 7 * this.k;
         context.fill(x, y, x + this.k - 4, y + this.k - 4, -14669261);
         context.renderOutline(x, y, this.k - 4, this.k - 4, -9865343);
         ItemStack stack = new ItemStack((ItemLike)this.c.get(index));
         context.renderItem(stack, x + (this.k - 20) / 2, y + (this.k - 20) / 2);
         if (this.c.get(index) == this.g) {
            context.renderOutline(x, y, this.k - 4, this.k - 4, -11275521);
         }
      }

      int scrollTrackX = panelRight - 8;
      int scrollTop = this.i;
      int scrollBottom = this.height - 35;
      context.fill(scrollTrackX, scrollTop, scrollTrackX + 3, scrollBottom, -10128772);
      if (this.l > 0) {
         int thumbHeight = Math.max(16, (scrollBottom - scrollTop) * visibleRows / Math.max(visibleRows, (this.c.size() + 7 - 1) / 7));
         int thumbY = scrollTop + (scrollBottom - scrollTop - thumbHeight) * this.f / this.l;
         context.fill(scrollTrackX - 1, thumbY, scrollTrackX + 5, thumbY + thumbHeight, -12064513);
      }

      context.fill(detailLeft, this.i - 8, this.width - 28, this.height - 26, -16051429);
      context.renderOutline(detailLeft, this.i - 8, this.width - 28 - detailLeft, this.height - this.i - 18, -14196109);
      if (this.g != null) {
         ItemStack selected = new ItemStack(this.g);
         context.renderItem(selected, detailLeft + 12, this.i + 6);
         String name = selected.getHoverName().getString();
         int nameWidth = Math.max(20, this.width - 28 - detailLeft - 44);
         context.drawString(this.font, Component.literal(this.b(name, nameWidth)), detailLeft + 36, this.i + 10, -10163);
         context.fill(detailLeft + 8, this.i + 28, this.width - 36, this.i + 29, -14196109);
         Item.TooltipContext tooltipContext = this.minecraft != null && this.minecraft.level != null ? Item.TooltipContext.of(this.minecraft.level) : Item.TooltipContext.EMPTY;
         List tooltipLines = selected.getTooltipLines(tooltipContext, this.minecraft != null ? this.minecraft.player : null, Default.NORMAL);
         int currentY = this.i + 36;
         int maxDetailWidth = this.width - 36 - (detailLeft + 12);

         for (int i2 = tooltipLines.size() > 0 && ((Component)tooltipLines.get(0)).getString().equalsIgnoreCase(name) ? 1 : 0;
            i2 < tooltipLines.size() && currentY < this.height - 35;
            i2++
         ) {
            Component lineText = (Component)tooltipLines.get(i2);
            String raw = lineText.getString();
            if (raw.isBlank()) {
               currentY += 4;
            } else {
               for (String wLine : this.a(raw, maxDetailWidth)) {
                  if (currentY >= this.height - 35) {
                     break;
                  }

                  context.drawString(this.font, Component.literal(wLine).withStyle(ChatFormatting.GRAY), detailLeft + 12, currentY, -3155744);
                  currentY += 10;
               }
            }
         }
      } else {
         context.drawCenteredString(this.font, Component.literal("Sin objetos que mostrar"), (detailLeft + this.width - 28) / 2, this.i + 50, -4602416);
      }

      if (this.c.isEmpty()) {
         context.drawCenteredString(this.font, Component.literal("No hay objetos que coincidan."), this.width / 2, this.height - 27, -4602416);
      }
   }

   private List<String> a(String text, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();
      StringBuilder line = new StringBuilder();

      for (String word : text.split("\\s+")) {
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
   }

   private String b(String title, int maxWidth) {
      if (this.font.width(title) <= maxWidth) {
         return title;
      } else {
         String suffix = "...";
         return this.font.plainSubstrByWidth(title, Math.max(0, maxWidth - this.font.width(suffix))) + suffix;
      }
   }

   private void a(GuiGraphics context, int x, int y, int w, int h2, boolean horizontal) {
      int length = horizontal ? w : h2;

      for (int i2 = 0; i2 < length; i2++) {
         int color = Mth.hsvToRgb((this.m + (float)i2 / (float)Math.max(1, length)) % 1.0F, 0.9F, 1.0F);
         if (horizontal) {
            context.fill(x + i2, y, x + i2 + 1, y + h2, 0xFF000000 | color);
         } else {
            context.fill(x, y + i2, x + w, y + i2 + 1, 0xFF000000 | color);
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int scrollTop = this.i;
      int scrollBottom = this.height - 35;
      if (button == 0
         && mouseX >= (double)(this.j - 9)
         && mouseX <= (double)(this.j + 2)
         && mouseY >= (double)scrollTop
         && mouseY < (double)scrollBottom
         && this.l > 0) {
         double progress = (mouseY - (double)scrollTop) / (double)(scrollBottom - scrollTop);
         this.f = (int)Math.round(progress * (double)this.l);
         return true;
      } else {
         if (button == 0) {
            int visibleRows = Math.max(1, (this.height - this.i - 32) / this.k);
            int row = ((int)mouseY - this.i) / this.k;
            int column = ((int)mouseX - this.h) / this.k;
            int index;
            if (row >= 0
               && row < visibleRows
               && column >= 0
               && column < 7
               && mouseX >= (double)this.h
               && mouseX < (double)(this.j - 12)
               && mouseY >= (double)this.i
               && (index = (this.f + row) * 7 + column) < this.c.size()) {
               this.g = this.c.get(index);
               return true;
            }
         }

         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      if (mouseX >= (double)(this.h - 12) && mouseX < (double)this.j && mouseY >= (double)(this.i - 8)) {
         this.f = Math.max(0, Math.min(this.l, this.f - (int)Math.signum(scrollY)));
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   private static final class a extends Button {
      private final Item a;

      private a(int x, int y, Item icon, String label, OnPress action) {
         super(x, y, 68, 42, Component.literal(label), action, DEFAULT_NARRATION);
         this.a = icon;
      }

      protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
         int x = this.getX();
         int y = this.getY();
         if (this.active && this.isHovered()) {
            context.fill(x, y, x + this.width, y + this.height, 808894442);
         }

         context.renderOutline(x, y, this.width, this.height, this.active ? -13189142 : -12234150);
         context.renderItem(new ItemStack(this.a), x + (this.width - 16) / 2, y + 3);
         context.pose().pushPose();
         context.pose().translate((float)x + (float)this.width / 2.0F, (float)y + 29.0F, 0.0F);
         context.pose().scale(0.5F, 0.5F, 1.0F);
         context.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), 0, -4, this.active ? -1640193 : -8944757);
         context.pose().popPose();
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class b extends Screen {
      private final Screen a;
      private int b;
      private int c;

      private b(Screen parent) {
         super(Component.literal("Manual de peligrosidad"));
         this.a = parent;
      }

      protected void init() {
         this.addRenderableWidget(
            Button.builder(Component.literal("Volver"), button -> this.minecraft.setScreen(this.a))
               .bounds(this.width / 2 - 45, this.height - 34, 90, 20)
               .build()
         );
      }

      public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
         context.fill(0, 0, this.width, this.height, -401927910);
         super.render(context, mouseX, mouseY, delta);
         context.drawCenteredString(this.font, Component.literal("MANUAL DE PELIGROSIDAD"), this.width / 2, 18, -10098433);
         List<com.eric.revill.g.a.DifficultyChange> changes = com.eric.revill.g.a.a();
         int y;
         int contentBottom = y = 40 - this.b;
         context.enableScissor(10, 36, this.width - 10, this.height - 42);

         for (com.eric.revill.g.a.DifficultyChange change : changes) {
            context.drawString(this.font, Component.literal(change.c()), 20, y, -1);
            y += 12;
            String description = change.d() != null && !change.d().isBlank() ? change.d() : change.f();

            for (String line : this.a(description, this.width - 40)) {
               context.drawString(this.font, Component.literal(line), 20, y, -4602416);
               y += 10;
            }

            y += 6;
            contentBottom = y;
         }

         context.disableScissor();
         this.c = Math.max(0, contentBottom + this.b - (this.height - 44));
         this.b = Math.min(this.b, this.c);
         if (this.c > 0) {
            context.drawString(this.font, Component.literal("Desplázate para ver más"), this.width / 2 - 60, this.height - 46, -4602416);
         }
      }

      private List<String> a(String text, int maxWidth) {
         ArrayList<String> lines = new ArrayList<>();
         StringBuilder line = new StringBuilder();

         for (String word : text.split("\\s+")) {
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
      }

      public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
         this.b = Math.max(0, Math.min(this.c, this.b - (int)(amount * 16.0)));
         return true;
      }

      public boolean isPauseScreen() {
         return false;
      }
   }
}
