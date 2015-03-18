/*
 * Copyright (C) 2015 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.osgi.ecm.util.method;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Util methods for ECM Util Method.
 */
public final class MethodUtil {

  /**
   * Check if method is accesible from the provided {@link Class} parameter based on the chapter
   * <i>112.9.4 Locating Component Methods</i> of OSGi compendium specification:
   *
   * <p>
   * <i>"If the method has the public or protected access modifier, then access is permitted.
   * Otherwise, if the method has the private access modifier, then access is permitted only if the
   * method is declared in the component implementation class. Otherwise, if the method has default
   * access, also known as package private access, then access is permitted only if the method is
   * declared in the component implementation class or if the method is declared in a superclass and
   * all classes in the hierarchy from the component implementation class to the superclass,
   * inclusive, are in the same package and loaded by the same class loader."</i>
   *
   * @param clazz
   *          The type that must have access to the method.
   * @param method
   *          The method that should be accessible from the class.
   * @param privateMethodAccapted
   *          Whether a method that is declared in clazz, should be accessible even if it is
   *          private. Based on the content of the specification, these methods should be marked as
   *          acecssible as well. However, private methods cannot be called via reflection by
   *          default, so there is an option to exclude private methods.
   */
  public static boolean isMethodAccessibleFromClass(final Class<?> clazz, final Method method,
      final boolean privateMethodAccapted) {
    Class<?> declaringClassOfMethod = method.getDeclaringClass();

    if (!declaringClassOfMethod.isAssignableFrom(clazz)) {
      return false;
    }

    int modifiers = method.getModifiers();

    if ((modifiers & (Modifier.STATIC | Modifier.ABSTRACT)) != 0) {
      return false;
    }

    if ((modifiers & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0) {
      return true;
    }

    if ((modifiers & Modifier.PRIVATE) != 0) {
      return privateMethodAccapted && declaringClassOfMethod.equals(clazz);
    }

    if (clazz.equals(declaringClassOfMethod)) {
      return true;
    }

    Class<?> subClass = clazz;
    Class<?> parentClass = clazz.getSuperclass();

    while (!subClass.equals(declaringClassOfMethod)) {
      if (!Objects.equals(subClass.getClassLoader(), parentClass.getClassLoader())
          || !Objects.equals(subClass.getPackage(), parentClass.getPackage())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Locates the first method that exists from the definition array. For more information about the
   * alorithm of the locating algorithm, see {@link MethodDescriptor#locate(Class, boolean)}.
   *
   * @param clazz
   *          The class where the search is started. The class and its super classes are checked.
   * @param privateMethodAccepted
   *          Whether to accept private method if it is locaed in the class that is specified with
   *          the clazz parameter or not.
   * @param methodDefinitions
   *          The method definitions that are searched in preference order.
   * @return The found method or {@code null} if no matching method was found.
   */
  public static Method locateMethodByPreference(final Class<?> clazz,
      final boolean privateMethodAccepted, final MethodDescriptor... methodDefinitions) {

    Objects.requireNonNull(clazz, "Clazz must not be null");
    Objects.requireNonNull(methodDefinitions, "At least one method definition must be specified");

    if (methodDefinitions.length == 0) {
      throw new IllegalArgumentException("At least one method definition must be specified");
    }

    Method locatedMethod = null;
    for (int i = 0; i < methodDefinitions.length && locatedMethod == null; i++) {
      MethodDescriptor methodDefinition = methodDefinitions[i];
      locatedMethod = methodDefinition.locate(clazz, privateMethodAccepted);
    }

    return locatedMethod;

  }

  private MethodUtil() {
  }
}
