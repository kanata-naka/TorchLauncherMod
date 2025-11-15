package jp.atelier_kanata.torchlaunchermod.data;

import java.util.concurrent.CompletableFuture;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class TorchLauncherModRecipeProvider extends RecipeProvider {


  protected TorchLauncherModRecipeProvider(Provider registries, RecipeOutput output) {
    super(registries, output);
  }

  @Override
  protected void buildRecipes() {
    shaped(RecipeCategory.TOOLS, TorchLauncherModItems.TORCH_LAUNCHER.get())
        .pattern("aaa")
        .pattern("bcb")
        .pattern(" b ")
        .define('a', Items.STRING)
        .define('b', Items.STICK)
        .define('c', Items.LEATHER)
        .unlockedBy("has_leather",has(Items.LEATHER))
        .save(this.output);
  }

  public static class Runner extends RecipeProvider.Runner {
    public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
      super(output, lookupProvider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
      return new TorchLauncherModRecipeProvider(provider, output);
    }

    @Override
    public String getName() {
      return "Torch Lancher Mod Recipes";
    }
  }
}
