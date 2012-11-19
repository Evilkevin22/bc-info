package tk.wouterhabets.android.bcinfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSHandler extends DefaultHandler {

	private Post currentPost = new Post();
	StringBuffer chars = new StringBuffer();

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) {

		chars = new StringBuffer();
		if (localName.equalsIgnoreCase("item")) {

		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equalsIgnoreCase("title")
				&& currentPost.getTitle() == null) {
			currentPost.setTitle(chars.toString());

		}
		if (localName.equalsIgnoreCase("pubDate")
				&& currentPost.getPubDate() == null) {
			currentPost.setPubDate(chars.toString());

		}
		if (localName.equalsIgnoreCase("link")
				&& currentPost.getUrl() == null) {
			currentPost.setUrl(chars.toString());
		}

		if (localName.equalsIgnoreCase("item")) {
			// PostList.add(currentPost);
			currentPost = new Post();
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}

}
