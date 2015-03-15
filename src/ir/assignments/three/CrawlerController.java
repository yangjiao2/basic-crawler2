package ir.assignments.three;



import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.ArrayList;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.io.BufferedReader;

/**
 * this is for configuration of crawlers that visiting websites
 *
 */
public class CrawlerController{
	public static File answers = new File("C:\\Users\\Yang\\Desktop\\Answers.txt"); 
	public static File subdomains = new File("C:\\Users\\Yang\\Desktop\\Subdomains.txt");
	public static File commonWords = new File("C:\\Users\\Yang\\Desktop\\CommonWords.txt");
	public static List<String> words = new ArrayList<String> ();
	
	static int mytime = 0;
	static Boolean runing = true;
	//	public Timer timer;
	private final SimpleDateFormat date = new SimpleDateFormat("mm.ss.SSS");
	//	class TimerTestTask extends TimerTask {     
	//		public void run() {     
	//			mytime++;
	//			System.out.println("Time: "+String.valueOf(mytime)+" seconds" );     
	//			if(!runing)
	//				timer.cancel();
	//		}     
	//	}  
	//	private void settimer()
	//	{
	//		timer = new Timer();     
	//		timer.schedule(new TimerTestTask(),0, 1000);
	//
	//	}

	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = "storage";
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setUserAgentString("UCI Inf141-CS121 crawler 82222745");
		config.setPolitenessDelay(300);
		config.setResumableCrawling(false);


		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		System.out.println("Timer starts");
		long tStart = System.currentTimeMillis();
		controller.addSeed("http://www.ics.uci.edu");
		controller.start(Crawler.class, numberOfCrawlers);


		if(controller.isFinished())
		{
			ArrayList<String> stopwords = new ArrayList<String>();
			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Yang\\workspace_cpp\\Assignment 3\\stopwords.txt"));
			StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	        	stopwords.add(line);
	            line = br.readLine();
	        }
	        
			System.out.println("Timer stops");

			
			
			WriteToSubdomains(Crawler.subdomainmap);
			words = Utilities.tokenizeFile(Crawler.texttxt);
			words.removeAll(stopwords);
			WriteToCommonWords(words);
			

			WriteToAnswers("1.How much time did it take to crawl the entire domain?\n");
			System.out.println(tDelta);
			WriteToAnswers(String.valueOf(tDelta) + "\n");
			WriteToAnswers("2. How many unique pages did you find in the entire domain? (Uniqueness is established by the URL, not the page's content.)"+ "\n");
			WriteToAnswers(String.valueOf(Crawler.urlset.size())+ "\n");
			System.out.println(Crawler.urlset.size());
			WriteToAnswers("3. How many subdomains did you find? "+ "\n");
			WriteToAnswers(String.valueOf(Crawler.subdomainmap.size())+ "\n");
			System.out.println(Crawler.subdomainmap.size());
			WriteToAnswers("4. What is the longest page in terms of number of words? "+ "\n");
			WriteToAnswers(Crawler.longestpage + " with length " + String.valueOf(Crawler.longestpagelength)+ "\n");
			System.out.println(Crawler.longestpage);
			WriteToAnswers("5. What are the 500 most common words in this domain?"+ "\n");
			WriteToAnswers("See CommonWords.txt"+ "\n");
			System.out.println(words);

		}
	}

	public static void WriteToAnswers(String input) throws IOException{
		FileWriter writer = new FileWriter(answers, true);
		writer.write(input);
		writer.close();
	}
	public static void WriteToSubdomains(Map<String, Integer> sd) throws IOException{
		FileWriter writer = new FileWriter(subdomains, true);
		for (String key : sd.keySet()){
			writer.write(String.format("%s, %d       %n", key, sd.get(key)));
		}
		writer.close();
	}
	public static void WriteToCommonWords(List<String> words) throws IOException{
		FileWriter writer = new FileWriter(commonWords);
		int lastIndex = 500;
		if(words.size() <= 500){
			lastIndex = words.size();
		}
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words.subList(0, lastIndex));
		for (Frequency wf : frequencies){
			writer.write(String.format("%s      %n", wf.getText()));
		}
		writer.close();
	}

}
