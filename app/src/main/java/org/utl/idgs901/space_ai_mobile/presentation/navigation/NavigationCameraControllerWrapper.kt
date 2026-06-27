package org.utl.idgs901.space_ai_mobile.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationCameraControllerWrapper @Inject constructor(
    val controller: NavigationCameraController
) : ViewModel()
