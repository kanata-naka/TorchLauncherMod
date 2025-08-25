package jp.atelier_kanata.torchlaunchermod;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModCreativeModeTabs;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(TorchLauncherMod.MODID)
public class TorchLauncherMod {

  public static final String MODID = "torchlaunchermod";
  public static final Logger LOGGER = LogUtils.getLogger();

  public TorchLauncherMod(IEventBus modEventBus, ModContainer modContainer) {
    TorchLauncherModItems.ITEMS.register(modEventBus);
    TorchLauncherModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    TorchLauncherModEntities.ENTITIES.register(modEventBus);

    modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {}

}
