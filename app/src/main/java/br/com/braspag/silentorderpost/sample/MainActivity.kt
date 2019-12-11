package br.com.braspag.silentorderpost.sample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import br.com.braspag.silentorderpost.R
import br.com.braspag.silentorderpost.sample.data.remote.TokenRemoteDatasource

import kotlinx.android.synthetic.main.activity_main.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            scope.launch {
                TokenRemoteDatasource.getAccessToken(
                    "538c04ae-f377-4da8-803b-67c812616387",
                    onSuccess = { s ->
                        Snackbar.make(
                            view,
                            "Sucesso! AccessToken = $s",
                            Snackbar.LENGTH_LONG
                        ).show()
                    },
                    onError = { e ->
                        Snackbar.make(
                            view,
                            "Error! code = $e",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                )

            }
        }
    }

}
