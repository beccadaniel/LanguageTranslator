package com.folashade.languagetranslator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

class TranslatorViewModel : ViewModel() {
    private val _translatedText = MutableLiveData<String>()
    val translatedText: LiveData<String>
        get() = _translatedText
    private val _targetLanguage = MutableLiveData<String>()
    val targetLanguage: LiveData<String>
        get() = _targetLanguage
    private val _eventTextChanged = MutableLiveData<Boolean>()
    val eventTextChanged: LiveData<Boolean>
        get() = _eventTextChanged

    init {
        _targetLanguage.value = "German"
    }

    fun  translateText(sourceText: String, destinationLanguage: String) {
        val translator = buildTranslator(destinationLanguage)
        translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    // (Set a flag, unhide the translation UI, etc.)
                    translator.translate(sourceText)
                            .addOnSuccessListener { translatedValue ->
                                // Translation successful.
                                onResultReady(translatedValue)
                            }
                            .addOnFailureListener { exception ->
                                // Error.
                                // ...
                                onResultReady(exception.message.toString())
                            }
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    // ...
                    _translatedText.value = exception.message.toString()
                }
    }

    fun onTextChangeComplete() {
        _eventTextChanged.value = false
    }

    private fun onResultReady(translatedValue: String) {
        _translatedText.value = translatedValue
        _eventTextChanged.value = true
    }

    private fun buildTranslator(destinationLanguage: String): FirebaseTranslator {
        val options = when(destinationLanguage.toLowerCase()) {
            "spanish" ->FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.ES)
                    .build()
            "french" -> FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.FR)
                    .build()
            else -> FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.AF)
                    .build()
        }

        return FirebaseNaturalLanguage.getInstance().getTranslator(options)
    }
}
