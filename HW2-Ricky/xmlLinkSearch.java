
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class xmlLinkSearch {
    public static void main(String[] args) {
       Pattern categoryTag = Pattern.compile("\\[\\[.*?\\]\\]");
       try {
          // Find the current directory so the file can be linked
          File currentDirectory = new File(new File(".").getAbsolutePath());
          String path = currentDirectory.getCanonicalPath();
          
          FileInputStream input = new FileInputStream(path + "/simplewiki-20140814-pages-articles-multistream.xml");
          FileChannel channel = input.getChannel();
          
          ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
          CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
          Matcher tagmatch = categoryTag.matcher(cbuf);

          while (tagmatch.find()){
              String match = tagmatch.group();
              match = match.replaceAll("\\[","");
              match = match.replaceAll("\\]","");              
              System.out.println("\"" + match + "\"");
          }

       } catch (IOException e) {
          e.printStackTrace();
        }
    }    
}