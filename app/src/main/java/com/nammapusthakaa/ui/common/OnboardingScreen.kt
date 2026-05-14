package com.nammapusthakaa.ui.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class OnboardingContent(
    val title: String,
    val description: String
)

private val onboardingItems = listOf(
    OnboardingContent(
        title = "Welcome to Namma Pustaka",
        description = "Your digital library management system. Browse, borrow, and manage books effortlessly."
    ),
    OnboardingContent(
        title = "Track Your Reading",
        description = "Keep track of borrowed books, reading progress, and discover new books recommended just for you."
    ),
    OnboardingContent(
        title = "Smart Library Management",
        description = "Teachers can manage books with QR scanning, track overdue items, and view detailed analytics."
    )
)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingItems.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPage(
                item = onboardingItems[page],
                pageIndex = page
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingItems.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (pagerState.currentPage < onboardingItems.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onGetStarted()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (pagerState.currentPage < onboardingItems.size - 1) "Next" else "Get Started",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun OnboardingPage(item: OnboardingContent, pageIndex: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (pageIndex) {
            0 -> HappyStudentsGroup(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .size(260.dp)
            )
            1 -> ExcitedStudentFull(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .size(240.dp)
            )
            2 -> HappyReader(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .size(220.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
