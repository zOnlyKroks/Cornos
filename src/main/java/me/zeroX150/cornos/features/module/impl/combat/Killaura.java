package me.zeroX150.cornos.features.module.impl.combat;

import com.google.common.base.CharMatcher;
import me.zeroX150.cornos.Cornos;
import me.zeroX150.cornos.etc.config.MConfMultiOption;
import me.zeroX150.cornos.etc.config.MConfNum;
import me.zeroX150.cornos.etc.config.MConfToggleable;
import me.zeroX150.cornos.features.module.Module;
import me.zeroX150.cornos.features.module.ModuleType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Killaura extends Module {
    MConfMultiOption mode = new MConfMultiOption("mode", "multi", new String[]{"single", "multi"}, "The mode");
    MConfToggleable entities = new MConfToggleable("entities", false, "Whether or not to attack entities");
    MConfToggleable players = new MConfToggleable("players", true, "Whether or not to attack players");
    MConfToggleable mobs = new MConfToggleable("mobs", false, "Whether or not to attack mobs");
    MConfToggleable swing = new MConfToggleable("swing", true, "Whether or not to send a swing packet");
    MConfNum range = new MConfNum("range", 4.0, 10.0, 1.0, "Self explanatory");
    MConfNum delay = new MConfNum("delay", 12.52, 20.0, 0, "How much delay in ticks to wait between attacks");
    MConfToggleable abNoname = new MConfToggleable("ab:noname", true, "Check if the entity has a name");
    MConfToggleable abColorName = new MConfToggleable("ab:colorname", true, "Check if the entity has a colored name");
    MConfToggleable abInvalidName = new MConfToggleable("ab:invalidName", false, "Check if the entity has an invalid name (Non a-zA-Z0-9)");
    MConfToggleable checkForHostile = new MConfToggleable("checkHostile", false, "Check if the entity is hostile towards you");
    int delayWaited = 0;
    List<LivingEntity> attacks = new ArrayList<>();

    public Killaura() {
        super("Killaura", "a white person on twitter when she sees a straight dude (ab = antibot)", ModuleType.COMBAT);
        this.mconf.add(mode);
        this.mconf.add(delay);
        this.mconf.add(players);
        this.mconf.add(mobs);
        this.mconf.add(entities);
        this.mconf.add(swing);
        this.mconf.add(range);
        this.mconf.add(abColorName);
        this.mconf.add(abNoname);
        this.mconf.add(abInvalidName);
        this.mconf.add(checkForHostile);
    }

    @Override
    public void onExecute() {
        delayWaited++;
        if (delayWaited > delay.getValue()) {
            delayWaited = 0;
        } else
            return;
        attacks.clear();
        assert Cornos.minecraft.player != null;
        Vec3d loc = Cornos.minecraft.player.getPos();
        Box selector = new Box(loc.add(-1, -1, -1), loc.add(1, 1, 1));
        selector = selector.expand(range.getValue());
        assert Cornos.minecraft.world != null;
        List<Entity> entities1 = Cornos.minecraft.world.getOtherEntities(Cornos.minecraft.player, selector);
        if (entities1.size() < 1)
            return;
        List<Entity> entities = new ArrayList<>();
        for (Entity e : entities1) {
            if ((e instanceof PlayerEntity) && !players.isEnabled())
                continue;
            if ((e instanceof HostileEntity) && !mobs.isEnabled())
                continue;
            if (!(e instanceof PlayerEntity) && !(e instanceof HostileEntity) && !this.entities.isEnabled())
                continue;
            if (Cornos.friendsManager.getFriends().containsKey(e.getName().asString()))
                continue;
            if (checkForHostile.isEnabled()) {
                if (e instanceof HostileEntity) {
                    HostileEntity m = (HostileEntity) e;
                    if (m.getTarget() != null && !m.getTarget().getUuid().equals(Cornos.minecraft.player.getUuid()))
                        continue;
                }
            }
            String n = e.getEntityName();
            List<Boolean> abThesis = new ArrayList<>();
            if (abColorName.isEnabled())
                abThesis.add(n.contains("§"));
            if (abNoname.isEnabled())
                abThesis.add(n.trim().isEmpty());
            if (abInvalidName.isEnabled())
                abThesis.add(!CharMatcher.anyOf("abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789_")
                        .matchesAllOf(n));
            boolean isBot = false;
            for (Boolean thesis : abThesis) {
                isBot = isBot || thesis;
            }
            if (isBot)
                continue;
            entities.add(e);
        }
        switch (mode.value) {
            case "single":
                for (Entity e : entities) {
                    if (!(e instanceof LivingEntity))
                        continue;
                    LivingEntity le1 = (LivingEntity) e;
                    if (le1 instanceof PlayerEntity
                            && Cornos.friendsManager.getFriends().containsKey(le1.getName().asString())) {
                        return;
                    }
                    if (le1.isDead() || !le1.isAttackable())
                        continue;
                    attacks.add(le1);
                    break;
                }
                break;
            case "multi":
                entities.forEach(entity -> {
                    if (!(entity instanceof LivingEntity))
                        return;
                    LivingEntity le1 = (LivingEntity) entity;
                    if (le1 instanceof PlayerEntity
                            && Cornos.friendsManager.getFriends().containsKey(le1.getName().asString())) {
                        return;
                    }
                    if (le1.isDead() || !le1.isAttackable())
                        return;
                    attacks.add(le1);
                });
                break;
        }
        for (Entity e : attacks) {
            assert Cornos.minecraft.interactionManager != null;
            Cornos.minecraft.interactionManager.attackEntity(Cornos.minecraft.player, e);
            if (swing.isEnabled()) {
                HandSwingC2SPacket p = new HandSwingC2SPacket(Hand.MAIN_HAND);
                Objects.requireNonNull(Cornos.minecraft.getNetworkHandler()).sendPacket(p);
            }
        }
        super.onExecute();

    }
}
