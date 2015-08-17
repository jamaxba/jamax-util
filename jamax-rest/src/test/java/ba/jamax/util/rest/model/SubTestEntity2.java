package ba.jamax.util.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SubTestEntity2 extends BaseTestEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = 1088632703216806922L;

	@Id
	@Column
	private Long id;
	@Column
	private String prop2;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProp2() {
		return prop2;
	}
	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}
}