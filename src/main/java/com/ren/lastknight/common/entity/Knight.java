package com.ren.lastknight.common.entity;

import com.ren.lastknight.core.init.EntityInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;

public class Knight extends MonsterEntity implements IAnimatable, IAnimationTickable {

    public static final DataParameter<Integer> CHARGING = EntityDataManager.createKey(Knight.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> SUMMONING = EntityDataManager.createKey(Knight.class, DataSerializers.BOOLEAN);

    public static final int MAX_CHARGING = 50;

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int amountMinions = 0;

    public Knight(EntityType<? extends Knight> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new SummonMinionsGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 300.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10.0F)
                .createMutableAttribute(Attributes.ARMOR, 20.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 10.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CHARGING, 0);
        this.dataManager.register(SUMMONING, false);
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.dataManager.set(CHARGING, nbt.getInt("charging"));
        this.dataManager.set(SUMMONING, nbt.getBoolean("summoning"));
    }


    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("charging", this.dataManager.get(CHARGING));
        nbt.putBoolean("summoning", this.dataManager.get(SUMMONING));
    }


    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void livingTick() {
        if (!world.isRemote()) {
            if (this.dataManager.get(SUMMONING)) {
                this.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 1, 99, false, false));
            }
        }
        super.livingTick();
    }

    void spawnSkeletons() {
        World world = this.getEntityWorld();

        // Obtiene la posición y rotación actual de la entidad
        double posX = this.getPosX();
        double posY = this.getPosY();
        double posZ = this.getPosZ();
        float rotationYaw = this.rotationYaw;

        // Calcula la posición relativa de cada esqueleto
        double northX = posX + (2 * Math.sin(Math.toRadians(-rotationYaw)));
        double northZ = posZ + (2 * Math.cos(Math.toRadians(-rotationYaw)));

        double southX = posX + (2 * Math.sin(Math.toRadians(180 - rotationYaw)));
        double southZ = posZ + (2 * Math.cos(Math.toRadians(180 - rotationYaw)));

        double eastX = posX + (2 * Math.sin(Math.toRadians(90 - rotationYaw)));
        double eastZ = posZ + (2 * Math.cos(Math.toRadians(90 - rotationYaw)));

        double westX = posX + (2 * Math.sin(Math.toRadians(-90 - rotationYaw)));
        double westZ = posZ + (2 * Math.cos(Math.toRadians(-90 - rotationYaw)));

        // Crea los caballeros y los coloca en las posiciones relativas
        LittleKnight littleKnight1 = EntityInit.LITTLE_KNIGHT.get().create(world);
        littleKnight1.setPosition(northX, posY, northZ);
        littleKnight1.setKnight(this);
        littleKnight1.setAttackTarget(this.getAttackTarget());
        world.addEntity(littleKnight1);

        LittleKnight littleKnight2 = EntityInit.LITTLE_KNIGHT.get().create(world);
        littleKnight2.setPosition(southX, posY, southZ);
        littleKnight2.setKnight(this);
        littleKnight2.setAttackTarget(this.getAttackTarget());
        world.addEntity(littleKnight2);

        LittleKnight littleKnight3 = EntityInit.LITTLE_KNIGHT.get().create(world);
        littleKnight3.setPosition(eastX, posY, eastZ);
        littleKnight3.setKnight(this);
        littleKnight3.setAttackTarget(this.getAttackTarget());
        world.addEntity(littleKnight3);

        LittleKnight littleKnight4 = EntityInit.LITTLE_KNIGHT.get().create(world);
        littleKnight4.setPosition(westX, posY, westZ);
        littleKnight4.setKnight(this);
        littleKnight4.setAttackTarget(this.getAttackTarget());
        world.addEntity(littleKnight4);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.ticksExisted % 20 == 0) {
            this.heal(0.05F);
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    private PlayState predicate(AnimationEvent<Knight> event) {
        if (this.dataManager.get(SUMMONING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("summoning.Knight.new", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("Walk.Knight.new", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("Idle.Knight.new", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent<Knight> event) {
        if (this.isSwingInProgress && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("Attack2.knight.new", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.isSwingInProgress = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        addCharging((int) damageAmount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
            f1 += (float) EnchantmentHelper.getKnockbackModifier(this);
        }

        int i = EnchantmentHelper.getFireAspectModifier(this);
        if (i > 0) {
            entityIn.setFire(i * 4);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            addCharging((int) f);
            heal(f * .6f);
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).applyKnockback(f1 * 0.5F, (double) MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
                this.setMotion(this.getMotion().mul(0.6D, 1.0D, 0.6D));
            }

            if (entityIn instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) entityIn;
                this.func_233655_a_(playerentity, this.getHeldItemMainhand(), playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY);
            }

            this.applyEnchantments(this, entityIn);
            this.setLastAttackedEntity(entityIn);
        }

        return flag;
    }

    protected int getCharging() {
        return this.dataManager.get(CHARGING);
    }

    void resetCharging() {
        this.dataManager.set(CHARGING, 0);
    }

    protected void addCharging(int toAdd) {
        this.dataManager.set(CHARGING, MathHelper.clamp(getCharging() + toAdd, 0, MAX_CHARGING));
    }

    void setAmountMinions(int amountMinions) {
        this.amountMinions = Math.min(0, amountMinions);
    }

    void reduceAmountMinions() {
        this.amountMinions = Math.min(0, amountMinions - 1);
    }

    int getAmountMinions() {
        return amountMinions;
    }

    void setSummoning(boolean summoning) {
        this.dataManager.set(SUMMONING, summoning);
    }

    public boolean canSummon() {
        return getCharging() >= MAX_CHARGING && amountMinions <= 0;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    private void func_233655_a_(PlayerEntity p_233655_1_, ItemStack p_233655_2_, ItemStack p_233655_3_) {
        if (!p_233655_2_.isEmpty() && !p_233655_3_.isEmpty() && p_233655_2_.getItem() instanceof AxeItem && p_233655_3_.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
            if (this.rand.nextFloat() < f) {
                p_233655_1_.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                this.world.setEntityState(p_233655_1_, (byte) 30);
            }
        }

    }

}
