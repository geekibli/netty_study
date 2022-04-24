package com.io.netty.bio.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    static final int PORT = 9090;

    public static void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("server listen on port:" + PORT);
            while (true){
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("receive client connect, localPort=" + client.getPort());
                    new Thread(new HandlerServer(client)).start();
                }catch (Exception e){
                    System.out.println("client exception,e=" + e.getMessage());
                }
            }
        }catch(Exception e){
            System.out.println("server exception,e=" + e.getMessage());
        }
    }



    public static void main(String[] args){
        EchoServer.start();
    }








    public static class HandlerServer implements Runnable{

        private Socket client;

        public HandlerServer(Socket client) {
            this.client = client;
        }

        public void run() {
            byte[] buf = new byte[1024];
            try {
                while (true){
                    int cnt = this.client.getInputStream().read(buf, 0,1023);
                    if(cnt > 0){
                        System.out.println("receive msg from client:" + new String(buf));
                        this.client.getOutputStream().write(buf,0,cnt);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(!this.client.isClosed()){
                    try {
                        this.client.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
