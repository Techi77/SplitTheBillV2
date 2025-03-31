package com.stb.appbase

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : UiEvent> Flow<T>.CollectAsEventWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEvent: suspend EventScope.(T) -> Unit
) {
    val context = LocalContext.current
    val onEventRemember by rememberUpdatedState(onEvent)
    val contextRemember by rememberUpdatedState(context)
    val scope = remember {
        object : EventScope {
            override val context: Context
                get() = contextRemember

            override fun showToast(message: String) {
                Toast.makeText(contextRemember, message, Toast.LENGTH_LONG).show()
            }

            override fun stringResource(resId: Int, vararg args: Any) = contextRemember.getString(resId, *args)
        }
    }
    LaunchedEffect(this, minActiveState, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(minActiveState) {
            collect { event ->
                scope.onEventRemember(event)
            }
        }
    }
}

@Immutable
interface EventScope {
    val context: Context

    @Stable
    fun showToast(message: String)

    @Stable
    fun stringResource(resId: Int, vararg args: Any): String
}