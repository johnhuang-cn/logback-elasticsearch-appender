package net.xdevelop.logback.appender;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

public class AsyncSender extends Thread {
	private final static int MAX_CAPACITY = 65536;
	
	private static BlockingQueue<Log> queue = new LinkedBlockingQueue<Log>(MAX_CAPACITY);
	private ElasticsearchTemplate esTemplate;
	
	public AsyncSender(ElasticsearchTemplate esTemplate) {
		this.esTemplate = esTemplate;
	}
	
	public void run() {
		while (true) {
			try {
				sendLog(queue.take());
			} catch (Exception e) {
			}
		}
	}
	
	public static void put(Log log) {
			queue.offer(log);
	}
	
	private void sendLog(Log log) {
		IndexQuery indexQuery = new IndexQueryBuilder().withId(log.getId())
				.withObject(log).build();
		esTemplate.index(indexQuery);
	}
}
