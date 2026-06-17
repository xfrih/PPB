package com.example.gamingspace

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gamingspace.ui.screen.*
import com.example.gamingspace.ui.viewmodel.GamingViewModel

@Composable
fun GamingNavHost(
    navController: NavHostController,
    viewModel: GamingViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(onFinish = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddMember = { navController.navigate("add_member") },
                onMemberClick = { memberId ->
                    navController.navigate("member_card/$memberId")
                }
            )
        }

        composable("add_member") {
            AddMemberScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "member_card/{memberId}",
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            MemberCardScreen(
                memberId = memberId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onTransaction = { navController.navigate("transaction") },
                onReward = { navController.navigate("reward") }
            )
        }

        composable("transaction") {
            TransactionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("reward") {
            RewardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}