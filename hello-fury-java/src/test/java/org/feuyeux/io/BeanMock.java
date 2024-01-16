package org.feuyeux.io;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanMock {

  private static int mockDepth = 0;

  private static final int MAX_MOCK_INSTANCE = 100;

  /**
   * Generate a mock object with randomly assigned values based on the object property types. To
   * avoid circular nested references, the number of object constructions is limited. Note that
   * arrays and enumerated properties are not supported for mocking.
   */
  public static <T> T mockBean(Class<T> bean) {
    mockDepth = 1;
    T object;
    try {
      object = bean.newInstance();
      mockProperty(object);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("mockProperty invoke failed", e);
    }
    return object;
  }

  private static void mockProperty(Object object)
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    if (mockDepth++ > MAX_MOCK_INSTANCE) {
      return;
    }
    PropertyDescriptor[] beanPds = getPropertyDescriptors(object.getClass());
    assert beanPds != null;
    for (PropertyDescriptor pds : beanPds) {
      Method writeMethod = pds.getWriteMethod();
      if (writeMethod != null) {
        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
          writeMethod.setAccessible(true);
        }
        Class<?> writeType = writeMethod.getParameterTypes()[0];
        if (writeType.equals(Boolean.class) || writeType.equals(boolean.class)) {
          writeMethod.invoke(object, new Random().nextBoolean());
        } else if (writeType.equals(Byte.class) || writeType.equals(byte.class)) {
          writeMethod.invoke(object, (byte) (new Random().nextInt(256) - 128));
        } else if (writeType.equals(Character.class) || writeType.equals(char.class)) {
          writeMethod.invoke(object, (char) (new Random().nextInt(26) + 'a'));
        } else if (writeType.equals(Double.class) || writeType.equals(double.class)) {
          writeMethod.invoke(object, Math.random());
        } else if (writeType.equals(Float.class) || writeType.equals(float.class)) {
          writeMethod.invoke(object, new Random().nextFloat());
        } else if (writeType.equals(Integer.class) || writeType.equals(int.class)) {
          writeMethod.invoke(object, new Random().nextInt());
        } else if (writeType.equals(Long.class) || writeType.equals(long.class)) {
          writeMethod.invoke(object, new Random().nextLong());
        } else if (writeType.equals(Short.class) || writeType.equals(short.class)) {
          writeMethod.invoke(object, (short) (new Random().nextInt(65535) - 32768));
        } else if (writeType.equals(String.class)) {
          writeMethod.invoke(object, UUID.randomUUID().toString() + new Random().nextLong());
        } else if (Collection.class.isAssignableFrom(writeType)) {
          Type[] genericParameterTypes = writeMethod.getGenericParameterTypes();
          Type genericParameterType = genericParameterTypes[0];
          String typeName = genericParameterType.getTypeName();
          if (typeName.equals("java.util.List<java.lang.String>")) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10_000; i++) {
              list.add(UUID.randomUUID().toString() + new Random().nextLong());
            }
            writeMethod.invoke(object, list);
          } else {
            // TODO
            log.debug("TODO");
          }
        } else if (writeType.isEnum()
            || writeType.isArray()
            || Map.class.isAssignableFrom(writeType)) {
          continue;
        } else {
          Object instance = writeType.newInstance();
          mockProperty(instance);
          writeMethod.invoke(object, instance);
        }
      }
    }
  }

  public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
    BeanInfo beanInfo = null;
    try {
      beanInfo = Introspector.getBeanInfo(beanClass);
    } catch (IntrospectionException e) {
      log.error("getPropertyDescriptors failed", e);
    }
    if (null == beanInfo) return null;
    return beanInfo.getPropertyDescriptors();
  }
}
