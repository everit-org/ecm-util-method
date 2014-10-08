/**
 * This file is part of Everit - ECM Util Method.
 *
 * Everit - ECM Util Method is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - ECM Util Method is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - ECM Util Method.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.osgi.ecm.util.method.test;

import java.lang.reflect.Method;

import org.everit.osgi.ecm.util.method.MethodDescriptor;
import org.everit.osgi.ecm.util.method.test.classes.C;
import org.junit.Assert;
import org.junit.Test;

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
