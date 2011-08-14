package pl.jw.mbank.common.request;

import java.util.List;

import pl.jw.mbank.common.dto.AccountData;

public interface IAccount extends IRequest {
	public void update(final AccountData data);

	public AccountData get(final Integer pk);

	public List<AccountData> get();
}
