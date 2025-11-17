package jp.atelier_kanata.torchlaunchermod.data;

import java.util.concurrent.CompletableFuture;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TorchLauncherMod.MOD_ID)
public class TorchLauncherModDataProvider {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeClient(), new TorchLauncherModEnUsLanguageProvider(packOutput));
    generator.addProvider(event.includeClient(), new TorchLauncherModJaJpLanguageProvider(packOutput));
    generator.addProvider(event.includeClient(),
        new TorchLauncherModItemModelProvider(packOutput, existingFileHelper));
    BlockTagsProvider blockTagsProvider = new TorchLauncherModBlockTagsProvider(packOutput, lookupProvider,
        existingFileHelper);
    generator.addProvider(event.includeServer(), blockTagsProvider);
    generator.addProvider(event.includeServer(),
        new TorchLauncherModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(),
            existingFileHelper));
    generator.addProvider(event.includeClient(),
        new TorchLauncherModRecipeProvider(packOutput, lookupProvider));
  }

}
