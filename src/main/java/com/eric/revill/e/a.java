package com.eric.revill.e;

import com.eric.revill.RevillMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameType;

public final class a {
   private a() {
   }

   public static void a(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                                      "dedsafio4"
                                                   )
                                                   .requires(source -> source.hasPermission(4)))
                                                .executes(context -> {
                                                   ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayer();
                                                   if (player != null) {
                                                      com.eric.revill.g.a.b(player);
                                                   } else {
                                                      ((CommandSourceStack)context.getSource())
                                                         .sendSuccess(() -> Component.literal("Abre esta UI desde un jugador."), false);
                                                   }

                                                   return 1;
                                                }))
                                             .then(
                                                ((LiteralArgumentBuilder)Commands.literal("start").requires(source -> source.hasPermission(4)))
                                                   .executes(context -> {
                                                      com.eric.revill.j.a.a.a(((CommandSourceStack)context.getSource()).getServer());
                                                      return 1;
                                                   })
                                             ))
                                          .then(((LiteralArgumentBuilder)Commands.literal("stop").requires(source -> source.hasPermission(4))).executes(context -> {
                                             com.eric.revill.j.a.a.a(((CommandSourceStack)context.getSource()).getServer(), true);
                                             ((CommandSourceStack)context.getSource())
                                                .sendSuccess(() -> Component.literal("El Evento Rojo ha sido cancelado."), false);
                                             return 1;
                                          })))
                                       .then(((LiteralArgumentBuilder)Commands.literal("status").requires(source -> source.hasPermission(4))).executes(context -> {
                                          com.eric.revill.j.a.a.a((CommandSourceStack)context.getSource());
                                          return 1;
                                       })))
                                    .then(
                                       ((LiteralArgumentBuilder)Commands.literal("announce").requires(source -> source.hasPermission(4)))
                                          .then(Commands.argument("mensaje", StringArgumentType.greedyString()).executes(context -> {
                                             String message = StringArgumentType.getString(context, "mensaje");
                                             MinecraftServer server = ((CommandSourceStack)context.getSource()).getServer();

                                             for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                                                player.displayClientMessage(Component.literal(message), false);
                                                player.playNotifySound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.MASTER, 1.0F, 1.0F);
                                             }

                                             com.eric.revill.p.a.b(server, "naranja", message);
                                             return 1;
                                          }))
                                    ))
                                 .then(
                                    ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fusion")
                                                .requires(source -> source.hasPermission(4)))
                                             .executes(context -> {
                                                a((CommandSourceStack)context.getSource(), !RevillMod.d);
                                                return 1;
                                             }))
                                          .then(Commands.literal("entrar").executes(context -> {
                                             a((CommandSourceStack)context.getSource(), true);
                                             return 1;
                                          })))
                                       .then(Commands.literal("salir").executes(context -> {
                                          a((CommandSourceStack)context.getSource(), false);
                                          return 1;
                                       }))
                                 ))
                              .then(
                                 ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bunker")
                                          .then(
                                             Commands.literal("set")
                                                .then(
                                                   Commands.argument("x1", IntegerArgumentType.integer())
                                                      .then(
                                                         Commands.argument("y1", IntegerArgumentType.integer())
                                                            .then(
                                                               Commands.argument("z1", IntegerArgumentType.integer())
                                                                  .then(
                                                                     Commands.argument("x2", IntegerArgumentType.integer())
                                                                        .then(
                                                                           Commands.argument("y2", IntegerArgumentType.integer())
                                                                              .then(
                                                                                 Commands.argument("z2", IntegerArgumentType.integer()).executes(context -> {
                                                                                    int x1 = IntegerArgumentType.getInteger(context, "x1");
                                                                                    int y1 = IntegerArgumentType.getInteger(context, "y1");
                                                                                    int z1 = IntegerArgumentType.getInteger(context, "z1");
                                                                                    int x2 = IntegerArgumentType.getInteger(context, "x2");
                                                                                    int y2 = IntegerArgumentType.getInteger(context, "y2");
                                                                                    int z2 = IntegerArgumentType.getInteger(context, "z2");
                                                                                    BlockPos pos1 = new BlockPos(x1, y1, z1);
                                                                                    BlockPos pos2 = new BlockPos(x2, y2, z2);
                                                                                    com.eric.revill.b.b.a(
                                                                                       ((CommandSourceStack)context.getSource()).getServer(), pos1, pos2
                                                                                    );
                                                                                    ((CommandSourceStack)context.getSource())
                                                                                       .sendSuccess(() -> Component.literal("Búnker actualizado."), false);
                                                                                    return 1;
                                                                                 })
                                                                              )
                                                                        )
                                                                  )
                                                            )
                                                      )
                                                )
                                          ))
                                       .then(Commands.literal("show").executes(context -> {
                                          if (com.eric.revill.b.b.c()) {
                                             ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Búnker configurado."), false);
                                          } else {
                                             ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Búnker no configurado."), false);
                                          }

                                          return 1;
                                       })))
                                    .then(Commands.literal("clear").executes(context -> {
                                       com.eric.revill.b.b.b();
                                       ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Búnker limpiado."), false);
                                       return 1;
                                    }))
                              ))
                           .then(((LiteralArgumentBuilder)Commands.literal("meteorites").then(Commands.literal("on").executes(context -> {
                              com.eric.revill.j.a.a.a().n = true;
                              ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Meteoritos activados."), false);
                              return 1;
                           }))).then(Commands.literal("off").executes(context -> {
                              com.eric.revill.j.a.a.a().n = false;
                              ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Meteoritos desactivados."), false);
                              return 1;
                           }))))
                        .then(
                           Commands.literal("config")
                              .executes(
                                 context -> {
                                    com.eric.revill.f.a config = com.eric.revill.j.a.a.a();
                                    ((CommandSourceStack)context.getSource())
                                       .sendSuccess(
                                          () -> Component.literal(
                                                "Configuración: duration=" + config.d + ", meteoritesMaxActive=" + config.o + ", destroyBlocks=" + config.u
                                             ),
                                          false
                                       );
                                    return 1;
                                 }
                              )
                        ))
                     .then(
                        ((LiteralArgumentBuilder)Commands.literal("waypoint")
                              .then(
                                 Commands.literal("add")
                                    .then(
                                       ((RequiredArgumentBuilder)Commands.argument("nombre", StringArgumentType.word())
                                             .executes(
                                                context -> {
                                                   ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
                                                   String name = StringArgumentType.getString(context, "nombre");
                                                   String id = "wp_" + System.currentTimeMillis();
                                                   com.eric.revill.t.a wp = new com.eric.revill.t.a(
                                                      id, name, player.blockPosition(), "star", -16711732, player.level().dimension().location().toString()
                                                   );
                                                   com.eric.revill.t.b.a(((CommandSourceStack)context.getSource()).getServer(), wp);
                                                   ((CommandSourceStack)context.getSource())
                                                      .sendSuccess(() -> Component.literal("Waypoint '" + name + "' creado en tu ubicación."), false);
                                                   return 1;
                                                }
                                             ))
                                          .then(
                                             Commands.argument("icono", StringArgumentType.word())
                                                .executes(
                                                   context -> {
                                                      ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
                                                      String name = StringArgumentType.getString(context, "nombre");
                                                      String icon = StringArgumentType.getString(context, "icono");
                                                      String id = "wp_" + System.currentTimeMillis();
                                                      com.eric.revill.t.a wp = new com.eric.revill.t.a(
                                                         id, name, player.blockPosition(), icon, -16711732, player.level().dimension().location().toString()
                                                      );
                                                      com.eric.revill.t.b.a(((CommandSourceStack)context.getSource()).getServer(), wp);
                                                      ((CommandSourceStack)context.getSource())
                                                         .sendSuccess(() -> Component.literal("Waypoint '" + name + "' [" + icon + "] creado."), false);
                                                      return 1;
                                                   }
                                                )
                                          )
                                    )
                              ))
                           .then(Commands.literal("remove").then(Commands.argument("id", StringArgumentType.string()).executes(context -> {
                              String id = StringArgumentType.getString(context, "id");
                              com.eric.revill.t.b.a(((CommandSourceStack)context.getSource()).getServer(), id);
                              ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Waypoint eliminado."), false);
                              return 1;
                           })))
                     ))
                  .then(
                     ((LiteralArgumentBuilder)Commands.literal("color").requires(source -> source.hasPermission(4)))
                        .then(Commands.argument("color", StringArgumentType.string()).executes(context -> {
                           String requestedColor = StringArgumentType.getString(context, "color");
                           String color;
                           if (!"random".equalsIgnoreCase(requestedColor) && !"aleatorio".equalsIgnoreCase(requestedColor)) {
                              color = requestedColor;
                           } else {
                              String[] palette = new String[]{"roja", "verde", "naranja", "rosada"};
                              color = palette[(int)(System.currentTimeMillis() % (long)palette.length)];
                           }

                           com.eric.revill.g.a.b(((CommandSourceStack)context.getSource()).getServer(), color);
                           ((CommandSourceStack)context.getSource())
                              .sendSuccess(() -> Component.literal("Ruleta DEDSAFIO4 lanzada para el color: " + color), false);
                           return 1;
                        }))
                  ))
               .then(
                  ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("revive").requires(source -> source.hasPermission(4)))
                        .executes(
                           context -> {
                              ServerPlayer target = ((CommandSourceStack)context.getSource()).getPlayerOrException();
                              com.eric.revill.g.a.a(target, GameType.SURVIVAL);
                              ((CommandSourceStack)context.getSource())
                                 .sendSuccess(() -> Component.literal("Jugador revivido: " + target.getGameProfile().getName() + " -> SURVIVAL"), false);
                              return 1;
                           }
                        ))
                     .then(
                        ((RequiredArgumentBuilder)Commands.argument("jugador", EntityArgument.player())
                              .executes(
                                 context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, "jugador");
                                    com.eric.revill.g.a.a(target, GameType.SURVIVAL);
                                    ((CommandSourceStack)context.getSource())
                                       .sendSuccess(() -> Component.literal("Jugador revivido: " + target.getGameProfile().getName() + " -> SURVIVAL"), false);
                                    return 1;
                                 }
                              ))
                           .then(
                              Commands.argument("modo", StringArgumentType.word())
                                 .executes(
                                    context -> {
                                       ServerPlayer target = EntityArgument.getPlayer(context, "jugador");
                                       String var3 = StringArgumentType.getString(context, "modo").toLowerCase();

                                       GameType mode = switch (var3) {
                                          case "survival", "supervivencia", "s" -> GameType.SURVIVAL;
                                          case "creative", "creativo", "c" -> GameType.CREATIVE;
                                          case "adventure", "aventura", "a" -> GameType.ADVENTURE;
                                          case "spectator", "espectador", "sp" -> GameType.SPECTATOR;
                                          default -> null;
                                       };
                                       if (mode == null) {
                                          throw new SimpleCommandExceptionType(
                                                Component.literal("Modo no válido. Usa: survival, creative, adventure, spectator.")
                                             )
                                             .create();
                                       } else {
                                          com.eric.revill.g.a.a(target, mode);
                                          ((CommandSourceStack)context.getSource())
                                             .sendSuccess(
                                                () -> Component.literal("Modo restaurado para " + target.getGameProfile().getName() + ": " + mode.name()), false
                                             );
                                          return 1;
                                       }
                                    }
                                 )
                           )
                     )
               ))
            .then(
               ((LiteralArgumentBuilder)Commands.literal("eon").requires(source -> source.hasPermission(4)))
                  .then(
                     Commands.argument("jugador", EntityArgument.player())
                        .executes(
                           context -> {
                              ServerPlayer target = EntityArgument.getPlayer(context, "jugador");
                              boolean enabled = com.eric.revill.s.a.a(((CommandSourceStack)context.getSource()).getServer(), target);
                              String status = enabled ? "activado" : "desactivado";
                              ((CommandSourceStack)context.getSource())
                                 .sendSuccess(() -> Component.literal("Modo EON " + status + " para " + target.getGameProfile().getName() + "."), false);
                              return 1;
                           }
                        )
                  )
            )
      );
   }

   static void a(CommandSourceStack source, boolean enabled) {
      RevillMod.d = enabled;
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeBoolean(enabled);

      for (ServerPlayer player : source.getServer().getPlayerList().getPlayers()) {
         ServerPlayNetworking.send(player, com.eric.revill.g.b.k, PacketByteBufs.copy(buf));
      }

      source.sendSuccess(() -> Component.literal(enabled ? "Fusión mental activada." : "Fusión mental desactivada."), false);
   }
}
