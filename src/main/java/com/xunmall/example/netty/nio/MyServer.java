package com.xunmall.example.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/7/23 9:37
 */
public class MyServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer byteBuffer;
    private int size = 1024;
    private int remoteClientNum;

    private MyServer(int port) {
        try {
            initServer(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initServer(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));

        selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        byteBuffer = ByteBuffer.allocate(size);
    }

    private void listener() throws Exception {
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = socketChannel.accept();
                    registerChannel(selector, accept, SelectionKey.OP_READ);
                    remoteClientNum++;
                    System.out.println("online client num + " + remoteClientNum);
                    write(accept, "hello client ".getBytes());
                }
                if (selectionKey.isReadable()) {
                    read(selectionKey);
                }
                iterator.remove();
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        byteBuffer.clear();
        while ((count = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
            byteBuffer.clear();
        }
        if (count < 0) {
            socketChannel.close();
        }
    }

    private void write(SocketChannel socketChannel, byte[] writeData) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(writeData);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    private void registerChannel(Selector selector, SocketChannel socketChannel, int opRead) throws IOException {
        if (socketChannel == null) {
            return;
        }
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, opRead);
    }

    public static void main(String[] args) {

        MyServer myServer = new MyServer(9999);
        try {
            myServer.listener();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
