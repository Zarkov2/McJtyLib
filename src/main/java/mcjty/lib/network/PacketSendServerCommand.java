package mcjty.lib.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.McJtyLib;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.Logging;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;

public class PacketSendServerCommand implements IMessage {

    private String modid;
    private String command;
    private TypedMap arguments;

    @Override
    public void fromBytes(ByteBuf buf) {
        modid = NetworkTools.readString(buf);
        command = NetworkTools.readString(buf);
        arguments = TypedMapTools.readArguments(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, modid);
        NetworkTools.writeString(buf, command);
        TypedMapTools.writeArguments(buf, arguments);
    }

    public PacketSendServerCommand() {
    }

    public PacketSendServerCommand(String modid, String command, @Nonnull TypedMap arguments) {
        this.modid = modid;
        this.command = command;
        this.arguments = arguments;
    }

    public static class Handler implements IMessageHandler<PacketSendServerCommand, IMessage> {
        @Override
        public IMessage onMessage(PacketSendServerCommand message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendServerCommand message, MessageContext ctx) {
            boolean result = McJtyLib.handleCommand(message.modid, message.command, ctx.getServerHandler().player, message.arguments);
            if (!result) {
                Logging.logError("Error handling command '" + message.command + "' for mod '" + message.modid + "'!");
            }
        }
    }
}
