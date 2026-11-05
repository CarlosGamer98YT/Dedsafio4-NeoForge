package com.eric.revill.fabric.fabric.api.client.keybinding.v1;

/**
 * Adaptador de Fabric API para el registro de teclas (KeyMappings).
 * 
 * Almacena las asignaciones de teclado registradas por el mod y las transfiere al evento
 * {@link net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent} en NeoForge.
 */

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;

public final class KeyBindingHelper {
    public static final List<KeyMapping> KEYS = new ArrayList<>();

    public static KeyMapping registerKeyBinding(KeyMapping key) {
        KEYS.add(key);
        return key;
    }
}
