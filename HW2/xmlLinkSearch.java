
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xmlLinkSearch {
    public static void main(String[] args) throws IOException {
       Pattern categoryTag = Pattern.compile("\\[\\[.*?\\]\\]");
       String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW2";
       FileInputStream input = new FileInputStream(filePath + "/simplewiki-20140814-pages-articles-multistream.xml");
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
    }    
}
