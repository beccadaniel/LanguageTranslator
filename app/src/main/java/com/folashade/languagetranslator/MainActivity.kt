package com.folashade.languagetranslator

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.folashade.languagetranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TranslatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(TranslatorViewModel::class.java)
        binding.translationViewModel = viewModel

        binding.translateButton.setOnClickListener {
            viewModel.translateText(binding.sourceText.text.toString(),
                    binding.languageSpinner.selectedItem.toString())
        }
        viewModel.eventTextChanged.observe(this, Observer { textChanged ->
            if (textChanged) {
                binding.destinationTextView.visibility = View.VISIBLE
                binding.destinationTextView.text = viewModel.translatedText.value
                viewModel.onTextChangeComplete()
            }
        })
    }
}
