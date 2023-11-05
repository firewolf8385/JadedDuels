/*
 * This file is part of JadedDuels, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedduels.game.kit;

import net.jadedmc.jadedduels.JadedDuelsPlugin;
import net.jadedmc.jadedduels.game.kit.kits.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Manages all existing kits.
 */
public class KitManager {
    private final Set<Kit> activeKits = new LinkedHashSet<>();
    private final Set<Kit> duelKits = new LinkedHashSet<>();

    /**
     * Creates the Kit Manager.
     * @param plugin Instance of the plugin.
     */
    public KitManager(final JadedDuelsPlugin plugin) {
        activeKits.add(new UHCKit(plugin));
        activeKits.add(new BowKit(plugin));
        activeKits.add(new AxeKit(plugin));
        activeKits.add(new SwordKit(plugin));

        duelKits.add(new HorseKit(plugin));
        duelKits.add(new IronPotKit(plugin));
        duelKits.add(new DoorKit(plugin));
        duelKits.add(new CactusKit(plugin));
        duelKits.add(new BoxingKit(plugin));
        duelKits.add(new SpleefKit(plugin));
    }

    /**
     * Get a kit from its name,
     * @param kitName Name of the kit.
     * @return Kit from name.
     */
    public Kit kit(final String kitName) {
        for(Kit kit : this.activeKits) {
            if(kit.name().equalsIgnoreCase(kitName) || kit.id().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }

        for(Kit kit : this.duelKits) {
            if(kit.name().equalsIgnoreCase(kitName) || kit.id().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }

        return null;
    }

    /**
     * Get all active kits.
     * @return All active kits.
     */
    public Set<Kit> activeKits() {
        return activeKits;
    }

    /**
     * Get all currently loaded kits, both active and duel kits.
     * @return All kits.
     */
    public Set<Kit> kits() {
        Set<Kit> kits = new LinkedHashSet<>();
        kits.addAll(activeKits);
        kits.addAll(duelKits);

        return kits;
    }
}