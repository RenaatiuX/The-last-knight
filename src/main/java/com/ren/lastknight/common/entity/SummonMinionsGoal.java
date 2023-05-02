package com.ren.lastknight.common.entity;

import net.minecraft.entity.ai.goal.Goal;

public class SummonMinionsGoal extends Goal {

    protected final Knight entity;
    protected int tickCounter = 0;

    public SummonMinionsGoal(Knight entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return entity.canSummon();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return tickCounter <= 74;
    }

    @Override
    public void startExecuting() {
        entity.setSummoning(true);
        entity.resetCharging();
    }

    @Override
    public void tick() {
        if (tickCounter == 20){
            entity.spawnSkeletons();
        }
        tickCounter++;
    }

    @Override
    public void resetTask() {
        tickCounter = 0;
        entity.setSummoning(false);
    }
}
