package com.device.server.protocol.base;

import com.device.server.Context;
import com.device.server.model.Telemetry;
import com.device.server.protocol.base.auth.AuthProvider;
import com.device.server.protocol.base.auth.ImeiAuthProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHandler<T> extends ChannelInboundHandlerAdapter {

    protected final String protocolName;

    protected String identifier = "UNKNOWN";

    protected AuthProvider authProvider;

    public BaseHandler(final String protocolName) {
        this.protocolName = protocolName;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    protected abstract void handle(T packet) throws Exception;

    @Override
    public final void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final T packet = (T) msg;
        handle(packet);
        ctx.writeAndFlush(packet);
    }

    @Override
    public final void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
        }
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        final String message = cause.getMessage();
        ctx.close();
    }

    protected final boolean authenticate(final String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }

        this.identifier = identifier;
        this.authProvider = new ImeiAuthProvider(identifier);

        return true;
    }

    protected final void checkAuthorized() {
        if (authProvider == null) {
            throw new IllegalStateException();
        }
    }

    protected final void writeTelemetries(final List<Telemetry> telemetries) {
        final String trackerId = authProvider.getTrackerId();
        telemetries.forEach(t -> t.editor().withImei(trackerId));
        final List<Telemetry> validateTelemetries = validateTelemetries(telemetries);
        if (validateTelemetries.isEmpty()) {
            return;
        }

        Context.getTelemetryRepository().saveAll(validateTelemetries);

    }

    protected List<Telemetry> validateTelemetries(final List<Telemetry> telemetries) {
        final List<Telemetry> result = new ArrayList<>();
        telemetries.forEach(rec -> {
            try {
                result.add(rec);
            } catch (Exception ex) {
            }
        });

        return result;
    }
}
