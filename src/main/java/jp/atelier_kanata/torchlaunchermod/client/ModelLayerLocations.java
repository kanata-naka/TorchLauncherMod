package jp.atelier_kanata.torchlaunchermod.client;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayerLocations {
  public static final ModelLayerLocation TORCH_LAUNCHER_PROJECTILE_ENTITY = createModelLayerLocation("torch_launcher_projectile_entity", "main");

  private static ModelLayerLocation createModelLayerLocation(String name, String layer) {
    return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TorchLauncherMod.MODID, name), layer);
  }
}
