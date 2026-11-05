package com.eric.revill.d.e;

/**
 * Componente auxiliar de lista desplegable y paginación para interfaces gráficas.
 * 
 * Gestiona el desplazamiento vertical (scroll), la selección de elementos y el filtrado
 * de listas (por ejemplo, lista de jugadores en el buzón o lista de gremios).
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
final class o {
   private static final int[] a = new int[]{-50347, -25294, -187, -12060806, -13179137, -11172609, -5088001};

   private o() {
   }

   static void a(GuiGraphics context, int x, int y, int width, int height) {
      context.fill(x, y, x + width, y + height, -6575694);
      context.fill(x + 2, y + 2, x + width - 2, y + height - 2, -13155509);
      context.fill(x + 4, y + 4, x + width - 4, y + height - 4, -15656415);
      context.fill(x + 5, y + 5, x + width - 5, y + 6, -9141622);

      for (int index = 0; index < a.length; index++) {
         int top = 7 + index * (height - 14) / a.length;
         int bottom = 7 + (index + 1) * (height - 14) / a.length;
         context.fill(x + 3, y + top, x + 5, y + bottom, a[index]);
         context.fill(x + width - 5, y + top, x + width - 3, y + bottom, a[a.length - index - 1]);
      }

      context.fill(x + width / 2 - 24, y + 2, x + width / 2 + 24, y + 4, -12192257);
      context.fill(x + 7, y + height - 5, x + 27, y + height - 3, -12192257);
      context.fill(x + width - 27, y + height - 5, x + width - 7, y + height - 3, -12192257);
   }

   static void a(GuiGraphics context, int x, int y, int width, int height, int borderColor) {
      context.fill(x, y, x + width, y + height, borderColor);
      context.fill(x + 1, y + 1, x + width - 1, y + height - 1, -14997455);
   }

   static void a(GuiGraphics context, int x, int y) {
      context.fill(x - 1, y - 1, x + 17, y + 17, -16183790);
      context.fill(x, y, x + 16, y + 16, -13155509);
      context.fill(x + 1, y + 1, x + 15, y + 15, -14998227);
   }

   static void b(GuiGraphics context, int x, int y, int width, int height) {
      for (int index = 0; index < a.length; index++) {
         int left = x + index * width / a.length;
         int right = x + (index + 1) * width / a.length;
         context.fill(left, y, right, y + 2, a[index]);
         context.fill(left, y + height - 2, right, y + height, a[index]);
         context.fill(x, y + index * height / a.length, x + 2, y + (index + 1) * height / a.length, a[index]);
         context.fill(x + width - 2, y + index * height / a.length, x + width, y + (index + 1) * height / a.length, a[a.length - index - 1]);
      }
   }
}
