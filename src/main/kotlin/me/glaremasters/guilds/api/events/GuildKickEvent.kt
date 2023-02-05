/*
 * MIT License
 *
 * Copyright (c) 2022 Glare
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.glaremasters.guilds.api.events

import me.glaremasters.guilds.api.events.base.GuildEvent
import me.glaremasters.guilds.guild.Guild
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

/**
 * Class representing an event that occurs when a player is kicked from a guild.
 *
 * @property player the player who performed the kick
 * @property guild the guild the player was kicked from
 * @property kicked the player who was kicked
 * @property cause the reason for the player being kicked
 *
 * @constructor Creates a new [GuildKickEvent].
 */
class GuildKickEvent(player: Player, guild: Guild, val kicked: OfflinePlayer, val cause: Cause) : GuildEvent(player, guild) {

    /**
     * Enumeration class representing the possible reasons a player may be kicked from a guild.
     */
    enum class Cause {
        /** The player was kicked by another player. */
        PLAYER_KICKED,
        /** The player was kicked by an administrator. */
        ADMIN_KICKED
    }
}
