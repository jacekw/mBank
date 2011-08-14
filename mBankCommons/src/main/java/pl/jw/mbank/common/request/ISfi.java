package pl.jw.mbank.common.request;

import java.util.List;

import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.pk.SfiPk;

public interface ISfi extends IRequest {

	public SfiPk update(SfiData sfiData);

	public SfiData get(final SfiData pk);

	public SfiPk add(final SfiData data);

	public List<SfiData> get();

}