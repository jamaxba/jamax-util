package ba.jamax.util.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SubEntityDiff {

	SUB1("SUB1"),
	SUB2("SUB2"),
	UNKNOWN("UNKNOWN");

	private final String value;

	private SubEntityDiff(String value) {
		this.value = value;
	}
	@JsonValue
	public String toValue() {
		return this.value;
	}
	@JsonCreator
	public static SubEntityDiff fromValue(String value) {
		if (value != null) {
			for (SubEntityDiff b : SubEntityDiff.values()) {
				if (value.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		return UNKNOWN;
	}
	@Override
	public String toString()
	{
		return this.value;
	}
}