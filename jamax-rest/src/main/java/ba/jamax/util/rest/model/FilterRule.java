package ba.jamax.util.rest.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
	@JsonSubTypes({  
	    @Type(value = Rule.class, name = "rule"),  
	    @Type(value = Filter.class, name = "filter") }) 
public abstract class FilterRule {

}