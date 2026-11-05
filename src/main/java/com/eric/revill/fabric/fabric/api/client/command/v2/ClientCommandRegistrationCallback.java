package com.eric.revill.fabric.fabric.api.client.command.v2;

/**
 * Interfaz de callback de Fabric API para registrar comandos de cliente.
 * 
 * Disparada durante la inicialización de comandos en el cliente para asociar árboles de Brigadier.
 */

import com.mojang.brigadier.CommandDispatcher;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface ClientCommandRegistrationCallback {
    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess);

    class Event {
        private final List<ClientCommandRegistrationCallback> listeners = new CopyOnWriteArrayList<>();
        public void register(ClientCommandRegistrationCallback listener) { listeners.add(listener); }
        public List<ClientCommandRegistrationCallback> getListeners() { return listeners; }
    }

    Event EVENT = new Event();
}
