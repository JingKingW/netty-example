package com.xunmall.example.netty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangyanjing
 * @date 2020/7/26 9:49
 */
public class MultiThreadEchoHandler implements Runnable {

    final SocketChannel socketChannel;

    final SelectionKey selectionkey;

    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    static final int RECIEVING = 0, SENDING = 1;

    int state = RECIEVING;

    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    MultiThreadEchoHandler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel = channel;
        channel.configureBlocking(false);
        selectionkey = channel.register(selector,0);
        selectionkey.attach(this);
        selectionkey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        executorService.execute(new AsyncTask());
    }

    public synchronized void asyncRun(){
        try{
            if (state == SENDING){
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                selectionkey.interestOps(SelectionKey.OP_READ);
                state = RECIEVING;
            }else if( state == RECIEVING ){
                int length = 0;

                while((length = socketChannel.read(byteBuffer)) >0){
                    System.out.println(new String(byteBuffer.array(),0,length));
                }
                byteBuffer.flip();

                selectionkey.interestOps(SelectionKey.OP_WRITE);

                state = SENDING;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    class AsyncTask implements Runnable{

        @Override
        public void run() {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }
        
}
