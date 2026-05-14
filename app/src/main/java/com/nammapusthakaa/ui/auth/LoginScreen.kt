package com.nammapusthakaa.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.ui.common.AppTextField
import com.nammapusthakaa.ui.common.LoadingOverlay
import com.nammapusthakaa.ui.common.PrimaryButton

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: (String, Int, String) -> Unit
) {
    val state = authViewModel.state
    var showPassword by remember { mutableStateOf(false) }

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
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Namma Pustaka",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Smart Library for Rural Schools",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RoleButton(
                    label = "Student",
                    isSelected = authViewModel.selectedRole == "Student",
                    onClick = { authViewModel.selectedRole = "Student" },
                    modifier = Modifier.weight(1f)
                )
                RoleButton(
                    label = "Teacher",
                    isSelected = authViewModel.selectedRole == "Teacher",
                    onClick = { authViewModel.selectedRole = "Teacher" },
                    modifier = Modifier.weight(1f)
                )
                RoleButton(
                    label = "Admin",
                    isSelected = authViewModel.selectedRole == "Admin",
                    onClick = { authViewModel.selectedRole = "Admin" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authViewModel.selectedRole == "Student") {
                StudentLoginForm(authViewModel)
            } else {
                TeacherLoginForm(authViewModel, authViewModel.selectedRole, showPassword, { showPassword = it }, onNavigateToSignup)
            }
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { authViewModel.clearError() },
                title = { Text("Notice") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = { authViewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }

        LoadingOverlay(state.isLoading)

        if (state.isLoggedIn) {
            onLoginSuccess(state.currentRole, state.currentUserId, state.currentUserName)
        }
    }
}

@Composable
private fun StudentLoginForm(authViewModel: AuthViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTextField(
            value = authViewModel.studentName,
            onValueChange = { authViewModel.studentName = it },
            label = "Your Name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        AppTextField(
            value = authViewModel.studentRegisterNo,
            onValueChange = { authViewModel.studentRegisterNo = it },
            label = "Register Number",
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) }
        )
        AppTextField(
            value = authViewModel.studentClass,
            onValueChange = { authViewModel.studentClass = it },
            label = "Class / Section",
            leadingIcon = { Icon(Icons.Default.School, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Sign In",
            onClick = { authViewModel.login() },
            isLoading = authViewModel.state.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No password needed! Just enter your details to start reading.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TeacherLoginForm(
    authViewModel: AuthViewModel,
    selectedRole: String,
    showPassword: Boolean,
    onTogglePassword: (Boolean) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTextField(
            value = authViewModel.teacherEmail,
            onValueChange = { authViewModel.teacherEmail = it },
            label = "Email",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        AppTextField(
            value = authViewModel.teacherPassword,
            onValueChange = { authViewModel.teacherPassword = it },
            label = "Password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { onTogglePassword(!showPassword) }) {
                    Icon(
                        if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Sign In",
            onClick = { authViewModel.login() },
            isLoading = authViewModel.state.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Don't have an account?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = if (selectedRole == "Admin") "Register as Admin" else "Register as Teacher",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToSignup),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
