package ba.jamax.util.rest.model;

import java.util.Collections;
import java.util.List;

public class Filter extends FilterRule {
	// filters	"{"groupOp":"AND","rules":[{"field":"location","op":"cn","data":"wi"},
	// 									   {"field":"maker","op":"cn","data":"test"}]}"

	private String groupOp; 
	private List<? extends FilterRule> rules = Collections.emptyList();
	
	public String getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	public List<? extends FilterRule> getRules() {
		return rules;
	}
	public void setRules(List<? extends FilterRule> rules) {
		this.rules = rules;
	} 	
}