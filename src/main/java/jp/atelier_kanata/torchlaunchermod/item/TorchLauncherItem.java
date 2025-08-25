package jp.atelier_kanata.torchlaunchermod.item;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import jp.atelier_kanata.torchlaunchermod.Config;
import jp.atelier_kanata.torchlaunchermod.entity.TorchLauncherProjectileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

public class TorchLauncherItem extends ProjectileWeaponItem {

  public TorchLauncherItem(Properties properties) {
    super(properties);
  }

  @Override
  public void releaseUsing(ItemStack weaponItemStack, Level level, LivingEntity livingEntity, int timeLeft) {
    if (!(livingEntity instanceof Player player)) {
      return;
    }

    ItemStack projectileItemStack = player.getProjectile(weaponItemStack);
    if (projectileItemStack.isEmpty()) {
      return;
    }

    int charge = this.getUseDuration(weaponItemStack, livingEntity) - timeLeft;
    charge = net.neoforged.neoforge.event.EventHooks.onArrowLoose(weaponItemStack, level, player, charge, true);
    if (charge < 0)
      return;

    float power = getPowerForTime(charge);
    if ((double) power < 0.1) {
      return;
    }

    List<ItemStack> drewProjectileItemStackList = draw(weaponItemStack, projectileItemStack, player);
    if (level instanceof ServerLevel serverLevel && !drewProjectileItemStackList.isEmpty()) {
      this.shoot(serverLevel, player, player.getUsedItemHand(), weaponItemStack, drewProjectileItemStackList, power * 3.0F, 1.0F, false, null);
    }
    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
        1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
    player.awardStat(Stats.ITEM_USED.get(this));
  }

  private static float getPowerForTime(float charge) {
    float f = (float) charge / 20.0F;
    f = (f * f + f * 2.0F) / 3.0F;
    if (f > 1.0F) {
      f = 1.0F;
    }
    return f;
  }

  @Override
  protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target) {
    projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
  }

  @Override
  protected Projectile createProjectile(Level level, LivingEntity livingEntity, ItemStack weaponItemStack, ItemStack ammoItemStack, boolean isCrit) {
    return new TorchLauncherProjectileEntity(level, livingEntity, livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ(), ammoItemStack);
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
    return 72000;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    ItemStack itemStackInHand = player.getItemInHand(interactionHand);
    boolean hasProjectile = !player.getProjectile(itemStackInHand).isEmpty();
    InteractionResultHolder<ItemStack> result = EventHooks.onArrowNock(itemStackInHand, level, player, interactionHand, hasProjectile);
    if (result != null)
      return result;

    if (!player.hasInfiniteMaterials() && !hasProjectile) {
      return InteractionResultHolder.fail(itemStackInHand);
    } else {
      player.startUsingItem(interactionHand);
      return InteractionResultHolder.consume(itemStackInHand);
    }
  }

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    // itemStack -> itemStack.is(Items.TORCH) || itemStack.is(Items.SOUL_TORCH)
    return itemStack -> Config.LAUNCHABLE_ITEMS.get().stream().anyMatch(name -> BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(ResourceLocation.parse(name)));
  }

  @Override
  public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack weaponItemStack) {
    return Items.TORCH.getDefaultInstance();
  }

  @Override
  public int getDefaultProjectileRange() {
    return 15;
  }


  @Override
  public UseAnim getUseAnimation(ItemStack itemStack) {
    return UseAnim.BOW;
  }

}
