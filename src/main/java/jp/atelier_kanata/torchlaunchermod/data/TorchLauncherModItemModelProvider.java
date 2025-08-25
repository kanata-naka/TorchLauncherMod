package jp.atelier_kanata.torchlaunchermod.data;

import org.apache.commons.lang3.StringUtils;
import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TorchLauncherModItemModelProvider extends ItemModelProvider {

  public TorchLauncherModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, TorchLauncherMod.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    registerTorchLauncherItemModel();
  }

  private void registerTorchLauncherItemModel() {
    ItemModelBuilder torchLauncherModelBuilder = createTorchLauncherItemModelBuilder(null).transforms()
        //
        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.68f, 0.68f, 0.68f).end()
        //
        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.68f, 0.68f, 0.68f).end()
        //
        .transform(ItemDisplayContext.HEAD).translation(2, 6.5f, 0).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).translation(0.25f, 3.125f, 1)
        .scale(0.625f, 0.625f, 0.625f).end()
        //
        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).translation(-0.25f, 3.125f, 1).scale(0.625f, 0.625f, 0.625f).end().end();

    ModelFile torchLauncherModelPulling0 = createTorchLauncherItemModelBuilder("pulling_0").parent(torchLauncherModelBuilder);
    ModelFile torchLauncherModelPulling1 = createTorchLauncherItemModelBuilder("pulling_1").parent(torchLauncherModelBuilder);
    ModelFile torchLauncherModelPulling2 = createTorchLauncherItemModelBuilder("pulling_2").parent(torchLauncherModelBuilder);

    torchLauncherModelBuilder
        //
        .override().predicate(ResourceLocation.withDefaultNamespace("pulling"), 1).model(torchLauncherModelPulling0).end()
        //
        .override().predicate(ResourceLocation.withDefaultNamespace("pulling"), 1).predicate(ResourceLocation.withDefaultNamespace("pull"), 0.55f).model(torchLauncherModelPulling1)
        .end()
        //
        .override().predicate(ResourceLocation.withDefaultNamespace("pulling"), 1).predicate(ResourceLocation.withDefaultNamespace("pull"), 0.9f).model(torchLauncherModelPulling2)
        .end();
  }

  private ItemModelBuilder createTorchLauncherItemModelBuilder(String state) {
    ResourceLocation torchLauncherId = TorchLauncherModItems.TORCH_LAUNCHER.getId();
    String path = torchLauncherId.getPath();
    if (StringUtils.isNotEmpty(state)) {
      path += "_" + state;
    }
    return singleTexture(path, mcLoc(folder + "/generated"), "layer0", ResourceLocation.fromNamespaceAndPath(torchLauncherId.getNamespace(), folder + "/" + path));
  }

}
