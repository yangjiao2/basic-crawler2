package ir.assignments.three;

import java.util.ArrayList;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.List;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;


/*
 * Getting info from: 
 * https://github.com/yasserg/crawler4j
 * 
 * Getting idea of filter from:
 * https://github.com/saybia1993/SearchEngine/blob/master/Web%20Crawler/Crawler
 * 
 */

public class Crawler extends WebCrawler {
	
	public static ArrayList <String> crawlweblist = new ArrayList<String>();
	public static File logtxt = new File("C:\\Users\\Yang\\Desktop\\CrawlLog.txt");
	public static File urltxt = new File("C:\\Users\\Yang\\Desktop\\Urls.txt"); 
	public static File texttxt = new File("C:\\Users\\Yang\\Desktop\\Text.txt"); 
	public static File tokenizedtxt = new File("C:\\Users\\Yang\\Desktop\\TokenizedText.txt");
	public static List<Frequency> frequencies = new ArrayList<Frequency>();

	public static Set<String> urlset = new HashSet<String>();
	public static Map<String, Integer> subdomainmap = new HashMap<String, Integer>();
	public static String longestpage = "";
	public static long longestpagelength  = 0;
	
	// exclude files/documents
	private final static Pattern FILTERS1 = Pattern.compile(".*\\.(bmp|gif|jpe?g|png|tiff?|pdf|ico|xaml|pict|rif|pptx?|ps" +
            "|mid|mp2|mp3|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox" +
            "|avi|mov|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv" +
            "|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha)" +
            "(\\?.*)?$");
	
	@Override
	public boolean shouldVisit(WebURL url) {
		 String href = url.getURL().toLowerCase();

		 
		 return !FILTERS1.matcher(href).matches() && href.matches("http://.*\\.ics\\.uci\\.edu.*") && !href.matches(".*drzaius\\.ics\\.uci\\.edu.*") && !href.matches(".*ftp\\.ics\\.uci\\.edu.*") && !href.matches(".*seraja\\.ics\\.uci\\.edu.*") && !href.matches(".*fano\\.ics\\.uci\\.edu.*")  && !href.contains("wics.com/news/") && !href.contains("archives.ics.uci.edu") && !href.contains("calendar") && !href.contains("?");


	}

	@Override
    public void visit(Page page) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("\n");
    		String url = page.getWebURL().getURL();
    		
            crawlweblist.add(url); 
            System.out.println("Number of URLs which have been crawled: " + crawlweblist.size());
            int docid = page.getWebURL().getDocid();
            String domain = (page.getWebURL()).getDomain();
            String path = page.getWebURL().getPath();
            String subDomain = page.getWebURL().getSubDomain();
            String domainforstore = subDomain.replaceAll("(.*)(\\.ics$)", "$1$2\\.uci.edu");
            String parentUrl = page.getWebURL().getParentUrl();
            
            if(subdomainmap.containsKey(subDomain))
				subdomainmap.put(domainforstore,subdomainmap.get(domainforstore)+1);
			else{
				subdomainmap.put(domainforstore,1);
			}

            
            buffer.append("Docid: " + docid+'\n');
            buffer.append("URL: " + url+'\n');
            buffer.append("Domain: '" + domain + "'"+'\n');
            buffer.append("Sub-domain: '" + domainforstore + "'"+'\n');
            buffer.append("Path: '" + path + "'"+'\n');
            buffer.append("Parent page: " + parentUrl+'\n');
            
            urlset.add(url);
            
            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml(); 
                    List<WebURL> links = htmlParseData.getOutgoingUrls();
                    buffer.append("Text length: " + text.length()+'\n');
                    buffer.append("Html length: " + html.length()+'\n');
                    buffer.append("Number of outgoing links: " + links.size()+'\n');
                    
                    getLongestPage(url, text);

                    	try {
                    		System.out.println(buffer.toString());
                    		CrawlLogToFile(buffer.toString(), logtxt);
                    		PageTextToFile(text, texttxt);
                    	} catch (IOException e) {
                    		e.printStackTrace();
                    	}

            }
            buffer.append("\n");
            
     }

	public void PageTextToFile(String input, File rawfile) throws IOException{
		FileWriter writer = new FileWriter(rawfile, true);
		writer.write(input);
		writer.close();

	}
	

	public void CrawlLogToFile(String input, File file) throws IOException{
		FileWriter writer = new FileWriter(file, true);
		writer.write(input);
		writer.close();
	}
	
	public void getLongestPage(String url, String text){
		if (longestpage == "" || text.length() > longestpagelength){
			longestpage = url;
			longestpagelength = (long) text.length();
		}
	}

			
}
