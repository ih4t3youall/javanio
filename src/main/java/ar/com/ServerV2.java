package ar.com;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerV2{

    private Selector selector;
    private InetSocketAddress listenAddress;
    private Map<SocketChannel,List> dataMapper;
    private BlockingQueue<SelectionKey> queue;

    public static void main (String [] args){

        try{
            new ServerV2();
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        dataMapper.put(channel, new ArrayList());
        channel.register(this.selector, SelectionKey.OP_READ);
    }


    public ServerV2() throws IOException{
        listenAddress = new InetSocketAddress("localhost", 8090);
        dataMapper = new HashMap<SocketChannel,List>();
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);


        this.queue = new ArrayBlockingQueue<>(1024);
        ReadThread rt = new ReadThread(this.queue);
        Thread thread = new Thread(rt);
        thread.start();

        //retrieve server socket and bind to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector,SelectionKey.OP_ACCEPT);
        System.out.println("server is started");


        while(true){

            //wait for events
            this.selector.select();
            long start = System.currentTimeMillis();

            Iterator keys = this.selector.selectedKeys().iterator();

            while (keys.hasNext()){
                SelectionKey key = (SelectionKey) keys.next();
                if(!key.isValid())
                    continue;
                if(key.isAcceptable()){
                    this.accept(key);
                }else{
                    if(key.isReadable()){
                        this.read(key);
                    }
                }
                keys.remove();
            }
        }

    }
    private int cont =0;

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {

        //this.queue.add(key);
        SocketChannel channel = (SocketChannel) key.channel();
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        int numRead = -1;
//        numRead = channel.read(buffer);
        new Thread(new ThreadV2(channel)).start();
        key.cancel();

//        if (numRead == -1) {
//            this.dataMapper.remove(channel);
//            Socket socket = channel.socket();
//            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
//            System.out.println("Connection closed by client: " + remoteAddr);
//            channel.close();
//            key.cancel();
//        }

//        byte[] data = new byte[numRead];
//        System.arraycopy(buffer.array(), 0, data, 0, numRead);
//        System.out.println("Got: " + new String(data));
//        buffer.clear();
    }
}


