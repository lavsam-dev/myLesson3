package lavsam.gb.libs.mylesson1

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import lavsam.gb.libs.mylesson1.MainPresenter.Companion.RX_LABEL
import java.util.concurrent.TimeUnit


class MainPresenter(private val view: IMainView) {

    companion object {
        const val RX_LABEL = "RX_LABEL"
    }

    private val model = CountersModel()

    fun counterClick(type: CounterType) {
        val behaviorCounter = BehaviorCounter(model)
        val c1 = Counting(view, behaviorCounter)
//        behaviorSubject.onNext(CounterType.COUNTER_OF_DAYS)

//        val nextValue = when (type) {
//            CounterType.COUNTER_OF_DAYS -> model.next(0)
//            CounterType.COUNTER_OF_YEARS -> model.next(1)
//            CounterType.COUNTER_OF_PAYLOAD -> model.next(2)
//        }
//        view.setButtonText(type, nextValue.toString())
    }

//    init {
//        behaviorSubject.onNext(CounterType.COUNTER_OF_YEARS)
//        behaviorSubject.subscribe {
//            when (it) {
//                CounterType.COUNTER_OF_DAYS -> TODO()
//                CounterType.COUNTER_OF_YEARS -> TODO()
//                CounterType.COUNTER_OF_PAYLOAD -> TODO()
//            }
//        }
//    }
}

fun BehaviorCounter(model: CountersModel): @NonNull Observable<Int> {

    val list = listOf<CounterType>(CounterType.COUNTER_OF_YEARS, CounterType.COUNTER_OF_YEARS,
        CounterType.COUNTER_OF_DAYS, CounterType.COUNTER_OF_PAYLOAD)
    val behaviorInterval = BehaviorSubject.interval(2, TimeUnit.SECONDS)
    val behaviorJust = BehaviorSubject.fromIterable(list)
    return BehaviorSubject.zip(behaviorInterval, behaviorJust, {
            _, t2 ->
        when (t2) {
            CounterType.COUNTER_OF_DAYS -> model.next(0)
            CounterType.COUNTER_OF_YEARS -> model.next(1)
            CounterType.COUNTER_OF_PAYLOAD -> model.next(2)
        }
    })
}

class Counting(view: IMainView, val observable: Observable<Int>) {
    private val observer = object : Observer<Int> {
        override fun onSubscribe(d: Disposable?) {
            Log.i(RX_LABEL, "onSubscribe")
        }

        override fun onNext(t: Int?) {
            Log.i(RX_LABEL, "onNext ${t.toString()}")
            Handler(Looper.getMainLooper()).post(Runnable {
                view.setButtonText(CounterType.COUNTER_OF_PAYLOAD, t.toString())
            })
        }

        override fun onError(e: Throwable?) {
            Log.i(RX_LABEL, "onError ${e?.message}")
        }

        override fun onComplete() {
            Log.i(RX_LABEL, "onComplete")
        }
    }

    init {
        observable.subscribe(observer)
    }
}

enum class CounterType {
    COUNTER_OF_DAYS,
    COUNTER_OF_YEARS,
    COUNTER_OF_PAYLOAD
}