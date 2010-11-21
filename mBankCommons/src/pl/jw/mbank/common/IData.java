package pl.jw.mbank.common;

import java.io.Serializable;

import javax.persistence.Transient;

public interface IData<P extends IPk> extends Serializable {
	@Transient
	public P getPk();
}
