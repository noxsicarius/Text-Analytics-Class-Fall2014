
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xmlLinkSearch {
    public static void main(String[] args) throws IOException {
       Pattern filePattern = Pattern.compile("\\[\\[File.*?\\[\\[");
       Pattern linkPattern = Pattern.compile("\\[\\[.*?\\]\\]");
       String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW2";
       FileInputStream input = new FileInputStream(filePath + "/simplewiki-20140814-pages-articles-multistream.xml");
       Writer tempFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/tempFile.xml"), "utf-8"));
       Writer outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/querryLinks.txt"), "utf-8"));
       FileChannel channel = input.getChannel();
       ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
       CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
       Matcher fileMatch = filePattern.matcher(cbuf);
       String bufferString = cbuf.toString();
       
       while (fileMatch.find()){
           String match = fileMatch.group();
           bufferString = bufferString.replace(match,"[[");
           //System.out.println(output);
           
       }
       tempFile.write(bufferString);
       
       input = new FileInputStream(filePath + "/tempFile.xml");
       channel = input.getChannel();
       bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
       cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
       Matcher linkMatch = linkPattern.matcher(cbuf);
       while (linkMatch.find()){
           String match = linkMatch.group();
           match = match.replaceAll("\\[","");
           match = match.replaceAll("\\]","");
           String output = "\"" + match + "\"";
           System.out.println(output);
           outputFile.write(output + "\n");
       }
    }
}
