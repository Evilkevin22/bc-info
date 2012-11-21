package tk.wouterhabets.android.bcinfo;

public class Post {

	private String title;
	private String url;
	private String description;
	private String pubDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPubDate() {
		return pubDate;
	}

}
