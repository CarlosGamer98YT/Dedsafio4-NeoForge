package com.eric.revill.k;

/**
 * Gestor del sistema de Gremios / Clanes de DEDSAFIO 4.
 * 
 * Permite a los jugadores crear gremios, invitar miembros, asignar un estandarte distintivo,
 * gestionar rangos y persistir los datos de equipos en el Scoreboard y SavedData del servidor.
 */

import com.eric.revill.RevillMod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.PlayerTeam;

public final class a {
   public static final int a = 24;
   public static final int b = 16;
   public static final int c = 16;
   private static final int d = 80;
   private static final String e = "dedsafio4_guilds";

   private a() {
   }

   /** Inicializa y sincroniza todos los gremios en el Scoreboard al arrancar el servidor */
   public static void a(MinecraftServer server) {
      a.f state = d(server);

      for (a.Guild guild : state.a.values()) {
         b(server, guild);
      }

      b(server);
      c(server);
   }

   /** Actualiza el nombre del jugador en el registro de su hermandad y le envía su estado */
   public static void a(ServerPlayer player) {
      a.Guild guild = a(player.getServer(), player.getUUID());
      if (guild != null) {
         a.d member = guild.h.get(player.getUUID());
         member.b = player.getName().getString();
         b(player.getServer(), guild);
         d(player.getServer()).setDirty();
      }

      b(player);
      b(player.getServer());
   }

   /** Envía el paquete de red al cliente con los datos de su gremio e invitaciones pendientes */
   public static void b(ServerPlayer player) {
      a.Guild guild = a(player.getServer(), player.getUUID());
      CompoundTag snapshot = new CompoundTag();
      snapshot.putBoolean("hasGuild", guild != null);
      if (guild != null) {
         snapshot.put("guild", a(guild, player));
      }

      CompoundTag invites = new CompoundTag();
      UUID invite = d(player.getServer()).b.get(player.getUUID());
      a.Guild invitedGuild;
      if (invite != null && (invitedGuild = d(player.getServer()).a.get(invite)) != null) {
         invites.putString("id", invite.toString());
         invites.putString("name", invitedGuild.b);
         invites.putString(
            "inviter",
            invitedGuild.h.values().stream().filter(member -> member.a.equals(invitedGuild.f)).map(member -> member.b).findFirst().orElse("Fundador")
         );
      }

      snapshot.put("invite", invites);
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeNbt(snapshot);
      ServerPlayNetworking.send(player, com.eric.revill.k.b.b, buf);
   }

   /** Difunde la actualización de la hermandad a todos sus integrantes conectados */
   public static void a(MinecraftServer server, a.Guild guild) {
      for (a.d member : guild.h.values()) {
         ServerPlayer player = server.getPlayerList().getPlayer(member.a);
         if (player != null) {
            b(player);
         }
      }

      b(server);
      c(server);
   }

   /** Sincroniza la lista de gremios y miembros con todos los clientes del servidor */
   public static void b(MinecraftServer server) {
      if (server != null) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         List<ServerPlayer> onlinePlayers = server.getPlayerList().getPlayers();
         buf.writeVarInt(onlinePlayers.size());

         for (ServerPlayer player : onlinePlayers) {
            a.Guild guild = a(server, player.getUUID());
            buf.writeUtf(player.getStringUUID(), 36);
            buf.writeBoolean(guild != null);
            if (guild != null) {
               buf.writeUtf(guild.b, 32);
               buf.writeInt(guild.d);
            }
         }

         for (ServerPlayer recipient : onlinePlayers) {
            ServerPlayNetworking.send(recipient, com.eric.revill.k.b.c, PacketByteBufs.copy(buf));
         }
      }
   }

   /** Registra los waypoints y marcas compartidas de todos los gremios en el gestor de waypoints */
   private static void c(MinecraftServer server) {
      ArrayList<com.eric.revill.t.a> waypoints = new ArrayList<>();

      for (a.Guild guild : d(server).a.values()) {
         for (a.b location : guild.l) {
            waypoints.add(new com.eric.revill.t.a("guild_" + guild.a + "_" + location.a, location.b, location.d, location.c, guild.d, location.e));
         }
      }

      com.eric.revill.t.b.a(server, waypoints);
   }

   /** Busca y devuelve la hermandad a la que pertenece un jugador por su UUID */
   public static a.Guild a(MinecraftServer server, UUID playerId) {
      for (a.Guild guild : d(server).a.values()) {
         if (guild.h.containsKey(playerId)) {
            return guild;
         }
      }

      return null;
   }

   /** Crea una nueva hermandad con el fundador, nombre y color especificados */
   public static void a(MinecraftServer server, ServerPlayer founder, String name, int color) {
      a(name, 32, "El nombre");
      if (!b(color)) {
         throw new IllegalArgumentException("Color de hermandad inválido.");
      } else if (a(server, founder.getUUID()) != null) {
         throw new IllegalArgumentException("Ya perteneces a una hermandad.");
      } else {
         a.f state = d(server);

         for (a.Guild guild : state.a.values()) {
            if (guild.b.equalsIgnoreCase(name.trim())) {
               throw new IllegalArgumentException("Ya existe una hermandad con ese nombre.");
            }
         }

         a.Guild guildx = new a.Guild(UUID.randomUUID());
         guildx.b = name.trim();
         guildx.d = color;
         guildx.f = founder.getUUID();
         guildx.h.put(founder.getUUID(), new a.d(founder.getUUID(), founder.getName().getString()));
         guildx.a("Sistema", "Se ha fundado la hermandad " + guildx.b + ".");
         state.a.put(guildx.a, guildx);
         state.setDirty();
         b(server, guildx);
         a(server, guildx);
         a(server, guildx, Component.literal(founder.getName().getString() + " ha fundado la hermandad."));
      }
   }

   /** Envía una invitación para unirse a la hermandad a un jugador objetivo */
   public static void a(MinecraftServer server, ServerPlayer inviter, String targetName) {
      a.Guild guild = c(server, inviter);
      ServerPlayer target = server.getPlayerList().getPlayerByName(targetName.trim());
      if (target == null) {
         throw new IllegalArgumentException("Ese jugador no está conectado.");
      } else if (guild.h.size() >= 24) {
         throw new IllegalArgumentException("La hermandad está llena.");
      } else if (a(server, target.getUUID()) != null) {
         throw new IllegalArgumentException("Ese jugador ya pertenece a una hermandad.");
      } else {
         d(server).b.put(target.getUUID(), guild.a);
         d(server).setDirty();
         target.displayClientMessage(
            Component.literal(inviter.getName().getString() + " te invita a la hermandad " + guild.b + ". Pulsa H para aceptar la invitación."), false
         );
         a(server, guild, Component.literal(inviter.getName().getString() + " ha invitado a " + target.getName().getString() + "."));
         b(target);
      }
   }

   public static void a(MinecraftServer server, ServerPlayer player) {
      UUID guildId = d(server).b.remove(player.getUUID());
      d(server).setDirty();
      a.Guild guild = guildId == null ? null : d(server).a.get(guildId);
      if (guild == null) {
         throw new IllegalArgumentException("No tienes una invitación pendiente.");
      } else if (a(server, player.getUUID()) != null) {
         throw new IllegalArgumentException("Ya perteneces a una hermandad.");
      } else if (guild.h.size() >= 24) {
         throw new IllegalArgumentException("La hermandad está llena.");
      } else {
         guild.h.put(player.getUUID(), new a.d(player.getUUID(), player.getName().getString()));
         guild.a("Sistema", player.getName().getString() + " se ha unido a la hermandad.");
         d(server).setDirty();
         b(server, guild);
         a(server, guild, Component.literal(player.getName().getString() + " se ha unido a la hermandad."));
         a(server, guild);
      }
   }

   public static void c(ServerPlayer player) {
      UUID guildId = d(player.getServer()).b.remove(player.getUUID());
      a.Guild guild = guildId == null ? null : d(player.getServer()).a.get(guildId);
      d(player.getServer()).setDirty();
      if (guild != null) {
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha rechazado la invitación."));
      }

      b(player);
   }

   public static void a(ServerPlayer sender, String message, String privateTarget) {
      a.Guild guild = b(sender.getServer(), sender);
      a(message, 240, "El mensaje");
      boolean isPrivate = privateTarget != null && !privateTarget.isBlank();
      a.d recipient = null;
      if (isPrivate && (recipient = guild.a(privateTarget)) == null) {
         throw new IllegalArgumentException("Ese jugador no pertenece a la hermandad.");
      } else {
         a.c entry = new a.c(
            sender.getName().getString(), isPrivate ? message.trim() + " (privado para " + recipient.b + ")" : message.trim(), isPrivate ? recipient.a : null
         );
         guild.a(entry);
         d(sender.getServer()).setDirty();
         if (!isPrivate) {
            MutableComponent chatMessage = Component.literal("[" + guild.b + "] ")
               .withStyle(ChatFormatting.GRAY)
               .append(Component.literal(sender.getName().getString() + ": ").withStyle(ChatFormatting.WHITE))
               .append(Component.literal(message.trim()));

            for (a.d member : guild.h.values()) {
               ServerPlayer recipientPlayer = sender.getServer().getPlayerList().getPlayer(member.a);
               if (recipientPlayer != null) {
                  recipientPlayer.displayClientMessage(chatMessage, false);
               }
            }

            a(sender.getServer(), guild);
         } else {
            ServerPlayer target = sender.getServer().getPlayerList().getPlayer(recipient.a);
            b(sender);
            if (target != null) {
               b(target);
            }
         }
      }
   }

   public static void a(ServerPlayer author, String message) {
      a.Guild guild = b(author.getServer(), author);
      a(message, 180, "El anuncio");
      if (guild.k.size() >= 16) {
         throw new IllegalArgumentException("El tablón ya tiene 16 anuncios.");
      } else {
         a.e post = new a.e(UUID.randomUUID().toString(), author.getName().getString(), message.trim(), -792671);
         guild.k.add(0, post);
         guild.a("Sistema", author.getName().getString() + " ha añadido un nuevo anuncio.");
         d(author.getServer()).setDirty();
         a(author.getServer(), guild, Component.literal(author.getName().getString() + " ha añadido un nuevo anuncio."));
         a(author.getServer(), guild);
      }
   }

   public static void b(ServerPlayer player, String postId) {
      a.Guild guild = b(player.getServer(), player);
      a.e post = guild.k
         .stream()
         .filter(value -> value.a.equals(postId))
         .findFirst()
         .orElseThrow(() -> new IllegalArgumentException("Ese anuncio ya no existe."));
      if (!a(guild, player.getUUID()) && !post.b.equals(player.getName().getString())) {
         throw new IllegalArgumentException("Solo puedes borrar tus anuncios; el fundador puede borrar todos.");
      } else {
         guild.k.remove(post);
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha eliminado un anuncio."));
         a(player.getServer(), guild);
      }
   }

   public static void a(ServerPlayer player, String postId, int color) {
      a.Guild guild = b(player.getServer(), player);
      if (!a(color)) {
         throw new IllegalArgumentException("El color del anuncio no es válido.");
      } else {
         int index = -1;

         for (int i2 = 0; i2 < guild.k.size(); i2++) {
            if (guild.k.get(i2).a.equals(postId)) {
               index = i2;
               break;
            }
         }

         if (index < 0) {
            throw new IllegalArgumentException("Ese anuncio ya no existe.");
         } else {
            a.e post = guild.k.get(index);
            guild.k.set(index, new a.e(post.a, post.b, post.c, color));
            d(player.getServer()).setDirty();
            a(player.getServer(), guild);
         }
      }
   }

   private static boolean a(int color) {
      return color == -792671
         || color == -870240
         || color == -3808859
         || color == -5777186
         || color == -4667416
         || color == -2638366
         || color == -736553
         || color == -1249295;
   }

   public static void b(ServerPlayer player, String name, String icon) {
      a.Guild guild = b(player.getServer(), player);
      a(name, 32, "El nombre de ubicación");
      if (icon == null || icon.length() > 32) {
         throw new IllegalArgumentException("El icono de ubicación no es válido.");
      } else if (guild.l.size() >= 16) {
         throw new IllegalArgumentException("La hermandad ya tiene 16 ubicaciones.");
      } else {
         a.b location = new a.b(
            UUID.randomUUID().toString(), name.trim(), icon, player.blockPosition(), player.level().dimension().location().toString(), player.getUUID()
         );
         guild.l.add(location);
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha añadido la ubicación " + location.b + "."));
         a(player.getServer(), guild);
      }
   }

   public static void c(ServerPlayer player, String locationId) {
      a.Guild guild = b(player.getServer(), player);
      a.b location = guild.l
         .stream()
         .filter(value -> value.a.equals(locationId))
         .findFirst()
         .orElseThrow(() -> new IllegalArgumentException("Esa ubicación no existe."));
      if (!a(guild, player.getUUID()) && !location.f.equals(player.getUUID())) {
         throw new IllegalArgumentException("Solo puedes borrar tus ubicaciones; el fundador puede borrar todas.");
      } else {
         guild.l.remove(location);
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha eliminado la ubicación " + location.b + "."));
         a(player.getServer(), guild);
      }
   }

   public static void a(ServerPlayer player, String locationId, boolean visible) {
      a.Guild guild = b(player.getServer(), player);
      if (guild.l.stream().noneMatch(location -> location.a.equals(locationId))) {
         throw new IllegalArgumentException("Esa ubicación no existe.");
      } else {
         Set hidden = guild.m.computeIfAbsent(player.getUUID(), ignored -> new HashSet<>());
         if (visible) {
            hidden.remove(locationId);
         } else {
            hidden.add(locationId);
         }

         d(player.getServer()).setDirty();
         a.b location2 = guild.l.stream().filter(value -> value.a.equals(locationId)).findFirst().orElseThrow();
         a(
            player.getServer(),
            guild,
            Component.literal(player.getName().getString() + (visible ? " ha mostrado" : " ha ocultado") + " la ubicación " + location2.b + ".")
         );
         b(player);
      }
   }

   public static void d(ServerPlayer player) {
      a.Guild guild = b(player.getServer(), player);
      ItemStack banner = player.getMainHandItem();
      if (!(banner.getItem() instanceof BannerItem) || banner.getCount() != 1) {
         throw new IllegalArgumentException("Sostén en la mano principal un estandarte diseñado (un único objeto).");
      } else if (guild.n.size() >= 18) {
         throw new IllegalArgumentException("La colección admite 18 estandartes.");
      } else {
         ItemStack copy = banner.copy();
         copy.setCount(1);
         guild.n.add(copy);
         banner.shrink(1);
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha añadido un estandarte."));
         a(player.getServer(), guild);
      }
   }

   public static void a(ServerPlayer player, int index) {
      a.Guild guild = b(player.getServer(), player);
      if (index >= -1 && index < guild.n.size()) {
         guild.g = index;
         d(player.getServer()).setDirty();
         a(
            player.getServer(),
            guild,
            Component.literal(player.getName().getString() + (index < 0 ? " ha quitado el estandarte activo." : " ha cambiado el estandarte activo."))
         );
         a(player.getServer(), guild);
      } else {
         throw new IllegalArgumentException("Selecciona un estandarte válido.");
      }
   }

   public static void a(ServerPlayer player, int index, boolean remove) {
      a.Guild guild = b(player.getServer(), player);
      if (index >= 0 && index < guild.n.size()) {
         ItemStack banner = guild.n.remove(index);
         if (remove && !player.getInventory().add(banner.copy())) {
            player.drop(banner.copy(), false);
         }

         if (guild.g == index) {
            guild.g = -1;
         } else if (guild.g > index) {
            guild.g--;
         }

         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha eliminado un estandarte."));
         a(player.getServer(), guild);
      } else {
         throw new IllegalArgumentException("Selecciona un estandarte válido.");
      }
   }

   public static void d(ServerPlayer player, String name) {
      a.Guild guild = c(player.getServer(), player);
      a(name, 32, "El nombre");

      for (a.Guild other : d(player.getServer()).a.values()) {
         if (other != guild && other.b.equalsIgnoreCase(name.trim())) {
            throw new IllegalArgumentException("Ya existe una hermandad con ese nombre.");
         }
      }

      String previousName = guild.b;
      guild.b = name.trim();
      guild.a("Sistema", player.getName().getString() + " cambió el nombre de la hermandad.");
      d(player.getServer()).setDirty();
      b(player.getServer(), guild);
      a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha cambiado el nombre de " + previousName + " a " + guild.b + "."));
      a(player.getServer(), guild);
   }

   public static void b(ServerPlayer player, int color) {
      a.Guild guild = c(player.getServer(), player);
      if (!b(color)) {
         throw new IllegalArgumentException("Color de hermandad inválido.");
      } else {
         guild.d = color;
         d(player.getServer()).setDirty();
         b(player.getServer(), guild);
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha cambiado el color de la hermandad."));
         a(player.getServer(), guild);
      }
   }

   public static void e(ServerPlayer player, String motd) {
      a.Guild guild = c(player.getServer(), player);
      if (motd != null && motd.length() <= 120) {
         guild.c = motd.trim();
         guild.a("Sistema", player.getName().getString() + " actualizó el MOTD.");
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha actualizado el mensaje de la hermandad."));
         a(player.getServer(), guild);
      } else {
         throw new IllegalArgumentException("El MOTD admite hasta 120 caracteres.");
      }
   }

   public static void a(ServerPlayer player, UUID targetId, boolean enabled) {
      a.Guild guild = c(player.getServer(), player);
      a.d member = guild.h.get(targetId);
      if (member != null && !targetId.equals(guild.f)) {
         if (enabled) {
            guild.i.add(targetId);
         } else {
            guild.i.remove(targetId);
         }

         guild.a("Sistema", member.b + (enabled ? " recibió permisos de fundador." : " dejó de tener permisos de fundador."));
         d(player.getServer()).setDirty();
         a(
            player.getServer(),
            guild,
            Component.literal(
               player.getName().getString() + (enabled ? " ha dado permisos de gestión a " : " ha quitado los permisos de gestión a ") + member.b + "."
            )
         );
         a(player.getServer(), guild);
      } else {
         throw new IllegalArgumentException("Selecciona otro integrante.");
      }
   }

   public static void a(ServerPlayer player, UUID targetId) {
      a.Guild guild = c(player.getServer(), player);
      if (targetId.equals(guild.f)) {
         throw new IllegalArgumentException("No puedes expulsar al fundador.");
      } else {
         a.d removed = guild.h.remove(targetId);
         if (removed == null) {
            throw new IllegalArgumentException("Ese jugador ya no pertenece a la hermandad.");
         } else {
            guild.i.remove(targetId);
            guild.a("Sistema", removed.b + " abandonó la hermandad.");
            d(player.getServer()).setDirty();
            a(player.getServer(), guild, removed.b, false);
            a(player.getServer(), guild, Component.literal(removed.b + " ha sido expulsado de la hermandad."));
            b(player);
            ServerPlayer target = player.getServer().getPlayerList().getPlayer(targetId);
            if (target != null) {
               b(target);
            }

            a(player.getServer(), guild);
         }
      }
   }

   public static void e(ServerPlayer player) {
      a.Guild guild = b(player.getServer(), player);
      if (player.getUUID().equals(guild.f)) {
         throw new IllegalArgumentException("El fundador debe transferir el liderazgo antes de abandonar.");
      } else {
         guild.h.remove(player.getUUID());
         guild.i.remove(player.getUUID());
         guild.m.remove(player.getUUID());
         guild.a("Sistema", player.getName().getString() + " abandonó la hermandad.");
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, player.getName().getString(), false);
         b(player);
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha abandonado la hermandad."));
         a(player.getServer(), guild);
      }
   }

   public static void b(ServerPlayer player, UUID targetId) {
      a.Guild guild = c(player.getServer(), player);
      if (!player.getUUID().equals(guild.f)) {
         throw new IllegalArgumentException("Solo el fundador original puede transferir el liderazgo.");
      } else if (guild.h.containsKey(targetId) && !targetId.equals(guild.f)) {
         guild.f = targetId;
         guild.i.add(targetId);
         d(player.getServer()).setDirty();
         a(player.getServer(), guild, Component.literal(player.getName().getString() + " ha transferido el liderazgo a " + guild.h.get(targetId).b + "."));
         a(player.getServer(), guild);
      } else {
         throw new IllegalArgumentException("Selecciona otro integrante.");
      }
   }

   public static void a(ServerPlayer player, int action, CompoundTag data) {
      try {
         switch (action) {
            case 0:
               a(player.getServer(), player, data.getString("name"), data.getInt("color"));
               break;
            case 1:
               a(player.getServer(), player, data.getString("target"));
               break;
            case 2:
               a(player.getServer(), player);
               break;
            case 3:
               c(player);
               break;
            case 4:
               a(player, data.getString("message"), data.getString("privateTarget"));
               break;
            case 5:
               a(player, data.getString("message"));
               break;
            case 6:
               b(player, data.getString("id"));
               break;
            case 7:
               b(player, data.getString("name"), data.getString("icon"));
               break;
            case 8:
               c(player, data.getString("id"));
               break;
            case 9:
               a(player, data.getString("id"), data.getBoolean("visible"));
               break;
            case 10:
               d(player);
               break;
            case 11:
               a(player, data.getInt("index"));
               break;
            case 12:
               a(player, data.getInt("index"), data.getBoolean("delete"));
               break;
            case 13:
               d(player, data.getString("name"));
               break;
            case 14:
               b(player, data.getInt("color"));
               break;
            case 15:
               e(player, data.getString("motd"));
               break;
            case 16:
               a(player, UUID.fromString(data.getString("uuid")), data.getBoolean("enabled"));
               break;
            case 17:
               a(player, UUID.fromString(data.getString("uuid")));
               break;
            case 18:
               e(player);
               break;
            case 19:
               b(player, UUID.fromString(data.getString("uuid")));
               break;
            case 20:
               a(player, data.getString("id"), data.getInt("color"));
               break;
            default:
               throw new IllegalArgumentException("Acción de hermandad desconocida.");
         }
      } catch (RuntimeException var4) {
         player.displayClientMessage(Component.literal("Hermandad: " + var4.getMessage()), false);
         b(player);
      }
   }

   static CompoundTag a(a.Guild guild, ServerPlayer viewer) {
      CompoundTag data = new CompoundTag();
      data.putString("id", guild.a.toString());
      data.putString("name", guild.b);
      data.putInt("color", guild.d);
      data.putString("motd", guild.c);
      data.putString("founder", guild.f.toString());
      data.putInt("bank", guild.e);
      data.putBoolean("canManage", guild.a(viewer.getUUID()));
      data.putBoolean("isFounder", viewer.getUUID().equals(guild.f));
      ListTag members = new ListTag();

      for (a.d d2 : guild.h.values()) {
         CompoundTag nbtCompound = new CompoundTag();
         nbtCompound.putString("uuid", d2.a.toString());
         nbtCompound.putString("name", d2.b);
         nbtCompound.putBoolean("online", viewer.getServer().getPlayerList().getPlayer(d2.a) != null);
         nbtCompound.putBoolean("founder", guild.a(d2.a));
         members.add(nbtCompound);
      }

      data.put("members", members);
      ListTag log = new ListTag();

      for (a.c c2 : guild.j) {
         if (c2.c == null || c2.c.equals(viewer.getUUID()) || c2.a.equals(viewer.getName().getString())) {
            CompoundTag value = new CompoundTag();
            value.putString("sender", c2.a);
            value.putString("message", c2.b);
            value.putBoolean("private", c2.c != null);
            log.add(value);
         }
      }

      data.put("log", log);
      ListTag nbtList = new ListTag();

      for (a.e post : guild.k) {
         CompoundTag value = new CompoundTag();
         value.putString("id", post.a);
         value.putString("author", post.b);
         value.putString("message", post.c);
         value.putInt("color", post.d);
         nbtList.add(value);
      }

      data.put("posts", nbtList);
      ListTag nbtList2 = new ListTag();
      Set hidden = guild.m.getOrDefault(viewer.getUUID(), Set.of());

      for (a.b location : guild.l) {
         CompoundTag value = new CompoundTag();
         value.putString("id", location.a);
         value.putString("name", location.b);
         value.putString("icon", location.c);
         value.putInt("x", location.d.getX());
         value.putInt("y", location.d.getY());
         value.putInt("z", location.d.getZ());
         value.putString("dimension", location.e);
         value.putString("author", location.f.toString());
         value.putBoolean("visible", !hidden.contains(location.a));
         nbtList2.add(value);
      }

      data.put("locations", nbtList2);
      ListTag banners = new ListTag();

      for (ItemStack banner : guild.n) {
         banners.add(banner.save(viewer.registryAccess(), new CompoundTag()));
      }

      data.put("banners", banners);
      data.putInt("activeBanner", guild.g);
      return data;
   }

   private static a.Guild b(MinecraftServer server, ServerPlayer player) {
      a.Guild guild = a(server, player.getUUID());
      if (guild == null) {
         throw new IllegalArgumentException("No perteneces a una hermandad.");
      } else {
         return guild;
      }
   }

   private static a.Guild c(MinecraftServer server, ServerPlayer player) {
      a.Guild guild = b(server, player);
      if (!guild.a(player.getUUID())) {
         throw new IllegalArgumentException("Necesitas permisos de fundador.");
      } else {
         return guild;
      }
   }

   private static boolean a(a.Guild guild, UUID id) {
      return guild.a(id);
   }

   private static void a(String value, int max, String label) {
      if (value == null || value.trim().isEmpty() || value.trim().length() > max) {
         throw new IllegalArgumentException(label + " debe tener entre 1 y " + max + " caracteres.");
      }
   }

   private static boolean b(int color) {
      int red = color >> 16 & 0xFF;
      int green = color >> 8 & 0xFF;
      int blue = color & 0xFF;
      return red + green + blue > 0 && red + green + blue < 765;
   }

   private static void a(MinecraftServer server, a.Guild guild, Component message) {
      MutableComponent chatMessage = Component.literal("[Hermandad] ").withStyle(ChatFormatting.AQUA).append(message);

      for (a.d member : guild.h.values()) {
         ServerPlayer player = server.getPlayerList().getPlayer(member.a);
         if (player != null) {
            player.displayClientMessage(chatMessage, false);
         }
      }
   }

   private static void b(MinecraftServer server, a.Guild guild) {
      ServerScoreboard scoreboard = server.getScoreboard();
      String teamName;
      PlayerTeam team = scoreboard.getPlayerTeam(teamName = "guild_" + guild.a.toString().replace("-", "").substring(0, 12));
      if (team == null) {
         team = scoreboard.addPlayerTeam(teamName);
      }

      team.setColor(c(guild.d));
      team.setPlayerPrefix(Component.empty());
      team.setPlayerSuffix(Component.empty());

      for (a.d member : guild.h.values()) {
         ServerPlayer online = server.getPlayerList().getPlayer(member.a);
         if (online != null) {
            scoreboard.addPlayerToTeam(online.getName().getString(), team);
         }
      }
   }

   private static void a(MinecraftServer server, a.Guild guild, String name, boolean add) {
      ServerScoreboard scoreboard = server.getScoreboard();
      PlayerTeam team = scoreboard.getPlayerTeam("guild_" + guild.a.toString().replace("-", "").substring(0, 12));
      if (team != null) {
         if (add) {
            scoreboard.addPlayerToTeam(name, team);
         } else {
            scoreboard.removePlayerFromTeam(name, team);
         }
      }
   }

   private static ChatFormatting c(int color) {
      int[][] palette = new int[][]{{16733525, 16755200, 16777045, 5635925}, {5636095, 5592575, 11141290, 16777215}};
      ChatFormatting[] formatting = new ChatFormatting[]{
         ChatFormatting.RED,
         ChatFormatting.GOLD,
         ChatFormatting.YELLOW,
         ChatFormatting.GREEN,
         ChatFormatting.AQUA,
         ChatFormatting.BLUE,
         ChatFormatting.LIGHT_PURPLE,
         ChatFormatting.WHITE
      };
      int bestIndex = 0;
      long bestDistance = Long.MAX_VALUE;

      for (int index = 0; index < formatting.length; index++) {
         int candidate = palette[index / 4][index % 4];
         long distance = a(color, candidate);
         if (distance < bestDistance) {
            bestDistance = distance;
            bestIndex = index;
         }
      }

      return formatting[bestIndex];
   }

   private static long a(int first, int second) {
      int red = (first >> 16 & 0xFF) - (second >> 16 & 0xFF);
      int green = (first >> 8 & 0xFF) - (second >> 8 & 0xFF);
      int blue = (first & 0xFF) - (second & 0xFF);
      return (long)red * (long)red + (long)green * (long)green + (long)blue * (long)blue;
   }

   private static a.f d(MinecraftServer server) {
      ServerLevel world = server.overworld();
      return world.getDataStorage().computeIfAbsent(f.FACTORY, "dedsafio4_guilds");
   }

   private static final class Guild {
      private final UUID a;
      private String b = "";
      private String c = "";
      private int d = 5636095;
      private int e;
      private UUID f;
      private int g = -1;
      private final Map<UUID, a.d> h = new HashMap<>();
      private final Set<UUID> i = new HashSet<>();
      private final List<a.c> j = new ArrayList<>();
      private final List<a.e> k = new ArrayList<>();
      private final List<a.b> l = new ArrayList<>();
      private final Map<UUID, Set<String>> m = new HashMap<>();
      private final List<ItemStack> n = new ArrayList<>();

      private Guild(UUID id) {
         this.a = id;
      }

      private void a(String sender, String message) {
         this.a(new a.c(sender, message, null));
      }

      private void a(a.c entry) {
         this.j.add(entry);

         while (this.j.size() > 80) {
            this.j.remove(0);
         }
      }

      private a.d a(String name) {
         return this.h.values().stream().filter(member -> member.b.equalsIgnoreCase(name)).findFirst().orElse(null);
      }

      private boolean a(UUID id) {
         return this.f.equals(id) || this.i.contains(id);
      }

      private CompoundTag a(net.minecraft.core.HolderLookup.Provider registries) {
         CompoundTag nbt = new CompoundTag();
         nbt.putString("id", this.a.toString());
         nbt.putString("name", this.b);
         nbt.putString("motd", this.c);
         nbt.putInt("color", this.d);
         nbt.putInt("bank", this.e);
         nbt.putString("founder", this.f.toString());
         nbt.putInt("activeBanner", this.g);
         ListTag membersList = new ListTag();
         this.h.values().forEach(member -> {
            CompoundTag value = new CompoundTag();
            value.putString("id", member.a.toString());
            value.putString("name", member.b);
            value.putBoolean("founder", this.i.contains(member.a));
            membersList.add(value);
         });
         nbt.put("members", membersList);
         ListTag logs = new ListTag();
         this.j.forEach(entry -> {
            CompoundTag value = new CompoundTag();
            value.putString("sender", entry.a);
            value.putString("message", entry.b);
            if (entry.c != null) {
               value.putString("privateTo", entry.c.toString());
            }

            logs.add(value);
         });
         nbt.put("log", logs);
         ListTag postsList = new ListTag();
         this.k.forEach(post -> {
            CompoundTag value = new CompoundTag();
            value.putString("id", post.a);
            value.putString("author", post.b);
            value.putString("message", post.c);
            value.putInt("color", post.d);
            postsList.add(value);
         });
         nbt.put("posts", postsList);
         ListTag locationsList = new ListTag();
         this.l.forEach(location -> {
            CompoundTag value = new CompoundTag();
            value.putString("id", location.a);
            value.putString("name", location.b);
            value.putString("icon", location.c);
            value.putLong("pos", location.d.asLong());
            value.putString("dimension", location.e);
            value.putString("author", location.f.toString());
            locationsList.add(value);
         });
         nbt.put("locations", locationsList);
         ListTag bannersList = new ListTag();
         this.n.forEach(banner -> bannersList.add(banner.save(registries, new CompoundTag())));
         nbt.put("banners", bannersList);
         ListTag hiddenList = new ListTag();
         this.m.forEach((playerId, hidden) -> hidden.forEach(location -> {
               CompoundTag value = new CompoundTag();
               value.putString("player", playerId.toString());
               value.putString("location", location);
               hiddenList.add(value);
            }));
         nbt.put("hidden", hiddenList);
         return nbt;
      }

      private static a.Guild a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         try {
            a.Guild guild = new a.Guild(UUID.fromString(nbt.getString("id")));
            guild.b = nbt.getString("name");
            guild.c = nbt.getString("motd");
            guild.d = nbt.getInt("color");
            guild.e = Math.max(0, nbt.getInt("bank"));
            guild.f = UUID.fromString(nbt.getString("founder"));
            guild.g = nbt.getInt("activeBanner");
            ListTag membersList = nbt.getList("members", 10);

            for (int i2 = 0; i2 < membersList.size(); i2++) {
               CompoundTag memberNbt = membersList.getCompound(i2);
               a.d member = new a.d(UUID.fromString(memberNbt.getString("id")), memberNbt.getString("name"));
               guild.h.put(member.a, member);
               if (memberNbt.getBoolean("founder")) {
                  guild.i.add(member.a);
               }
            }

            ListTag logList = nbt.getList("log", 10);

            for (int i3 = 0; i3 < logList.size(); i3++) {
               CompoundTag value = logList.getCompound(i3);
               UUID privateTo = value.hasUUID("privateTo") ? value.getUUID("privateTo") : null;
               guild.a(new a.c(value.getString("sender"), value.getString("message"), privateTo));
            }

            ListTag postsList = nbt.getList("posts", 10);

            for (int i4 = 0; i4 < postsList.size() && i4 < 16; i4++) {
               CompoundTag value = postsList.getCompound(i4);
               guild.k
                  .add(
                     new a.e(
                        value.getString("id"),
                        value.getString("author"),
                        value.getString("message"),
                        value.contains("color") ? value.getInt("color") : -792671
                     )
                  );
            }

            ListTag locationsList = nbt.getList("locations", 10);

            for (int i5 = 0; i5 < locationsList.size() && i5 < 16; i5++) {
               CompoundTag value = locationsList.getCompound(i5);
               guild.l
                  .add(
                     new a.b(
                        value.getString("id"),
                        value.getString("name"),
                        value.getString("icon"),
                        BlockPos.of(value.getLong("pos")),
                        value.getString("dimension"),
                        UUID.fromString(value.getString("author"))
                     )
                  );
            }

            ListTag bannerList = nbt.getList("banners", 10);

            for (int i6 = 0; i6 < bannerList.size() && i6 < 18; i6++) {
               guild.n.add(ItemStack.parseOptional(registries, bannerList.getCompound(i6)));
            }

            ListTag hiddenList = nbt.getList("hidden", 10);

            for (int i7 = 0; i7 < hiddenList.size(); i7++) {
               CompoundTag value = hiddenList.getCompound(i7);
               guild.m.computeIfAbsent(value.getUUID("player"), ignored -> new HashSet<>()).add(value.getString("location"));
            }

            return guild;
         } catch (IllegalArgumentException var10) {
            RevillMod.b.warn("Se ha omitido una hermandad con datos inválidos.", var10);
            return null;
         }
      }
   }

   private static record b(String a, String b, String c, BlockPos d, String e, UUID f) {
   }

   private static record c(String a, String b, UUID c) {
   }

   private static final class d {
      private final UUID a;
      private String b;

      private d(UUID id, String name) {
         this.a = id;
         this.b = name;
      }
   }

   private static record e(String a, String b, String c, int d) {
   }

   private static final class f extends SavedData {
      public static final SavedData.Factory<f> FACTORY = new SavedData.Factory<>(f::new, f::a);
      private final Map<UUID, Guild> a = new HashMap<>();
      private final Map<UUID, UUID> b = new HashMap<>();

      private static f a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         f state = new f();
         ListTag guilds = nbt.getList("guilds", 10);

         for (int index = 0; index < guilds.size(); index++) {
            Guild guild = Guild.a(guilds.getCompound(index), registries);
            if (guild != null) {
               state.a.put(guild.a, guild);
            }
         }

         ListTag invites = nbt.getList("invites", 10);

         for (int indexx = 0; indexx < invites.size(); indexx++) {
            CompoundTag invite = invites.getCompound(indexx);
            if (invite.hasUUID("player") && invite.hasUUID("guild")) {
               state.b.put(invite.getUUID("player"), invite.getUUID("guild"));
            }
         }

         return state;
      }

      @Override
      public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         ListTag list = new ListTag();
         this.a.values().forEach(guild -> list.add(guild.a(registries)));
         nbt.put("guilds", list);
         ListTag inviteList = new ListTag();
         this.b.forEach((player, guild) -> {
            CompoundTag invite = new CompoundTag();
            invite.putUUID("player", player);
            invite.putUUID("guild", guild);
            inviteList.add(invite);
         });
         nbt.put("invites", inviteList);
         return nbt;
      }
   }
}
