package com.eric.revill.d.e;

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public final class q {
   private q() {
   }

   public static boolean a(Screen screen) {
      Class<?> type = screen.getClass();
      return screen instanceof l
         || screen instanceof a
         || screen instanceof p
         || screen instanceof k
         || screen instanceof b
         || screen instanceof n
         || screen instanceof m
         || screen instanceof g
         || type.getEnclosingClass() == l.class;
   }
}
