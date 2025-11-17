package jp.atelier_kanata.torchlaunchermod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import jp.atelier_kanata.torchlaunchermod.client.renderer.entity.state.TorchLauncherProjectileRenderState;
import jp.atelier_kanata.torchlaunchermod.entity.TorchLauncherProjectileEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;

public class TorchLauncherProjectileRenderer
    extends EntityRenderer<TorchLauncherProjectileEntity, TorchLauncherProjectileRenderState> {

  public TorchLauncherProjectileRenderer(Context context) {
    super(context);
    this.shadowRadius = 0.5F;
  }

  @Override
  public TorchLauncherProjectileRenderState createRenderState() {
    return new TorchLauncherProjectileRenderState();
  }

  @Override
  public void extractRenderState(TorchLauncherProjectileEntity entity, TorchLauncherProjectileRenderState reusedState,
      float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
    reusedState.movingBlockRenderState.level = entity.level();
    reusedState.movingBlockRenderState.biome = entity.level().getBiome(blockPos);
    reusedState.movingBlockRenderState.blockPos = blockPos;
    reusedState.movingBlockRenderState.blockState = entity.getBlockState();
  }

  @Override
  public boolean shouldRender(TorchLauncherProjectileEntity entity, Frustum camera, double camX, double camY,
      double camZ) {
    return !super.shouldRender(entity, camera, camX, camY, camZ) ? false
        : entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
  }

  @Override
  public void submit(TorchLauncherProjectileRenderState renderState, PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
    if (renderState.movingBlockRenderState.blockState.getRenderShape() != RenderShape.MODEL) {
      return;
    }
    poseStack.pushPose();
    poseStack.translate(-0.5, 0.0, -0.5);
    submitNodeCollector.submitMovingBlock(poseStack, renderState.movingBlockRenderState);
    poseStack.popPose();
    super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
  }

}
