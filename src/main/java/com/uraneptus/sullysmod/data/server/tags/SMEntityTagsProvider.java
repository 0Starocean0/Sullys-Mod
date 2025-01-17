package com.uraneptus.sullysmod.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.uraneptus.sullysmod.SullysMod;
import com.uraneptus.sullysmod.core.other.tags.SMEntityTags;
import com.uraneptus.sullysmod.core.registry.SMEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SMEntityTagsProvider extends EntityTypeTagsProvider {
    public SMEntityTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, pProvider, SullysMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(SMEntityTypes.THROWN_THROWING_KNIFE.get());

        tag(BlueprintEntityTypeTags.FISHES).add(
                SMEntityTypes.LANTERNFISH.get(),
                SMEntityTypes.PIRANHA.get()
        );

        tag(SMEntityTags.SCARES_TORTOISES).add(
                //Undead
                EntityType.ZOMBIE,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.DROWNED,
                EntityType.HUSK,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.PHANTOM,
                EntityType.SKELETON_HORSE,
                EntityType.WITHER,
                EntityType.ZOGLIN,
                EntityType.ZOMBIE_HORSE,
                EntityType.ZOMBIFIED_PIGLIN,

                //Illager
                EntityType.PILLAGER,
                EntityType.VINDICATOR,
                EntityType.EVOKER,
                EntityType.ILLUSIONER,
                EntityType.RAVAGER,
                EntityType.EVOKER_FANGS,
                EntityType.VEX
        );
        tag(SMEntityTags.ATTACKS_BABY_TORTOISES).add(
                EntityType.ZOMBIE,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.HUSK,
                EntityType.DROWNED,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.OCELOT,
                EntityType.WOLF,
                EntityType.CAT
                );

        tag(SMEntityTags.CANNOT_BOUNCE).add(
                EntityType.SMALL_FIREBALL,
                EntityType.FIREBALL,
                EntityType.DRAGON_FIREBALL,
                EntityType.SHULKER_BULLET,
                EntityType.WITHER_SKULL,
                EntityType.FISHING_BOBBER
        );

        tag(SMEntityTags.CANNOT_BE_FLUNG).add(
                EntityType.SMALL_FIREBALL,
                EntityType.FIREBALL,
                EntityType.DRAGON_FIREBALL,
                EntityType.SHULKER_BULLET,
                EntityType.WITHER_SKULL,
                EntityType.FISHING_BOBBER
        ).addOptional(new ResourceLocation("botania", "mana_burst"));

        tag(SMEntityTags.IS_LIVING_INORGANIC).add(
                EntityType.IRON_GOLEM,
                EntityType.SKELETON,
                EntityType.SKELETON_HORSE,
                EntityType.ALLAY,
                EntityType.STRAY,
                EntityType.VEX,
                EntityType.WITHER,
                EntityType.WITHER_SKELETON
        );
        tag(SMEntityTags.PIRANHA_ALWAYS_ATTACKS).add(
                EntityType.BAT,
                EntityType.BEE,
                EntityType.CHICKEN,
                EntityType.COD,
                EntityType.ENDERMITE,
                EntityType.SALMON,
                EntityType.PUFFERFISH,
                EntityType.RABBIT,
                EntityType.SILVERFISH,
                EntityType.TADPOLE,
                EntityType.TROPICAL_FISH
        );
        tag(SMEntityTags.SPAWN_IN_AMBER).add(
                EntityType.BEE,
                EntityType.SPIDER,
                EntityType.CAVE_SPIDER,
                EntityType.SILVERFISH,
                SMEntityTypes.JUNGLE_SPIDER.get()
        );
    }
}
