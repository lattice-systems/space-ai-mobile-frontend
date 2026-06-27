package org.utl.idgs901.space_ai_mobile.presentation.navigation.renderer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteRendererWrapper @Inject constructor(
    val renderer: RouteRenderer
) : ViewModel()
