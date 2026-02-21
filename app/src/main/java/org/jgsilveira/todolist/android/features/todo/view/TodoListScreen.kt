package org.jgsilveira.todolist.android.features.todo.view

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jgsilveira.todolist.android.R
import org.jgsilveira.todolist.android.features.todo.presentation.TodoListItemViewData
import org.jgsilveira.todolist.android.ui.theme.OrangePrimary
import org.jgsilveira.todolist.android.ui.theme.TODOListAppTheme

@Composable
fun TodoListScreen(
    todoListItems: List<TodoListItemViewData>,
    modifier: Modifier = Modifier,
    onNewItemClick: () -> Unit = {},
    onCheckChange: (TodoListItemViewData, Boolean) -> Unit = { _, _ -> },
    onTitleChange: (TodoListItemViewData, String) -> Unit = { _, _ -> },
    onRemoveClick: (TodoListItemViewData) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_todo_list),
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewItemClick,
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_item)
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = todoListItems,
                key = { todoListItem -> todoListItem.id }
            ) { todoListItem ->
                TodoListItem(
                    text = todoListItem.text,
                    isCompleted = todoListItem.isDone,
                    deboundTimeMillis = 1000L,
                    modifier = Modifier.fillMaxWidth(),
                    onTextChange = { text ->
                        onTitleChange(todoListItem, text)
                    },
                    onCheckedChange = { isChecked ->
                        onCheckChange(todoListItem, isChecked)
                    },
                    onDeleteClick = { onRemoveClick(todoListItem) }
                )
            }
        }
    }
}

@Composable
fun TodoListItem(
    text: String,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    deboundTimeMillis: Long = 500L,
    onTextChange: (String) -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var textState by remember { mutableStateOf(text) }
    LaunchedEffect(textState) {
        if (textState != text) {
            delay(deboundTimeMillis)
            onTextChange(textState)
        }
    }
    Surface(
        modifier = modifier.clip(MaterialTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.wrapContentSize()
            )
            Spacer(modifier = Modifier.width(16.dp))
            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                singleLine = true,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove_item),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("TAREFAS", "AGENDA", "FOCO", "PERFIL")
    val icons = listOf(
        Icons.Filled.TaskAlt,
        Icons.Filled.CalendarToday,
        Icons.Filled.Timer,
        Icons.Default.AccountCircle
    )

    NavigationBar(
        containerColor = Color.Black
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = OrangePrimary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Black
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoListScreenPreview() {
    TODOListAppTheme(darkTheme = false) {
        val todoListItems = listOf(
            TodoListItemViewData(
                id = "1",
                text = "Comprar leite",
                isDone = false,
                createdAt = 0,
                updatedAt = 0,
                lastSyncAt = 0
            ),
            TodoListItemViewData(
                id = "2",
                text = "Comprar leite",
                isDone = true,
                createdAt = 0,
                updatedAt = 0,
                lastSyncAt = 0
            )
        )
        TodoListScreen(
            todoListItems = todoListItems
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TodoListScreenDarkPreview() {
    TODOListAppTheme(darkTheme = true) {
        val todoListItems = listOf(
            TodoListItemViewData(
                id = "1",
                text = "Comprar leite",
                isDone = false,
                createdAt = 0,
                updatedAt = 0,
                lastSyncAt = 0
            ),
            TodoListItemViewData(
                id = "2",
                text = "Comprar leite",
                isDone = true,
                createdAt = 0,
                updatedAt = 0,
                lastSyncAt = 0
            )
        )
        TodoListScreen(
            todoListItems = todoListItems
        )
    }
}
