package com.farestec.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.farestec.app.ui.theme.FaresTecTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

data class SubjectCard(
    val title: String,
    val subtitle: String,
    val progress: Int,
    val color: Color
)

data class LessonCard(
    val title: String,
    val level: String,
    val description: String
)

data class ProjectCard(
    val title: String,
    val type: String,
    val difficulty: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaresTecTheme {
                FaresTecApp()
            }
        }
    }
}

@Composable
fun FaresTecApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen() }
            composable("courses") { CoursesScreen() }
            composable("quiz") { QuizScreen() }
            composable("projects") { ProjectsScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

@Composable
fun AppBottomBar(navController: NavController) {
    val items = listOf(
        "home" to "Accueil",
        "courses" to "Cours",
        "quiz" to "Quiz",
        "projects" to "Projets",
        "profile" to "Profil"
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { (route, label) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                } },
                icon = {
                    val icon = when (route) {
                        "home" -> Icons.Default.Home
                        "courses" -> Icons.Default.Code
                        "quiz" -> Icons.Default.Quiz
                        "projects" -> Icons.Default.PlayArrow
                        else -> Icons.Default.AccountCircle
                    }
                    Icon(icon, contentDescription = label)
                },
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val featuredCourses = listOf(
        SubjectCard("Python", "Programmation", 80, Color(0xFF22C55E)),
        SubjectCard("JavaScript", "Web", 65, Color(0xFFF59E0B)),
        SubjectCard("Java", "Objet", 52, Color(0xFF3B82F6)),
        SubjectCard("SQL", "Base de données", 75, Color(0xFF8B5CF6))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bienvenue sur Fares Tec",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Apprends la programmation du collège à la terminale",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Objectif du jour", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Créer une page web simple avec HTML et CSS.")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { }) {
                    Text("Continuer")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Parcours", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(featuredCourses) { course ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(18.dp),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(course.color.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(course.title.take(2).uppercase())
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(course.title, fontWeight = FontWeight.Bold)
                            Text(course.subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }

                        Text("${course.progress}%")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    val courses = listOf(
        LessonCard("Variables et types", "Niveau 1", "Découvre les bases pour stocker des données."),
        LessonCard("Conditions", "Niveau 1", "Apprends à prendre des décisions dans un programme."),
        LessonCard("Boucles", "Niveau 2", "Répète des actions efficacement."),
        LessonCard("Fonctions", "Niveau 2", "Organise ton code avec des blocs réutilisables."),
        LessonCard("HTML & CSS", "Niveau 3", "Crée un site web simple et propre."),
        LessonCard("SQL", "Niveau 3", "Manipule des tables et des données."),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text("Cours") })
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(courses) { lesson ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(lesson.level, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(lesson.description, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizScreen() {
    val quizItems = listOf(
        "Les variables" to "15 min",
        "Les boucles" to "12 min",
        "JavaScript" to "20 min",
        "HTML/CSS" to "18 min"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Quiz de révision", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        quizItems.forEach { (title, time) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(title, fontWeight = FontWeight.Bold)
                        Text(time, color = MaterialTheme.colorScheme.primary)
                    }
                    Button(onClick = { }) {
                        Text("Démarrer")
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectsScreen() {
    val projects = listOf(
        ProjectCard("Calculatrice", "Application", "Facile"),
        ProjectCard("Portfolio web", "Site", "Moyen"),
        ProjectCard("Liste de tâches", "Logiciel", "Facile"),
        ProjectCard("Mini blog", "Web", "Moyen")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Projets pratiques", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        projects.forEach { project ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(project.title, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(project.type)
                    Text("Niveau: ${project.difficulty}")
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profil",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Fares", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Niveau : 2ème année secondaire", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Progression générale", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("72%")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cours complétés : 18 / 25")
                Text("Quiz réussis : 6 / 8")
            }
        }
    }
}

