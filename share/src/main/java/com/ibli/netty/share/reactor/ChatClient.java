package com.ibli.netty.share.reactor;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 聊天程序客户端
 */
public class ChatClient {
    private final String HOST = "127.0.0.1";        //服务器地址
    private int PORT = 9090;                        //服务器端口
    private SocketChannel socketChannel;            //网络通道
    private String userName;                        //聊天用户名

    public ChatClient() throws Exception{
        //1. 得到网络通道
        socketChannel = SocketChannel.open();
        //2. 设置非阻塞方式
        socketChannel.configureBlocking(false);
        //3. 提供服务器端的IP地址和端口号
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        //4. 连接服务器端
        if (!socketChannel.connect(socketAddress)) {    //第一次尝试连接服务器端
            //再次连接服务器端,nio非阻塞式的优势
            while (!socketChannel.finishConnect()) {
                System.out.println("Client:连接服务器的同时，我还可以干别的事情");
            }
        }

        //5. 得到客户端IP地址和端口信息,作为聊天用户名使用
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("----------------------Client(" + userName + ") is ready-----------------------");
    }

    /**
     * 客户端发送消息的方法
     * @param msg
     * @throws Exception
     */
    public void sendMsg(String msg) throws Exception{
        //约定客户端发送的消息为"bye"时，聊天终止
        if (msg.equalsIgnoreCase("bye")) {
            //关闭通道
            socketChannel.close();
            return;
        }
        msg = userName + "说: " + msg;
        //将消息写入缓存区
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        //将缓存区中的数据写入通道
        socketChannel.write(byteBuffer);
    }

    /**
     * 从服务器端接收数据
     * @throws Exception
     */
    public void receiveMsg() throws Exception{
        //构造一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //从通道中读取数据到缓冲区
        int count = socketChannel.read(byteBuffer);
        if (count > 0) {
            String msg = new String(byteBuffer.array());
            //输出接收到服务器中的数据
            System.out.println(msg.trim());
        }
    }


    public static void main(String[] args) throws Exception {
        ChatClient chatClient = new ChatClient();

        chatClient.sendMsg("hhhh");

        chatClient.sendMsg("hhhhsdsd");
        chatClient.sendMsg("hhhhsdsdsdasdas");

    }
}
