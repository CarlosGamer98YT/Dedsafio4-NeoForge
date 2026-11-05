package com.eric.revill.l;

/**
 * Ítem base de moneda (Dedita) con soporte para descripciones y tooltips traducibles.
 */

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class b extends Item {
   private final String a;

   public b(Properties settings, String tooltipTranslationKey) {
      super(settings);
      this.a = tooltipTranslationKey;
   }

   public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
      boolean hasParagraph = this.a(tooltip, ".summary", ChatFormatting.GRAY, false);
      hasParagraph = this.a(tooltip, ".action", ChatFormatting.AQUA, hasParagraph);
      hasParagraph = this.a(tooltip, ".benefit", ChatFormatting.GREEN, hasParagraph);
      this.a(tooltip, ".warning", ChatFormatting.RED, hasParagraph);
   }

   public Component getName(ItemStack stack) {
      Component name = super.getName(stack);
      return (Component)(this.a.contains(".totem_") ? name.copy().withStyle(ChatFormatting.YELLOW) : name);
   }

   private boolean a(List<Component> tooltip, String suffix, ChatFormatting color, boolean hasParagraph) {
      String key = this.a + suffix;
      if (Language.getInstance().has(key)) {
         if (hasParagraph) {
            tooltip.add(Component.empty());
         }

         String[] words = Language.getInstance().getOrDefault(key).trim().split("\\s+");
         StringBuilder line = new StringBuilder();
         int wordCount = 0;

         for (String word : words) {
            if (!word.isEmpty()) {
               if (wordCount == 4) {
                  tooltip.add(Component.literal(line.toString()).withStyle(color));
                  line.setLength(0);
                  wordCount = 0;
               }

               if (line.length() > 0) {
                  line.append(' ');
               }

               line.append(word);
               wordCount++;
            }
         }

         if (line.length() > 0) {
            tooltip.add(Component.literal(line.toString()).withStyle(color));
         }

         return true;
      } else {
         return hasParagraph;
      }
   }
}
