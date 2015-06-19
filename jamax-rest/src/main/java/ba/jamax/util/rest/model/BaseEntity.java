package ba.jamax.util.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -5131408748512651233L;

	@Column(name = "CREATED_DATE")
	@JsonProperty(value="created")
	@JsonIgnore
    private Date created;
    
    @Column(name = "CREATED_BY")
	@JsonProperty(value="createdBy")
    @JsonIgnore
    private String createdBy;
    
    @Column(name = "MODIFIED_DATE")
	@JsonProperty(value="modified")
    @JsonIgnore
    private Date modified;
    
    @Column(name = "MODIFIED_BY")
	@JsonProperty(value="modifiedBy")
    @JsonIgnore
    private String modifiedBy;

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}