/**
 * 
 */
package pl.jw.mbank.client.data.loader;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.jw.mbank.common.Util;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;

class MBankSfiHtmlParser extends ParserCallback {

	private static Log log = LogFactory.getLog(MBankSfiHtmlParser.class);

	private enum HtmlParseState {
		TR, TD, A, OTHERS;
	}

	private static class StateIterator {

		private enum ReadState {

			// Wartoœæ j.u., Zmiana (%), Stopa zwrotu, ? , ? , ? , Op³ata za
			// zarz¹dzanie aktywami, Wielkoœæ aktywów funduszu, Minimalna
			// pierwsza
			// wp³ata,Procent aktywów,Data
			NAME, VALUE, DELTA, INCOME, UI_I, UI_II, UI_III, FEE, FUND, MIN_FIRST, CLIENTS_MONEY, DATE;
		}

		final List<PresentationData> data = new ArrayList<PresentationData>();
		private PresentationData currentPosData;

		private StateIterator.ReadState readState;

		public void init() {
			readState = ReadState.NAME;
		}

		public void nextState(String value) {
			if (readState == ReadState.NAME) {
				currentPosData = new PresentationData();
				currentPosData.setInvestmentData(new InvestmentData());
				currentPosData.setSfiData(new SfiData());
				currentPosData.setStockQuotesData(new StockQuotesData());

				currentPosData.getStockQuotesData().setSfi(currentPosData.getSfiData());
				currentPosData.getSfiData().setName(value);

			} else if (readState == ReadState.VALUE) {
				currentPosData.getStockQuotesData().setValue(Util.getNumber(value));
			} else if (readState == ReadState.DELTA) {
				currentPosData.getStockQuotesData().setDelta(Util.getNumber(value));
			} else if (readState == ReadState.DATE) {
				Date date = null;
				try {
					value = "20" + value;
					date = Date.valueOf(value);
				} catch (IllegalArgumentException e) {
					log.info("Date conversion error.\nFund: \"" + currentPosData.getSfiData().getName() + "\"\nDate: "
							+ value + ".\nAssuming today.");
					date = new Date(System.currentTimeMillis());
				}
				currentPosData.getStockQuotesData().setDate(date);
				this.data.add(currentPosData);
				currentPosData = null;
			}

			if (readState == ReadState.NAME) {
				readState = ReadState.VALUE;
			} else if (readState == ReadState.VALUE) {
				readState = ReadState.DELTA;
			} else if (readState == ReadState.DELTA) {
				readState = ReadState.INCOME;
			} else if (readState == ReadState.INCOME) {
				readState = ReadState.UI_I;
			} else if (readState == ReadState.UI_I) {
				readState = ReadState.UI_II;
			} else if (readState == ReadState.UI_II) {
				readState = ReadState.UI_III;
			} else if (readState == ReadState.UI_III) {
				readState = ReadState.FEE;
			} else if (readState == ReadState.FEE) {
				readState = ReadState.FUND;
			} else if (readState == ReadState.FUND) {
				readState = ReadState.MIN_FIRST;
			} else if (readState == ReadState.MIN_FIRST) {
				readState = ReadState.CLIENTS_MONEY;
			} else if (readState == ReadState.CLIENTS_MONEY) {
				readState = ReadState.DATE;
			} else if (readState == ReadState.DATE) {
				readState = ReadState.NAME;
			}
		}
	}

	static class HtmlParser extends HTMLEditorKit {

		@Override
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	/******************************************************************************************************************************/

	private final StateIterator stateIterator = new StateIterator();

	private MBankSfiHtmlParser.HtmlParseState htmlParseState;

	public List<PresentationData> getData() {
		return stateIterator.data;
	}

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		if (t == Tag.TR) {
			htmlParseState = HtmlParseState.TR;
		} else if (t == Tag.TD) {
			String val = (String) a.getAttribute(HTML.Attribute.CLASS);
			if (val == null || val.trim().matches("tra1"))
				htmlParseState = HtmlParseState.OTHERS;
			else
				htmlParseState = HtmlParseState.TD;

		} else if (t == Tag.A) {

			String val = (String) a.getAttribute(HTML.Attribute.TARGET);
			if (val != null && val.matches("_parent"))
				stateIterator.init();

		} else {
			htmlParseState = HtmlParseState.OTHERS;
		}
	}

	@Override
	public void handleText(char[] data, int pos) {

		if (htmlParseState == HtmlParseState.TD) {
			stateIterator.nextState(Util.getString(data));
		}
	}

	@Override
	public void handleEndTag(Tag t, int pos) {
		if (t == Tag.TR) {
		} else if (t == Tag.TD) {

		} else if (t == Tag.A) {
		}
	}

}