package com.supdevinci.aieaie

import com.supdevinci.aieaie.model.IaModel
import com.supdevinci.aieaie.viewmodel.MainActivityViewModel
import org.junit.Test

internal class MainActivityTest {

    // Ceci est votre viewModel
    private var mainActivityViewModel = MainActivityViewModel()

    // ceci est votre model
    private var goodIAModel = IaModel("messageuuuh", 200)
    private var messageVide = IaModel("", 200)
    private var badIAModel = IaModel("messageuuuh", 500)

    @Test
    fun isResponseCode200() {
        // Appeler la fonction qui check le ResponseCode dans votre viewModel
        // check si isResponse est 200 renvoyer true sinon false
    }

    @Test
    fun `check si message vide`() {
        // check si les messages sont vides sur les différents models

    }

    @Test
    fun `check si message null`() {
        // check si les messages sont null sur les différents models
    }
}