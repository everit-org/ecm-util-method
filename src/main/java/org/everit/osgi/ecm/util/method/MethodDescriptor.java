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
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Definition of a method that can be used to locate the method in a class or in one of its super
 * class.
 *
 */
public class MethodDescriptor {

  private static final String GROUP_METHOD_NAME = "methodName";

  private static final String GROUP_PARAMETER_TYPES = "parameterTypes";

  private static final String[] NO_PARAMETER_TYPES_SPECIFIED = null;

  private static final Pattern PATTERN_METHOD_DESCRIPTOR;

  private static final Pattern PATTERN_PARAM_TYPE;

  private static final String REGEX_METHOD_NAME = "(?<" + GROUP_METHOD_NAME
      + ">[\\p{L}_][\\p{L}\\p{N}_]*)";

  private static final String REGEX_PARAM_TYPE =
      "([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*(\\s*\\[\\s*\\])?";

  private static final String REGEX_PARAM_TYPE_WITH_SPACES = "\\s*" + REGEX_PARAM_TYPE + "\\s*";

  private static final String REGEX_PARAM_TYPE_WITH_SPACES_AND_COMMA = REGEX_PARAM_TYPE_WITH_SPACES
      + "\\,";

  private static final String REGEX_PARAMETER_TYPES = "(?<" + GROUP_PARAMETER_TYPES + ">\\((("
      + REGEX_PARAM_TYPE_WITH_SPACES_AND_COMMA + ")*(" + REGEX_PARAM_TYPE_WITH_SPACES + "))?\\))?";

  static {
    PATTERN_METHOD_DESCRIPTOR = Pattern
        .compile(REGEX_METHOD_NAME + REGEX_PARAMETER_TYPES);

    PATTERN_PARAM_TYPE = Pattern.compile(REGEX_PARAM_TYPE);
  }

  private static String removeWhiteSpacesFromParameterTypeString(final String parameterTypeString) {

    StringBuilder sb = new StringBuilder(parameterTypeString.length());

    char[] charArray = parameterTypeString.toCharArray();
    for (char c : charArray) {
      if (c > ' ') {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  private final String methodName;

  private final String[] parameterTypeNames;

  /**
   * Create a {@link MethodDescriptor} based on a method object. The parameters types will be listed
   * with their canonical name.
   *
   * @param method
   *          The method that is converted to a {@link MethodDescriptor}.
   */
  public MethodDescriptor(final Method method) {
    this.methodName = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    this.parameterTypeNames = new String[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      Class<?> parameterType = parameterTypes[i];
      this.parameterTypeNames[i] = parameterType.getCanonicalName();
    }
  }

  /**
   * Creates a {@link MethodDescriptor} based on its {@link String} representation.
   *
   * @param methodDescriptor
   *          The string representation of the {@link MethodDescriptor}. See {@link #toString()}.
   * @throws NullPointerException
   *           if the methodDescriptor parameter is <code>null</code>.
   * @throws IllegalArgumentException
   *           if the provided {@link String} representation cannot be parsed.
   */
  public MethodDescriptor(final String methodDescriptor) {
    Objects.requireNonNull(methodDescriptor, "Method descriptor cannot be null");
    Matcher matcher = PATTERN_METHOD_DESCRIPTOR.matcher(methodDescriptor);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("The method signature cannot be parsed: "
          + methodDescriptor);
    }

    this.methodName = matcher.group(GROUP_METHOD_NAME);
    String parameterTypesString = matcher.group(GROUP_PARAMETER_TYPES);

    if (parameterTypesString != null) {
      this.parameterTypeNames = parseParameterTypeNames(parameterTypesString);
    } else {
      this.parameterTypeNames = NO_PARAMETER_TYPES_SPECIFIED;
    }

  }

  /**
   * Creates a method descriptor based on the name of the method and the name of the type of the
   * parameters.
   *
   * @param name
   *          The name of the method.
   * @param parameterTypeNames
   *          The name of parameter types or <code>null</code> if the parameters are not defined. In
   *          case the parameters are not defined, the {@link #locate(Class, boolean)} method will
   *          return with the first found method that has the specified name. The type names can be
   *          specified in the form of {@link Class#getCanonicalName()} or
   *          {@link Class#getSimpleName()}.
   * @throws NullPointerException
   *           if the name parameter is <code>null</code> or any of the element of
   *           parameterTypeNames is <code>null</code>.
   */
  public MethodDescriptor(final String name, final String[] parameterTypeNames) {
    Objects.requireNonNull(name, "Name of method must not be null");

    this.methodName = name;

    if (parameterTypeNames != null) {
      this.parameterTypeNames = new String[parameterTypeNames.length];
      for (int i = 0; i < parameterTypeNames.length; i++) {
        String parameterTypeName = parameterTypeNames[i];
        Objects.requireNonNull(parameterTypeName,
            "Null element found in the array of parameterTypeNames");
        if (!PATTERN_PARAM_TYPE.matcher(parameterTypeName).matches()) {
          throw new IllegalArgumentException("Syntax error in parameter type name: "
              + parameterTypeName);
        }
        this.parameterTypeNames[i] = parameterTypeNames[i];
      }
    } else {
      this.parameterTypeNames = NO_PARAMETER_TYPES_SPECIFIED;
    }

  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MethodDescriptor other = (MethodDescriptor) obj;
    if (!methodName.equals(other.methodName)) {
      return false;
    }
    if (!Arrays.equals(parameterTypeNames, other.parameterTypeNames)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the name of the method.
   *
   * @return The name of the method.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Returns a clone of the parameter type array. Modification of the returned array will not cause
   * the modification of the array that is stored by the {@link MethodDescriptor} instance.
   *
   * @return The clone of the parameterTypeNames array.
   */
  public String[] getParameterTypeNames() {
    if (parameterTypeNames == NO_PARAMETER_TYPES_SPECIFIED) {
      return noParameterTypeNamesSpecified();
    }
    return parameterTypeNames.clone();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + methodName.hashCode();
    result = prime * result + Arrays.hashCode(parameterTypeNames);
    return result;
  }

  /**
   * Searching the method in the provided class and in super classes. For more information see
   * {@link #matches(Method)} and
   * {@link MethodUtil#locateMethodByPreference(Class, boolean, MethodDescriptor...)}.
   *
   * @param clazz
   *          The class where the search starts.
   * @param privateMethodAccepted
   *          Whether to search private methods in the class that is passed in the clazz parameter.
   * @return The matched method or null if no such method could be found.
   * @throws NullPointerException
   *           if the clazz parameter is null.
   */
  public Method locate(final Class<?> clazz, final boolean privateMethodAccepted) {
    Objects.requireNonNull(clazz, "Clazz must not be null");

    Class<?> currentClass = clazz;

    while (currentClass != null) {
      Method[] declaredMethods = currentClass.getDeclaredMethods();
      for (Method method : declaredMethods) {
        if (matches(method)
            && MethodUtil.isMethodAccessibleFromClass(clazz, method, privateMethodAccepted)) {
          return method;
        }
      }
      currentClass = currentClass.getSuperclass();
    }
    return null;
  }

  /**
   * Matches the provided method with this {@link MethodDescriptor}. A method matches to this
   * descriptor if the method name is the same and if the parameters of the method match the
   * {@link #getParameterTypeNames()} array. If {@link #getParameterTypeNames()} returns null, the
   * method will return true if the passed method has the same name as the one that is specified in
   * this {@link MethodDescriptor}.
   *
   * @param method
   *          The method object.
   * @return True if the specified method matches with this {@link MethodDescriptor}.
   */
  public boolean matches(final Method method) {
    if (!this.methodName.equals(method.getName())) {
      return false;
    }
    if (parameterTypeNames == NO_PARAMETER_TYPES_SPECIFIED) {
      return true;
    }

    Class<?>[] parameterTypes = method.getParameterTypes();

    if (parameterTypes.length != parameterTypeNames.length) {
      return false;
    }

    boolean matches = true;
    for (int i = 0; i < parameterTypeNames.length && matches; i++) {
      String parameterTypeName = parameterTypeNames[i];
      Class<?> parameterType = parameterTypes[i];
      matches = (parameterTypeName.equals(parameterType.getCanonicalName())
          || (!parameterType.isPrimitive() && parameterTypeName.equals(parameterType
          .getSimpleName())));
    }
    return matches;
  }

  /**
   * This method is separated to avoid findbugs alert
   * <code>BIT: Check for sign of bitwise operation (BIT_SIGNED_CHECK)</code>.
   */
  private String[] noParameterTypeNamesSpecified() {
    return NO_PARAMETER_TYPES_SPECIFIED;
  }

  private String[] parseParameterTypeNames(final String pParameterTypesString) {
    // Crop the brackets and trim
    String parameterTypesString = pParameterTypesString.substring(1,
        pParameterTypesString.length() - 1);
    parameterTypesString = parameterTypesString.trim();

    if ("".equals(parameterTypesString)) {
      return new String[0];
    }

    String[] parameterTypeStringArray = parameterTypesString.split(",");
    for (int i = 0; i < parameterTypeStringArray.length; i++) {
      parameterTypeStringArray[i] =
          removeWhiteSpacesFromParameterTypeString(parameterTypeStringArray[i]);

    }

    return parameterTypeStringArray;
  }

  /**
   * Returns the String representation of this {@link MethodDescriptor}. The string representation
   * contains the name of the method and if available, the list of parameter types enclosed with
   * brackets. The {@link String} that is generated by this method can be passed to the
   * {@link #MethodDescriptor(String)} constructor. Parameter type names are listed as they are
   * stored in the {@link MethodDescriptor}, the type name might be a
   * {@link Class#getCanonicalName()} or a {@link Class#getSimpleName()}. Examples: "myMethod",
   * myMethod(java.lang.String), myMethod(String).
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(methodName);
    sb.append("(");
    if (parameterTypeNames != NO_PARAMETER_TYPES_SPECIFIED) {
      for (int i = 0, n = parameterTypeNames.length; i < n; i++) {
        String parameterTypeName = parameterTypeNames[i];
        sb.append(parameterTypeName);
        if (i < n - 1) {
          sb.append(", ");
        }
      }
    }
    sb.append(")");
    return sb.toString();
  }

}
