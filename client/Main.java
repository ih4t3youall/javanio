import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import java.nio.file.Files;
import java.util.stream.Collectors;

public class Main{


  public static void main(String [] args){
  
    new Main();
  
  }


  public Main(){
    String dirName ="/mnt/c/Users/mlequerica/Documents/workspace/javanio/client/files";
    try {
      List<Path> paths =  Files.list(new File(dirName).toPath()).collect(Collectors.toList());
      paths.stream().forEach(x->{
      
	System.out.println(x);
      
      });

    }catch(IOException e){
      e.printStackTrace();
    }
  }

}



