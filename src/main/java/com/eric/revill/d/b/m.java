package com.eric.revill.d.b;

/**
 * Renderizador de créditos y marca de agua en pantalla.
 * 
 * Dibuja de manera no invasiva el texto de atribución oficial del mod ("Mod de Dedsafio4 por EricReck")
 * en la esquina de la interfaz.
 */

import java.util.Objects;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public final class m {
   private static final String a = "Mod de Dedsafio4 por ";
   private static final String b = "EricReck";
   private static final String c = "https://www.curseforge.com/members/ericreck/projects";
   private static final int d = 2;
   private static final int e = 22;

   private m() {
   }

   public static void a(GuiGraphics context) {
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.font != null) {
         int lineWidth = client.font.width("Mod de Dedsafio4 por EricReck");
         int x = context.guiWidth() - 2 - lineWidth;
         int y = context.guiHeight() - 22;
         context.drawString(client.font, "Mod de Dedsafio4 por ", x, y, -5592406);
         context.drawString(client.font, "EricReck", x + client.font.width("Mod de Dedsafio4 por "), y, -11141121);
      }
   }

   public static boolean a(double mouseX, double mouseY, int screenWidth, int screenHeight) {
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.font != null) {
         int prefixWidth = client.font.width("Mod de Dedsafio4 por ");
         int authorWidth = client.font.width("EricReck");
         int lineWidth = prefixWidth + authorWidth;
         int lineX = screenWidth - 2 - lineWidth;
         int lineY = screenHeight - 22;
         if (mouseX >= (double)(lineX + prefixWidth) && mouseX < (double)(lineX + lineWidth) && mouseY >= (double)(lineY - 2)) {
            Objects.requireNonNull(client.font);
            if (mouseY < (double)(lineY + 9)) {
               Util.getPlatform().openUri("https://www.curseforge.com/members/ericreck/projects");
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
