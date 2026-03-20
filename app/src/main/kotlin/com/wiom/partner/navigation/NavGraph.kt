package com.wiom.partner.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.content.Intent
import android.net.Uri
import com.wiom.partner.screens.*
import com.wiom.partner.state.FlowViewModel
import com.wiom.partner.theme.*

@Composable
fun NavGraph(navController: NavHostController, vm: FlowViewModel = viewModel()) {
    fun go(route: String) {
        vm.onNavigate(route)
        navController.navigate(route) { launchSingleTop = true }
    }
    fun goReplace(route: String) {
        vm.onNavigate(route)
        navController.navigate(route) { popUpTo(route) { inclusive = true }; launchSingleTop = true }
    }

    val flowOrder = listOf(
        "s1","s2","s3","s4","s5","s6","s8","s8c","s12","s13","s14",
        "s11","s15","s16","s17","s18","s19","s20","s21","s22",
        "s23","s24","s25","s26","s27","s28","s29","s30","s31","s32","s33"
    )
    fun nextAfter(current: String): String {
        val idx = flowOrder.indexOf(current)
        return if (idx >= 0 && idx < flowOrder.size - 1) flowOrder[idx + 1] else "s1"
    }

    // Shared exit function for all close buttons
    val exitClose = { vm.showExitDialog = true }

    // WiFi connect dialog — between S16 and S17
    if (vm.showWifiDialog) {
        com.wiom.partner.components.WifiConnectDialog(
            onDismiss = { vm.showWifiDialog = false },
            onConnect = { vm.showWifiDialog = false; go("s17") }
        )
    }

    // Global exit dialog — shown on any screen when X is tapped
    if (vm.showExitDialog) {
        com.wiom.partner.components.ExitDialog(
            onDismiss = { vm.showExitDialog = false },
            onConfirmExit = { vm.showExitDialog = false; vm.exitSetup(); goReplace("s1") }
        )
    }

    NavHost(navController = navController, startDestination = Screen.S1.route) {
        // S1: Task list — startFresh clears resume screen
        composable(Screen.S1.route) {
            S01TaskList(onTaskTap = { vm.startFresh(); go("s2") })
        }

        // S2: Task detail — "काम पूरा करें" resumes from saved screen or starts fresh
        composable(Screen.S2.route) {
            S02TaskDetail(
                onStartFlow = { go(vm.getResumeTarget()) },
                onBack = { navController.popBackStack() }
            )
        }

        // S3: PayG acceptance
        composable(Screen.S3.route) {
            S03PaygAcceptance(
                onNext = { go("s4") },
                onPaygAccepted = { vm.acceptPayg() }
            )
        }

        // S4: Transfer info + dialogs
        composable(Screen.S4.route) {
            S04TransferInfo(
                onNext = { go("s5") },
                onCall = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919876543210"))
                    navController.context.startActivity(intent)
                },
                onClose = exitClose,
                onExitToHome = { vm.exitSetup(); goReplace("s1") }
            )
        }

        // S5: Selfie camera (captures to ViewModel)
        composable(Screen.S5.route) {
            S05SelfieCamera(
                vm = vm,
                onCapture = { go("s6") },
                onClose = exitClose
            )
        }

        // S6: Selfie review (displays from ViewModel)
        composable(Screen.S6.route) {
            S06SelfieReview(
                vm = vm,
                onNext = { go("s8") },
                onRetake = { vm.selfieData = null; navController.popBackStack() },
                onClose = exitClose
            )
        }

        // S17: Placement check (timer + checkbox)
        composable(Screen.S17.route) {
            S17PlacementCheck(
                onNext = { go(nextAfter("s17")) },
                onClose = exitClose
            )
        }

        // S18: Netbox camera (captures to vm.netboxPhotoData)
        composable(Screen.S18.route) {
            S18NetboxCamera(
                vm = vm,
                onCapture = { go(nextAfter("s18")) },
                onClose = exitClose
            )
        }

        // S19: Netbox photo review (displays vm.netboxPhotoData)
        composable(Screen.S19.route) {
            S19NetboxReview(
                vm = vm,
                onNext = { go(nextAfter("s19")) },
                onRetake = { vm.netboxPhotoData = null; navController.popBackStack() },
                onClose = exitClose
            )
        }

        // S20: 3-pin info (timer + checkbox)
        composable(Screen.S20.route) {
            S20ThreepinInfo(
                onNext = { go(nextAfter("s20")) },
                onClose = exitClose
            )
        }

        // S21: 3-pin camera (captures to vm.threepinPhotoData)
        composable(Screen.S21.route) {
            S21ThreepinCamera(
                vm = vm,
                onCapture = { go(nextAfter("s21")) },
                onClose = exitClose
            )
        }

        // S22: 3-pin photo review (displays vm.threepinPhotoData)
        composable(Screen.S22.route) {
            S22ThreepinReview(
                vm = vm,
                onNext = { go(nextAfter("s22")) },
                onRetake = { vm.threepinPhotoData = null; navController.popBackStack() },
                onClose = exitClose
            )
        }

        // S23: Wiring check (audio + timer + checkbox)
        composable(Screen.S23.route) {
            S23WiringCheck(
                onNext = { go(nextAfter("s23")) },
                onClose = exitClose
            )
        }

        // S24: Wiring camera (captures to vm.wiringPhotoData)
        composable(Screen.S24.route) {
            S24WiringCamera(
                vm = vm,
                onCapture = { go(nextAfter("s24")) },
                onClose = exitClose
            )
        }

        // S25: Wiring photo review (displays vm.wiringPhotoData)
        composable(Screen.S25.route) {
            S25WiringReview(
                vm = vm,
                onNext = { go(nextAfter("s25")) },
                onRetake = { vm.wiringPhotoData = null; navController.popBackStack() },
                onClose = exitClose
            )
        }

        // S26: Loading (auto-transition)
        composable(Screen.S26.route) {
            S26Loading(
                onNext = { goReplace(nextAfter("s26")) },
                onClose = exitClose
            )
        }

        // S27: Success
        composable(Screen.S27.route) {
            S27Success(
                onNext = { go(nextAfter("s27")) },
                onClose = exitClose
            )
        }

        // S28: Optical power
        composable(Screen.S28.route) {
            S28OpticalPower(
                onNext = { go(nextAfter("s28")) },
                onClose = exitClose
            )
        }

        // S29: Speed test
        composable(Screen.S29.route) {
            S29SpeedTest(
                onNext = { go(nextAfter("s29")) },
                onClose = exitClose
            )
        }

        // S30: Recharge info (no header, no close)
        composable(Screen.S30.route) {
            S30RechargeInfo(
                onNext = { go(nextAfter("s30")) }
            )
        }

        // S31: Happy code rating
        composable(Screen.S31.route) {
            S31HappyCodeRating(
                onNext = { go(nextAfter("s31")) },
                onClose = exitClose
            )
        }

        // S32: OTP entry
        composable(Screen.S32.route) {
            S32OtpEntry(
                onCodeComplete = { go(nextAfter("s32")) },
                onClose = exitClose
            )
        }

        // S33: Lottery
        composable(Screen.S33.route) {
            S33Lottery(
                onReset = { goReplace("s1") }
            )
        }

        // S7: Skip — Aadhaar camera is handled inside S08
        composable(Screen.S7.route) {
            // Redirect to S8 which handles camera internally
            LaunchedEffect(Unit) { go("s8") }
        }

        // S8: Aadhaar capture (3-state flow with internal camera)
        composable(Screen.S8.route) {
            S08AadhaarCapture(
                vm = vm,
                onNext = { go("s8c") },
                onClose = exitClose
            )
        }

        // S8C: PayG system info audio
        composable(Screen.S8C.route) {
            S08cPaygSystemInfo(onNext = { go("s12") })
        }

        // S12: Payment checklist
        composable(Screen.S12.route) {
            S12PaymentChecklist(onComplete = { go("s13") })
        }

        // S13: Power-up timer
        composable(Screen.S13.route) {
            S13PowerUpTimer(onNext = { go("s14") })
        }

        // S14: Switch-on confirm
        composable(Screen.S14.route) {
            S14SwitchOnConfirm(
                onNext = { go("s11") },
                onClose = exitClose
            )
        }

        // S11: Customer details
        composable(Screen.S11.route) {
            S11CustomerDetails(
                vm = vm,
                onNext = { go("s15") },
                onClose = exitClose
            )
        }

        // S15: ISP recharge audio
        composable(Screen.S15.route) {
            S15IspRechargeAudio(
                onNext = { go("s16") },
                onOpenPortal = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://crmh.myion.in/Login.aspx"))
                    navController.context.startActivity(intent)
                },
                onOpenWhatsapp = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/?text=ISP%20details%20needed"))
                    navController.context.startActivity(intent)
                }
            )
        }

        // S16: ISP form
        composable(Screen.S16.route) {
            S16IspForm(
                onNext = { vm.showWifiDialog = true },
                onClose = exitClose
            )
        }

        // S9/S10: Simple loading/transition screens
        composable(Screen.S9.route) {
            PlaceholderScreen(screenId = "S9", step = vm.getStepCount("s9"),
                onNext = { go(nextAfter("s9")) }, onBack = { navController.popBackStack() })
        }
        composable(Screen.S10.route) {
            PlaceholderScreen(screenId = "S10", step = vm.getStepCount("s10"),
                onNext = { go(nextAfter("s10")) }, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun PlaceholderScreen(screenId: String, step: Int, onNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = screenId,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Brand
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Step $step / 8",
            fontSize = 16.sp,
            color = Sec
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .background(Brand, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .clickable { onNext() },
            contentAlignment = Alignment.Center
        ) {
            Text("Next →", color = Bg, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "← Back",
            color = Hint,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onBack() }
        )
    }
}
