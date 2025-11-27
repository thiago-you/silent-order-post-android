package br.com.braspag.silentorderpost.sample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.braspag.silentorder.SilentOrderPost
import br.com.braspag.silentorder.model.Environment
import br.com.braspag.silentorderpost.sample.data.remote.TokenRemoteDatasource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)

    private val sdk = SilentOrderPost(Environment.SANDBOX)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar)?.also { toolbar ->
            setSupportActionBar(toolbar)
        }

        findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            scope.launch {
                TokenRemoteDatasource.getAccessToken(
                    "YOUR-MERCHANT-ID",
                    onSuccess = { s -> silentOrder(s) },
                    onError = { e -> snackbar("Error! code = $e") }
                )
            }
        }
    }

    private fun silentOrder(accessToken: String) {
        snackbar("Sucesso! AccessToken = $accessToken")

        sdk.accessToken = accessToken
        sdk.enableBinQuery = true

        sdk.sendCardData(
            cardHolderName = "Joselito Barbacena",
            cardNumber = "4000000000001091",
            cardExpirationDate = "10/2029",
            cardCvv = "621",
            onError = { snackbar(">>>>>> Error - $it") },
            onSuccess = { snackbar(">>>>>> Success - payment token: ${it.paymentToken}") },
            onValidation = { snackbar(">>>>>> Validation issue(s) - $it") }
        )
    }

    private fun snackbar(message: String) {
        findViewById<FloatingActionButton>(R.id.fab)?.also { fab ->
            Snackbar.make(fab.rootView, message, Snackbar.LENGTH_LONG).show()
        }
    }
}
