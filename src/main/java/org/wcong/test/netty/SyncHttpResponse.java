package org.wcong.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * get http response sync
 * Created by wcong on 2016/8/8.
 */
public class SyncHttpResponse {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedEncodingException {
        final ClientHandler clientHandler = new ClientHandler();
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(loopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().
                        addLast(new HttpRequestEncoder()).
                        addLast(new HttpResponseDecoder()).
                        addLast(clientHandler);
            }
        });
        Channel channel = b.connect("www.baidu.com", 80).sync().channel();
        while (!channel.isActive()) {
            Thread.sleep(1000);
        }
        URI uri = new URI("https://www.baidu.com");
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString());

        // 构建http请求
        request.headers().set(HttpHeaders.Names.HOST, "www.baidu.com");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        ChannelPromise promise = clientHandler.sendMessage(request);
        promise.await();
        System.out.println(clientHandler.getData());
        channel.close();
    }

    public static class ClientHandler extends ChannelInboundHandlerAdapter {
        private ChannelHandlerContext ctx;
        private ChannelPromise promise;
        private String data;
        private long readByte;
        private long contentLength;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.ctx = ctx;
        }

        public ChannelPromise sendMessage(Object message) {
            if (ctx == null)
                throw new IllegalStateException();
            promise = ctx.writeAndFlush(message).channel().newPromise();
            return promise;
        }

        public String getData() {
            return data;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                contentLength = Long.parseLong(response.headers().get(HttpHeaders.Names.CONTENT_LENGTH));
                readByte = 0;
            }
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                readByte += buf.readableBytes();
                data += buf.toString(io.netty.util.CharsetUtil.UTF_8);
                if (readByte >= contentLength) {
                    promise.setSuccess();
                }
                buf.release();
            }
        }
    }

}
