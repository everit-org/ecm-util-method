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
package org.everit.osgi.ecm.util.method.test;

import java.lang.reflect.Method;

import org.everit.osgi.ecm.util.method.MethodDescriptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing method signature creation.
 */
public class CreateMethodSignatureTest {

  @Test
  public void testArrayParam() {
    Class<String> stringType = String.class;
    Method method;
    try {
      method = stringType.getMethod("getChars", int.class, int.class, char[].class, int.class);
      MethodDescriptor methodDescriptor = new MethodDescriptor(method);
      String methodSignature = methodDescriptor.toString();
      Assert.assertEquals("getChars(int, int, char[], int)", methodSignature);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testMultipleSimpleParameters() {
    Class<String> stringType = String.class;
    Method method;
    try {
      method = stringType.getMethod("regionMatches", boolean.class, int.class, String.class,
          int.class,
          int.class);
      MethodDescriptor methodDescriptor = new MethodDescriptor(method);
      String methodSignature = methodDescriptor.toString();
      Assert.assertEquals("regionMatches(boolean, int, java.lang.String, int, int)",
          methodSignature);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNoParameter() {
    Class<String> stringType = String.class;
    Method method;
    try {
      method = stringType.getMethod("trim");
      MethodDescriptor methodDescriptor = new MethodDescriptor(method);
      String methodSignature = methodDescriptor.toString();
      Assert.assertEquals("trim()", methodSignature);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
