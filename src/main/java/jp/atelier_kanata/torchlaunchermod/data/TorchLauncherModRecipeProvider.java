package jp.atelier_kanata.torchlaunchermod.data;

import java.util.concurrent.CompletableFuture;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class TorchLauncherModRecipeProvider extends RecipeProvider {

  public TorchLauncherModRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
    super(output, registries);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TorchLauncherModItems.TORCH_LAUNCHER.get())
        .pattern("aaa")
        .pattern("bcb")
        .pattern(" b ")
        .define('a', Items.STRING)
        .define('b', Items.STICK)
        .define('c', Items.LEATHER)
        .unlockedBy("has_leather",has(Items.LEATHER))
        .save(recipeOutput);
  }

}
