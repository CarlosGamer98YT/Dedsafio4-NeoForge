package com.eric.revill.d.b;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class a extends EntityModel<com.eric.revill.i.a> {
   public static final ModelLayerLocation a = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "cajero"), "main");
   private final ModelPart b;

   public a(ModelPart root) {
      this.b = root.getChild("bb_main");
   }

   public static LayerDefinition a() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bb_main = partdefinition.addOrReplaceChild(
         "bb_main", CubeListBuilder.create().texOffs(56, 0).addBox(-6.0F, -12.0F, -8.0F, 12.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      PartDefinition cube_r1 = bb_main.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(50, 50).addBox(-7.0F, -9.0F, -3.0F, 12.0F, 7.0F, 3.0F),
         PartPose.offsetAndRotation(1.0F, -14.0F, -4.0F, -0.3054F, 0.0F, 0.0F)
      );
      PartDefinition cube_r2 = bb_main.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(56, 5).addBox(-1.0F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F),
         PartPose.offsetAndRotation(-5.0F, -13.0F, -6.0F, 0.5672F, 0.0F, 0.0F)
      );
      bb_main.addOrReplaceChild(
         "cube_r3",
         CubeListBuilder.create()
            .texOffs(50, 30)
            .addBox(4.0F, -45.0F, -7.0F, 3.0F, 5.0F, 14.0F)
            .texOffs(0, 30)
            .addBox(-7.0F, -45.0F, -7.0F, 11.0F, 16.0F, 14.0F)
            .texOffs(0, 0)
            .addBox(-7.0F, -29.0F, -7.0F, 14.0F, 14.0F, 14.0F),
         PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, 0.0F, 1.5708F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 128, 128);
   }

   @Override
   public void setupAnim(com.eric.revill.i.a entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
      this.b.render(matrices, vertices, light, overlay, color);
   }
}
