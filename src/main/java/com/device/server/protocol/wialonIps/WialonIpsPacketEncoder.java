package com.device.server.protocol.wialonIps;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class WialonIpsPacketEncoder extends MessageToByteEncoder<WialonIpsPacket> {

    @Override
    protected void encode(final ChannelHandlerContext ctx, final WialonIpsPacket packet, final ByteBuf out) {
        switch (packet.getType()) {
            case L:
                if (packet.isAuthorized()) {
                    out.writeBytes("#AL#1\r\n".getBytes(StandardCharsets.US_ASCII));
                } else {
                    out.writeBytes("#AL#0\r\n".getBytes(StandardCharsets.US_ASCII));
                }
                break;

            case SD:
                out.writeBytes("#ASD#1\r\n".getBytes(StandardCharsets.US_ASCII));
                break;

            case D:
                out.writeBytes("#AD#1\r\n".getBytes(StandardCharsets.US_ASCII));
                break;

            case B:
                out.writeBytes(("#AB#" + packet.getRecords().size() + "\r\n").getBytes(StandardCharsets.US_ASCII));
                break;

            case P:
                out.writeBytes("#AP#\r\n".getBytes(StandardCharsets.US_ASCII));
                break;

            case M:
                out.writeBytes("#AM#1\r\n".getBytes(StandardCharsets.US_ASCII));
                break;

            default:
                throw new IllegalArgumentException();
        }
    }
}
