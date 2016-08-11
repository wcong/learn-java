### 前言
最近在用netty开发项目，主要用于tcp通信，处理逻辑时发现没有很好的同步获取response的机制，研究了一下现在主流的方式，就自定义实现了一下。
### SyncHttpResponse
这里以http服务为例，tcp的协议可以类似使用，完整的代码还是放在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/netty/SyncHttpResponse.java)上了。
1. 定义ClientHandler，继承了*ChannelInboundHandlerAdapter*，并保存了*ChannelHandlerContext*来实现发送请求的功能。
同步主要是用到了*ChannelPromise*，发送完请求后，会新生成一个*ChannelPromise*并返回，并在接收到完整的response后*setSuccess*。
```
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
                data += buf.toString(Charset.forName("gb2312"));
                if (readByte >= contentLength) {
                    promise.setSuccess();
                }
                buf.release();
            }
        }
    }
```
2. client，这里开启一个*Bootstrap*，*connect*会链接到指定的host，这里会有一个死循环，直到链接激活为止。
发送HttpRequest后，会await直到*ChannelPromise*的完成，然后取出读到的数据。
```
    public static class HttpClient {
        private ClientHandler clientHandler = new ClientHandler();
        private String url;
        private URI uri;
        public HttpClient(String url) {
            this.url = url;
        }
        public void connect() throws Exception {
            uri = new URI(url);
            EventLoopGroup loopGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(loopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpRequestEncoder()).addLast(new HttpResponseDecoder()).addLast(clientHandler);
                }
            });
            Channel channel = b.connect(uri.getHost(), uri.getPort() < 0 ? 80 : uri.getPort()).sync().channel();
            while (!channel.isActive()) {
                Thread.sleep(1000);
            }
        }
        public String getBody() throws Exception {
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
            request.headers().set(HttpHeaders.Names.HOST, uri.getHost());
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
            ChannelPromise promise = clientHandler.sendMessage(request);
            promise.await();
            return clientHandler.getData();
        }
    }
```
3. 程序入口。
```
    public class SyncHttpResponse {
        public static void main(String[] args) throws Exception {
            HttpClient client = new HttpClient("http://www.baidu.com");
            client.connect();
            System.out.println(client.getBody());
        }
    }
```

运行程序就会打印出response的内容了。
### 结语
netty是异步io框架，理论上不提倡同步的处理，但是某些情况下强依赖同步的结果，可以采用这种方式。