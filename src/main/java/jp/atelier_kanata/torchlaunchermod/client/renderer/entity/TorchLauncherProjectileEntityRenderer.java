package jp.atelier_kanata.torchlaunchermod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import jp.atelier_kanata.torchlaunchermod.entity.TorchLauncherProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TorchLauncherProjectileEntityRenderer extends EntityRenderer<TorchLauncherProjectileEntity> {

  private final BlockRenderDispatcher dispatcher;

  public TorchLauncherProjectileEntityRenderer(Context context) {
    super(context);
    this.dispatcher = context.getBlockRenderDispatcher();
  }

  public void render(TorchLauncherProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    BlockState blockState = Block.byItem(entity.getItemStack().getItem()).defaultBlockState();
    if (blockState.getRenderShape() != RenderShape.MODEL) {
      return;
    }

    Level level = entity.level();
    if (blockState == level.getBlockState(entity.blockPosition())) {
      return;
    }

    poseStack.pushPose();
    poseStack.translate(-0.5, 0.0, -0.5);

    BakedModel model = this.dispatcher.getBlockModel(blockState);
    long seed = blockState.getSeed(entity.getStartBlockPos());
    BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
    for (RenderType renderType : model.getRenderTypes(blockState, RandomSource.create(seed), net.neoforged.neoforge.client.model.data.ModelData.EMPTY))
      this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockState, blockPos, poseStack,
          buffer.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)), false, RandomSource.create(), seed, OverlayTexture.NO_OVERLAY,
          net.neoforged.neoforge.client.model.data.ModelData.EMPTY, renderType);

    poseStack.popPose();

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(TorchLauncherProjectileEntity entity) {
    return TextureAtlas.LOCATION_BLOCKS;
  }

}
