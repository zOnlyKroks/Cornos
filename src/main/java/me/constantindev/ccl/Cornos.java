package me.constantindev.ccl;

import me.constantindev.ccl.etc.FriendsManager;
import me.constantindev.ccl.etc.NotificationManager;
import me.constantindev.ccl.etc.base.Module;
import me.constantindev.ccl.etc.config.CConf;
import me.constantindev.ccl.etc.event.EventHelper;
import me.constantindev.ccl.etc.helper.ConfMan;
import me.constantindev.ccl.etc.helper.KeybindMan;
import me.constantindev.ccl.etc.helper.STL;
import me.constantindev.ccl.etc.reg.CommandRegistry;
import me.constantindev.ccl.etc.reg.ModuleRegistry;
import me.constantindev.ccl.module.ext.ClientConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.ArrayList;
import net.minecraft.entity.EquipmentSlot;
import java.util.List;
import java.awt.*;
import java.util.UUID;


import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;


public class Cornos implements ModInitializer {

    public static final String MOD_ID = "ccl";
    public static final String MOD_NAME = "Cornos";
    public static SoundEvent BONG_SOUND = new SoundEvent(new Identifier("ccl", "bong"));
    public static SoundEvent HITMARKER_SOUND = new SoundEvent(new Identifier("ccl", "hitmarker"));
    public static SoundEvent VINEBOOM_SOUND = new SoundEvent(new Identifier("ccl", "vineboom"));
    public static Logger LOGGER = LogManager.getLogger();
    public static MinecraftClient minecraft = MinecraftClient.getInstance();
    public static Thread fastUpdater;
    public static NotificationManager notifMan;
    public static FriendsManager friendsManager;
    public static List<String> capes = new ArrayList<>();

    public static me.constantindev.ccl.module.ext.ClientConfig config;

    //itemgroups (pog)
    public static final ItemGroup GENERAL = FabricItemGroupBuilder.create(
        new Identifier("ccl", "general"))
        .icon(() -> new ItemStack(Items.BOOK))
        .appendItems(stacks ->{
            //sussed from the funny items screen (could add more later)

            ItemStack is = new ItemStack(Items.BAT_SPAWN_EGG);
            is.setCustomName(Text.of("§r§cOuch. §4Generated by §lCornos"));
            CompoundTag ct = is.getOrCreateSubTag("EntityTag");
            ListTag vel = new ListTag();
            vel.add(DoubleTag.of(0));
            vel.add(DoubleTag.of(-0.5));
            vel.add(DoubleTag.of(0));
            ct.put("power", vel);
            ct.put("id", StringTag.of("minecraft:fireball"));
            ct.put("ExplosionPower", DoubleTag.of(65535));

            ItemStack isi = new ItemStack(Items.BAT_SPAWN_EGG);
            isi.setCustomName(Text.of("§r§cBlockban. §4Generated by §lCornos"));
            CompoundTag cti = isi.getOrCreateSubTag("EntityTag");
            cti.put("id", StringTag.of("minecraft:area_effect_cloud"));
            cti.put("Particle", StringTag.of("barrier"));
            cti.put("Radius", FloatTag.of(16777216f));
            cti.put("RadiusPerTick", FloatTag.of(0f));
            cti.put("Duration", IntTag.of(1310700));
            cti.put("WaitTime", IntTag.of(60));

            ItemStack isa = new ItemStack(Items.ARMOR_STAND);
            isa.setCustomName(Text.of("§r§cLagmor stand. §4Generated by §lCornos"));
            CompoundTag cta = isa.getOrCreateSubTag("EntityTag");
            cta.put("CustomName", StringTag.of("\"" + rndAscii(32000) + "\""));
            cta.put("CustomNameVisible", ByteTag.of(true));

            ItemStack stand = new ItemStack(Items.ARMOR_STAND);
            CompoundTag standTag = stand.getOrCreateTag();
            standTag.put("HideFlags", IntTag.of(63));
            CompoundTag entityTagt = new CompoundTag();
            entityTagt.put("CustomName", StringTag.of("{\"text\":\"" + rndAscii(52731) + "\",\"obfuscated\":true}"));
            entityTagt.put("Invulnerable", ByteTag.of(true));
            entityTagt.put("Invisible", ByteTag.of(true));
            entityTagt.put("CustomNameVisible", ByteTag.of(true));
            standTag.put("EntityTag", entityTagt);
            stand.addEnchantment(Enchantments.PROTECTION, 1);
            stand.setCustomName(Text.of("§r§cKick stand. §4Generated by §lCornos"));

            ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
            helmet.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.nameUUIDFromBytes(new byte[]{
                    (byte) 1772998660,
                    (byte) 874858241,
                    (byte) -1911483451,
                    (byte) 795787135
            }), "generic.movement_speed", Double.POSITIVE_INFINITY + 1, EntityAttributeModifier.Operation.fromId(0)), EquipmentSlot.HEAD);
            helmet.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.nameUUIDFromBytes(new byte[]{
                    (byte) -774122372,
                    (byte) -1202042950,
                    (byte) -1378952527,
                    (byte) -1780667942
            }), "generic.movement_speed", Double.NEGATIVE_INFINITY - 1, EntityAttributeModifier.Operation.fromId(0)), EquipmentSlot.HEAD);
            CompoundTag tag = helmet.getOrCreateTag();
            tag.put("HideFlags", IntTag.of(71));
            tag.put("Unbreakable", ByteTag.of(true));
            helmet.addEnchantment(Enchantments.PROTECTION, 1);
            helmet.setCustomName(Text.of("§r§cTroll helmet. §4Generated by §lCornos"));
            
            ItemStack stacke = new ItemStack(Items.BAT_SPAWN_EGG);
            stacke.setCustomName(Text.of("§r§cNo Entity Kill. §4Generated by §aCornos"));
            CompoundTag cte = stacke.getOrCreateTag();
            CompoundTag entityTag = new CompoundTag();
            entityTag.put("LootTable", StringTag.of(""));
            entityTag.putString("id", "minecraft:chest_minecart");
            entityTag.putByte("Invulnerable", (byte) 1);
            entityTag.putByte("Invisible", (byte) 1);
            entityTag.putByte("NoGravity", (byte) 1);
            cte.put("EntityTag", entityTag);

            stacks.add(is);
            stacks.add(isi);
            stacks.add(isa);
            stacks.add(stand);
            stacks.add(helmet);
            stacks.add(stacke);

        })
        .build();

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static void onMinecraftCreate() {
        if (config.mconf.getByName("customProcessIcon").value.equals("on")) {
            InputStream inputStream = Cornos.class.getClassLoader().getResourceAsStream("assets/ccl/icon1.png");
            Cornos.minecraft.getWindow().setIcon(inputStream, inputStream);
        }
    }

    public static void openCongratsScreen() {
        ConfirmScreen cs1 = new ConfirmScreen(t -> {
            if (t) {
                Util.getOperatingSystem().open("https://github.com/AriliusClient/Cornos/issues/new?title=Broke%20the%20client");
            } else Cornos.minecraft.openScreen(null);
        }, Text.of("Congrats!"), Text.of("You broke the client! Dare to tell me how you did it?"));
        Cornos.minecraft.openScreen(cs1);
    }

    @Override
    public void onInitialize() {
        Runtime.getRuntime().addShutdownHook(new Thread(ConfMan::sconf));
        log(Level.INFO, "Initializing main client");
        Registry.register(Registry.SOUND_EVENT, BONG_SOUND.getId(), BONG_SOUND);
        Registry.register(Registry.SOUND_EVENT, HITMARKER_SOUND.getId(), HITMARKER_SOUND);
        notifMan = new NotificationManager();
        friendsManager = new FriendsManager();

        log(Level.INFO, "Initializing configuration");
        CConf.init();
        log(Level.INFO, "Registering event bus");
        EventHelper.BUS.init();
        log(Level.INFO, "Initializing command registry");
        CommandRegistry.init();
        log(Level.INFO, "Initializing module registry");
        ModuleRegistry.init();
        config = (me.constantindev.ccl.module.ext.ClientConfig) ModuleRegistry.search(ClientConfig.class);
        log(Level.INFO, "Loading the configuration file");
        ConfMan.lconf();
        log(Level.INFO, "Registering all keybinds");
        KeybindMan.init();
        try {
            log(Level.INFO, "Getting capes");
            capes = STL.downloadCapes();
            log(Level.INFO, "Contributor UUIDs:");
            for (String cape : capes) {
                log(Level.INFO, "  " + cape);
            }
        } catch (IOException e) {
            log(Level.INFO, "Failed to download capes.");
            e.printStackTrace();
        }
        log(Level.INFO, "All features registered. Ready to load game");

        fastUpdater = new Thread(() -> {
            while (true) {
                try {
                    if (Cornos.minecraft.player != null) {
                        for (Module m : ModuleRegistry.getAll()) {
                            if (m.isEnabled()) m.onFastUpdate();
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception ignored) {
                }
            }
        });
        fastUpdater.start();
    }

    //had to do this because yes
    private static String rndAscii(int size) {
        StringBuilder end = new StringBuilder();
        for (int i = 0; i < size; i++) {
            // 97+25
            end.append((char) (new Random().nextInt(25) + 97));
        }
        return end.toString();
    }

}