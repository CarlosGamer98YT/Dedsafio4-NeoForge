package com.eric.revill.r;

import com.eric.revill.ForgeRegistration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class a {
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "ui");
   public static final SoundEvent b = ForgeRegistration.register(Registries.SOUND_EVENT, a, SoundEvent.createVariableRangeEvent(a));

   private a() {
   }

   public static void a() {
   }
}
