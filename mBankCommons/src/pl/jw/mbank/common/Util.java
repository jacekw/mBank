package pl.jw.mbank.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.jw.mbank.common.exception.MBankSfiDataRetrieveException;

public class Util {

	private static Log log = LogFactory.getLog(Util.class);

	public static String getString(String html) {
		return html.replaceAll(",", ".").replace("&nbsp;", "").trim();// "Â"
	}

	public static String getString(char[] html) {
		try {
			String htmlStr = new String(String.valueOf(html, 0, html.length).getBytes(Charset.forName("Windows-1250")),
					"UTF-8");

			return getString(htmlStr);
		} catch (UnsupportedEncodingException ex) {
			log.error("Conversion error: \"" + new String(html) + "\".", ex);
			throw new MBankSfiDataRetrieveException("Data encoding conversion error.", ex);
		}
	}

	public static String getDate(String html) throws UnsupportedEncodingException {
		return getString(html).substring(0, 10);
	}

	public static BigDecimal getNumber(String html) {
		try {
			return new BigDecimal(html.replaceAll("b/d", "0").replaceAll(",", ".").replace("&nbsp;", "").replaceAll(
					"[^-0-9\\.]", "").trim()).setScale(3);
		} catch (Exception e) {
			log.debug("Conversion error: \"" + html + "\".", e);
			return BigDecimal.ZERO;
		}
	}
}
