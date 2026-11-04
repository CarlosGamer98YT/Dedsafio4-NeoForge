package com.eric.revill.fabric.fabric.api.event.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PlayerBlockBreakEvents {
    @FunctionalInterface
    public interface After {
        void afterBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<After> AFTER = new Event<>();
}
