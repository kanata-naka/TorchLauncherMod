package jp.atelier_kanata.torchlaunchermod;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModCreativeModeTabs;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TorchLauncherMod.MODID)
public class TorchLauncherMod {
  // Define mod id in a common place for everything to reference
  public static final String MODID = "torchlaunchermod";
  // Directly reference a slf4j logger
  public static final Logger LOGGER = LogUtils.getLogger();


  // The constructor for the mod class is the first code that is run when your mod is loaded.
  // FML will recognize some parameter types like IEventBus or ModContainer and pass them in
  // automatically.
  public TorchLauncherMod(IEventBus modEventBus, ModContainer modContainer) {
    // Register the commonSetup method for modloading
    modEventBus.addListener(this::commonSetup);

    // Register the Deferred Register to the mod event bus so items get registered
    TorchLauncherModItems.ITEMS.register(modEventBus);
    // Register the Deferred Register to the mod event bus so tabs get registered
    TorchLauncherModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

    TorchLauncherModEntities.ENTITIES.register(modEventBus);

    // Register our mod's ModConfigSpec so that FML can create and load the config file for us
    modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }

  private void commonSetup(FMLCommonSetupEvent event) {
    // Some common setup code
    LOGGER.info("HELLO FROM COMMON SETUP");

    if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
      LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
    }

    LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

    Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
    LOGGER.info("HELLO from server starting");
  }
}
