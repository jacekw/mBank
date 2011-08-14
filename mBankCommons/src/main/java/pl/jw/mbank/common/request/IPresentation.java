package pl.jw.mbank.common.request;

import java.util.List;

import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.InvstmentDirectionsData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.PresentationSummaryData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.filter.DataFilter;

public interface IPresentation extends IRequest {
	public List<PresentationData> get(AccountData accountData, DataFilter filter);

	public void update(List<PresentationData> dataList);

	public PresentationSummaryData getSummary(AccountData accountData);

	public List<PresentationGraphData> getGraphData(final SfiData sfiPk);

	public List<InvstmentDirectionsData> getInvestmentDirectionsData(Integer months);

	public PresentationGraphData getPeriodSummaryData(final SfiData sfiPk, final Integer periodLenght);
}