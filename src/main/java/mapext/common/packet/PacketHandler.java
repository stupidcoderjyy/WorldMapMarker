/*
 * Open Parties and Claims - adds chunk claims and player parties to Minecraft
 * Copyright (C) 2024, Xaero <xaero1996@gmail.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of version 3 of the GNU Lesser General Public License
 * (LGPL-3.0-only) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received copies of the GNU Lesser General Public License
 * and the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package mapext.common.packet;

import io.netty.buffer.Unpooled;
import mapext.common.packet.type.PacketType;
import mapext.common.packet.type.PacketTypeManager;
import mapext.core.Mod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketHandler{
	protected final PacketTypeManager packetTypeManager = new PacketTypeManager();

	public <P> void register(int index,
                             Class<P> type,
                             ICodec<P> codec,
                             BiConsumer<P, ServerPlayerEntity> serverHandler,
                             Consumer<P> clientHandler) {
		packetTypeManager.register(index, type, codec, serverHandler, clientHandler);
	}

	public PacketType<?> getPacketTypeByIndex(int index){
		return packetTypeManager.getByIndex(index);
	}

    public <T> void sendToServer(T packet) {
        ClientPlayNetworking.send(Mod.MAIN_CHANNEL, getPacketBuffer(packet));
    }

    public <T> void sendToPlayer(ServerPlayerEntity player, T packet) {
        ServerPlayNetworking.send(player, Mod.MAIN_CHANNEL, getPacketBuffer(packet));
    }

    private <T> PacketByteBuf getPacketBuffer(T packet){
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        encodePacket(packetTypeManager.getType(packet), packet, buffer);
        return buffer;
    }

    public static <T> void encodePacket(PacketType<T> packetType, T packet, PacketByteBuf buffer) {
        if(packetType == null) {
            throw new IllegalArgumentException("unregistered packet class!");
        }
        buffer.writeByte(packetType.index());
        packetType.codec().accept(packet, buffer);
    }
}
