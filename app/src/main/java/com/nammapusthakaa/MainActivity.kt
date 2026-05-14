package com.nammapusthakaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nammapusthakaa.ui.admin.AdminViewModel
import com.nammapusthakaa.ui.auth.AuthViewModel
import com.nammapusthakaa.ui.navigation.NavGraph
import com.nammapusthakaa.ui.student.StudentViewModel
import com.nammapusthakaa.ui.teacher.TeacherViewModel
import com.nammapusthakaa.ui.theme.NammaPusthakaaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NammaPusthakaaApp
        val container = app.container

        setContent {
            NammaPusthakaaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel = remember {
                        AuthViewModel(container.authRepository, container.studentRepository)
                    }
                    val studentViewModel = remember {
                        StudentViewModel(container.bookRepository, container.studentRepository, container.transactionRepository, container.reviewRepository)
                    }
                    val teacherViewModel = remember {
                        TeacherViewModel(container.bookRepository, container.transactionRepository, container.studentRepository)
                    }
                    val adminViewModel = remember {
                        AdminViewModel(container.bookRepository, container.studentRepository, container.transactionRepository, container.userRepository)
                    }

                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        studentViewModel = studentViewModel,
                        teacherViewModel = teacherViewModel,
                        adminViewModel = adminViewModel
                    )
                }
            }
        }
    }
}
