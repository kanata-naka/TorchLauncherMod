package jp.atelier_kanata.torchlaunchermod.client.renderer.entity;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import jp.atelier_kanata.torchlaunchermod.client.renderer.entity.state.TorchLauncherProjectileRenderState;
import jp.atelier_kanata.torchlaunchermod.entity.TorchLauncherProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.neoforged.neoforge.client.RenderTypeHelper;

public class TorchLauncherProjectileRenderer extends EntityRenderer<TorchLauncherProjectileEntity, TorchLauncherProjectileRenderState> {

  private final BlockRenderDispatcher dispatcher;

  public TorchLauncherProjectileRenderer(Context context) {
    super(context);
    this.shadowRadius = 0.5F;
    this.dispatcher = context.getBlockRenderDispatcher();
  }

  @Override
  public TorchLauncherProjectileRenderState createRenderState() {
    return new TorchLauncherProjectileRenderState();
  }

  @Override
  public void extractRenderState(TorchLauncherProjectileEntity entity, TorchLauncherProjectileRenderState reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
    reusedState.level = entity.level();
    reusedState.biome = entity.level().getBiome(blockPos);
    reusedState.startBlockPos = entity.getStartBlockPos();
    reusedState.blockPos = blockPos;
    reusedState.blockState = entity.getBlockState();
  }

  @Override
  public boolean shouldRender(TorchLauncherProjectileEntity entity, Frustum frustum, double camX, double camY, double camZ) {
    return !super.shouldRender(entity, frustum, camX, camY, camZ) ? false : entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
  }

  @Override
  public void render(TorchLauncherProjectileRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    if (renderState.blockState.getRenderShape() != RenderShape.MODEL) {
      return;
    }
    poseStack.pushPose();
    poseStack.translate(-0.5, 0.0, -0.5);
    List<BlockModelPart> blockModelPartList = this.dispatcher.getBlockModel(renderState.blockState).collectParts(renderState.level, renderState.blockPos, renderState.blockState,
        RandomSource.create(renderState.blockState.getSeed(renderState.startBlockPos)));
    this.dispatcher.getModelRenderer().tesselateBlock(renderState, blockModelPartList, renderState.blockState, renderState.blockPos, poseStack,
        renderType -> buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)), false, OverlayTexture.NO_OVERLAY);
    poseStack.popPose();
    super.render(renderState, poseStack, buffer, packedLight);
  }

}
