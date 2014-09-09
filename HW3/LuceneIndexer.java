import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndexer {
    private static final String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW3-Ricky";
    private static final String indexDirectory = filePath;
    private static final String fileToBeIndexed = filePath + "/20121202-wiki-en_000000 short.xml";

    public static void main(String[] args) throws IOException{
        
        Directory dir = FSDirectory.open(new File(indexDirectory));
            
        //here we are using a standard analyzer, there are a lot of analyzers available to our use.
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46, analyzer);

        //this mode by default overwrites the previous index, not a very good option in real usage
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        // Converted to try with resources for stability
        try (IndexWriter writer = new IndexWriter(dir, iwc)) {
            File file = new File(fileToBeIndexed);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String title = null;
                String docno = null;
                String text = null;
                String line = null;
                boolean docStarted = false;
                Document doc = null;
                
                while ((line = br.readLine()) != null) {
                    
                    //Note that these fields are part of a TRECtext file
                    if (line.indexOf("<DOC>") > -1) {
                        docStarted = true;
                        doc = new Document();
                    } else if (line.indexOf("</DOC>") > -1) {
                        docStarted = false;
                        
                        //I have used 'Field' for the sake of ease of use. You can also use others like 'StringField', etc
                        doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
                        doc.add(new Field("docno", docno, Field.Store.NO, Field.Index.NOT_ANALYZED));
                        doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED));
                        writer.addDocument(doc);
                    }
                    
                    if (docStarted) {
                        
                        int i = -1;
                        
                        if ((i = line.indexOf("<title>")) > -1) {
                            title = (line.substring(i + "<title>".length(), line.indexOf("</title>")));
                        } else if ((i = line.indexOf("<text>")) > -1) {
                            text = line.substring(i + "<text>".length());
                        } else if ((i = line.indexOf("<docno>")) > -1) {
                            docno = line.substring(i + "<docno>".length(), line.indexOf("</docno>"));
                        }
                    }
                }
            }
        }
        System.out.println("Done indexing at " + indexDirectory);
    }
}
