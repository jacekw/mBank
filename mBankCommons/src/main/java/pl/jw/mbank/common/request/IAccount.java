package pl.jw.mbank.common.request;

import java.sql.SQLException;
import java.util.List;

import pl.jw.mbank.common.dto.AccountData;

public interface IAccount extends IRequest {
	public void update(final AccountData data) throws SQLException;

	public AccountData get(final Integer pk) throws SQLException;

	public List<AccountData> get() throws SQLException;
}
