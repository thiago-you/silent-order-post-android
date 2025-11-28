package br.com.braspag.silentorderpost.sample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.braspag.silentorder.SilentOrderPost
import br.com.braspag.silentorder.enums.Environment
import br.com.braspag.silentorder.model.ApiResult
import br.com.braspag.silentorder.model.SilentOrderResult
import br.com.braspag.silentorder.model.ValidationResults
import br.com.braspag.silentorderpost.sample.data.remote.TokenRemoteDatasource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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

        enableEdgeToEdge()
        
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar)?.also { toolbar ->
            setSupportActionBar(toolbar)
        }

        findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            scope.launch {
                TokenRemoteDatasource.getAccessToken(
                    merchantId = "YOUR-MERCHANT-ID",
                    onSuccess = { accessToken -> silentOrder(accessToken) },
                    onError = { e -> snackbar("Error! code = $e") },
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
            onResult = Result()
        )
    }

    private fun snackbar(message: String) {
        findViewById<FloatingActionButton>(R.id.fab)?.also { fab ->
            Snackbar.make(fab.rootView, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private inner class Result : SilentOrderResult {
        override fun onValidation(errors: List<ValidationResults>) {
            snackbar(">>>>>> Validation issue(s) - $errors")
        }

        override fun onResult(result: ApiResult) {
            if (result.data != null) {
                snackbar(">>>>>> Success - payment token: ${result.data?.paymentToken}")
            } else {
                snackbar(">>>>>> Error - $result")
            }
        }
    }
}
