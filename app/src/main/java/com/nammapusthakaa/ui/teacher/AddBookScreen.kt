package com.nammapusthakaa.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nammapusthakaa.data.model.BookCategory
import com.nammapusthakaa.ui.common.AppTextField
import com.nammapusthakaa.ui.common.LoadingOverlay
import com.nammapusthakaa.ui.common.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    teacherViewModel: TeacherViewModel,
    bookId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val state = teacherViewModel.addBookState
    var categoryExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }
    val languages = listOf("Kannada", "English", "Hindi", "Sanskrit", "Urdu", "Other")
    val categories = BookCategory.entries.map { it.displayName }

    LaunchedEffect(bookId) {
        if (bookId != null && bookId > 0) {
            teacherViewModel.loadBookForEdit(bookId)
        } else {
            teacherViewModel.resetAddBookState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (state.isEditing) "Edit Book" else "Add Book") },
                    navigationIcon = {
                        IconButton(onClick = {
                            teacherViewModel.resetAddBookState()
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    value = state.title,
                    onValueChange = { teacherViewModel.updateAddBookField("title", it) },
                    label = "Book Title *",
                    leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) }
                )

                AppTextField(
                    value = state.author,
                    onValueChange = { teacherViewModel.updateAddBookField("author", it) },
                    label = "Author *",
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    teacherViewModel.updateAddBookField("category", category)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = languageExpanded,
                    onExpandedChange = { languageExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.language,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Language") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                        leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = languageExpanded,
                        onDismissRequest = { languageExpanded = false }
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang) },
                                onClick = {
                                    teacherViewModel.updateAddBookField("language", lang)
                                    languageExpanded = false
                                }
                            )
                        }
                    }
                }

                AppTextField(
                    value = state.copies,
                    onValueChange = { teacherViewModel.updateAddBookField("copies", it) },
                    label = "Copies"
                )

                AppTextField(
                    value = state.description,
                    onValueChange = { teacherViewModel.updateAddBookField("description", it) },
                    label = "Description",
                    singleLine = false,
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                )

                AppTextField(
                    value = state.kannadaSummary,
                    onValueChange = { teacherViewModel.updateAddBookField("kannadaSummary", it) },
                    label = "Kannada Summary",
                    singleLine = false,
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                )

                AppTextField(
                    value = state.coverUrl,
                    onValueChange = { teacherViewModel.updateAddBookField("coverUrl", it) },
                    label = "Cover Image URL (Optional)"
                )

                Spacer(modifier = Modifier.height(8.dp))

                PrimaryButton(
                    text = if (state.isEditing) "Update Book" else "Save Book",
                    onClick = { teacherViewModel.saveBook(bookId) },
                    isLoading = state.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = { teacherViewModel.resetAddBookState() },
                title = { Text("Error") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = { teacherViewModel.resetAddBookState() }) {
                        Text("OK")
                    }
                }
            )
        }

        if (state.success != null) {
            AlertDialog(
                onDismissRequest = {
                    teacherViewModel.resetAddBookState()
                    onNavigateBack()
                },
                title = { Text("Success") },
                text = { Text(state.success) },
                confirmButton = {
                    TextButton(onClick = {
                        teacherViewModel.resetAddBookState()
                        onNavigateBack()
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        LoadingOverlay(state.isSaving)
    }
}
