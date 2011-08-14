package pl.jw.mbank.common.request;

import pl.jw.mbank.common.dto.AccountData;
import pl.jw.mbank.common.dto.InvestmentData;
import pl.jw.mbank.common.dto.SfiData;

public interface IInvestment extends IRequest {
	public abstract InvestmentData get(Integer pk);

	public abstract InvestmentData getBySfi(AccountData accountData, SfiData sfiData);

	public abstract void update(InvestmentData data);

}
