package ba.jamax.util.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SubTestEntity1 extends BaseTestEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = -6411594341328712352L;

	@Id
    @Column
    private Long id;
    @Column
    private String prop1;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProp1() {
		return prop1;
	}
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}
}