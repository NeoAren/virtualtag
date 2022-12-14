package com.virtualtag.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.virtualtag.app.db.Card
import com.virtualtag.app.db.CardDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : ViewModel() {
    private val cardDB = CardDB.get(application)

    fun getAllCards(): LiveData<List<Card>> = cardDB.cardDao().getAllCards()

    fun getCardById(id: Int): LiveData<Card> = cardDB.cardDao().getCardById(id)

    fun addCard(card: Card) {
        CoroutineScope(Dispatchers.IO).launch {
            cardDB.cardDao().addCard(card)
        }
    }

    fun updateCard(id: Int, name: String, color: String) {
        CoroutineScope(Dispatchers.IO).launch {
            cardDB.cardDao().updateCard(id, name, color)
        }
    }

    fun deleteCard(card: Card) {
        CoroutineScope(Dispatchers.IO).launch {
            cardDB.cardDao().deleteCard(card)
        }
    }
}
