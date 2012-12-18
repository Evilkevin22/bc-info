package tk.wouterhabets.android.bcinfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class RSSHandler extends DefaultHandler {

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

		if (localName.equalsIgnoreCase("title")) {
			Log.i("RSSHandler", "Title gevonden: " + chars.toString());

		}

		if (localName.equalsIgnoreCase("description")) {
			Log.i("RSSHandler", "Description gevonden: " + chars.toString());

		}

		if (localName.equalsIgnoreCase("item")) {
			Log.i("RSSHandler", "Item gevonden.");
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}

}
