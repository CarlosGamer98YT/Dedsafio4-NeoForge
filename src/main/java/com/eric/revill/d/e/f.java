package com.eric.revill.d.e;

/**
 * Botón gráfico personalizado con estilo visual de DEDSAFIO 4.
 * 
 * Extiende la clase {@link net.minecraft.client.gui.components.Button} añadiendo
 * estados resaltados al pasar el cursor y sonidos característicos al pulsar.
 */

import java.util.Objects;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
final class f extends Button {
   f(int x, int y, int width, int height, Component message, OnPress action) {
      super(x, y, width, height, message, action, DEFAULT_NARRATION);
   }

   protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
      int x = this.getX();
      int y = this.getY();
      if (this.active && this.isHovered()) {
         context.fill(x, y, x + this.width, y + this.height, 808894442);
      }

      context.renderOutline(x, y, this.width, this.height, this.active ? -13189142 : -12234150);
      Font textRenderer = Minecraft.getInstance().font;
      float textScale = 0.75F;
      context.pose().pushPose();
      context.pose().translate((float)x + (float)this.width / 2.0F, (float)y + (float)this.height / 2.0F, 0.0F);
      context.pose().scale(textScale, textScale, 1.0F);
      Component text = this.getMessage();
      Objects.requireNonNull(textRenderer);
      context.drawCenteredString(textRenderer, text, 0, -4, this.active ? -1640193 : -8944757);
      context.pose().popPose();
   }
}
