package com.eric.revill.e;

import com.eric.revill.h.d;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class b {
   private b() {
   }

   public static void a(CommandDispatcher<CommandSourceStack> dispatcher) {
      a(dispatcher, "dedsafio4");
   }

   private static void a(CommandDispatcher<CommandSourceStack> dispatcher, String root) {
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(root).requires(source -> source.hasPermission(4))).then(a("coins")));
   }

   private static LiteralArgumentBuilder<CommandSourceStack> a(String name) {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(name)
                  .executes(context -> {
                     ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayer();
                     if (player == null) {
                        ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Este comando debe ejecutarse desde un jugador."), false);
                        return 1;
                     } else {
                        d.b(player);
                        return 1;
                     }
                  }))
               .then(
                  Commands.literal("add")
                     .then(
                        Commands.argument("jugador", EntityArgument.player())
                           .then(
                              Commands.argument("tipo", StringArgumentType.word())
                                 .then(
                                    Commands.argument("cantidad", IntegerArgumentType.integer(1, 1000000))
                                       .executes(
                                          context -> a(
                                                (CommandSourceStack)context.getSource(),
                                                EntityArgument.getPlayer(context, "jugador"),
                                                StringArgumentType.getString(context, "tipo"),
                                                IntegerArgumentType.getInteger(context, "cantidad"),
                                                true
                                             )
                                       )
                                 )
                           )
                     )
               ))
            .then(
               Commands.literal("remove")
                  .then(
                     Commands.argument("jugador", EntityArgument.player())
                        .then(
                           Commands.argument("tipo", StringArgumentType.word())
                              .then(
                                 Commands.argument("cantidad", IntegerArgumentType.integer(1, 1000000))
                                    .executes(
                                       context -> a(
                                             (CommandSourceStack)context.getSource(),
                                             EntityArgument.getPlayer(context, "jugador"),
                                             StringArgumentType.getString(context, "tipo"),
                                             IntegerArgumentType.getInteger(context, "cantidad"),
                                             false
                                          )
                                    )
                              )
                        )
                  )
            ))
         .then(
            Commands.literal("see")
               .then(
                  Commands.argument("jugador", EntityArgument.player())
                     .executes(context -> a((CommandSourceStack)context.getSource(), EntityArgument.getPlayer(context, "jugador")))
               )
         );
   }

   private static int a(CommandSourceStack source, ServerPlayer player, String typeName, int amount, boolean add) throws CommandSyntaxException {
      com.eric.revill.h.c type = com.eric.revill.h.c.b(typeName);
      if (type == null) {
         throw new SimpleCommandExceptionType(Component.literal("Tipo inválido. Usa: azul, roja o verde.")).create();
      } else {
         if (add) {
            com.eric.revill.h.b.a(source.getServer(), player.getUUID(), type, amount);
         } else if (!com.eric.revill.h.b.b(source.getServer(), player.getUUID(), type, amount)) {
            throw new SimpleCommandExceptionType(Component.literal("El jugador no tiene suficientes monedas de ese tipo.")).create();
         }

         source.sendSuccess(
            () -> Component.literal((add ? "Añadidas " : "Retiradas ") + amount + " deditas " + type.b() + " a " + player.getName().getString() + "."), false
         );
         return 1;
      }
   }

   private static int a(CommandSourceStack source, ServerPlayer player) {
      int[] balances = com.eric.revill.h.b.a(source.getServer(), player.getUUID());
      source.sendSuccess(
         () -> Component.literal(
               "Cuenta de " + player.getName().getString() + " | Azul: " + balances[0] + " | Roja: " + balances[1] + " | Verde: " + balances[2]
            ),
         false
      );
      return 1;
   }
}
