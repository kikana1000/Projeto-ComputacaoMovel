package pt.ipp.estg.doctorbrain.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.models.Group
import pt.ipp.estg.doctorbrain.screens.app.AddGroupBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.groupsScreen.InfoGroupBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.groupsScreen.SearchViewGroups
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
 * Screen que demonstra todos os grupos existentes e que permite editar e visualizar
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun GroupsScreen() {
    val selectedGroup = remember {
        mutableStateOf(Group(""))
    }
    val bottomSheetScaffoldStateAdd = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val bottomSheetScaffoldStateInfo = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold( //bottomSheetAdd
        scaffoldState = bottomSheetScaffoldStateAdd,
        sheetContent = {
            AddGroupBottomSheetContent(
                coroutineScope = coroutineScope,
                bottomSheetScaffoldState = bottomSheetScaffoldStateAdd,
            )
        }, sheetPeekHeight = 0.dp
    ) {
        BottomSheetScaffold( //bottomSheetAdd
            scaffoldState = bottomSheetScaffoldStateInfo, sheetContent = {
                InfoGroupBottomSheetContent(
                    coroutineScope = coroutineScope,
                    bottomSheetScaffoldState = bottomSheetScaffoldStateInfo,
                    selectedGroup.value
                )
            }, sheetPeekHeight = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Groups", style = TitleofScreen())
                    IconButton(onClick = {
                        changeBottomSheetState(
                            coroutineScope,
                            bottomSheetScaffoldStateAdd
                        )
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Group")
                    }
                }
                SearchViewGroups {
                    selectedGroup.value = it
                    coroutineScope.launch {
                        bottomSheetScaffoldStateInfo.bottomSheetState.expand()
                    }
                }
            }
        }
    }
}
