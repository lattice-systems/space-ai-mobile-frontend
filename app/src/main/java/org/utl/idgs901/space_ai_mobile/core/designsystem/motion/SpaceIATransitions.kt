package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

object SpaceIATransitions {

    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        fadeIn(
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        ) + slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            ),
            initialOffset = { it / 10 }
        ) + scaleIn(
            initialScale = 0.95f,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        )
    }

    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        fadeOut(
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        ) + slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            ),
            targetOffset = { it / 10 }
        ) + scaleOut(
            targetScale = 0.95f,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        )
    }

    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        fadeIn(
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        ) + slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            ),
            initialOffset = { it / 10 }
        )
    }

    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        fadeOut(
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            )
        ) + slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Normal,
                easing = SpaceIAMotion.Easing.Decelerate
            ),
            targetOffset = { it / 10 }
        )
    }
}
