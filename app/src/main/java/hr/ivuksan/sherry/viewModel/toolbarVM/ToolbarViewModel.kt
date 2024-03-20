package hr.ivuksan.sherry.viewModel.toolbarVM

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ivuksan.sherry.R
import java.util.Calendar

class ToolbarViewModel(application: Application): ViewModel() {

    var welcomeMessage = MutableLiveData<String>()
    private val appContext: Context = application.applicationContext

    fun getWelcomeMessage(){
        val currentTime = Calendar.getInstance()
        val messageId = when (currentTime.get(Calendar.HOUR_OF_DAY)) {
            in 6..11 -> R.string.morning
            in 12..16 -> R.string.day
            else -> R.string.evening
        }
        val message = appContext.getString(messageId)
        welcomeMessage.value = message
    }
}