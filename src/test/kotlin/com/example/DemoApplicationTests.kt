package com.example

import com.example.MathUtil.Companion.dist
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


class DemoApplicationTests {

	@Test
	fun MathTests() {

		val c1 = Cell(0,0)
		val c2 = Cell(0,1)

		val out = dist(c2, c1)

        println(out)

	}

}
