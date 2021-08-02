package br.unifor.cct.gestaofinanceira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.cct.gestaofinanceira.R
import br.unifor.cct.gestaofinanceira.database.UserDAO
import br.unifor.cct.gestaofinanceira.model.User
import br.unifor.cct.gestaofinanceira.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDAO: UserDAO

    private lateinit var mRegisterFirstname: EditText
    private lateinit var mRegisterLastName: EditText
    private lateinit var mRegisterEmail: EditText
    private lateinit var mRegisterPassword: EditText
    private lateinit var mRegisterPasswordConfirm: EditText
    private lateinit var mRegisterSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userDAO = DatabaseUtil.getInstance(this).getUserDao()

        mRegisterFirstname = findViewById(R.id.register_editText_firstname)
        mRegisterLastName = findViewById(R.id.register_editText_lastname)
        mRegisterEmail = findViewById(R.id.register_editText_email)
        mRegisterPassword = findViewById(R.id.register_editText_password)
        mRegisterPasswordConfirm = findViewById(R.id.register_editText_password_confirm)

        mRegisterSignup = findViewById(R.id.register_button_signup)
        mRegisterSignup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.register_button_signup -> {
                var isFormRightFilled = true
                if (mRegisterFirstname.text.isEmpty()) {
                    mRegisterFirstname.error = getString(R.string.form_error_required)
                    isFormRightFilled = false
                }

                if (mRegisterLastName.text.isEmpty()) {
                    mRegisterLastName.error = getString(R.string.form_error_required)
                    isFormRightFilled = false
                }

                if (mRegisterEmail.text.isEmpty()) {
                    mRegisterEmail.error = getString(R.string.form_error_required)
                    isFormRightFilled = false
                }

                if (mRegisterPassword.text.isEmpty()) {
                    mRegisterPassword.error = getString(R.string.form_error_required)
                    isFormRightFilled = false
                }

                if (mRegisterPasswordConfirm.text.isEmpty()) {
                    mRegisterPasswordConfirm.error = getString(R.string.form_error_required)
                    isFormRightFilled = false
                }

                if (isFormRightFilled) {
                    if (mRegisterPassword.text.toString() != mRegisterPasswordConfirm.text.toString()) {
                        mRegisterPasswordConfirm.error = getString(R.string.form_error_password_confirm)
                        return
                    }

                    GlobalScope.launch {

                        val handler = Handler(Looper.getMainLooper())

                        val user: User = userDAO.findByEmail(mRegisterEmail.text.toString())

                        if (user === null) {

                            val newUser = User(
                                firstName = mRegisterFirstname.text.toString(),
                                lastName = mRegisterLastName.text.toString(),
                                email = mRegisterEmail.text.toString(),
                                password = BCrypt
                                    .withDefaults()
                                    .hashToString(12, mRegisterPassword.text.toString().toCharArray())
                            )

                            userDAO.insert(newUser)

                            handler.post {
                                Toast.makeText(
                                    applicationContext,
                                    "Usuário Cadastrado!",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }
                        } else {
                            handler.post {
                                mRegisterEmail.error = "Já existe um usuário com este e-mail"
                            }
                        }
                    }
                }
            }
        }
    }
}