package net.xdevelop.logback.appender;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="log-index", type="Log")
public class Log implements Serializable {
	private static final long serialVersionUID = 4902288107813352023L;
	
	@Id
	private String id;
	
	@Field(type=FieldType.Text, store=false)
	private String applicationName;
	
	@Field(type=FieldType.Text, store=false)
	private String level;
	
	@Field(type=FieldType.Text, store=false)
	private String threadName;
	
	@Field(type=FieldType.Text, store=false)
	private String loggerName;
	
	@Field(type=FieldType.Text, store=false)
	private String message;
	
	@Field(
		    type = FieldType.Date, 
		    store = false//, 
		    //format = DateFormat.custom, pattern = "yyyy-MM-dd hh:mm:ss"
		)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date timeStamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
