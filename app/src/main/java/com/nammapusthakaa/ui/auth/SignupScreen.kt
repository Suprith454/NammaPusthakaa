package com.nammapusthakaa.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.AppTextField
import com.nammapusthakaa.ui.common.LoadingOverlay
import com.nammapusthakaa.ui.common.PrimaryButton

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    val state = authViewModel.state

    LaunchedEffect(state.error) {
        if (state.error == null && !state.isLoading && authViewModel.signupName.isNotEmpty()) {
            onSignupSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (authViewModel.selectedRole == "Admin") "Admin Registration" else "Teacher Registration",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppTextField(
                value = authViewModel.signupName,
                onValueChange = { authViewModel.signupName = it },
                label = "Full Name",
                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = authViewModel.signupEmail,
                onValueChange = { authViewModel.signupEmail = it },
                label = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = authViewModel.signupPhone,
                onValueChange = { authViewModel.signupPhone = it },
                label = "Phone (Optional)",
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = authViewModel.signupPassword,
                onValueChange = { authViewModel.signupPassword = it },
                label = "Password",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = authViewModel.signupConfirmPassword,
                onValueChange = { authViewModel.signupConfirmPassword = it },
                label = "Confirm Password",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Register",
                onClick = { authViewModel.signup() },
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Sign In",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onNavigateToLogin)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { authViewModel.clearError() },
                title = { Text("Error") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = { authViewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }

        LoadingOverlay(state.isLoading)
    }
}
