package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;

public class TorchLauncherModModelProvider extends ModelProvider {

  public TorchLauncherModModelProvider(PackOutput output) {
    super(output, TorchLauncherMod.MOD_ID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    registerTorchLauncherItemModel(itemModels);
  }

  private void registerTorchLauncherItemModel(ItemModelGenerators itemModels) {
    ModelTemplate parentModelTemplate = ModelTemplates.createItem("generated", TextureSlot.LAYER0).extend()
        //
        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, builder -> builder.scale(0.68f, 0.68f, 0.68f))
        //
        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, builder -> builder.scale(0.68f, 0.68f, 0.68f))
        //
        .transform(ItemDisplayContext.HEAD, builder -> builder.translation(2, 6.5f, 0))
        //
        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, builder -> builder.translation(0.25f, 3.125f, 1).scale(0.625f, 0.625f, 0.625f))
        //
        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, builder -> builder.translation(-0.25f, 3.125f, 1).scale(0.625f, 0.625f, 0.625f)).build();
    ItemModel.Unbaked modelNotPulling = ItemModelUtils.plainModel(itemModels.createFlatItemModel(TorchLauncherModItems.TORCH_LAUNCHER.get(), parentModelTemplate));

    ModelTemplate childModelTemplate = ModelTemplates.createItem(TorchLauncherModItems.TORCH_LAUNCHER.getRegisteredName(), TextureSlot.LAYER0);
    ItemModel.Unbaked modelPulling0 = ItemModelUtils.plainModel(itemModels.createFlatItemModel(TorchLauncherModItems.TORCH_LAUNCHER.get(), "_pulling_0", childModelTemplate));
    ItemModel.Unbaked modelPulling1 = ItemModelUtils.plainModel(itemModels.createFlatItemModel(TorchLauncherModItems.TORCH_LAUNCHER.get(), "_pulling_1", childModelTemplate));
    ItemModel.Unbaked modelPulling2 = ItemModelUtils.plainModel(itemModels.createFlatItemModel(TorchLauncherModItems.TORCH_LAUNCHER.get(), "_pulling_2", childModelTemplate));

    itemModels.itemModelOutput.accept(TorchLauncherModItems.TORCH_LAUNCHER.get(), ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
        //
        ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F,
            //
            modelPulling0,
            //
            ItemModelUtils.override(modelPulling1, 0.55f),
            //
            ItemModelUtils.override(modelPulling2, 0.9F)),
        //
        modelNotPulling));
  }

}
