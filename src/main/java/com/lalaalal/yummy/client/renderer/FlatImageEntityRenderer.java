package com.lalaalal.yummy.client.renderer;

import com.lalaalal.yummy.YummyMod;
import com.lalaalal.yummy.entity.FlatImageEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FlatImageEntityRenderer extends EntityRenderer<FlatImageEntity> {
    public FlatImageEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(FlatImageEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        poseStack.scale(entity.getWidth(), entity.getWidth(), entity.getWidth());
        poseStack.translate(0, 0.001, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float) entity.getRotationDegree()));

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        drawVertex(matrix4f, matrix3f, vertexConsumer, -1, 0, -1, 0.0F, 0.0F, 1, 0, 1, 240);
        drawVertex(matrix4f, matrix3f, vertexConsumer, -1, 0, 1, 0.0F, 1.0F, 1, 0, 1, 240);
        drawVertex(matrix4f, matrix3f, vertexConsumer, 1, 0, 1, 1.0F, 1.0F, 1, 0, 1, 240);
        drawVertex(matrix4f, matrix3f, vertexConsumer, 1, 0, -1, 1.0F, 0.0F, 1, 0, 1, 240);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FlatImageEntity entity) {
        return new ResourceLocation(YummyMod.MOD_ID, "textures/entity/" + entity.imageName + ".png");
    }

    public void drawVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int poseX, int poseY, int poseZ, float u, float v, int normalX, int normalZ, int normalY, int lightUV) {
        vertexConsumer.vertex(matrix4f, (float) poseX, (float) poseY, (float) poseZ)
                .color(255, 255, 255, 255)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightUV)
                .normal(matrix3f, (float) normalX, (float) normalY, (float) normalZ).endVertex();
    }
}