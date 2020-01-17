import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import java.time.LocalDateTime;

public class FillRandomFile{

  public static void main (String []args) throws IOException{


    String filename = "file.txt";
    if ( args.length > 1){
      filename = args[1];
    }

    checkFileExistence();
    String dirName ="/home/hate/Documents/workspace/erisx/client/clientV2/files";
    FileWriter fw = new FileWriter(dirName+"/"+filename);

    for (int i = 0 ; i < Integer.valueOf(args[0]) ; i++){
      fw.write("8=FIX.4.4|9="+getRandomChar()+"|35=D|34=1|49=TRADER|52=20190101-20:20:41:000|56=ERISX|11=123|38="+getRandomNumber(1,10)+"|40=2|44="+getRandomNumber(3500,4000)+"|54=1|55=BTCUSD|59=1|60="+ LocalDateTime.now()+"|10=111|");
      fw.write("\n");
      
    }
    fw.close();


  }



  private static String getRandomNumber(int min ,int max){

    return String.valueOf((Math.random() * ((max-min)+1)+min));


  }

  private static void checkFileExistence() throws IOException {
  File file = new File("file.txt");
    if (file.exists()){
      file.delete();
    }
  }

  private static char getRandomChar(){
    Random r = new Random();
    return (char)(r.nextInt(26) + 'a');
  }


}
