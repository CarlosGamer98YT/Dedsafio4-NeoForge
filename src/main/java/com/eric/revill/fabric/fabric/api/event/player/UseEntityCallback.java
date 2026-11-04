package com.eric.revill.fabric.fabric.api.event.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

@FunctionalInterface
public interface UseEntityCallback {
    InteractionResult interact(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult);

    class Event {
        private final List<UseEntityCallback> listeners = new CopyOnWriteArrayList<>();
        public void register(UseEntityCallback listener) { listeners.add(listener); }
        public List<UseEntityCallback> getListeners() { return listeners; }
    }

    Event EVENT = new Event();
}
