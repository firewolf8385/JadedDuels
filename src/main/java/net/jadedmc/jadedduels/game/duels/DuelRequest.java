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
package net.jadedmc.jadedduels.game.duels;

import net.jadedmc.jadedduels.game.arena.Arena;
import net.jadedmc.jadedduels.game.kit.Kit;
import org.bukkit.entity.Player;

public class DuelRequest {
    private final Player sender;
    private final Player receiver;
    private final Kit kit;
    private final Arena arena;
    private final int pointsNeeded;

    public DuelRequest(Player sender, Player receiver, Kit kit, Arena arena, int pointsNeeded) {
        this.sender = sender;
        this.receiver = receiver;
        this.kit = kit;
        this.arena = arena;
        this.pointsNeeded = pointsNeeded;
    }

    public DuelRequest(Player sender, Player receiver, Kit kit, int pointsNeeded) {
        this(sender, receiver, kit, null, pointsNeeded);
    }

    public Arena arena() {
        return arena;
    }

    public Kit kit() {
        return kit;
    }

    public Player receiver() {
        return receiver;
    }

    public Player sender() {
        return sender;
    }

    public int pointsNeeded() {
        return pointsNeeded;
    }
}