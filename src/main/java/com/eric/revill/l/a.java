package com.eric.revill.l;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

public final class a extends DeferredSpawnEggItem {
   public a(Supplier<? extends EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor, Properties settings) {
      super(entityType, primaryColor, secondaryColor, settings);
   }

   public InteractionResult useOn(UseOnContext context) {
      if (!context.getLevel().isClientSide && context.getPlayer() != null) {
         AABB spawnArea = new AABB(context.getClickedPos()).inflate(3.0);
         ArrayList<UUID> existingEntities = new ArrayList<>();

         for (com.eric.revill.i.a entity : context.getLevel().getEntitiesOfClass(com.eric.revill.i.a.class, spawnArea, ignored -> true)) {
            existingEntities.add(entity.getUUID());
         }

         InteractionResult result = super.useOn(context);
         if (!result.consumesAction()) {
            return result;
         } else {
            Player spawner = context.getPlayer();
            Iterator iterator = context.getLevel()
               .getEntitiesOfClass(com.eric.revill.i.a.class, spawnArea, candidate -> !existingEntities.contains(candidate.getUUID()))
               .iterator();
            if (iterator.hasNext()) {
               com.eric.revill.i.a entity = (com.eric.revill.i.a)iterator.next();
               entity.a(spawner);
            }

            return result;
         }
      } else {
         return super.useOn(context);
      }
   }
}
