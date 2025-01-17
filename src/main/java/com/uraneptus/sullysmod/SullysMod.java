package com.uraneptus.sullysmod;

import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.uraneptus.sullysmod.common.caps.SMEntityCap;
import com.uraneptus.sullysmod.common.entities.*;
import com.uraneptus.sullysmod.common.networking.SMPacketHandler;
import com.uraneptus.sullysmod.common.recipes.SMFeatureRecipeCondition;
import com.uraneptus.sullysmod.core.SMConfig;
import com.uraneptus.sullysmod.core.other.SMTextDefinitions;
import com.uraneptus.sullysmod.core.registry.*;
import com.uraneptus.sullysmod.data.client.*;
import com.uraneptus.sullysmod.data.server.SMRecipeProvider;
import com.uraneptus.sullysmod.data.server.advancements.SMAdvancementProvider;
import com.uraneptus.sullysmod.data.server.builtin.SMBuiltinEntriesProvider;
import com.uraneptus.sullysmod.data.server.loot.SMLootTableProvider;
import com.uraneptus.sullysmod.data.server.modifiers.SMAdvancementModifiersProvider;
import com.uraneptus.sullysmod.data.server.modifiers.SMLootModifierProvider;
import com.uraneptus.sullysmod.data.server.tags.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(SullysMod.MOD_ID)
@Mod.EventBusSubscriber(modid = SullysMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SullysMod {
    public static final String MOD_ID = "sullysmod";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public SullysMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::gatherData);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SMConfig.CLIENT);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SMConfig.COMMON);

        SMTextDefinitions.init();
        SMPetrifiedTreeVariants.init();

        REGISTRY_HELPER.register(bus);
        SMBlocks.BLOCKS.register(bus);
        SMItems.ITEMS.register(bus);
        SMBlockEntityTypes.BLOCK_ENTITY.register(bus);
        SMParticleTypes.PARTICLES.register(bus);
        SMPotions.POTIONS.register(bus);
        SMRecipeTypes.RECIPE_TYPES.register(bus);
        SMRecipeSerializer.SERIALIZERS.register(bus);
        SMLootItemConditions.ITEM_CONDITIONS.register(bus);
        SMPaintingVariants.PAINTINGS.register(bus);
        SMBiomeModifiers.BIOME_MODIFIERS.register(bus);
        SMTreeDecoratorTypes.TREE_DECORATORS.register(bus);
        com.uraneptus.sullysmod.core.registry.SMFeatures.FEATURES.register(bus);
        SMCreativeModeTabs.TABS.register(bus);
        SMFluids.FLUIDS.register(bus);
        SMFluidTypes.FLUID_TYPES.register(bus);

        CraftingHelper.register(new SMFeatureRecipeCondition.Serializer());

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation modPrefix(String path) {
        return new ResourceLocation(SullysMod.MOD_ID, path);
    }

    public static ResourceLocation blueprintPrefix(String path) {
        return new ResourceLocation(Blueprint.MOD_ID, path);
    }

    @SubscribeEvent
    public static void addEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(SMEntityTypes.LANTERNFISH.get(), Lanternfish.createAttributes().build());
        event.put(SMEntityTypes.TORTOISE.get(), Tortoise.createAttributes().build());
        event.put(SMEntityTypes.BOULDERING_ZOMBIE.get(), BoulderingZombie.createAttributes().build());
        event.put(SMEntityTypes.JUNGLE_SPIDER.get(), JungleSpider.createAttributes().build());
        event.put(SMEntityTypes.PIRANHA.get(), Piranha.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SMEntityCap.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SMBrewingRecipes.register();
            SMDispenseBehaviors.register();
            SMNoteBlockInstruments.register();
            SMPacketHandler.register();
        });
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(includeClient, new SMBlockStateProvider(packOutput, fileHelper));
        generator.addProvider(includeClient, new SMItemModelProvider(packOutput, fileHelper));
        generator.addProvider(includeClient, new SMSoundDefinitionsProvider(packOutput, fileHelper));
        generator.addProvider(includeClient, new SMLangProvider(packOutput, SullysMod.MOD_ID));
        generator.addProvider(includeClient, new SMSpriteSourceProvider(packOutput, fileHelper));

        SMBlockTagsProvider blockTagProvider = new SMBlockTagsProvider(packOutput, lookupProvider, fileHelper);
        generator.addProvider(includeServer, new SMEntityTagsProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(includeServer, blockTagProvider);
        generator.addProvider(includeServer, new SMItemTagsProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), fileHelper));
        generator.addProvider(includeServer, new SMBiomeTagsProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(includeServer, new SMPaintingVariantTagsProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(includeServer, new SMAdvancementModifiersProvider(packOutput, lookupProvider));
        generator.addProvider(includeServer, new SMLootTableProvider(packOutput));
        generator.addProvider(includeServer, new SMAdvancementProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(includeServer, new SMRecipeProvider(packOutput));
        generator.addProvider(includeServer, new SMLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(includeServer, new SMBuiltinEntriesProvider(packOutput, lookupProvider));
        generator.addProvider(includeServer, new SMFluidTagsProvider(packOutput, lookupProvider, SullysMod.MOD_ID, fileHelper));
    }
}