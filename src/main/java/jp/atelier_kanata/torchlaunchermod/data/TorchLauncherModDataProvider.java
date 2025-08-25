package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TorchLauncherMod.MODID)
public class TorchLauncherModDataProvider {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    generator.addProvider(event.includeClient(), new TorchLauncherModEnUsLanguageProvider(packOutput));
    generator.addProvider(event.includeClient(), new TorchLauncherModItemModelProvider(packOutput, event.getExistingFileHelper()));
    generator.addProvider(event.includeClient(), new TorchLauncherModRecipeProvider(packOutput, event.getLookupProvider()));
  }

}
