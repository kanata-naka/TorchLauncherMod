package jp.atelier_kanata.torchlaunchermod.registry;

import java.util.function.Supplier;
import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.client.renderer.entity.TorchLauncherProjectileRenderer;
import jp.atelier_kanata.torchlaunchermod.entity.TorchLauncherProjectileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = TorchLauncherMod.MOD_ID, value = Dist.CLIENT)
public class TorchLauncherModEntities {

  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
      TorchLauncherMod.MOD_ID);

  public static final Supplier<EntityType<TorchLauncherProjectileEntity>> TORCH_LAUNCHER_PROJECTILE_ENTITY = ENTITIES
      .register("torch_launcher_projectile_entity",
          () -> EntityType.Builder
              .<TorchLauncherProjectileEntity>of(TorchLauncherProjectileEntity::new, MobCategory.MISC).sized(1.0F, 1.0F)
              .eyeHeight(0.5F).clientTrackingRange(4)
              .updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE,
                  ResourceLocation.fromNamespaceAndPath(TorchLauncherMod.MOD_ID, "torch_launcher_projectile_entity"))));

  @SubscribeEvent
  public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), TorchLauncherProjectileRenderer::new);
  }

}
