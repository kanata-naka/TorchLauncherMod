package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TorchLauncherMod.MOD_ID)
public class TorchLauncherModDataProvider {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent.Client event) {
    event.createProvider(TorchLauncherModEnUsLanguageProvider::new);
    event.createProvider(TorchLauncherModJaJpLanguageProvider::new);
    event.createProvider(TorchLauncherModModelProvider::new);
    event.createProvider(TorchLauncherModRecipeProvider.Runner::new);
    event.createProvider(TorchLauncherModItemTagsProvider::new);
  }

}
