package examples

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

val currentThread
    get() = Thread.currentThread().name

fun main() {
    runBlocking {
        val a: Deferred<Int> = async {
            println("a starts $currentThread")
            delay(100)
            println("a complete $currentThread")
            1
        }

        val b = async {
            println("b starts $currentThread")
            delay(200)
            println("b complete $currentThread")
            2
        }

        val c = async<Int> {
            println("c starts $currentThread")
            delay(300)
            println("c throws error $currentThread")
            throw RuntimeException("C did something bad..")
            println("c compete")
            3
        }

        val d = async {
            println("d starts $currentThread")
            try {
                delay(400)
            } catch (e: CancellationException) {
                println("d cancelled $currentThread")
                throw e
            }
            println("d complete $currentThread")
            4
        }
        println("x starts $currentThread")
        val time = measureTimeMillis {
            try {
                val x: Int = a.await() + b.await() + c.await() + d.await()
                println("x = $x $currentThread")
            } catch (e: RuntimeException) {
                println("x cancelled: ${e.message} $currentThread")
            }
        }

        println("Completed in $time ms")
    }

    // Thread.sleep(1000)
}