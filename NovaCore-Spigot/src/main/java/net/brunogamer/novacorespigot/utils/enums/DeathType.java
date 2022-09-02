package net.brunogamer.novacorespigot.utils.enums;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public enum DeathType {
    PROJECTILE_ARROW,PROJECTILE_OTHER,CACTUS,CACTUS_COMBAT,DROWN,DROWN_COMBAT,ELYTRA_WALL,ELYTRA_WALL_COMBAT,EXPLOSION,EXPLOSION_COMBAT,INTENTIONAL_GAME_DESIGN,
    FALL_SMALL,FALL_SMALL_COMBAT,FALL_BIG,FALL_CLIMBING_LADDER,FALL_CLIMBING_VINE,FALL_CLIMBING_WEEPING_VINE,FALL_CLIMBING_TWISTING_VINES,FALL_CLIMBING_SCAFFOLDING,
    FALL_CLIMBING_OTHER,FALL_WATER, FALL_STALAGMITE, FALL_STALAGMITE_COMBAT,ANVIL_FALL,ANVIL_FALL_COMBAT,BLOCK_FALL,BLOCK_FALL_COMBAT,STALAGTITE_FALL,
    STALAGTITE_FALL_COMBAT,FIRE_SOURCE,FIRE_SOURCE_COMBAT,FIRE_NATURAL, FIRE_NATURAL_COMBAT, ROCKET_EXPLOSION,ROCKET_EXPLOSION_COMBAT_RENAMED,LAVA, LAVA_COMBAT, LIGHTNING,
    LIGHTNING_COMBAT, MAGMA_BLOCK, MAGMA_BLOCK_COMBAT, MAGIC, MAGIC_COMBAT_ACCIDENT, MAGIC_COMBAT, FROZEN, FROZEN_COMBAT,COMBAT_NORMAL,COMBAT_FIREBALL, COMBAT_BEE,
    COMBAT_WITHER, COMBAT_WARDEN, STARVING, STARVING_COMBAT, SUFFOCATION, SUFFOCATION_COMBAT,SUFFOCATION_CRAMMING, SUFFOCATION_CRAMMING_COMBAT, BUSH, BUSH_COMBAT,
    THORNS, TRIDENT, VOID, VOID_COMBAT, EFFECT_WITHER, EFFECT_WITHER_COMBAT, DEHYDRATION, DEHYDRATION_COMBAT, GENERIC, GENERIC_COMBAT, PROJECTILE_FALL,
    PROJECTILE_FALL_COMBAT, DRAGON_BREATH, DRAGON_BREATH_COMBAT;

    public static DeathType getDeathFromString(String death) {
        if (death.contains("was shot by")) {
            return PROJECTILE_ARROW;
        } else if (death.contains("was pummeled by")) {
            return PROJECTILE_OTHER;
        } else if (death.contains("was pricked to death")) {
            return CACTUS;
        } else if (death.contains("walked into a cactus whilst trying to escape")) {
            return CACTUS_COMBAT;
        } else if (death.contains("drowned whilst trying to escape")) {
            return DROWN_COMBAT;
        } else if (death.contains("drowned")) {
            return DROWN;
        } else if (death.contains("experienced kinetic energy whilst trying to escape")) {
            return ELYTRA_WALL_COMBAT;
        } else if (death.contains("experienced kinetic energy")) {
            return ELYTRA_WALL;
        } else if (death.contains("blew up")) {
            return EXPLOSION;
        } else if (death.contains("was blown up by")) {
            return EXPLOSION_COMBAT;
        } else if (death.contains("hit the ground too hard whilst trying to escape")) {
            return FALL_SMALL_COMBAT;
        } else if (death.contains("hit the ground too hard")) {
            return FALL_SMALL;
        } else if (death.contains("fell from a high place")) {
            return FALL_BIG;
        } else if (death.contains("fell off a ladder")) {
            return FALL_CLIMBING_LADDER;
        } else if (death.contains("fell off some vines")) {
            return FALL_CLIMBING_VINE;
        } else if (death.contains("fell off some weeping vines")) {
            return FALL_CLIMBING_WEEPING_VINE;
        } else if (death.contains("fell off some twisting vines")) {
            return FALL_CLIMBING_TWISTING_VINES;
        } else if (death.contains("fell off scaffolding"))  {
            return FALL_CLIMBING_SCAFFOLDING;
        } else if (death.contains("fell while climbing")) {
            return FALL_CLIMBING_OTHER;
        } else if (death.contains("death.fell.accident.water") || death.contains("fell out of the water")) {
            return FALL_WATER;
        } else if (death.contains("was impaled on a stalagmite whilst fighting")) {
            return FALL_STALAGMITE_COMBAT;
        } else if (death.contains("was impaled on a stalagmite")) {
            return FALL_STALAGMITE;
        } else if (death.contains("was squashed by a falling anvil whilst fighting")) {
            return ANVIL_FALL_COMBAT;
        } else if (death.contains("was squashed by a falling anvil")) {
            return ANVIL_FALL;
        } else if (death.contains("was squashed by a falling block whilst fighting")) {
            return BLOCK_FALL_COMBAT;
        } else if (death.contains("was squashed by a falling block")) {
            return BLOCK_FALL;
        } else if (death.contains("was skewered by a falling stalactite whilst fighting")) {
            return STALAGTITE_FALL_COMBAT;
        } else if (death.contains("was skewered by a falling stalactite")) {
            return STALAGTITE_FALL;
        } else if (death.contains("went up in flames")) {
            return FIRE_SOURCE;
        } else if (death.contains("walked into fire whilst fighting")) {
            return FIRE_SOURCE_COMBAT;
        } else if (death.contains("burned to death")) {
            return FIRE_NATURAL;
        } else if (death.contains("was burnt to a crisp whilst fighting")) {
            return FIRE_NATURAL_COMBAT;
        } else if (death.contains("went off with a bang due to a firework fired from")) {
            return ROCKET_EXPLOSION_COMBAT_RENAMED;
        } else if (death.contains("went off with a bang")) {
            return ROCKET_EXPLOSION;
        } else if (death.contains("tried to swim in lava to escape")) {
            return LAVA_COMBAT;
        } else if (death.contains("tried to swim in lava")) {
            return LAVA;
        } else if (death.contains("was struck by lightning whilst fighting")) {
            return LIGHTNING_COMBAT;
        } else if (death.contains("was struck by lightning")) {
            return LIGHTNING;
        } else if (death.contains("discovered the floor was lava")) {
            return MAGMA_BLOCK;
        } else if (death.contains("walked into danger zone due to")) {
            return MAGMA_BLOCK_COMBAT;
        } else if (death.contains("was killed by magic whilst trying to escape")) {
            return MAGIC_COMBAT_ACCIDENT;
        } else if (death.contains("was killed by magic")) {
            return MAGIC;
        } else if (death.contains("was killed by")) {
            return MAGIC_COMBAT;
        } else if (death.contains("froze to death")) {
            return FROZEN;
        } else if (death.contains("was frozen to death by")) {
            return FROZEN_COMBAT;
        } else if (death.contains("was slain by")) {
            return COMBAT_NORMAL;
        } else if (death.contains("was fireballed by")) {
            return COMBAT_FIREBALL;
        } else if (death.contains("was stung to death")) {
            return COMBAT_BEE;
        } else if (death.contains("was shot by a skull from")) {
            return COMBAT_WITHER;
        } else if (death.contains("was obliterated by a sonically-charged shriek")) {
            return COMBAT_WARDEN;
        } else if (death.contains("starved to death whilst fighting")) {
            return STARVING_COMBAT;
        } else if (death.contains("starved to death")) {
            return STARVING;
        } else if (death.contains("suffocated in a wall whilst fighting")) {
            return SUFFOCATION_COMBAT;
        } else if (death.contains("was squished too much")) {
            return SUFFOCATION_CRAMMING;
        } else if (death.contains("was squashed by")) {
            return SUFFOCATION_CRAMMING_COMBAT;
        } else if (death.contains("was poked to death by a sweet berry bush whilst trying to escape")) {
            return BUSH_COMBAT;
        } else if (death.contains("was poked to death by a sweet berry bush")) {
            return BUSH;
        } else if (death.contains("trying to hurt")) {
            return THORNS;
        } else if (death.contains("was impaled by")) {
            return TRIDENT;
        } else if (death.contains("fell out of the world")) {
            return VOID;
        } else if (death.contains("didn't want to live in the same world as")) {
            return VOID_COMBAT;
        } else if (death.contains("withered away whilst fighting")) {
            return EFFECT_WITHER_COMBAT;
        } else if (death.contains("withered away")) {
            return EFFECT_WITHER;
        } else if (death.contains("died from dehydration whilst trying to escape")) {
            return DEHYDRATION_COMBAT;
        } else if (death.contains("died from dehydration")) {
            return DEHYDRATION;
        } else if (death.contains("died because of")) {
            return GENERIC_COMBAT;
        } else if (death.contains("was doomed to fall by")) {
            return PROJECTILE_FALL_COMBAT;
        } else if (death.contains("was doomed to fall")) {
            return PROJECTILE_FALL;
        } else if (death.contains("was roasted in dragon breath by")) {
            return DRAGON_BREATH_COMBAT;
        } else if (death.contains("was roasted in dragon breath")) {
            return DRAGON_BREATH;
        } else if (death.contains("died")) {
            return GENERIC;
        } else {
            return GENERIC;
        }
    }
    public static DeathType getDeathFromEvent(PlayerDeathEvent e) {
        return getDeathFromString(e.getDeathMessage());
    }


}
