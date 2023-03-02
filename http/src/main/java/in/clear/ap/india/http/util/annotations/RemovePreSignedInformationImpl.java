package in.clear.ap.india.http.util.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class RemovePreSignedInformationImpl {

    public void process(Object object) throws Exception {
        Class<?> parentClass = object.getClass();
        for (Field field : parentClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(RemovePreSignedInformation.class)) {
                String fieldName = field.getName();
                String getterMethodName =
                        "get" +
                                fieldName.substring(0, 1).toUpperCase() +
                                fieldName.substring(1);
                Method getterMethod = parentClass.getMethod(getterMethodName);
                String returnValue = (String) getterMethod.invoke(object);
                if(returnValue.contains("?")) {
                    String[] arr = returnValue.split(Pattern.quote("?"));
                    String setterMethodName = getterMethodName.substring(0, 1).replace("g", "s")
                            + getterMethodName.substring(1);
                    Method setterMethod = parentClass.getMethod(setterMethodName, String.class);
                    setterMethod.invoke(object, arr[0]);
                }
            }
        }
    }
}
