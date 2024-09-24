package mapext.common.packet.marks;

import mapext.common.packet.ICodec;
import mapext.core.Mod;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;

public class SLogIn {

    public static class Codec implements ICodec<SLogIn> {

        @Override
        public void accept(SLogIn packet, PacketByteBuf buf) {
        }

        @Override
        public SLogIn apply(PacketByteBuf buf) {
            return new SLogIn();
        }
    }

    public static class ClientHandler implements Consumer<SLogIn> {
        @Override
        public void accept(SLogIn sLogIn) {
            Mod.getMod().getClientData().clientMarksManager().reset();
        }
    }
}
