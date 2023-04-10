package com.lalaalal.yummy.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.lalaalal.yummy.entity.ThrownSpearOfLonginus;
import com.lalaalal.yummy.misc.ItemDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpearOfLonginusItem extends Item {
    public SpearOfLonginusItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    private void hurtUser(ItemStack itemStack, LivingEntity user, float damageRate) {
        DamageSource damageSource = new ItemDamageSource("spear_of_longinus", null, itemStack);
        damageSource.bypassArmor();
        float damage = user.getMaxHealth() * damageRate;
        user.hurt(damageSource, damage);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        DamageSource damageSource = new ItemDamageSource("spear_of_longinus", attacker, itemStack).bypassArmor().bypassInvul();
        damageSource.bypassArmor().bypassInvul();
        target.hurt(damageSource, Float.MAX_VALUE);
        hurtUser(itemStack, attacker, 0.8f);

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(itemStack) - timeLeft;
            if (i >= 10 && !level.isClientSide) {
                ThrownSpearOfLonginus thrownSpearOfLonginus = new ThrownSpearOfLonginus(level, player, itemStack);
                thrownSpearOfLonginus.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    thrownSpearOfLonginus.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                level.addFreshEntity(thrownSpearOfLonginus);
                level.playSound(null, thrownSpearOfLonginus, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(itemStack);
                    hurtUser(itemStack, player, 0.9f);
                }
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ImmutableMultimap.of();
    }
}
