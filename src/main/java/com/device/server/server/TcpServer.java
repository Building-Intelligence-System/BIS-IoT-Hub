package com.device.server.server;

import com.device.server.server.base.BaseServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;

public final class TcpServer extends BaseServer {

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap bootstrap;

    public TcpServer(final String address, final int port,
                     final int acceptorCount, final int handlerCount,
                     final ChannelInitializer<?> initializer) {
        super(address, port);

        final Class<? extends ServerChannel> channelClass;
        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup(acceptorCount, new DefaultThreadFactory(port + "-TCP-ACCEPTOR-EPOLL-POOL"));
            workerGroup = new EpollEventLoopGroup(handlerCount, new DefaultThreadFactory(port + "-TCP-HANDLER-EPOLL-POOL"));
            channelClass = EpollServerSocketChannel.class;
        } else {
            throw new IllegalArgumentException("Linux Epoll native transport is unavailable", Epoll.unavailabilityCause());
        }


        bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(channelClass)
                .handler(new LoggingHandler(LogLevel.TRACE))
                .option(ChannelOption.SO_BACKLOG, 10_000)
                .childHandler(initializer)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        if (System.getProperty("so.linger.disable") != null) {
            bootstrap.childOption(ChannelOption.SO_LINGER, 0);
        }
    }

    @Override
    public void start() throws InterruptedException {
        bootstrap.bind(new InetSocketAddress(address, port)).sync();
    }

    @Override
    public void stop() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }
}
