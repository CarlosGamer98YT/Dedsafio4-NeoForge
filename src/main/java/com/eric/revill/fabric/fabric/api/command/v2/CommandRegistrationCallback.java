package com.eric.revill.fabric.fabric.api.command.v2;

import com.mojang.brigadier.CommandDispatcher;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@FunctionalInterface
public interface CommandRegistrationCallback {
    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment);

    class Event {
        private final List<CommandRegistrationCallback> listeners = new CopyOnWriteArrayList<>();
        public void register(CommandRegistrationCallback listener) { listeners.add(listener); }
        public List<CommandRegistrationCallback> getListeners() { return listeners; }
    }

    Event EVENT = new Event();
}
