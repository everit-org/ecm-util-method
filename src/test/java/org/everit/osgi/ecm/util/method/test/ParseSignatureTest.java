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

import org.everit.osgi.ecm.util.method.MethodDescriptor;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertArrayEquals(new String[] { String.class.getName() }, methodDescrtiptor.getParameterTypeNames());
    }
}
