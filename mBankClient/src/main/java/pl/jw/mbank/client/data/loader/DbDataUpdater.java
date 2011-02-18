package pl.jw.mbank.client.data.loader;

import java.util.List;

import pl.jw.mbank.client.Env;
import pl.jw.mbank.client.data.DataUpdaterAdapter;
import pl.jw.mbank.client.gui.SystemTraySupport;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.request.IPresentation;

public class DbDataUpdater extends DataUpdaterAdapter<PresentationData> {
	@Override
	public List<PresentationData> get() throws Exception {
		try {
			return Env.requestData(IPresentation.class).get(null, null);
		} finally {
			SystemTraySupport.showTrayPopUpMessage("Data loaded");
		}
	}
}