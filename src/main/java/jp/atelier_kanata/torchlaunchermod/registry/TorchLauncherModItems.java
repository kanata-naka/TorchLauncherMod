package jp.atelier_kanata.torchlaunchermod.registry;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.item.TorchLauncherItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = TorchLauncherMod.MODID, value = Dist.CLIENT)
public class TorchLauncherModItems {
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorchLauncherMod.MODID);

  public static final DeferredItem<Item> TORCH_LAUNCHER = ITEMS.registerItem("torch_launcher", (prop) -> new TorchLauncherItem(prop.durability(384)));

  @SubscribeEvent
  public static void registerItemProperties(FMLClientSetupEvent event) {
    ItemProperties.register(TORCH_LAUNCHER.get(), ResourceLocation.withDefaultNamespace("pulling"),
        (itemStack, level, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
    ItemProperties.register(TORCH_LAUNCHER.get(), ResourceLocation.withDefaultNamespace("pull"), (itemStack, level, livingEntity, seed) -> {
      if (livingEntity == null) {
        return 0.0F;
      } else {
        return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 20.0F;
      }
    });
  }
}
