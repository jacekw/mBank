package pl.jw.mbank.common.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Account")
@Table(name = "Account", uniqueConstraints = @UniqueConstraint(columnNames = { "NAME" }))
public class AccountData implements Serializable {

	private int id;

	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AccountData [id=" + id + ", name=" + name + "]";
	}

}
