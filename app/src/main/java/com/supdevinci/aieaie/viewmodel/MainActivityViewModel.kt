package com.supdevinci.aieaie.viewmodel

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    // Fonctions métier à mettre ici
    fun checkResponseCode(codeToCheck: Int, responseCode: Int)
    : Boolean {
        return codeToCheck == responseCode
    }

    fun concatText(responseMsg: String) : String {
        return "La réponse de l'ia est $responseMsg"
    }
}