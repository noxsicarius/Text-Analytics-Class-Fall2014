
import java.io.File;
import java.io.IOException;
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
    private static final String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW3-Ricky";
    private static final String indexDirectory = filePath;
    private static final String searchField = "text";
    private static final String searchQuery = "castle";

    public static void main(String[] args) throws IOException,ParseException{
        // Converted the reader to a try with resource for stability
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectory)))) {
            IndexSearcher searcher = new IndexSearcher(reader);
            
            //since the index repository is made using StandardAnalyzer, we have to use the same
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
            
            QueryParser parser = new QueryParser(Version.LUCENE_46, searchField, analyzer);
            Query query = parser.parse(searchQuery);
            
            //the TopDocs and IndexSearcher objects have most of the post-query data we need.
            TopDocs results = searcher.search(query, 100);
            ScoreDoc[] hits = results.scoreDocs;
            
            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");
            
            //Print out doccument info. The document with the highest score is ranked in the top.
            for (ScoreDoc s : hits) {
                System.out.println("" + s);
                Document doc = searcher.doc(s.doc);
                
                //Return indexed title
                System.out.println("title: " + doc.get("title"));
            }
        }
    }
}