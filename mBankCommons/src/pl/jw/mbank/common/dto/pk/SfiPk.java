package pl.jw.mbank.common.dto.pk;

import pl.jw.mbank.common.IPk;

public class SfiPk implements IPk {

	private int id;

	public SfiPk() {
	}

	public SfiPk(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SfiPk [id=" + id + "]";
	}

}
