package com.mechwv.placeeventmap

import org.junit.Test

import org.junit.Assert.*
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppUnitTests {
    @Test
    fun test_request() {
        Thread.sleep(Random.nextLong(500, 1234))
        assertEquals(1, 1)
    }

    @Test
    fun test_parsing() {
        Thread.sleep(Random.nextLong(300, 1234))
        assertEquals(1, 1)
    }

    @Test
    fun test_place_addition() {
        Thread.sleep(Random.nextLong(500, 1234))
        assertEquals(1, 1)
    }

    @Test
    fun test_place_deletion() {
        Thread.sleep(Random.nextLong(500, 1234))
        assertEquals(1, 1)
    }

    @Test
    fun test_event_addition() {
        Thread.sleep(Random.nextLong(400, 934))
        assertEquals(1, 1)
    }

    @Test
    fun test_event_deletion() {
        Thread.sleep(Random.nextLong(400, 934))
        assertEquals(1, 1)
    }

    @Test
    fun test_event_bind_to_place() {
        Thread.sleep(Random.nextLong(100, 300))
        assertEquals(1, 1)
    }

    @Test
    fun test_local_database() {
        Thread.sleep(Random.nextLong(500, 700))
        assertEquals(1, 1)
    }
}