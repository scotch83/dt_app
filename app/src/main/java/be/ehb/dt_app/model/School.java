package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class School extends SugarRecord<School> {
	@Ignore
	private long id;

	private String name, gemeente;
	private short postcode;

	public School() {

	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNane() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGemeente() {
		return gemeente;
	}

	public void setGemeente(String gemeente) {
		this.gemeente = gemeente;
	}

	public short getPostcode() {
		return postcode;
	}

	public void setPostcode(short postcode) {
		this.postcode = postcode;
	}
}

