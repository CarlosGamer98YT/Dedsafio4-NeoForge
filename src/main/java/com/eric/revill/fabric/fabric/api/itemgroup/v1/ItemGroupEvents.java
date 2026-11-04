package com.eric.revill.fabric.fabric.api.itemgroup.v1;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class ItemGroupEvents {
    public interface ModifyEntries {
        void modifyEntries(Entries entries);
    }

    public interface Entries {
        void accept(ItemStack stack);
        default void accept(ItemLike item) { accept(new ItemStack(item)); }
    }

    private static final Map<ResourceKey<CreativeModeTab>, List<ModifyEntries>> MODIFIERS = new ConcurrentHashMap<>();

    public static class Event {
        private final ResourceKey<CreativeModeTab> key;
        public Event(ResourceKey<CreativeModeTab> key) { this.key = key; }
        public void register(ModifyEntries modifier) {
            MODIFIERS.computeIfAbsent(this.key, k -> new CopyOnWriteArrayList<>()).add(modifier);
        }
    }

    public static Event modifyEntriesEvent(ResourceKey<CreativeModeTab> tab) {
        return new Event(tab);
    }

    public static List<ModifyEntries> getModifiers(ResourceKey<CreativeModeTab> tab) {
        return MODIFIERS.get(tab);
    }
}
