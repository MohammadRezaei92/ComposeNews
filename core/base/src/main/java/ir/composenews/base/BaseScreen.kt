package ir.composenews.base

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.designsystem.widget.LoadingView

@Composable
fun BaseRoute(
    baseViewModel: BaseViewModel,
    content: @Composable () -> Unit,
) {

    val (baseState, baseEffect, baseEvent)
            = useBase(viewModel = baseViewModel)

    val context = LocalContext.current
    val activity = context as? Activity

    baseEffect.collectInLaunchedEffect { effect ->
        when (effect) {
            BaseContract.BaseEffect.OnBackPressed -> {
                activity?.onBackPressed()
            }
        }
    }

    BaseScreen(
        baseState = baseState,
        content = content,
    )

}

@Composable
private fun BaseScreen(
    baseState: BaseContract.BaseState,
    content: @Composable () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = baseState,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(TRANSITION_DURATION)
                ) togetherWith fadeOut(animationSpec = tween(TRANSITION_DURATION))
            },
            label = "Animated Content"
        ) { targetState ->
            when (targetState) {
                BaseContract.BaseState.OnLoading -> {
                    LoadingView(modifier = Modifier.fillMaxSize())
                }

                BaseContract.BaseState.OnLoadingDialog -> TODO()

                is BaseContract.BaseState.OnError -> {
                    ErrorView(errorMessage = targetState.errorMessage)
                }

                BaseContract.BaseState.OnSuccess -> content()
            }
        }
    }
}

const val TRANSITION_DURATION = 500