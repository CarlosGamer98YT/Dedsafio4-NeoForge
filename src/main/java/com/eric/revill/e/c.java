package com.eric.revill.e;

import com.eric.revill.RevillMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public final class c {
   private c() {
   }

   public static void a(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                    "revill"
                                 )
                                 .requires(source -> source.hasPermission(4)))
                              .then(Commands.literal("start").executes(context -> {
                                 com.eric.revill.j.a.a.a(((CommandSourceStack)context.getSource()).getServer());
                                 return 1;
                              })))
                           .then(Commands.literal("stop").executes(context -> {
                              com.eric.revill.j.a.a.a(((CommandSourceStack)context.getSource()).getServer(), true);
                              ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("El Evento Rojo ha sido cancelado."), false);
                              return 1;
                           })))
                        .then(Commands.literal("status").executes(context -> {
                           com.eric.revill.j.a.a.a((CommandSourceStack)context.getSource());
                           return 1;
                        })))
                     .then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fusion").executes(context -> {
                        a.a((CommandSourceStack)context.getSource(), !RevillMod.d);
                        return 1;
                     })).then(Commands.literal("entrar").executes(context -> {
                        a.a((CommandSourceStack)context.getSource(), true);
                        return 1;
                     }))).then(Commands.literal("salir").executes(context -> {
                        a.a((CommandSourceStack)context.getSource(), false);
                        return 1;
                     }))))
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
                                                                  .then(Commands.argument("z2", IntegerArgumentType.integer()).executes(context -> {
                                                                     int x1 = IntegerArgumentType.getInteger(context, "x1");
                                                                     int y1 = IntegerArgumentType.getInteger(context, "y1");
                                                                     int z1 = IntegerArgumentType.getInteger(context, "z1");
                                                                     int x2 = IntegerArgumentType.getInteger(context, "x2");
                                                                     int y2 = IntegerArgumentType.getInteger(context, "y2");
                                                                     int z2 = IntegerArgumentType.getInteger(context, "z2");
                                                                     BlockPos pos1 = new BlockPos(x1, y1, z1);
                                                                     BlockPos pos2 = new BlockPos(x2, y2, z2);
                                                                     com.eric.revill.b.b.a(((CommandSourceStack)context.getSource()).getServer(), pos1, pos2);
                                                                     ((CommandSourceStack)context.getSource())
                                                                        .sendSuccess(() -> Component.literal("Búnker actualizado."), false);
                                                                     return 1;
                                                                  }))
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
            )
      );
   }
}
