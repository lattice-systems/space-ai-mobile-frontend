package org.utl.idgs901.space_ai_mobile.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIATransitions
import org.utl.idgs901.space_ai_mobile.presentation.auth.LoginScreen
import org.utl.idgs901.space_ai_mobile.presentation.dashboard.DashboardScreen
import org.utl.idgs901.space_ai_mobile.presentation.profile.ProfileScreen
import org.utl.idgs901.space_ai_mobile.presentation.settings.SettingsScreen
import org.utl.idgs901.space_ai_mobile.presentation.settings.detail.CreditsScreen
import org.utl.idgs901.space_ai_mobile.presentation.settings.detail.PrivacyPolicyScreen
import org.utl.idgs901.space_ai_mobile.presentation.settings.detail.TermsAndConditionsScreen
import org.utl.idgs901.space_ai_mobile.presentation.splash.SplashScreen

@Composable
fun AppNavigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = SpaceIATransitions.enterTransition,
        exitTransition = SpaceIATransitions.exitTransition,
        popEnterTransition = SpaceIATransitions.popEnterTransition,
        popExitTransition = SpaceIATransitions.popExitTransition
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                windowSizeClass = windowSizeClass,
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable("dashboard") {
            DashboardScreen(
                windowSizeClass = windowSizeClass,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToPrivacy = {
                    navController.navigate("settings_privacy")
                },
                onNavigateToTerms = {
                    navController.navigate("settings_terms")
                },
                onNavigateToCredits = {
                    navController.navigate("settings_credits")
                }
            )
        }

        composable("settings_privacy") {
            PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
        }

        composable("settings_terms") {
            TermsAndConditionsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("settings_credits") {
            CreditsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
