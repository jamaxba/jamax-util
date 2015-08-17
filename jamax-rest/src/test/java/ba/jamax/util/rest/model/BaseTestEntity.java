package ba.jamax.util.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "subEntityDiff",
	    defaultImpl = SubEntityDiff.class, 
	    visible = true)  
	@JsonSubTypes({  
	    @Type(value = SubTestEntity1.class, name = "SUB1"),  
	    @Type(value = SubTestEntity2.class, name = "SUB2") }) 
@MappedSuperclass
public abstract class BaseTestEntity extends BaseEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = 8481306291760430990L;
	
	@Column
	private String name;
	@Column
	private String description;
	@Column
	@Enumerated(EnumType.STRING)
	private SubEntityDiff subEntityDiff;
	
	public abstract Long getId();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SubEntityDiff getSubEntityDiff() {
		return subEntityDiff;
	}
	public void setSubEntityDiff(SubEntityDiff subEntityDiff) {
		this.subEntityDiff = subEntityDiff;
	}
}