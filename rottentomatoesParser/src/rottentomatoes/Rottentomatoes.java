/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rottentomatoes;

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

public class Rottentomatoes {
    
    public static void main(String[] args) throws IOException {
       Pattern patternTitle = Pattern.compile("\"title\": \".*?\"");
       Pattern patternYear = Pattern.compile("\"year\": .*?,");
       Pattern patternSynop = Pattern.compile("\"synopsis\": \".*?\"");
       Pattern patternCast = Pattern.compile("\"abridged_cast\": \\[(\\s*\\{\\s*\"name\": \"[a-zA-Z .]+\",\\s*\"characters\": \\[(\"[a-zA-Z .]+\", )*\"[a-zA-Z .]+\"\\]\\s*\\}(,(?=\\s*\\{)|\\s))*\\s*\\],?");
       Pattern patternNCast = Pattern.compile("\\{([^]]+)\\]");
       Pattern patternName = Pattern.compile("name: .*?,");
       Pattern patternChar = Pattern.compile("characters: \\[([^]]+)\\]");

       String filePath = System.getProperty("user.home") + "/Desktop";
       FileInputStream input = new FileInputStream(filePath + "/rottentomatoes.xml");
       Writer outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/results.txt"), "utf-8"));
       FileChannel channel = input.getChannel();
       ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
       CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);

       Matcher titleMatch = patternTitle.matcher(cbuf);
       Matcher yearMatch = patternYear.matcher(cbuf);
       Matcher synopMatch = patternSynop.matcher(cbuf);
       Matcher castMatch = patternCast.matcher(cbuf);
       
       while (titleMatch.find()){
           System.out.println("<DOC>");
           outputFile.write("<DOC>\n");
           String match = titleMatch.group();
           match = match.replaceAll("\"title\": ","");
           match = match.replaceAll("\"","");
           String output = "<title>" + match + "</title>";
           System.out.println(output);
           outputFile.write(output + "\n");

           yearMatch.find();
           match = yearMatch.group();
           match = match.replaceAll("\"year\": ","");
           match = match.replaceAll("\"","");
           match = match.replaceAll(",","");
           output = "<year>" + match + "</year>";
           System.out.println(output);
           outputFile.write(output + "\n");
           
           synopMatch.find();
           match = synopMatch.group();
           match = match.replaceAll("\"synopsis\": ","");
           match = match.replaceAll("\"","");
           output = "<synopsis>" + match + "</synopsis>";
           System.out.println(output);
           outputFile.write(output + "\n");

           castMatch.find();
           match = castMatch.group();
           match = match.replaceAll("\"abridged_cast\": \\[","");
           match = match.replaceAll("\"","");
           Matcher nCastMatch = patternNCast.matcher(match);
           Matcher nameMatch = patternName.matcher(match);
           Matcher charMatch = patternChar.matcher(match);
           while (nCastMatch.find()){
               nameMatch.find();
               String nMatch = nameMatch.group();
               nMatch = nMatch.replaceAll("name: ","");
               nMatch = nMatch.replaceAll(",","");
               output = "<actor>" + nMatch + "</actor>";
               System.out.println(output);
               outputFile.write(output + "\n");
               
               charMatch.find();
               String cMatch = charMatch.group();
               cMatch = cMatch.replaceAll("characters: ","");
               cMatch = cMatch.replaceAll("\\[","");
               cMatch = cMatch.replaceAll("\\]","");
               output = "<character>" + cMatch + "</character>";
               System.out.println(output);
               outputFile.write(output + "\n");
           }
           System.out.println("</DOC>");
           outputFile.write("</DOC>\n");
       }
       
       //Cleanup
       input.close();
       channel.close();
       outputFile.close();
    }    
}
