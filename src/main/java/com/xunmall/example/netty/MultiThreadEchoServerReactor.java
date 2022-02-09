package com.xunmall.example.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangyanjing
 * @date 2020/7/25 22:06
 */
public class MultiThreadEchoServerReactor {

    ServerSocketChannel serverSocketChannel;

    AtomicInteger next = new AtomicInteger(0);

    Selector[] selectors = new Selector[2];

    SubReactor[] subReactors = null;


    MultiThreadEchoServerReactor() throws IOException {

        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        serverSocketChannel.socket().bind(address);
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selectors[0],SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptorHandler());
        SubReactor subReactor1 = new SubReactor(selectors[0]);
        SubReactor subReactor2 = new SubReactor(selectors[1]);
        subReactors = new SubReactor[]{subReactor1,subReactor2};
    }

    private void startService(){
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }


    class SubReactor implements Runnable {

        final Selector selector;

        public SubReactor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();

                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();

                    Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();

                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();

                        dispatch(selectionKey);

                    }

                    selectionKeySet.clear();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        void dispatch(SelectionKey key) {

            Runnable handler = (Runnable) key.attachment();

            if (handler != null) {
                handler.run();
            }
        }
    }

    class AcceptorHandler implements Runnable{

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null){
                    new MultiThreadEchoHandler(selectors[next.get()],socketChannel);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if (next.incrementAndGet() == selectors.length){
                next.set(0);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor multiThreadEchoServerReactor = new MultiThreadEchoServerReactor();
        multiThreadEchoServerReactor.startService();
    }

}
