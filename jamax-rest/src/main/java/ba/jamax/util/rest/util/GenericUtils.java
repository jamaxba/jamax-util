package ba.jamax.util.rest.util;

import java.lang.reflect.Method;

import org.apache.commons.lang.WordUtils;

public class GenericUtils<T> {

	public Method getGetter(Class<T> entityClass, String key) {

		Method m = null;
		try {
			try {
				m = entityClass.getMethod(constructGetter(key));
			} catch (NoSuchMethodException e) {
				// it's not GET
				// let's try IS
				m = entityClass.getMethod(constructIsGetter(key));
			}
		} catch (Exception e) {
			// skip method - out of the scope
		}
		return m;
	}
	
	private String constructGetter(String strKey) {
		return "get" + WordUtils.capitalize(strKey);
	}

	private String constructIsGetter(String strKey) {
		return "is" + WordUtils.capitalize(strKey);
	}
}
