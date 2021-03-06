/*
 * Copyright 2014 Red Hat, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.test.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxFactory;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.shareddata.SharedData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SharedDataTest {

  private Vertx vertx;
  private SharedData sharedData;

  @Before
  public void setUp() {
    vertx = VertxFactory.newVertx();
    sharedData = vertx.sharedData();
  }

  @After
  public void teardown() {
    vertx.close();
  }

  @Test
  public void testMap() throws Exception {

    Map<String, String> map = sharedData.getMap("foo");
    Map<String, String> map2 = sharedData.getMap("foo");
    assertTrue(map == map2);
    Map<String, String> map3 = sharedData.getMap("bar");
    assertFalse(map3 == map2);
    assertTrue(sharedData.removeMap("foo"));
    Map<String, String> map4 = sharedData.getMap("foo");
    assertFalse(map4 == map3);
  }

  @Test
  public void testMapTypes() throws Exception {

    Map<String, Object> map = sharedData.getMap("foo");

    String key = "key";

    double d = new Random().nextDouble();
    map.put(key, d);
    assertEquals(d, map.get(key));

    float f = new Random().nextFloat();
    map.put(key, f);
    assertEquals(f, map.get(key));

    byte b = (byte)new Random().nextInt();
    map.put(key, b);
    assertEquals(b, map.get(key));

    short s = (short)new Random().nextInt();
    map.put(key, s);
    assertEquals(s, map.get(key));

    int i = new Random().nextInt();
    map.put(key, i);
    assertEquals(i, map.get(key));

    long l = new Random().nextLong();
    map.put(key, l);
    assertEquals(l, map.get(key));

    map.put(key, true);
    assertTrue((Boolean)map.get(key));

    map.put(key, false);
    assertFalse((Boolean) map.get(key));

    char c = (char)new Random().nextLong();
    map.put(key, c);
    assertEquals(c, map.get(key));

    Buffer buff = TestUtils.randomBuffer(100);
    map.put(key, buff);
    Buffer got1 = (Buffer)map.get(key);
    assertTrue(got1 != buff); // Make sure it's copied
    assertEquals(buff, map.get(key));
    Buffer got2 = (Buffer)map.get(key);
    assertTrue(got1 != got2); // Should be copied each time
    assertTrue(got2 != buff);
    assertEquals(buff, map.get(key));


    byte[] bytes = TestUtils.randomByteArray(100);
    map.put(key, bytes);
    byte[] bgot1 = (byte[]) map.get(key);
    assertTrue(bgot1 != bytes);
    assertTrue(TestUtils.byteArraysEqual(bytes, bgot1));
    byte[] bgot2 = (byte[]) map.get(key);
    assertTrue(bgot2 != bytes);
    assertTrue(bgot1 != bgot2);
    assertTrue(TestUtils.byteArraysEqual(bytes, bgot2));

    try {
      map.put(key, new SomeOtherClass());
      fail("Should throw exception");
    } catch (IllegalArgumentException e) {
      //OK
    }
  }

  @Test
  public void testSetTypes() throws Exception {

    Set<Object> set = sharedData.getSet("foo");

    double d = new Random().nextDouble();
    set.add(d);
    assertEquals(d, set.iterator().next());
    set.clear();

    float f = new Random().nextFloat();
    set.add(f);
    assertEquals(f, set.iterator().next());
    set.clear();

    byte b = (byte)new Random().nextInt();
    set.add(b);
    assertEquals(b, set.iterator().next());
    set.clear();

    short s = (short)new Random().nextInt();
    set.add(s);
    assertEquals(s, set.iterator().next());
    set.clear();

    int i = new Random().nextInt();
    set.add(i);
    assertEquals(i, set.iterator().next());
    set.clear();

    long l = new Random().nextLong();
    set.add(l);
    assertEquals(l, set.iterator().next());
    set.clear();

    set.add(true);
    assertTrue((Boolean)set.iterator().next());
    set.clear();

    set.add(false);
    assertFalse((Boolean) set.iterator().next());
    set.clear();

    char c = (char)new Random().nextLong();
    set.add(c);
    assertEquals(c, set.iterator().next());
    set.clear();

    Buffer buff = TestUtils.randomBuffer(100);
    set.add(buff);
    Buffer got1 = (Buffer)set.iterator().next();
    assertTrue(got1 != buff); // Make sure it's copied
    assertEquals(buff, set.iterator().next());
    Buffer got2 = (Buffer)set.iterator().next();
    assertTrue(got1 != got2); // Should be copied on each get
    assertTrue(got2 != buff);
    assertEquals(buff, set.iterator().next());
    set.clear();


    byte[] bytes = TestUtils.randomByteArray(100);
    set.add(bytes);
    byte[] bgot1 = (byte[]) set.iterator().next();
    assertTrue(bgot1 != bytes);
    assertTrue(TestUtils.byteArraysEqual(bytes, bgot1));
    byte[] bgot2 = (byte[]) set.iterator().next();
    assertTrue(bgot2 != bytes);
    assertTrue(bgot1 != bgot2);
    assertTrue(TestUtils.byteArraysEqual(bytes, bgot2));
    set.clear();

    try {
      set.add(new SomeOtherClass());
      fail("Should throw exception");
    } catch (IllegalArgumentException e) {
      //OK
    }
  }


  @Test
  public void testSet() throws Exception {

    Set<String> set = sharedData.getSet("foo");
    Set<String> set2 = sharedData.getSet("foo");
    assert (set == set2);
    Set<String> set3 = sharedData.getSet("bar");
    assert (set3 != set2);
    assert (sharedData.removeSet("foo"));
    Set<String> set4 = sharedData.getSet("foo");
    assert (set4 != set3);
  }

  class SomeOtherClass {
  }
}
