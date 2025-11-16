package jp.atelier_kanata.torchlaunchermod.entity;

import javax.annotation.Nullable;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;

public class TorchLauncherProjectileEntity extends Projectile {

  private static final EntityDataAccessor<BlockPos> DATA_START_BLOCK_POS = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.BLOCK_POS);
  private static final EntityDataAccessor<ItemStack> DATA_ID_ITEM_STACK = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.ITEM_STACK);
  private static final EntityDataAccessor<Boolean> DATA_IN_GROUND = SynchedEntityData.defineId(TorchLauncherProjectileEntity.class, EntityDataSerializers.BOOLEAN);

  public TorchLauncherProjectileEntity(EntityType<? extends TorchLauncherProjectileEntity> entityType, Level level) {
    super(entityType, level);
  }

  public TorchLauncherProjectileEntity(Level level, @Nullable Entity shooter, double x, double y, double z, ItemStack itemStack) {
    super(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), level);
    this.setOwner(shooter);
    this.setPos(x, y, z);
    setStartBlockPos(this.blockPosition());
    setItemStack(itemStack);
  }

  public BlockState getBlockState() {
    return Block.byItem(getItemStack().getItem()).defaultBlockState();
  }

  public BlockPos getStartBlockPos() {
    return this.entityData.get(DATA_START_BLOCK_POS);
  }

  public ItemStack getItemStack() {
    return this.entityData.get(DATA_ID_ITEM_STACK);
  }

  private void setItemStack(ItemStack itemStack) {
    ItemStack itemStackCopy = itemStack.copy();
    itemStackCopy.remove(DataComponents.INTANGIBLE_PROJECTILE);
    this.entityData.set(DATA_ID_ITEM_STACK, itemStackCopy);
  }

  private void setStartBlockPos(BlockPos blockPos) {
    this.entityData.set(DATA_START_BLOCK_POS, blockPos);
  }

  protected boolean isInGround() {
    return this.entityData.get(DATA_IN_GROUND);
  }

  protected void setInGround(boolean inGround) {
    this.entityData.set(DATA_IN_GROUND, inGround);
  }

  @Override
  protected void onHitBlock(BlockHitResult result) {
    super.onHitBlock(result);
    setInGround(true);

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
        this.level().setBlock(setBlockPos, setBlockState, 3);
        this.gameEvent(GameEvent.BLOCK_PLACE, this.getOwner());
        this.playSound(setBlockState.getSoundType(this.level(), setBlockPos, null).getPlaceSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
      }
    }

    this.remove(RemovalReason.KILLED);
  }

  private void dropItemStack() {
    this.spawnAtLocation((ServerLevel) this.level(), getItemStack().copy());
  }

  private BlockState createTorchBlockState(Direction direction) {
    if (getItemStack().is(Items.TORCH)) {
      if (direction == Direction.UP) {
        return Blocks.TORCH.defaultBlockState();
      } else if (direction == Direction.DOWN) {
        return null;
      } else {
        return Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
      }
    } else if (getItemStack().is(Items.SOUL_TORCH)) {
      if (direction == Direction.UP) {
        return Blocks.SOUL_TORCH.defaultBlockState();
      } else if (direction == Direction.DOWN) {
        return null;
      } else {
        return Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
      }
    } else {
      Item item = getItemStack().getItem();
      return item instanceof BlockItem ? Block.byItem(item).defaultBlockState() : null;
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
    return isInGround() && this.level().noCollision(new AABB(this.position(), this.position()).inflate(0.06));
  }

  @Override
  public void move(MoverType type, Vec3 pos) {
    super.move(type, pos);
    if (type != MoverType.SELF && this.shouldFall()) {
      this.startFalling();
    }
  }

  private void startFalling() {
    setInGround(false);
    this.setDeltaMovement(
        this.getDeltaMovement().multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
  }

  @Override
  public void tick() {
    Vec3 deltaMovement = this.getDeltaMovement();
    BlockPos blockPos = this.blockPosition();
    BlockState blockState = this.level().getBlockState(blockPos);
    if (!blockState.isAir()) {
      VoxelShape voxelShape = blockState.getCollisionShape(this.level(), blockPos);
      if (!voxelShape.isEmpty()) {
        Vec3 entityPos = this.position();
        for (AABB aabb : voxelShape.toAabbs()) {
          if (aabb.move(blockPos).contains(entityPos)) {
            this.setDeltaMovement(Vec3.ZERO);
            setInGround(true);
            break;
          }
        }
      }
    }

    if (!isInGround()) {
      Vec3 entityPos = this.position();
      this.setXRot(lerpRotation(this.getXRot(), (float) (Mth.atan2(deltaMovement.y, deltaMovement.horizontalDistance()) * 180.0F / (float) Math.PI)));
      this.setYRot(lerpRotation(this.getYRot(), (float) (Mth.atan2(deltaMovement.x, deltaMovement.z) * 180.0F / (float) Math.PI)));

      BlockHitResult blockHitResult =
          this.level().clipIncludingBorder(new ClipContext(entityPos, entityPos.add(deltaMovement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
      this.stepMoveAndHit(blockHitResult);

      this.applyInertia(0.99F);
      this.applyGravity();

      super.tick();
    }
  }

  private void stepMoveAndHit(BlockHitResult blockHitResult) {
    Vec3 oldEntityPos = this.position();
    Vec3 newEntityPos = blockHitResult.getLocation();
    this.setPos(newEntityPos);
    this.applyEffectsFromBlocks(oldEntityPos, newEntityPos);
    if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
      this.handlePortal();
    }
    if (blockHitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, blockHitResult)) {
      this.hitTargetOrDeflectSelf(blockHitResult);
      this.hasImpulse = true;
    }
  }

  private void applyInertia(float inertia) {
    this.setDeltaMovement(this.getDeltaMovement().scale(inertia));
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
    builder.define(DATA_START_BLOCK_POS, BlockPos.ZERO);
    builder.define(DATA_ID_ITEM_STACK, getDefaultItemStack());
    builder.define(DATA_IN_GROUND, false);
  }

  private ItemStack getDefaultItemStack() {
    return new ItemStack(Items.TORCH);
  }

  @Override
  public void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);
    output.store("itemStack", ItemStack.CODEC, getItemStack());
    output.putBoolean("inGround", isInGround());
  }

  @Override
  public void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);
    this.setItemStack(input.read("itemStack", ItemStack.CODEC).orElse(getDefaultItemStack()));
    setInGround(input.getBooleanOr("inGround", false));
  }

}
