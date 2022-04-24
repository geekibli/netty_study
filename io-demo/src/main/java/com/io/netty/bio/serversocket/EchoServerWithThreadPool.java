package com.io.netty.bio.serversocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class EchoServerWithThreadPool extends ThreadGroup {

    final private static int PORT = 9090;

    private LinkedList<Runnable> workQueue;
    private int threadID = 0;
    private boolean isClose = false;
    private static int threadPoolID;

    public EchoServerWithThreadPool(int poolSize) {
        super("EchoServerWithThreadPool-" + (threadPoolID++));
        setDaemon(true);
        workQueue = new LinkedList<Runnable>();
        for (int i = 0; i < poolSize; i++) {
            new WorkThread().start();
        }
    }

    public synchronized void execute(Runnable task) {
        if (isClose) {
            throw new IllegalStateException("thrad pool is closed!");
        }
        if (task != null) {
            workQueue.add(task);
            notify();
        }
    }

    protected synchronized Runnable getTask() throws InterruptedException {
        while (workQueue.size() == 0) {
            if (isClose) {
                return null;
            }
            wait();
        }
        return workQueue.removeFirst();
    }

    public synchronized void close() {
        if (!isClose) {
            isClose = true;
            workQueue.clear();
            interrupt();
        }
    }

    public void join() {
        synchronized (this) {
            isClose = true;
            notifyAll();
        }
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i = 0; i < count; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        EchoServerWithThreadPool threadPool = null;
        try {
            threadPool = new EchoServerWithThreadPool(10);
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("server listen on port:" + PORT);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("current thread : " + Thread.currentThread().getName() + "receive client connect, localPort=" + client.getPort());
                ClientHandler handler = new ClientHandler(client);
                threadPool.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.close();
            threadPool.join();
        }

    }


    /**
     * 客户端读写处理任务
     */
    private static class ClientHandler implements Runnable {

        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024];
            try {
                while (true) {
                    if (!this.client.isConnected()) {
                        return;
                    }
                    int cnt = this.client.getInputStream().read(buf, 0, 1023);
                    if (cnt > 0) {
                        System.out.println("receive msg from client:" + new String(buf) + "  " + Thread.currentThread().getName());
                        this.client.getOutputStream().write(buf, 0, cnt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!this.client.isClosed()) {
                    try {
                        this.client.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 工作线程
     */
    private class WorkThread extends Thread {
        public WorkThread() {
            super(EchoServerWithThreadPool.this, "WorkThread-" + (threadID++));
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                Runnable task = null;
                try {
                    task = getTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (task == null) {
                    return;
                }
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
