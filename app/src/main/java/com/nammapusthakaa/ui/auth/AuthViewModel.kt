package com.nammapusthakaa.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammapusthakaa.data.local.entity.UserEntity
import com.nammapusthakaa.data.local.entity.StudentEntity
import com.nammapusthakaa.data.repository.AuthRepository
import com.nammapusthakaa.data.repository.StudentRepository
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentRole: String = "",
    val currentUserId: Int = 0,
    val currentUserName: String = "",
    val error: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    var selectedRole by mutableStateOf("Student")

    var studentName by mutableStateOf("")
    var studentRegisterNo by mutableStateOf("")
    var studentClass by mutableStateOf("")

    var teacherEmail by mutableStateOf("")
    var teacherPassword by mutableStateOf("")

    var signupName by mutableStateOf("")
    var signupEmail by mutableStateOf("")
    var signupPassword by mutableStateOf("")
    var signupConfirmPassword by mutableStateOf("")
    var signupPhone by mutableStateOf("")

    fun login() {
        when (selectedRole) {
            "Student" -> loginStudent()
            "Admin" -> loginAdmin()
            else -> loginTeacher()
        }
    }

    private fun loginStudent() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                if (studentName.isBlank()) {
                    state = state.copy(isLoading = false, error = "Please enter your name")
                    return@launch
                }
                if (studentRegisterNo.isBlank()) {
                    state = state.copy(isLoading = false, error = "Please enter register number")
                    return@launch
                }
                if (studentClass.isBlank()) {
                    state = state.copy(isLoading = false, error = "Please enter your class")
                    return@launch
                }

                var student = studentRepository.getStudentByRegisterNumber(studentRegisterNo)
                if (student == null) {
                    val id = studentRepository.insertStudent(
                        StudentEntity(
                            name = studentName,
                            registerNumber = studentRegisterNo,
                            className = studentClass
                        )
                    )
                    student = StudentEntity(
                        id = id.toInt(),
                        name = studentName,
                        registerNumber = studentRegisterNo,
                        className = studentClass
                    )
                }

                state = state.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentRole = "Student",
                    currentUserId = student.id,
                    currentUserName = student.name
                )
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Login failed: ${e.message}")
            }
        }
    }

    private fun loginTeacher() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val user = authRepository.login(teacherEmail, teacherPassword)
                if (user != null) {
                    state = state.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentRole = user.role,
                        currentUserId = user.id,
                        currentUserName = user.name
                    )
                } else {
                    state = state.copy(isLoading = false, error = "Invalid email or password")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Login failed: ${e.message}")
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                if (signupPassword != signupConfirmPassword) {
                    state = state.copy(isLoading = false, error = "Passwords do not match")
                    return@launch
                }
                val existing = authRepository.getUserByEmail(signupEmail)
                if (existing != null) {
                    state = state.copy(isLoading = false, error = "Email already registered")
                    return@launch
                }
                val user = UserEntity(
                    name = signupName,
                    email = signupEmail,
                    password = signupPassword,
                    role = if (selectedRole == "Admin") "Admin" else "Teacher",
                    phone = signupPhone
                )
                authRepository.register(user)
                state = state.copy(isLoading = false, error = null)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Registration failed: ${e.message}")
            }
        }
    }

    private fun loginAdmin() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val user = authRepository.login(teacherEmail, teacherPassword)
                if (user != null && user.role == "Admin") {
                    state = state.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentRole = "Admin",
                        currentUserId = user.id,
                        currentUserName = user.name
                    )
                } else if (user != null) {
                    state = state.copy(isLoading = false, error = "This account is not an admin")
                } else {
                    state = state.copy(isLoading = false, error = "Invalid email or password")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        state = AuthState()
        studentName = ""
        studentRegisterNo = ""
        studentClass = ""
        teacherEmail = ""
        teacherPassword = ""
    }

    fun clearError() {
        state = state.copy(error = null, isLoggedIn = false)
    }
}
