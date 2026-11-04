package com.eric.revill.fabric.fabric.api.event.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface UseBlockCallback {
    InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult hitResult);

    class Event {
        private final List<UseBlockCallback> listeners = new CopyOnWriteArrayList<>();
        public void register(UseBlockCallback listener) { listeners.add(listener); }
        public List<UseBlockCallback> getListeners() { return listeners; }
    }

    Event EVENT = new Event();
}
