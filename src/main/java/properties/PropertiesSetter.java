package properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * <p>Provide static methods to automatically set object's properties.
 * More info provided in {@link PropertiesSetter#setProperties setProperties().}</p>
 * Created by Илья on 26.03.2017.
 */
public class PropertiesSetter {
    /** default logger */
    private static final Logger log = Logger.getLogger(PropertiesSetter.class);

    /**
     * Set object's properties according to provided config. Target object must
     * have setProperty or isProperty (in case of boolean) and setProperty methods.
     * @param o target object
     * @param cl class of the target object
     * @param config configurations
     * @throws NoSuchMethodException if a matching method is not found or if the name is "&lt;init&gt;"or "&lt;clinit&gt;".
     * @throws IllegalAccessException if the method is an instance method and the specified object argument is
     * not an instance of the class or interface declaring the underlying method (or of a subclass or implementor
     * thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for primitive
     * arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding
     * formal parameter type by a method invocation conversion.
     */
    public static void setProperties(Object o, Class<?> cl, Configuration config)
            throws NoSuchMethodException, IllegalAccessException {
        Iterator<String> keys = config.getKeys();
        while(keys.hasNext()){
            String key = keys.next();
            String propertyName = key.substring(0, 1).toUpperCase() + key.substring(1);
            try {
                Method getter;
                try {
                    getter = cl.getMethod("get" + propertyName);
                } catch (NoSuchMethodException e) {
                    // if a boolean property
                    getter = cl.getMethod("is" + propertyName);
                }

                Class<?> getterReturnType = getter.getReturnType();

                Method setter = cl.getMethod("set" + propertyName, getterReturnType);
                setter.invoke(o, config.get(getterReturnType, key));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
