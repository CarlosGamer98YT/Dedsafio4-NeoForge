package com.eric.revill.d.e;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public final class k extends Screen {
   private static final int a = 560;
   private static final int b = 300;
   private static final int c = 26;
   private static final int d = 26;
   private static final int e = 22;
   private static final int f = 29;
   private static final int g = 56;
   private static final int h = 6;
   private static final int i = 26;
   private static final int j = 52;
   private static final int k = 38;
   private static final int[] l = new int[]{
      -6737101,
      -3381709,
      -1656832,
      -10053325,
      -13395610,
      -13391190,
      -13395508,
      -13408564,
      -10079335,
      -6736999,
      -3381607,
      -10075102,
      -14540254,
      -11184811,
      -5592406,
      -1118482
   };
   private int m = 0;
   private int n = 16733525;
   private String o = "";
   private int p = 0;
   private int q = 0;
   private int r = 0;
   private String s = "compass";
   private boolean t;
   private String u;
   private static final int[] v = new int[]{-792671, -870240, -3808859, -5777186, -4667416, -2638366, -736553, -1249295};
   private EditBox w;
   private EditBox x;
   private EditBox y;
   private static final Item[] z = new Item[]{Items.WRITABLE_BOOK, Items.OAK_SIGN, Items.COMPASS, Items.WHITE_BANNER, Items.PLAYER_HEAD};
   private int A = -1;

   public k() {
      super(Component.literal("Hermandad"));
   }

   protected void init() {
      this.clearWidgets();
      int px = this.a();
      int py = this.b();
      CompoundTag snapshot = com.eric.revill.d.a.b.b();
      this.o = b(snapshot);
      boolean hasGuild = snapshot.getBoolean("hasGuild");
      if (!hasGuild) {
         this.a(px, py, snapshot);
      } else {
         for (int i2 = 0; i2 < z.length; i2++) {
            int idx = i2;
            int tx = px + 6 + i2 * 27;
            int ty = py + 29;
            this.addRenderableWidget(new k.a(tx, ty, 26, 22, Component.empty(), () -> {
               this.m = idx;
               this.r = 0;
               this.init();
            }));
         }

         CompoundTag guild = snapshot.getCompound("guild");
         switch (this.m) {
            case 0:
               this.b(px, py, guild);
               break;
            case 1:
               this.c(px, py, guild);
               break;
            case 2:
               this.d(px, py, guild);
               break;
            case 3:
               this.e(px, py, guild);
               break;
            case 4:
               this.f(px, py, guild);
         }
      }
   }

   private void a(GuiGraphics ctx, ItemStack stack, int x, int y) {
      if (stack != null && !stack.isEmpty()) {
         ctx.renderItem(stack, x, y);
      }
   }

   private void a(GuiGraphics ctx, int px, int py) {
      int pickerX = px + 280 - 39;
      int pickerY = py + 26 + 4;
      ctx.fill(pickerX - 4, pickerY - 4, pickerX + 72, pickerY + 72, -15722464);
      ctx.renderOutline(pickerX - 4, pickerY - 4, 76, 76, -6641998);

      for (int i2 = 0; i2 < l.length; i2++) {
         int x = pickerX + i2 % 4 * 18;
         int y = pickerY + i2 / 4 * 18;
         ctx.fill(x, y, x + 14, y + 14, l[i2]);
         ctx.renderOutline(x, y, 14, 14, -1118482);
      }
   }

   private void a(GuiGraphics ctx, UUID playerId, int x, int y) {
      ResourceLocation skin = DefaultPlayerSkin.get(playerId).texture();
      PlayerInfo entry;
      if (this.minecraft != null && this.minecraft.getConnection() != null && (entry = this.minecraft.getConnection().getPlayerInfo(playerId)) != null) {
         skin = entry.getSkin().texture();
      }

      ctx.blit(skin, x, y, 16, 16, 8.0F, 8.0F, 8, 8, 64, 64);
      ctx.blit(skin, x, y, 16, 16, 40.0F, 8.0F, 8, 8, 64, 64);
   }

   private void a(int px, int py, CompoundTag snapshot) {
      CompoundTag invite = snapshot.getCompound("invite");
      int cy = py + 26 + 40;
      if (!invite.isEmpty()) {
         this.addRenderableWidget(this.a("Aceptar invitación", px + 280 - 115, cy, 120, 22, () -> this.a(2, new CompoundTag())));
         this.addRenderableWidget(this.a("Rechazar", px + 280 + 15, cy, 90, 22, () -> this.a(3, new CompoundTag())));
      } else {
         this.w = this.a(px + 20, cy, 300, 22, "Nombre de hermandad...");
         this.addRenderableWidget(this.w);
         this.addRenderableWidget(this.a("Crear", px + 328, cy, 80, 22, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("name", this.w.getValue());
            data.putInt("color", this.n);
            this.a(0, data);
         }));
         this.addRenderableWidget(this.a("Color", px + 416, cy, 80, 22, () -> this.n = this.n == 5636095 ? 16733525 : (this.n == 16733525 ? 5635925 : 5636095)));
      }

      this.addRenderableWidget(this.a("Actualizar", px + 20, py + 300 - 38, 100, 22, com.eric.revill.d.a.b::c));
   }

   private void b(int px, int py, CompoundTag guild) {
      int innerY = py + 56;
      int leftW = 347;
      int chatY = py + 300 - 26 - 2;
      this.w = this.a(px + 6 + 2, chatY, leftW - 72, 20, "Escribe a la hermandad...");
      this.addRenderableWidget(this.w);
      this.addRenderableWidget(this.a("Enviar", px + leftW - 64, chatY, 58, 20, () -> {
         CompoundTag data = new CompoundTag();
         data.putString("message", this.w.getValue());
         data.putString("privateTarget", "");
         this.a(4, data);
         this.w.setValue("");
      }));
      this.addRenderableWidget(this.b("‹", "Anterior", px + 560 - 56, innerY + 2, 24, 18, () -> this.a(guild, 7, -1)));
      this.addRenderableWidget(this.b("›", "Siguiente", px + 560 - 30, innerY + 2, 24, 18, () -> this.a(guild, 7, 1)));
   }

   private void c(int px, int py, CompoundTag guild) {
      ListTag posts = guild.getList("posts", 10);

      int pageStart;
      for (int i2 = pageStart = this.r * 5; i2 < Math.min(posts.size(), pageStart + 5); i2++) {
         CompoundTag post = posts.getCompound(i2);
         if (post.getString("id").equals(this.u)) {
            int popupX = px + 280 - 130;
            int popupY = py + 300 - 26 - 78;
            if (guild.getBoolean("canManage") || post.getString("author").equals(this.minecraft.player.getName().getString())) {
               String postId = this.u;
               this.addRenderableWidget(this.a("Borrar", popupX + 208, popupY + 8, 62, 18, () -> {
                  CompoundTag data = new CompoundTag();
                  data.putString("id", postId);
                  this.u = null;
                  this.a(6, data);
               }));
            }
            break;
         }
      }

      int botY = py + 300 - 26 - 2;
      this.w = this.a(px + 6 + 2, botY, 418, 20, "Mensaje...");
      this.addRenderableWidget(this.w);
      this.addRenderableWidget(this.a("Publicar anuncio", px + 560 - 136, botY, 130, 20, () -> {
         CompoundTag data = new CompoundTag();
         data.putString("message", this.w.getValue());
         this.a(5, data);
         this.w.setValue("");
      }));
      this.a(px, py, posts.size(), 5);
   }

   private void d(int px, int py, CompoundTag guild) {
      ListTag locations = guild.getList("locations", 10);
      int innerY = py + 56;

      int pageStart;
      for (int i2 = pageStart = this.r * 4; i2 < Math.min(locations.size(), pageStart + 4); i2++) {
         CompoundTag loc = locations.getCompound(i2);
         String id = loc.getString("id");
         boolean vis = loc.getBoolean("visible");
         this.addRenderableWidget(this.a(loc.getString("name"), px + 6 + 2, innerY + 4 + (i2 - pageStart) * 22, 270, 20, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("id", id);
            data.putBoolean("visible", !vis);
            this.a(9, data);
         }));
         if (guild.getBoolean("canManage") || loc.getString("author").equals(this.minecraft.player.getUUID().toString())) {
            this.addRenderableWidget(this.a("Borrar", px + 6 + 278, innerY + 4 + (i2 - pageStart) * 22, 70, 20, () -> {
               CompoundTag data = new CompoundTag();
               data.putString("id", id);
               this.a(8, data);
            }));
         }
      }

      int botY = py + 300 - 26 - 2;
      this.addRenderableWidget(this.a(this.a(this.s), px + 6 + 2, botY, 50, 20, () -> {
         this.s = this.b(this.s);
         this.init();
      }));
      this.w = this.a(px + 6 + 56, botY, 390, 20, "Nombre ubicación...");
      this.addRenderableWidget(this.w);
      this.addRenderableWidget(this.a("Guardar", px + 560 - 6 - 100, botY, 96, 20, () -> {
         CompoundTag data = new CompoundTag();
         data.putString("name", this.w.getValue());
         data.putString("icon", this.s);
         this.a(7, data);
         this.w.setValue("");
      }));
      this.a(px, py, locations.size(), 4);
   }

   private void e(int px, int py, CompoundTag guild) {
      ListTag banners = guild.getList("banners", 10);
      int innerY = py + 56 + 34;
      int cols = 6;
      int rows = 3;
      int slotW = 50;
      int slotH = 22;
      int gridW = cols * slotW + (cols - 1) * 2;
      int gridX = px + (560 - gridW) / 2;

      int pageStart;
      for (int i2 = pageStart = this.p * cols * rows; i2 < Math.min(banners.size(), pageStart + cols * rows); i2++) {
         int index = i2;
         int local = i2 - pageStart;
         int col = local % cols;
         int row = local / cols;
         int bx = gridX + col * (slotW + 2);
         int by = innerY + row * (slotH + 2);
         this.addRenderableWidget(this.a(Integer.toString(i2 + 1), bx, by, slotW, slotH, () -> {
            this.A = index;
            this.init();
         }));
      }

      int midY = innerY + rows * (slotH + 2) + 6;
      int btnW = 90;
      this.addRenderableWidget(this.a("Activar", px + 6 + 40, midY, btnW, 20, () -> {
         if (this.A >= 0 && this.A < banners.size()) {
            CompoundTag data = new CompoundTag();
            data.putInt("index", this.A);
            this.a(11, data);
         }
      }));
      this.addRenderableWidget(this.a("Recuperar", px + 6 + 140, midY, btnW, 20, () -> {
         if (this.A >= 0 && this.A < banners.size()) {
            CompoundTag data = new CompoundTag();
            data.putInt("index", this.A);
            data.putBoolean("delete", true);
            this.a(12, data);
            this.A = -1;
         }
      }));
      this.addRenderableWidget(this.a("Borrar", px + 6 + 240, midY, btnW, 20, () -> {
         if (this.A >= 0 && this.A < banners.size()) {
            CompoundTag data = new CompoundTag();
            data.putInt("index", this.A);
            data.putBoolean("delete", false);
            this.a(12, data);
            this.A = -1;
         }
      }));
      int botY = py + 300 - 26 - 2;
      this.addRenderableWidget(this.a("Quitar activo", px + 6 + 2, botY, 110, 20, () -> {
         CompoundTag data = new CompoundTag();
         data.putInt("index", -1);
         this.a(11, data);
      }));
      this.addRenderableWidget(this.a("Guardar diseño en mano", px + 560 - 6 - 180, botY, 176, 20, () -> this.a(10, new CompoundTag())));
      this.addRenderableWidget(this.b("‹", "Anterior", px + 560 - 58, py + 56 + 2, 24, 18, () -> {
         this.p = Math.max(0, this.p - 1);
         this.init();
      }));
      this.addRenderableWidget(this.b("›", "Siguiente", px + 560 - 30, py + 56 + 2, 24, 18, () -> {
         this.p = Math.min(Math.max(0, (banners.size() - 1) / (cols * rows)), this.p + 1);
         this.init();
      }));
   }

   private void f(int px, int py, CompoundTag guild) {
      int innerY = py + 56;
      int halfW = 268;
      if (guild.getBoolean("canManage")) {
         this.w = this.a(px + 6 + 2, innerY + 40, halfW - 90, 20, "Jugador...");
         this.addRenderableWidget(this.w);
         this.addRenderableWidget(this.a("Invitar", px + 6 + halfW - 84, innerY + 40, 80, 20, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("target", this.w.getValue());
            this.a(1, data);
         }));
         this.x = this.a(px + 6 + 2, innerY + 69, 110, 20, "Mensaje MOTD...");
         this.addRenderableWidget(this.x);
         this.addRenderableWidget(this.a("Fijar", px + 6 + 118, innerY + 69, 60, 20, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("motd", this.x.getValue());
            this.a(15, data);
         }));
         this.addRenderableWidget(this.a("Limpiar", px + 6 + 182, innerY + 69, 68, 20, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("motd", "");
            this.a(15, data);
            this.x.setValue("");
         }));
         this.y = this.a(px + 6 + 2, innerY + 98, 120, 20, "Nuevo nombre...");
         this.addRenderableWidget(this.y);
         this.addRenderableWidget(this.a("Renombrar", px + 6 + 126, innerY + 98, 100, 20, () -> {
            CompoundTag data = new CompoundTag();
            data.putString("name", this.y.getValue());
            this.a(13, data);
         }));
      }

      int botY = py + 300 - 26 - 2;
      this.addRenderableWidget(this.a("Abandonar hermandad", px + 6 + 2, botY, halfW - 2, 20, () -> this.a(18, new CompoundTag())));
      int rightX = px + 280 + 6;
      ListTag members = guild.getList("members", 10);

      int pageStart;
      for (int i2 = pageStart = this.q * 4; i2 < Math.min(members.size(), pageStart + 4); i2++) {
         CompoundTag member = members.getCompound(i2);
         UUID id = UUID.fromString(member.getString("uuid"));
         int rowY = innerY + 18 + (i2 - pageStart) * 43;
         boolean isFounder = id.toString().equals(guild.getString("founder"));
         boolean canManageMember = guild.getBoolean("canManage") && !isFounder;
         boolean canTransferLeadership = guild.getBoolean("isFounder") && !isFounder;
         int controlCount = (canManageMember ? 2 : 0) + (canTransferLeadership ? 1 : 0);
         int controlWidth = (canManageMember ? 106 : 0) + (canTransferLeadership ? 62 : 0) + Math.max(0, controlCount - 1) * 4;
         int controlX = rightX + Math.max(4, (halfW - controlWidth) / 2);
         if (canManageMember) {
            this.addRenderableWidget(this.a("Expulsar", "Expulsa a este integrante de la hermandad.", controlX, rowY + 18, 58, 14, () -> {
               CompoundTag data = new CompoundTag();
               data.putString("uuid", id.toString());
               this.a(17, data);
            }));
            controlX += 62;
         }

         if (canTransferLeadership) {
            this.addRenderableWidget(this.a("Transferir", "Transfiere el liderazgo de la hermandad a este integrante.", controlX, rowY + 18, 62, 14, () -> {
               CompoundTag data = new CompoundTag();
               data.putString("uuid", id.toString());
               this.a(19, data);
            }));
            controlX += 66;
         }

         if (canManageMember) {
            String permLabel = member.getBoolean("founder") ? "Quitar" : "Dar";
            String permissionTooltip = member.getBoolean("founder")
               ? "Retira a este integrante sus permisos de gestión."
               : "Concede a este integrante permisos de gestión.";
            this.addRenderableWidget(this.a(permLabel, permissionTooltip, controlX, rowY + 18, 48, 14, () -> {
               CompoundTag data = new CompoundTag();
               data.putString("uuid", id.toString());
               data.putBoolean("enabled", !member.getBoolean("founder"));
               this.a(16, data);
            }));
         }
      }

      this.addRenderableWidget(this.b("‹", "Anterior", rightX + halfW - 56, botY, 24, 20, () -> this.a(guild, 4, -1)));
      this.addRenderableWidget(this.b("›", "Siguiente", rightX + halfW - 28, botY, 24, 20, () -> this.a(guild, 4, 1)));
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      this.renderBackground(context, mouseX, mouseY, delta);
      int px = this.a();
      int py = this.b();
      CompoundTag snapshot = com.eric.revill.d.a.b.b();
      CompoundTag guild = snapshot.getCompound("guild");
      int accentRaw = com.eric.revill.d.a.b.d();
      int accent = (accentRaw != 0 ? accentRaw : this.n) | 0xFF000000;
      this.a(context, px, py, accent, snapshot, guild);
      if (snapshot.getBoolean("hasGuild")) {
         context.fill(px + 6, py + 56 - 1, px + 560 - 6, py + 56, -12959152);
         int iy = py + 56;
         int ih = 218;
         context.fill(px + 6, iy, px + 560 - 6, iy + ih, -15920873);
         context.fill(px + 6, iy + ih, px + 560 - 6, py + 300 - 4, -15393756);
         int tx = px + 6 + this.m * 27;
         context.fill(tx, py + 29 + 22 - 1, tx + 26, py + 29 + 22 + 1, accent);
         switch (this.m) {
            case 0:
               this.b(context, guild, px, py);
               break;
            case 1:
               this.c(context, guild, px, py);
               break;
            case 2:
               this.d(context, guild, px, py);
               break;
            case 3:
               this.e(context, guild, px, py);
               break;
            case 4:
               this.f(context, guild, px, py);
         }
      } else {
         this.a(context, snapshot, px, py);
      }

      super.render(context, mouseX, mouseY, delta);
      if (snapshot.getBoolean("hasGuild")) {
         this.b(context, px, py);
      }

      if (this.t && snapshot.getBoolean("hasGuild") && guild.getBoolean("canManage")) {
         this.a(context, px, py);
      }
   }

   private void b(GuiGraphics ctx, int px, int py) {
      for (int i2 = 0; i2 < z.length; i2++) {
         int tx = px + 6 + i2 * 27;
         int ty = py + 29;
         ctx.renderItem(new ItemStack(z[i2]), tx + 6, ty + 3);
      }
   }

   private void a(GuiGraphics ctx, int px, int py, int accent, CompoundTag snapshot, CompoundTag guild) {
      ctx.fill(px, py, px + 560, py + 300, -15064784);
      ctx.fill(px, py, px + 560, py + 3, accent);
      ctx.fill(px, py + 300 - 3, px + 560, py + 300, accent);
      ctx.fill(px, py + 3, px + 3, py + 300 - 3, -8418923);
      ctx.fill(px + 560 - 3, py + 3, px + 560, py + 300 - 3, -8418923);

      for (int i2 = 0; i2 < 4; i2++) {
         ctx.fill(px + 1, py + 32 + i2 * 8, px + 3, py + 36 + i2 * 8, -12959152);
         ctx.fill(px + 560 - 3, py + 32 + i2 * 8, px + 560 - 1, py + 36 + i2 * 8, -12959152);
      }

      ctx.fill(px + 3, py + 3, px + 560 - 3, py + 26, -14800070);
      boolean hasGuild = snapshot.getBoolean("hasGuild");
      String guildName = hasGuild ? guild.getString("name") : "Hermandad";
      int nameW = this.font.width(guildName);
      ctx.fill(px + 280 - nameW / 2 - 10, py + 8, px + 280 - nameW / 2 - 4, py + 14, accent);
      ctx.drawCenteredString(this.font, Component.literal(guildName), px + 280, py + 8, accent);
      int bx = px + 280 - 26;
      int banY = py - 38 + 3;
      ctx.fill(bx - 2, banY, bx + 52 + 2, py + 3, -15064784);
      ctx.fill(bx - 2, banY, bx + 52 + 2, banY + 2, accent);
      ctx.fill(bx - 2, banY + 2, bx, py + 3, -8418923);
      ctx.fill(bx + 52, banY + 2, bx + 52 + 2, py + 3, -8418923);
      ctx.fill(bx, banY + 2, bx + 52, py + 3, -15920873);
      if (hasGuild) {
         ListTag banners = guild.getList("banners", 10);
         int active = guild.getInt("activeBanner");
         if (active >= 0 && active < banners.size()) {
            this.a(ctx, ItemStack.parseOptional(Minecraft.getInstance().level.registryAccess(), banners.getCompound(active)), bx + 26 - 8, banY + 10);
         } else {
            ctx.drawCenteredString(this.font, Component.literal("Sin estandarte"), bx + 26, banY + 14, -7829368);
         }
      } else {
         ctx.drawCenteredString(this.font, Component.literal("No hay estandarte"), bx + 26, banY + 14, -7829368);
      }
   }

   private void a(GuiGraphics ctx, CompoundTag snapshot, int px, int py) {
      CompoundTag invite = snapshot.getCompound("invite");
      String msg = invite.isEmpty()
         ? "Crea una hermandad para comenzar."
         : invite.getString("inviter") + " te invita a << " + invite.getString("name") + " >>";
      ctx.drawCenteredString(this.font, Component.literal(msg), px + 280, py + 26 + 20, 16777215);
      ctx.fill(px + 14, py + 26 + 36, px + 30, py + 26 + 52, this.n | 0xFF000000);
   }

   private void b(GuiGraphics ctx, CompoundTag guild, int px, int py) {
      int innerY = py + 56;
      int innerH = 218;
      int leftW = 347;
      int rightX = px + leftW + 6;
      int rightW = 560 - leftW - 12;
      ctx.renderOutline(px + 6, innerY, leftW - 6, innerH, -14011067);
      String motd = guild.getString("motd");
      ctx.drawString(this.font, Component.literal(motd.isEmpty() ? "Welcome to the Family" : motd), px + 6 + 4, innerY + 4, -1);
      int logH = innerH - 26;
      ctx.fill(px + 6 + 2, innerY + 14, px + leftW - 2, innerY + 14 + logH, -16249580);
      ctx.renderOutline(px + 6 + 2, innerY + 14, leftW - 6 - 2, logH, -13746350);
      ListTag log = guild.getList("log", 10);
      int maxLines = logH / 11 - 1;

      int start;
      for (int i2 = start = Math.max(0, log.size() - maxLines); i2 < log.size(); i2++) {
         CompoundTag entry = log.getCompound(i2);
         String logText = entry.getString("sender") + ": " + entry.getString("message");
         ctx.drawString(
            this.font,
            Component.literal(this.font.plainSubstrByWidth(logText, leftW - 16)),
            px + 6 + 5,
            innerY + 17 + (i2 - start) * 11,
            entry.getBoolean("private") ? -3364097 : -3090723,
            false
         );
      }

      ctx.renderOutline(rightX, innerY, rightW, innerH, -14011067);
      ListTag members = guild.getList("members", 10);
      ctx.drawString(this.font, Component.literal("En linea: " + this.a(members) + "/" + members.size()), rightX + 4, innerY + 4, -11141121, false);

      int pageStart;
      for (int i3 = pageStart = this.q * 7; i3 < Math.min(members.size(), pageStart + 7); i3++) {
         CompoundTag member = members.getCompound(i3);
         UUID id = UUID.fromString(member.getString("uuid"));
         int rowY = innerY + 18 + (i3 - pageStart) * 14;
         this.a(ctx, id, rightX + 2, rowY - 2);
         boolean isLeader = member.getBoolean("founder");
         ctx.drawString(this.font, Component.literal(isLeader ? "* " : "+ "), rightX + 20, rowY, isLeader ? -10240 : -11141291, false);
         ctx.drawString(
            this.font, Component.literal(member.getString("name")), rightX + 30, rowY, member.getBoolean("online") ? -11141291 : -7829368, false
         );
      }
   }

   private void c(GuiGraphics ctx, CompoundTag guild, int px, int py) {
      int innerY = py + 56;
      int innerH = 218;
      ListTag posts = guild.getList("posts", 10);
      ctx.drawString(this.font, Component.literal(posts.size() + "/16"), px + 560 - 6 - 30, innerY + 4, -11141121, false);
      ctx.fill(px + 6, innerY, px + 560 - 6, innerY + innerH, -15920873);
      ctx.renderOutline(px + 6, innerY, 548, innerH, -14011067);
      int pageStart = this.r * 5;
      int selectedIndex = -1;

      for (int i2 = pageStart; i2 < Math.min(posts.size(), pageStart + 5); i2++) {
         CompoundTag post = posts.getCompound(i2);
         int localIndex = i2 - pageStart;
         int[] bounds = this.a(px, innerY, localIndex);
         int postColor = post.contains("color") ? post.getInt("color") : v[0];
         ctx.fill(bounds[0], bounds[1], bounds[0] + bounds[2], bounds[1] + bounds[3], -2147483648 | postColor & 16777215);
         ctx.renderOutline(bounds[0], bounds[1], bounds[2], bounds[3], post.getString("id").equals(this.u) ? -12792065 : -13620963);
         ctx.drawString(this.font, Component.literal(post.getString("author")), bounds[0] + 7, bounds[1] + 6, -14080741, false);
         int textY = bounds[1] + 19;

         for (String line : this.a(post.getString("message"), bounds[2] - 14)) {
            if (textY >= bounds[1] + bounds[3] - 5) {
               break;
            }

            ctx.drawString(this.font, Component.literal(line), bounds[0] + 7, textY, -14080741, false);
            textY += 10;
         }

         if (post.getString("id").equals(this.u)) {
            selectedIndex = i2;
         }
      }

      if (selectedIndex >= 0) {
         CompoundTag selected = posts.getCompound(selectedIndex);
         int popupX = px + 280 - 130;
         int popupY = innerY + innerH - 78;
         ctx.fill(popupX, popupY, popupX + 278, popupY + 72, -267380704);
         ctx.renderOutline(popupX, popupY, 278, 72, -12400660);
         ctx.drawString(this.font, Component.literal("Publicado por: " + selected.getString("author")), popupX + 9, popupY + 10, -1, false);
         ctx.drawString(this.font, Component.literal("Color:"), popupX + 9, popupY + 42, -3155744, false);

         for (int i3 = 0; i3 < v.length; i3++) {
            int x = popupX + 47 + i3 % 6 * 18;
            int y = popupY + 37 + i3 / 6 * 17;
            ctx.fill(x, y, x + 13, y + 13, v[i3]);
            ctx.renderOutline(x, y, 13, 13, -14080741);
         }
      } else if (this.u != null) {
         this.u = null;
      }
   }

   private int[] a(int px, int innerY, int index) {
      int column = index % 3;
      int row = index / 3;
      int size = 94 + index * 7 % 15;
      int left = px + 280 - 174 + column * 120;
      int top = innerY + 1 + row * 109;
      return new int[]{left, top, size, size};
   }

   private List<String> a(String message, int maxWidth) {
      ArrayList<String> lines = new ArrayList<>();
      StringBuilder line = new StringBuilder();

      for (String word : message.split("\\s+")) {
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

   private void d(GuiGraphics ctx, CompoundTag guild, int px, int py) {
      int innerY = py + 56;
      int innerH = 218;
      ListTag locations = guild.getList("locations", 10);
      ctx.drawString(this.font, Component.literal(locations.size() + "/16"), px + 560 - 6 - 30, innerY + 4, -11141121, false);
      ctx.fill(px + 6, innerY, px + 560 - 6, innerY + innerH, -15920873);
      ctx.renderOutline(px + 6, innerY, 548, innerH, -14011067);

      int pageStart;
      for (int i2 = pageStart = this.r * 4; i2 < Math.min(locations.size(), pageStart + 4); i2++) {
         CompoundTag loc = locations.getCompound(i2);
         int rowY = innerY + 5 + (i2 - pageStart) * 22;
         ctx.fill(px + 6 + 2, rowY, px + 560 - 6 - 76, rowY + 18, -15459034);
         ctx.renderOutline(px + 6 + 2, rowY, 470, 18, -13746350);
         String locationText = loc.getString("name") + "  (" + loc.getInt("x") + ", " + loc.getInt("y") + ", " + loc.getInt("z") + ")";
         ctx.drawString(this.font, Component.literal(this.font.plainSubstrByWidth(locationText, 460)), px + 6 + 5, rowY + 4, -1, false);
      }
   }

   private void e(GuiGraphics ctx, CompoundTag guild, int px, int py) {
      int innerY = py + 56;
      ListTag banners = guild.getList("banners", 10);
      ctx.drawString(this.font, Component.literal("Colección de estandartes"), px + 6 + 8, innerY, -11141121, false);
      int cols = 6;
      int rows = 3;
      int slotW = 50;
      int slotH = 22;
      int gridW = cols * slotW + (cols - 1) * 2;
      int gridX = px + (560 - gridW) / 2;
      int gridY = innerY + 34;

      for (int r = 0; r < rows; r++) {
         for (int c2 = 0; c2 < cols; c2++) {
            int sx = gridX + c2 * (slotW + 2);
            int sy = gridY + r * (slotH + 2);
            ctx.fill(sx, sy, sx + slotW, sy + slotH, -15459034);
            ctx.renderOutline(sx, sy, slotW, slotH, -13746350);
         }
      }

      int active = guild.getInt("activeBanner");

      int pageStart;
      for (int i2 = pageStart = this.p * cols * rows; i2 < Math.min(banners.size(), pageStart + cols * rows); i2++) {
         int local = i2 - pageStart;
         int col = local % cols;
         int row = local / cols;
         int sx = gridX + col * (slotW + 2);
         int sy = gridY + row * (slotH + 2);
         this.a(ctx, ItemStack.parseOptional(Minecraft.getInstance().level.registryAccess(), banners.getCompound(i2)), sx + slotW / 2 - 8, sy + 2);
         if (i2 == active || i2 == this.A) {
            ctx.renderOutline(sx, sy, slotW, slotH, i2 == this.A ? -11141121 : -10240);
         }
      }
   }

   private void f(GuiGraphics ctx, CompoundTag guild, int px, int py) {
      int innerY = py + 56;
      int innerH = 218;
      int halfW = 268;
      int rightX = px + 280 + 6;
      ctx.fill(px + 6, innerY, px + 280, innerY + innerH, -15920873);
      ctx.renderOutline(px + 6, innerY, halfW, innerH, -14011067);
      ctx.drawString(this.font, Component.literal("Fundador: " + this.a(guild)), px + 6 + 4, innerY + 5, -11141121, false);
      ctx.drawString(this.font, Component.literal("Banco: Nvl 1"), px + 6 + 4, innerY + 15, -171, false);
      ctx.drawString(this.font, Component.literal("Invitar jugador"), px + 6 + 4, innerY + 29, -5592406, false);
      ctx.drawString(this.font, Component.literal("MOTD"), px + 6 + 4, innerY + 58, -5592406, false);
      ctx.drawString(this.font, Component.literal("Nombre"), px + 6 + 4, innerY + 87, -5592406, false);
      ctx.fill(rightX, innerY, px + 560 - 6, innerY + innerH, -15920873);
      ctx.renderOutline(rightX, innerY, halfW, innerH, -14011067);
      ctx.drawString(this.font, Component.literal("Integrantes"), rightX + 4, innerY + 4, -11141121, false);
      ListTag members = guild.getList("members", 10);

      int pageStart;
      for (int i2 = pageStart = this.q * 4; i2 < Math.min(members.size(), pageStart + 4); i2++) {
         CompoundTag member = members.getCompound(i2);
         UUID id = UUID.fromString(member.getString("uuid"));
         int rowY = innerY + 18 + (i2 - pageStart) * 43;
         ctx.fill(rightX + 3, rowY - 2, rightX + halfW - 3, rowY + 36, -15459034);
         ctx.renderOutline(rightX + 3, rowY - 2, halfW - 6, 38, -14272698);
         this.a(ctx, id, rightX + 6, rowY);
         String name = (member.getBoolean("founder") ? "* " : "") + member.getString("name");
         int nameColor = member.getBoolean("online") ? -11141291 : -7829368;
         ctx.drawString(this.font, Component.literal(this.font.plainSubstrByWidth(name, halfW - 34)), rightX + 26, rowY + 4, nameColor, false);
      }
   }

   private int a() {
      return Math.max(10, this.width / 2 - 280);
   }

   private int b() {
      return Math.max(40, this.height / 2 - 150);
   }

   private Button a(String label, int x, int y, int w, int h2, Runnable action) {
      return new k.a(x, y, w, h2, Component.literal(label), action);
   }

   private Button a(String label, String tooltip, int x, int y, int w, int h2, Runnable action) {
      k.a button = new k.a(x, y, w, h2, Component.literal(label), action);
      button.setTooltip(Tooltip.create(Component.literal(tooltip)));
      return button;
   }

   private Button b(String icon, String tooltip, int x, int y, int w, int h2, Runnable action) {
      k.a button = new k.a(x, y, w, h2, Component.literal(icon), action);
      button.setTooltip(Tooltip.create(Component.literal(tooltip)));
      return button;
   }

   private EditBox a(int x, int y, int w, int h2, String placeholder) {
      EditBox widget = new EditBox(this.font, x, y, w, h2, Component.empty());
      widget.setMaxLength(240);
      widget.setHint(Component.literal(placeholder));
      return widget;
   }

   private void a(int px, int py, int size, int pageSize) {
      int navY = py + 300 - 26 - 22;
      this.addRenderableWidget(this.b("‹", "Anterior", px + 560 - 58, navY, 24, 18, () -> this.b(size, pageSize, -1)));
      this.addRenderableWidget(this.b("›", "Siguiente", px + 560 - 30, navY, 24, 18, () -> this.b(size, pageSize, 1)));
   }

   private void b(int size, int pageSize, int delta) {
      int pages = Math.max(1, (size + pageSize - 1) / pageSize);
      this.r = Math.max(0, Math.min(pages - 1, this.r + delta));
      this.init();
   }

   private void a(CompoundTag guild, int pageSize, int delta) {
      int total = guild.getList("members", 10).size();
      int pages = Math.max(1, (total + pageSize - 1) / pageSize);
      this.q = Math.max(0, Math.min(pages - 1, this.q + delta));
      this.init();
   }

   private void a(int action, CompoundTag data) {
      com.eric.revill.d.a.b.a(action, data);
      com.eric.revill.d.a.b.c();
   }

   private int a(ListTag members) {
      int count = 0;

      for (int i2 = 0; i2 < members.size(); i2++) {
         if (members.getCompound(i2).getBoolean("online")) {
            count++;
         }
      }

      return count;
   }

   private String a(CompoundTag guild) {
      ListTag members = guild.getList("members", 10);
      String founderId = guild.getString("founder");

      for (int i2 = 0; i2 < members.size(); i2++) {
         CompoundTag m2 = members.getCompound(i2);
         if (m2.getString("uuid").equals(founderId)) {
            return m2.getString("name");
         }
      }

      return "N/A";
   }

   private String a(String icon) {
      return switch (icon) {
         case "star" -> "*";
         case "flag" -> "F";
         case "warning" -> "!";
         default -> "o";
      };
   }

   private String b(String icon) {
      return switch (icon) {
         case "compass" -> "star";
         case "star" -> "flag";
         case "flag" -> "warning";
         default -> "compass";
      };
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         int px = this.a();
         int py = this.b();
         CompoundTag snapshot = com.eric.revill.d.a.b.b();
         CompoundTag guild = snapshot.getCompound("guild");
         if (this.t) {
            int pickerX = px + 280 - 39;
            int pickerY = py + 26 + 4;

            for (int i2 = 0; i2 < l.length; i2++) {
               int x = pickerX + i2 % 4 * 18;
               int y = pickerY + i2 / 4 * 18;
               if (mouseX >= (double)x && mouseX < (double)(x + 14) && mouseY >= (double)y && mouseY < (double)(y + 14)) {
                  CompoundTag data = new CompoundTag();
                  data.putInt("color", l[i2]);
                  this.a(14, data);
                  this.t = false;
                  return true;
               }
            }

            this.t = false;
         }

         if (snapshot.getBoolean("hasGuild") && guild.getBoolean("canManage")) {
            String guildName = guild.getString("name");
            int colorX = px + 280 - this.font.width(guildName) / 2 - 10;
            int colorY = py + 8;
            if (mouseX >= (double)(colorX - 2) && mouseX < (double)(colorX + 8) && mouseY >= (double)(colorY - 2) && mouseY < (double)(colorY + 8)) {
               this.t = true;
               return true;
            }
         }

         if (this.m == 1 && this.u != null) {
            int popupX = px + 280 - 130;
            int popupY = py + 300 - 26 - 78;

            for (int i2x = 0; i2x < v.length; i2x++) {
               int x = popupX + 47 + i2x % 6 * 18;
               int y = popupY + 37 + i2x / 6 * 17;
               if (mouseX >= (double)x && mouseX < (double)(x + 13) && mouseY >= (double)y && mouseY < (double)(y + 13)) {
                  CompoundTag data = new CompoundTag();
                  data.putString("id", this.u);
                  data.putInt("color", v[i2x]);
                  this.a(20, data);
                  return true;
               }
            }
         }
      }

      if (super.mouseClicked(mouseX, mouseY, button)) {
         return true;
      } else {
         if (button == 0 && this.m == 1) {
            int pxx = this.a();
            int innerY = this.b() + 56;
            CompoundTag guildx = com.eric.revill.d.a.b.b().getCompound("guild");
            ListTag posts = guildx.getList("posts", 10);

            int pageStart;
            for (int i3 = pageStart = this.r * 5; i3 < Math.min(posts.size(), pageStart + 5); i3++) {
               int[] bounds = this.a(pxx, innerY, i3 - pageStart);
               if (mouseX >= (double)bounds[0]
                  && mouseX < (double)(bounds[0] + bounds[2])
                  && mouseY >= (double)bounds[1]
                  && mouseY < (double)(bounds[1] + bounds[3])) {
                  this.u = posts.getCompound(i3).getString("id");
                  this.init();
                  return true;
               }
            }

            if (this.u != null) {
               this.u = null;
               this.init();
               return true;
            }
         }

         return false;
      }
   }

   public void tick() {
      super.tick();
      String sig = b(com.eric.revill.d.a.b.b());
      if (!sig.equals(this.o)) {
         this.init();
      }
   }

   private static String b(CompoundTag snapshot) {
      if (!snapshot.getBoolean("hasGuild")) {
         return "none:" + snapshot.getCompound("invite").getString("id");
      } else {
         CompoundTag guild = snapshot.getCompound("guild");
         StringBuilder sb = new StringBuilder(guild.getString("id"))
            .append(':')
            .append(guild.getList("members", 10).size())
            .append(':')
            .append(guild.getList("posts", 10).size())
            .append(':')
            .append(guild.getList("locations", 10).size())
            .append(':')
            .append(guild.getList("banners", 10).size())
            .append(':')
            .append(guild.getInt("activeBanner"))
            .append(':')
            .append(guild.getBoolean("canManage"))
            .append(':')
            .append(guild.getString("motd"));
         ListTag members = guild.getList("members", 10);

         for (int i2 = 0; i2 < members.size(); i2++) {
            sb.append(members.getCompound(i2).getBoolean("founder"));
         }

         ListTag locations = guild.getList("locations", 10);

         for (int i3 = 0; i3 < locations.size(); i3++) {
            sb.append(locations.getCompound(i3).getBoolean("visible"));
         }

         return sb.toString();
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class a extends Button {
      private a(int x, int y, int width, int height, Component message, Runnable action) {
         super(x, y, width, height, message, button -> action.run(), DEFAULT_NARRATION);
      }

      protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
         int x = this.getX();
         int y = this.getY();
         if (this.active && this.isHovered()) {
            context.fill(x, y, x + this.width, y + this.height, 808295374);
         }

         context.renderOutline(x, y, this.width, this.height, this.active ? -11887420 : -12234150);
         context.drawCenteredString(
            Minecraft.getInstance().font,
            this.getMessage(),
            x + this.width / 2,
            y + (this.height - 8) / 2,
            this.active ? (this.isHovered() ? -1 : -4330497) : -8944757
         );
      }
   }
}
