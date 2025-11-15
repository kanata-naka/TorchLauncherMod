package jp.atelier_kanata.torchlaunchermod.registry;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.item.TorchLauncherItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TorchLauncherModItems {

  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TorchLauncherMod.MODID);

  public static final DeferredItem<Item> TORCH_LAUNCHER = ITEMS.registerItem("torch_launcher", (prop) -> new TorchLauncherItem(prop.durability(384)));

}
