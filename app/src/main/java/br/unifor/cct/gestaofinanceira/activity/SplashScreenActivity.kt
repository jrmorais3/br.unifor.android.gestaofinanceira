package br.unifor.cct.gestaofinanceira.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.unifor.cct.gestaofinanceira.R
import br.unifor.cct.gestaofinanceira.util.DatabaseUtil

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        DatabaseUtil.getInstance(this)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val it = Intent(this, LoginActivity::class.java)
            startActivity(it)
            finish()
        }, 3000)
    }
}