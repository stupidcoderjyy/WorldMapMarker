package mapext.common.packet.marks;

import mapext.common.packet.ICodec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xaero.map.WorldMapSession;

import java.util.function.Consumer;

public class CMarkNoPermission {

    public static class Codec implements ICodec<CMarkNoPermission> {

        @Override
        public void accept(CMarkNoPermission packet, PacketByteBuf buf) {
        }

        @Override
        public CMarkNoPermission apply(PacketByteBuf buf) {
            return new CMarkNoPermission();
        }
    }

    public static class ClientHandler implements Consumer<CMarkNoPermission> {

        @Override
        public void accept(CMarkNoPermission packet) {
            var text = Text.translatable("message.mapext.no_permission").formatted(Formatting.RED);
            WorldMapSession session = WorldMapSession.getCurrentSession();
            if (session != null) {
                session.getMapProcessor().getMessageBox().addMessage(text);
            }
        }
    }
}
