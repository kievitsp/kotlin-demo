package examples

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertFailsWith
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
class CoroutineTests : CoroutineScope {

    private suspend fun longRunningTask(): String {
        println("longRunningTask - starting")
        try {
            delay(1_000)
        } catch (e: CancellationException) {
            println("longRunningTask - Cancelled!")
            throw e
        }
        println("longRunningTask - Returning")
        return "hello world"
    }

    @Test
    fun `coroutines are async`() {
        var result: String? = null

        runBlocking {
            launch { result = longRunningTask() }
            println("Launched!")
        }

        println("Received result")
        assert(result == "hello world")
    }
    @Test
    fun `coroutines are async - demo power assist`() {
        var result: String? = null

        runBlocking {
            launch { result = longRunningTask() }
            println("Launched!")
        }

        println("Received result")
        val error = assertFailsWith<AssertionError> {
            assert(result != "hello world")
        }

        println(error.message)
    }

    @Test
    fun `lightweight coroutines`() = runBlocking {
        val counter = AtomicInteger(0)
        val totalJobs = 100_000

        val jobs = List(totalJobs) {
            launch {
                delay(1)
                counter.incrementAndGet()
            }
        }

        jobs.joinAll()

        assert(counter.get() == totalJobs)
    }

    @Test
    fun `heavy threads`() {
        val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        val totalJobs = 100_000
        val counter = AtomicInteger(0)

        val jobs = List(totalJobs) {
            pool.submit {
                Thread.sleep(1)
                counter.incrementAndGet()
            }
        }

        jobs.forEach { it.get() }

        assert(counter.get() == totalJobs)
    }

    @Test
    fun `coroutine cancellation`() {
        val ex = assertFailsWith<RuntimeException> {
            runBlocking {
                launch {
                    longRunningTask()
                }

                launch {
                    delay(500)
                    println("Cancelling")
                    throw RuntimeException("Error")
                }
            }
        }

        assert(ex.message == "Error")

        ex.printStackTrace()
    }

    @Test
    fun `coroutine cancellation - with supervisor`() {
        runBlocking {
            val job = launch(SupervisorJob()) {
                launch {
                    longRunningTask()
                }

                launch {
                    delay(500)
                    throw RuntimeException("Error")
                }
                println("Returning..")
            }

            println("Waiting...")
            job.join()
            println("Complete")
        }
    }

    @Test
    fun `coroutines with timeout`() {
        assertFailsWith<TimeoutCancellationException> {
            runBlocking {
                withTimeout(500) {
                    longRunningTask()
                }
            }
        }
    }

    @Test
    fun `running tasks asynchronously`() = runBlocking {
        suspend fun launchExpensiveComputation(time: Long): Long {
            delay(time)
            return time / 10
        }

        val (value, time) = measureTimedValue {
            val a = async { launchExpensiveComputation(100) }
            val b = async { launchExpensiveComputation(200) }
            val c = async { launchExpensiveComputation(300) }
            val d = async { launchExpensiveComputation(400) }

            a.await() + b.await() + c.await() + d.await()
        }

        assert(value == 100L)
        println("value: $value, time taken: ${time.inWholeMilliseconds}ms")
    }

    override val coroutineContext: CoroutineContext =
        newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "tests") + SupervisorJob()

    @After
    fun tearDown() {
        cancel()
    }
}