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

import org.everit.osgi.ecm.util.method.MethodDescriptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing method signature parse.
 */
public class ParseSignatureTest {

  @Test
  public void testEmptyParameters() throws ClassNotFoundException {
    MethodDescriptor methodDescrtiptor = new MethodDescriptor("myMethod()");
    Assert.assertEquals("myMethod", methodDescrtiptor.getMethodName());
    Assert.assertEquals(0, methodDescrtiptor.getParameterTypeNames().length);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSignature() throws ClassNotFoundException {
    new MethodDescriptor("test(.)");
  }

  @Test
  public void testMethodNameOnly() throws ClassNotFoundException {
    MethodDescriptor methodDescrtiptor = new MethodDescriptor("myMethod");
    Assert.assertEquals("myMethod", methodDescrtiptor.getMethodName());
    Assert.assertNull(methodDescrtiptor.getParameterTypeNames());
  }

  @Test
  public void testMultipleParams() throws ClassNotFoundException {
    MethodDescriptor methodDescrtiptor = new MethodDescriptor("myMethod(boolean, byte)");
    String[] parameterTypes = methodDescrtiptor.getParameterTypeNames();
    Assert.assertArrayEquals(new String[] { "boolean", "byte" }, parameterTypes);
  }

  @Test
  public void testOneParameter() throws ClassNotFoundException {
    MethodDescriptor methodDescrtiptor = new MethodDescriptor("myMethod(java.lang.String)");
    Assert.assertEquals("myMethod", methodDescrtiptor.getMethodName());
    Assert.assertArrayEquals(new String[] { String.class.getName() },
        methodDescrtiptor.getParameterTypeNames());
  }
}
