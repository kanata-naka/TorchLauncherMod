package jp.atelier_kanata.torchlaunchermod.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TorchLauncherModBlockTagsProvider extends BlockTagsProvider {

    public TorchLauncherModBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TorchLauncherMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
    }

}
