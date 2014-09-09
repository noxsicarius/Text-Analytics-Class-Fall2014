import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneQueryer {
    // Path, field, and search variables
    private static final String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW3";
    private static final String indexDirectory = filePath;
    private static final String searchField = "text";
    private static final String searchQuery = "water";

    public static void main(String[] args) throws IOException,ParseException{

        // File writer
        Writer outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/querryResults.txt"), "utf-8"));
        
        // String for holding output text
        String output;
        // Converted the reader to a try with resource for stability
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectory)))) {
            IndexSearcher searcher = new IndexSearcher(reader);
            
            //since the index repository is made using StandardAnalyzer, we have to use the same
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
            
            QueryParser parser = new QueryParser(Version.LUCENE_4_9, searchField, analyzer);
            Query query = parser.parse(searchQuery);
            
            //the TopDocs and IndexSearcher objects have most of the post-query data we need.
            TopDocs results = searcher.search(query, 100);
            ScoreDoc[] hits = results.scoreDocs;
            
            int numTotalHits = results.totalHits;
            output = numTotalHits + " total matching documents\n";
            System.out.println(output);
            outputFile.write(output);
            
            //Print out doccument info. The document with the highest score is ranked in the top.
            for (ScoreDoc s : hits) {
                output = "" + s;
                System.out.println(output);
                outputFile.write(output + "\n");
                Document doc = searcher.doc(s.doc);
                
                //Return indexed title
                output = "title: " + doc.get("title") + "\n";
                System.out.println(output);
                outputFile.write(output);
            }
        } finally { outputFile.close();}
    }
}