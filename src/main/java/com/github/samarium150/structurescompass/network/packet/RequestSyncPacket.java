package com.github.samarium150.structurescompass.network.packet;

import com.github.samarium150.structurescompass.network.StructuresCompassNetwork;
import com.github.samarium150.structurescompass.util.ItemUtils;
import com.github.samarium150.structurescompass.util.StructureUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Class for client to request data synchronization
 */
public final class RequestSyncPacket implements Packet {
    
    /**
     * Initializer of the packet
     */
    public RequestSyncPacket() { }
    
    /**
     * Decoder of the packet
     * @param buffer PacketBuffer
     */
    @SuppressWarnings("unused")
    public RequestSyncPacket(FriendlyByteBuf buffer) { }
    
    public void toBytes(@Nonnull FriendlyByteBuf buffer) { }
    
    public void handle(@Nonnull Supplier<Context> ctx) {
        Context context = ctx.get();
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();
            final ItemStack stack = ItemUtils.getHeldStructuresCompass(player);
            if (!stack.isEmpty() && player != null) {
                final List<StructureFeature<?>> allowed = StructureUtils.getAllowedStructures();
                final HashMap<String, List<String>> map = new HashMap<>();
                allowed.forEach(structure -> map.put(
                    StructureUtils.getStructureName(structure),
                    StructureUtils.getDimensions(player.getLevel(), structure)
                ));
                StructuresCompassNetwork.channel.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncPacket(stack, allowed, map)
                );
            }
        });
        context.setPacketHandled(true);
    }
}
