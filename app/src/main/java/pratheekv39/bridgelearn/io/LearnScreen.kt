package pratheekv39.bridgelearn.io

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun LearnScreen(
    navController: NavController,
    viewModel: LearnViewModel = viewModel()
) {
    val selectedSubject by viewModel.selectedSubject.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedSubject == null) {
            LearnDashboard(
                subjects = viewModel.subjects,
                onSubjectSelect = { viewModel.selectSubject(it) }
            )
        } else {
            SubjectLearningContent(
                subject = selectedSubject!!,
                onBackPress = { viewModel.clearSelectedSubject() },
                navController
            )
        }
    }
}

@Composable
fun LearnDashboard(
    subjects: List<LearnSubject>,
    onSubjectSelect: (LearnSubject) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Interactive Learning",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        items(subjects) { subject ->
            SubjectCard(
                subject = subject,
                onClick = { onSubjectSelect(subject) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectCard(
    subject: LearnSubject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon for the subject
            Icon(
                painter = painterResource(id = subject.drawableResId), // Pass the icon resource ID
                contentDescription = "${subject.name} Icon",
                tint = Color.White, // Optional: Set icon color
                modifier = Modifier
                    .size(48.dp) // Adjust size as needed
                    .padding(end = 16.dp) // Add spacing between the icon and text
            )

            Column(
                modifier = Modifier.weight(1f) // Ensures text takes the remaining space
            ) {
                Text(
                    text = subject.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subject.description,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = subject.learningContent.map { it.progress }.average().toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectLearningContent(
    subject: LearnSubject,
    onBackPress: () -> Unit,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(subject.name) },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(subject.learningContent) { content ->
                LearningContentCard(content,navController)
            }
        }
    }
}

@Composable
fun LearningContentCard(content: LearningContent, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (content.id == "1") {
                    navController.navigate("Interactive")
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on content type
            Icon(
                painter = painterResource(
                    id = when (content.type.name) {
                        "READING" -> R.drawable.book_open_text // Replace with your reading icon resource
                        "SIMULATION" -> R.drawable.waves // Replace with your simulation icon resource
                        "VIDEO" -> R.drawable.monitor_play // Replace with your video icon resource
                        else -> R.drawable.ic_launcher_foreground // Fallback icon if type doesn't match
                    }
                ),
                contentDescription = "${content.type.name} Icon",
                tint = Color.White, // Optional: Change the color of the icon
                modifier = Modifier
                    .size(48.dp) // Adjust size as needed
                    .padding(end = 16.dp) // Add spacing between the icon and text
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = content.type.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                LinearProgressIndicator(
                    progress = content.progress,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}