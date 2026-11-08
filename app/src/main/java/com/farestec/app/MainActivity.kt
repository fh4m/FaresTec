package com.farestec.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farestec.app.ui.theme.FaresTecTheme

private data class Lesson(
    val id: String,
    val grade: String,
    val titleAr: String,
    val titleFr: String,
    val summaryAr: String,
    val summaryFr: String,
    val example: String,
    val questionAr: String,
    val questionFr: String,
    val options: List<Pair<String, String>>,
    val correctIndex: Int
)

private enum class AppTab { HOME, COURSES, QUIZ, PROFILE }

private val grades = listOf("2", "3", "4")

private val lessons = listOf(
    Lesson("algorithm", "2", "ما هي الخوارزمية؟", "Qu’est-ce qu’un algorithme ?", "الخوارزمية سلسلة مرتبة من التعليمات لحلّ مسألة. نبدأ بفهم المعطيات والنتيجة المطلوبة، ثم نكتب خطوات واضحة ومحدودة.", "Un algorithme est une suite ordonnée d’instructions qui résout un problème. On identifie les données et le résultat attendu, puis on décrit des étapes claires et finies.", "Début\n  Lire(a, b)\n  somme ← a + b\n  Écrire(somme)\nFin", "ما الخاصية الأساسية للخوارزمية؟", "Quelle est une propriété essentielle d’un algorithme ?", listOf("خطوات واضحة ومنتهية" to "Des étapes claires et finies", "تعليمات عشوائية" to "Des instructions au hasard", "يجب أن تحتوي على صور" to "Il doit contenir des images"), 0),
    Lesson("variables", "2", "المتغيّرات وأنواع البيانات", "Variables et types de données", "المتغيّر اسمٌ لمكان تخزين قيمة. نختار نوعًا مناسبًا مثل عدد صحيح، عدد عشري، أو نصّ، ونُسند إليه قيمة قبل استعمالها.", "Une variable nomme un espace de stockage. On choisit un type adapté (entier, réel ou chaîne) et on lui affecte une valeur avant de l’utiliser.", "age ← 16\nnom ← \"Amine\"\nÉcrire(age)", "ما نوع البيانات الأنسب لتخزين العمر كعدد كامل؟", "Quel type convient pour stocker un âge en nombre entier ?", listOf("Entier" to "Entier", "Chaîne" to "Chaîne", "Booléen" to "Booléen"), 0),
    Lesson("conditions", "3", "الشروط والاختيار", "Conditions et choix", "تسمح البنية الشرطية باختيار تعليمات حسب تحقق شرط. يمكن استعمال إذا/وإلا للتعامل مع الحالات المختلفة.", "La structure conditionnelle choisit les instructions à exécuter selon qu’une condition est vraie ou fausse. On utilise si/sinon pour traiter les cas.", "Si note >= 10 Alors\n  Écrire(\"Admis\")\nSinon\n  Écrire(\"À revoir\")\nFinSi", "متى تُنفّذ تعليمات Sinon؟", "Quand les instructions de Sinon sont-elles exécutées ?", listOf("عندما يكون الشرط غير محقق" to "Quand la condition est fausse", "دائمًا قبل Si" to "Toujours avant Si", "فقط عند تعريف متغيّر" to "Seulement à la déclaration d’une variable"), 0),
    Lesson("loops", "3", "الحلقات التكرارية", "Les boucles", "تكرّر الحلقة مجموعة تعليمات. اختر حلقة بعدد تكرارات معلوم عندما يكون ذلك مناسبًا، أو حلقة مرتبطة بشرط عند الحاجة.", "Une boucle répète un groupe d’instructions. Choisissez une boucle à nombre d’itérations connu lorsque c’est adapté, ou une boucle contrôlée par une condition.", "Pour i de 1 à 5 Faire\n  Écrire(i)\nFinPour", "كم مرة تُنفّذ الحلقة في المثال؟", "Combien de fois la boucle de l’exemple s’exécute-t-elle ?", listOf("5 مرات" to "5 fois", "4 مرات" to "4 fois", "إلى ما لا نهاية" to "À l’infini"), 0),
    Lesson("arrays", "4", "الجداول أحادية البعد", "Les tableaux à une dimension", "يجمع الجدول عدة قيم من النوع نفسه تحت اسم واحد. نصل ��لى كل عنصر باستعمال فهرس، مع احترام حدود الفهارس المحددة.", "Un tableau regroupe plusieurs valeurs de même type sous un seul nom. On accède à chaque élément avec un indice, en respectant les bornes définies.", "T[1] ← 12\nT[2] ← 7\nÉcrire(T[1])", "ما فائدة الجدول؟", "À quoi sert un tableau ?", listOf("تجميع قيم من النوع نفسه" to "Regrouper des valeurs de même type", "ترجمة البرنامج" to "Traduire le programme", "إنشاء اتصال بالإنترنت" to "Créer une connexion Internet"), 0),
    Lesson("procedures", "4", "الإجراءات والدوال", "Procédures et fonctions", "تقسم الإجراءات والدوال البرنامج إلى أجزاء قابلة لإعادة الاستعمال. تُرجع الدالة نتيجة، بينما ينفّذ الإجراء مهمة وقد لا يُرجع قيمة.", "Les procédures et fonctions divisent le programme en parties réutilisables. Une fonction renvoie un résultat, tandis qu’une procédure réalise une tâche et peut ne rien renvoyer.", "Fonction carré(x)\n  Retourner x * x\nFinFonction", "ما الذي يميّز الدالة عادةً؟", "Qu���est-ce qui caractérise généralement une fonction ?", listOf("ترجع نتيجة" to "Elle renvoie un résultat", "لا تستقبل أي تعليمات" to "Elle ne contient aucune instruction", "تُنفّذ مرة واحدة فقط" to "Elle s’exécute une seule fois"), 0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FaresTecTheme { FaresTecApp() } }
    }
}

@Composable
private fun FaresTecApp() {
    var isArabic by rememberSaveable { mutableStateOf(true) }
    var selectedGrade by rememberSaveable { mutableStateOf("2") }
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME) }
    var selectedLessonId by rememberSaveable { mutableStateOf<String?>(null) }
    var completedCount by rememberSaveable { mutableIntStateOf(0) }

    val selectedLesson = lessons.firstOrNull { it.id == selectedLessonId }
    val visibleLessons = lessons.filter { it.grade == selectedGrade }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AppTab.values().forEach { tab ->
                    TextButton(onClick = { selectedTab = tab; selectedLessonId = null }) {
                        Text(tab.label(isArabic), fontSize = 12.sp, fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (selectedLesson != null) {
                LessonDetailScreen(selectedLesson, isArabic, onBack = { selectedLessonId = null }, onComplete = {
                    completedCount += 1
                    selectedLessonId = null
                    selectedTab = AppTab.COURSES
                })
            } else {
                when (selectedTab) {
                    AppTab.HOME -> HomeScreen(isArabic, selectedGrade, completedCount,
                        onLanguage = { isArabic = !isArabic }, onGrade = { selectedGrade = it },
                        onContinue = { selectedTab = AppTab.COURSES })
                    AppTab.COURSES -> CoursesScreen(isArabic, visibleLessons, selectedGrade, onGrade = { selectedGrade = it }, onLesson = { selectedLessonId = it })
                    AppTab.QUIZ -> QuizScreen(isArabic, visibleLessons)
                    AppTab.PROFILE -> ProfileScreen(isArabic, selectedGrade, completedCount, onLanguage = { isArabic = !isArabic })
                }
            }
        }
    }
}

private fun AppTab.label(ar: Boolean): String = when (this) {
    AppTab.HOME -> if (ar) "الرئيسية" else "Accueil"
    AppTab.COURSES -> if (ar) "الدروس" else "Cours"
    AppTab.QUIZ -> if (ar) "اختبار" else "Quiz"
    AppTab.PROFILE -> if (ar) "حسابي" else "Profil"
}

@Composable
private fun HomeScreen(ar: Boolean, grade: String, completed: Int, onLanguage: () -> Unit, onGrade: (String) -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Fares Tec", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            OutlinedButton(onClick = onLanguage) { Text(if (ar) "Français" else "العربية") }
        }
        Spacer(Modifier.height(8.dp))
        Text(if (ar) "تعلّم البرمجة خطوة بخطوة" else "Apprends la programmation pas à pas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(if (ar) "مسار علوم الإعلامية • من الثانية ثانوي إلى الباكالوريا" else "Parcours sciences de l’informatique • de la 2e secondaire au bac", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        GradePicker(ar, grade, onGrade)
        Spacer(Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.padding(20.dp)) {
                Text(if (ar) "واصل التعلّم" else "Reprends ton apprentissage", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(if (ar) "ابدأ بالخوارزميات، ثم تدرب على المفاهيم الأساسية." else "Commence par les algorithmes, puis pratique les notions essentielles.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = onContinue) { Text(if (ar) "عرض الدروس" else "Voir les cours") }
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(if (ar) "تقدّمك" else "Ta progression", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(progress = { (completed.coerceAtMost(6) / 6f) }, modifier = Modifier.fillMaxWidth())
        Text(if (ar) "$completed من 6 دروس مكتملة" else "$completed cours sur 6 terminés", modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        Text(if (ar) "مسار تعليمي" else "Parcours pédagogique", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        listOf(if (ar) "الخوارزميات والمنطق" else "Algorithmique et logique", if (ar) "البرمجة وحلّ المسائل" else "Programmation et résolution", if (ar) "التطبيق والمراجعة" else "Pratique et révision").forEachIndexed { index, text ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) { Text("${index + 1}", Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.width(12.dp)); Text(text, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun GradePicker(ar: Boolean, selected: String, onSelect: (String) -> Unit) {
    Text(if (ar) "السنة الدراسية" else "Année scolaire", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        grades.forEach { grade ->
            val label = when (grade) { "2" -> if (ar) "2 ثانوي" else "2e secondaire"; "3" -> if (ar) "3 ثانوي" else "3e secondaire"; else -> if (ar) "4 ثانوي / باك" else "4e secondaire / bac" }
            if (selected == grade) Button(onClick = { onSelect(grade) }) { Text(label) }
            else OutlinedButton(onClick = { onSelect(grade) }) { Text(label) }
        }
    }
}

@Composable
private fun CoursesScreen(ar: Boolean, courseLessons: List<Lesson>, grade: String, onGrade: (String) -> Unit, onLesson: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(if (ar) "الدروس" else "Cours", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 14.dp, bottom = 10.dp))
        GradePicker(ar, grade, onGrade)
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(courseLessons) { lesson ->
                Card(onClick = { onLesson(lesson.id) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(if (ar) "درس تفاعلي • ${lesson.grade} ثانوي" else "Leçon interactive • ${lesson.grade}e secondaire", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.height(5.dp))
                        Text(if (ar) lesson.titleAr else lesson.titleFr, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(if (ar) lesson.summaryAr else lesson.summaryFr, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 5.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonDetailScreen(lesson: Lesson, ar: Boolean, onBack: () -> Unit, onComplete: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        TextButton(onClick = onBack) { Text(if (ar) "← العودة إلى الدروس" else "← Retour aux cours") }
        Text(if (ar) lesson.titleAr else lesson.titleFr, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(if (ar) lesson.summaryAr else lesson.summaryFr, style = MaterialTheme.typography.bodyLarge, lineHeight = 26.sp)
        Spacer(Modifier.height(16.dp))
        Text(if (ar) "مثال" else "Exemple", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Surface(color = MaterialTheme.colorScheme.inverseSurface, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text(lesson.example, color = MaterialTheme.colorScheme.inverseOnSurface, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, modifier = Modifier.padding(16.dp), lineHeight = 22.sp)
        }
        Spacer(Modifier.height(18.dp))
        Text(if (ar) "تحقّق من فهمك" else "Vérifie ta compréhension", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(if (ar) lesson.questionAr else lesson.questionFr, modifier = Modifier.padding(top = 6.dp, bottom = 10.dp))
        lesson.options.forEachIndexed { index, option ->
            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                Text(if (ar) option.first else option.second, textAlign = TextAlign.Center)
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onComplete, modifier = Modifier.fillMaxWidth()) { Text(if (ar) "إكمال الدرس" else "Terminer la leçon") }
        Text(if (ar) "ملاحظة: هذه نسخة أولية؛ تتم مراجعة المحتوى ومطابقته للبرنامج الرسمي قبل النشر." else "Note : cette version initiale doit être vérifiée et alignée sur le programme officiel avant publication.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun QuizScreen(ar: Boolean, courseLessons: List<Lesson>) {
    var questionIndex by rememberSaveable(courseLessons.firstOrNull()?.id) { mutableIntStateOf(0) }
    var selectedAnswer by rememberSaveable(courseLessons.firstOrNull()?.id, questionIndex) { mutableIntStateOf(-1) }
    val lesson = courseLessons.getOrNull(questionIndex)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text(if (ar) "اختبر معلوماتك" else "Teste tes connaissances", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (lesson == null) {
            Text(if (ar) "لا توجد أسئلة لهذا المستوى بعد." else "Pas encore de questions pour ce niveau.", modifier = Modifier.padding(top = 16.dp))
        } else {
            Text(if (ar) "السؤال ${questionIndex + 1} من ${courseLessons.size}" else "Question ${questionIndex + 1} sur ${courseLessons.size}", modifier = Modifier.padding(top = 12.dp), color = MaterialTheme.colorScheme.primary)
            Text(if (ar) lesson.questionAr else lesson.questionFr, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 14.dp))
            lesson.options.forEachIndexed { index, option ->
                OutlinedButton(onClick = { selectedAnswer = index }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text((if (ar) option.first else option.second) + if (selectedAnswer == index) " ✓" else "", textAlign = TextAlign.Center)
                }
            }
            if (selectedAnswer >= 0) {
                val correct = selectedAnswer == lesson.correctIndex
                Text(if (correct) { if (ar) "إجابة صحيحة، أحسنت!" else "Bonne réponse, bravo !" } else { if (ar) "ليست الإجابة الصحيحة، حاول مراجعة الدرس." else "Ce n’est pas la bonne réponse. Revois la leçon." }, color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold)
                Button(onClick = { questionIndex = (questionIndex + 1) % courseLessons.size; selectedAnswer = -1 }, modifier = Modifier.fillMaxWidth()) { Text(if (ar) "السؤال التالي" else "Question suivante") }
            }
        }
    }
}

@Composable
private fun ProfileScreen(ar: Boolean, grade: String, completed: Int, onLanguage: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) { Text("FT", Modifier.padding(horizontal = 26.dp, vertical = 20.dp), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) }
        Text("Fares Tec", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
        Text(if (ar) "مسار علوم الإعلامية" else "Parcours sciences de l’informatique", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text(if (ar) "ملخص التعلّم" else "Résumé d’apprentissage", fontWeight = FontWeight.Bold)
                Text(if (ar) "المستوى: ${grade} ثانوي" else "Niveau : ${grade}e secondaire", modifier = Modifier.padding(top = 10.dp))
                Text(if (ar) "الدروس المكتملة: $completed" else "Leçons terminées : $completed", modifier = Modifier.padding(top = 6.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onLanguage) { Text(if (ar) "Changer de langue · Français" else "Changer de langue · العربية") }
    }
}
