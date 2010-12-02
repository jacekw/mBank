package pl.jw.mbank.common.request;

import java.sql.SQLException;
import java.util.List;

import pl.jw.mbank.common.dto.InvstmentDirectionsData;
import pl.jw.mbank.common.dto.PresentationData;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.PresentationSummaryData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.filter.DataFilter;

public interface IPresentation extends IRequest {
	public List<PresentationData> get(DataFilter filter) throws SQLException;

	public void update(List<PresentationData> dataList) throws SQLException;

	public PresentationSummaryData getSummary() throws SQLException;

	public List<PresentationGraphData> getGraphData(final SfiData sfiPk) throws SQLException;

	public List<InvstmentDirectionsData> getInvestmentDirectionsData() throws SQLException;

	public PresentationGraphData getPeriodSummaryData(final SfiData sfiPk, final int periodLenght) throws SQLException;
}