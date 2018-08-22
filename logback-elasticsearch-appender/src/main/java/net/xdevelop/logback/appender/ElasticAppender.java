package net.xdevelop.logback.appender;

import static org.apache.commons.lang.StringUtils.split;
import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;

import java.net.InetAddress;
import java.util.Date;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Elasticsearch appender for logback
 * 
 * @author: john.h.cn@gmail.com
 */
public class ElasticAppender extends AppenderBase<ILoggingEvent> {
	private static Object locker = new Object();
	private static ElasticsearchTemplate esTemplate;
	
	private Log log = new Log();
	private String clusterName;
	private String clusterNodes;
	private String applicationName = "default";
	
	@Override
	public void start() {
		super.start();
		
		synchronized(locker) {
			if (esTemplate != null) {
				return;
			}
			
			try {
				Settings settings = Settings.builder()
						.put("cluster.name", clusterName)
//						.put("client.transport.sniff", true)
//						.put("client.transport.ignore_cluster_name", Boolean.FALSE)
//						.put("client.transport.ping_timeout", "5s")
//						.put("client.transport.nodes_sampler_interval", "5s")
						.build();

				PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
				for (String clusterNode : split(clusterNodes, ",")) {
					String hostName = substringBeforeLast(clusterNode, ":");
					String port = substringAfterLast(clusterNode, ":");
					client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port)));
				}
				client.connectedNodes();
				
				esTemplate = new ElasticsearchTemplate(client);
				
				// create elasticsearch index and mapping at the first time
				if (!esTemplate.indexExists(Log.class)) {
					esTemplate.createIndex(Log.class);
					esTemplate.putMapping(Log.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void append(ILoggingEvent eo) {
		if (esTemplate != null) {
			synchronized (log) {
				log.setApplicationName(applicationName);
				log.setId(applicationName + "-" + System.currentTimeMillis());
				log.setLevel(eo.getLevel().toString());
				log.setLoggerName(eo.getLoggerName());
				log.setMessage(eo.getFormattedMessage());
				log.setThreadName(eo.getThreadName());
				log.setTimeStamp(new Date(eo.getTimeStamp()));
				IndexQuery indexQuery = new IndexQueryBuilder().withId(log.getId())
						.withObject(log).build();
				
				esTemplate.index(indexQuery);
			}
		}
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

/*	 @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLoggerList().forEach(new Consumer<Logger>() {

            @Override
            public void accept(Logger logger) {
                logger.addAppender(ElasticAppender.this);
            }
        });

        setContext(context);
        start();
    }*/
}
