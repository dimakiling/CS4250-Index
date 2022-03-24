import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class InvertedIndex {

    private static Logger log = Logger.getLogger(InvertedIndex.class.getName());

    private HashMap<String, HashMap<String, ArrayList<Integer>>> wordMap = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
    private Lock lock;
    private static InvertedIndex index = null;

    private InvertedIndex() {
        lock = new Lock();
        log.info("Building InvertedIndex...");
    }

    public void insert(String word, String fileName, int position) {
        lock.acquireWriteLock();
        log.debug("Adding " + word + " to index.");
        if(wordMap.get(word) == null) {
            wordMap.get(word, new HashMap<String, ArrayList<Integer>>());
        }
        ArrayList<Integer> occurrence = fileMap.get(fileName);
        occurrence.add(position);
        lock.releaseWriteLock(); 
    }

    public void printIndex() throws IOException {
        lock.acquireReadLock();
        Filewriter stream = new FileWriter("invertedindex.txt");
        PrintWriter out = new PrintWriter(stream);
        for(String word : wordMap.keySet()) {
            out.println(word);
            HashMap<String, ArrayList<Integer>> map = wordMap.get(word);
            for(String fileName : map.KeySet()) {
                out.print("\"" + fileName + "\"");
                ArrayList<Integer> occurrences = map.get(fileName);
                for(Integer i : occurrences) {
                    out.print(", " + i);
                }
                out.println();
            }
            out.println();
        }
        out.println();
        out.println();
        out.close();
        lock.releaseReadLock();
    }

    public Set<String> getMapsWords() {
        lock.acquireReadLock();
        Set<String> keys = wordMap.keySet();
        lock.releaseReadLock();
        return keys;
    }

    public Set<String> getWordsUrls(String word) {
        lock.acquireReadLock();
        Set<String> sites = wordMap.get(word).keySet();
        lock.releaseReadLock();
        return sites;
    }

    public Integer getSitesRankforWord(String word, String fileName) {
        lock.acquireReadLock();
        Integer rank = wordMap.get(word).get(fileName).size();
        lock.releaseReadLock();
        return rank;
    }

    public static InvertedIndex getInstance() {
        if(index == null) {
            synchronized (InvertedIndex.class) {
                if(index == null) {
                    index = new InvertedIndex();
                }
            }
        }
        return index;
    }
}