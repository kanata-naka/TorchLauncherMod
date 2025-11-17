package jp.atelier_kanata.torchlaunchermod;

import jp.atelier_kanata.torchlaunchermod.client.gui.screens.TorchLauncherModConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = TorchLauncherMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TorchLauncherMod.MOD_ID, value = Dist.CLIENT)
public class TorchLauncherModClient {

  public TorchLauncherModClient(ModContainer container) {
    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  @SubscribeEvent
  static void onClientSetup(FMLClientSetupEvent event) {
    if (ModList.get().isLoaded("cloth_config")) {
      ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
          () -> (modContainer, parent) -> TorchLauncherModConfigScreen.create(parent));
    }
  }

}
