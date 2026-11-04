package com.eric.revill.d.d;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public final class a {
   public static final a a = new a();
   private static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/bocadilloeon.png");
   private static final int c = 4;
   private static final int d = 100;
   private static final int e = 5;
   private static final int f = 3;
   private static final float g = 0.02F;
   private static final float h = 0.35F;
   private static final int i = 12;
   private static final int j = 8;
   private static final int k = 10;
   private static final int l = 10;
   private static final int m = 30;
   private static final int n = -15658735;
   private final Map<UUID, a.SpeechBubbleState> o = new HashMap<>();

   private a() {
   }

   public void a(UUID playerId, String message) {
      this.o.put(playerId, new a.SpeechBubbleState(message));
   }

   public void a(UUID playerId) {
      this.o.remove(playerId);
   }

   public void a() {
      this.o.clear();
   }

   public void b() {
      Iterator<Entry<UUID, a.SpeechBubbleState>> it = this.o.entrySet().iterator();

      while (it.hasNext()) {
         a.SpeechBubbleState state = it.next().getValue();
         state.a();
         if (state.b()) {
            it.remove();
         }
      }
   }

   public void a(PoseStack matrices, BufferSource immediate, Camera camera, float tickDelta) {
      Minecraft client = Minecraft.getInstance();
      if (client.level != null) {
         Font tr = client.font;

         for (Entry<UUID, a.SpeechBubbleState> entry : this.o.entrySet()) {
            UUID uuid = entry.getKey();
            a.SpeechBubbleState state = entry.getValue();
            Player player = client.level.players().stream().filter(p2 -> p2.getUUID().equals(uuid)).findFirst().orElse(null);
            if (player != null) {
               this.a(matrices, immediate, camera, tr, player, state, tickDelta);
            }
         }

         immediate.endBatch();
      }
   }

   private void a(PoseStack matrices, BufferSource immediate, Camera camera, Font tr, Player player, a.SpeechBubbleState state, float tickDelta) {
      List<String> visibleLines = state.c();
      if (!visibleLines.isEmpty()) {
         Vec3 playerPos = player.getPosition(tickDelta);
         double headY = playerPos.y + (double)player.getEyeHeight(player.getPose()) + 0.35F;
         Vec3 camPos = camera.getPosition();
         double dx = playerPos.x - camPos.x;
         double dy = headY - camPos.y;
         double dz = playerPos.z - camPos.z;
         matrices.pushPose();
         matrices.translate(dx, dy, dz);
         Quaternionf rotation = camera.rotation();
         matrices.mulPose(rotation);
         matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
         matrices.scale(-0.02F, -0.02F, 0.02F);
         int maxLineWidth = 30;

         for (String line : visibleLines) {
            int w = tr.width(line);
            if (w > maxLineWidth) {
               maxLineWidth = w;
            }
         }

         int bubbleW = Math.max(30, maxLineWidth + 24);
         int bubbleH = visibleLines.size() * 10 + 16;
         int bx = -bubbleW / 2;
         int by = -bubbleH - 10;
         Matrix4f mat = matrices.last().pose();
         this.a(mat, immediate, bx, by, bubbleW, bubbleH + 10);
         immediate.endBatch(RenderType.entityTranslucent(b));
         matrices.pushPose();
         matrices.translate(0.0F, 0.0F, 0.1F);
         Matrix4f textMatrix = matrices.last().pose();

         for (int i2 = 0; i2 < visibleLines.size(); i2++) {
            String linex = visibleLines.get(i2);
            int lx = bx + 12;
            int ly = by + 8 + i2 * 10;
            tr.drawInBatch(linex, (float)lx, (float)ly, -15658735, false, textMatrix, immediate, DisplayMode.SEE_THROUGH, 0, 15728880);
         }

         matrices.popPose();
         matrices.popPose();
      }
   }

   private void a(Matrix4f matrix, BufferSource immediate, int x, int y, int width, int height) {
      RenderType layer = RenderType.entityTranslucent(b);
      VertexConsumer vertexConsumer = immediate.getBuffer(layer);
      vertexConsumer.addVertex(matrix, (float)x, (float)y, 0.0F)
         .setColor(255, 255, 255, 255)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(15728880)
         .setNormal(0.0F, 0.0F, 1.0F);
      vertexConsumer.addVertex(matrix, (float)x, (float)(y + height), 0.0F)
         .setColor(255, 255, 255, 255)
         .setUv(0.0F, 1.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(15728880)
         .setNormal(0.0F, 0.0F, 1.0F);
      vertexConsumer.addVertex(matrix, (float)(x + width), (float)(y + height), 0.0F)
         .setColor(255, 255, 255, 255)
         .setUv(1.0F, 1.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(15728880)
         .setNormal(0.0F, 0.0F, 1.0F);
      vertexConsumer.addVertex(matrix, (float)(x + width), (float)y, 0.0F)
         .setColor(255, 255, 255, 255)
         .setUv(1.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(15728880)
         .setNormal(0.0F, 0.0F, 1.0F);
   }

   @Environment(EnvType.CLIENT)
   private static final class SpeechBubbleState {
      private final List<String[]> a;
      private final int b;
      private int c = 0;
      private int d = 0;
      private int e = -1;

      SpeechBubbleState(String message) {
         String trimmed = message.trim();
         String[] allWords = trimmed.isEmpty() ? new String[0] : trimmed.split("\\s+");
         this.a = new ArrayList<>();
         int remaining = allWords.length;
         int idx = 0;

         while (idx < allWords.length && this.a.size() < 3) {
            int count = Math.min(5, remaining);
            String[] lineWords = new String[count];
            System.arraycopy(allWords, idx, lineWords, 0, count);
            this.a.add(lineWords);
            idx += count;
            remaining -= count;
         }

         this.b = idx;
      }

      void a() {
         if (this.c < this.b) {
            this.d++;
            if (this.d >= 4) {
               this.d = 0;
               this.c++;
               if (this.c >= this.b) {
                  this.e = 100;
               }
            }
         } else if (this.e > 0) {
            this.e--;
         }
      }

      boolean b() {
         return this.e == 0;
      }

      List<String> c() {
         ArrayList<String> result = new ArrayList<>();
         int wordsLeft = this.c;

         for (String[] lineWords : this.a) {
            if (wordsLeft <= 0) {
               break;
            }

            int take = Math.min(wordsLeft, lineWords.length);
            StringBuilder sb = new StringBuilder();

            for (int i2 = 0; i2 < take; i2++) {
               if (i2 > 0) {
                  sb.append(' ');
               }

               sb.append(lineWords[i2]);
            }

            result.add(sb.toString());
            wordsLeft -= take;
         }

         return result;
      }
   }
}
