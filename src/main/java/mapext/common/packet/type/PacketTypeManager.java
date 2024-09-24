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

package mapext.common.packet.type;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mapext.common.packet.ICodec;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Used on platforms that don't handle packet types like Forge
 */
public class PacketTypeManager {

	private final Int2ObjectOpenHashMap<PacketType<?>> index2Type = new Int2ObjectOpenHashMap<>();
	private final Map<Class<?>, PacketType<?>> class2Type = new HashMap<>();

	public <P> void register(int index, Class<P> type, ICodec<P> codec, BiConsumer<P, ServerPlayerEntity> serverHandler, Consumer<P> clientHandler) {
		PacketType<P> packetType = new PacketType<>(index, type, codec, serverHandler, clientHandler);
		if(index2Type.containsKey(index))
			throw new IllegalArgumentException("duplicate index!");
		if(class2Type.containsKey(type))
			throw new IllegalArgumentException("duplicate packet class!");
		index2Type.put(index, packetType);
		class2Type.put(type, packetType);
	}

	public PacketType<?> getByIndex(int index){
		return index2Type.get(index);
	}

	public PacketType<?> getByClass(Class<?> clazz){
		return class2Type.get(clazz);
	}

	public <P> PacketType<P> getType(P message){
		return (PacketType<P>) class2Type.get(message.getClass());
	}
}
