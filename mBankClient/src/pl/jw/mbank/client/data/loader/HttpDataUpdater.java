package pl.jw.mbank.client.data.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.data.IDataUpdater;
import pl.jw.mbank.client.gui.SystemTraySupport;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.request.IPresentation;

public class HttpDataUpdater implements IDataUpdater<PresentationData> {

	private static Log log = LogFactory.getLog(HttpDataUpdater.class);

	@Override
	public List<PresentationData> get() throws Exception {
		List<PresentationData> data = getData();

		log.info("Pobranych kursów: " + data.size());

		Env.requestData(IPresentation.class).update(data);

		if (log.isDebugEnabled())
			log.debug(getHtml());

		//		try {
		//			return Env.requestData(IPresentation.class).get(null, null);
		//		} finally {
		SystemTraySupport.showTrayPopUpMessage("Data updated");
		//		}
		return null;
	}

	private List<PresentationData> getData() throws IOException {

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// Create a method instance.
		GetMethod method = new GetMethod(Env.URL);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		MBankSfiHtmlParser mSfiParser = new MBankSfiHtmlParser();
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

			javax.swing.text.html.HTMLEditorKit.Parser parse = new MBankSfiHtmlParser.HtmlParser().getParser();

			parse.parse(br, mSfiParser, true);
		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			throw e;
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			throw e;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

		return mSfiParser.getData();
	}

	private String getHtml() throws IOException {
		StringBuilder sb = new StringBuilder();

		URL url = new URL(Env.URL);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		try {
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
		} finally {
			in.close();
		}
		return sb.toString();
	}
}