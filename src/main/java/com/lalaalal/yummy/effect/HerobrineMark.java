package com.lalaalal.yummy.effect;

import com.lalaalal.yummy.misc.EffectDamageSource;
import com.lalaalal.yummy.tags.YummyTagRegister;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class HerobrineMark extends MobEffect {
    public static void overlapMark(LivingEntity entity, @Nullable LivingEntity herobrine) {
        if (entity.getType().is(YummyTagRegister.HEROBRINE))
            return;

        final MobEffect HEROBRINE_MARK = YummyEffects.HEROBRINE_MARK.get();

        MobEffectInstance mobEffectInstance = entity.getEffect(HEROBRINE_MARK);
        if (mobEffectInstance != null) {
            int amplifier = Math.min(mobEffectInstance.getAmplifier() + 1, 6);
            entity.addEffect(new MobEffectInstance(HEROBRINE_MARK, 20 * 66, amplifier));
            if (amplifier == 6 && herobrine != null)
                herobrine.setHealth(herobrine.getHealth() + 66);
        } else {
            entity.addEffect(new MobEffectInstance(HEROBRINE_MARK, 20 * 66, 0));
        }
    }

    public static void overlapMark(LivingEntity entity) {
        overlapMark(entity, null);
    }

    public static void reduceMark(LivingEntity entity) {
        final MobEffect HEROBRINE_MARK = YummyEffects.HEROBRINE_MARK.get();
        MobEffectInstance markEffectInstance = entity.getEffect(HEROBRINE_MARK);
        if (markEffectInstance == null)
            return;

        int modifiedAmplifier = markEffectInstance.getAmplifier() - 1;
        int leftDuration = markEffectInstance.getDuration();
        entity.removeEffect(HEROBRINE_MARK);
        if (modifiedAmplifier >= 0)
            entity.addEffect(new MobEffectInstance(HEROBRINE_MARK, leftDuration, modifiedAmplifier));
    }

    protected HerobrineMark() {
        super(MobEffectCategory.HARMFUL, 0x441f0b);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getLevel().isClientSide && amplifier >= 6) {
            DamageSource damageSource = new EffectDamageSource("mark", this);
            damageSource.bypassArmor().bypassInvul();
            livingEntity.removeAllEffects();
            livingEntity.hurt(damageSource, Float.MAX_VALUE);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
