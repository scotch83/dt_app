package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Image extends SugarRecord<Image>{
	
	@Ignore
	private long id;
	
	private short priority;
	private byte [] image;

	public Image(){

	}


	@Override
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
