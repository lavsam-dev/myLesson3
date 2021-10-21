package lavsam.gb.libs.mylesson1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import lavsam.gb.libs.mylesson1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var presenter: MainPresenter

    private var currentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        presenter = MainPresenter(this)

        runOnUiThread {
            Toast.makeText(this, "runOnUiThread", Toast.LENGTH_SHORT).show() }

        val listener = View.OnClickListener {
            val type = when (it.id) {
                R.id.btn_counter1 -> CounterType.COUNTER_OF_DAYS
                R.id.btn_counter2 -> CounterType.COUNTER_OF_YEARS
                R.id.btn_counter3 -> CounterType.COUNTER_OF_PAYLOAD
                else -> throw IllegalStateException(getString(R.string.noButton))
            }
            presenter.counterClick(type)
        }

        val listenerStart = View.OnClickListener {
            presenter.counterStart()
        }

        mainBinding.btnCounter1.setOnClickListener(listener)
        mainBinding.btnCounter2.setOnClickListener(listener)
        mainBinding.btnCounter3.setOnClickListener(listener)
        mainBinding.btnStart.setOnClickListener ( listenerStart )
    }

    override fun onStart() {
        super.onStart()
        if (mainBinding.root != currentView) {
            currentView = mainBinding.root
        }
    }

    override fun onStop() {
        super.onStop()
        if (mainBinding.root == currentView) {
            currentView = null
        }
    }

    override fun setButtonText(type: CounterType, text: String) {
        when (type) {
            CounterType.COUNTER_OF_DAYS -> mainBinding.btnCounter1.text = text
            CounterType.COUNTER_OF_YEARS -> mainBinding.btnCounter2.text = text
            CounterType.COUNTER_OF_PAYLOAD -> mainBinding.btnCounter3.text = text
        }
    }
}