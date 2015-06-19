package ba.jamax.util.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ba.jamax.util.rest.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "TEST_ENTITY")
@JsonIgnoreProperties({"action"})
@JsonRootName(value = "testEntity")
@SequenceGenerator(name = "idSequence", sequenceName = "TEST_ENTITY_ID_SEQ")
public class TestEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8284431320992417408L;

	@Id
    @Column(name = "TEST_ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idSequence")
	@JsonProperty(value="id")
    private int titleId;

    @Column(name = "TEST_ENTITY_TITLE")
    private String title;

	public int getTitleId() {
		return titleId;
	}
	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
