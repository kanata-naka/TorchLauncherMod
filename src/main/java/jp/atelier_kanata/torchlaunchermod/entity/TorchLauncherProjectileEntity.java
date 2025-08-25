package jp.atelier_kanata.torchlaunchermod.entity;

import javax.annotation.Nullable;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;

public class TorchLauncherProjectileEntity extends Projectile {

  protected static final EntityDataAccessor<ItemStack> DATA_ID_ITEM_STACK = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.ITEM_STACK);
  protected static final EntityDataAccessor<BlockPos> DATA_START_BLOCK_POS = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.BLOCK_POS);

  protected boolean inGround;

  public TorchLauncherProjectileEntity(EntityType<? extends TorchLauncherProjectileEntity> entityType, Level level) {
    super(entityType, level);
  }

  public TorchLauncherProjectileEntity(Level level, @Nullable Entity shooter, double x, double y, double z, ItemStack itemStack) {
    super(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), level);
    this.setOwner(shooter);
    this.setPos(x, y, z);
    setItemStack(itemStack);
    getItemStack().remove(DataComponents.INTANGIBLE_PROJECTILE);
    setStartBlockPos(this.blockPosition());
  }

  public ItemStack getItemStack() {
    return this.entityData.get(DATA_ID_ITEM_STACK);
  }

  protected void setItemStack(ItemStack itemStack) {
    this.entityData.set(DATA_ID_ITEM_STACK, itemStack.copy());
  }

  public BlockPos getStartBlockPos() {
    return this.entityData.get(DATA_START_BLOCK_POS);
  }

  protected void setStartBlockPos(BlockPos blockPos) {
    this.entityData.set(DATA_START_BLOCK_POS, blockPos);
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

    BlockPos hitBlockPos = result.getBlockPos();
    if (this.level().getBlockState(hitBlockPos).isAir()) {
      return;
    }

    Direction direction = ((BlockHitResult) result).getDirection();
    BlockPos setBlockPos = switch (direction) {
      case UP -> hitBlockPos.above();
      case EAST -> hitBlockPos.east();
      case WEST -> hitBlockPos.west();
      case SOUTH -> hitBlockPos.south();
      case NORTH -> hitBlockPos.north();
      case DOWN -> hitBlockPos.below();
    };
    if (!this.level().getBlockState(setBlockPos).canBeReplaced()) {
      dropItemStack();
    } else {
      BlockState setBlockState = createTorchBlockState(direction);
      if (setBlockState == null || !setBlockState.canSurvive(this.level(), setBlockPos)) {
        dropItemStack();
      } else {
        level().setBlock(setBlockPos, setBlockState, 3);
        this.gameEvent(GameEvent.BLOCK_PLACE, this.getOwner());
        this.playSound(SoundEvents.WOOD_PLACE, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
      }
    }

    this.remove(RemovalReason.KILLED);
  }

  private void dropItemStack() {
    this.spawnAtLocation(getItemStack().copy());
  }

  private BlockState createTorchBlockState(Direction direction) {
    ItemStack itemStack = getItemStack();
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
      return itemStack.getItem() instanceof BlockItem ? Block.byItem(itemStack.getItem()).defaultBlockState() : null;
    }
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
    this.setDeltaMovement(
        this.getDeltaMovement().multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
  }

  @Override
  public void tick() {
    super.tick();
    Vec3 deltaMovement = this.getDeltaMovement();
    if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
      this.setYRot((float) (Mth.atan2(deltaMovement.x, deltaMovement.z) * 180.0F / (float) Math.PI));
      this.setXRot((float) (Mth.atan2(deltaMovement.y, deltaMovement.horizontalDistance()) * 180.0F / (float) Math.PI));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
    }

    BlockPos blockPos = this.blockPosition();
    BlockState blockState = this.level().getBlockState(blockPos);
    Vec3 position = this.position();
    if (!blockState.isAir()) {
      VoxelShape voxelShape = blockState.getCollisionShape(this.level(), blockPos);
      if (!voxelShape.isEmpty()) {
        for (AABB aabb : voxelShape.toAabbs()) {
          if (aabb.move(blockPos).contains(position)) {
            this.inGround = true;
            break;
          }
        }
      }
    }

    if (!this.inGround) {
      HitResult hitResult = this.level().clip(new ClipContext(position, position.add(deltaMovement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
      if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
        if (!EventHooks.onProjectileImpact(this, hitResult)) {
          this.hitTargetOrDeflectSelf(hitResult);
          this.hasImpulse = true;
        }
      }

      this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(deltaMovement.y, deltaMovement.horizontalDistance()) * 180.0F / (float) Math.PI)));
      this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(deltaMovement.x, deltaMovement.z) * 180.0F / (float) Math.PI)));
      this.setDeltaMovement(deltaMovement.scale(0.99D));
      this.applyGravity();
      this.setPos(this.getX() + deltaMovement.x, this.getY() + deltaMovement.y, this.getZ() + deltaMovement.z);
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
    builder.define(DATA_ID_ITEM_STACK, new ItemStack(Items.TORCH));
    builder.define(DATA_START_BLOCK_POS, BlockPos.ZERO);
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
