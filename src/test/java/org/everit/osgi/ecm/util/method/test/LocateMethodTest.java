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
import org.everit.osgi.ecm.util.method.test.classes.C;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing method locate mechanism.
 */
public class LocateMethodTest {

  @Test
  public void testArrayParam() {
    MethodDescriptor descriptor = new MethodDescriptor("protectedB(int[], java.lang.String[])");
    Method method = descriptor.locate(C.class, true);
    Assert.assertEquals("protectedB", method.getName());
  }

  @Test
  public void testDifferentParams() {
    MethodDescriptor descriptor = new MethodDescriptor("protectedDifferentParams");
    Method method = descriptor.locate(C.class, false);
    Assert.assertEquals("C", method.getDeclaringClass().getSimpleName());

    descriptor = new MethodDescriptor("protectedDifferentParams(int)");
    method = descriptor.locate(C.class, false);
    Assert.assertEquals("C", method.getDeclaringClass().getSimpleName());

    descriptor = new MethodDescriptor("protectedDifferentParams()");
    method = descriptor.locate(C.class, false);
    Assert.assertEquals("A", method.getDeclaringClass().getSimpleName());
  }

  @Test
  public void testNoAvailableMethod() {
    MethodDescriptor descriptor = new MethodDescriptor("testNoMetohd");
    Method method = descriptor.locate(C.class, true);
    Assert.assertNull(method);
  }

  @Test
  public void testPrivateBFromC() {
    MethodDescriptor descriptor = new MethodDescriptor("privateB");
    Method method = descriptor.locate(C.class, true);
    Assert.assertNull(method);
  }

  @Test
  public void testPrivateC() {
    MethodDescriptor descriptor = new MethodDescriptor("privateC");
    Method method = descriptor.locate(C.class, true);
    Assert.assertEquals("privateC", method.getName());

    method = descriptor.locate(C.class, false);
    Assert.assertNull(method);
  }

  @Test
  public void testProtectedBFromC() {
    MethodDescriptor descriptor = new MethodDescriptor("privateC");
    Method method = descriptor.locate(C.class, true);
    Assert.assertEquals("privateC", method.getName());
  }

  @Test
  public void testSimpleClassName() {
    MethodDescriptor descriptor = new MethodDescriptor("protectedB(int[], String[])");
    Method method = descriptor.locate(C.class, true);
    Assert.assertEquals("protectedB", method.getName());
  }
}
