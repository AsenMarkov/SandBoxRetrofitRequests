package com.example.sandboxretrofitrequests.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sandboxretrofitrequests.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    private val args: LoginFragmentArgs by navArgs()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)

        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailTextChangedListener()
        passwordTextChangedListener()

        sharedPreferences.edit().putBoolean("logged", args.loggedState).apply()

        binding.btnLogin.setOnClickListener {
            submitLogin()
        }
    }

    private fun submitLogin() {
        if (binding.emailContainer.helperText == null && binding.passwordContainer.helperText == null) {
            logIn()
        } else {
            var message = ""
            if (binding.emailContainer.helperText != null) {
                message += "\n\nE-mail: " + binding.emailContainer.helperText
            }
            if (binding.passwordContainer.helperText != null) {
                message += "\n\nPassword: " + binding.passwordContainer.helperText
            }
            AlertDialog.Builder(requireContext()).setTitle("Invalid Input").setMessage(message)
                .setPositiveButton("Okay") { _, _ ->
                    // do this
                }.show()
        }
    }

    private fun logIn() {
        val action = LoginFragmentDirections.actionLoginFragmentToUnsplashPhotosFragment()
        findNavController().navigate(action)
        sharedPreferences.edit().putBoolean("logged", true).apply()
    }

    private fun emailTextChangedListener() {
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.emailContainer.helperText = validEmail()
                enableLoginBtn()
            }
        })
    }

    private fun passwordTextChangedListener() {
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.passwordContainer.helperText = validPassword()
                enableLoginBtn()
            }
        })
    }

    private fun validEmail(): String? {
        val emailText = binding.editTextEmail.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid E-mail Address"
        }
        return null
    }

    private fun validPassword(): String? {
        val passwordPattern = ("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[!@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$")
        if (!binding.editTextPassword.text.toString().matches(passwordPattern.toRegex())) {
            return "U have entered Invalid Password"
        }
        return null
    }

    fun enableLoginBtn() {
        if (binding.emailContainer.helperText == null && binding.passwordContainer.helperText == null) {
            binding.btnLogin.isEnabled = true
            binding.btnLogin.isClickable = true
        }
    }
}
