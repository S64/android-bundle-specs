package jp.s64.android.bundlespecs.tests;

/*
 * Copyright (C) 2018 Shuma Yoshioka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.support.test.runner.AndroidJUnit4;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BundleTest {

    @Test
    public void testBinderTypeCheck() {
        final String key = "key";
        final IBinder orgBinder = new Binder();

        Bundle org = new Bundle();
        {
            org.putBinder(key, orgBinder);
        }

        Bundle copied = org.deepCopy();

        assertEquals(copied.getBinder(key), org.getBinder(key));
        assertEquals( // 🚨
                System.identityHashCode(copied.getBinder(key)),
                System.identityHashCode(org.getBinder(key))
        );
        assertTrue(copied.get(key) instanceof IBinder);
        assertNull(copied.getString(key));
    }

    @Test
    public void testBundleTypeCheck() {
        final String key = "key";
        final Bundle orgValue = new Bundle();

        Bundle org = new Bundle();
        {
            org.putBundle(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertNotEquals(org.getBundle(key), copied.getBundle(key));// 🚨
        assertNotEquals(
                System.identityHashCode(copied.getBundle(key)),
                System.identityHashCode(org.getBundle(key))
        );
        assertTrue(copied.get(key) instanceof Bundle);
        assertTrue(copied.get(key) instanceof Parcelable); // 🚨
        assertNotNull(copied.getParcelable(key)); // 🚨
        assertNull(copied.getString(key));
    }

    @Test
    public void testParcelableTypeCheck_1() {
        final String key = "key";
        final Parcelable orgValue = new Bundle();

        Bundle org = new Bundle();
        {
            org.putParcelable(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertNotEquals(org.getParcelable(key), copied.getParcelable(key)); // 🚨
        assertNotEquals(
                System.identityHashCode(copied.getParcelable(key)),
                System.identityHashCode(org.getParcelable(key))
        );
        assertTrue(copied.get(key) instanceof Parcelable);
        assertTrue(copied.get(key) instanceof Bundle);
        assertNotNull(copied.getBundle(key)); // 🚨
        assertNull(copied.getString(key));
    }

    @Test
    public void testParcelableTypeCheck_2() {
        final String key = "key";
        final Parcelable orgValue = new Intent();

        Bundle org = new Bundle();
        {
            org.putParcelable(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(copied.get(key) instanceof Intent);
        assertFalse(copied.get(key) instanceof Bundle);
        assertNull(copied.getBundle(key));
    }

    @Test
    public void testByteTypeCheck() {
        final String key = "key";
        final byte orgValue = Byte.MAX_VALUE;

        Bundle org = new Bundle();
        {
            org.putByte(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getByte(key), copied.getByte(key));
        assertTrue(copied.get(key) instanceof Byte);
        assertNull(copied.getString(key));
    }

    @Test
    public void testByteArrayTypeCheck() {
        final String key = "key";
        final byte[] orgValue = new byte[] {Byte.MIN_VALUE, Byte.MAX_VALUE};

        Bundle org = new Bundle();
        {
            org.putByteArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getByteArray(key), org.getByteArray(key));
        assertTrue(copied.get(key) instanceof byte[]);
        assertNull(copied.getString(key));
    }

    @Test
    public void testCharTypeCheck() {
        final String key = "key";
        final char orgValue = 'X';

        Bundle org = new Bundle();
        {
            org.getChar(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getChar(key), copied.getChar(key));
        assertNull(copied.get(key)); // 🚨
        // == assertFalse(copied.get(key) instanceof Character); // 🚨
        assertNull(copied.getString(key));
    }

    @Test
    public void testCharArrayTypeCheck() {
        final String key = "key";
        final char[] orgValue = new char[] {0, 1};

        Bundle org = new Bundle();
        {
            org.putCharArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(org.getCharArray(key), copied.getCharArray(key)));
        assertTrue(copied.get(key) instanceof char[]);
        assertNull(copied.getString(key));
    }

    @Test
    public void testCharSequenceTypeCheck() {
        final String key = "key";
        final CharSequence orgValue = "myCsValue";

        Bundle org = new Bundle();
        {
            org.putCharSequence(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getCharSequence(key), copied.getCharSequence(key));
        assertTrue(copied.get(key) instanceof CharSequence);
        assertTrue(copied.get(key) instanceof String); // 🚨
        assertEquals(org.getString(key), copied.getString(key));// 🚨
    }

    @Test
    public void testCharSequenceArrayTypeCheck() {
        final String key = "key";
        final CharSequence[] orgValue = new CharSequence[] { "one", "two" };

        Bundle org = new Bundle();
        {
            org.putCharSequenceArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getCharSequenceArray(key), copied.getCharSequenceArray(key)
        ));
        assertTrue(copied.get(key) instanceof CharSequence[]);
        assertNull(copied.getStringArray(key));
        assertNull(copied.getCharSequenceArrayList(key));
    }

    @Test
    public void testCharSequenceArrayListTypeCheck() {
        final String key = "key";
        final ArrayList<CharSequence> orgValue = Lists.<CharSequence>newArrayList("one", "two");

        Bundle org = new Bundle();
        {
            org.putCharSequenceArrayList(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(org.getCharSequenceArrayList(key).containsAll(copied.getCharSequenceArrayList(key)));
        assertTrue(copied.getCharSequenceArrayList(key).containsAll(org.getCharSequenceArrayList(key)));

        assertTrue(orgValue.getClass().isInstance(copied.get(key)));
        assertNotNull(copied.getStringArrayList(key)); // 🚨
        assertNotNull(copied.getCharSequenceArrayList(key)); // 🚨
        assertNotNull(copied.getIntegerArrayList(key)); // 🚨
        assertNull(copied.getCharSequenceArray(key));
        assertNull(copied.getStringArray(key));
    }

    @Test
    public void testFloatTypeCheck() {
        final String key = "key";
        final float orgValue = Float.MAX_VALUE;

        Bundle org = new Bundle();
        {
            org.putFloat(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getFloat(key), copied.getFloat(key), 0f);
        assertTrue(copied.get(key) instanceof Float);
        assertFalse(copied.get(key) instanceof Double);
        assertFalse(copied.get(key) instanceof Integer);
        assertFalse(copied.get(key) instanceof Short);
        assertFalse(copied.get(key) instanceof Long);
        assertNull(copied.getString(key));
    }

    @Test
    public void testFloatArrayTypeCheck() {
        final String key = "key";
        final float[] orgValue = new float[] { Float.MAX_VALUE, Float.MIN_VALUE };

        Bundle org = new Bundle();
        {
            org.putFloatArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getFloatArray(key), copied.getFloatArray(key)
        ));
        assertTrue(copied.get(key) instanceof float[]);
        assertFalse(copied.get(key) instanceof int[]);
        assertFalse(copied.get(key) instanceof double[]);
        assertNull(copied.getIntegerArrayList(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testIntegerArrayListTypeCheck() {
        final String key = "key";
        final ArrayList<Integer> orgValue = Lists.newArrayList(Integer.MAX_VALUE, Integer.MIN_VALUE);

        Bundle org = new Bundle();
        {
            org.putIntegerArrayList(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(org.getIntegerArrayList(key).containsAll(copied.getIntegerArrayList(key)));
        assertTrue(copied.getIntegerArrayList(key).containsAll(org.getIntegerArrayList(key)));

        assertTrue(orgValue.getClass().isInstance(copied.get(key)));
        assertNotNull(copied.getCharSequenceArrayList(key)); // 🚨
        assertNotNull(copied.getStringArrayList(key)); // 🚨
        assertNull(copied.getIntArray(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testParcelableArrayTypeCheck() {
        final String key = "key";
        final Parcelable[] orgValue = new Parcelable[] { new Bundle(), new Intent() };

        Bundle org = new Bundle();
        {
            org.putParcelableArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getParcelableArray(key), copied.getParcelableArray(key)
        ));
        assertTrue(copied.get(key) instanceof Parcelable[]);
        assertNull(copied.getParcelableArrayList(key));
        assertNull(copied.getSparseParcelableArray(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testParcelableArrayListTypeCheck() {
        final String key = "key";
        final ArrayList<Parcelable> orgValue = Lists.<Parcelable>newArrayList(new Bundle(), new Intent());

        Bundle org = new Bundle();
        {
            org.putParcelableArrayList(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertFalse(org.getParcelableArrayList(key).containsAll(copied.getParcelableArrayList(key))); // 🚨
        assertFalse(copied.getParcelableArrayList(key).containsAll(org.getParcelableArrayList(key))); // 🚨

        assertTrue(orgValue.getClass().isInstance(copied.get(key)));
        assertFalse(copied.get(key) instanceof Parcelable[]);
        assertNull(copied.getParcelableArray(key));
        assertNull(copied.getSparseParcelableArray(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testSerializableTypeCheck() {
        final String key = "key";
        final Serializable orgValue = "myStringValue";

        Bundle org = new Bundle();
        {
            org.putSerializable(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getSerializable(key), copied.getSerializable(key));
        assertEquals( // 🚨
                System.identityHashCode(copied.getSerializable(key)),
                System.identityHashCode(org.getSerializable(key))
        );
        assertTrue(org.get(key) instanceof Serializable);
        assertTrue(org.get(key) instanceof String); // 🚨
        assertEquals(copied.getString(key), orgValue); // 🚨
        assertEquals(copied.getCharSequence(key), orgValue); // 🚨
        assertNull(copied.getIntArray(key));
    }

    @Test
    public void testShortTypeCheck() {
        final String key = "key";
        final short orgValue = Short.MAX_VALUE;

        Bundle org = new Bundle();
        {
            org.putShort(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getShort(key), copied.getShort(key));
        assertTrue(copied.get(key) instanceof Short);
        assertFalse(copied.get(key) instanceof Integer);
        assertFalse(copied.get(key) instanceof Long);
        assertFalse(copied.get(key) instanceof Float);
        assertFalse(copied.get(key) instanceof Double);
        assertNull(copied.getString(key));
    }

    @Test
    public void testShortArrayTypeCheck() {
        final String key = "key";
        final short[] orgValue = new short[] { Short.MAX_VALUE, Short.MIN_VALUE };

        Bundle org = new Bundle();
        {
            org.putShortArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getShortArray(key), copied.getShortArray(key)
        ));
        assertTrue(copied.get(key) instanceof short[]);
        assertFalse(copied.get(key) instanceof int[]);
        assertFalse(copied.get(key) instanceof long[]);
        assertFalse(copied.get(key) instanceof float[]);
        assertFalse(copied.get(key) instanceof double[]);
        assertNull(copied.getIntegerArrayList(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testSizeTypeCheck() {
        final String key = "key";
        final Size orgValue = new Size(48, 48);

        Bundle org = new Bundle();
        {
            org.putSize(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getSize(key), copied.getSize(key));
        assertTrue(copied.get(key) instanceof Size);
        assertNull(copied.getSizeF(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testSizeFTypeCheck() {
        final String key = "key";
        final SizeF orgValue = new SizeF(Float.MAX_VALUE, Float.MAX_VALUE);

        Bundle org = new Bundle();
        {
            org.putSizeF(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getSizeF(key), copied.getSizeF(key));
        assertTrue(copied.get(key) instanceof SizeF);
        assertNull(copied.getSize(key));
        assertNull(copied.getString(key));
    }

    @Test
    public void testSparseParcelableArrayTypeCheck() {
        final String key = "key";
        final SparseArray<Parcelable> orgValue = new SparseArray<>();
        {
            orgValue.append(0, new Bundle());
            orgValue.append(1, new Intent());
        }

        Bundle org = new Bundle();
        {
            org.putSparseParcelableArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        // TODO: (not) equality check
        assertTrue(orgValue.getClass().isInstance(copied.get(key)));
        assertNull(copied.getParcelableArray(key));
        assertNull(copied.getParcelableArrayList(key));
    }

    @Test
    public void testStringArrayListTypeCheck() {
        final String key = "key";
        final ArrayList<String> orgValue = Lists.newArrayList("one", "two");

        Bundle org = new Bundle();
        {
            org.putStringArrayList(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(org.getStringArrayList(key).containsAll(copied.getStringArrayList(key)));
        assertTrue(copied.getStringArrayList(key).containsAll(org.getStringArrayList(key)));

        assertTrue(orgValue.getClass().isInstance(copied.get(key)));
        assertTrue(copied.get(key) instanceof Serializable); // 🚨
        assertNull(copied.getStringArray(key));
        assertNotNull(copied.getSerializable(key)); // 🚨
        assertNotNull(copied.getIntegerArrayList(key)); // 🚨
    }

    @Test
    public void testBooleanTypeCheck() {
        final String key = "key";
        final boolean orgValue = true;

        Bundle org = new Bundle();
        {
            org.putBoolean(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getBoolean(key), copied.getBoolean(key));
        assertTrue(copied.get(key) instanceof Boolean);
        assertFalse(copied.get(key) instanceof Integer);
        assertTrue(copied.get(key) instanceof Serializable); // 🚨
        assertNull(copied.getString(key));
    }

    @Test
    public void testBooleanArrayTypeCheck() {
        final String key = "key";
        final boolean[] orgValue = new boolean[] {true, false, true};

        Bundle org = new Bundle();
        {
            org.putBooleanArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getBooleanArray(key), copied.getBooleanArray(key)
        ));
        assertTrue(copied.get(key) instanceof boolean[]);
        assertTrue(copied.get(key) instanceof Serializable); // 🚨
        assertNull(copied.getString(key));
    }

    @Test
    public void testDoubleTypeCheck() {
        final String key = "key";
        final double orgValue = Double.MAX_VALUE;

        Bundle org = new Bundle();
        {
            org.putDouble(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getDouble(key), copied.getDouble(key), 0);
        assertTrue(copied.get(key) instanceof Double);
        assertFalse(copied.get(key) instanceof Float);
        assertFalse(copied.get(key) instanceof Integer);
        assertFalse(copied.get(key) instanceof Short);
        assertFalse(copied.get(key) instanceof Long);
        assertNull(copied.getString(key));
    }

    @Test
    public void testDoubleArrayTypeCheck() {
        final String key = "key";
        final double[] orgValue = new double[] { Double.MAX_VALUE, Double.MIN_VALUE };

        Bundle org = new Bundle();
        {
            org.putDoubleArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getDoubleArray(key), copied.getDoubleArray(key)
        ));
        assertTrue(copied.get(key) instanceof double[]);
        assertFalse(copied.get(key) instanceof float[]);
        assertFalse(copied.get(key) instanceof int[]);
        assertFalse(copied.get(key) instanceof short[]);
        assertFalse(copied.get(key) instanceof long[]);
        assertNull(copied.getString(key));
        assertNull(copied.getIntArray(key));
    }

    @Test
    public void testIntArrayTypeCheck() {
        final String key = "key";
        final int[] orgValue = new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE };

        Bundle org = new Bundle();
        {
            org.putIntArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getIntArray(key), copied.getIntArray(key)
        ));
        assertTrue(copied.get(key) instanceof int[]);
        assertFalse(copied.get(key) instanceof float[]);
        assertFalse(copied.get(key) instanceof double[]);
        assertFalse(copied.get(key) instanceof short[]);
        assertFalse(copied.get(key) instanceof long[]);
        assertNull(copied.getString(key));
        assertNull(copied.getIntegerArrayList(key));
        assertNull(copied.getStringArray(key));
        assertNull(copied.getShortArray(key));
    }

    @Test
    public void testLongTypeCheck() {
        final String key = "key";
        final long orgValue = Long.MAX_VALUE;

        Bundle org = new Bundle();
        {
            org.putLong(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertEquals(org.getLong(key), copied.getLong(key));
        assertTrue(copied.get(key) instanceof Long);
        assertFalse(copied.get(key) instanceof Float);
        assertFalse(copied.get(key) instanceof Integer);
        assertFalse(copied.get(key) instanceof Short);
        assertFalse(copied.get(key) instanceof Double);
        assertNull(copied.getString(key));
    }

    @Test
    public void testLongArrayTypeCheck() {
        final String key = "key";
        final long[] orgValue = new long[] { Long.MIN_VALUE, Long.MAX_VALUE };

        Bundle org = new Bundle();
        {
            org.putLongArray(key, orgValue);
        }

        Bundle copied = org.deepCopy();

        assertTrue(Arrays.equals(
                org.getLongArray(key), copied.getLongArray(key)
        ));
        assertTrue(copied.get(key) instanceof long[]);
        assertFalse(copied.get(key) instanceof float[]);
        assertFalse(copied.get(key) instanceof double[]);
        assertFalse(copied.get(key) instanceof short[]);
        assertFalse(copied.get(key) instanceof int[]);
        assertNull(copied.getString(key));
        assertNull(copied.getIntegerArrayList(key));
        assertNull(copied.getStringArray(key));
        assertNull(copied.getIntArray(key));
    }

}

