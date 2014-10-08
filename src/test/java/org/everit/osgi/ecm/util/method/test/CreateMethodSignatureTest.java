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
import org.junit.Assert;
import org.junit.Test;

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
            method = stringType.getMethod("regionMatches", boolean.class, int.class, String.class, int.class,
                    int.class);
            MethodDescriptor methodDescriptor = new MethodDescriptor(method);
            String methodSignature = methodDescriptor.toString();
            Assert.assertEquals("regionMatches(boolean, int, java.lang.String, int, int)", methodSignature);
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
