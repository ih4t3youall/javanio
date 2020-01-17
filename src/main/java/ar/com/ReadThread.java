package ar.com;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class ReadThread implements Runnable{


    private BlockingQueue<SelectionKey> queue;
   public ReadThread(BlockingQueue<SelectionKey> queue){
       this.queue = queue;
   }


    @Override
    public void run() {

       while (true) {
           SelectionKey key = null;
           try {
               key = this.queue.take();
               System.out.println("taked from queue");
               key.isReadable();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           SocketChannel channel = (SocketChannel) key.channel();
           ByteBuffer buffer = ByteBuffer.allocate(1024);
           int numRead = -1;
           try {
               numRead = channel.read(buffer);
           } catch (IOException e) {
               e.printStackTrace();
           }

           if (numRead == -1) {
               Socket socket = channel.socket();
               SocketAddress remoteAddr = socket.getRemoteSocketAddress();
               System.out.println("Connection closed by client: " + remoteAddr);
               try {
                   channel.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               key.cancel();
               continue;
           }

           byte[] data = new byte[numRead];
           System.arraycopy(buffer.array(), 0, data, 0, numRead);
           System.out.println("Got: " + new String(data));
       }

    }
}
