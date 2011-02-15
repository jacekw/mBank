package pl.jw.mbank.common.request;

import java.sql.SQLException;
import java.util.List;

import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.pk.SfiPk;

public interface ISfi extends IRequest {

	public SfiPk update(SfiData sfiData) throws SQLException;

	public SfiData get(final SfiData pk) throws SQLException;

	public SfiPk add(final SfiData data) throws SQLException;

	public List<SfiData> get() throws SQLException;

}