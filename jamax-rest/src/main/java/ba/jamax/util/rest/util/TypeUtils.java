package ba.jamax.util.rest.util;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

@Component
public class TypeUtils {

	public Serializable getCorrectObjectType(Method method, Serializable value) {
		try {
			Class<?> returnType = method.getReturnType();
			if (returnType.equals(value.getClass())) {
				return value;
			} else if (Integer.class.equals(returnType)) {
				return Integer.valueOf((String) value);
			} else if (Long.class.equals(returnType)) {
				return Long.valueOf((String) value);
			} else if (Float.class.equals(returnType)) {
				return Float.valueOf((String) value);
			} else if (Double.class.equals(returnType)) {
				return Double.valueOf((String) value);
			} else if (Boolean.class.equals(returnType)) {
				return Boolean.valueOf((String) value);
			} else if (returnType.isEnum()) {
				return (Serializable) returnType.getDeclaredMethod("valueOf", String.class).invoke(null, (String) value);
			}
		} catch (Exception e) {
			// skip method - out of the scope
		}
		return value;
	}
}