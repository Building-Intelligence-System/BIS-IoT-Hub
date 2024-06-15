package com.device.server.protocol.wialonIps;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class WialonIpsFrameDecoder extends ByteToMessageDecoder {

    private static final String START_SIGN = "#";
    private static final String END_SIGN = "\r\n";

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        final String packet = in.toString(StandardCharsets.US_ASCII);

        if (!packet.startsWith(START_SIGN)) {
            ctx.close();
            return;
        }

        if (!packet.contains(END_SIGN)) {
            return;
        }

        final String[] records = packet.split(END_SIGN);
        if (records.length != 0) {
            out.add(in.readBytes(records[0].length() + END_SIGN.length()));
        }
    }
}
