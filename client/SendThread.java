import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SendThread implements Runnable{

  private String path;
  public SendThread(String path){
    this.path = path;
  }

  public void run() {

    InetSocketAddress hostAddress = new InetSocketAddress("localhost",8090);
    SocketChannel client = null; 
    List<String> allLines = null;
    try{
      client = SocketChannel.open(hostAddress);
      allLines = Files.readAllLines(Paths.get(path));
    }catch(IOException e){
      e.printStackTrace();
    }

    System.out.println("Client... started");
    String threadName = Thread.currentThread().getName();

    try{
      for (String pureMessage : allLines){
	System.out.println(pureMessage);
	byte[] message = new String(pureMessage).getBytes();
	ByteBuffer buffer  = ByteBuffer.wrap(message);
	client.write(buffer);
//	int numRead = client.read(buffer);
//	byte[] data  = new byte[numRead];
//	System.arraycopy(buffer.array(), 0, data, 0, numRead);
//        System.out.println("Got: " + new String(data));
      }
      client.close();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
