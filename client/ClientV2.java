import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ClientV2{

  public static void main(String []args){
  
    try{
    new ClientV2();
    }catch(IOException e){
    

      e.printStackTrace();
    
    }catch(InterruptedException e){
      e.printStackTrace();
    
    }
  
  }



  public ClientV2() throws InterruptedException,IOException{
  
    List<Path> path = readFiles();
    if (path == null){
      System.exit(1);
    }
    path.stream().forEach(file->{
      Thread t =  new Thread(new SendThread(file.toString()));
      t.start();
    });
  
  
  }

  public List<Path> readFiles() {
       String dirName ="/mnt/c/Users/mlequerica/Documents/workspace/javanio/client/files";
       try {
	return Files.list(new File(dirName).toPath()).collect(Collectors.toList());
       }catch(IOException e){
	 e.printStackTrace();
	 return null;
       }
  }




}
