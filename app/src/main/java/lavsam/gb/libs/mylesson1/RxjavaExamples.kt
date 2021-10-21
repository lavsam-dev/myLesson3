package lavsam.gb.libs.mylesson1

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class RxjavaExamples {
}

fun main() {
//    obsTestMap()
//    obsJust()
//    println("-------------")
//    obsIterable()
//    println("-------------")
//    obsInterval()
//    println("-------------")
//    obsRange()
//    obsCreate()
//    val observableTake = obsTake()
//    val consumer1 = Consumer("consumer1", observableTake)
//    val consumer2 = Consumer("consumer2", observableTake)
    val observableTake = obsCombine()
    val consumer1 = Consumer("consumer1", observableTake)
//    val consumer2 = Consumer("consumer2", observableTake)
    Thread.sleep(20000)
}

fun obsCombine(): @NonNull Observable<String> {
    val list = listOf("Hello", "from", "smr", "?", "!")
    val interval = Observable.interval(2, TimeUnit.SECONDS)
        .map {"сейчас время $it"}
    val observable = Observable.fromIterable(list)
    return Observable.combineLatest(interval, observable, { t1, t2 -> // zip
        "number = $t1 text = $t2"
    })
        .map { it.toString() }
}

fun obsTake(): @NonNull Observable<String> {
    return Observable.interval(1, TimeUnit.SECONDS)
        .take(20)
        .skip(3)
        .map { it.toString() }
}

class Consumer(private val name: String, val observable: Observable<String>) {
    private val observer = object : Observer<String> {
        override fun onSubscribe(d: Disposable?) {
            println("$name onSubscribe")
        }

        override fun onNext(t: String?) {
            println("${Thread.currentThread()} $name onNext $t")
        }

        override fun onError(e: Throwable?) {
            println("$name onError ${e?.message}")
        }

        override fun onComplete() {
            println("$name onComplete")
        }
    }

    init {
        observable.subscribe(observer)
    }
}

fun obsJust() {
    Observable.just(listOf("Hello", "from", "smr")).subscribe { println("element = $it") }
}

fun obsIterable() {
    Observable.fromIterable(
        listOf("Hello", "from", "smr", null, "!").map { Test1(it) })
        .subscribe(
            { println("element = $it") },
            { println(it.message) }
        )
}

fun obsInterval() {
    Observable.interval(1, TimeUnit.SECONDS)
        .subscribe(
            { println("element = $it") },
            { println(it.message) }
        )
    Thread.sleep(10000)
}

fun obsRange() {
    Observable.range(1, 100)
        .subscribe(
            { println("element = $it") },
            { println(it.message) }
        )
    Thread.sleep(10000)
}

fun obsCreate() {
    Observable.create<String> { emitter ->
        var now = 0
        val max = 27
        while (now < max) {
            emitter.onNext((now++).toString())
            Thread.sleep(1000)
        }
        emitter.onComplete()
    }
        .subscribe(
            { println("element = $it") },
            { println(it.message) },
            { println("onComplete") }
        )
    Thread.sleep(10000)
}

fun obsTestMap() {
//    val test = listOf(1, 2, 3)
    val test = 1..6
    val test2 = test.map { it + 3 }
    val test3 = test.map { Test1((it + 4).toString()) }
    println(test)
    println(test2)
    println(test3)
}

data class Test1(
    val something: String? = null
)