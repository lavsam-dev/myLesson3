package lavsam.gb.libs.mylesson1

import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainPresenter(private val view: IMainView) {

    val behaviorSubject = BehaviorSubject.create<CounterType>()

    private val model = CountersModel()

    fun counterClick(type: CounterType) {

        behaviorSubject.onNext(CounterType.COUNTER_OF_DAYS)

        val nextValue = when (type) {
            CounterType.COUNTER_OF_DAYS -> model.next(0)
            CounterType.COUNTER_OF_YEARS -> model.next(1)
            CounterType.COUNTER_OF_PAYLOAD -> model.next(2)
        }
        view.setButtonText(type, nextValue.toString())
    }

    init {
        behaviorSubject.onNext(CounterType.COUNTER_OF_YEARS)
        behaviorSubject.subscribe {
            when (it) {
                CounterType.COUNTER_OF_DAYS -> TODO()
                CounterType.COUNTER_OF_YEARS -> TODO()
                CounterType.COUNTER_OF_PAYLOAD -> TODO()
            }
        }
    }
}

enum class CounterType {
    COUNTER_OF_DAYS,
    COUNTER_OF_YEARS,
    COUNTER_OF_PAYLOAD
}