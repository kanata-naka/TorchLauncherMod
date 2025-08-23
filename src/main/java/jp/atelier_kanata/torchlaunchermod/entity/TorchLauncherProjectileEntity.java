package jp.atelier_kanata.torchlaunchermod.entity;

import javax.annotation.Nullable;
import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchLauncherProjectileEntity extends Projectile {
  private static final EntityDataAccessor<ItemStack> DATA_ID_ITEM = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.ITEM_STACK);

  protected boolean inGround;

  public TorchLauncherProjectileEntity(EntityType<? extends TorchLauncherProjectileEntity> entityType, Level level) {
    super(entityType, level);
  }

  public TorchLauncherProjectileEntity(Level level, @Nullable Entity shooter, double x, double y, double z, ItemStack itemStack) {
    super(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), level);
    this.setPos(x, y, z);
    this.entityData.set(DATA_ID_ITEM, itemStack.getItem().getDefaultInstance());
    this.setOwner(shooter);
  }

  @Override
  protected void onHitBlock(BlockHitResult result) {
    super.onHitBlock(result);
    this.inGround = true;

    if (this.level().isClientSide) {
      return;
    }

    if (result.getType() != HitResult.Type.BLOCK) {
      return;
    }

    // Player player = null;
    // if (this.getOwner() instanceof Player p) {
    // player = p;
    // }
    //
    // BlockPlaceContext context = new BlockPlaceContext(this.level(), player,
    // InteractionHand.MAIN_HAND, this.entityData.get(DATA_ID_ITEM), result);


    BlockPos hitBlockPos = result.getBlockPos();
    if (this.level().getBlockState(hitBlockPos).isAir()) {
      return;
    }

    Direction direction = ((BlockHitResult) result).getDirection();
    TorchLauncherMod.LOGGER
        .info("[TorchLauncherProjectileEntity][onHitBlock] hit block: " + this.level().getBlockState(hitBlockPos).getBlock().toString() + ", direction: " + direction);

    BlockPos setBlockPos = switch (direction) {
      case UP -> hitBlockPos.above();
      case EAST -> hitBlockPos.east();
      case WEST -> hitBlockPos.west();
      case SOUTH -> hitBlockPos.south();
      case NORTH -> hitBlockPos.north();
      case DOWN -> hitBlockPos.below();
    };
    TorchLauncherMod.LOGGER.info("[TorchLauncherProjectileEntity][onHitBlock] replace block: " + this.level().getBlockState(setBlockPos).getBlock().toString());
    if (!this.level().getBlockState(setBlockPos).canBeReplaced()) {
      dropItem();
    } else {
      BlockState torchBlockState = createTorchBlockState(direction);
      if (torchBlockState == null || !torchBlockState.canSurvive(this.level(), setBlockPos)) {
        dropItem();
      } else {
        level().setBlock(setBlockPos, torchBlockState, 3);
        this.gameEvent(GameEvent.BLOCK_PLACE, this.getOwner());
        this.playSound(SoundEvents.WOOD_PLACE, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
      }
    }

    this.remove(RemovalReason.KILLED);
  }

  private BlockState createTorchBlockState(Direction direction) {
    ItemStack itemStack = this.entityData.get(DATA_ID_ITEM);
    if (itemStack.is(Items.TORCH)) {
      if (direction == Direction.UP) {
        return Blocks.TORCH.defaultBlockState();
      } else if (direction == Direction.DOWN) {
        return null;
      } else {
        return Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
      }
    } else if (itemStack.is(Items.SOUL_TORCH)) {
      if (direction == Direction.UP) {
        return Blocks.SOUL_TORCH.defaultBlockState();
      } else if (direction == Direction.DOWN) {
        return null;
      } else {
        return Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
      }
    } else {
      return null;
    }
  }

  private void dropItem() {
    ItemStack itemStack = this.entityData.get(DATA_ID_ITEM);
    this.spawnAtLocation(itemStack.copy());
  }

  @Override
  protected boolean canHitEntity(Entity target) {
    return false;
  }

  @Override
  protected double getDefaultGravity() {
    return 0.05;
  }

  private boolean shouldFall() {
    return this.inGround && this.level().noCollision(new AABB(this.position(), this.position()).inflate(0.06));
  }

  @Override
  public void move(MoverType type, Vec3 pos) {
    super.move(type, pos);
    if (type != MoverType.SELF && this.shouldFall()) {
      this.startFalling();
    }
  }

  private void startFalling() {
    this.inGround = false;
    Vec3 vec3 = this.getDeltaMovement();
    this.setDeltaMovement(vec3.multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
  }

  @Override
  public void tick() {
    super.tick();
    Vec3 vec3 = this.getDeltaMovement();
    if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
      double d0 = vec3.horizontalDistance();
      this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
      this.setXRot((float) (Mth.atan2(vec3.y, d0) * 180.0F / (float) Math.PI));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
    }

    BlockPos blockpos = this.blockPosition();
    BlockState blockstate = this.level().getBlockState(blockpos);
    if (!blockstate.isAir()) {
      VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
      if (!voxelshape.isEmpty()) {
        Vec3 vec31 = this.position();

        for (AABB aabb : voxelshape.toAabbs()) {
          if (aabb.move(blockpos).contains(vec31)) {
            this.inGround = true;
            break;
          }
        }
      }
    }

    if (!this.inGround) {
      Vec3 vec32 = this.position();
      Vec3 vec33 = vec32.add(vec3);
      HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
      if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
        if (!net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
          this.hitTargetOrDeflectSelf(hitresult);
          this.hasImpulse = true;
        }
      }

      vec3 = this.getDeltaMovement();
      double d5 = vec3.x;
      double d6 = vec3.y;
      double d1 = vec3.z;
      double d7 = this.getX() + d5;
      double d2 = this.getY() + d6;
      double d3 = this.getZ() + d1;
      double d4 = vec3.horizontalDistance();
      this.setYRot((float) (Mth.atan2(d5, d1) * 180.0F / (float) Math.PI));
      this.setXRot((float) (Mth.atan2(d6, d4) * 180.0F / (float) Math.PI));
      this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
      this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
      float f = 0.99F;
      this.setDeltaMovement(vec3.scale((double) f));
      this.applyGravity();
      this.setPos(d7, d2, d3);
      this.checkInsideBlocks();
    }
  }

  @Override
  protected Entity.MovementEmission getMovementEmission() {
    return Entity.MovementEmission.NONE;
  }

  @Override
  public boolean isAttackable() {
    return this.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE);
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    builder.define(DATA_ID_ITEM, new ItemStack(Items.TORCH));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("inGround", this.inGround);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.inGround = compound.getBoolean("inGround");
  }
}
