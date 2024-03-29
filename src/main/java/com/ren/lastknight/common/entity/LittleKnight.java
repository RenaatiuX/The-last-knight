package com.ren.lastknight.common.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class LittleKnight extends MonsterEntity implements IAnimatable, IAnimationTickable {
    private static final DataParameter<Boolean> SPAWN = EntityDataManager.createKey(LittleKnight.class, DataSerializers.BOOLEAN);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    private int knightID, spawningTick;

    public LittleKnight(EntityType<? extends LittleKnight> type, World worldIn) {
        super(type, worldIn);
        setSpawningTick(36);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 0.8D) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return !getSpawn() && super.shouldExecute();
            }
        });
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0F)
                .createMutableAttribute(Attributes.ARMOR, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SPAWN, true);
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.knightID = nbt.getInt("KnightID");
        this.setSpawn(nbt.getBoolean("spawning"));
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("KnightID", this.knightID);
        nbt.putBoolean("spawning", this.getSpawn());
    }

    public void setKnight(Knight knight) {
        this.knightID = knight.getEntityId();
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            Entity e = this.world.getEntityByID(knightID);
            if (e == null || !e.isAlive()) {
                this.setHealth(0);
            }
        }
        if (spawningTick > 0) {
            spawningTick--;
            if (spawningTick == 0)
                setSpawn(false);
        }
        super.tick();
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    public boolean getSpawn() {
        return this.dataManager.get(SPAWN);
    }

    public void setSpawn(boolean spawn) {
        this.dataManager.set(SPAWN, spawn);
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        super.onDeath(p_70645_1_);
        Entity e = world.getEntityByID(this.knightID);
        if (e instanceof Knight) {
            Knight knight = (Knight) e;
            knight.reduceAmountMinions();
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public void livingTick() {
        super.livingTick();
    }

    private PlayState predicate(AnimationEvent<LittleKnight> event) {
        if (this.getSpawn()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("miniknight.Emerge", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            if (event.getController().getAnimationState() == AnimationState.Stopped)
                setSpawn(false);
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("miniknight.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("miniknight.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent<LittleKnight> event) {
        if (this.isSwingInProgress && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("miniknight.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.isSwingInProgress = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    public void setSpawningTick(int spawningTick) {
        this.spawningTick = spawningTick;
    }

    public int getSpawningTick() {
        return spawningTick;
    }
}
